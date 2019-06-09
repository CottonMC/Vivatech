package vivatech.block;

import net.minecraft.util.Identifier;
import vivatech.util.MachineTier;

public abstract class AbstractTieredMachineBlock extends AbstractMachineBlock {
	
	public final MachineTier TIER;

	public AbstractTieredMachineBlock(Settings settings, MachineTier tier) {
		super(settings);
		TIER = tier;
	}
	
	public abstract Identifier getTieredID();

}
