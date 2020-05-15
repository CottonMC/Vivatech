package vivatech.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.common.block.entity.CrusherBlockEntity;
import vivatech.common.init.VivatechRecipes;
import vivatech.api.tier.Tier;

public class CrushingRecipe extends ProcessingRecipe {
	public static final Identifier ID = new Identifier(Vivatech.MOD_ID, "crushing");

	public CrushingRecipe(Identifier id, Ingredient input, ItemStack output, Tier minTier, float exp, int processTime, Identifier bonusLootTable) {
		super(id, input, output, minTier, exp, processTime, bonusLootTable);
	}

	@Override
	public RecipeType<?> getType() {
		return VivatechRecipes.CRUSHING;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return VivatechRecipes.CRUSHING_SERIALIZER;
	}
	
	@Override
	public int getEnergyCost() {
		return (processTime / CrusherBlockEntity.TICK_PER_CONSUME) * CrusherBlockEntity.TICK_PER_CONSUME;
	}
}
