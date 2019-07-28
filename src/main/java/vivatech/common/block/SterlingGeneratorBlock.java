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
import vivatech.api.block.AbstractMachineBlock;
import vivatech.common.block.entity.SterlingGeneratorBlockEntity;

public class SterlingGeneratorBlock extends AbstractMachineBlock {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "sterling_generator");

    public SterlingGeneratorBlock() {
        super(Vivatech.MACHINE_BLOCK_SETTINGS);
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
        return new SterlingGeneratorBlockEntity();
    }
}
