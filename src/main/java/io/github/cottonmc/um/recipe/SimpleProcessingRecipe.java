package io.github.cottonmc.um.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.cottonmc.um.block.UMBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;

public class SimpleProcessingRecipe implements Recipe<Inventory> {
	public static final Serializer SERIALIZER = new Serializer();

	protected transient final Identifier identifier;
	protected final Ingredient ingredient;
	protected final ItemStack result;
	protected final int energy;
	protected int duration;

	public SimpleProcessingRecipe(Identifier id, Ingredient ingredient, ItemStack result, int energy, int duration) {
		this.identifier = id;
		this.ingredient = ingredient;
		this.result = result;
		this.energy = energy;
		this.duration = duration;
	}

	//implements Recipe {
	@Override
	public boolean matches(Inventory inventory, World world) {
		return ingredient.method_8093(inventory.getInvStack(0));
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return result.copy();
	}

	/** DO NOT MODIFY the returned itemStack! */
	@Override
	public ItemStack getOutput() {
		return result;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public Identifier getId() {
		return identifier;
	}

	@Override
	public RecipeSerializer<SimpleProcessingRecipe> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return UMRecipes.HAMMER_MILL;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public boolean fits(int gridWidth, int gridHeight) {
		return true;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(UMBlocks.HAMMER_MILL);
	}
	//}

	public static class Serializer implements RecipeSerializer<SimpleProcessingRecipe> {
		@Override
		public SimpleProcessingRecipe read(Identifier id, JsonObject json) {
			JsonElement ingredientElem = json.get("ingredient");
			if (ingredientElem==null) throw new JsonSyntaxException("Recipe must have an ingredient.");
			Ingredient input = UMRecipes.getIngredient(ingredientElem, "ingredient");

			JsonElement resultElem = json.get("result");
			if (resultElem==null) throw new JsonSyntaxException("Recipe must have a result.");
			ItemStack result = UMRecipes.getItemStack(resultElem, "result");

			int energy = JsonHelper.getInt(json, "energy", 0);
			int processTime = JsonHelper.getInt(json, "duration");

			System.out.println("Recipe '"+id+"' read successfully.");

			return new SimpleProcessingRecipe(id, input, result, energy, processTime);
		}

		@Override
		public SimpleProcessingRecipe read(Identifier id, PacketByteBuf buffer) {
			Ingredient ingredient = Ingredient.fromPacket(buffer);
			ItemStack result = buffer.readItemStack();
			int energy = buffer.readInt();
			int processTime = buffer.readInt();

			return new SimpleProcessingRecipe(id, ingredient, result, energy, processTime);
		}

		@Override
		public void write(PacketByteBuf buffer, SimpleProcessingRecipe recipe) {
			recipe.ingredient.write(buffer);
			buffer.writeItemStack(recipe.result);
			buffer.writeInt(recipe.energy);
			buffer.writeInt(recipe.duration);
		}
	}
}
