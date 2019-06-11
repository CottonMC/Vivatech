package vivatech.rei;

import me.shedaniel.rei.api.REIPluginEntry;
import me.shedaniel.rei.api.RecipeHelper;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import vivatech.recipe.CrushingRecipe;
import vivatech.recipe.PressingRecipe;

public final class VivatechREIPlugin implements REIPluginEntry {
    public static final Identifier ID = new Identifier("vivatech", "rei_plugin");
    public static final Identifier CRUSHING = new Identifier("vivatech", "crushing");
    public static final Identifier PRESSING = new Identifier("vivatech", "pressing");

    @Override
    public Identifier getPluginIdentifier() {
        return ID;
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new CrushingCategory());
        recipeHelper.registerCategory(new PressingCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        for (Recipe<?> recipe : recipeHelper.getAllSortedRecipes()) {
            if (recipe instanceof PressingRecipe) {
                recipeHelper.registerDisplay(PRESSING, new PressingDisplay((PressingRecipe) recipe));
            } else if (recipe instanceof CrushingRecipe) {
                recipeHelper.registerDisplay(CRUSHING, new CrushingDisplay((CrushingRecipe) recipe));
            }
        }
    }
}
