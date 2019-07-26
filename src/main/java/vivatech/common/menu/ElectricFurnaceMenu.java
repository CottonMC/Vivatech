package vivatech.common.menu;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.client.VivatechClient;
import vivatech.util.StringHelper;

public class ElectricFurnaceMenu extends CottonScreenController {
    public ElectricFurnaceMenu(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Bars
        WBar energyBar = new WBar(VivatechClient.ENERGY_BAR_BG, VivatechClient.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", new Identifier(Vivatech.MODID, "energy_with_max")));
        root.add(energyBar, 1, 2, 14, 64);

        WBar progressBar = new WBar(VivatechClient.PROGRESS_BAR_BG, VivatechClient.PROGRESS_BAR,2, 3, WBar.Direction.RIGHT);
        root.add(progressBar, 59, 27, 40, 18);

        // Slots
        root.add(WItemSlot.of(blockInventory, 0), 36, 27);
        root.add(WItemSlot.outputOf(blockInventory, 1), 108, 27);
        root.add(createPlayerInventoryPanel(), 0, 72);

        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }
}
