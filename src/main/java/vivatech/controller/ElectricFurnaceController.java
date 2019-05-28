package vivatech.controller;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.container.BlockContext;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.Vivatech;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.util.StringHelper;

public class ElectricFurnaceController extends CottonScreenController {
    public ElectricFurnaceController(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WGridPanel rootPanel = (WGridPanel) getRootPanel();
        rootPanel.add(new WLabel(StringHelper.getTranslatableComponent("block", ElectricFurnaceBlock.ID),
                WLabel.DEFAULT_TEXT_COLOR), 0, 0);

        rootPanel.add(new WLabel(String.format("%s/%s", propertyDelegate.get(0), propertyDelegate.get(1)),
                WLabel.DEFAULT_TEXT_COLOR), 0, 1);

//        rootPanel.add(new WBar(new Identifier(Vivatech.MODID, "energy_bar_bg"), new Identifier(Vivatech.MODID, "energy_bar"),
//                propertyDelegate.get(0), propertyDelegate.get(1)), 1, 1);

        WItemSlot inputSlot = WItemSlot.of(blockInventory, 0);
        rootPanel.add(inputSlot, 2, 2);

//        rootPanel.add(new WBar(new Identifier(Vivatech.MODID, "progress_bar_bg"), new Identifier(Vivatech.MODID, "progress_bar"),
//                propertyDelegate.get(2), propertyDelegate.get(3)), 3, 2);

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
