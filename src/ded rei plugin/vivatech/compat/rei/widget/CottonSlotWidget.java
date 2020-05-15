package vivatech.compat.rei.widget;

import io.github.cottonmc.cotton.gui.EmptyInventory;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import me.shedaniel.rei.api.Renderer;
import me.shedaniel.rei.gui.widget.SlotBaseWidget;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * A slot widget that uses Cotton's {@link WItemSlot} for rendering.
 */
public final class CottonSlotWidget extends SlotBaseWidget {
    private final WItemSlot renderSlot;
    private final int x;
    private final int y;

    private CottonSlotWidget(int x, int y, Renderer renderer, boolean showToolTips, boolean clickToMoreRecipes, WItemSlot renderSlot) {
        super(x, y, renderer, false, showToolTips, clickToMoreRecipes);
        this.renderSlot = renderSlot;
        this.x = x;
        this.y = y;
    }

    public CottonSlotWidget(int x, int y, Renderer itemList, boolean showToolTips, boolean clickToMoreRecipes) {
        this(x, y, itemList, showToolTips, clickToMoreRecipes, WItemSlot.of(EmptyInventory.INSTANCE, 0));
    }

    public static CottonSlotWidget createBig(int x, int y, List<ItemStack> itemList, boolean showToolTips, boolean clickToMoreRecipes) {
        return new CottonSlotWidget(x, y, Renderer.fromItemStacks(itemList), showToolTips, clickToMoreRecipes, WItemSlot.outputOf(EmptyInventory.INSTANCE, 0));
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderSlot.paintBackground(x, y);
        super.render(mouseX, mouseY, delta);
    }
}
