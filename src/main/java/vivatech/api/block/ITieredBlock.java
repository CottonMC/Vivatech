package vivatech.api.block;

import net.minecraft.util.Identifier;
import vivatech.api.util.BlockTier;

public interface ITieredBlock {
    Identifier getTieredId();

    BlockTier getTier();
}
