package vivatech.common.init;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import vivatech.common.block.CoalGeneratorBlock;
import vivatech.common.block.CrusherBlock;
import vivatech.common.block.ElectricFurnaceBlock;
import vivatech.common.block.EnergyBankBlock;
import vivatech.common.block.EnergyConduitBlock;
import vivatech.common.block.PressBlock;
import vivatech.common.entity.CoalGeneratorEntity;
import vivatech.common.entity.CrusherEntity;
import vivatech.common.entity.ElectricFurnaceEntity;
import vivatech.common.entity.EnergyBankEntity;
import vivatech.common.entity.EnergyConduitEntity;
import vivatech.common.entity.PressEntity;
import vivatech.api.util.BlockTier;

public class VivatechEntities {
    public static final BlockEntityType<CoalGeneratorEntity> COAL_GENERATOR;
    public static final BlockEntityType<CrusherEntity> CRUSHER;
    public static final BlockEntityType<ElectricFurnaceEntity> ELECTRIC_FURNACE;
    public static final BlockEntityType<EnergyBankEntity> ENERGY_BANK;
    public static final BlockEntityType<EnergyConduitEntity> ENERGY_CONDUIT;
    public static final BlockEntityType<PressEntity> PRESS;

    static {
        COAL_GENERATOR = BlockEntityType.Builder.create(CoalGeneratorEntity::new,
                VivatechBlocks.COAL_GENERATOR).build(null);
        CRUSHER = BlockEntityType.Builder.create(CrusherEntity::new,
                VivatechBlocks.CRUSHER.toArray(new Block[BlockTier.values().length])).build(null);
        ELECTRIC_FURNACE = BlockEntityType.Builder.create(ElectricFurnaceEntity::new,
                VivatechBlocks.ELECTRIC_FURNACE.toArray(new Block[BlockTier.values().length])).build(null);
        ENERGY_BANK = BlockEntityType.Builder.create(EnergyBankEntity::new,
                VivatechBlocks.ENERGY_BANK).build(null);
        ENERGY_CONDUIT = BlockEntityType.Builder.create(EnergyConduitEntity::new,
                VivatechBlocks.ENERGY_CONDUIT.toArray(new Block[BlockTier.values().length])).build(null);
        PRESS = BlockEntityType.Builder.create(PressEntity::new,
                VivatechBlocks.PRESS.toArray(new Block[BlockTier.values().length])).build(null);
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK_ENTITY, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK_ENTITY, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK_ENTITY, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.BLOCK_ENTITY, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.BLOCK_ENTITY, PressBlock.ID, PRESS);
    }
}
