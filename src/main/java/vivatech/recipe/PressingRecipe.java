package vivatech.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import vivatech.Vivatech;
import vivatech.init.VivatechRecipes;

public class PressingRecipe extends ProcessingRecipe {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "pressing");

    public PressingRecipe(Identifier id, Ingredient input, ItemStack output, float exp, int processTime, Identifier bonusLootTable) {
        super(id, input, output, exp, processTime, bonusLootTable);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return VivatechRecipes.PRESSING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return VivatechRecipes.PRESSING;
    }
}
