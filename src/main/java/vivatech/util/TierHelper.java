package vivatech.util;

import java.lang.reflect.InvocationTargetException;

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
	
	public static AbstractTieredMachineBlock[] fillTieredBlockArray(Class<? extends AbstractTieredMachineBlock> clazz, AbstractTieredMachineBlock[] array) {
		for(MachineTier tier : MachineTier.values()) {
			try {
				array[tier.ordinal()] = clazz.getConstructor(MachineTier.class).newInstance(tier);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		return array;
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