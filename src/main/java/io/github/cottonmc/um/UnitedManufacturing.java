package io.github.cottonmc.um;

import io.github.cottonmc.um.block.UMBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class UnitedManufacturing implements ModInitializer {
	public static ItemGroup ITEMGROUP_MACHINES = FabricItemGroupBuilder.build(new Identifier("united-manufacturing","machines"), ()->new ItemStack(UMBlocks.COAL_GENERATOR.getItem()));
	
	@Override
	public void onInitialize() {
		UMBlocks.init();
	}
	
	
}
