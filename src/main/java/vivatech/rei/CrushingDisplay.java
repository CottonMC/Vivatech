package vivatech.rei;

import net.minecraft.util.Identifier;
import vivatech.recipe.CrushingRecipe;

final class CrushingDisplay extends TieredMachineDisplay<CrushingRecipe> {

    CrushingDisplay(CrushingRecipe recipe) {
    	super(recipe);
    }

    @Override
    public Identifier getRecipeCategory() {
        return VivatechREIPlugin.CRUSHING;
    }
}
