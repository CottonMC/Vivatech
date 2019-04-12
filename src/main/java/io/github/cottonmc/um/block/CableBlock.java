package io.github.cottonmc.um.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import io.github.cottonmc.energy.api.EnergyType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CableBlock extends Block implements AttributeProvider {
	public static BooleanProperty NORTH = Properties.NORTH_BOOL;
	public static BooleanProperty EAST = Properties.EAST_BOOL;
	public static BooleanProperty SOUTH = Properties.SOUTH_BOOL;
	public static BooleanProperty WEST = Properties.WEST_BOOL;
	public static BooleanProperty UP = Properties.UP_BOOL;
	
	protected EnergyType voltageTier;
	
	
	public CableBlock(EnergyType voltageTier) {
		super(
				FabricBlockSettings
				.of(Material.METAL, DyeColor.ORANGE)
				.breakByHand(true)
				.hardness(2f)
				.resistance(4f)
				.build()
				);
		
		this.voltageTier = voltageTier;
	}
	
	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		builder.with(NORTH, EAST, SOUTH, WEST, UP);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		
		//TODO: Search for energynet changes
		
		super.onPlaced(world, pos, state, entity, stack);
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		
		//TODO: Search for energynet changes
		
		super.onBreak(world, pos, state, player);
	}
	
	
	
	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		//TODO: Add an energynet capability once known
	}
}
