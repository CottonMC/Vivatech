package io.github.cottonmc.um.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public interface InventoryDelegate extends Inventory {
	Inventory getInventoryDelegate();

	@Override
	default int getInvSize() {
		return getInventoryDelegate().getInvSize();
	}

	@Override
	default boolean isInvEmpty() {
		return getInventoryDelegate().isInvEmpty();
	}

	@Override
	default ItemStack getInvStack(int slotIndex) {
		return getInventoryDelegate().getInvStack(slotIndex);
	}

	@Override
	default ItemStack takeInvStack(int slotIndex, int amount) {
		return getInventoryDelegate().takeInvStack(slotIndex, amount);
	}

	@Override
	default ItemStack removeInvStack(int slotIndex) {
		return getInventoryDelegate().removeInvStack(slotIndex);
	}

	@Override
	default void setInvStack(int slotIndex, ItemStack itemStack) {
		getInventoryDelegate().setInvStack(slotIndex,itemStack);
	}

	@Override
	default void markDirty() {
		getInventoryDelegate().markDirty();
	}

	@Override
	default boolean canPlayerUseInv(PlayerEntity playerEntity) {
		return getInventoryDelegate().canPlayerUseInv(playerEntity);
	}

	@Override
	default void clear() {
		getInventoryDelegate().clear();
	}
}