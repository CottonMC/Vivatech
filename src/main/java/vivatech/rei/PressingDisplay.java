package vivatech.rei;

import net.minecraft.util.Identifier;
import vivatech.recipe.PressingRecipe;

final class PressingDisplay extends TieredMachineDisplay<PressingRecipe> {
	
    public PressingDisplay(PressingRecipe recipe) {
		super(recipe);
	}
    
    @Override
    public Identifier getRecipeCategory() {
        return VivatechREIPlugin.PRESSING;
    }
}
