package io.github.cottonmc.um.component;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.InventoryUtil;

public class SimpleItemComponent implements ItemComponent, Observable {
	protected TextComponent name = new StringTextComponent("");
	protected final DefaultedList<ItemStack> storage;// = DefaultedList.create(ItemStack.EMPTY);
	protected final ArrayList<Runnable> observers = new ArrayList<>();
	protected int maxStackSize = 64;
	
	public SimpleItemComponent(int size) {
		storage = DefaultedList.create(size, ItemStack.EMPTY);
	}
	
	//implements ItemComponent {
		
		@Override
		public int size() {
			return storage.size();
		}
	
		@Override
		public TextComponent getName() {
			return name;
		}
	
		@Override
		public boolean isEmpty() {
			if (storage.size()==0) return true;
			for(int i=0; i<storage.size(); i++) {
				if (!storage.get(i).isEmpty()) return false;
			}
			return true;
		}
	
		@Override
		public ItemStack get(int slotIndex) {
			if (!checkSlot(slotIndex)) return ItemStack.EMPTY;
			return storage.get(slotIndex);
		}
	
		@Override
		public ItemStack extract(int slotIndex, int amount) {
			//TODO: Access control
			
			//Same invariants and failure results as InventoryUtil.splitStack
			if (!checkSlot(slotIndex)) return ItemStack.EMPTY;
			if (storage.get(slotIndex).isEmpty()) return ItemStack.EMPTY;
			if (amount<=0) return ItemStack.EMPTY;
			
			ItemStack result = storage.get(slotIndex).split(amount);
			onChanged();
			return result;
		}
	
		@Override
		public ItemStack extract(int slotIndex) {
			if (!checkSlot(slotIndex)) return ItemStack.EMPTY;
			if (storage.get(slotIndex).isEmpty()) return ItemStack.EMPTY;
			
			ItemStack result = storage.get(slotIndex);
			storage.set(slotIndex, ItemStack.EMPTY);
			onChanged();
			return result;
		}
	
		@Override
		public ItemStack insert(int slotIndex, ItemStack stack) {
			if (!checkSlot(slotIndex)) return stack;
			if (stack.isEmpty()) return ItemStack.EMPTY;
			
			ItemStack existing = storage.get(slotIndex);
			if (existing.isEmpty()) {
				ItemStack result = ItemStack.EMPTY;
				storage.set(slotIndex, stack);
				int max = getMaxStackSize(slotIndex, stack);
				if (stack.getAmount()>max) {
					result = stack.split(stack.getAmount()-max);
				}
				onChanged();
				return result;
			} else {
				ItemStack result = ItemStack.EMPTY;
				boolean canStack = stack.isEqualIgnoreTags(existing) && ItemStack.areTagsEqual(stack, existing);
				if (canStack) {
					existing.setAmount(existing.getAmount()+stack.getAmount());
					int max = getMaxStackSize(slotIndex, existing);
					if (existing.getAmount()>max) result = existing.split(existing.getAmount()-max);
					onChanged();
				}
				return result;
			}
		}
		
	//}
	
	public void put(int slotIndex, ItemStack itemStack) {
		if (checkSlot(slotIndex)) {
			storage.set(slotIndex, itemStack);
			int max = getMaxStackSize(slotIndex, itemStack);
			if (itemStack.getAmount()>max) itemStack.setAmount(max);
			onChanged();
		}
	}
	
	/** Sets all slots to empty stacks, such that isEmpty will return true after this call */
	public void clear() {
		for(int i=0; i<size(); i++) {
			put(i, ItemStack.EMPTY);
		}
	}
	
	protected boolean checkSlot(int slotIndex) {
		return slotIndex>=0 && slotIndex<storage.size();
	}
	
	protected int getMaxStackSize(int slotIndex, ItemStack existingStack) {
		int itemLimitation = (existingStack!=null && !existingStack.isEmpty()) ? existingStack.getMaxAmount() : 9999;
		
		return Math.min(itemLimitation, maxStackSize);
	}
	
	protected void onChanged() {
		for(Runnable r : observers) r.run();
	}
	
	public void addObserver(Runnable onChanged) {
		observers.add(onChanged);
	}
	
	/**
	 * Gets the serialized version of this Component. In this case, it should generally get an "Items" key on the host
	 * tile.
	 */
	public Tag toTag() {
		ListTag tag = new ListTag();

		for(int i = 0; i < storage.size(); ++i) {
			ItemStack itemStack = storage.get(i);
			if (!itemStack.isEmpty()) {
				CompoundTag itemTag = new CompoundTag();
				itemTag.putByte("Slot", (byte)i);
				itemStack.toTag(itemTag);
				tag.add((Tag)itemTag);
			}
		}
		
		return tag;
	}

	public void fromTag(Tag tag) {
		if (tag instanceof ListTag) {
			ListTag list = (ListTag)tag;
			
			for(int i = 0; i < list.size(); ++i) {
				CompoundTag itemTag = list.getCompoundTag(i);
				int slot = itemTag.getByte("Slot") & 255;
				if (slot >= 0 && slot < storage.size()) {
					storage.set(slot, ItemStack.fromTag(itemTag));
				}
			}
		}
	}
}
