package vivatech.entity;

import net.minecraft.block.entity.BlockEntityType;
import vivatech.util.MachineTier;

public abstract class AbstractTieredMachineEntity extends AbstractMachineEntity {

	public MachineTier TIER;
	
	public AbstractTieredMachineEntity(BlockEntityType<?> type, MachineTier tier) {
		super(type);
		TIER = tier;
	}
	
	public AbstractTieredMachineEntity(BlockEntityType<?> type) {
		super(type);
	}

}
