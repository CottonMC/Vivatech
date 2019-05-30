package vivatech.init;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.CrusherBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.block.PressBlock;

public class VivatechBlocks implements Initializable {
    public static final Identifier MACHINE_CHASSIS_ID = new Identifier(Vivatech.MODID, "machine_chassis");

    public static final Block MACHINE_CHASSIS;
    public static final CoalGeneratorBlock COAL_GENERATOR;
    public static final CrusherBlock CRUSHER;
    public static final ElectricFurnaceBlock ELECTRIC_FURNACE;
    public static final PressBlock PRESS;

    static {
        MACHINE_CHASSIS = new Block(Vivatech.METALLIC_BLOCK_SETTINGS);
        COAL_GENERATOR = new CoalGeneratorBlock();
        CRUSHER = new CrusherBlock();
        ELECTRIC_FURNACE = new ElectricFurnaceBlock();
        PRESS = new PressBlock();
    }

    @Override
    public void initialize() {
        Registry.register(Registry.BLOCK, MACHINE_CHASSIS_ID, MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK, PressBlock.ID, PRESS);
    }
}
