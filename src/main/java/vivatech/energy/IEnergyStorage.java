package vivatech.energy;

import net.minecraft.nbt.CompoundTag;

public interface IEnergyStorage {
    /**
     * @return The maximum amount of energy that can be stored
     */
    int getMaxEnergy();

    /**
     * @return The current amount of energy that is stored
     */
    int getCurrentEnergy();

    /**
     * Use this to block energy insertion. Could be useful
     * for energy generators.
     * @return Whether an storage can take energy
     */
    default boolean canInsertEnergy() {
        return true;
    }

    /**
     * Use this to block energy extraction. Could be useful
     * for energy consumers.
     * @return Whether an storage can give energy
     */
    default boolean canExtractEnergy() {
        return true;
    }

    /**
     * @param amount The amount of energy to insert
     * @return Leftover amount of energy, that could not be inserted, or zero if all energy can be inserted
     */
    int insertEnergy(int amount);

    /**
     * @param amount The amount of energy to extract
     * @return Amount of extracted energy or zero if energy cannot be extracted
     */
    int extractEnergy(int amount);

    CompoundTag writeEnergyToTag(CompoundTag nbt);

    void readEnergyFromTag(CompoundTag nbt);
}
