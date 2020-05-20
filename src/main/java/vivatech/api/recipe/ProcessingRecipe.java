package vivatech.api.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import vivatech.api.tier.Tier;

public interface ProcessingRecipe extends Recipe<Inventory> {
    Tier getMinTier();

    float getExperience();

    int getProcessTime();

    default Identifier getBonusLootTable() {
        return null;
    }
}
