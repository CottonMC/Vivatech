package io.github.cottonmc.um.block;
import io.github.cottonmc.um.VoxelMath;
import io.github.cottonmc.um.block.entity.ConveyorEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class ConveyorBlock extends Block implements BlockEntityProvider, InventoryProvider {

	public static DirectionProperty FACING = Properties.FACING_HORIZONTAL;
	public static BooleanProperty FRONT = BooleanProperty.create("front");
	public static BooleanProperty REAR = BooleanProperty.create("rear");
	
	private static BoundingBox FRONT_BOUNDS = new BoundingBox(0, 0, 0, 16/16.0, 8/16.0, 8/16.0);
	private static BoundingBox NO_FRONT_BOUNDS = new BoundingBox(0, 0, 4/16.0, 16/16.0, 8/16.0, 8/16.0);
	private static BoundingBox REAR_BOUNDS = new BoundingBox(0, 0, 8/16.0, 16/16.0, 8/16.0, 16/16.0);
	private static BoundingBox NO_REAR_BOUNDS = new BoundingBox(0, 0, 8/16.0, 16/16.0, 8/16.0, 12/16.0);

	public ConveyorBlock() {
		super(FabricBlockSettings.of(Material.METAL, DyeColor.LIGHT_GRAY).build());
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
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Direction facing = context.getPlayerHorizontalFacing();
		
		
		return this.getDefaultState()
				.with(FACING, context.getPlayerHorizontalFacing())
				.with(FRONT, canConnect(world, pos.offset(facing)))
				.with(REAR, canConnect(world, pos.offset(facing.getOpposite())));
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState oldState, Direction dir, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		Direction facing = oldState.get(FACING);
		return oldState
				.with(FRONT, canConnect(world, pos.offset(facing)))
				.with(REAR, canConnect(world, pos.offset(facing.getOpposite())));
	}
	
	public boolean canConnect(IWorld world, BlockPos pos) {
		if (world.getBlockState(pos).getBlock() instanceof ConveyorBlock) return true;
		
		//TODO: Check for inventories and capability support
		return false;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.with(FACING, FRONT, REAR);
	}
	
	@Override
	public boolean hasDynamicBounds() {
		return true;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, VerticalEntityPosition entityPos) {
		Direction dir = state.get(FACING);
		BoundingBox front = state.get(FRONT) ? FRONT_BOUNDS : NO_FRONT_BOUNDS;
		BoundingBox rear = state.get(REAR) ? REAR_BOUNDS : NO_REAR_BOUNDS;
		
		front = VoxelMath.rotate(front, dir);
		rear = VoxelMath.rotate(rear, dir);
		
		return VoxelShapes.union(VoxelShapes.cuboid(front), VoxelShapes.cuboid(rear));
	}
	
	
}
