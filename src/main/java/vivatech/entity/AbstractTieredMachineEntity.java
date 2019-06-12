package vivatech.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import vivatech.util.MachineTier;

public abstract class AbstractTieredMachineEntity extends AbstractMachineEntity {

	public MachineTier TIER;

	public AbstractTieredMachineEntity(BlockEntityType<?> type, MachineTier tier) {
		super(type);
		TIER = tier;
	}

	public AbstractTieredMachineEntity(BlockEntityType<?> type) {
		super(type);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		TIER = MachineTier.values()[tag.getInt("Tier")];
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("Tier", TIER.ordinal());
		return tag;
	}

}
