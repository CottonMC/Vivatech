package vivatech.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import vivatech.block.*;
import vivatech.entity.*;

public class VivatechEntities {
    public static final BlockEntityType<EnergyConduitEntity> ENERGY_CONDUIT;
    public static final BlockEntityType<CoalGeneratorEntity> COAL_GENERATOR;
    public static final BlockEntityType<CrusherEntity> CRUSHER;
    public static final BlockEntityType<ElectricFurnaceEntity> ELECTRIC_FURNACE;
    public static final BlockEntityType<EnergyBankEntity> ENERGY_BANK;
    public static final BlockEntityType<PressEntity> PRESS;

    static {
        ENERGY_CONDUIT = BlockEntityType.Builder.create(EnergyConduitEntity::new, VivatechBlocks.ENERGY_CONDUIT).build(null);
        COAL_GENERATOR = BlockEntityType.Builder.create(CoalGeneratorEntity::new, VivatechBlocks.COAL_GENERATOR).build(null);
        CRUSHER = BlockEntityType.Builder.create(CrusherEntity::new, VivatechBlocks.CRUSHER).build(null);
        ELECTRIC_FURNACE = BlockEntityType.Builder.create(ElectricFurnaceEntity::new, VivatechBlocks.ELECTRIC_FURNACE).build(null);
        ENERGY_BANK = BlockEntityType.Builder.create(EnergyBankEntity::new, VivatechBlocks.ENERGY_BANK).build(null);
        PRESS = BlockEntityType.Builder.create(PressEntity::new, VivatechBlocks.PRESS).build(null);
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK_ENTITY, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.BLOCK_ENTITY, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK_ENTITY, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK_ENTITY, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.BLOCK_ENTITY, PressBlock.ID, PRESS);
    }
}
