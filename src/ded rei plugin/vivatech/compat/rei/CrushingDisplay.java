package vivatech.compat.rei;

import me.shedaniel.rei.api.EntryStack;
import net.minecraft.util.Identifier;
import vivatech.common.recipe.CrushingRecipe;

import java.util.List;

final class CrushingDisplay extends TieredMachineDisplay<CrushingRecipe> {

    CrushingDisplay(CrushingRecipe recipe) {
    	super(recipe);
    }

    @Override
    public List<List<EntryStack>> getInputEntries() {
        return null;
    }

    @Override
    public List<EntryStack> getOutputEntries() {
        return null;
    }

    @Override
    public Identifier getRecipeCategory() {
        return VivatechReiPlugin.CRUSHING;
    }
}
