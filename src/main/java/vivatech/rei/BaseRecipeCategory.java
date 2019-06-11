package vivatech.rei;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import vivatech.rei.widget.CottonSlotWidget;
import vivatech.rei.widget.ProgressBarWidget;

import java.awt.Rectangle;
import java.util.List;
import java.util.function.Supplier;

abstract class BaseRecipeCategory implements RecipeCategory<RecipeDisplay<Recipe<?>>> {
    private final Identifier id;
    private final String translationKey;

    BaseRecipeCategory(Identifier id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public String getCategoryName() {
        return I18n.translate(translationKey);
    }

    @Override
    public List<Widget> setupDisplay(Supplier<RecipeDisplay<Recipe<?>>> recipeDisplaySupplier, Rectangle bounds) {
        RecipeDisplay<?> display = recipeDisplaySupplier.get();
        int x = (int) bounds.getCenterX();
        int y = (int) bounds.getCenterY() - 9;

        return ImmutableList.of(
                new RecipeBaseWidget(bounds),
                new ProgressBarWidget(x - 18 - 5, y, 40, 18, 200),
                new CottonSlotWidget(x - 2 * 18 - 9, y, display.getInput().get(0), true, true),
                CottonSlotWidget.createBig(x + 2 * 18 - 9, y, display.getOutput(), true, true)
        );
    }
}
