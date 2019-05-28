package vivatech.entity;

import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import vivatech.Vivatech;
import vivatech.energy.EnergyAttributeProvider;
import vivatech.init.VivatechEntities;

import javax.annotation.Nullable;

public class ElectricFurnaceEntity extends BlockEntity implements Tickable, SidedInventory, PropertyDelegateHolder, EnergyAttributeProvider {

    private final int consumePerTick = 2;
    private int smeltTime = 0;
    private int smeltTimeTotal = 0;
    private final int invSize = 2;
    private DefaultedList<ItemStack> inventory = DefaultedList.create(invSize, ItemStack.EMPTY);
    private SimpleEnergyAttribute energy = new SimpleEnergyAttribute(100, Vivatech.ENERGY) {
        @Override
        public boolean canExtractEnergy() { return false; }
    };
    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int propertyId) {
            switch (propertyId) {
                case 0: // Current Energy
                    return energy.getCurrentEnergy();
                case 1: // Max Energy
                    return energy.getMaxEnergy();
                case 2: // Smelt Time
                    return smeltTime;
                case 3: // Smelt Time Total
                    return smeltTimeTotal;
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
                case 2: // Smelt Time
                    smeltTime = value;
                    break;
                case 3: // Smelt Time Total
                    smeltTimeTotal = value;
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

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        energy.fromTag(tag.getTag("Energy"));
        smeltTime = tag.getInt("SmeltTime");
        smeltTimeTotal = tag.getInt("SmeltTimeTotal");
        inventory = DefaultedList.create(invSize, ItemStack.EMPTY);
        Inventories.fromTag(tag, inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Energy", energy.toTag());
        tag.putInt("SmeltTime", smeltTime);
        tag.putInt("SmeltTimeTotal", smeltTimeTotal);
        Inventories.toTag(tag, inventory);
        return tag;
    }

    // Tickable
    @Override
    public void tick() {
        if (canRun()) {
            smeltTime++;
            energy.extractEnergy(Vivatech.ENERGY, consumePerTick, Simulation.ACTION);
            if (smeltTime >= 120) {
                smeltTime = 0;
                smeltItem();
                world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
            }
        } else if (!canRun() && smeltTime > 0) {
            smeltTime = 0;
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
        if (inventory.get(0).isEmpty() || output.isEmpty() || inventory.get(1).getAmount() > 64
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

    // SidedInventory
    @Override
    public int[] getInvAvailableSlots(Direction direction) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertInvStack(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return slot != 1;
    }

    @Override
    public boolean canExtractInvStack(int slot, ItemStack itemStack, Direction direction) {
        return slot != 0;
    }

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
