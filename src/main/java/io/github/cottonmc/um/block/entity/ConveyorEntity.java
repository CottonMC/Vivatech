package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.um.block.ConveyorBlock;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.cottonmc.um.component.wrapper.SidedItemView;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;


public class ConveyorEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {

	//for now, a line of conveyors can only carry one stack at a time
	private ItemStack lastUpdated = ItemStack.EMPTY;
	private SimpleItemComponent items = new SimpleItemComponent(1);
	private int MAX_TIME = 10; // 2 m/s
	private int time = MAX_TIME;

	public ConveyorEntity() {
		super(UMBlocks.CONVEYOR_ENTITY);
		items.listen(this::markDirty);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);

		result.put("Items", items.toTag());
		result.putInt("Time", time);
		return result;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);

		items.fromTag(tag.getTag("Items"));
		//lastUpdated = items.get(0).isEmpty() ? ItemStack.EMPTY : items.get(0).copy();
		time = tag.getInt("Time");
	}
	
	@Override
	public void fromClientTag(CompoundTag tag) {
		//System.out.println("From Client tag!");
		//super.fromTag(tag);
		items.fromTag(tag.getTag("Items"));
		//lastUpdated = items.get(0).isEmpty() ? ItemStack.EMPTY : items.get(0).copy();
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		//System.out.println("To Client tag!");
		CompoundTag result = super.toTag(tag);
		result.put("Items", items.toTag());
		return result;
	}
	
	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(pos, 127, toClientTag(new CompoundTag())); //127 seems to be a magic number for automatic BlockEntityClientSerializable unpacking
	}

	public void tick() {
		if (world==null || items.isEmpty() || world.isClient) return;
		
		ItemStack toInsert = items.getInvStack(0).copy();
		if (!toInsert.isEmpty()) {
			if (time>0) {
				time--;
				markDirty();
			}
			
			if (time<=0) {
				time = MAX_TIME;
				Direction dir = world.getBlockState(pos).get(ConveyorBlock.FACING);
				BlockPos dropoff = pos.offset(dir);
				
				BlockState state = world.getBlockState(dropoff);
				Block block = state.getBlock();
				
				
				
				//Best-case scenario
				/*
				//However, due to reported bugs in LBA inventories, we'll be testing the fallback cases for now.
				ItemInsertable insertable = ItemAttributes.INSERTABLE.getFirstOrNull(world, dropoff, SearchOptionDirectional.of(dir));
				if (insertable!=null) {
					
					ItemStack leftover = insertable.attemptInsertion(toInsert, Simulation.ACTION);
					if (leftover.getAmount()!=toInsert.getAmount()) {
						items.setInvStack(0, leftover.isEmpty() ? ItemStack.EMPTY : leftover); //intern EMPTY
						this.markDirty();
					}
					
					return;
				}*/
				/*
				Direction insertSide = dir.getOpposite();
				if (block instanceof InventoryProvider) {
					SidedInventory inv = ((InventoryProvider) block).getInventory(state, world, dropoff);
					if (inv != null) {
						for(int insertSlot : inv.getInvAvailableSlots(insertSide)) {
							if (!inv.canInsertInvStack(insertSlot, toInsert, insertSide)) continue;
							
							toInsert = pushTo(toInsert, inv, insertSlot);
							if (toInsert.isEmpty()) break;
						}
						if (toInsert.getAmount()!=items.get(0).getAmount()) {
							items.setInvStack(0, toInsert);
							markDirty();
						}
					}
				} else*/ if (world.getBlockEntity(dropoff) instanceof Inventory) {
					Inventory inv = (Inventory)world.getBlockEntity(dropoff);
					for(int insertSlot=0; insertSlot<inv.getInvSize(); insertSlot++) {
						if (!inv.isValidInvStack(insertSlot, toInsert)) continue;
						toInsert = pushTo(toInsert, inv, insertSlot);
						if (toInsert.isEmpty()) break;
					}
					if (toInsert.getAmount()!=items.get(0).getAmount()) {
						items.setInvStack(0, toInsert);
						markDirty();
					}
				}
			}
		}
			
		if (items.get(0).getItem()!=lastUpdated.getItem()) {
			updateListeners();
			lastUpdated = items.get(0).isEmpty() ? ItemStack.EMPTY : items.get(0).copy();
			//Packet updatePacket = toUpdatePacket();
			//if (world instanceof ServerWorld) {
				//((ServerWorld)world).se
			//}
		}
	}
	
	private void updateListeners() {
		this.markDirty();
		this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), 3);
	}
	
	/** Even more thorough than the logic used by Containers and their Slots*/
	public static boolean canStacksCombine(ItemStack a, ItemStack b) {
		return a.canStack() && b.canStack() && a.getItem() == b.getItem() && ItemStack.areTagsEqual(a, b);
	}
	
	/**
	 * Combines the two ItemStacks into dest, and returns any leftover items. Modifies dest.
	 * <li>Does not check whether the stacks are valid to combine! Call canStacksCombine first!
	 * <li>Many ItemStack-providing APIs have contracts on whether or not they can be modified. Don't use this method on stacks you're not allowed to modify.
	 * <li>Destination inventory potentially needs to be marked dirty after this operation! Again, respect contracts about the provided ItemStacks and modifications to them.
	 * <li>Do not call with dest.isEmpty()==true! You can't modify ItemStack.EMPTY!
	 */
	public ItemStack combine(ItemStack source, ItemStack dest) {
		if (dest.isEmpty()) return source;
		
		if (dest.getAmount()>=dest.getMaxAmount()) return source;
		int combination = source.getAmount() + dest.getAmount();
		int destAmount = Math.min(combination, dest.getMaxAmount());
		int resultAmount = combination - destAmount;
		if (destAmount>dest.getAmount()) {
			dest.addAmount(dest.getAmount()-destAmount);
			if (resultAmount==0) return ItemStack.EMPTY;
			ItemStack result = source.copy();
			result.setAmount(resultAmount);
			return result;
		} else {
			return source;
		}
	}
	
	/** returns the count of items that could not be inserted. Marks the destination inv dirty if needed. */
	public ItemStack pushTo(ItemStack toInsert, Inventory inv, int insertSlot) {
		if (!inv.isValidInvStack(insertSlot, toInsert)) return toInsert;
		
		if (inv.getInvStack(insertSlot).isEmpty()) {
			inv.setInvStack(insertSlot, toInsert);
			inv.markDirty();
			return ItemStack.EMPTY;
		} else {
			ItemStack destStack = inv.getInvStack(insertSlot);
			if (!canStacksCombine(toInsert, destStack)) return toInsert;
			ItemStack leftover = combine(toInsert, destStack);
			if (leftover.getAmount()==toInsert.getAmount()) return toInsert; //Nothing changed
			
			inv.markDirty();
			return leftover.isEmpty() ? ItemStack.EMPTY : leftover;
		}
	}

	//@Override
	public SidedInventory getInventory() {
		return new SidedItemView(items);
	}

	/*
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
	}*/

	
	
	/*
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
	}*/
}
