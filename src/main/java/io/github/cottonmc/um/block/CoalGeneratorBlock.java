package io.github.cottonmc.um.block;

import java.util.Random;

import io.github.cottonmc.ecs.api.ComponentContainer;
import io.github.cottonmc.um.block.entity.CoalGeneratorEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class CoalGeneratorBlock extends AbstractMachineBlock implements BlockEntityProvider, InventoryProvider {
	
	public CoalGeneratorBlock() {
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		//System.out.println("BlockEntity created.");
		return new CoalGeneratorEntity();
	}
	
	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.isClient) {
			//System.out.println("Client tick");
		} else {
			//System.out.println("Server tick");
			
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof CoalGeneratorEntity) {
				((CoalGeneratorEntity)entity).pulse();
			} else {
				setStatus(world, pos, MachineStatus.ERROR);
			}
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerHorizontalFacing().getOpposite());
	}

	@Override
	public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be!=null && be instanceof CoalGeneratorEntity) {
			return ((CoalGeneratorEntity)be).getInventory(state, world, pos);
		} else {
			return null;
		}
	}
}
