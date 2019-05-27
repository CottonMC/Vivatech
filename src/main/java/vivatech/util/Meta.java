package vivatech.util;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;

public class Meta {
    public static final String MODID = "vivatech";
    public static final Item.Settings ITEM_SETTINGS = new Item.Settings();
    public static final Block.Settings MACHINE_BLOCK_SETTINGS = FabricBlockSettings.copy(Blocks.IRON_BLOCK).build();
}
