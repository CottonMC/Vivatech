package vivatech.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import vivatech.block.AbstractTieredMachineBlock;
import vivatech.util.MachineTier;

public abstract class AbstractTieredMachineEntity extends AbstractMachineEntity {

	public AbstractTieredMachineEntity(BlockEntityType<?> type) {
		super(type);
	}
	public MachineTier getMachineTier() {
		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof AbstractTieredMachineBlock) {
			return ((AbstractTieredMachineBlock)block).getMachineTier();
		} else {
			return MachineTier.MINIMAL;
		}
	}
}
