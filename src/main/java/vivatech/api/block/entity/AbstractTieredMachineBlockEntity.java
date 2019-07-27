package vivatech.api.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import vivatech.api.block.ITieredBlock;
import vivatech.api.util.BlockTier;

public abstract class AbstractTieredMachineBlockEntity extends AbstractMachineBlockEntity implements ITieredBlockEntity {
	public AbstractTieredMachineBlockEntity(BlockEntityType<?> type) {
		super(type);
	}

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
