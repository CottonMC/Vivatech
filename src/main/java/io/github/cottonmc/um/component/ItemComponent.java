package io.github.cottonmc.um.component;

import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;

/**
 * Class of components designed to both help porting of Capability-based blocks from Forge, and enable delegation
 * from Inventory/LockableContainer methods to an easy to use, pre-written and pre-tested solution.
 */
public interface ItemComponent {
	/** Gets the number of available itemslots in this Component */
	public int size();
	
	/** Gets the name of this Component, if viewed in a gui */
	public TextComponent getName();
	
	/** Returns true if there are no items in this Component */
	public boolean isEmpty();
	
	/** Returns the ItemStack in the indicated slot */
	public ItemStack get(int slotIndex);
	
	/** Attempts to extract the specified number of items from the indicated slot. */
	public ItemStack extract(int slotIndex, int amount);
	
	/** Attempts to extract the entire stack from the indicated slot. */
	public ItemStack extract(int slotIndex);
	
	/**
	 * Attempts to insert an ItemStack into the indicated slot.
	 * @return any items leftover that weren't able to be inserted
	 */
	public ItemStack insert(int slotIndex, ItemStack stack);
	
}
