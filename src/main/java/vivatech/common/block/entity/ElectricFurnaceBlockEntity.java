package vivatech.common.block.entity;

import net.minecraft.recipe.RecipeType;
import vivatech.api.block.entity.AbstractProcessingMachineBlockEntity;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.common.init.VivatechEntities;

public class ElectricFurnaceBlockEntity extends AbstractProcessingMachineBlockEntity {
    public ElectricFurnaceBlockEntity() {
        super(VivatechEntities.ELECTRIC_FURNACE, (RecipeType<? extends ProcessingRecipe>) (RecipeType<?>) RecipeType.SMELTING);
    }
}
