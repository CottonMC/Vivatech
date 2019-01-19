package io.github.cottonmc.um.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class HammerMillRecipe implements Recipe<Inventory> {
	protected final RecipeType<?> type;
	protected final Identifier id;
	protected final String group;
	protected final Ingredient input;
	protected final ItemStack output;
	protected final float experience;
	protected final int processTime;

	public HammerMillRecipe(RecipeType<?> type, Identifier id, String group, Ingredient input, ItemStack output, float xp, int processTime) {
		this.type = type;
		this.id = id;
		this.group = group;
		this.input = input;
		this.output = output;
		this.experience = xp;
		this.processTime = processTime;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return this.input.matches(inventory.getInvStack(0));
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return this.output.copy();
	}

	@Override
	public boolean fits(int x, int y) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return null;
	}

	@Override
	public RecipeType<?> getType() {
		return this.type;
	}

	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> inputs = DefaultedList.create();
		inputs.add(this.input);
		return inputs;
	}

	public float getExperience() {
		return experience;
	}

	public int getProcessTime() {
		return processTime;
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return null;
	}
}
