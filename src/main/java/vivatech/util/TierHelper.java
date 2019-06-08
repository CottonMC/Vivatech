package vivatech.util;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TierHelper {
	
	public static void registerTeiredItems(Item[] itemArray, Identifier id) {
		for (int i = 0; MachineTiers.values().length < i; i++) {
			Registry.register(Registry.ITEM, new Identifier(id.getNamespace(), id.getPath() + MachineTiers.values()[i]), itemArray[i]);
		}
	}
}
