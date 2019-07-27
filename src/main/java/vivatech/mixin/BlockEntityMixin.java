package vivatech.mixin;

import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import vivatech.api.entity.IBlockEntity;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements IBlockEntity {
}
