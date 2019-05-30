package vivatech.controller;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import vivatech.VivatechClient;
import vivatech.energy.InfiniteEnergyType;
import vivatech.init.VivatechRecipes;
import vivatech.util.StringHelper;

public class PressController extends CottonScreenController {

    public PressController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(VivatechRecipes.PRESSING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Bars
        WBar energyBar = new WBar(VivatechClient.ENERGY_BAR_BG, VivatechClient.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", InfiniteEnergyType.energyWithMaxI18nId));
        root.add(energyBar, 1, 0, 14, 86);

        WBar progressBar = new WBar(VivatechClient.PROGRESS_BAR_BG, VivatechClient.PROGRESS_BAR,2, 3, WBar.Direction.RIGHT);
        root.add(progressBar, 59, 36, 40, 18);

        // Slots
        root.add(WItemSlot.of(blockInventory, 0), 36, 36);
        root.add(WItemSlot.outputOf(blockInventory, 1), 108, 36);
        root.add(createPlayerInventoryPanel(), 0, 90);

        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }
}
