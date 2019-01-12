package io.github.cottonmc.um.block;

import java.util.Random;

import io.github.cottonmc.um.block.entity.CoalGeneratorEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CoalGeneratorBlock extends AbstractMachineBlock implements BlockEntityProvider {
	
	public CoalGeneratorBlock() {
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		System.out.println("BlockEntity created.");
		return new CoalGeneratorEntity();
	}
	
	@Override
	public void scheduledTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.isClient) {
			System.out.println("CLIENT TICK");
		} else {
			System.out.println("SERVER-LOGIC-TICK");
			
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity!=null && entity instanceof CoalGeneratorEntity) {
				
				
				
				
				
			} else {
				setStatus(world, pos, MachineStatus.ERROR);
			}
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerHorizontalFacing().getOpposite());
	}
}
