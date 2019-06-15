package vivatech.block;

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
import vivatech.Vivatech;
import vivatech.entity.PressEntity;
import vivatech.util.MachineTier;
import vivatech.util.TierHelper;

public class PressBlock extends AbstractTieredMachineBlock {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "press");
    final Identifier TIERED_ID;

    public PressBlock(MachineTier tier) {
        super(Vivatech.MACHINE_BLOCK_SETTINGS, "press", tier);
        TIERED_ID = TierHelper.getTieredID(ID, tier);
    }
    
	@Override
	public Identifier getTieredID() {
		return TIERED_ID;
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
        return new PressEntity(tier);
    }
}
