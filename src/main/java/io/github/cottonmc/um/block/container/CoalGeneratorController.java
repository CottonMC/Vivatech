package io.github.cottonmc.um.block.container;

import io.github.cottonmc.gui.CottonScreenController;
import io.github.cottonmc.gui.client.BackgroundPainter;
import io.github.cottonmc.gui.widget.WGridPanel;
import io.github.cottonmc.gui.widget.WItemSlot;
import io.github.cottonmc.gui.widget.WLabel;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.TranslatableTextComponent;

public class CoalGeneratorController extends CottonScreenController {

	public CoalGeneratorController(int syncId, PlayerInventory playerInventory, BlockContext context) {
		super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));
		
		WGridPanel rootPanel = (WGridPanel) getRootPanel();
		
		rootPanel.add(new WLabel(new TranslatableTextComponent("block.united-manufacturing.coal_generator"), 0xFF69c9b4), 0, 0);
		
		//WBar grating = new WBar();
		
		WItemSlot inputSlot = WItemSlot.of(blockInventory, 0);
		rootPanel.add(inputSlot, 4, 3);
		
		rootPanel.add(this.createPlayerInventoryPanel(), 0, 5);
		
		rootPanel.validate(this);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return -1; //There's no real result slot
	}
	
	@Override
	public void addPainters() {
		this.getRootPanel().setBackgroundPainter(BackgroundPainter.createColorful(0xFF075745, 0.10f));
	}
}
