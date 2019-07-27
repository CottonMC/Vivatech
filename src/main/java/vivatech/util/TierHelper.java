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
import vivatech.api.block.ITieredBlock;
import vivatech.common.Vivatech;
import vivatech.api.block.AbstractTieredMachineBlock;
import vivatech.api.util.BlockTier;

public class TierHelper {

	public static Identifier getTieredID(Identifier id, BlockTier tier) {
		return new Identifier(id.getNamespace(), tier.getAffix() + "_" + id.getPath());
	}
	
	public static <T extends ITieredBlock> ImmutableList<T> fillTieredBlockArray(Function<BlockTier, T> function) {
		List<T> blocks = new ArrayList<T>();
	    for(int i = 0; i < BlockTier.values().length; i++) {
	    	blocks.add(i, function.apply(BlockTier.values()[i]));
	    }
	    return ImmutableList.copyOf(blocks);
	}
	
	public static Item[] fillTieredBlockItemArray(ImmutableList<? extends ITieredBlock> blockArray) {
		Item[] items = new Item[BlockTier.values().length];
		for(BlockTier tier : BlockTier.values()) {
			items[tier.ordinal()] = new BlockItem((Block) blockArray.get(tier.ordinal()), Vivatech.ITEM_SETTINGS);
		}
		return items;
	}

	public static void registerTieredBlocks(ImmutableList<? extends ITieredBlock> blocks) {
		for(int i = 0; i < blocks.size(); i++) {
			ITieredBlock block = blocks.get(i);
			Registry.register(Registry.BLOCK, block.getTieredId(), (Block) block);
		}
	}
	
	public static void registerTieredBlockItems(ImmutableList<? extends ITieredBlock> blocks, Item[] items) {
		for(int i = 0; i < items.length; i++) {
			Registry.register(Registry.ITEM, blocks.get(i).getTieredId(), items[i]);
		}
	}
}