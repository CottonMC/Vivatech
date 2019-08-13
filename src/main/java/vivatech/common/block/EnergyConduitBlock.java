package vivatech.common.block;

import alexiil.mc.lib.attributes.SearchOption;
import alexiil.mc.lib.attributes.SearchOptions;
import io.github.cottonmc.energy.api.EnergyAttribute;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vivatech.api.tier.Tiered;
import vivatech.api.tier.Tier;
import vivatech.common.Vivatech;
import vivatech.common.block.entity.EnergyConduitBlockEntity;
import vivatech.util.TierHelper;

import javax.annotation.Nullable;
import java.util.List;

public class EnergyConduitBlock extends Block implements BlockEntityProvider, Tiered {
    private final Tier tier;
    private final Identifier tieredId;

    public static final Identifier ID = new Identifier(Vivatech.MODID, "energy_conduit");

    public static final BooleanProperty CONNECTED_UP    = BooleanProperty.of("connected_up");
    public static final BooleanProperty CONNECTED_DOWN  = BooleanProperty.of("connected_down");
    public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.of("connected_north");
    public static final BooleanProperty CONNECTED_EAST  = BooleanProperty.of("connected_east");
    public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.of("connected_south");
    public static final BooleanProperty CONNECTED_WEST  = BooleanProperty.of("connected_west");

    public EnergyConduitBlock(Tier tier) {
        super(Vivatech.METALLIC_BLOCK_SETTINGS);

        this.tier = tier;
        this.tieredId = TierHelper.getTieredID(ID, tier);

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
    public String getTranslationKey() {
        return "block.vivatech." + ID.getPath();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void buildTooltip(ItemStack stack, BlockView view, List<Text> lines, TooltipContext context) {

        Text tierLine = new TranslatableText("info.vivatech.tier",
                new TranslatableText("info.vivatech.tier." + tier.toString().toLowerCase()));
        tierLine.formatted(Formatting.GRAY);
        lines.add(tierLine);

        super.buildTooltip(stack, view, lines, context);

    }

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
    public BlockState getPlacementState(ItemPlacementContext context) {
        StateManager sm = new StateManager(null, context.getBlockPos(), context.getWorld());
        return getDefaultState()
                .with(CONNECTED_EAST,   sm.canConnect(Direction.EAST))
                .with(CONNECTED_WEST,   sm.canConnect(Direction.WEST))
                .with(CONNECTED_UP,     sm.canConnect(Direction.UP))
                .with(CONNECTED_DOWN,   sm.canConnect(Direction.DOWN))
                .with(CONNECTED_SOUTH,  sm.canConnect(Direction.SOUTH))
                .with(CONNECTED_NORTH,  sm.canConnect(Direction.NORTH));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block otherBlock, BlockPos otherPos, boolean b) {
        super.neighborUpdate(state, world, pos, otherBlock, otherPos, b);

        updateProperties(state, pos, world);
    }

    private void updateProperties(BlockState state, BlockPos pos, World world) {
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

        public StateManager calcProperty(Direction direction, BooleanProperty property) {
            boolean sideState = state.get(property);

            if (canConnect(direction)) {
                if (!sideState) dirty = true;
                state = state.with(property, true);
            } else {
                if (sideState) dirty = true;
                state = state.with(property, false);
            }

            return this;
        }

        public boolean canConnect(Direction direction) {
            BlockPos offsetPos = pos.offset(direction);
            SearchOption<Object> option = SearchOptions.inDirection(direction);
            Block block = world.getBlockState(offsetPos).getBlock();
            return block instanceof EnergyConduitBlock && ((Tiered) block).getTier() == getTier()
                    || EnergyAttribute.ENERGY_ATTRIBUTE.getFirstOrNull(world, offsetPos, option) != null;
        }
    }

    // BlockEntityProvider
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new EnergyConduitBlockEntity();
    }

    // ITieredBlock
    @Override
    public Identifier getTierId() {
        return tieredId;
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}