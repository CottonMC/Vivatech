package vivatech.init;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.CrusherBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.block.EnergyBankBlock;
import vivatech.block.EnergyConduitBlock;
import vivatech.block.PressBlock;
import vivatech.entity.CoalGeneratorEntity;
import vivatech.entity.CrusherEntity;
import vivatech.entity.ElectricFurnaceEntity;
import vivatech.entity.EnergyBankEntity;
import vivatech.entity.EnergyConduitEntity;
import vivatech.entity.PressEntity;
import vivatech.util.MachineTier;

public class VivatechEntities implements Initializable {
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
        ELECTRIC_FURNACE = BlockEntityType.Builder.create(ElectricFurnaceEntity::new, VivatechBlocks.ELECTRIC_FURNACE.toArray(new Block[MachineTier.values().length])).build(null);
        ENERGY_BANK = BlockEntityType.Builder.create(EnergyBankEntity::new, VivatechBlocks.ENERGY_BANK).build(null);
        PRESS = BlockEntityType.Builder.create(PressEntity::new, VivatechBlocks.PRESS).build(null);
    }

    @Override
    public void initialize() {
        Registry.register(Registry.BLOCK_ENTITY, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.BLOCK_ENTITY, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK_ENTITY, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK_ENTITY, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.BLOCK_ENTITY, PressBlock.ID, PRESS);
    }
}
