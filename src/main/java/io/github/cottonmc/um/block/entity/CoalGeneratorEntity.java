package io.github.cottonmc.um.block.entity;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import io.github.cottonmc.ecs.api.Component;
import io.github.cottonmc.ecs.api.BlockComponentContainer;
import io.github.cottonmc.energy.api.EnergyComponent;
import io.github.cottonmc.energy.impl.SimpleEnergyComponent;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.cottonmc.um.component.wrapper.SidedItemView;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

public class CoalGeneratorEntity extends BlockEntity implements InventoryProvider {
	public static int MAX_WU = 4;
	public static int FUEL_PER_WU = 5; //Fractional fuel will get banked; generators are lossless.
	public static int PULSE_LENGTH = 30; //Might shorten to 20, we'll see how it feels in-game.
	
	//private ContainerLock lock; //TODO: Awaiting design direction, to be decided alongside Locky
	private int remainingTicks;
	private int totalTicks;
	//private int wuBuffer;
	private SimpleItemComponent items = new SimpleItemComponent(1);
	private SimpleEnergyComponent energy = new SimpleEnergyComponent(MAX_WU);
	
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
			//Check if we need to start pulsing again
			if (needsPulse()) world.getBlockTickScheduler().schedule(pos, UMBlocks.COAL_GENERATOR, PULSE_LENGTH);
		}
	}
	
	public void pulse() {
		
		//[continue to] flush any stored energy
		if (energy.getCurrentEnergy()>0) {
			pushEnergy();
		}
		
		//[continue to] spend stored fuel-ticks to create energy
		burn:
		for(int i=0; i<5; i++) {
			if (energy.getCurrentEnergy()<MAX_WU && remainingTicks>FUEL_PER_WU) {
				remainingTicks -= FUEL_PER_WU;
				energy.insertEnergy(1, ActionType.PERFORM);
				markDirty();
			} else break burn;
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
	
	public void pushEnergy() {
		for(Direction d : Direction.values()) {
			/*
			BlockState state = world.getBlockState(pos.offset(d));
			//TODO: Add "world, pos" arguments to SidedComponentContainer so this can work
			if (!state.isAir() && (state.getBlock() instanceof SidedComponentContainer)) {
				((SidedComponentContainer)state.getBlock()).getComponent(d.getOpposite(), EnergyComponent.class, "low_voltage");
			}*/
			//TODO: Fix up for LBA
			/*
			BlockEntity be = world.getBlockEntity(pos.offset(d));
			if (be!=null && be instanceof BlockComponentContainer) {
				//This is a valid target
				
				EnergyComponent target = ((BlockComponentContainer)be).getComponent(d.getOpposite(), EnergyComponent.class, "cotton:low_voltage");
				if (target==null || !target.canInsertEnergy()) continue;
				int toInsert = Math.min(4, energy.getCurrentEnergy());
				toInsert = energy.extractEnergy(toInsert, ActionType.PERFORM);
				int leftover = target.insertEnergy(4, ActionType.PERFORM);
				if (leftover>0) energy.insertEnergy(leftover, ActionType.PERFORM);
				markDirty();
			}*/
		}
	}
	
	public boolean needsPulse() {
		return
			energy.getCurrentEnergy()>0 ||
			(energy.getCurrentEnergy()<MAX_WU && remainingTicks>FUEL_PER_WU);
	}
	
	@Override
	public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
		return new SidedItemView(items);
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
