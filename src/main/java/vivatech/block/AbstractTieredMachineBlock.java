package vivatech.block;

import vivatech.util.MachineTiers;

public abstract class AbstractTieredMachineBlock extends AbstractMachineBlock {
	
	public final MachineTiers TIER;

	public AbstractTieredMachineBlock(Settings settings, MachineTiers tier) {
		super(settings);
		TIER = tier;
	}

}
