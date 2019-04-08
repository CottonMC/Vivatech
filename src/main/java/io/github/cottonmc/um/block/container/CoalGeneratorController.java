package io.github.cottonmc.um.block.container;

import io.github.cottonmc.gui.CottonScreenController;
import io.github.cottonmc.gui.client.BackgroundPainter;
import io.github.cottonmc.um.block.entity.CoalGeneratorEntity;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;

public class CoalGeneratorController extends CottonScreenController {


	public CoalGeneratorController(int syncId, PlayerInventory playerInventory, BlockContext context) {
		super(null, RecipeType.SMELTING, syncId, playerInventory);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return CoalGeneratorEntity.SLOT_FUEL; //Not really, but there technically is none.
	}

	@Override
	public void setup() {
		//TODO: Put panels in.
	}
	
	@Override
	public void addPainters() {
		this.getRootPanel().setBackgroundPainter(BackgroundPainter.createColorful(0xFF075745, 0.10f));
	}
}
