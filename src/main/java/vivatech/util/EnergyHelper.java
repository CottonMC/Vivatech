package vivatech.util;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import vivatech.Vivatech;

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

            if (!attribute.canInsertEnergy()
                    || !attribute.getPreferredType().isCompatibleWith(Vivatech.ENERGY)
                    || attribute.getCurrentEnergy() == attribute.getMaxEnergy()) continue;

            int transferSize = energy.getCurrentEnergy();
            int leftover = attribute.insertEnergy(energy.getPreferredType(), transferSize, Simulation.ACTION);
            energy.extractEnergy(energy.getPreferredType(), transferSize - leftover, Simulation.ACTION);
        }
    }
}
