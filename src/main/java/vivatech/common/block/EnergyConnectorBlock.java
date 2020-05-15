package vivatech.common.block;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
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
import vivatech.common.block.entity.EnergyConnectorBlockEntity;
import vivatech.util.TierHelper;

import javax.annotation.Nullable;

public class EnergyConnectorBlock extends Block implements BlockEntityProvider, AttributeProvider, Tiered {
	public static final Identifier ID = new Identifier(Vivatech.MOD_ID, "connector");
	private final Tier tier;
	private final Identifier tieredId;

	public static final DirectionProperty FACING = Properties.FACING;

	public EnergyConnectorBlock(Tier tier) {
		super(FabricBlockSettings.of(Material.WOOD).build());
		this.tier = tier;
		tieredId = TierHelper.getTieredID(ID, tier);
		this.setDefaultState(this.getStateManager().getDefaultState().with(FACING, Direction.DOWN));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(FACING);
	}

	@Override
	public void onBlockRemoved(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean boolean_1) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof EnergyConnectorBlockEntity) {
			((EnergyConnectorBlockEntity)be).dropAllPeers();
		}
		super.onBlockRemoved(oldState, world, pos, newState, boolean_1);
	}

	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof EnergyConnectorBlockEntity) {
			to.offer(((EnergyConnectorBlockEntity)be).getEnergy());
		}
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new EnergyConnectorBlockEntity(this.getTier());
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
