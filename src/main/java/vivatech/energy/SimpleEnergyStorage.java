package vivatech.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;

public class SimpleEnergyStorage implements IEnergyStorage {
    protected int maxEnergy;
    protected int currentEnergy;

    public SimpleEnergyStorage(int maxEnergy) {
        this(maxEnergy, 0);
    }

    public SimpleEnergyStorage(int maxEnergy, int initialEnergy) {
        this.maxEnergy = maxEnergy;
        currentEnergy = initialEnergy;
    }

    @Override
    public int getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public void setMaxEnergy(int amount) {
        maxEnergy = amount;
    }

    @Override
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    @Override
    public void setCurrentEnergy(int amount) {
        currentEnergy = amount;
    }

    @Override
    public int giveEnergy(int amount) {
        if (currentEnergy + amount <= maxEnergy) {
            currentEnergy += amount;
            amount = 0;
        } else {
            amount -= maxEnergy - currentEnergy;
            currentEnergy = maxEnergy;
        }
        return amount;
    }

    @Override
    public int takeEnergy(int amount) {
        if (amount <= currentEnergy) {
            currentEnergy -= amount;
        } else {
            amount = currentEnergy;
            currentEnergy = 0;
        }
        return amount;
    }

    @Override
    public void emitEnergy(IWorld world, BlockPos pos) {
        if (canGiveEnergy() && !world.isClient()) {
            for (Direction direction : Direction.values()) {
                BlockPos offsetPos = pos.offset(direction);
                if (world.getBlockEntity(offsetPos) instanceof IEnergyHolder) {
                    IEnergyStorage storage = ((IEnergyHolder) world.getBlockEntity(offsetPos)).getEnergyStorage();
                    if (storage.canTakeEnergy()) {
                        int energySent = storage.giveEnergy(getCurrentEnergy());
                        currentEnergy -= energySent;
                    }
                }
            }
        }

    }

    @Override
    public CompoundTag writeEnergyToTag(CompoundTag tag) {
        tag.putInt("currentEnergy", currentEnergy);
        tag.putInt("maxEnergy", maxEnergy);
        return tag;
    }

    @Override
    public void readEnergyFromTag(CompoundTag tag) {
        currentEnergy = tag.getInt("currentEnergy");
        maxEnergy = tag.getInt("maxEnergy");
    }
}
