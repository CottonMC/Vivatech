package vivatech.common.init;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;
import vivatech.api.recipe.ProcessingRecipe;
import vivatech.api.tier.Tier;
import vivatech.common.block.*;
import vivatech.common.block.entity.*;

public class VivatechEntities {
    public static final BlockEntityType<SterlingGeneratorBlockEntity> COAL_GENERATOR;
    public static final BlockEntityType<CrusherBlockEntity> CRUSHER;
    public static BlockEntityType<SimpleProcessingMachineBlockEntity> ELECTRIC_FURNACE = null;
    public static final BlockEntityType<EnergyBankBlockEntity> ENERGY_BANK;
    public static final BlockEntityType<PressBlockEntity> PRESS;
    public static final BlockEntityType<EnergyConnectorBlockEntity> CONNECTOR;

    static {
        COAL_GENERATOR = BlockEntityType.Builder.create(SterlingGeneratorBlockEntity::new,
            VivatechBlocks.COAL_GENERATOR).build(null);
        CRUSHER = BlockEntityType.Builder.create(CrusherBlockEntity::new,
            VivatechBlocks.CRUSHER.toArray(new Block[Tier.values().length])).build(null);
        ELECTRIC_FURNACE = BlockEntityType.Builder.create(
            () -> new SimpleProcessingMachineBlockEntity(ELECTRIC_FURNACE, (RecipeType<? extends ProcessingRecipe>) (RecipeType<?>) RecipeType.SMELTING),
            VivatechBlocks.ELECTRIC_FURNACE.toArray(new Block[Tier.values().length])).build(null);
        ENERGY_BANK = BlockEntityType.Builder.create(EnergyBankBlockEntity::new,
            VivatechBlocks.ENERGY_BANK).build(null);
        PRESS = BlockEntityType.Builder.create(PressBlockEntity::new,
            VivatechBlocks.PRESS.toArray(new Block[Tier.values().length])).build(null);
        CONNECTOR = BlockEntityType.Builder.create(EnergyConnectorBlockEntity::new,
            VivatechBlocks.CONNECTOR.toArray(new Block[Tier.values().length])).build(null);
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, SterlingGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, PressBlock.ID, PRESS);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, EnergyConnectorBlock.ID, CONNECTOR);
    }
}
