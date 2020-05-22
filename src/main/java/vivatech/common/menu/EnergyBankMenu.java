package vivatech.common.menu;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.util.StringHelper;
import vivatech.data.TextureData;

public class EnergyBankMenu extends CottonCraftingController {

    public EnergyBankMenu(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(null, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WPlainPanel root = new WPlainPanel();
        ((WGridPanel) getRootPanel()).add(root, 0, 0);

        // Bars
        WBar energyBar = new WBar(TextureData.ENERGY_BAR_BG, TextureData.ENERGY_BAR, 0, 1);
        energyBar.withTooltip(StringHelper.getTranslationKey("info", new Identifier(Vivatech.MOD_ID, "energy_with_max")));
        root.add(energyBar, 1, 2, 14, 64);

        // Decorations
        root.add(new WSprite(TextureData.ARROW_DOWN), 56, 28);
        root.add(new WSprite(TextureData.ARROW_UP), 91, 28);

        // Slots
        root.add(WItemSlot.of(blockInventory, 0), 36, 27);
        root.add(WItemSlot.of(blockInventory, 1), 108, 27);
        root.add(createPlayerInventoryPanel(), 0, 72);

        root.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return -1;
    }
}
