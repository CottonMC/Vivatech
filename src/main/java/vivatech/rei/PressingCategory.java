package vivatech.rei;

import me.shedaniel.rei.api.Renderable;
import me.shedaniel.rei.api.Renderer;
import net.minecraft.item.ItemStack;
import vivatech.init.VivatechBlocks;

final class PressingCategory extends BaseRecipeCategory {
    PressingCategory() {
        super(VivatechReiPlugin.PRESSING, "gui.vivatech.pressing");
    }

    @Override
    public Renderer getIcon() {
        return Renderable.fromItemStack(new ItemStack(VivatechBlocks.PRESS.get(0)));
    }
}
