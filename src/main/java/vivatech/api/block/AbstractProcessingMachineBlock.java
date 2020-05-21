package vivatech.api.block;

import net.minecraft.util.Identifier;
import vivatech.api.tier.Tier;

public abstract class AbstractProcessingMachineBlock extends AbstractTieredMachineBlock {
    public AbstractProcessingMachineBlock(Settings settings, Identifier id, Tier tier) {
        super(settings, id, tier);
    }
}
