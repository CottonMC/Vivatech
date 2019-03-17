package io.github.cottonmc.um.block;

import io.github.cottonmc.ecs.api.ComponentContainer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.TaskPriority;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AbstractMachineBlock extends Block {
	public static DirectionProperty FACING = Properties.FACING_HORIZONTAL;
	public static EnumProperty<MachineStatus> STATUS = EnumProperty.create("status", MachineStatus.class);
	
	public AbstractMachineBlock() {
		this(FabricBlockSettings.of(Material.METAL, DyeColor.WHITE).build());
	}

	public AbstractMachineBlock(Block.Settings settings) {
		super(settings);
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.with(FACING, STATUS);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerHorizontalFacing().getOpposite());
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		world.getBlockTickScheduler().schedule(pos, this, 1, TaskPriority.LOW);
	}
	
	public void setStatus(World world, BlockPos pos, MachineStatus status) {
		BlockState cur = world.getBlockState(pos);
		if (cur.contains(STATUS) && cur.get(STATUS)==status) return;
		world.setBlockState(pos, cur.with(STATUS, status), 2);
	}
	
	public void setStatusAndSchedule(World world, BlockPos pos, MachineStatus status, int numTicks) {
		setStatus(world, pos, status);
		world.getBlockTickScheduler().schedule(pos, this, numTicks, TaskPriority.LOW);
	}
}
