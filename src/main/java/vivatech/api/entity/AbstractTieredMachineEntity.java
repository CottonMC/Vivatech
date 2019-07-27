package vivatech.api.entity;

import net.minecraft.block.entity.BlockEntityType;

public abstract class AbstractTieredMachineEntity extends AbstractMachineEntity implements ITieredEntity {
	public AbstractTieredMachineEntity(BlockEntityType<?> type) {
		super(type);
	}
}
