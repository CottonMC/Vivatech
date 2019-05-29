package vivatech.controller;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import vivatech.VivatechClient;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.energy.InfiniteEnergyType;
import vivatech.util.StringHelper;

public class ElectricFurnaceController extends CottonScreenController {
    public ElectricFurnaceController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel) getRootPanel();

        // Decorations
        rootPanel.add(new WLabel(StringHelper.getTranslatableComponent("block", ElectricFurnaceBlock.ID),
                WLabel.DEFAULT_TEXT_COLOR), 1, 0);

        // Bars
        WBar energyBar = new WBar(VivatechClient.ENERGY_BAR_BG, VivatechClient.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", InfiniteEnergyType.energyWithMaxI18nId));
        rootPanel.add(energyBar, 0, 0, 1, 5);

        WBar progressBar = new WBar(VivatechClient.PROGRESS_BAR_BG, VivatechClient.PROGRESS_BAR,2, 3, WBar.Direction.RIGHT);
        rootPanel.add(progressBar, 3, 2, 3, 1);

        // Slots
        rootPanel.add(WItemSlot.of(blockInventory, 0), 2, 2);
        rootPanel.add(WItemSlot.of(blockInventory, 1), 6, 2);
        rootPanel.add(createPlayerInventoryPanel(), 0, 5);

        rootPanel.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }
}
