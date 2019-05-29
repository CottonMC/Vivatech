package vivatech.init;

import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.ElectricFurnaceBlock;

public class VivatechBlocks implements Initializable {
    public static final Identifier MACHINE_CHASSIS_ID = new Identifier(Vivatech.MODID, "machine_chassis");

    public static final Block MACHINE_CHASSIS;
    public static final CoalGeneratorBlock COAL_GENERATOR;
    public static final ElectricFurnaceBlock ELECTRIC_FURNACE;

    static {
        MACHINE_CHASSIS = new Block(Vivatech.MACHINE_BLOCK_SETTINGS);
        COAL_GENERATOR = new CoalGeneratorBlock();
        ELECTRIC_FURNACE = new ElectricFurnaceBlock();
    }

    @Override
    public void initialize() {
        Registry.register(Registry.BLOCK, MACHINE_CHASSIS_ID, MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
    }
}
