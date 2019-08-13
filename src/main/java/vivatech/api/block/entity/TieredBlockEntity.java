package vivatech.api.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import vivatech.api.tier.Tiered;
import vivatech.api.tier.Tier;

/**
 * WARNING: Only apply on BlockEntity!
 */
public interface TieredBlockEntity {
    default Tier getTier() {
        BlockEntity self = (BlockEntity) this;
        Block block = self.getWorld().getBlockState(self.getPos()).getBlock();
        if (block instanceof Tiered) {
            return ((Tiered) block).getTier();
        } else {
            return Tier.MINIMAL;
        }
    }
}
