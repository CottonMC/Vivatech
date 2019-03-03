package io.github.cottonmc.um.block;
import io.github.cottonmc.um.block.entity.ConveyorEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.Random;

public class ConveyorBlock extends AbstractMachineBlock implements BlockEntityProvider, InventoryProvider {

	public ConveyorBlock() {
		super(FabricBlockSettings.of(Material.METAL, DyeColor.ORANGE).noCollision().build());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new ConveyorEntity();
	}

	@Override
	public SidedInventory getInventory(BlockState blockState, IWorld iWorld, BlockPos blockPos) {
		return null;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerHorizontalFacing());
	}

	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.isClient) {
			System.out.println("CLIENT TICK");
		} else {
			System.out.println("SERVER-LOGIC-TICK");

			BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof ConveyorEntity) {
				((ConveyorEntity) entity).pulse();
			} else {
				setStatus(world, pos, MachineStatus.ERROR);
			}
		}
	}
}
