package vivatech.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import vivatech.common.init.*;

public class Vivatech implements ModInitializer {
    public static final String MOD_ID = "vivatech";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "item_group"),
        () -> new ItemStack(VivatechItems.NORMAL_MACHINE_CHASSIS));
    public static final Block.Settings METALLIC_BLOCK_SETTINGS = Block.Settings.copy(Blocks.IRON_BLOCK);
    public static final Block.Settings MACHINE_BLOCK_SETTINGS = Block.Settings.copy(Blocks.IRON_BLOCK);

    public static Item.Settings getSettings() {
        return new Item.Settings().group(ITEM_GROUP);
    }

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Configurator.setLevel(MOD_ID, Level.DEBUG);
        }

        VivatechRecipes.initialize();
        VivatechBlocks.initialize();
        VivatechEntities.initialize();
        VivatechItems.initialize();
        VivatechMenus.initialize();
    }
}
