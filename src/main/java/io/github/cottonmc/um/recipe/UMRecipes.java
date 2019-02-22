package io.github.cottonmc.um.recipe;

import java.util.Collection;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import io.github.cottonmc.um.recipe.polytype.PolyTypeRecipe;
import io.github.cottonmc.um.recipe.polytype.PolyTypeRecipeSerializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;

public class UMRecipes {
	public static RecipeType<HammerMillRecipe> HAMMER_MILL;

	public static Registry<PolyTypeRecipeSerializer> POLY_TYPE_RECIPE_SERIALIZER;

	//public static RecipeSerializer<HammerMillRecipe> HAMMER_MILL_SERIALIZER = register("hammer_mill", new MachineRecipeSerializer<HammerMillRecipe>(HammerMillRecipe::new, 200));

	public static void init() {
		HAMMER_MILL = register("united-manufacturing:hammer_mill");
		Registry.register(Registry.RECIPE_SERIALIZER, "united-manufacturing:hammer_mill", HammerMillRecipe.SERIALIZER);
	}
	
	/*
	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, "unitedmanufacturing:"+name, serializer);
	}*/

	public static <T extends Recipe<?>> RecipeType<T> register(final String id) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(id), new RecipeType<T>() {
			public String toString() {
				return id;
			}
		});
	}
	
	public static Ingredient getIngredient(JsonElement elem, String elemKey) throws JsonSyntaxException {
		if (elem instanceof JsonPrimitive) {
			Item item = JsonHelper.asItem(elem, elemKey);
			return Ingredient.ofStacks(new ItemStack(item));
		} else {
			return Ingredient.fromJson(elem);
		}
	}
	
	/**
	 * Gets an ItemStack from the specified JsonElement. This should really already exist in JsonHelper.
	 * @param elem      The element to turn into an ItemStack
	 * @param elemKey   The key the element has on its parent object, for constructing error messages
	 * @return          The ItemStack expressed by this Json. Will never be nonnull or an empty ItemStack.
	 * @throws JsonSyntaxException If the ItemStack expressed is empty or invalid.
	 */
	@Nonnull
	public static ItemStack getItemStack(JsonElement elem, String elemKey) throws JsonSyntaxException {
		if (elem instanceof JsonPrimitive) {
			Item item = JsonHelper.asItem(elem, elemKey);
			return new ItemStack(item);
		} else if (elem instanceof JsonObject) {
			JsonObject obj = (JsonObject)elem;
			int count = JsonHelper.getInt(obj, "count", 1);
			int meta = JsonHelper.getInt(obj, "meta", 0);
			
			if (obj.has("item")) {
				Item item = JsonHelper.asItem(elem, elemKey);
				ItemStack stack = new ItemStack(item, count);
				if (meta!=0) stack.setDamage(meta);
				return stack;
			} else if (obj.has("tag")) {
				String tagName = JsonHelper.getString(obj, "tag");
				Tag<Item> tag = ItemTags.getContainer().get(new Identifier(tagName));
				if (tag==null) throw new JsonSyntaxException("Can't find a tag named '"+tagName+"'.");
				Collection<Item> tagValues = tag.values();
				if (tagValues.size()==0) throw new JsonSyntaxException("Tag '"+tagName+"' has no Items associated with it.");
				Item firstEntry = tagValues.iterator().next();
				ItemStack stack = new ItemStack(firstEntry, count);
				if (meta!=0) stack.setDamage(meta);
				return stack;
			} else {
				throw new JsonSyntaxException("Expected " + elemKey + " to contain an 'item' or 'tag' key, found neither.");
			}
		} else {
			throw new JsonSyntaxException("Expected " + elemKey + " to be a valid item string or object, found "+JsonHelper.getType(elem));
		}
	}
}
