package io.github.cottonmc.um.block.entity;

import alexiil.mc.lib.attributes.AttributeList;
import io.github.cottonmc.energy.api.DefaultEnergyTypes;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;

import io.github.cottonmc.gui.PropertyDelegateHolder;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.block.container.CoalGeneratorController;
import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.cottonmc.um.component.wrapper.EnergyView;
import io.github.cottonmc.um.component.wrapper.SidedItemView;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.TextComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

public class CoalGeneratorEntity extends BlockEntity implements InventoryProvider, NameableContainerProvider, PropertyDelegateHolder {
	public static int MAX_WU = 10;
	public static int FUEL_PER_WU = 5; //Fractional fuel will get banked; generators are lossless.
	public static int PULSE_LENGTH = 30; //Might shorten to 20, we'll see how it feels in-game.
	
	public static final int SLOT_FUEL = 0;
	
	//private ContainerLock lock; //TODO: Awaiting design direction, to be decided alongside Locky
	private int remainingTicks;
	private int totalTicks;
	//private int wuBuffer;
	private SimpleItemComponent items = new SimpleItemComponent(1);
	private SimpleEnergyAttribute energy = new SimpleEnergyAttribute(MAX_WU);
	
	public CoalGeneratorEntity() {
		super(UMBlocks.COAL_GENERATOR_ENTITY);
		
		items.listen(this::markDirty);
		energy.listen(this::markDirty);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);
		
		result.put("Items", items.toTag());
		result.put("Energy", energy.toTag());
		
		result.putInt("BurnTime", remainingTicks);
		result.putInt("BurnTimeTotal", totalTicks);
		
		return result;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		
		if (tag.containsKey("Items")) items.fromTag(tag.getTag("Items"));
		if (tag.containsKey("Energy")) energy.fromTag(tag.getTag("Energy"));
		
		remainingTicks = tag.getInt("BurnTime");
		totalTicks = tag.getInt("BurnTimeTotal");
	}
	
	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.getBlockTickScheduler().isScheduled(pos, UMBlocks.COAL_GENERATOR)) {
			if (needsPulse()) {
				System.out.println("Scheduling next pulse");
			} else {
				System.out.println("Sleeping till a pulse is needed.");
			}
			
			//Check if we need to start pulsing again
			if (needsPulse()) world.getBlockTickScheduler().schedule(pos, UMBlocks.COAL_GENERATOR, PULSE_LENGTH);
		}
	}
	
	public void pulse() {
		System.out.println("Pulse fuel:"+remainingTicks+"/"+totalTicks+" wu:"+energy.getCurrentEnergy()+"/"+energy.getMaxEnergy());
		
		
		//[continue to] flush any stored energy
		if (energy.getCurrentEnergy()>0) {
			pushEnergy();
		}
		
		//[continue to] spend stored fuel-ticks to create energy
		burn:
		if (remainingTicks>0) {
			for(int i=0; i<5; i++) {
				if (energy.getCurrentEnergy()<MAX_WU && remainingTicks>=FUEL_PER_WU) {
					remainingTicks -= FUEL_PER_WU;
					energy.setCurrentEnergy(energy.getCurrentEnergy()+1); //IF THIS WORKS SIMPLENERGYCOMPONENT IS BROKEN'D
					//energy.insertEnergy(DefaultEnergyTypes.LOW_VOLTAGE, 1, ActionType.PERFORM);
					markDirty();
				} else break burn;
			}
		}
		
		//Consume fuel to recharge fuel-ticks
		if (remainingTicks<FUEL_PER_WU) {
			//TODO: Consume fuel
			ItemStack fuelStack = items.get(0);
			if (!fuelStack.isEmpty()) {
				int burnTime = FurnaceBlockEntity.createFuelTimeMap().getOrDefault(fuelStack.getItem(), 0);
				if (burnTime>0) {
					remainingTicks += burnTime;
					totalTicks = remainingTicks;
					
					fuelStack.subtractAmount(1);
					items.setInvStack(0, (fuelStack.isEmpty()) ? ItemStack.EMPTY : fuelStack); //Stuff it back in to trigger a markDirty
				}
			}
		}
		
		/* Decide if we should schedule a followup pulse. We should followup if:
		 * - If we have any energy left, so we can keep pushing it out
		 * - If we have fuel-ticks left and our energy's not full, so we can refill it
		 * 
		 * We don't have to check if we have fuel left in the fuel slot and our burnTime is less than fuelPerWU, because
		 * that's the very last thing that happens in a tick - it's always tidy in between ticks.
		 */
		if (needsPulse()) {
			world.getBlockTickScheduler().schedule(pos, UMBlocks.COAL_GENERATOR, PULSE_LENGTH);
		}
	}
	
	/**
	 * @param facing which direction to push current in
	 * @return how much energy was successfully pushed
	 */
	private int pushEnergyTo(Direction facing) {
		if (energy.getCurrentEnergy()==0) return 0;
		
		AttributeList<EnergyAttribute> attributes = EnergyAttribute.ENERGY_ATTRIBUTE.getAll(world, pos.offset(facing));
		for(int i=0; i<attributes.getCount(); i++) {
			EnergyAttribute attribute = attributes.get(i);
			if (!attribute.canInsertEnergy()) continue;
			if ( energy.getPreferredType().isCompatibleWith(attribute.getPreferredType()) || attribute.getPreferredType().isCompatibleWith(energy.getPreferredType()) ) {
				//One end or the other seems to think that the energy is compatible.
				int transferSize = Math.min(energy.getPreferredType().getMaximumTransferSize(), energy.getCurrentEnergy());
				
				transferSize = transferSize - attribute.insertEnergy(energy.getPreferredType(), transferSize, ActionType.SIMULATE);
				transferSize = energy.extractEnergy(energy.getPreferredType(), transferSize, ActionType.PERFORM); //Transfer actually starts here
				int notTransferred = attribute.insertEnergy(energy.getPreferredType(), transferSize, ActionType.PERFORM);
				if (notTransferred>0) {
					//Complain loudly but keep working
					new RuntimeException("Misbehaving EnergyAttribute "+attribute.getClass().getCanonicalName()+" accepted energy in SIMULATE then didn't accept the same or less energy in PERFORM").printStackTrace();
					energy.insertEnergy(energy.getPreferredType(), notTransferred, ActionType.PERFORM);
				}
				return transferSize - notTransferred;
			}
			
		}
		return 0;
	}
	
	public void pushEnergy() {
		for(Direction d : Direction.values()) {
			pushEnergyTo(d);
		}
	}
	
	public boolean needsPulse() {
		//We need to keep pushing the energy we've got out to consumers
		if (energy.getCurrentEnergy()>0) return true;
		
		if (energy.getCurrentEnergy()<MAX_WU) {
			//We might be able to put more energy in the buffer
			if (remainingTicks>FUEL_PER_WU) return true;
			if (!items.isEmpty()) return true;
		}
		
		return false;
	}
	
	@Override
	public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
		return new SidedItemView(items);
	}
	
	@Override
	public Container createMenu(int var1, PlayerInventory var2, PlayerEntity var3) {
		return new CoalGeneratorController(var1, var2, BlockContext.create(this.world, this.pos));
	}

	@Override
	public TextComponent getDisplayName() {
		return items.getDisplayName();
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return new PropertyDelegate() {
			@Override
			public int get(int index) {
				switch(index) {
				case 0: //return 10;
					System.out.println("Getting remainingTicks=="+remainingTicks);
					return CoalGeneratorEntity.this.remainingTicks;
				case 1: //return 20;
					return CoalGeneratorEntity.this.totalTicks;
				case 2: //return 10;
					return CoalGeneratorEntity.this.energy.getCurrentEnergy();
				case 3: //return 20;
					return CoalGeneratorEntity.this.energy.getMaxEnergy();
				default: return 0;
				}
			}

			@Override
			public void set(int index, int value) {
				switch(index) {
				case 0:
					System.out.println("Setting remainingTicks="+value);
					CoalGeneratorEntity.this.remainingTicks = value; break;
				case 1: CoalGeneratorEntity.this.totalTicks = value; break;
				case 2: CoalGeneratorEntity.this.energy.setCurrentEnergy(value); //TODO: Probably scale this number so it'll always fit in a short!
					break;
				case 3: CoalGeneratorEntity.this.energy.setMaxEnergy(value); //TODO: '   '
					break;
				default: break;
				}
			}

			@Override
			public int size() {
				return 2;
			}
			
		};
	}

	public EnergyAttribute getEnergy() {
		return EnergyView.extractOnly(energy);
	}
	
	/*
	
	//implements SidedComponentContainer {
		@Override
		public <T extends Component> boolean registerExtraComponent(Direction side, Class<T> componentClass, String key, T component) {
			return false;
		}
	
		@Override
		public <T extends Component> boolean registerExtraComponent(Class<T> componentClass, String key, T component) {
			return false;
		}
	
		@SuppressWarnings("unchecked")
		@Override
		@Nullable
		public <T extends Component> T getComponent(Direction side, Class<T> componentClass, String key) {
			if (componentClass==EnergyComponent.class) {
				return (T)energy; //TODO: Wrap it
			}
			return null;
		}
	
		@Override
		@Nonnull
		public Set<String> getComponentKeys(Direction side, Class<? extends Component> componentClass) {
			if (componentClass==EnergyComponent.class) {
				return Sets.newHashSet("cotton:low_voltage");
			} else {
				return Sets.newHashSet();
			}
		}
	//} */
}
