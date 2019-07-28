package vivatech.api.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import vivatech.api.block.ITieredBlock;
import vivatech.api.util.BlockTier;

/**
 * WARNING: Only apply on BlockEntity!
 */
public interface ITieredBlockEntity {
    default BlockTier getTier() {
        BlockEntity self = (BlockEntity) this;
        Block block = self.getWorld().getBlockState(self.getPos()).getBlock();
        if (block instanceof ITieredBlock) {
            return ((ITieredBlock) block).getTier();
        } else {
            return BlockTier.MINIMAL;
        }
    };
}
