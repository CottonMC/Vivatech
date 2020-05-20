package vivatech.common.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.RecipeType;
import vivatech.api.block.entity.AbstractProcessingMachineBlockEntity;
import vivatech.api.recipe.ProcessingRecipe;

public class SimpleProcessingMachineBlockEntity extends AbstractProcessingMachineBlockEntity {
    public SimpleProcessingMachineBlockEntity(BlockEntityType<?> type, RecipeType<? extends ProcessingRecipe> recipeType) {
        super(type, recipeType);
    }
}
