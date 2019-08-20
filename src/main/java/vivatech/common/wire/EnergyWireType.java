package vivatech.common.wire;

import net.minecraft.entity.player.PlayerEntity;
import vivatech.api.tier.Tier;
import vivatech.api.wire.WireType;

public class EnergyWireType implements WireType {
	private Tier tier;

	public EnergyWireType(Tier tier) {
		this.tier = tier;
	}

	@Override
	public int getColor() {
		//TODO: color properly
		return 0xFFFFFF;
	}

	@Override
	public float getWidth() {
		return 1;
	}

	@Override
	public void onPlayerTouch(PlayerEntity player) {

	}
}
