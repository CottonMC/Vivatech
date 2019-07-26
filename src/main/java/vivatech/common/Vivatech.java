package vivatech.common;

import io.github.cottonmc.energy.CottonEnergy;
import io.github.cottonmc.energy.api.ElectricalEnergyType;
import io.github.cottonmc.energy.api.EnergyType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.container.BlockContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import vivatech.common.block.*;
import vivatech.common.menu.*;
import vivatech.common.init.VivatechBlocks;
import vivatech.common.init.VivatechEntities;
import vivatech.common.init.VivatechItems;
import vivatech.common.init.VivatechRecipes;

public class Vivatech implements ModInitializer {
    public static final String MODID = "vivatech";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "item_group"),
            () -> new ItemStack(VivatechItems.NORMAL_MACHINE_CHASSIS));
    public static final Item.Settings ITEM_SETTINGS = new Item.Settings().group(ITEM_GROUP);
    public static final Block.Settings METALLIC_BLOCK_SETTINGS = FabricBlockSettings.copy(Blocks.IRON_BLOCK).build();
    public static final Block.Settings MACHINE_BLOCK_SETTINGS = FabricBlockSettings.copy(Blocks.IRON_BLOCK).build();
    public static final EnergyType INFINITE_VOLTAGE = new ElectricalEnergyType(Integer.MAX_VALUE);

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            Configurator.setLevel(MODID, Level.DEBUG);
        }

        VivatechRecipes.initialize();
        VivatechBlocks.initialize();
        VivatechEntities.initialize();
        VivatechItems.initialize();

        ContainerProviderRegistry.INSTANCE.registerFactory(CoalGeneratorBlock.ID, (syncId, id, player, buf) ->
                new CoalGeneratorMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(CrusherBlock.ID, (syncId, id, player, buf) ->
                new CrusherMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(ElectricFurnaceBlock.ID, (syncId, id, player, buf) ->
                new ElectricFurnaceMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(EnergyBankBlock.ID, (syncId, id, player, buf) ->
                new EnergyBankMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(PressBlock.ID, (syncId, id, player, buf) ->
                new PressMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

        Registry.register(CottonEnergy.ENERGY_REGISTRY, new Identifier(MODID, "infinite_voltage"), INFINITE_VOLTAGE);
    }
}
