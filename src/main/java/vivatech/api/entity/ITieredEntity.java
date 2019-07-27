package vivatech.api.entity;

import net.minecraft.block.Block;
import vivatech.api.block.ITieredBlock;
import vivatech.api.util.BlockTier;

public interface ITieredEntity extends IBlockEntity {

    default BlockTier getTier() {
        Block block = getWorld().getBlockState(getPos()).getBlock();
        if (block instanceof ITieredBlock) {
            return ((ITieredBlock) block).getTier();
        } else {
            return BlockTier.MINIMAL;
        }
    };
}
