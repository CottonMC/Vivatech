package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.ItemComponent;
import io.github.cottonmc.um.component.SimpleItemComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.LockContainer;
import net.minecraft.container.LockableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextComponent;
public class CoalGeneratorEntity extends BlockEntity implements Inventory, LockableContainer {
	private LockContainer lock;
	private int remainingTicks;
	private int wuBuffer;
	private SimpleItemComponent itemStorage = new SimpleItemComponent();
	
	public CoalGeneratorEntity() {
		super(UMBlocks.COAL_GENERATOR_ENTITY);
	}

	//delegates Inventory to itemStorage {
		@Override
		public void clearInv() {
			itemStorage.clear();
		}
	
		@Override
		public TextComponent getName() {
			return itemStorage.getName();
		}
	
		@Override
		public int getInvSize() {
			return itemStorage.size();
		}
	
		@Override
		public boolean isInvEmpty() {
			return itemStorage.isEmpty();
		}
	
		@Override
		public ItemStack getInvStack(int slotIndex) {
			return itemStorage.get(slotIndex);
		}
	
		@Override
		public ItemStack takeInvStack(int slotIndex, int amount) {
			return itemStorage.extract(slotIndex, amount);
		}
	
		@Override
		public ItemStack removeInvStack(int slotIndex) {
			return itemStorage.extract(slotIndex);
		}
	
		@Override
		public void setInvStack(int slotIndex, ItemStack itemStack) {
			itemStorage.put(slotIndex, itemStack);
		}
	
		@Override
		public boolean canPlayerUseInv(PlayerEntity player) {
			//This one we always have to implement ourselves
			return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	//}
	
	//implements ContainerProvider (implied by LockableContainer) {
		
		@Override
		public Container createContainer(PlayerInventory var1, PlayerEntity var2) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getContainerId() {
			// TODO Auto-generated method stub
			return null;
		}
	//}
	
	//implements LockableContainer {
		@Override
		public boolean hasContainerLock() {
			return lock!=null && !lock.isEmpty();
		}

		@Override
		public void setContainerLock(LockContainer lock) {
			this.lock = lock;
		}

		@Override
		public LockContainer getContainerLock() {
			return lock;
		}
	//}
}
