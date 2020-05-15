package vivatech.common.block.entity;

import io.github.cottonmc.component.api.ActionType;
import io.github.cottonmc.component.item.InventoryComponent;
import io.github.cottonmc.component.item.impl.SimpleInventoryComponent;
import io.github.cottonmc.energy.api.DefaultEnergyTypes;
import io.github.cottonmc.energy.api.EnergyType;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import vivatech.api.block.entity.AbstractTieredMachineBlockEntity;
import vivatech.common.init.VivatechEntities;

import javax.annotation.Nullable;

public class ElectricFurnaceBlockEntity extends AbstractTieredMachineBlockEntity {
    private static final int TICK_PER_CONSUME = 5;
    private static final int CONSUME_PER_TICK = 2;
    private int cookTime = 0;
    private int cookTimeTotal = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return energy.getCurrentEnergy();
                case 1: // Max Energy
                    return energy.getMaxEnergy();
                case 2: // Cook Time
                    return cookTime;
                case 3: // Cook Time Total
                    return cookTimeTotal;
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
                case 2: // Cook Time
                    cookTime = value;
                    break;
                case 3: // Cook Time Total
                    cookTimeTotal = value;
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

    public ElectricFurnaceBlockEntity() {
        this(DefaultEnergyTypes.LOW_VOLTAGE);
    }

    public ElectricFurnaceBlockEntity(EnergyType type) {
        super(VivatechEntities.ELECTRIC_FURNACE, type);
    }

    @Override
    protected int getMaxEnergy() {
        return 1_000;
    }

    // SidedInventory
    @Override
    public int[] getInvAvailableSlots(Direction side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack stack, @Nullable Direction dir) {
        return slot == 0;
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
        return slot == 1;
    }

    @Override
    public int getInvSize() {
        return 2;
    }

    // PropertyDelegateHolder
    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }
}
