package vivatech.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.api.recipe.AbstractProcessingRecipe;
import vivatech.common.block.entity.CrusherBlockEntity;
import vivatech.common.init.VivatechRecipes;
import vivatech.api.tier.Tier;

public class CuttingRecipe extends AbstractProcessingRecipe {
	public static final Identifier ID = new Identifier(Vivatech.MOD_ID, "cutting");

	public CuttingRecipe(Identifier id, Ingredient input, ItemStack output, Tier minTier, float exp, int processTime, Identifier bonusLootTable) {
		super(id, input, output, minTier, exp, processTime, bonusLootTable);
	}

	@Override
	public RecipeType<?> getType() {
		return VivatechRecipes.CUTTING;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return VivatechRecipes.CUTTING_SERIALIZER;
	}
}
