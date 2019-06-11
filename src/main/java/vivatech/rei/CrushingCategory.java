package vivatech.rei;

import me.shedaniel.rei.api.Renderable;
import me.shedaniel.rei.api.Renderer;
import net.minecraft.item.ItemStack;
import vivatech.init.VivatechBlocks;

final class CrushingCategory extends BaseRecipeCategory {
    CrushingCategory() {
        super(VivatechREIPlugin.CRUSHING, "gui.vivatech.crushing");
    }

    @Override
    public Renderer getIcon() {
        return Renderable.fromItemStack(new ItemStack(VivatechBlocks.CRUSHER));
    }
}
