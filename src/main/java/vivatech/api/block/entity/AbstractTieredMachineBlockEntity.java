package vivatech.api.block.entity;

import io.github.cottonmc.energy.api.EnergyType;
import net.minecraft.block.entity.BlockEntityType;

public abstract class AbstractTieredMachineBlockEntity extends AbstractMachineBlockEntity implements TieredBlockEntity {
	public AbstractTieredMachineBlockEntity(BlockEntityType<?> type, EnergyType energyType) {
		super(type, energyType);

	}
}
