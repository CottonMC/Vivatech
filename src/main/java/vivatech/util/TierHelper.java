package vivatech.util;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block.Settings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.AbstractTieredMachineBlock;

public class TierHelper {

	public static Identifier getTieredID(Identifier id, MachineTiers tier) {
		return new Identifier(id.getNamespace(), tier.getAffix() + "_" + id.getPath());
	}
	
	public static AbstractTieredMachineBlock[] getBlockArray(Map<MachineTiers, ? extends AbstractTieredMachineBlock> map) {
		return map.values().toArray(new AbstractTieredMachineBlock[MachineTiers.values().length]);
	}

	public static void registerTieredBlockMap(Map<MachineTiers, ? extends AbstractTieredMachineBlock> map) {
		for(MachineTiers tier : MachineTiers.values()) {
			AbstractTieredMachineBlock block = map.get(tier);
			Registry.register(Registry.BLOCK, block.getTieredID(), block);
		}
	}

	public static Map<MachineTiers, Item> fillTieredBlockItemMap(Map<MachineTiers, ? extends AbstractTieredMachineBlock> blockMap) {
		Map<MachineTiers, Item> map = new LinkedHashMap<MachineTiers, Item>();
		for (MachineTiers tier : MachineTiers.values()) {
			map.put(tier, new BlockItem(blockMap.get(tier), Vivatech.ITEM_SETTINGS));
		}
		return map;
	}

	public static void registerTieredItems(Map<MachineTiers, Item> itemSet, Identifier id) {
		for (MachineTiers tier : MachineTiers.values()) {
			Registry.register(Registry.ITEM, getTieredID(id, tier), itemSet.get(tier));
		}
	}
}