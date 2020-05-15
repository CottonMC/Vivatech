package vivatech.common.block;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vivatech.common.Vivatech;
import vivatech.api.block.AbstractTieredMachineBlock;
import vivatech.common.block.entity.PressBlockEntity;
import vivatech.api.tier.Tier;
import vivatech.util.TierHelper;

public class PressBlock extends AbstractTieredMachineBlock {
    public static final Identifier ID = new Identifier(Vivatech.MOD_ID, "press");
    private final Identifier tieredId;

    public PressBlock(Tier tier) {
        super(Vivatech.MACHINE_BLOCK_SETTINGS, ID, tier);
        tieredId = TierHelper.getTieredID(ID, tier);
    }
    
	@Override
	public Identifier getTierId() {
		return tieredId;
	}

    // Block
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(ID, player, buf -> buf.writeBlockPos(pos));
        }

        return ActionResult.SUCCESS;
    }

    // BlockEntityProvider
    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new PressBlockEntity(this.getTier().getEnergyType());
    }
}
