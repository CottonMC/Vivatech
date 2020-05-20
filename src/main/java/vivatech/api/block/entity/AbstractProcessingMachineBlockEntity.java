package vivatech.api.block.entity;

import io.github.cottonmc.component.api.ActionType;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.math.Direction;
import vivatech.api.recipe.ProcessingRecipe;

import javax.annotation.Nullable;

public abstract class AbstractProcessingMachineBlockEntity extends AbstractTieredMachineBlockEntity {
    private final RecipeType recipeType;
    private ProcessingRecipe recipe = null;
    private int timePassed = 0;
    private int timeToProcess = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0:
                    return capacitor.getCurrentEnergy();
                case 1:
                    return capacitor.getMaxEnergy();
                case 2:
                    return timePassed;
                case 3:
                    return timeToProcess;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int propertyId, int value) {
            switch (propertyId) {
                case 0:
                    capacitor.setCurrentEnergy(value);
                    break;
                case 1:
                    capacitor.setMaxEnergy(value);
                    break;
                case 2:
                    timePassed = value;
                    break;
                case 3:
                    timeToProcess = value;
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

    public AbstractProcessingMachineBlockEntity(BlockEntityType<?> type, RecipeType<? extends ProcessingRecipe> recipeType) {
        super(type);
        this.recipeType = recipeType;
    }

    // AbstractMachineEntity
    @Override
    protected int getMaxEnergy() {
        return 1_000;
    }

    @Override
    protected boolean canExtractEnergy() {
        return false;
    }

    @Override
    protected void serverTick() {
        if (!active) {
            recipe = (ProcessingRecipe) world.getRecipeManager()
                .getFirstMatch(RecipeType.SMELTING, inventory.asInventory(), world)
                .orElse(null);

            if (recipe != null) {
                timeToProcess = (int) (recipe.getProcessTime() / getTier().getSpeedMultiplier());

                if (inventory.insertStack(1, recipe.getOutput().copy(), ActionType.TEST).isEmpty()) {
                    setActive(true);
                }
            }
        }

        if (active) {
            timePassed++;
            if (timePassed >= timeToProcess) {
                timePassed = 0;
                inventory.takeStack(0, 1, ActionType.PERFORM);
                inventory.insertStack(1, recipe.getOutput().copy(), ActionType.PERFORM);
                notifyWorldListeners();
            }

            if (inventory.getStack(0).isEmpty()
                || !inventory.insertStack(1, recipe.getOutput().copy(), ActionType.TEST).isEmpty()) {
                timePassed = 0;
                setActive(false);
            }
        }
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        if (tag.contains("ProcessData", NbtType.COMPOUND)) {
            CompoundTag processData = tag.getCompound("ProcessData");
            timePassed = processData.getInt("TimePassed");
            timeToProcess = processData.getInt("TimeToProcess");
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        CompoundTag processData = new CompoundTag();
        processData.putInt("TimePassed", timePassed);
        processData.putInt("TimeToProcess", timeToProcess);
        tag.put("ProcessData", processData);
        return tag;
    }

    // SidedInventory
    @Override
    public int[] getInvAvailableSlots(Direction side) {
        return new int[]{0, 1};
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
