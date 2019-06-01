package vivatech.block;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.loot.context.LootContext;

import java.util.List;

public class BaseBlock extends Block {
    public BaseBlock(Settings settings) {
        super(settings);
    }

    // Block
    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return Lists.newArrayList(new ItemStack(asItem()));
    }
}
