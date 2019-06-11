package vivatech.init;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.recipe.CrushingRecipe;
import vivatech.recipe.PressingRecipe;
import vivatech.recipe.ProcessingRecipe;

public class VivatechRecipes {

    public static final RecipeType<PressingRecipe> PRESSING;
    public static final RecipeSerializer<PressingRecipe> PRESSING_SERIALIZER;
    public static final RecipeType<CrushingRecipe> CRUSHING;
    public static final RecipeSerializer<CrushingRecipe> CRUSHING_SERIALIZER;


    static {
        PRESSING = buildRecipeType(PressingRecipe.ID);
        PRESSING_SERIALIZER = new ProcessingRecipe.Serializer<>(PressingRecipe::new, 200);
        CRUSHING = buildRecipeType(CrushingRecipe.ID);
        CRUSHING_SERIALIZER = new ProcessingRecipe.Serializer<>(CrushingRecipe::new, 100);
    }

    public static void initialize() {
        Registry.register(Registry.RECIPE_TYPE, PressingRecipe.ID, PRESSING);
        Registry.register(Registry.RECIPE_TYPE, CrushingRecipe.ID, CRUSHING);
        Registry.register(Registry.RECIPE_SERIALIZER, PressingRecipe.ID, PRESSING_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, CrushingRecipe.ID, CRUSHING_SERIALIZER);
    }

    private static <T extends Recipe<?>> RecipeType<T> buildRecipeType(Identifier id) {
        return new RecipeType<T>() {
            public String toString() {
                return id.getPath();
            }
        };
    }
}
