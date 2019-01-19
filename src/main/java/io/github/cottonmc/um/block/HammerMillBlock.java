package io.github.cottonmc.um.block;

import io.github.cottonmc.um.block.entity.HammerMillEntity;
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

import javax.annotation.Nullable;
import java.util.Random;

public class HammerMillBlock extends AbstractMachineBlock implements BlockEntityProvider, InventoryProvider {

	public HammerMillBlock() {
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		System.out.println("BlockEntity created.");
		return new HammerMillEntity();
	}

	@Override
	public void scheduledTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.isClient) {
			System.out.println("CLIENT TICK");
		} else {
			System.out.println("SERVER-LOGIC-TICK");

			BlockEntity entity = world.getBlockEntity(pos);
			if (entity!=null && entity instanceof HammerMillEntity) {





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
		if (be!=null && be instanceof HammerMillEntity) {
			return ((HammerMillEntity)be).getInventory(state, world, pos);
		} else {
			return null;
		}
	}
}
