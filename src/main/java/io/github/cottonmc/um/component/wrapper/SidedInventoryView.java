package io.github.cottonmc.um.component.wrapper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;

public class SidedInventoryView implements SidedInventory {
	private Inventory delegate;

	public SidedInventoryView(Inventory inv) {
		this.delegate = inv;
	}
	@Override
	public int[] getInvAvailableSlots(Direction direction) {
		int[] slots = new int[delegate.getInvSize()];
		for (int i = 0; i < delegate.getInvSize(); i++) {
			slots[i] = i;
		}
		return slots;
	}

	@Override
	public boolean canInsertInvStack(int i, ItemStack itemStack, @Nullable Direction direction) {
		return delegate.isValidInvStack(i, itemStack);
	}

	@Override
	public boolean canExtractInvStack(int i, ItemStack itemStack, Direction direction) {
		return true;
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
	public ItemStack getInvStack(int i) {
		return delegate.getInvStack(i);
	}

	@Override
	public ItemStack takeInvStack(int i, int i1) {
		ItemStack done = delegate.takeInvStack(i, i1);
		delegate.markDirty();
		return done;
	}

	@Override
	public ItemStack removeInvStack(int i) {
		ItemStack done = delegate.removeInvStack(i);
		delegate.markDirty();
		return done;
	}

	@Override
	public void setInvStack(int i, ItemStack itemStack) {
		delegate.setInvStack(i, itemStack);
		delegate.markDirty();
	}

	@Override
	public void markDirty() {
		delegate.markDirty();
	}

	@Override
	public boolean canPlayerUseInv(PlayerEntity playerEntity) {
		return delegate.canPlayerUseInv(playerEntity);
	}

	@Override
	public void clear() {
		delegate.clear();
		delegate.markDirty();
	}
}
