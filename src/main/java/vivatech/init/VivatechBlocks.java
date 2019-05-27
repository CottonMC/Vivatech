package vivatech.init;

import net.minecraft.util.registry.Registry;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.ElectricFurnaceBlock;

public class VivatechBlocks implements Initializable {
    public static final CoalGeneratorBlock COAL_GENERATOR;
    public static final ElectricFurnaceBlock ELECTRIC_FURNACE;

    static {
        COAL_GENERATOR = new CoalGeneratorBlock();
        ELECTRIC_FURNACE = new ElectricFurnaceBlock();
    }

    @Override
    public void initialize() {
        Registry.register(Registry.BLOCK, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
    }
}
