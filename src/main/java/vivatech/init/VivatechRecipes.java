package vivatech.init;

import io.github.cottonmc.cotton.datapack.recipe.ProcessingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.recipe.PressingRecipe;

public class VivatechRecipes implements Initializable {

    public static final RecipeType<PressingRecipe> PRESSING;
    public static final RecipeSerializer<PressingRecipe> PRESSING_SERIALIZER;

    static {
        PRESSING = buildRecipeType(PressingRecipe.ID);
        PRESSING_SERIALIZER = new ProcessingRecipe.Serializer<>(PressingRecipe::new, 200);
    }

    public void initialize() {
        Registry.register(Registry.RECIPE_TYPE, PressingRecipe.ID, PRESSING);
        Registry.register(Registry.RECIPE_SERIALIZER, PressingRecipe.ID, PRESSING_SERIALIZER);
    }

    private static <T extends Recipe<?>> RecipeType<T> buildRecipeType(Identifier id) {
        return new RecipeType<T>() {
            public String toString() {
                return id.getPath();
            }
        };
    }
}
