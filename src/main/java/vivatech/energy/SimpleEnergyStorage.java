package vivatech.energy;

import net.minecraft.nbt.CompoundTag;

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
    public int getCurrentEnergy() {
        return currentEnergy;
    }

    @Override
    public int insertEnergy(int amount) {
        if (canInsertEnergy()) {
            if (currentEnergy + amount <= maxEnergy) {
                currentEnergy += amount;
                amount = 0;
            } else {
                amount -= maxEnergy - currentEnergy;
                currentEnergy = maxEnergy;
            }
        }
        return amount;
    }

    @Override
    public int extractEnergy(int amount) {
        if (canExtractEnergy()) {
            if (amount <= currentEnergy) {
                currentEnergy -= amount;
            } else {
                amount = currentEnergy;
                currentEnergy = 0;
            }
        } else {
            amount = 0;
        }
        return amount;
    }

    @Override
    public CompoundTag writeEnergyToTag(CompoundTag tag) {
        tag.putInt("currentEnergy", currentEnergy);
        tag.putInt("maxEnergy", maxEnergy);
        return tag;
    }

    @Override
    public void readEnergyFromTag(CompoundTag tag) {
        System.out.println("THE ENERGY TAG " + tag);
        currentEnergy = tag.getInt("currentEnergy");
        maxEnergy = tag.getInt("maxEnergy");
    }
}
