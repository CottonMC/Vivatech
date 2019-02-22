package io.github.cottonmc.um.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UMItems {

	public static TaserItem TASER;

	public static void init() {
		TASER = item("taser", new TaserItem());
	}

	public static <T extends Item> T item(String name, T item) {
		Registry.register(Registry.ITEM, new Identifier("united-manufacturing", name), item);
		return item;
	}
}
