package vivatech.entity;

import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.cotton.datapack.recipe.CottonRecipes;
import io.github.cottonmc.cotton.datapack.recipe.CrushingRecipe;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.math.Direction;
import vivatech.Vivatech;
import vivatech.init.VivatechEntities;

import javax.annotation.Nullable;

public class CrusherEntity extends AbstractMachineEntity {

    private static final int CONSUME_PER_TICK = 1;
    private int crushTime = 0;
    private int crushTimeTotal = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return energy.getCurrentEnergy();
                case 1: // Max Energy
                    return energy.getMaxEnergy();
                case 2: // Crush Time
                    return crushTime;
                case 3: // Crush Time Total
                    return crushTimeTotal;
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
                case 2: // Crush Time
                    crushTime = value;
                    break;
                case 3: // Crush Time Total
                    crushTimeTotal = value;
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

    public CrusherEntity() {
        super(VivatechEntities.CRUSHER);
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
            crushTime++;
            energy.extractEnergy(Vivatech.ENERGY, CONSUME_PER_TICK, Simulation.ACTION);
            if (crushTimeTotal == 0) {
                crushTimeTotal = world.getRecipeManager().getFirstMatch(CottonRecipes.CRUSHING_RECIPE, this, world)
                        .map(CrushingRecipe::getProcessTime).orElse(200);
            }
            setBlockActive(true);
            if (crushTime >= crushTimeTotal) {
                crushTime = 0;
                crushTimeTotal = 0;
                crushItem();
                if (inventory.get(0).getAmount() == 0) {
                    setBlockActive(false);
                }
                updateEntity();
            }
        } else if (!canRun() && crushTime > 0) {
            crushTime = 0;
            setBlockActive(false);
        }
    }

    public ItemStack getOutputStack() {
        if (!inventory.get(0).isEmpty()) {
            Recipe recipe = world.getRecipeManager().getFirstMatch(CottonRecipes.CRUSHING_RECIPE, this, world).orElse(null);
            return recipe != null ? recipe.getOutput().copy() : ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }

    public boolean canRun() {
        ItemStack output = getOutputStack();
        if (inventory.get(0).isEmpty()
                || output.isEmpty()
                || inventory.get(1).getAmount() > 64
                || energy.getCurrentEnergy() < CONSUME_PER_TICK) {
            return false;
        } else if (!inventory.get(1).isEmpty()) {
            return output.getItem() == inventory.get(1).getItem();
        }

        return true;
    }

    public void crushItem() {
        ItemStack output = getOutputStack();
        if (!output.isEmpty()) {
            if (inventory.get(1).isEmpty()) {
                inventory.set(1, output);
            } else {
                inventory.get(1).addAmount(1);
            }

            inventory.get(0).subtractAmount(1);
        }
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        crushTime = tag.getInt("CrushTime");
        crushTimeTotal = tag.getInt("CrushTimeTotal");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("CrushTime", crushTime);
        tag.putInt("CrushTimeTotal", crushTimeTotal);
        return tag;
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
}
