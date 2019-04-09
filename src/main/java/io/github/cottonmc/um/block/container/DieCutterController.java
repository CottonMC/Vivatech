package io.github.cottonmc.um.block.container;

import io.github.cottonmc.gui.CottonScreenController;
import io.github.cottonmc.um.recipe.UMRecipes;
import net.minecraft.entity.player.PlayerInventory;

public class DieCutterController extends CottonScreenController {
	public DieCutterController(int syncId, PlayerInventory playerInventory) {
		super(UMRecipes.DIE_CUTTER, syncId, playerInventory);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 2;
	}
}