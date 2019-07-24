package vivatech.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import vivatech.entity.AbstractMachineEntity;
import vivatech.util.EnergyConduitConnection;
import vivatech.util.IEnergyConduitConnectable;

public abstract class AbstractMachineBlock extends Block implements BlockEntityProvider, AttributeProvider,
        IEnergyConduitConnectable {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    public AbstractMachineBlock(Settings settings) {
        super(settings);

        setDefaultState(getStateFactory().getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }

    // Block
    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(FACING, context.getPlayerFacing().getOpposite());
    }

    @Override
    public void onBlockRemoved(BlockState stateFrom, World world, BlockPos pos, BlockState stateTo, boolean boolean_1) {
        BlockEntity be = world.getBlockEntity(pos);

        if(stateFrom.getBlock() != stateTo.getBlock() && be instanceof SidedInventory) {
            ItemScatterer.spawn(world, pos, (SidedInventory) be);
            world.updateHorizontalAdjacent(pos, this);
        }

        super.onBlockRemoved(stateFrom, world, pos, stateTo, boolean_1);
    }

    // AttributeProvider
    @Override
    public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof AbstractMachineEntity) {
            to.offer(((AbstractMachineEntity) be).getEnergy());
        }
    }

    // IEnergyConduitConnectable
    @Override
    public EnergyConduitConnection getConnection() {
        return EnergyConduitConnection.CONSUMER;
    }
}
