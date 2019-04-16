package io.github.cottonmc.um.block;

import java.util.ArrayList;

import alexiil.mc.lib.attributes.Attribute;
import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import alexiil.mc.lib.attributes.SearchOptions;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyType;
import io.github.cottonmc.um.block.entity.CableEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CableBlock extends Block implements AttributeProvider, Waterloggable, BlockEntityProvider {
	public static BooleanProperty NORTH = Properties.NORTH_BOOL;
	public static BooleanProperty EAST = Properties.EAST_BOOL;
	public static BooleanProperty SOUTH = Properties.SOUTH_BOOL;
	public static BooleanProperty WEST = Properties.WEST_BOOL;
	public static BooleanProperty UP = Properties.UP_BOOL;
	public static BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	protected EnergyType voltageTier;
	
	private final double CABLE_RADIUS = 1d;
	private final VoxelShape NODE_SHAPE  = Block.createCuboidShape(8d-CABLE_RADIUS, 0d, 8d-CABLE_RADIUS, 8d+CABLE_RADIUS, CABLE_RADIUS*2, 8d+CABLE_RADIUS);
	private final VoxelShape NORTH_SHAPE = Block.createCuboidShape(8d-CABLE_RADIUS, 0d, 0d,              8d+CABLE_RADIUS, CABLE_RADIUS*2, 8d-CABLE_RADIUS);
	private final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(8d-CABLE_RADIUS, 0d, 8d+CABLE_RADIUS, 8d+CABLE_RADIUS, CABLE_RADIUS*2, 16d);
	private final VoxelShape EAST_SHAPE  = Block.createCuboidShape(8d+CABLE_RADIUS, 0d, 8d-CABLE_RADIUS, 16d,             CABLE_RADIUS*2, 8d+CABLE_RADIUS);
	private final VoxelShape WEST_SHAPE  = Block.createCuboidShape(0d,              0d, 8d-CABLE_RADIUS, 8d-CABLE_RADIUS, CABLE_RADIUS*2, 8d+CABLE_RADIUS);
	private final VoxelShape UP_SHAPE    = Block.createCuboidShape(8d-CABLE_RADIUS, CABLE_RADIUS*2, 8d-CABLE_RADIUS, 8d+CABLE_RADIUS, 16, 8d+CABLE_RADIUS);
	
	
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
		builder.with(NORTH, EAST, SOUTH, WEST, UP, WATERLOGGED);
	}
	
	
	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be!=null && be instanceof CableEntity) {
			to.offer( ((CableEntity)be).energy );
		}
	}
	
	@Override
	public boolean hasDynamicBounds() {
		return true;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, VerticalEntityPosition entityPosition) {
		return getRayTraceShape(state, view, pos);
	}
	
	@Override
	public VoxelShape getRayTraceShape(BlockState state, BlockView view, BlockPos pos) {
		VoxelShape result = NODE_SHAPE;
		if (state.get(NORTH)) result = VoxelShapes.union(result, NORTH_SHAPE);
		if (state.get(SOUTH)) result = VoxelShapes.union(result, SOUTH_SHAPE);
		if (state.get(EAST)) result = VoxelShapes.union(result, EAST_SHAPE);
		if (state.get(WEST)) result = VoxelShapes.union(result, WEST_SHAPE);
		if (state.get(UP)) result = VoxelShapes.union(result, UP_SHAPE);
		return result;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = new BlockPos(ctx.getPos());
		return this.getDefaultState()
				.with(NORTH, canConnect(world, pos, Direction.NORTH))
				.with(SOUTH, canConnect(world, pos, Direction.SOUTH))
				.with(EAST, canConnect(world, pos, Direction.EAST))
				.with(WEST, canConnect(world, pos, Direction.WEST))
				.with(UP, canConnect(world, pos, Direction.UP))
				.with(WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean unknown) {
		if (state.get(WATERLOGGED)) {
			FluidState fluidState = world.getFluidState(pos);
			if (!fluidState.isEmpty()) {
				world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
			}
		}
		
		world.setBlockState(pos, state
				.with(NORTH, canConnect(world, pos, Direction.NORTH))
				.with(SOUTH, canConnect(world, pos, Direction.SOUTH))
				.with(EAST, canConnect(world, pos, Direction.EAST))
				.with(WEST, canConnect(world, pos, Direction.WEST))
				.with(UP, canConnect(world, pos, Direction.UP))
		);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState stateFrom, boolean unknown) {
		for (Direction dir : Direction.values()) {
			world.updateNeighborsAlways(pos.offset(dir), this);
		}
	}

	@Override
	public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState stateFrom, boolean b) {
		for (Direction dir : Direction.values()) {
			world.updateNeighborsAlways(pos.offset(dir), this);
		}
	}
	
	boolean canConnect(World world, BlockPos pos, Direction dir) {
		if (world.getBlockState(pos.offset(dir)).getBlock()==this) return true;
		EnergyAttribute target = EnergyAttribute.ENERGY_ATTRIBUTE.getFirstOrNull(world, pos.offset(dir), SearchOptions.matching((EnergyAttribute it)->it.getPreferredType()==voltageTier));
		return (target!=null);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		if (state.get(WATERLOGGED)) {
			return Fluids.WATER.getDefaultState();
		} else {
			return Fluids.EMPTY.getDefaultState();
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		return new CableEntity();
	}
}
