package vivatech.common.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import javafx.beans.property.BooleanProperty;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vivatech.api.tier.Tier;
import vivatech.api.tier.Tiered;
import vivatech.common.Vivatech;
import vivatech.common.block.entity.ConnectorBlockEntity;
import vivatech.util.TierHelper;

import javax.annotation.Nullable;

public class ConnectorBlock extends Block implements BlockEntityProvider, AttributeProvider, Tiered {
	public static final Identifier ID = new Identifier(Vivatech.MODID, "connector");
	private final Tier tier;
	private final Identifier tieredId;

	public static final DirectionProperty FACING = Properties.FACING;

	public ConnectorBlock(Tier tier) {
		super(FabricBlockSettings.of(Material.WOOD).build());
		this.tier = tier;
		tieredId = TierHelper.getTieredID(ID, tier);
		this.setDefaultState(this.getStateFactory().getDefaultState().with(FACING, Direction.DOWN));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING);
	}

	@Override
	public void onBlockRemoved(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean boolean_1) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ConnectorBlockEntity) {
			((ConnectorBlockEntity)be).dropPeers();
		}
		super.onBlockRemoved(oldState, world, pos, newState, boolean_1);
	}

	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof ConnectorBlockEntity) {
			to.offer(((ConnectorBlockEntity)be).getEnergyAttribute());
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new ConnectorBlockEntity(this.getTier());
	}

	@Override
	public Identifier getTierId() {
		return tieredId;
	}

	@Override
	public Tier getTier() {
		return tier;
	}
}
