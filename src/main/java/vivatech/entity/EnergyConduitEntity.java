package vivatech.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import vivatech.init.VivatechEntities;
import vivatech.network.EnergyNetwork;

public class EnergyConduitEntity extends BlockEntity implements Tickable {
    private boolean tickedOnce = false;
    public long networkId;

    public EnergyConduitEntity() {
        super(VivatechEntities.ENERGY_CONDUIT);
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) return;

        if (!tickedOnce) {
            EnergyNetwork.addConduit(this);
            tickedOnce = true;
        }
    }
}
