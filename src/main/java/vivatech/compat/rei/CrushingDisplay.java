package vivatech.compat.rei;

import net.minecraft.util.Identifier;
import vivatech.common.recipe.CrushingRecipe;

final class CrushingDisplay extends TieredMachineDisplay<CrushingRecipe> {

    CrushingDisplay(CrushingRecipe recipe) {
    	super(recipe);
    }

    @Override
    public Identifier getRecipeCategory() {
        return VivatechReiPlugin.CRUSHING;
    }
}
