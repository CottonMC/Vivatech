package vivatech.entity;

import net.minecraft.block.entity.BlockEntityType;
import vivatech.util.MachineTiers;

public abstract class AbstractTieredMachineEntity extends AbstractMachineEntity {

	public MachineTiers TIER;
	
	public AbstractTieredMachineEntity(BlockEntityType<?> type, MachineTiers tier) {
		super(type);
		TIER = tier;
	}
	
	public AbstractTieredMachineEntity(BlockEntityType<?> type) {
		super(type);
	}

}
