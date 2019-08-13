package vivatech.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.common.block.entity.PressBlockEntity;
import vivatech.common.init.VivatechRecipes;
import vivatech.api.tier.Tier;

public class PressingRecipe extends ProcessingRecipe {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "pressing");

    public PressingRecipe(Identifier id, Ingredient input, ItemStack output, Tier minTier, float exp, int processTime, Identifier bonusLootTable) {
        super(id, input, output, minTier, exp, processTime, bonusLootTable);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return VivatechRecipes.PRESSING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return VivatechRecipes.PRESSING;
    }
    
    @Override
	public int getEnergyCost() {
		return (processTime / PressBlockEntity.TICK_PER_CONSUME) * PressBlockEntity.TICK_PER_CONSUME;
	}
}
