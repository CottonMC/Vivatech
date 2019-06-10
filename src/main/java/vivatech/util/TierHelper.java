package vivatech.util;

import java.util.function.Function;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.AbstractTieredMachineBlock;

public class TierHelper {

	public static Identifier getTieredID(Identifier id, MachineTier tier) {
		return new Identifier(id.getNamespace(), tier.getAffix() + "_" + id.getPath());
	}
	
	public static void fillTieredBlockArray(AbstractTieredMachineBlock[] blocks, Function<MachineTier, ? extends AbstractTieredMachineBlock> function) {
	    for(int i = 0; i < MachineTier.values().length; i++) {
	    	blocks[i] = function.apply(MachineTier.values()[i]);
	    }
	}
	
	public static Item[] fillTieredBlockItemArray(AbstractTieredMachineBlock[] blockArray) {
		Item[] items = new Item[MachineTier.values().length];
		for(MachineTier tier : MachineTier.values()) {
			items[tier.ordinal()] = new BlockItem(blockArray[tier.ordinal()], Vivatech.ITEM_SETTINGS);
		}
		return items;
	}

	public static void registerTieredBlocks(AbstractTieredMachineBlock... blocks) {
		for(int i = 0; i < blocks.length; i++) {
			AbstractTieredMachineBlock block = blocks[i];
			Registry.register(Registry.BLOCK, block.getTieredID(), block);
		}
	}
}