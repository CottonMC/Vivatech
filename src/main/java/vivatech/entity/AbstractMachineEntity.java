package vivatech.entity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import vivatech.Vivatech;
import vivatech.energy.EnergyAttributeProvider;

public abstract class AbstractMachineEntity extends BlockEntity implements Tickable, SidedInventory, PropertyDelegateHolder, EnergyAttributeProvider {
    protected DefaultedList<ItemStack> inventory = DefaultedList.create(getInvSize(), ItemStack.EMPTY);
    protected SimpleEnergyAttribute energy = new SimpleEnergyAttribute(getMaxEnergy(), Vivatech.ENERGY) {
        @Override
        public boolean canInsertEnergy() { return AbstractMachineEntity.this.canInsertEnergy(); }

        @Override
        public boolean canExtractEnergy() { return AbstractMachineEntity.this.canExtractEnergy(); }
    };


    public AbstractMachineEntity(BlockEntityType<?> type) {
        super(type);
    }

    protected abstract int getMaxEnergy();
    protected abstract boolean canInsertEnergy();
    protected abstract boolean canExtractEnergy();

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        energy.fromTag(tag.getTag("Energy"));
        inventory = DefaultedList.create(getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Energy", energy.toTag());
        Inventories.toTag(tag, inventory);
        return tag;
    }

    // SidedInventory
    @Override
    public boolean isInvEmpty() {
        for (ItemStack itemStack : inventory) if (!itemStack.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack takeInvStack(int slot, int parts) {
        return Inventories.splitStack(inventory, slot, parts);
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setInvStack(int slot, ItemStack itemStack) {
        inventory.set(slot, itemStack);
        if (itemStack.getAmount() > getInvMaxStackAmount()) {
            itemStack.setAmount(getInvMaxStackAmount());
        }
        markDirty();
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        if (world.getBlockEntity(pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo(
                    (double) pos.getX() + 0.5D,
                    (double) pos.getY() + 0.5D,
                    (double) pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}
