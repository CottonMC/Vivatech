package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.um.block.ConveyorBlock;
import io.github.cottonmc.um.block.MachineStatus;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.cottonmc.um.component.wrapper.SidedItemView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class ConveyorEntity extends BlockEntity implements Tickable {

	//for now, a line of conveyors can only carry one stack at a time
	private SimpleItemComponent items = new SimpleItemComponent(1);
	int targetDistance = 0;
	int travel = 0;

	public ConveyorEntity() {
		super(UMBlocks.CONVEYOR_ENTITY);
		items.addObserver(this::markDirty);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);

		result.put("Items", items.toTag());
		result.putInt("Target", targetDistance);
		result.putInt("Travel", travel);

		return result;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);

		items.fromTag(tag.getTag("Items"));
		targetDistance = tag.getInt("Target");
		travel = tag.getInt("Travel");

	}

	public void tick() {
		if (world==null || items.isEmpty() || world.isClient) return;
		BlockPos dropoff = findDropoff();
		double diff = pos.distanceTo(dropoff);
		targetDistance = (int)(10*diff);
		if (travel < targetDistance) {
			travel++;
		} else {
		 	BlockState state = world.getBlockState(dropoff);
		 	Block block = state.getBlock();
		 	Direction insertSide = world.getBlockState(pos).get(ConveyorBlock.FACING).getOpposite();
		 	if (block instanceof InventoryProvider) {
		 		SidedInventory inv = ((InventoryProvider) block).getInventory(state, world, dropoff);
		 		if (inv != null) {
					int insertSlot = getAvailableSlot(inv, (inv).getInvAvailableSlots(insertSide));
					if (insertSlot >= 0) {
						inv.setInvStack(insertSlot, items.getInvStack(0));
						inv.markDirty();
						items.removeInvStack(0);
						this.markDirty();
					}
				}
		 	} else if (world.getBlockEntity(dropoff) instanceof Inventory) {
		 		Inventory inv = (Inventory)world.getBlockEntity(dropoff);
				//if (inv instanceof ChestBlockEntity && block instanceof ChestBlock) {
				//	inv = ChestBlock.method_17458(state, world, dropoff, true);
				//}
				if (inv != null) {
					int insertSlot = getAvailableSlot(inv);
					if (insertSlot >= 0) {
						inv.setInvStack(insertSlot, items.getInvStack(0).copy());
						inv.markDirty();
						items.setInvStack(0, ItemStack.EMPTY);
						this.markDirty();
					}
				}
			} else if (!state.isFullBoundsCubeForCulling()) {
				ItemEntity item = new ItemEntity(world, dropoff.getX()+0.5, dropoff.getY()+0.5, dropoff.getZ()+0.5, items.getInvStack(0).copy());
				items.setInvStack(0, ItemStack.EMPTY);
				this.markDirty();
				world.spawnEntity(item);
		 	}
		 	travel = 0;
		}
	}

	//@Override
	public SidedInventory getInventory() {
		return new SidedItemView(items);
	}

	public BlockPos findDropoff() {
		Direction dir = world.getBlockState(pos).get(ConveyorBlock.FACING);
		BlockPos checkPos = pos;
		for (int i = 0; i < 64; i++) {
			checkPos = checkPos.offset(dir);
			if (world.getBlockState(checkPos).getBlock() == UMBlocks.CONVEYOR) {
				if (!world.getBlockState(checkPos).get(ConveyorBlock.FACING).equals(dir)) return checkPos;
			} else return checkPos;
		}
		return checkPos;
	}

	public int getAvailableSlot(Inventory inv, int[] slots) {
		for (int i : slots) {
			if (inv.getInvStack(i).isEmpty()) return i;
		}
		return -1;
	}

	public int getAvailableSlot(Inventory inv) {
		for (int i = 0; i < inv.getInvSize(); i++) {
			if (inv.getInvStack(i).isEmpty()) return i;
		}
		return -1;
	}
}
