package vivatech.api.block;

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
import vivatech.api.block.entity.AbstractProcessingMachineBlockEntity;
import vivatech.api.tier.Tier;
import vivatech.data.BlockData;

import java.util.function.Function;

public abstract class AbstractProcessingMachineBlock extends AbstractTieredMachineBlock {
    private final Identifier tieredId;
    private final Function<BlockView, AbstractProcessingMachineBlockEntity> blockEntityFactory;

    public AbstractProcessingMachineBlock(
        Identifier id,
        Tier tier,
        Identifier tieredId,
        Function<BlockView, AbstractProcessingMachineBlockEntity> blockEntityFactory
    ) {
        super(BlockData.MACHINE_BLOCK_SETTINGS, id, tier);
        this.tieredId = tieredId;
        this.blockEntityFactory = blockEntityFactory;
    }

    // Block
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(id, player, buf -> buf.writeBlockPos(pos));
        }

        return ActionResult.SUCCESS;
    }

    // BlockEntityProvider
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return blockEntityFactory.apply(world);
    }

    // Tiered

    @Override
    public Identifier getTieredId() {
        return tieredId;
    }
}
