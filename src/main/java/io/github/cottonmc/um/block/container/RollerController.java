package io.github.cottonmc.um.block.container;

import io.github.cottonmc.cotton.gui.CottonScreenController;
import io.github.cottonmc.um.block.entity.RollerEntity;
import io.github.cottonmc.um.recipe.UMRecipes;
import net.minecraft.container.ContainerType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;

public class RollerController extends CottonScreenController {
	public RollerController(ContainerType<?> containerType, RecipeType<? extends Inventory> recipeType, int syncId, PlayerInventory playerInventory) {
		super(UMRecipes.ROLLER, syncId, playerInventory);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return RollerEntity.SLOT_RESULT;
	}
}
