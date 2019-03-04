package io.github.cottonmc.um.block;
import io.github.cottonmc.um.block.entity.ConveyorEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

public class ConveyorBlock extends Block implements BlockEntityProvider, InventoryProvider {

	public static DirectionProperty FACING = Properties.FACING_HORIZONTAL;

	public ConveyorBlock() {
		super(FabricBlockSettings.of(Material.METAL, DyeColor.ORANGE).noCollision().build());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new ConveyorEntity();
	}

	@Override
	public SidedInventory getInventory(BlockState blockState, IWorld world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ConveyorEntity) {
			return ((ConveyorEntity)be).getInventory();
		}
		return null;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerHorizontalFacing());
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.with(FACING);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, VerticalEntityPosition entityPos) {
		return VoxelShapes.cube(0, 0, 0, 1, 0.5, 1);
	}
}
