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
import vivatech.block.CoalGeneratorBlock;
import vivatech.energy.InfiniteEnergyType;
import vivatech.util.StringHelper;

public class CoalGeneratorController extends CottonScreenController {

    public CoalGeneratorController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel) getRootPanel();

        // Decorations
        rootPanel.add(new WLabel(StringHelper.getTranslatableComponent("block", CoalGeneratorBlock.ID),
                WLabel.DEFAULT_TEXT_COLOR), 1, 0);

        // Bars
        WBar energyBar = new WBar(VivatechClient.ENERGY_BAR_BG, VivatechClient.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", InfiniteEnergyType.energyWithMaxI18nId));
        rootPanel.add(energyBar, 0, 0, 1, 5);

        // Slots
        rootPanel.add(WItemSlot.of(blockInventory, 0), 2, 2);
        rootPanel.add(createPlayerInventoryPanel(), 0, 5);

        rootPanel.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return -1; //There's no real result slot
    }
}
