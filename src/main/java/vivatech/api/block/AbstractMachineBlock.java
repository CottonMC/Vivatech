package vivatech.api.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import vivatech.api.block.entity.AbstractMachineBlockEntity;

public abstract class AbstractMachineBlock extends Block implements BlockEntityProvider, AttributeProvider, InventoryProvider {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public AbstractMachineBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }

    // Block
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
    }

    @Override
    public void onBlockRemoved(BlockState stateFrom, World world, BlockPos pos, BlockState stateTo, boolean boolean_1) {

        if(getInventory(stateFrom, world, pos) != null) {
            ItemScatterer.spawn(world, pos, getInventory(stateFrom, world, pos));
            world.updateHorizontalAdjacent(pos, this);
        }

        super.onBlockRemoved(stateFrom, world, pos, stateTo, boolean_1);
    }

    // AttributeProvider
    @Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof AbstractMachineBlockEntity) {
            to.offer(((AbstractMachineBlockEntity)be).getEnergy());
            to.offer(((AbstractMachineBlockEntity)be).getInventory());
        }
    }

    //InventoryProvider for vanilla safety
    @Override
    public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
        if (!(world.getBlockEntity(pos) instanceof SidedInventory)) return null;
        return (SidedInventory)world.getBlockEntity(pos);
    }
}
