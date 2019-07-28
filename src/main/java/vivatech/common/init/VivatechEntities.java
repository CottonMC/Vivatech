package vivatech.common.init;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import vivatech.common.block.SterlingGeneratorBlock;
import vivatech.common.block.CrusherBlock;
import vivatech.common.block.ElectricFurnaceBlock;
import vivatech.common.block.EnergyBankBlock;
import vivatech.common.block.EnergyConduitBlock;
import vivatech.common.block.PressBlock;
import vivatech.common.block.entity.SterlingGeneratorBlockEntity;
import vivatech.common.block.entity.CrusherBlockEntity;
import vivatech.common.block.entity.ElectricFurnaceBlockEntity;
import vivatech.common.block.entity.EnergyBankBlockEntity;
import vivatech.common.block.entity.EnergyConduitBlockEntity;
import vivatech.common.block.entity.PressBlockEntity;
import vivatech.api.util.BlockTier;

public class VivatechEntities {
    public static final BlockEntityType<SterlingGeneratorBlockEntity> COAL_GENERATOR;
    public static final BlockEntityType<CrusherBlockEntity> CRUSHER;
    public static final BlockEntityType<ElectricFurnaceBlockEntity> ELECTRIC_FURNACE;
    public static final BlockEntityType<EnergyBankBlockEntity> ENERGY_BANK;
    public static final BlockEntityType<EnergyConduitBlockEntity> ENERGY_CONDUIT;
    public static final BlockEntityType<PressBlockEntity> PRESS;

    static {
        COAL_GENERATOR = BlockEntityType.Builder.create(SterlingGeneratorBlockEntity::new,
                VivatechBlocks.COAL_GENERATOR).build(null);
        CRUSHER = BlockEntityType.Builder.create(CrusherBlockEntity::new,
                VivatechBlocks.CRUSHER.toArray(new Block[BlockTier.values().length])).build(null);
        ELECTRIC_FURNACE = BlockEntityType.Builder.create(ElectricFurnaceBlockEntity::new,
                VivatechBlocks.ELECTRIC_FURNACE.toArray(new Block[BlockTier.values().length])).build(null);
        ENERGY_BANK = BlockEntityType.Builder.create(EnergyBankBlockEntity::new,
                VivatechBlocks.ENERGY_BANK).build(null);
        ENERGY_CONDUIT = BlockEntityType.Builder.create(EnergyConduitBlockEntity::new,
                VivatechBlocks.ENERGY_CONDUIT.toArray(new Block[BlockTier.values().length])).build(null);
        PRESS = BlockEntityType.Builder.create(PressBlockEntity::new,
                VivatechBlocks.PRESS.toArray(new Block[BlockTier.values().length])).build(null);
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK_ENTITY, SterlingGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK_ENTITY, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK_ENTITY, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.BLOCK_ENTITY, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.BLOCK_ENTITY, PressBlock.ID, PRESS);
    }
}
