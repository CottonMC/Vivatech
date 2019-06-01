package vivatech;

import io.github.cottonmc.energy.CottonEnergy;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.container.BlockContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.block.*;
import vivatech.controller.*;
import vivatech.energy.InfiniteEnergyType;
import vivatech.init.VivatechBlocks;
import vivatech.init.VivatechEntities;
import vivatech.init.VivatechItems;
import vivatech.init.VivatechRecipes;

public class Vivatech implements ModInitializer {
    public static final String MODID = "vivatech";
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, "item_group"),
            () -> new ItemStack(VivatechItems.MACHINE_CHASSIS));
    public static final Item.Settings ITEM_SETTINGS = new Item.Settings().itemGroup(ITEM_GROUP);
    public static final Block.Settings METALLIC_BLOCK_SETTINGS = FabricBlockSettings.copy(Blocks.IRON_BLOCK).build();
    public static final Block.Settings MACHINE_BLOCK_SETTINGS = FabricBlockSettings.copy(Blocks.IRON_BLOCK).build();
    public static final InfiniteEnergyType ENERGY = new InfiniteEnergyType();

    @Override
    public void onInitialize() {
        new VivatechRecipes().initialize();
        new VivatechBlocks().initialize();
        new VivatechEntities().initialize();
        new VivatechItems().initialize();

        ContainerProviderRegistry.INSTANCE.registerFactory(CoalGeneratorBlock.ID, (syncId, id, player, buf) ->
                new CoalGeneratorController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(CrusherBlock.ID, (syncId, id, player, buf) ->
                new CrusherController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(ElectricFurnaceBlock.ID, (syncId, id, player, buf) ->
                new ElectricFurnaceController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(EnergyBankBlock.ID, (syncId, id, player, buf) ->
                new EnergyBankController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(PressBlock.ID, (syncId, id, player, buf) ->
                new PressController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

        Registry.register(CottonEnergy.ENERGY_REGISTRY, new Identifier(MODID, "infinite_energy_type"), ENERGY);
    }
}
