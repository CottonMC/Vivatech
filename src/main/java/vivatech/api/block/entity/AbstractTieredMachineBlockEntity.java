package vivatech.api.block.entity;

import io.github.cottonmc.energy.api.EnergyType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import vivatech.api.tier.Tier;
import vivatech.api.tier.Tiered;

public abstract class AbstractTieredMachineBlockEntity extends AbstractMachineBlockEntity implements Tiered {
	public AbstractTieredMachineBlockEntity(BlockEntityType<?> type, EnergyType energyType) {
		super(type, energyType);

	}

	@Override
	public Tier getTier() {
		Block block = this.getWorld().getBlockState(this.getPos()).getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getTier();
		} else {
			return Tier.MINIMAL;
		}
	}

	@Override
	public Identifier getTierId() {
		Block block = this.getWorld().getBlockState(this.getPos()).getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getTierId();
		} else {
			return null;
		}
	}
}
