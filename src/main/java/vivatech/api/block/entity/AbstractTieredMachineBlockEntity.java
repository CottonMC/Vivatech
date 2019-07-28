package vivatech.api.block.entity;

import net.minecraft.block.entity.BlockEntityType;

public abstract class AbstractTieredMachineBlockEntity extends AbstractMachineBlockEntity implements ITieredBlockEntity {
	public AbstractTieredMachineBlockEntity(BlockEntityType<?> type) {
		super(type);
	}
}
