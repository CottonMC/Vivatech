package vivatech.util;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import vivatech.Vivatech;

import javax.annotation.Nullable;

public class EnergyHelper {
    public static void emit(EnergyAttribute energy, World world, BlockPos pos) {
        emit(energy, world, pos, null);
    }

    public static void emit(EnergyAttribute energy, World world, BlockPos pos, Integer transferSize) {
        for (Direction direction : Direction.values()) {
            emit(direction, energy, world, pos, transferSize);
        }
    }

    public static void emit(Direction direction, EnergyAttribute energy, World world, BlockPos pos) {
        emit(direction, energy, world, pos, null);
    }

    public static void emit(Direction direction, EnergyAttribute energy, World world, BlockPos pos, Integer transferSize) {
        if (energy.getCurrentEnergy() == 0) return;

        AttributeList<EnergyAttribute> attributes = EnergyAttribute.ENERGY_ATTRIBUTE.getAll(world, pos.offset(direction));
        for (int i = 0; i < attributes.getCount(); i++) {
            EnergyAttribute attribute = attributes.get(i);
            transfer(energy, attribute, transferSize);
        }
    }

    public static void transfer(EnergyAttribute from, EnergyAttribute to, @Nullable Integer transferSize) {
        if (!to.canInsertEnergy() || !to.getPreferredType().isCompatibleWith(Vivatech.INFINITE_VOLTAGE)
                || to.getCurrentEnergy() == to.getMaxEnergy()) {
            return;
        }

        if (transferSize == null) {
            transferSize = from.getCurrentEnergy();
        }
        int leftover = to.insertEnergy(from.getPreferredType(), transferSize, Simulation.ACTION);
        from.extractEnergy(from.getPreferredType(), transferSize - leftover, Simulation.ACTION);
    }
}
