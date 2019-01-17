package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.energy.CottonEnergy;
import io.github.cottonmc.energy.api.ActionType;
import io.github.cottonmc.energy.api.DefaultEnergyTypes;
import io.github.cottonmc.energy.api.EnergyComponent;
import io.github.cottonmc.energy.api.SidedEnergyComponentHolder;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.InventoryDelegate;
import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.cottonmc.um.component.wrapper.SidedItemView;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.LockContainer;
import net.minecraft.inventory.Inventory;
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
	
	private LockContainer lock; //TODO: Awaiting design direction, to be decided alongside Locky
	private int remainingTicks;
	private int totalTicks;
	private int wuBuffer;
	private SimpleItemComponent itemStorage = new SimpleItemComponent(1);
	//private EnergyHandler energyStorage = new EnergyHandler(MAX_WU);
	
	public CoalGeneratorEntity() {
		super(UMBlocks.COAL_GENERATOR_ENTITY);
		itemStorage.addObserver(this::markDirty); //TODO: on markDirty, check if we need to schedule a tick!
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);
		
		result.put("Items", itemStorage.toTag());
		
		//result.put("Energy", energyStorage.toTag());
		CompoundTag energyTag = new CompoundTag();
		energyTag.putInt("Stored", wuBuffer);
		energyTag.putInt("Limit", MAX_WU);
		energyTag.putString("Type", CottonEnergy.ENERGY_REGISTRY.getId(DefaultEnergyTypes.LOW_VOLTAGE).toString());
		result.put("Energy", energyTag);
		
		result.putInt("BurnTime", remainingTicks);
		result.putInt("BurnTimeTotal", totalTicks);
		
		return result;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		
		if (tag.containsKey("Items", 9)) {
			itemStorage.fromTag(tag.getList("Items", 10)); //10 is CompoundTag
		}
		
		if (tag.containsKey("Energy", 10)) {
			CompoundTag energyTag = tag.getCompound("Energy");
			wuBuffer = energyTag.getInt("Stored");
			//Ignore limit and type
		}
		
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
		if (wuBuffer>0) {
			pushEnergy();
		}
		
		//[continue to] spend stored fuel-ticks to create energy
		burn:
		for(int i=0; i<5; i++) {
			if (wuBuffer<MAX_WU && remainingTicks>FUEL_PER_WU) {
				remainingTicks -= FUEL_PER_WU;
				wuBuffer++;
				markDirty();
			} else break burn;
		}
		
		//Consume fuel to recharge fuel-ticks
		if (remainingTicks<FUEL_PER_WU) {
			//TODO: Consume fuel
			ItemStack fuelStack = itemStorage.get(0);
			if (!fuelStack.isEmpty()) {
				int burnTime = FurnaceBlockEntity.createBurnableMap().getOrDefault(fuelStack.getItem(), 0);
				if (burnTime>0) {
					remainingTicks += burnTime;
					totalTicks = remainingTicks;
					
					fuelStack.subtractAmount(1);
					itemStorage.setInvStack(0, (fuelStack.isEmpty()) ? ItemStack.EMPTY : fuelStack); //Stuff it back in to trigger a markDirty
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
			BlockEntity be = world.getBlockEntity(pos.offset(d));
			if (be!=null && be instanceof SidedEnergyComponentHolder) {
				//This is a valid target
				//TODO: Use ECS once we have a solid call on whether a BlockEntity is a SidedComponentHolder or the holder of a SidedComponentMap; a component-component if you will
				
				EnergyComponent target = ((SidedEnergyComponentHolder)be).getEnergyComponent(d.getOpposite(), DefaultEnergyTypes.LOW_VOLTAGE);
				if (!target.canInsertEnergy()) continue;
				int toInsert = Math.min(4, wuBuffer);
				wuBuffer -= toInsert;
				wuBuffer += target.insertEnergy(4, ActionType.PERFORM);
				markDirty();
			}
		}
	}
	
	public boolean needsPulse() {
		return
			wuBuffer>0 ||
			(wuBuffer<MAX_WU && remainingTicks>FUEL_PER_WU);
	}
	
	@Override
	public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
		return new SidedItemView(itemStorage);
	}
	
}
