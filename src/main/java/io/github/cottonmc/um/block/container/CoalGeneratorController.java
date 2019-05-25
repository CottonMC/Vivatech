package io.github.cottonmc.um.block.container;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPanel;
import io.github.cottonmc.um.client.gui.WFireGratingBar;
import io.github.cottonmc.um.client.gui.WLEDBar;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.recipe.RecipeType;

public class CoalGeneratorController extends CottonScreenController {
	WGridPanel machinePanel;
	WPanel playerInventoryPanel;
	
	public CoalGeneratorController(int syncId, PlayerInventory playerInventory, BlockContext context) {
		super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));
		
		WGridPanel rootPanel = (WGridPanel) getRootPanel();
		machinePanel = new WGridPanel();
		rootPanel.add(machinePanel, 0, 0);
		
		machinePanel.add(new WLabel(new TranslatableComponent("block.united-manufacturing.coal_generator"), 0xFF69c9b4), 0, 0);
		
		WBar grating = new WFireGratingBar(0, 1);
		machinePanel.add(grating, 1, 1, 7, 4);
		
		WItemSlot inputSlot = WItemSlot.of(blockInventory, 0);
		machinePanel.add(inputSlot, 4, 5);
		
		WLEDBar energyBar = new WLEDBar(2, 3, WBar.Direction.RIGHT);
		machinePanel.add(energyBar, 2, 6, 5, 1);
		
		playerInventoryPanel = createPlayerInventoryPanel();
		rootPanel.add(playerInventoryPanel, 0, 8);
		
		machinePanel.setSize(18*9, 18); //This will get stretched vertically to the intended size
		
		rootPanel.validate(this);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return -1; //There's no real result slot
	}
	
	@Override
	public void addPainters() {
		machinePanel.setBackgroundPainter(BackgroundPainter.createColorful(0xFF4f6577, 0.10f));
		playerInventoryPanel.setBackgroundPainter(BackgroundPainter.createColorful(0xFFC6C6C6, 0.20f)); //This is the default color with greatly-reduced bevel contrast
	}
}
