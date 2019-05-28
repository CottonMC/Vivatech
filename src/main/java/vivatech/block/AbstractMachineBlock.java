package vivatech.block;

import alexiil.mc.lib.attributes.AttributeProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public abstract class AbstractMachineBlock extends Block implements BlockEntityProvider, AttributeProvider {
    public static DirectionProperty FACING = Properties.FACING_HORIZONTAL;
    public static BooleanProperty ACTIVE = BooleanProperty.create("active");

    public AbstractMachineBlock(Settings settings) {
        super(settings);

        setDefaultState(getStateFactory().getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    }

    // Block
    @Override
    protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING, ACTIVE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getDefaultState().with(FACING, context.getPlayerHorizontalFacing().getOpposite());
    }
}
