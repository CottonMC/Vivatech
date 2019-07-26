package vivatech.common.block;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vivatech.common.Vivatech;
import vivatech.api.block.AbstractTieredMachineBlock;
import vivatech.common.entity.ElectricFurnaceEntity;
import vivatech.api.util.MachineTier;
import vivatech.util.TierHelper;

public class ElectricFurnaceBlock extends AbstractTieredMachineBlock {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "electric_furnace");
    final Identifier TIERED_ID;

    public ElectricFurnaceBlock(MachineTier tier) {
        super(Vivatech.MACHINE_BLOCK_SETTINGS, "electric_furnace", tier);
        TIERED_ID = TierHelper.getTieredID(ID, tier);
    }

    // Block
    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(ID, player, buf -> buf.writeBlockPos(pos));
        }

        return true;
    }

    // BlockEntityProvider
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new ElectricFurnaceEntity();
    }

	@Override
	public Identifier getTieredID() {
		return TIERED_ID;
	}
}
