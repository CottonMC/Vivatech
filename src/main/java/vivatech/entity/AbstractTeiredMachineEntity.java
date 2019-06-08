package vivatech.entity;

import net.minecraft.block.entity.BlockEntityType;
import vivatech.util.MachineTeirs;

public abstract class AbstractTeiredMachineEntity extends AbstractMachineEntity {

	public MachineTeirs TIER;
	
	public AbstractTeiredMachineEntity(BlockEntityType<?> type, MachineTeirs teir) {
		super(type);
		TIER = teir;
	}
	
	public AbstractTeiredMachineEntity(BlockEntityType<?> type) {
		super(type);
	}

}
