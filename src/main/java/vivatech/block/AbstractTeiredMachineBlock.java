package vivatech.block;

import vivatech.util.MachineTeirs;

public abstract class AbstractTeiredMachineBlock extends AbstractMachineBlock {
	
	public final MachineTeirs TEIR;

	public AbstractTeiredMachineBlock(Settings settings, MachineTeirs teir) {
		super(settings);
		TEIR = teir;
	}

}
