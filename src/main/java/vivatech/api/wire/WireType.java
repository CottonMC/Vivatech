package vivatech.api.wire;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public interface WireType {
	Registry<WireType> WIRE_TYPES = new SimpleRegistry<>();

	/**
	 * @return The RGB color of this wire.
	 */
	int getColor();

	/**
	 * @return How wide the wire should be, in 1/16ths of a block.
	 */
	float getWidth();

	/**
	 * NYI.
	 * @param player The player touching this wire.
	 */
	void onPlayerTouch(PlayerEntity player);
}
