package vivatech.common.init;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.common.recipe.CrushingRecipe;
import vivatech.common.recipe.CuttingRecipe;
import vivatech.common.recipe.PressingRecipe;
import vivatech.api.recipe.AbstractProcessingRecipe;

@SuppressWarnings("unchecked")
public class VivatechRecipes {

    public static final RecipeType<PressingRecipe> PRESSING;
    public static final RecipeSerializer<PressingRecipe> PRESSING_SERIALIZER;
    public static final RecipeType<CrushingRecipe> CRUSHING;
    public static final RecipeSerializer<CrushingRecipe> CRUSHING_SERIALIZER;
    public static final RecipeType<CuttingRecipe> CUTTING;
    public static final RecipeSerializer<CuttingRecipe> CUTTING_SERIALIZER;
    public static final RecipeType<ProcessingRecipe> SMELTING_RECIPE_PROXY;

    static {
        PRESSING = buildRecipeType(PressingRecipe.ID);
        PRESSING_SERIALIZER = new AbstractProcessingRecipe.Serializer<>(PressingRecipe::new, 200);
        CRUSHING = buildRecipeType(CrushingRecipe.ID);
        CRUSHING_SERIALIZER = new AbstractProcessingRecipe.Serializer<>(CrushingRecipe::new, 100);
        CUTTING = buildRecipeType(CuttingRecipe.ID);
        CUTTING_SERIALIZER = new AbstractProcessingRecipe.Serializer<>(CuttingRecipe::new, 100);
        SMELTING_RECIPE_PROXY = (RecipeType<ProcessingRecipe>) (RecipeType<?>) RecipeType.SMELTING;
    }

    public static void initialize() {
        Registry.register(Registry.RECIPE_TYPE, PressingRecipe.ID, PRESSING);
        Registry.register(Registry.RECIPE_TYPE, CrushingRecipe.ID, CRUSHING);
        Registry.register(Registry.RECIPE_TYPE, CuttingRecipe.ID, CUTTING);
        Registry.register(Registry.RECIPE_SERIALIZER, PressingRecipe.ID, PRESSING_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, CrushingRecipe.ID, CRUSHING_SERIALIZER);
        Registry.register(Registry.RECIPE_SERIALIZER, CuttingRecipe.ID, CUTTING_SERIALIZER);
    }

    private static <T extends Recipe<?>> RecipeType<T> buildRecipeType(Identifier id) {
        return new RecipeType<T>() {
            public String toString() {
                return id.getPath();
            }
        };
    }
}
