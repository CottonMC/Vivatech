package io.github.cottonmc.um.block;

import java.util.function.Supplier;

import io.github.cottonmc.um.UnitedManufacturing;
import io.github.cottonmc.um.block.entity.CoalGeneratorEntity;
import io.github.cottonmc.um.block.entity.ConveyorEntity;
import io.github.cottonmc.um.block.entity.HammerMillEntity;
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
	public static HammerMillBlock HAMMER_MILL;
	public static ConveyorBlock CONVEYOR;

	public static BlockEntityType<CoalGeneratorEntity> COAL_GENERATOR_ENTITY;
	public static BlockEntityType<HammerMillEntity> HAMMER_MILL_ENTITY;
	public static BlockEntityType<ConveyorEntity> CONVEYOR_ENTITY;
	
	public static void init() {
		COAL_GENERATOR_ENTITY = registerType("coal_generator", CoalGeneratorEntity::new);
		HAMMER_MILL_ENTITY = registerType("hammer_mill", HammerMillEntity::new);
		CONVEYOR_ENTITY = registerType("conveyor", ConveyorEntity::new);
		
		COAL_GENERATOR = block("coal_generator", new CoalGeneratorBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
		HAMMER_MILL = block("hammer_mill", new HammerMillBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
		CONVEYOR = block("conveyor", new ConveyorBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
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
		Registry.register(Registry.BLOCK_ENTITY, "united-manufacturing:"+id, result);
		return result;
	}
}
