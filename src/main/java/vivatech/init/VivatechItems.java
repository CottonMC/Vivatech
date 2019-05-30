package vivatech.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.CrusherBlock;
import vivatech.block.ElectricFurnaceBlock;

public class VivatechItems implements Initializable {
    public static final Item MACHINE_CHASSIS;
    public static final Item COAL_GENERATOR;
    public static final Item CRUSHER;
    public static final Item ELECTRIC_FURNACE;

    static {
        MACHINE_CHASSIS = new BlockItem(VivatechBlocks.MACHINE_CHASSIS, Vivatech.ITEM_SETTINGS);
        COAL_GENERATOR = new BlockItem(VivatechBlocks.COAL_GENERATOR, Vivatech.ITEM_SETTINGS);
        CRUSHER = new BlockItem(VivatechBlocks.CRUSHER, Vivatech.ITEM_SETTINGS);
        ELECTRIC_FURNACE = new BlockItem(VivatechBlocks.ELECTRIC_FURNACE, Vivatech.ITEM_SETTINGS);
    }

    @Override
    public void initialize() {
        Registry.register(Registry.ITEM, VivatechBlocks.MACHINE_CHASSIS_ID, MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.ITEM, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.ITEM, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
    }
}
