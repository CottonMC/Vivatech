package vivatech.compat.rei;

import net.minecraft.util.Identifier;
import vivatech.common.recipe.PressingRecipe;

final class PressingDisplay extends TieredMachineDisplay<PressingRecipe> {
	
    public PressingDisplay(PressingRecipe recipe) {
		super(recipe);
	}
    
    @Override
    public Identifier getRecipeCategory() {
        return VivatechReiPlugin.PRESSING;
    }
}
