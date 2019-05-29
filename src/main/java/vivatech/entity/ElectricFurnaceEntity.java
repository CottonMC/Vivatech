package vivatech.entity;

import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.math.Direction;
import vivatech.Vivatech;
import vivatech.block.AbstractMachineBlock;
import vivatech.init.VivatechEntities;

import javax.annotation.Nullable;

public class ElectricFurnaceEntity extends AbstractMachineEntity {

    private final int consumePerTick = 2;
    private int cookTime = 0;
    private int cookTimeTotal = 100;
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

    public ElectricFurnaceEntity() {
        super(VivatechEntities.ELECTRIC_FURNACE);
    }

    // AbstractMachineEntity
    @Override
    protected int getMaxEnergy() {
        return 10_000;
    }

    @Override
    protected boolean canExtractEnergy() {
        return false;
    }

    @Override
    protected void serverTick() {
        if (canRun()) {
            cookTime++;
            energy.extractEnergy(Vivatech.ENERGY, consumePerTick, Simulation.ACTION);
            setBlockActive(true);
            if (cookTime >= cookTimeTotal) {
                cookTime = 0;
                smeltItem();
                if (inventory.get(0).getAmount() == 0) {
                    setBlockActive(false);
                }
                updateListeners();
            }
        } else if (!canRun() && cookTime > 0) {
            cookTime = 0;
            setBlockActive(false);
        }
    }

    public ItemStack getOutputStack() {
        if (!inventory.get(0).isEmpty()) {
            Recipe recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, this, world).orElse(null);
            return recipe != null ? recipe.getOutput().copy() : ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }

    public boolean canRun() {
        ItemStack output = getOutputStack();
        if (inventory.get(0).isEmpty()
                || output.isEmpty()
                || inventory.get(1).getAmount() > 64
                || energy.getCurrentEnergy() < consumePerTick) {
            return false;
        } else if (!inventory.get(1).isEmpty()) {
            return output.getItem() == inventory.get(1).getItem();
        }

        return true;
    }

    public void smeltItem() {
        ItemStack output = getOutputStack();
        if (!output.isEmpty()) {
            if (!world.isClient) {
                if (inventory.get(1).isEmpty()) {
                    inventory.set(1, output);
                } else {
                    inventory.get(1).addAmount(1);
                }

                inventory.get(0).subtractAmount(1);
            }
        }
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        cookTime = tag.getInt("CookTime");
        cookTimeTotal = tag.getInt("CookTimeTotal");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("CookTime", cookTime);
        tag.putInt("CookTimeTotal", cookTimeTotal);
        return tag;
    }

    // SidedInventory
    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return isValidInvStack(slot, itemStack);
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack itemStack, Direction direction) {
        return slot == 1;
    }

    @Override
    public int getInvSize() {
        return 2;
    }

    @Override
    public boolean isValidInvStack(int slot, ItemStack itemStack) {
        return slot == 0;
    }

    // PropertyDelegateHolder
    @Override
    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    // EnergyAttributeProvider
    @Override
    public EnergyAttribute getEnergy() {
        return energy;
    }
}
