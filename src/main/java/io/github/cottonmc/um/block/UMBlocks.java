package io.github.cottonmc.um.block;

import java.util.function.Supplier;

import io.github.cottonmc.um.UnitedManufacturing;
import io.github.cottonmc.um.block.entity.*;
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
	public static RollerBlock ROLLER;
	public static DieCutterBlock DIE_CUTTER;
	public static ConveyorBlock CONVEYOR;
	public static ChannelBlock CHANNEL;

	public static BlockEntityType<CoalGeneratorEntity> COAL_GENERATOR_ENTITY;
	public static BlockEntityType<HammerMillEntity> HAMMER_MILL_ENTITY;
	public static BlockEntityType<RollerEntity> ROLLER_ENTITY;
	public static BlockEntityType<DieCutterEntity> DIE_CUTTER_ENTITY;
	public static BlockEntityType<ConveyorEntity> CONVEYOR_ENTITY;
	public static BlockEntityType<ChannelEntity> CHANNEL_ENTITY;
	
	public static void init() {
		COAL_GENERATOR = block("coal_generator", new CoalGeneratorBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
		HAMMER_MILL = block("hammer_mill", new HammerMillBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
		ROLLER = block("roller", new RollerBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
		DIE_CUTTER = block("die_cutter", new DieCutterBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
		CONVEYOR = block("conveyor", new ConveyorBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);
		CHANNEL = block("channel", new ChannelBlock(), UnitedManufacturing.ITEMGROUP_MACHINES);

		COAL_GENERATOR_ENTITY = registerType("coal_generator", CoalGeneratorEntity::new);
		HAMMER_MILL_ENTITY = registerType("hammer_mill", HammerMillEntity::new);
		ROLLER_ENTITY = registerType("roller", RollerEntity::new);
		DIE_CUTTER_ENTITY = registerType("die_cutter", DieCutterEntity::new);
		CONVEYOR_ENTITY = registerType("conveyor", ConveyorEntity::new);
		CHANNEL_ENTITY = registerType("channel", ChannelEntity::new);
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
