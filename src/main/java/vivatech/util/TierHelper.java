package vivatech.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.api.tier.Tiered;
import vivatech.common.Vivatech;
import vivatech.api.tier.Tier;

public class TierHelper {

	public static Identifier getTieredID(Identifier id, Tier tier) {
		return new Identifier(id.getNamespace(), tier.getAffix() + "_" + id.getPath());
	}

	public static <T extends Tiered> ImmutableList<T> fillTieredBlockArray(Function<Tier, T> function) {
		List<T> blocks = new ArrayList<T>();
	    for(int i = 0; i < Tier.values().length; i++) {
	    	blocks.add(i, function.apply(Tier.values()[i]));
	    }
	    return ImmutableList.copyOf(blocks);
	}
	
	public static Item[] fillTieredBlockItemArray(ImmutableList<? extends Tiered> blockArray) {
		Item[] items = new Item[Tier.values().length];
		for(Tier tier : Tier.values()) {
			items[tier.ordinal()] = new BlockItem((Block) blockArray.get(tier.ordinal()), Vivatech.getSettings());
		}
		return items;
	}

	public static void registerTieredBlocks(ImmutableList<? extends Tiered> blocks) {
		for(int i = 0; i < blocks.size(); i++) {
			Tiered block = blocks.get(i);
			Registry.register(Registry.BLOCK, block.getTieredId(), (Block) block);
		}
	}
	
	public static void registerTieredBlockItems(ImmutableList<? extends Tiered> blocks, Item[] items) {
		for(int i = 0; i < items.length; i++) {
			Registry.register(Registry.ITEM, blocks.get(i).getTieredId(), items[i]);
		}
	}
}