package vivatech.api.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import vivatech.api.tier.Tier;
import vivatech.api.tier.Tiered;

public abstract class AbstractTieredMachineBlockEntity extends AbstractMachineBlockEntity implements Tiered {
	public AbstractTieredMachineBlockEntity(BlockEntityType<?> type) {
		super(type);

	}

	// Tiered
	@Override
	public Tier getTier() {
		Block block = getCachedState().getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getTier();
		} else {
			return Tier.MINIMAL;
		}
	}

	@Override
	public Identifier getTieredId() {
		Block block = getCachedState().getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getTieredId();
		} else {
			return null;
		}
	}

	@Override
	public Identifier getBaseId() {
		Block block = getCachedState().getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getBaseId();
		} else {
			return null;
		}
	}
}
