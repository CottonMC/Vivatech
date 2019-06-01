package vivatech.block;

import alexiil.mc.lib.attributes.SearchOption;
import alexiil.mc.lib.attributes.SearchOptions;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vivatech.Vivatech;

public class EnergyConduitBlock extends Block {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "energy_conduit");

    public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
    public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create("connected_north");
    public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create("connected_east");
    public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create("connected_south");
    public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create("connected_west");

    public EnergyConduitBlock() {
        super(Vivatech.METALLIC_BLOCK_SETTINGS);

        setDefaultState(getStateFactory().getDefaultState()
                .with(CONNECTED_UP, false)
                .with(CONNECTED_DOWN, false)
                .with(CONNECTED_NORTH, false)
                .with(CONNECTED_EAST, false)
                .with(CONNECTED_SOUTH, false)
                .with(CONNECTED_WEST, false));
    }

    // Block
    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_UP, CONNECTED_DOWN, CONNECTED_NORTH, CONNECTED_EAST, CONNECTED_SOUTH, CONNECTED_WEST);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView blockView, BlockPos pos, EntityContext context) {
        double startOffset = 0.3125;
        double endOffset = 0.6875;

        VoxelShape shape = VoxelShapes.cuboid(startOffset, startOffset, startOffset, endOffset, endOffset, endOffset);

        if (state.get(CONNECTED_EAST))  shape = modifyShape(shape, Direction.EAST,  startOffset, endOffset);
        if (state.get(CONNECTED_WEST))  shape = modifyShape(shape, Direction.WEST,  startOffset, endOffset);
        if (state.get(CONNECTED_UP))    shape = modifyShape(shape, Direction.UP,    startOffset, endOffset);
        if (state.get(CONNECTED_DOWN))  shape = modifyShape(shape, Direction.DOWN,  startOffset, endOffset);
        if (state.get(CONNECTED_SOUTH)) shape = modifyShape(shape, Direction.SOUTH, startOffset, endOffset);
        if (state.get(CONNECTED_NORTH)) shape = modifyShape(shape, Direction.NORTH, startOffset, endOffset);

        return shape;
    }

    private VoxelShape modifyShape(VoxelShape shape, Direction direction, double startOffset, double endOffset) {
        double minX = startOffset;
        double minY = startOffset;
        double minZ = startOffset;
        double maxX = endOffset;
        double maxY = endOffset;
        double maxZ = endOffset;

        switch (direction) {
            case EAST:  maxX = 1; break;
            case WEST:  minX = 0; break;
            case UP:    maxY = 1; break;
            case DOWN:  minY = 0; break;
            case SOUTH: maxZ = 1; break;
            case NORTH: minZ = 0; break;
        }

        return VoxelShapes.combineAndSimplify(shape, VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ), BooleanBiFunction.OR);
    }

    @Override
    public void onBlockAdded(BlockState stateFrom, World world, BlockPos pos, BlockState stateTo, boolean b) {
        updateConnectionProperty(stateFrom, pos, world);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block otherBlock, BlockPos otherPos, boolean b) {
        super.neighborUpdate(state, world, pos, otherBlock, otherPos, b);

        updateConnectionProperty(state, pos, world);
    }

    private void updateConnectionProperty(BlockState state, BlockPos pos, World world) {
        calcConnectionProperty(state, pos, Direction.UP, world, CONNECTED_UP);
        calcConnectionProperty(state, pos, Direction.DOWN, world, CONNECTED_DOWN);
        calcConnectionProperty(state, pos, Direction.NORTH, world, CONNECTED_NORTH);
        calcConnectionProperty(state, pos, Direction.EAST, world, CONNECTED_EAST);
        calcConnectionProperty(state, pos, Direction.SOUTH, world, CONNECTED_SOUTH);
        calcConnectionProperty(state, pos, Direction.WEST, world, CONNECTED_WEST);
    }

    private void calcConnectionProperty(BlockState state, BlockPos pos, Direction direction, World world, BooleanProperty property) {
        boolean dirty = false;

        boolean sideState = state.get(property);
        BlockPos offsetPos = pos.offset(direction);
        SearchOption option = SearchOptions.inDirection(direction);

        if (EnergyAttribute.ENERGY_ATTRIBUTE.getFirstOrNull(world, offsetPos, option) != null
                || world.getBlockState(offsetPos).getBlock() instanceof EnergyConduitBlock) {
            if (!sideState) {
                dirty = true;
            }
            state = state.with(property, true);
        } else {
            if (sideState) {
                dirty = true;
            }
            state = state.with(property, false);
        }

        if (dirty) {
            world.setBlockState(pos, state);
            world.updateNeighbors(pos, this);
        }
    }
}
