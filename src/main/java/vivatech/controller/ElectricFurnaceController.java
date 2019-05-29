package vivatech.controller;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.*;
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

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Decorations
        WLabel label = new WLabel(StringHelper.getTranslatableComponent("block", ElectricFurnaceBlock.ID), WLabel.DEFAULT_TEXT_COLOR);
        root.add(label, 1, 0);

        // Bars
        WBar energyBar = new WBar(VivatechClient.ENERGY_BAR_BG, VivatechClient.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", InfiniteEnergyType.energyWithMaxI18nId));
        root.add(energyBar, 0, 0, 18, 67);

        WBar progressBar = new WBar(VivatechClient.PROGRESS_BAR_BG, VivatechClient.PROGRESS_BAR,2, 3, WBar.Direction.RIGHT);
        root.add(progressBar, 30, 20, 30, 10);

        // Slots
        root.add(WItemSlot.of(blockInventory, 0), 20, 20);
        root.add(WItemSlot.of(blockInventory, 1), 60, 20);
        root.add(createPlayerInventoryPanel(), 0, 90);

        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }
}
