package vivatech.entity;

import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import vivatech.energy.EnergyAttributeProviderItem;
import vivatech.init.VivatechEntities;
import vivatech.util.EnergyHelper;

import javax.annotation.Nullable;

public class EnergyBankEntity extends AbstractMachineEntity {

    private static final int TRANSFER_PER_TICK = 4;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return energy.getCurrentEnergy();
                case 1: // Max Energy
                    return energy.getMaxEnergy();
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
                default:
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public EnergyBankEntity() {
        super(VivatechEntities.ENERGY_BANK);
    }

    // AbstractMachineEntity
    @Override
    protected int getMaxEnergy() {
        return 3_000;
    }

    @Override
    protected void serverTick() {
        boolean updateNeeded = false;

        ItemStack chargingStack = inventory.get(0);
        if (!chargingStack.isEmpty() && energy.getCurrentEnergy() <= energy.getMaxEnergy()) {
            EnergyAttributeProviderItem chargingItem = (EnergyAttributeProviderItem) chargingStack.getItem();
            EnergyAttribute stackEnergy = chargingItem.getEnergyAttribute(chargingStack);

            int transferSize = Math.min(stackEnergy.getCurrentEnergy(), TRANSFER_PER_TICK);
            EnergyHelper.transfer(stackEnergy, energy, transferSize);

            updateNeeded = true;
        }

        ItemStack dischargingStack = inventory.get(1);
        if (!dischargingStack.isEmpty() && energy.getCurrentEnergy() > 0) {
            EnergyAttributeProviderItem dischargingItem = (EnergyAttributeProviderItem) dischargingStack.getItem();
            EnergyAttribute stackEnergy = dischargingItem.getEnergyAttribute(dischargingStack);

            int transferSize = Math.min(energy.getCurrentEnergy(), TRANSFER_PER_TICK);
            EnergyHelper.transfer(energy, stackEnergy, transferSize);

            updateNeeded = true;
        }

        if (energy.getCurrentEnergy() != 0) {
            EnergyHelper.emit(energy, world, pos);
            updateNeeded = true;
        }

        if (updateNeeded) {
            updateEntity();
        }
    }

    // PropertyDelegateProvider
    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    // SidedInventory
    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[]{0,1};
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return isValidInvStack(slot, itemStack);
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public int getInvSize() {
        return 2;
    }

    @Override
    public boolean isValidInvStack(int slot, ItemStack itemStack) {
        return itemStack.getItem() instanceof EnergyAttributeProviderItem;
    }
}
