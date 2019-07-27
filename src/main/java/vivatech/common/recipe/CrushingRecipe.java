package vivatech.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.common.entity.CrusherEntity;
import vivatech.common.init.VivatechRecipes;
import vivatech.api.util.BlockTier;

public class CrushingRecipe extends ProcessingRecipe {
	public static final Identifier ID = new Identifier(Vivatech.MODID, "crushing");

	public CrushingRecipe(Identifier id, Ingredient input, ItemStack output, BlockTier minTier, float exp, int processTime, Identifier bonusLootTable) {
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
		return (processTime / CrusherEntity.TICK_PER_CONSUME) * CrusherEntity.TICK_PER_CONSUME;
	}
}
