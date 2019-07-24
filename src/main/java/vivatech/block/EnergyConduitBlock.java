package vivatech.block;

import alexiil.mc.lib.attributes.AttributeList;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vivatech.Vivatech;
import vivatech.entity.EnergyConduitEntity;
import vivatech.util.EnergyConduitConnection;
import vivatech.util.IEnergyConduitConnectable;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EnergyConduitBlock extends Block implements BlockEntityProvider, IEnergyConduitConnectable {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "energy_conduit");

    public static final EnumProperty<EnergyConduitConnection> CONNECTED_UP
            = EnumProperty.of("connected_up", EnergyConduitConnection.class);
    public static final EnumProperty<EnergyConduitConnection> CONNECTED_DOWN
            = EnumProperty.of("connected_down", EnergyConduitConnection.class);
    public static final EnumProperty<EnergyConduitConnection> CONNECTED_NORTH
            = EnumProperty.of("connected_north", EnergyConduitConnection.class);
    public static final EnumProperty<EnergyConduitConnection> CONNECTED_EAST
            = EnumProperty.of("connected_east", EnergyConduitConnection.class);
    public static final EnumProperty<EnergyConduitConnection> CONNECTED_SOUTH
            = EnumProperty.of("connected_south", EnergyConduitConnection.class);
    public static final EnumProperty<EnergyConduitConnection> CONNECTED_WEST
            = EnumProperty.of("connected_west", EnergyConduitConnection.class);

    public EnergyConduitBlock() {
        super(Vivatech.METALLIC_BLOCK_SETTINGS);

        setDefaultState(getStateFactory().getDefaultState()
                .with(CONNECTED_UP, EnergyConduitConnection.DISCONNECTED)
                .with(CONNECTED_DOWN,  EnergyConduitConnection.DISCONNECTED)
                .with(CONNECTED_NORTH, EnergyConduitConnection.DISCONNECTED)
                .with(CONNECTED_EAST,  EnergyConduitConnection.DISCONNECTED)
                .with(CONNECTED_SOUTH,  EnergyConduitConnection.DISCONNECTED)
                .with(CONNECTED_WEST,  EnergyConduitConnection.DISCONNECTED));
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
        ArrayList<VoxelShape> shapes = new ArrayList<>();
        double startOffset = 0.3125;
        double endOffset = 0.6875;

        if (state.get(CONNECTED_EAST) != EnergyConduitConnection.DISCONNECTED)
            shapes.add(offsetShape(Direction.EAST,  startOffset, endOffset));
        if (state.get(CONNECTED_WEST) != EnergyConduitConnection.DISCONNECTED)
            shapes.add(offsetShape(Direction.WEST,  startOffset, endOffset));
        if (state.get(CONNECTED_UP) != EnergyConduitConnection.DISCONNECTED)
            shapes.add(offsetShape(Direction.UP,    startOffset, endOffset));
        if (state.get(CONNECTED_DOWN) != EnergyConduitConnection.DISCONNECTED)
            shapes.add(offsetShape(Direction.DOWN,  startOffset, endOffset));
        if (state.get(CONNECTED_SOUTH) != EnergyConduitConnection.DISCONNECTED)
            shapes.add(offsetShape(Direction.SOUTH, startOffset, endOffset));
        if (state.get(CONNECTED_NORTH) != EnergyConduitConnection.DISCONNECTED)
            shapes.add(offsetShape(Direction.NORTH, startOffset, endOffset));

        return VoxelShapes.union(
                VoxelShapes.cuboid(startOffset, startOffset, startOffset, endOffset, endOffset, endOffset),
                shapes.toArray(new VoxelShape[0]));
    }

    private VoxelShape offsetShape(Direction direction, double startOffset, double endOffset) {
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

        return VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        StateManager sm = new StateManager(null, context.getBlockPos(), context.getWorld());
        return getDefaultState()
                .with(CONNECTED_EAST,   sm.getConnection(Direction.EAST))
                .with(CONNECTED_WEST,   sm.getConnection(Direction.WEST))
                .with(CONNECTED_UP,     sm.getConnection(Direction.UP))
                .with(CONNECTED_DOWN,   sm.getConnection(Direction.DOWN))
                .with(CONNECTED_SOUTH,  sm.getConnection(Direction.SOUTH))
                .with(CONNECTED_NORTH,  sm.getConnection(Direction.NORTH));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block otherBlock, BlockPos otherPos, boolean b) {
        super.neighborUpdate(state, world, pos, otherBlock, otherPos, b);

        StateManager sm = new StateManager(state, pos, world);
        BlockState newState = sm
                .calcProperty(Direction.EAST,   CONNECTED_EAST)
                .calcProperty(Direction.WEST,   CONNECTED_WEST)
                .calcProperty(Direction.UP,     CONNECTED_UP)
                .calcProperty(Direction.DOWN,   CONNECTED_DOWN)
                .calcProperty(Direction.SOUTH,  CONNECTED_SOUTH)
                .calcProperty(Direction.NORTH,  CONNECTED_NORTH)
                .state;
        if (sm.dirty) {
            world.setBlockState(pos, newState);
            world.updateNeighbors(pos,this);
        }
    }

    private class StateManager {
        public boolean dirty = false;
        public BlockState state;
        public BlockPos pos;
        public World world;

        public StateManager(BlockState state, BlockPos pos, World world) {
            this.state = state;
            this.pos = pos;
            this.world = world;
        }

        public StateManager calcProperty(Direction direction, EnumProperty<EnergyConduitConnection> property) {
            EnergyConduitConnection sideState = state.get(property);

            EnergyConduitConnection connection = getConnection(direction);
            if (connection != EnergyConduitConnection.DISCONNECTED) {
                if (sideState == EnergyConduitConnection.DISCONNECTED
                        || sideState == EnergyConduitConnection.DISABLED) dirty = true;
                state = state.with(property, connection);
            } else {
                if (sideState != EnergyConduitConnection.DISCONNECTED
                        && sideState != EnergyConduitConnection.DISABLED) dirty = true;
                state = state.with(property, connection);
            }

            return this;
        }

        public EnergyConduitConnection getConnection(Direction direction) {
            Block block = world.getBlockState(pos.offset(direction)).getBlock();

            if (block instanceof IEnergyConduitConnectable) {
                return ((IEnergyConduitConnectable) block).getConnection();
            }

            AttributeList<EnergyAttribute> attributes = EnergyAttribute.ENERGY_ATTRIBUTE.getAll(world, pos.offset(direction));
            boolean isConsumer = false;
            boolean isProducer = false;
            for (int i = 0; i < attributes.getCount(); i++) {
                EnergyAttribute attribute = attributes.get(i);
                if (attribute.canExtractEnergy()) isProducer = true;
                if (attribute.canInsertEnergy()) isConsumer = true;
            }

            if (isConsumer && isProducer) {
                return EnergyConduitConnection.ENERGY_BANK;
            }
            if (isConsumer) {
                return EnergyConduitConnection.CONSUMER;
            }
            if (isProducer) {
                return EnergyConduitConnection.PRODUCER;
            }

            return EnergyConduitConnection.DISCONNECTED;
        }
    }

    // BlockEntityProvider
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new EnergyConduitEntity();
    }

    // IEnergyConduitConnectable
    @Override
    public EnergyConduitConnection getConnection() {
        return EnergyConduitConnection.CONDUIT;
    }
}
