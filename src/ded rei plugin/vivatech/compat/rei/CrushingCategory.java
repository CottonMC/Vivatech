package vivatech.compat.rei;

import me.shedaniel.rei.api.Renderer;
import net.minecraft.item.ItemStack;
import vivatech.common.init.VivatechBlocks;

final class CrushingCategory extends BaseRecipeCategory {
    CrushingCategory() {
        super(VivatechReiPlugin.CRUSHING, "gui.vivatech.crushing");
    }

    @Override
    public Renderer getIcon() {
        return Renderer.fromItemStack(new ItemStack(VivatechBlocks.CRUSHER.get(0)));
    }
}
