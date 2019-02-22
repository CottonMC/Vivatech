package io.github.cottonmc.um.component.wrapper;

import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class SidedItemView implements SidedInventory {
	protected SimpleItemComponent delegate;
	
	public SidedItemView(SimpleItemComponent delegate) {
		this.delegate = delegate;
	}

	@Override
	public int getInvSize() {
		return delegate.getInvSize();
	}

	@Override
	public boolean isInvEmpty() {
		return delegate.isInvEmpty();
	}

	@Override
	public ItemStack getInvStack(int slotIndex) {
		return delegate.getInvStack(slotIndex);
	}

	@Override
	public ItemStack takeInvStack(int var1, int var2) {
		return delegate.takeInvStack(var1, var2);
	}

	@Override
	public ItemStack removeInvStack(int var1) {
		return delegate.removeInvStack(var1);
	}

	@Override
	public void setInvStack(int var1, ItemStack var2) {
		delegate.setInvStack(var1, var2);
	}

	@Override
	public void markDirty() {
		delegate.markDirty();
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity var1) {
		return delegate.canPlayerUseInv(var1);
	}

	@Override
	public void clear() {
		delegate.clear();
	}

	@Override
	public int[] getInvAvailableSlots(Direction var1) {
		return new int[delegate.size()]; //TODO: FIXME: THIS IS JUST TO MAKE IT COMPILE FOR NOW. Much larger changes are needed to really fix for 19w03a+
	}

	@Override
	public boolean canInsertInvStack(int var1, ItemStack var2, Direction var3) {
		return delegate.insert(var1, var2, ActionType.SIMULATE).isEmpty();
	}

	@Override
	public boolean canExtractInvStack(int var1, ItemStack var2, Direction var3) {
		return !delegate.extract(var1, var2.getAmount(), ActionType.SIMULATE).isEmpty();
	}
}
