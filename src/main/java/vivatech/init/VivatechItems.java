package vivatech.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import vivatech.block.CoalGeneratorBlock;
import vivatech.util.Meta;
import vivatech.block.ElectricFurnaceBlock;

public class VivatechItems implements Initializable {
    public static final Item COAL_GENERATOR;
    public static final Item ELECTRIC_FURNACE;

    static {
        COAL_GENERATOR = new BlockItem(VivatechBlocks.COAL_GENERATOR, Meta.ITEM_SETTINGS);
        ELECTRIC_FURNACE = new BlockItem(VivatechBlocks.ELECTRIC_FURNACE, Meta.ITEM_SETTINGS);
    }

    @Override
    public void initialize() {
        Registry.register(Registry.ITEM, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.ITEM, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
    }
}
