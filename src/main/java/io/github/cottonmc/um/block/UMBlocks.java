package io.github.cottonmc.um.block;

import java.util.function.Supplier;

import io.github.cottonmc.um.UnitedManufacturing;
import io.github.cottonmc.um.block.entity.CoalGeneratorEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UMBlocks {
	public static CoalGeneratorBlock COAL_GENERATOR;
	
	public static BlockEntityType<CoalGeneratorEntity> COAL_GENERATOR_ENTITY;
	
	
	public static void init() {
		COAL_GENERATOR_ENTITY = registerType("united-manufacturing:coal_generator", CoalGeneratorEntity::new);
		
		COAL_GENERATOR = block("coal_generator", new CoalGeneratorBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
	}
	
	
	
	
	/* === REGISTRATION HELPERS === */
	
	public static <T extends Block> T technicalBlock(String name, T block) {
		Registry.register(Registry.BLOCK, new Identifier("united-manufacturing", name), block);
		return block;
	}
	
	public static <T extends Block> T block(String name, T block, ItemGroup tab) {
		Identifier id = new Identifier("united-manufacturing", name);
		
		Registry.register(Registry.BLOCK, id, block);
		BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
		Registry.register(Registry.ITEM, id, item);
		
		return block;
	}
	
	public static <T extends BlockEntity> BlockEntityType<T> registerType(String id, Supplier<T> supplier) {
		BlockEntityType<T> result = BlockEntityType.Builder.create(supplier).build(null);
		Registry.register(Registry.BLOCK_ENTITY, id, result);
		return result;
	}
}
