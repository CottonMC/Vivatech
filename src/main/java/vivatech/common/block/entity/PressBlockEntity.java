package vivatech.common.block.entity;

import javax.annotation.Nullable;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import vivatech.common.Vivatech;
import vivatech.api.block.entity.AbstractTieredMachineBlockEntity;
import vivatech.common.init.VivatechEntities;
import vivatech.common.init.VivatechRecipes;
import vivatech.common.recipe.PressingRecipe;
import vivatech.api.util.BlockTier;

public class PressBlockEntity extends AbstractTieredMachineBlockEntity {

    public static final int TICK_PER_CONSUME = 5;
    public static final int CONSUME_PER_TICK = 1;
    private int pressTime = 0;
    private int pressTimeTotal = 0;
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return energy.getCurrentEnergy();
                case 1: // Max Energy
                    return energy.getMaxEnergy();
                case 2: // Press Time
                    return pressTime;
                case 3: // Press Time Total
                    return pressTimeTotal;
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
                case 2: // Press Time
                    pressTime = value;
                    break;
                case 3: // Press Time Total
                    pressTimeTotal = value;
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
    
    public PressBlockEntity() {
        super(VivatechEntities.PRESS);
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
    	BlockTier tier = getTier();
    	
        if (canRun()) {
            pressTime++;
            if (pressTime % TICK_PER_CONSUME == 0) energy.extractEnergy(Vivatech.INFINITE_VOLTAGE, CONSUME_PER_TICK * (int)tier.getSpeedMultiplier(), Simulation.ACTION);
            if (pressTimeTotal == 0) {
                pressTimeTotal = (int) (world.getRecipeManager().getFirstMatch(VivatechRecipes.PRESSING, this, world)
                        .map(PressingRecipe::getProcessTime).orElse(200) / tier.getSpeedMultiplier());
            }
            setBlockActive(true);
            if (pressTime >= pressTimeTotal) {
                pressTime = 0;
                pressTimeTotal = 0;
                pressItem();
                if (inventory.get(0).getCount() == 0) {
                    setBlockActive(false);
                }
                updateEntity();
            }
        } else if (!canRun() && pressTime > 0) {
            pressTime = 0;
            setBlockActive(false);
        }
    }

    public ItemStack getInputStack() {
        if (!inventory.get(0).isEmpty()) {
            PressingRecipe recipe = world.getRecipeManager().getFirstMatch(VivatechRecipes.PRESSING, this, world).orElse(null);
            return recipe != null ? recipe.getInput().getStackArray()[0].copy() : ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }


    public ItemStack getOutputStack() {
        if (!inventory.get(0).isEmpty()) {
            PressingRecipe recipe = world.getRecipeManager().getFirstMatch(VivatechRecipes.PRESSING, this, world).orElse(null);
            return recipe != null ? recipe.getOutput().copy() : ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }

    public boolean canRun() {
        ItemStack output = getOutputStack();
        if (inventory.get(0).isEmpty()
                || output.isEmpty()
                || inventory.get(1).getCount() > 64
                || energy.getCurrentEnergy() < CONSUME_PER_TICK) {
            return false;
        } else if (!inventory.get(1).isEmpty()) {
            return output.getItem() == inventory.get(1).getItem();
        }

        return true;
    }

    public void pressItem() {
        ItemStack input = getInputStack();
        ItemStack output = getOutputStack();
        if (!output.isEmpty() && !input.isEmpty()) {
            if (inventory.get(1).isEmpty()) {
                inventory.set(1, output);
            } else {
                inventory.get(1).increment(output.getCount());
            }

            inventory.get(0).decrement(input.getCount());
        }
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        pressTime = tag.getInt("PressTime");
        pressTimeTotal = tag.getInt("PressTimeTotal");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("PressTime", pressTime);
        tag.putInt("PressTimeTotal", pressTimeTotal);
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
