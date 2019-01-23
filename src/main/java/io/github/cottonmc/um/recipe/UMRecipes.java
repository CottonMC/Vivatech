package io.github.cottonmc.um.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class UMRecipes {
	public static RecipeType<HammerMillRecipe> HAMMER_MILL = register("hammer_mill");

	public static RecipeSerializer<HammerMillRecipe> HAMMER_MILL_SERIALIZER = register("hammer_mill", new MachineRecipeSerializer(HammerMillRecipe::new, 200));

	public void init() {

	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, "unitedmanufacturing:"+name, serializer);
	}

	public static <T extends Recipe<?>> RecipeType<T> register(final String id) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(id), new RecipeType<T>() {
			public String toString() {
				return id;
			}
		});
	}
}
