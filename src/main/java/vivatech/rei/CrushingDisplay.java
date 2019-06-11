package vivatech.rei;

import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import vivatech.recipe.CrushingRecipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class CrushingDisplay implements RecipeDisplay<CrushingRecipe> {
    private final CrushingRecipe recipe;
    private final List<List<ItemStack>> input;
    private final List<ItemStack> output;
//    private final List<ItemStack> bonusStacks;

    CrushingDisplay(CrushingRecipe recipe) {
        this.recipe = recipe;
        input = recipe.getPreviewInputs().stream()
                .map(ingredient -> Arrays.asList(ingredient.getStackArray()))
                .collect(Collectors.toList());
        output = Collections.singletonList(recipe.getOutput());
        //bonusStacks = LootUtils.getAllStacks(recipe.getBonusLootTable());
    }

    @Override
    public Optional<CrushingRecipe> getRecipe() {
        return Optional.of(recipe);
    }

    @Override
    public List<List<ItemStack>> getInput() {
        return input;
    }

    @Override
    public List<ItemStack> getOutput() {
        return output;
    }

    @Override
    public Identifier getRecipeCategory() {
        return VivatechREIPlugin.CRUSHING;
    }

    /*public List<ItemStack> getBonusStacks() {
        return bonusStacks;
    }*/
}
