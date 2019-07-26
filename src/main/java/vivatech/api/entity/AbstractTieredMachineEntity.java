package vivatech.api.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import vivatech.api.block.AbstractTieredMachineBlock;
import vivatech.api.util.MachineTier;

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
