package vivatech.util;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EnergyHelper {
    public static void emit(EnergyAttribute energy, World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            emit(direction, energy, world, pos);
        }
    }

    public static void emit(Direction direction, EnergyAttribute energy, World world, BlockPos pos) {
        if (energy.getCurrentEnergy() == 0) return;

        AttributeList<EnergyAttribute> attributes = EnergyAttribute.ENERGY_ATTRIBUTE.getAll(world, pos.offset(direction));
        for (int i = 0; i < attributes.getCount(); i++) {
            EnergyAttribute attribute = attributes.get(i);
            if (!attribute.canInsertEnergy()) continue;
            if (energy.getPreferredType().isCompatibleWith(attribute.getPreferredType())
                    || attribute.getPreferredType().isCompatibleWith(energy.getPreferredType())) {
                // One end or the other seems to think that the energy is compatible.
                int transferSize = Math.min(energy.getPreferredType().getMaximumTransferSize(), energy.getCurrentEnergy());

                transferSize = transferSize - attribute.insertEnergy(energy.getPreferredType(), transferSize, Simulation.SIMULATE);
                transferSize = energy.extractEnergy(energy.getPreferredType(), transferSize, Simulation.ACTION); //Transfer actually starts here
                int notTransferred = attribute.insertEnergy(energy.getPreferredType(), transferSize, Simulation.ACTION);
                if (notTransferred > 0) {
                    //Complain loudly but keep working
                    new RuntimeException("Misbehaving EnergyAttribute " + attribute.getClass().getCanonicalName()
                            + " accepted energy in SIMULATE then didn't accept the same or less energy in PERFORM").printStackTrace();
                    energy.insertEnergy(energy.getPreferredType(), notTransferred, Simulation.ACTION);
                }
            }
        }
    }
}
