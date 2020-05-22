package vivatech.common.block.entity;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import vivatech.api.block.entity.AbstractMachineBlockEntity;
import vivatech.common.init.VivatechBlockEntities;

import javax.annotation.Nullable;

public class SterlingGeneratorBlockEntity extends AbstractMachineBlockEntity {
    private static final int GENERATE_PER_FRAME = 1;
    private static final int TICKS_PER_FRAME = 4;
    private int burnTime = 0;
    private int burnTimeTotal = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return capacitor.getCurrentEnergy();
                case 1: // Max Energy
                    return capacitor.getMaxEnergy();
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
                    capacitor.setCurrentEnergy(value);
                    break;
                case 1: // Max Energy
                    capacitor.setMaxEnergy(value);
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
        super(VivatechBlockEntities.STERLING_GENERATOR);
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
        // TODO: actual logic
//        if (burnTime > 0) {
//            burnTime--;
//            if (burnTime % TICKS_PER_FRAME == 0) energyOld.insertEnergy(energyType, GENERATE_PER_FRAME, Simulation.ACTION);
//        } else if (inventory.getStack(0).getCount() > 0 && energyOld.getCurrentEnergy() < energyOld.getMaxEnergy()) {
//            burnTime = FurnaceBlockEntity.createFuelTimeMap().getOrDefault(inventory.getStack(0).getItem(), 0) / 2;
//            burnTimeTotal = burnTime;
//            inventory.getStack(0).decrement(1);
//            setActive(true);
//            notifyWorldListeners();
//        }
//
//        if (burnTime == 0) {
//            burnTimeTotal = 0;
//            if (inventory.getStack(0).getCount() == 0) {
//                setActive(false);
//            }
//            notifyWorldListeners();
//        }
//
//        if (energyOld.getCurrentEnergy() != 0) {
//            EnergyHelper.emit(energyOld, world, pos);
//            notifyWorldListeners();
//        }
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
