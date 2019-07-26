package vivatech.compat.rei;

import com.google.common.collect.ImmutableList;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.gui.widget.RecipeBaseWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import vivatech.compat.rei.widget.CottonSlotWidget;
import vivatech.compat.rei.widget.LabelWidget;
import vivatech.compat.rei.widget.ProgressBarWidget;
import vivatech.api.util.MachineTier;

import java.awt.Rectangle;
import java.util.ArrayList;
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
        
        MachineTier tier = MachineTier.MINIMAL;
        int energyCost = 0;
        if (display instanceof TieredMachineDisplay) {
        	tier = ((TieredMachineDisplay<?>) display).getMinTier();
        	energyCost = ((TieredMachineDisplay<?>)display).getEnergyCost();
        }
        
        ArrayList<Widget> widgets = new ArrayList<>();
        widgets.add(new RecipeBaseWidget(bounds));
        widgets.add(new ProgressBarWidget(x - 18 - 5, y, 40, 18, 200));
        widgets.add(new CottonSlotWidget(x - 2 * 18 - 9, y, display.getInput().get(0), true, true));
        widgets.add(CottonSlotWidget.createBig(x + 2 * 18 - 9, y, display.getOutput(), true, true));
        
        if (energyCost!=0) {
        	Text energyComponent = new TranslatableText("info.vivatech.energy_cost", energyCost)
        			.formatted(Formatting.DARK_GRAY);
        	widgets.add(new LabelWidget(x - (2*18) - 7, y + (2*18), energyComponent));
        }
        
        if (tier!=MachineTier.MINIMAL) {
        	Text tierComponent = new TranslatableText("info.vivatech.tier", new TranslatableText("info.vivatech.tier."+tier.getAffix()))
        			.formatted(Formatting.DARK_GRAY);
        	widgets.add(new LabelWidget(x - (2*18) - 7, y + (3*18), tierComponent));
        }
        	
        
        
        return ImmutableList.copyOf(widgets);
    }
}
