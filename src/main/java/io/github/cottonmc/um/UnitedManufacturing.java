package io.github.cottonmc.um;

import io.github.cottonmc.um.block.CoalGeneratorBlock;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.block.container.CoalGeneratorController;
import io.github.cottonmc.um.item.UMItems;
import io.github.cottonmc.um.recipe.UMRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class UnitedManufacturing implements ModInitializer {
	public static ItemGroup ITEMGROUP_MACHINES = FabricItemGroupBuilder.build(new Identifier("united-manufacturing","machines"), ()->new ItemStack(UMBlocks.COAL_GENERATOR.getItem()));
	public static ItemGroup ITEMGROUP_TOOLS = FabricItemGroupBuilder.build(new Identifier("united-manufacturing","tools"), ()->new ItemStack(UMItems.TASER));
	public static Identifier CONTAINER_COAL_GENERATOR = new Identifier("united-manufacturing", "coal-generator");
	
	@Override
	public void onInitialize() {
		UMBlocks.init();
		UMItems.init();
		UMRecipes.init();
		
		
		ContainerProviderRegistry.INSTANCE.registerFactory(CoalGeneratorBlock.ID, (syncId, id, player, buf) -> new CoalGeneratorController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
	}
	
	
}
