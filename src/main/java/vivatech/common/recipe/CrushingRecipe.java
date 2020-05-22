package vivatech.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.api.recipe.AbstractProcessingRecipe;
import vivatech.api.tier.Tier;
import vivatech.common.Vivatech;
import vivatech.common.init.VivatechRecipes;

public class CrushingRecipe extends AbstractProcessingRecipe {
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
}
