package io.github.cottonmc.um.block;

import io.github.cottonmc.um.block.entity.ChannelEntity;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidContainerProvider;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

public class ChannelBlock extends Block implements BlockEntityProvider, FluidContainerProvider {

	public static final BooleanProperty NORTH = Properties.NORTH_BOOL;
	public static final BooleanProperty EAST = Properties.EAST_BOOL;
	public static final BooleanProperty SOUTH = Properties.SOUTH_BOOL;
	public static final BooleanProperty WEST = Properties.WEST_BOOL;
	public static final BooleanProperty UP = Properties.UP_BOOL;
	public static final BooleanProperty DOWN = Properties.DOWN_BOOL;

	public ChannelBlock() {
		super(FabricBlockSettings.of(Material.METAL, DyeColor.BLACK).build());
	}

	@Override
	public FluidContainer getContainer(BlockState state, IWorld world, BlockPos pos) {
		return ((ChannelEntity)world.getBlockEntity(pos)).getFluids();
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new ChannelEntity();
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.with(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}
}
