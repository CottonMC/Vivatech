package io.github.cottonmc.um.block.container;

import io.github.cottonmc.gui.CottonScreenController;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;

public class HammerMillController extends CottonScreenController {
	public HammerMillController(ContainerType<?> containerType, RecipeType<? extends Inventory> recipeType, int syncId, PlayerInventory playerInventory) {
		super(containerType, recipeType, syncId, playerInventory);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return 2;
	}
}
