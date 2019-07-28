package vivatech.common.block.entity;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import vivatech.common.Vivatech;
import vivatech.api.block.entity.AbstractMachineBlockEntity;
import vivatech.common.init.VivatechEntities;
import vivatech.util.EnergyHelper;

import javax.annotation.Nullable;

public class SterlingGeneratorBlockEntity extends AbstractMachineBlockEntity {
    private static final int GENERATE_PER_TICK = 1;
    private static final int TICK_PER_GENERATE = 5;
    private int burnTime = 0;
    private int burnTimeTotal = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return energy.getCurrentEnergy();
                case 1: // Max Energy
                    return energy.getMaxEnergy();
                case 2: // Burn Time
                    return burnTime;
                case 3: // Burn Time Total
                    return burnTimeTotal;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int propertyId, int value) {
            switch (propertyId) {
                case 0: // Current Energy
                    energy.setCurrentEnergy(value);
                    break;
                case 1: // Max Energy
                    energy.setMaxEnergy(value);
                    break;
                case 2: // Burn Time
                    burnTime = value;
                    break;
                case 3: // Burn Time Total
                    burnTimeTotal = value;
                    break;
                default:
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };


    public SterlingGeneratorBlockEntity() {
        super(VivatechEntities.COAL_GENERATOR);
    }

    // AbstractMachineEntity
    @Override
    protected int getMaxEnergy() {
        return 1_000;
    }

    @Override
    protected boolean canInsertEnergy() {
        return false;
    }

    @Override
    protected void serverTick() {
        if (burnTime > 0) {
            burnTime--;
            if (burnTime % TICK_PER_GENERATE == 0) energy.insertEnergy(Vivatech.INFINITE_VOLTAGE, GENERATE_PER_TICK, Simulation.ACTION);
        } else if (inventory.get(0).getCount() > 0 && energy.getCurrentEnergy() < energy.getMaxEnergy()) {
            burnTime = FurnaceBlockEntity.createFuelTimeMap().getOrDefault(inventory.get(0).getItem(), 0) / 2;
            burnTimeTotal = burnTime;
            inventory.get(0).decrement(1);
            setBlockActive(true);
            updateEntity();
        }

        if (burnTime == 0) {
            burnTimeTotal = 0;
            if (inventory.get(0).getCount() == 0) {
                setBlockActive(false);
            }
            updateEntity();
        }

        if (energy.getCurrentEnergy() != 0) {
            EnergyHelper.emit(energy, world, pos);
            updateEntity();
        }
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        burnTime = tag.getInt("BurnTime");
        burnTimeTotal = tag.getInt("BurnTimeTotal");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("BurnTimeTotal", burnTimeTotal);
        return tag;
    }

    // SidedInventory
    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return isValidInvStack(slot, itemStack);
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getInvSize() {
        return 1;
    }

    @Override
    public boolean isValidInvStack(int slot, ItemStack itemStack) {
        return AbstractFurnaceBlockEntity.canUseAsFuel(itemStack);
    }

    // PropertyDelegateHolder
    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }
}
