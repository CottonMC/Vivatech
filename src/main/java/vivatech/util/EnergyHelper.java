package vivatech.util;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;
import vivatech.Vivatech;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EnergyHelper {
    public static void emit(EnergyAttribute from, World world, BlockPos pos) {
        emit(from, world, pos, from.getCurrentEnergy());
    }

    public static void emit(EnergyAttribute from, World world, BlockPos pos, int transferSize) {
        if (from.getCurrentEnergy() == 0) return;

        List<EnergyAttribute> tos = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            AttributeList<EnergyAttribute> attributes = EnergyAttribute.ENERGY_ATTRIBUTE.getAll(world, pos.offset(direction));
            for (int i = 0; i < attributes.getCount(); i++) tos.add(attributes.get(i));
        }

        if (tos.size() == 0) return;
        if (transferSize % tos.size() != 0) return;

        AtomicInteger finalTransferSize = new AtomicInteger(transferSize / tos.size());
        tos.forEach(to -> transfer(from, to, finalTransferSize.get()));
    }

    public static void transfer(EnergyAttribute from, EnergyAttribute to, int transferSize) {
        if (!to.canInsertEnergy()
                || !to.getPreferredType().isCompatibleWith(Vivatech.INFINITE_VOLTAGE)
                || to.getCurrentEnergy() == to.getMaxEnergy()) {
            return;
        }

        int leftover = to.insertEnergy(from.getPreferredType(), transferSize, Simulation.ACTION);
        from.extractEnergy(from.getPreferredType(), transferSize - leftover, Simulation.ACTION);
    }
}
