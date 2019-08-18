package vivatech.common.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import vivatech.api.tier.Tiered;
import vivatech.api.tier.Tier;
import vivatech.common.init.VivatechEntities;

import java.util.UUID;

public class EnergyConduitBlockEntity extends BlockEntity implements Tickable, Tiered {
    public UUID networkId = null;

    public EnergyConduitBlockEntity() {
        super(VivatechEntities.ENERGY_CONDUIT);
    }

    public int getTransferRate() {
        return getTier().getEnergyType().getMaximumTransferSize();
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) return;
    }

    @Override
    public Identifier getTierId() {
        Block block = this.getWorld().getBlockState(this.getPos()).getBlock();
        if (block instanceof Tiered) {
            return ((Tiered) block).getTierId();
        } else {
            return null;
        }
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
