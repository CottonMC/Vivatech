package io.github.cottonmc.um.component;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.DefaultedList;

import io.github.cottonmc.ecs.api.Component;
import io.github.prospector.silk.util.ActionType;

public class SimpleItemComponent implements Component, ItemComponent, Observable, Inventory {
	protected TextComponent name = new TranslatableTextComponent("container.inventory"); //en_us: "Inventory"
	protected final DefaultedList<ItemStack> storage;// = DefaultedList.create(ItemStack.EMPTY);
	protected final ArrayList<Runnable> observers = new ArrayList<>();
	protected int maxStackSize = 64;
	
	public SimpleItemComponent(int size) {
		storage = DefaultedList.create(size, ItemStack.EMPTY);
	}
	
	public SimpleItemComponent setName(TextComponent name) {
		this.name = name;
		return this;
	}
	
	protected boolean checkSlot(int slotIndex) {
		return slotIndex>=0 && slotIndex<storage.size();
	}
	
	protected void onChanged() {
		for(Runnable r : observers) r.run();
	}
	
	protected int getMaxStackSize(int slotIndex, ItemStack existingStack) {
		int itemLimitation = (existingStack!=null && !existingStack.isEmpty()) ? existingStack.getMaxAmount() : 9999;
		
		return Math.min(itemLimitation, maxStackSize);
	}
	
	
	//implements Component {
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
	//}
	
	//implements Observable {
		public void addObserver(Runnable onChanged) {
			observers.add(onChanged);
		}
	//}
	
	//implements Iterable<ItemStack> {
		@Override
		public Iterator<ItemStack> iterator() {
			return storage.iterator();
		}
	//}
	
	//implements Inventory {
		@Override
		public int getInvSize() {
			return storage.size();
		}
		
		@Override
		public boolean isInvEmpty() {
			return storage.isEmpty();
		}
		
		@Override
		public ItemStack getInvStack(int slotIndex) {
			if (!checkSlot(slotIndex)) return ItemStack.EMPTY;
			return storage.get(slotIndex);
		}
		
		@Override
		public ItemStack takeInvStack(int slotIndex, int amount) {
			return extract(slotIndex, amount, ActionType.PERFORM);
		}
		
		@Override
		public ItemStack removeInvStack(int slotIndex) {
			return extract(slotIndex, ActionType.PERFORM);
		}
		
		@Override
		public void setInvStack(int slotIndex, ItemStack stack) {
			storage.set(slotIndex, stack);
			onChanged();
		}
		
		@Override
		public int getInvMaxStackAmount() {
			return maxStackSize;
		}
		
		@Override
		public void markDirty() {
			onChanged();
		}
		
		@Override
		public boolean canPlayerUseInv(PlayerEntity player) {
			return true;
		}
		
		public void onInvOpen(PlayerEntity player) {
		}

		public void onInvClose(PlayerEntity player) {
		}
		
		public boolean isValidInvStack(int int_1, ItemStack itemStack_1) {
			//TODO: Access control
			return true;
		}
		
		//TODO: Property support (see ConcreteInventory)
		public int getInvProperty(int fieldId) {
			return 0;
		}

		public void setInvProperty(int fieldId, int value) {
		}
		
		public int getInvPropertyCount() {
			return 0;
		}
		
		public int getInvHeight() {
			return 0;
		}
		
		public int getInvWidth() {
			return 0;
		}
		
		//From class_3829 a.k.a. Clearable, implied by Inventory
		@Override
		public void clearInv() {
			for(int i=0; i<storage.size(); i++) {
				storage.set(i, ItemStack.EMPTY);
			}
			onChanged();
		}
		
		//Frome Nameable, implied by Inventory
		@Override
		public TextComponent getName() {
			return name;
		}
	//}
	
	//implements ItemComponent {
		@Override
		public int size() {
			return storage.size();
		}
	
		@Override
		public boolean isEmpty() {
			return storage.isEmpty();
		}
	
		@Override
		public ItemStack get(int slotIndex) {
			if (!checkSlot(slotIndex)) return ItemStack.EMPTY;
			return storage.get(slotIndex);
		}
	
		@Override
		public ItemStack extract(int slotIndex, int amount, ActionType action) {
			//TODO: Access control
			
			//Same invariants and failure results as InventoryUtil.splitStack
			if (!checkSlot(slotIndex)) return ItemStack.EMPTY;
			if (storage.get(slotIndex).isEmpty()) return ItemStack.EMPTY;
			if (amount<=0) return ItemStack.EMPTY;
			
			int toTake = Math.min(storage.get(slotIndex).getAmount(), amount);
			
			ItemStack result = storage.get(slotIndex).copy();
			result.setAmount(toTake);
			
			if (action==ActionType.PERFORM) {
				storage.get(slotIndex).subtractAmount(toTake);
				onChanged();
			}
			
			return result;
		}
	
		@Override
		public ItemStack insert(int slotIndex, ItemStack stack, ActionType action) {
			//TODO: Access control
			
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
	
		@Override
		public ItemStack insert(ItemStack stack, ActionType action) {
			//TODO: Is this the best way? It certainly might not be the fastest.
			
			ItemStack remaining = stack.copy();
			for(int i=0; i<storage.size(); i++) {
				remaining = insert(i, remaining, action);
				if (remaining.isEmpty()) return ItemStack.EMPTY;
			}
			
			return remaining;
		}
		
		public int getMaxStackSize(int slotIndex) {
			return getMaxStackSize(slotIndex, ItemStack.EMPTY);
		}
	//}
}