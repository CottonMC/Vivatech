package vivatech.api.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IBlockEntity {
    World getWorld();
    BlockPos getPos();
}
