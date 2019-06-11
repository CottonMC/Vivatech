package vivatech.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

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
	
	public static <T extends AbstractTieredMachineBlock> ImmutableList<T> fillTieredBlockArray(Function<MachineTier, T> function) {
		List<T> blocks = new ArrayList<T>();
	    for(int i = 0; i < MachineTier.values().length; i++) {
	    	blocks.add(i, function.apply(MachineTier.values()[i]));
	    }
	    return ImmutableList.copyOf(blocks);
	}
	
	public static Item[] fillTieredBlockItemArray(ImmutableList<? extends AbstractTieredMachineBlock> blockArray) {
		Item[] items = new Item[MachineTier.values().length];
		for(MachineTier tier : MachineTier.values()) {
			items[tier.ordinal()] = new BlockItem(blockArray.get(tier.ordinal()), Vivatech.ITEM_SETTINGS);
		}
		return items;
	}

	public static void registerTieredBlocks(ImmutableList<? extends AbstractTieredMachineBlock> blocks) {
		for(int i = 0; i < blocks.size(); i++) {
			AbstractTieredMachineBlock block = blocks.get(i);
			Registry.register(Registry.BLOCK, block.getTieredID(), block);
		}
	}
	
	public static void registerTieredBlockItems(ImmutableList<? extends AbstractTieredMachineBlock> blocks, Item[] items) {
		for(int i = 0; i < items.length; i++) {
			Registry.register(Registry.ITEM, blocks.get(i).getTieredID(), items[i]);
		}
	}
}