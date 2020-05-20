package vivatech.mixin;

import net.minecraft.recipe.SmeltingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.api.tier.Tier;

@Mixin(SmeltingRecipe.class)
public abstract class SmeltingRecipeMixin implements ProcessingRecipe {
    @Override
    public Tier getMinTier() {
        return Tier.MINIMAL;
    }

    @Override
    public int getProcessTime() {
        return ((SmeltingRecipe) (Object) this).getCookTime();
    }
}
