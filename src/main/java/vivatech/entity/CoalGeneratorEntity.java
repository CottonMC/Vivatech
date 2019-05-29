package vivatech.entity;

import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Direction;
import vivatech.Vivatech;
import vivatech.block.AbstractMachineBlock;
import vivatech.init.VivatechEntities;
import vivatech.util.Flag;
import vivatech.util.EnergyHelper;

import javax.annotation.Nullable;

public class CoalGeneratorEntity extends AbstractMachineEntity {

    private final int generatePerTick = 1;
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


    public CoalGeneratorEntity() {
        super(VivatechEntities.COAL_GENERATOR);
    }

    // AbstractMachineEntity
    @Override
    protected int getMaxEnergy() {
        return 10_000;
    }

    @Override
    protected boolean canInsertEnergy() {
        return false;
    }

    @Override
    protected boolean canExtractEnergy() {
        return true;
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

    // Tickable
    @Override
    public void tick() {
        if (energy.getCurrentEnergy() < energy.getMaxEnergy()) {
            if (burnTime > 0) {
                burnTime -= 1;
                if (burnTime == 0) burnTimeTotal = 0;
                energy.insertEnergy(Vivatech.ENERGY, generatePerTick, Simulation.ACTION);
            } else if (inventory.get(0).getAmount() > 0) {
                burnTime = FurnaceBlockEntity.createFuelTimeMap().getOrDefault(inventory.get(0).getItem(), 0) / 2;
                burnTimeTotal = burnTime;
                inventory.get(0).subtractAmount(1);
                world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), Flag.NOTIFY_CLIENT_AND_NEIGHBOURS);
            }
        }
        world.setBlockState(pos, world.getBlockState(pos).with(AbstractMachineBlock.ACTIVE, burnTime != 0),
                Flag.NOTIFY_CLIENT_AND_NEIGHBOURS);
        if (energy.getCurrentEnergy() != 0) EnergyHelper.emit(energy, world, pos);
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

    // EnergyAttributeProvider
    @Override
    public EnergyAttribute getEnergy() {
        return energy;
    }
}
