package vivatech.energy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface IEnergyStorage {
    /**
     * @return The maximum amount of energy that can be stored
     */
    int getMaxEnergy();

    /**
     * @param amount The maximum amount of energy that can be stored
     */
    void setMaxEnergy(int amount);

    /**
     * @return The current amount of energy that is stored
     */
    int getCurrentEnergy();

    /**
     * @param amount The current amount of energy that is stored
     */
    void setCurrentEnergy(int amount);

    /**
     * Use this to prevent energy insertion. Could be useful
     * for energy generators.
     * @return Whether an storage can take energy
     */
    default boolean canTakeEnergy() {
        return true;
    }

    /**
     * Use this to prevent energy extraction. Could be useful
     * for energy consumers.
     * @return Whether an storage can give energy
     */
    default boolean canGiveEnergy() {
        return true;
    }

    /**
     * @param amount The amount of energy to insert
     * @return Leftover amount of energy, that could not be inserted, or zero if all energy can be inserted
     */
    int giveEnergy(int amount);

    /**
     * @param amount The amount of energy to extract
     * @return Amount of extracted energy or zero if energy cannot be extracted
     */
    int takeEnergy(int amount);

    /**
     * Finds first neighbour accepting energy and starts to insert to it energy
     * @param world The world where storage is located
     * @param pos The position of the storage
     */
    void emitEnergy(IWorld world, BlockPos pos);

    CompoundTag writeEnergyToTag(CompoundTag nbt);

    void readEnergyFromTag(CompoundTag nbt);
}
