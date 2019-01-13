package io.github.cottonmc.um.component;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.prospector.silk.util.ActionType;
import net.minecraft.item.ItemStack;

/**
 * Class of components designed help porting of Capability-based item storage blocks from Forge, and to avoid repeated,
 * error-prone manual reimplementation of a large number of methods on BlockEntities
 */
@ParametersAreNonnullByDefault
public interface ItemComponent extends Iterable<ItemStack> {
	/** Gets the number of available itemslots in this Component */
	public int size();
	
	/** Returns true if there are no items in this Component */
	public boolean isEmpty();
	
	/** Returns the ItemStack in the indicated slot. DO NOT MODIFY THE RETURNED ITEMSTACK. */
	@Nonnull
	public ItemStack get(int slotIndex);
	
	/**
	 * Attempts to extract the specified number of items from the indicated slot.
	 * 
	 * The caller is permitted to modify the returned ItemStack.
	 * @param slotIndex the index of the slot to extract from.
	 * @param amount the maximum stack size to extract.
	 * @param action whether to just simulate, or to actually modify the component by performing the extraction.
	 * @return if action is SIMULATE, the ItemStack that would be extracted. If it's PERFORM, the stack that *was*
	 *         extracted. Returns ItemStack.EMPTY if nothing could/would be extracted.
	 */
	@Nonnull
	public ItemStack extract(int slotIndex, int amount, ActionType action);
	
	/**
	 * Attempts to extract the entire stack from the indicated slot. This is a convenience version of
	 * {@link #extract(int, int, ActionType)}
	 * 
	 * As with that method, the caller is permitted to modify the returned ItemStack.
	 */
	@Nonnull
	public default ItemStack extract(int slotIndex, ActionType action) {
		return extract(slotIndex, get(slotIndex).getAmount(), action);
	}
	
	
	/**
	 * Attempts to insert an ItemStack into the indicated slot of this Component.
	 * @param slotIndex the index of the slot to insert into.
	 * @param stack the ItemStack to insert.
	 * @param action whether to just simulate, or to actually modify the component by performing the insert.
	 * @return any items left over that weren't able to be inserted.
	 */
	@Nonnull
	public ItemStack insert(int slotIndex, ItemStack stack, ActionType action);
	
	/**
	 * Attempts to insert an ItemStack into any available slot in this Component. The Component MAY at its discretion
	 * split the stack between multiple slots.
	 * @param stack the ItemStack to insert.
	 * @param action whether to just simulate, or to actually modify the component by performing the insert.
	 * @return any items left over that weren't able to be inserted.
	 */
	@Nonnull
	public ItemStack insert(ItemStack stack, ActionType action);
	
	/**
	 * Gets the maximum stack size for the specified slot.
	 */
	public int getMaxStackSize(int slotIndex);
}
