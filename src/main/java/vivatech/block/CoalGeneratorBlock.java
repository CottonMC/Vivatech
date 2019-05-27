package vivatech.block;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vivatech.util.Meta;
import vivatech.entity.CoalGeneratorEntity;

public class CoalGeneratorBlock extends Block implements BlockEntityProvider {
    public static final Identifier ID = new Identifier(Meta.MODID, "coal_generator");

    public CoalGeneratorBlock() {
        super(Meta.MACHINE_BLOCK_SETTINGS);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new CoalGeneratorEntity();
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(ID, player, buf -> buf.writeBlockPos(pos));
        }

        return true;
    }
}
