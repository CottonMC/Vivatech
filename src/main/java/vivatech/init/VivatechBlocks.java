package vivatech.init;


import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
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
    public static final Identifier MACHINE_CHASSIS_ID = new Identifier(Vivatech.MODID, "machine_chassis");

    public static final Block MACHINE_CHASSIS;
    public static final EnergyConduitBlock ENERGY_CONDUIT;
    public static final CoalGeneratorBlock COAL_GENERATOR;
    public static final ImmutableList<CrusherBlock> CRUSHER;
    public static final ImmutableList<ElectricFurnaceBlock> ELECTRIC_FURNACE;
    public static final EnergyBankBlock ENERGY_BANK;
    public static final ImmutableList<PressBlock> PRESS;

    static {
        MACHINE_CHASSIS = new Block(Vivatech.METALLIC_BLOCK_SETTINGS);
        ENERGY_CONDUIT = new EnergyConduitBlock();
        COAL_GENERATOR = new CoalGeneratorBlock();
        ENERGY_BANK = new EnergyBankBlock();
        
        ELECTRIC_FURNACE = TierHelper.<ElectricFurnaceBlock>fillTieredBlockArray(ElectricFurnaceBlock::new);
        PRESS = TierHelper.<PressBlock>fillTieredBlockArray(PressBlock::new);
        CRUSHER = TierHelper.<CrusherBlock>fillTieredBlockArray(CrusherBlock::new);
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK, MACHINE_CHASSIS_ID, MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.BLOCK, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK, EnergyBankBlock.ID, ENERGY_BANK);
        TierHelper.registerTieredBlocks(ELECTRIC_FURNACE);
        TierHelper.registerTieredBlocks(CRUSHER);
        TierHelper.registerTieredBlocks(PRESS);
    }
}
