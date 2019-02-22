package io.github.cottonmc.um.recipe.polytype;

import io.github.prospector.silk.fluid.FluidInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface PolyTypeRecipe<C extends Inventory> {
	boolean matches(C inv, World world);

	ItemStack craft(C inv);
	FluidInstance produce(C inv);

	@Environment(EnvType.CLIENT)
	boolean fits(int x, int y);

	ItemStack getItemOutput();
	FluidInstance getFluidOutput();

	default DefaultedList<ItemStack> getRemainingStacks(C inv) {
		DefaultedList<ItemStack> stacks = DefaultedList.create(inv.getInvSize(), ItemStack.EMPTY);

		for(int i = 0; i < stacks.size(); ++i) {
			Item item = inv.getInvStack(i).getItem();
			if (item.hasRecipeRemainder()) {
				stacks.set(i, new ItemStack(item.getRecipeRemainder()));
			}
		}

		return stacks;
	}

	default DefaultedList<Ingredient> getPreviewItemInputs() {
		return DefaultedList.create();
	}
	default DefaultedList<FluidIngredient> getPreviewFluidInputs() {
		return DefaultedList.create();
	}

	default boolean isIgnoredInRecipeBook() {
		return false;
	}

	@Environment(EnvType.CLIENT)
	default String getGroup() {
		return "";
	}

	@Environment(EnvType.CLIENT)
	default ItemStack getRecipeKindIcon() {
		return new ItemStack(Blocks.CRAFTING_TABLE);
	}

	Identifier getId();

	PolyTypeRecipeSerializer<?> getSerializer();

	RecipeType<?> getType();
}
