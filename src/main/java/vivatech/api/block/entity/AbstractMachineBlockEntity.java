package vivatech.api.block.entity;

import io.github.cottonmc.component.api.ActionType;
import io.github.cottonmc.component.item.InventoryComponent;
import io.github.cottonmc.component.item.impl.SimpleInventoryComponent;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyAttributeProvider;
import io.github.cottonmc.energy.api.EnergyType;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import vivatech.api.block.AbstractMachineBlock;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements Tickable, SidedInventory, PropertyDelegateHolder,
        BlockEntityClientSerializable, EnergyAttributeProvider {
    protected boolean active = false;
    protected EnergyType energyType;
    protected InventoryComponent inventory = new SimpleInventoryComponent(getInvSize());
    protected SimpleEnergyAttribute energy;

    public AbstractMachineBlockEntity(BlockEntityType<?> type, EnergyType energyType) {
        super(type);
        this.energyType = energyType;
        this.energy = new SimpleEnergyAttribute(getMaxEnergy(), energyType) {
            @Override
            public boolean canInsertEnergy() { return AbstractMachineBlockEntity.this.canInsertEnergy(); }

            @Override
            public boolean canExtractEnergy() { return AbstractMachineBlockEntity.this.canExtractEnergy(); }
        };
    }

    protected abstract int getMaxEnergy();

    protected boolean canInsertEnergy() {
        return true;
    }

    protected boolean canExtractEnergy() {
        return true;
    }

    public EnergyAttribute getEnergy() {
        return energy;
    }

    public InventoryComponent getInventoryComponent() {
        return inventory;
    }

    protected void serverTick() {}

    protected void clientTick() {}

    protected void setActive(boolean active) {
        if (this.active == active) return;
        this.active = active;
        world.setBlockState(pos, getCachedState().with(AbstractMachineBlock.ACTIVE, active), 3);
    }

    protected void notifyWorldListeners() {
        markDirty();
        world.updateListeners(pos, getCachedState(), world.getBlockState(pos), 3);
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        energy.fromTag(tag.getCompound("Energy"));
        inventory.fromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Energy", energy.toTag());
        tag = inventory.toTag(tag);
        return tag;
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) {
            clientTick();
        } else {
            serverTick();
        }
    }

    // SidedInventory

    @Override
    public boolean isInvEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return inventory.getMutableStacks().get(slot);
    }

    @Override
    public ItemStack takeInvStack(int slot, int count) {
        return inventory.takeStack(slot, count, ActionType.PERFORM);
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        return inventory.removeStack(slot, ActionType.PERFORM);
    }

    @Override
    public void setInvStack(int slot, ItemStack itemStack) {
        inventory.setStack(slot, itemStack);
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

    // BlockEntityClientSerializable
    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    // EnergyAttributeProvider
    @Override
    public EnergyAttribute getEnergyAttribute() {
        return energy;
    }
}
