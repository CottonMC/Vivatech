package io.github.cottonmc.um.block;

import io.github.cottonmc.um.block.entity.HammerMillEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class HammerMillBlock extends AbstractMachineBlock implements BlockEntityProvider, InventoryProvider {

	public HammerMillBlock() {
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new HammerMillEntity();
	}

	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
		if (!world.isClient) {
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity!=null && entity instanceof HammerMillEntity) {
				((HammerMillEntity)entity).pulse();
			} else {
				setStatus(world, pos, MachineStatus.ERROR);
			}
		}
	}
	
	@Override
	public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be!=null && be instanceof HammerMillEntity) {
			return ((HammerMillEntity)be).getInventory();
		} else {
			return null;
		}
	}
}
