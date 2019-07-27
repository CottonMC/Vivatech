package vivatech.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import vivatech.api.block.ITieredBlock;
import vivatech.api.block.entity.IConduitBlockEntity;
import vivatech.api.util.BlockTier;
import vivatech.common.init.VivatechEntities;
import vivatech.common.network.EnergyNetwork;

import java.util.UUID;

public class EnergyConduitBlockEntity extends BlockEntity implements Tickable, IConduitBlockEntity {
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

    // ITieredEntity
    @Override
    public BlockTier getTier() {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ITieredBlock) {
            return ((ITieredBlock) block).getTier();
        } else {
            return BlockTier.MINIMAL;
        }
    }
}
