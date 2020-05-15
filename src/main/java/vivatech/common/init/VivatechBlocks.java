package vivatech.common.init;

import com.google.common.collect.ImmutableList;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.common.Vivatech;
import vivatech.common.block.*;
import vivatech.util.TierHelper;

public class VivatechBlocks {
    public static final Identifier MINIMAL_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MOD_ID, "minimal_machine_chassis");
    public static final Identifier NORMAL_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MOD_ID, "normal_machine_chassis");
    public static final Identifier ADVANCED_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MOD_ID, "advanced_machine_chassis");

    public static final Block MINIMAL_MACHINE_CHASSIS;
    public static final Block NORMAL_MACHINE_CHASSIS;
    public static final Block ADVANCED_MACHINE_CHASSIS;
    public static final SterlingGeneratorBlock COAL_GENERATOR;
    public static final EnergyBankBlock ENERGY_BANK;

    public static final ImmutableList<CrusherBlock> CRUSHER;
    public static final ImmutableList<ElectricFurnaceBlock> ELECTRIC_FURNACE;
    public static final ImmutableList<EnergyConduitBlock> ENERGY_CONDUIT;
    public static final ImmutableList<PressBlock> PRESS;
    public static final ImmutableList<EnergyConnectorBlock> CONNECTOR;

    static {
        MINIMAL_MACHINE_CHASSIS = new Block(FabricBlockSettings.copy(Blocks.STONE).build());
        NORMAL_MACHINE_CHASSIS = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK).build());
        ADVANCED_MACHINE_CHASSIS = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK).hardness(7f).resistance(10f).build());
        COAL_GENERATOR = new SterlingGeneratorBlock();
        ENERGY_BANK = new EnergyBankBlock();

        CRUSHER = TierHelper.fillTieredBlockArray(CrusherBlock::new);
        ELECTRIC_FURNACE = TierHelper.fillTieredBlockArray(ElectricFurnaceBlock::new);
        ENERGY_CONDUIT = TierHelper.fillTieredBlockArray(EnergyConduitBlock::new);
        PRESS = TierHelper.fillTieredBlockArray(PressBlock::new);
        CONNECTOR = TierHelper.fillTieredBlockArray(EnergyConnectorBlock::new);
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK, MINIMAL_MACHINE_CHASSIS_ID, MINIMAL_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, NORMAL_MACHINE_CHASSIS_ID, NORMAL_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, ADVANCED_MACHINE_CHASSIS_ID, ADVANCED_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, SterlingGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK, EnergyBankBlock.ID, ENERGY_BANK);
        TierHelper.registerTieredBlocks(CRUSHER);
        TierHelper.registerTieredBlocks(ELECTRIC_FURNACE);
        TierHelper.registerTieredBlocks(ENERGY_CONDUIT);
        TierHelper.registerTieredBlocks(PRESS);
        TierHelper.registerTieredBlocks(CONNECTOR);
    }
}
