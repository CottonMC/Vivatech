package vivatech.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import vivatech.api.tier.Tiered;
import vivatech.api.block.entity.Conduit;
import vivatech.api.tier.Tier;
import vivatech.common.init.VivatechEntities;
import vivatech.common.network.EnergyNetwork;

import java.util.UUID;

public class EnergyConduitBlockEntity extends BlockEntity implements Tickable, Conduit {
    public UUID networkId = null;

    public EnergyConduitBlockEntity() {
        super(VivatechEntities.ENERGY_CONDUIT);
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) return;
        if (networkId == null) networkId = new EnergyNetwork(this).getId();
    }

    // TieredBlockEntity
    @Override
    public Tier getTier() {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof Tiered) {
            return ((Tiered) block).getTier();
        } else {
            return Tier.MINIMAL;
        }
    }
}
