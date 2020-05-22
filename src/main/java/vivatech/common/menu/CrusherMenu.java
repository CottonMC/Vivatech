package vivatech.common.menu;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.common.init.VivatechRecipes;
import vivatech.util.StringHelper;
import vivatech.data.TextureData;

public class CrusherMenu extends CottonCraftingController {

    public CrusherMenu(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(VivatechRecipes.CRUSHING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Bars
        WBar energyBar = new WBar(TextureData.ENERGY_BAR_BG, TextureData.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", new Identifier(Vivatech.MOD_ID, "energy_with_max")));
        root.add(energyBar, 1, 2, 14, 64);

        WBar progressBar = new WBar(TextureData.PROGRESS_BAR_BG, TextureData.PROGRESS_BAR, 2, 3, WBar.Direction.RIGHT);
        root.add(progressBar, 59, 27, 40, 18);

        // Slots
        root.add(WItemSlot.of(blockInventory, 0), 36, 27);
        root.add(WItemSlot.outputOf(blockInventory, 1).setModifiable(false), 108, 27);
        root.add(createPlayerInventoryPanel(), 0, 72);

        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }
}
