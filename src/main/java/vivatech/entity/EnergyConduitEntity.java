package vivatech.entity;

import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import vivatech.init.VivatechEntities;
import vivatech.util.EnergyHelper;

public class EnergyConduitEntity extends BlockEntity implements Tickable {

    protected static final int TRANSFER_PER_TICK = 4;
    protected SimpleEnergyAttribute energy = new SimpleEnergyAttribute(20);

    public EnergyConduitEntity() {
        super(VivatechEntities.ENERGY_CONDUIT);
    }

    public EnergyAttribute getEnergy() {
        return energy;
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        energy.fromTag(tag.getTag("Energy"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Energy", energy.toTag());
        return tag;
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) return;

        if (energy.getCurrentEnergy() != 0) {
            EnergyHelper.emit(energy, world, pos, TRANSFER_PER_TICK);
            markDirty();
            world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
    }
}
