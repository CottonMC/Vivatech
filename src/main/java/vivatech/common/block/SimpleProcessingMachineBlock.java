package vivatech.common.block;

import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;
import vivatech.api.block.AbstractProcessingMachineBlock;
import vivatech.api.block.entity.AbstractProcessingMachineBlockEntity;
import vivatech.api.tier.Tier;

import java.util.function.Function;

public class SimpleProcessingMachineBlock extends AbstractProcessingMachineBlock {
    public SimpleProcessingMachineBlock(Identifier id, Tier tier, Identifier tieredId, Function<BlockView, AbstractProcessingMachineBlockEntity> blockEntityFactory) {
        super(id, tier, tieredId, blockEntityFactory);
    }
}
