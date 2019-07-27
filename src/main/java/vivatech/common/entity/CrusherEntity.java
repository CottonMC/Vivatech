package vivatech.common.entity;

import javax.annotation.Nullable;

import alexiil.mc.lib.attributes.Simulation;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import vivatech.common.Vivatech;
import vivatech.api.entity.AbstractTieredMachineEntity;
import vivatech.common.init.VivatechEntities;
import vivatech.common.init.VivatechRecipes;
import vivatech.common.recipe.CrushingRecipe;
import vivatech.api.util.BlockTier;

public class CrusherEntity extends AbstractTieredMachineEntity {

    public static final int CONSUME_PER_TICK = 1;
    public static final int TICK_PER_CONSUME = 5;
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
            crushTime++;
            if (crushTime % TICK_PER_CONSUME == 0) energy.extractEnergy(Vivatech.INFINITE_VOLTAGE, CONSUME_PER_TICK * (int)tier.getSpeedMultiplier(), Simulation.ACTION);
            if (crushTimeTotal == 0) {
                crushTimeTotal = (int) (world.getRecipeManager().getFirstMatch(VivatechRecipes.CRUSHING, this, world)
                        .map(CrushingRecipe::getProcessTime).orElse(200) / tier.getSpeedMultiplier());
            }
            setBlockActive(true);
            if (crushTime >= crushTimeTotal) {
                crushTime = 0;
                crushTimeTotal = 0;
                crushItem();
                if (inventory.get(0).getCount() == 0) {
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
            CrushingRecipe recipe = world.getRecipeManager().getFirstMatch(VivatechRecipes.CRUSHING, this, world).orElse(null);
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

    public void crushItem() {
        ItemStack output = getOutputStack();
        if (!output.isEmpty()) {
            if (inventory.get(1).isEmpty()) {
                inventory.set(1, output);
            } else {
                inventory.get(1).increment(1);
            }

            inventory.get(0).decrement(1);
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
