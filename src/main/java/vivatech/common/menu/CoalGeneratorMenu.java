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
import vivatech.util.StringHelper;
import vivatech.data.TextureData;

public class CoalGeneratorMenu extends CottonCraftingController {

    public CoalGeneratorMenu(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(null, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Bars
        WBar energyBar = new WBar(TextureData.ENERGY_BAR_BG, TextureData.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", new Identifier(Vivatech.MOD_ID, "energy_with_max")));
        root.add(energyBar, 1, 2, 14, 64);

        WBar fireBar = new WBar(TextureData.FIRE_BAR_BG, TextureData.FIRE_BAR, 2, 3);
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
