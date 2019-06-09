package vivatech.util;

import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.block.AbstractTieredMachineBlock;

public class TierHelper {

	public static Identifier getTieredID(Identifier id, MachineTiers tier) {
		return new Identifier(id.getNamespace(), tier.getAffix() + "_" + id.getPath());
	}

	public static void registerTieredBlocks(AbstractTieredMachineBlock... blocks) {
		for(int i = 0; i < blocks.length; i++) {
			AbstractTieredMachineBlock block = blocks[i];
			Registry.register(Registry.BLOCK, block.getTieredID(), block);
		}
	}
}