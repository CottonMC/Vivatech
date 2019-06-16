package vivatech.init;

import com.google.common.collect.ImmutableList;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.CrusherBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.block.EnergyBankBlock;
import vivatech.block.EnergyConduitBlock;
import vivatech.block.PressBlock;
import vivatech.util.TierHelper;

public class VivatechBlocks {
    public static final Identifier LIGHT_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MODID, "light_machine_chassis");
    public static final Identifier REGULAR_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MODID, "regular_machine_chassis");
    public static final Identifier HEAVY_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MODID, "heavy_machine_chassis");

    public static final Block LIGHT_MACHINE_CHASSIS;
    public static final Block REGULAR_MACHINE_CHASSIS;
    public static final Block HEAVY_MACHINE_CHASSIS;
    public static final EnergyConduitBlock ENERGY_CONDUIT;
    public static final CoalGeneratorBlock COAL_GENERATOR;
    public static final ImmutableList<CrusherBlock> CRUSHER;
    public static final ImmutableList<ElectricFurnaceBlock> ELECTRIC_FURNACE;
    public static final EnergyBankBlock ENERGY_BANK;
    public static final ImmutableList<PressBlock> PRESS;

    static {
        LIGHT_MACHINE_CHASSIS = new Block(FabricBlockSettings.copy(Blocks.STONE).build());
        REGULAR_MACHINE_CHASSIS = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK).build());
        HEAVY_MACHINE_CHASSIS = new Block(FabricBlockSettings.copy(Blocks.IRON_BLOCK).hardness(7f).resistance(10f).build());
        ENERGY_CONDUIT = new EnergyConduitBlock();
        COAL_GENERATOR = new CoalGeneratorBlock();
        ENERGY_BANK = new EnergyBankBlock();
        
        ELECTRIC_FURNACE = TierHelper.<ElectricFurnaceBlock>fillTieredBlockArray(ElectricFurnaceBlock::new);
        PRESS = TierHelper.<PressBlock>fillTieredBlockArray(PressBlock::new);
        CRUSHER = TierHelper.<CrusherBlock>fillTieredBlockArray(CrusherBlock::new);
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK, LIGHT_MACHINE_CHASSIS_ID, LIGHT_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, REGULAR_MACHINE_CHASSIS_ID, REGULAR_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, HEAVY_MACHINE_CHASSIS_ID, HEAVY_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.BLOCK, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK, EnergyBankBlock.ID, ENERGY_BANK);
        TierHelper.registerTieredBlocks(ELECTRIC_FURNACE);
        TierHelper.registerTieredBlocks(CRUSHER);
        TierHelper.registerTieredBlocks(PRESS);
    }
}
