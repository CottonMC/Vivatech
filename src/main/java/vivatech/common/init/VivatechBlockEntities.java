package vivatech.common.init;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.api.tier.Tier;
import vivatech.common.block.EnergyBankBlock;
import vivatech.common.block.EnergyConnectorBlock;
import vivatech.common.block.SterlingGeneratorBlock;
import vivatech.common.block.entity.EnergyBankBlockEntity;
import vivatech.common.block.entity.EnergyConnectorBlockEntity;
import vivatech.common.block.entity.SimpleProcessingMachineBlockEntity;
import vivatech.common.block.entity.SterlingGeneratorBlockEntity;
import vivatech.data.BlockData;

import java.util.function.Supplier;

public class VivatechBlockEntities {
    public static final BlockEntityType<SterlingGeneratorBlockEntity> STERLING_GENERATOR;
    public static final BlockEntityType<EnergyBankBlockEntity> ENERGY_BANK;
    public static final BlockEntityType<EnergyConnectorBlockEntity> CONNECTOR;
    public static final BlockEntityType<SimpleProcessingMachineBlockEntity> CRUSHER;
    public static final BlockEntityType<SimpleProcessingMachineBlockEntity> ELECTRIC_FURNACE;
    public static final BlockEntityType<SimpleProcessingMachineBlockEntity> PRESS;

    static {
        STERLING_GENERATOR = BlockEntityType.Builder.create(SterlingGeneratorBlockEntity::new,
            VivatechBlocks.STERLING_GENERATOR).build(null);
        ENERGY_BANK = BlockEntityType.Builder.create(EnergyBankBlockEntity::new,
            VivatechBlocks.ENERGY_BANK).build(null);
        CONNECTOR = BlockEntityType.Builder.create(EnergyConnectorBlockEntity::new,
            VivatechBlocks.CONNECTOR.toArray(new Block[Tier.values().length])).build(null);
        CRUSHER = createSimpleProcessingMachine(
            () -> VivatechBlockEntities.CRUSHER,
            VivatechRecipes.CRUSHING,
            VivatechBlocks.CRUSHER.toArray(new Block[Tier.values().length])
        );
        ELECTRIC_FURNACE = createSimpleProcessingMachine(
            () -> VivatechBlockEntities.ELECTRIC_FURNACE,
            VivatechRecipes.SMELTING_RECIPE_PROXY,
            VivatechBlocks.ELECTRIC_FURNACE.toArray(new Block[Tier.values().length])
        );
        PRESS = createSimpleProcessingMachine(
            () -> VivatechBlockEntities.PRESS,
            VivatechRecipes.PRESSING,
            VivatechBlocks.PRESS.toArray(new Block[Tier.values().length])
        );
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, SterlingGeneratorBlock.ID, STERLING_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, EnergyConnectorBlock.ID, CONNECTOR);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BlockData.Ids.CRUSHER, CRUSHER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BlockData.Ids.ELECTRIC_FURNACE, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, BlockData.Ids.PRESS, PRESS);
    }

    // Self is required to be wrapped to effectively pass by reference and avoid NPEs
    // Credit to B0undarybreaker
    private static BlockEntityType<SimpleProcessingMachineBlockEntity> createSimpleProcessingMachine(
        Supplier<BlockEntityType<SimpleProcessingMachineBlockEntity>> self,
        RecipeType<? extends ProcessingRecipe> recipeType,
        Block[] blocks) {
        return BlockEntityType.Builder
            .create(() -> new SimpleProcessingMachineBlockEntity(self.get(), recipeType), blocks)
            .build(null);
    }
}
