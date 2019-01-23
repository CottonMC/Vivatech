package io.github.cottonmc.um.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class MachineRecipeSerializer<T extends AbstractMachineRecipe> implements RecipeSerializer<T> {
	private final int cookingTime;
	private final MachineRecipeSerializer.RecipeFactory<T> recipeFactory;

	public MachineRecipeSerializer(MachineRecipeSerializer.RecipeFactory<T> cookingRecipeSerializer$RecipeFactory_1, int time) {
		this.cookingTime = time;
		this.recipeFactory = cookingRecipeSerializer$RecipeFactory_1;
	}

	public T read(Identifier id, JsonObject json) {
		String group = JsonHelper.getString(json, "group", "");
		JsonElement element = JsonHelper.hasArray(json, "ingredient") ? JsonHelper.getArray(json, "ingredient") : JsonHelper.getObject(json, "ingredient");
		Ingredient input = Ingredient.fromJson(json);
		String result = JsonHelper.getString(json, "result");
		Identifier resultId = new Identifier(result);
		if (!Registry.ITEM.contains(resultId)) {
			throw new IllegalStateException("Item: " + result + " does not exist");
		} else {
			ItemStack output = new ItemStack(Registry.ITEM.get(resultId));
			float xp = JsonHelper.getFloat(json, "experience", 0.0F);
			int time = JsonHelper.getInt(json, "processtime", this.cookingTime);
			return this.recipeFactory.create(id, group, input, output, xp, time);
		}
	}

	public T read(Identifier id, PacketByteBuf buf) {
		String group = buf.readString(32767);
		Ingredient input = Ingredient.fromPacket(buf);
		ItemStack output = buf.readItemStack();
		float xp = buf.readFloat();
		int time = buf.readVarInt();
		return this.recipeFactory.create(id, group, input, output, xp, time);
	}

	public void write(PacketByteBuf buf, T recipe) {
		buf.writeString(recipe.group);
		recipe.input.write(buf);
		buf.writeItemStack(recipe.output);
		buf.writeFloat(recipe.experience);
		buf.writeVarInt(recipe.processTime);
	}

	interface RecipeFactory<T extends AbstractMachineRecipe> {
		T create(Identifier id, String group, Ingredient input, ItemStack output, float xp, int time);
	}
}

