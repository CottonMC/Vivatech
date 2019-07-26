package vivatech.common.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import vivatech.api.entity.IConduit;
import vivatech.common.init.VivatechEntities;
import vivatech.common.network.EnergyNetwork;

import java.util.UUID;

public class EnergyConduitEntity extends BlockEntity implements Tickable, IConduit {
    private static final int TRANSFER_PER_TICK = 100;
    public UUID networkId = null;

    public EnergyConduitEntity() {
        super(VivatechEntities.ENERGY_CONDUIT);
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) return;
        if (networkId == null) networkId = new EnergyNetwork(this).getId();
    }

    // IConduit
    @Override
    public int getTransferRate() {
        return TRANSFER_PER_TICK;
    }
}
