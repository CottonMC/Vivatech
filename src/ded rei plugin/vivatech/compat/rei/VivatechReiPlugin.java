package vivatech.compat.rei;

import me.shedaniel.rei.api.REIPluginEntry;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import vivatech.common.recipe.CrushingRecipe;
import vivatech.common.recipe.PressingRecipe;

public final class VivatechReiPlugin implements REIPluginV0 {
    public static final Identifier ID = new Identifier("vivatech", "rei_plugin");
    public static final Identifier CRUSHING = new Identifier("vivatech", "crushing");
    public static final Identifier PRESSING = new Identifier("vivatech", "pressing");

    @Override
    public Identifier getPluginIdentifier() {
        return ID;
    }
    
    @Override
    public SemanticVersion getMinimumVersion() throws VersionParsingException {
        return SemanticVersion.parse("3.0-pre");
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
