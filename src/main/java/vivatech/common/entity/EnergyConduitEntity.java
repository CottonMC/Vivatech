package vivatech.common.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import vivatech.api.entity.ITieredEntity;
import vivatech.common.init.VivatechEntities;
import vivatech.common.network.EnergyNetwork;

import java.util.UUID;

public class EnergyConduitEntity extends BlockEntity implements Tickable, ITieredEntity {
    private static final int BASE_TRANSFER_RATE = 50;
    public UUID networkId = null;

    public EnergyConduitEntity() {
        super(VivatechEntities.ENERGY_CONDUIT);
    }

    public int getTransferRate() {
        return (int) (BASE_TRANSFER_RATE * getTier().getEnergyMultiplier());
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) return;
        if (networkId == null) networkId = new EnergyNetwork(this).getId();
    }
}
