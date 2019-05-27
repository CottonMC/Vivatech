package vivatech.controller;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.Vivatech;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.util.StringUtil;

public class ElectricFurnaceController extends CottonScreenController {

    public ElectricFurnaceController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel) getRootPanel();
        rootPanel.add(new WLabel(StringUtil.getTranslatableComponent("block", ElectricFurnaceBlock.ID),
                WLabel.DEFAULT_TEXT_COLOR), 0, 0);

        rootPanel.add(new WLabel(String.format("%s/%s", propertyDelegate.get(0), propertyDelegate.get(1)),
                WLabel.DEFAULT_TEXT_COLOR), 0, 1);

//        WBar bar = new WBar(new Identifier(Vivatech.MODID, "background"), new Identifier(Vivatech.MODID, "bar"), 2, 3);
//        rootPanel.add(bar, 3, 2);

        WItemSlot inputSlot = WItemSlot.of(blockInventory, 0);
        rootPanel.add(inputSlot, 2, 2);

        WItemSlot outputSlot = WItemSlot.of(blockInventory, 1);
        rootPanel.add(outputSlot, 6, 2);

        rootPanel.add(createPlayerInventoryPanel(), 0, 5);

        rootPanel.validate(this);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 1;
    }
}
