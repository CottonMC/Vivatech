package vivatech.entity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import vivatech.energy.IEnergyHolder;
import vivatech.energy.IEnergyStorage;
import vivatech.energy.SimpleEnergyGenerator;
import vivatech.init.VivatechEntities;

public class CoalGeneratorEntity extends BlockEntity implements Tickable, Inventory, IEnergyHolder, PropertyDelegateHolder {

    private final int generatePerTick = 1;
    private int burnTime;
    private final int invSize = 1;
    private DefaultedList<ItemStack> inventory = DefaultedList.create(invSize, ItemStack.EMPTY);
    private IEnergyStorage energyStorage = new SimpleEnergyGenerator(100);
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return energyStorage.getCurrentEnergy();
                case 1: // Max Energy
                    return energyStorage.getMaxEnergy();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int propertyId, int value) {
            switch (propertyId) {
                case 0: // Current Energy
                    energyStorage.setCurrentEnergy(value);
                    break;
                case 1: // Max Energy
                    energyStorage.setMaxEnergy(value);
                    break;
                default:
                    break;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };


    public CoalGeneratorEntity() {
        super(VivatechEntities.COAL_GENERATOR);
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        inventory = DefaultedList.create(invSize, ItemStack.EMPTY);
        Inventories.fromTag(tag, inventory);
        energyStorage.readEnergyFromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, inventory);
        energyStorage.writeEnergyToTag(tag);
        return tag;
    }

    // Tickable
    @Override
    public void tick() {
        if (energyStorage.getCurrentEnergy() < energyStorage.getMaxEnergy()) {
            if (burnTime > 0) {
                burnTime -= 1;
                energyStorage.giveEnergy(generatePerTick);
            } else if (FurnaceBlockEntity.canUseAsFuel(inventory.get(0))) {
                burnTime = FurnaceBlockEntity.createFuelTimeMap().getOrDefault(inventory.get(0).getItem(), 0) / 10;
                inventory.get(0).subtractAmount(1);
            }
        }
        energyStorage.emitEnergy(world, pos);
        markDirty();
    }

    // Inventory
    @Override
    public int getInvSize() {
        return invSize;
    }

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


    // IEnergyHolder
    @Override
    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    // PropertyDelegateHolder
    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }
}
