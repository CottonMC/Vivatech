package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.um.block.ConveyorBlock;
import io.github.cottonmc.um.block.MachineStatus;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.cottonmc.um.component.wrapper.SidedItemView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class ConveyorEntity extends BlockEntity {

	public ConveyorEntity() {
		super(UMBlocks.CONVEYOR_ENTITY);
		items.addObserver(this::markDirty);
	}

	//for now, a line of conveyors can only carry one stack at a time
	private SimpleItemComponent items = new SimpleItemComponent(1);

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);

		result.put("Items", items.toTag());

		return result;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);

		items.fromTag(tag.getTag("Items"));

	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.getBlockTickScheduler().isScheduled(pos, UMBlocks.CONVEYOR)) {
			//Check if we need to start pulsing again
			if (needsPulse()) world.getBlockTickScheduler().schedule(pos, UMBlocks.CONVEYOR, 1);
		}
	}

	public void pulse() {
		if (world==null) return;
		BlockPos dropoff = findDropoff();
		 if (world.getBlockState(pos).get(ConveyorBlock.STATUS)== MachineStatus.INACTIVE) {
			 double diff = pos.distanceTo(dropoff);
			 world.setBlockState(pos, world.getBlockState(pos).with(ConveyorBlock.STATUS, MachineStatus.ACTIVE));
			 world.getBlockTickScheduler().schedule(pos, UMBlocks.CONVEYOR, (int)diff);
		 } else {
		 	BlockState state = world.getBlockState(dropoff);
		 	Block block = state.getBlock();
		 	Direction insertSide = world.getBlockState(pos).get(ConveyorBlock.FACING).getOpposite();
		 	if (block instanceof InventoryProvider) {
		 		SidedInventory inv = ((InventoryProvider) block).getInventory(state, world, dropoff);
			 	int insertSlot = getAvailableSlot(inv, (inv).getInvAvailableSlots(insertSide));
			 	if (insertSlot >= 0) {
			 		inv.setInvStack(insertSlot, items.getInvStack(0));
			 		items.removeInvStack(0);
				}
		 	} else if (state.isAir()) {
				ItemEntity item = new ItemEntity(world, dropoff.getX(), dropoff.getY(), dropoff.getZ(), items.getInvStack(0));
				items.removeInvStack(0);
				world.spawnEntity(item);
			}
		 	if (!needsPulse()) world.setBlockState(pos, world.getBlockState(pos).with(ConveyorBlock.STATUS, MachineStatus.INACTIVE));
		 }

	}

	public boolean needsPulse() {
		return !items.isEmpty(); // only need to tick while we have items
	}

	//@Override
	public SidedInventory getInventory() {
		return new SidedItemView(items);
	}

	public BlockPos findDropoff() {
		Direction dir = world.getBlockState(pos).get(ConveyorBlock.FACING);
		BlockPos checkPos = pos;
		boolean foundDropoff = false;
		while (!foundDropoff) {
			checkPos = checkPos.offset(dir);
			if (world.getBlockState(checkPos).getBlock() == UMBlocks.CONVEYOR) {
				foundDropoff = world.getBlockState(checkPos).get(ConveyorBlock.FACING).equals(dir);
			} else foundDropoff = true;
		}
		return checkPos;
	}

	public int getAvailableSlot(Inventory inv, int[] slots) {
		for (int i : slots) {
			if (inv.getInvStack(i).isEmpty()) return i;
		}
		return -1;
	}
}
