package vivatech.controller;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import vivatech.block.CoalGeneratorBlock;
import vivatech.util.StringHelper;

public class CoalGeneratorController extends CottonScreenController {

    public CoalGeneratorController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel) getRootPanel();
        rootPanel.add(new WLabel(StringHelper.getTranslatableComponent("block", CoalGeneratorBlock.ID),
                WLabel.DEFAULT_TEXT_COLOR), 0, 0);

        rootPanel.add(new WLabel(String.format("%s/%s", propertyDelegate.get(0), propertyDelegate.get(1)),
                WLabel.DEFAULT_TEXT_COLOR), 0, 1);

        WItemSlot inputSlot = WItemSlot.of(blockInventory, 0);
        rootPanel.add(inputSlot, 2, 2);

        rootPanel.add(createPlayerInventoryPanel(), 0, 5);

        rootPanel.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return -1; //There's no real result slot
    }
}
