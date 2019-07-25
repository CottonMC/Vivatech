package vivatech.menu;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;
import vivatech.Vivatech;
import vivatech.VivatechClient;
import vivatech.util.StringHelper;

public class CoalGeneratorController extends CottonScreenController {

    public CoalGeneratorController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(null, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Bars
        WBar energyBar = new WBar(VivatechClient.ENERGY_BAR_BG, VivatechClient.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", new Identifier(Vivatech.MODID, "energy_with_max")));
        root.add(energyBar, 1, 2, 14, 64);

        WBar fireBar = new WBar(VivatechClient.FIRE_BAR_BG, VivatechClient.FIRE_BAR,2, 3);
        root.add(fireBar, 89, 27, 14, 14);

        // Slots
        root.add(new WItemSlot(blockInventory, 0, 1, 1, true, true), 63, 27);
        root.add(createPlayerInventoryPanel(), 0, 72);

        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return -1; //There's no real result slot
    }
}
