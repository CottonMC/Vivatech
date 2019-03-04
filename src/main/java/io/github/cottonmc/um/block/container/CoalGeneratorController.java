package io.github.cottonmc.um.block.container;

import io.github.cottonmc.gui.CottonScreenController;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;

public class CoalGeneratorController extends CottonScreenController {


	public CoalGeneratorController(ContainerType<?> containerType, RecipeType<? extends Inventory> recipeType, int syncId, PlayerInventory playerInventory) {
		super(containerType, recipeType, syncId, playerInventory);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 0;
	}
}
