package vivatech.controller;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.*;
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

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Bars
        WBar energyBar = new WBar(VivatechClient.ENERGY_BAR_BG, VivatechClient.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", InfiniteEnergyType.energyWithMaxI18nId));
        root.add(energyBar, 1, 0, 14, 86);

        WBar fireBar = new WBar(VivatechClient.FIRE_BAR_BG, VivatechClient.FIRE_BAR,2, 3);
        root.add(fireBar, 89, 36, 14, 14);

        // Slots
        root.add(new WItemSlot(blockInventory, 0, 1, 1, true, true), 63, 36);
        root.add(createPlayerInventoryPanel(), 0, 90);

        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return -1; //There's no real result slot
    }
}
