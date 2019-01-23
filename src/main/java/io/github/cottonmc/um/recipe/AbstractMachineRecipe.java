package io.github.cottonmc.um.recipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class AbstractMachineRecipe implements Recipe<Inventory> {
	protected final RecipeType<?> type;
	protected final Identifier id;
	protected final String group;
	protected final Ingredient input;
	protected final ItemStack output;
	protected final float experience;
	protected final int processTime;

	public AbstractMachineRecipe(RecipeType<?> type, Identifier id, String group, Ingredient input, ItemStack output, float experience, int processTime) {
		this.type = type;
		this.id = id;
		this.group = group;
		this.input = input;
		this.output = output;
		this.experience = experience;
		this.processTime = processTime;
	}

	public boolean matches(Inventory inv, World world) {
		return this.input.matches(inv.getInvStack(0));
	}

	public ItemStack craft(Inventory inv) {
		return this.output.copy();
	}

	@Environment(EnvType.CLIENT)
	public boolean fits(int x, int y) {
		return true;
	}

	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> ingredientList = DefaultedList.create();
		ingredientList.add(this.input);
		return ingredientList;
	}

	public float getExperience() {
		return this.experience;
	}

	public ItemStack getOutput() {
		return this.output;
	}

	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return this.group;
	}

	public int getProcessTime() {
		return this.processTime;
	}

	public Identifier getId() {
		return this.id;
	}

	public RecipeType<?> getType() {
		return this.type;
	}
}
