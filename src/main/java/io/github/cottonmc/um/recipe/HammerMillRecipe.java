package io.github.cottonmc.um.recipe;


import io.github.cottonmc.um.block.UMBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class HammerMillRecipe extends AbstractMachineRecipe {


	public HammerMillRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int processTime) {
		super(UMRecipes.HAMMER_MILL, id, group, input, output, experience, processTime);
	}

	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(UMBlocks.HAMMER_MILL);
	}

	public RecipeSerializer<?> getSerializer() {
		return UMRecipes.HAMMER_MILL_SERIALIZER;
	}
}
