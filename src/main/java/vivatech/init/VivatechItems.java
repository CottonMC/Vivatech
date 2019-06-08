package vivatech.init;

import java.util.Map;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.CrusherBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.block.EnergyBankBlock;
import vivatech.block.EnergyConduitBlock;
import vivatech.block.PressBlock;
import vivatech.item.BatteryItem;
import vivatech.util.MachineTiers;
import vivatech.util.TierHelper;

public class VivatechItems implements Initializable {
    public static final Item MACHINE_CHASSIS;
    public static final Item ENERGY_CONDUIT;
    public static final Item COAL_GENERATOR;
    public static final Item CRUSHER;
    public static final Map<MachineTiers, Item> ELECTRIC_FURNACE;
    public static final Item ENERGY_BANK;
    public static final Item PRESS;
    public static final Item BATTERY;

    static {
        MACHINE_CHASSIS = new BlockItem(VivatechBlocks.MACHINE_CHASSIS, Vivatech.ITEM_SETTINGS);
        ENERGY_CONDUIT = new BlockItem(VivatechBlocks.ENERGY_CONDUIT, Vivatech.ITEM_SETTINGS);
        COAL_GENERATOR = new BlockItem(VivatechBlocks.COAL_GENERATOR, Vivatech.ITEM_SETTINGS);
        CRUSHER = new BlockItem(VivatechBlocks.CRUSHER, Vivatech.ITEM_SETTINGS);
        ENERGY_BANK = new BlockItem(VivatechBlocks.ENERGY_BANK, Vivatech.ITEM_SETTINGS);
        PRESS = new BlockItem(VivatechBlocks.PRESS, Vivatech.ITEM_SETTINGS);
        BATTERY = new BatteryItem();
        
        ELECTRIC_FURNACE = TierHelper.fillTieredBlockItemMap(VivatechBlocks.ELECTRIC_FURNACE);
    }

    @Override
    public void initialize() {
        Registry.register(Registry.ITEM, VivatechBlocks.MACHINE_CHASSIS_ID, MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.ITEM, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.ITEM, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.ITEM, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.ITEM, PressBlock.ID, PRESS);
        Registry.register(Registry.ITEM, BatteryItem.ID, BATTERY);
        
        TierHelper.registerTieredItems(ELECTRIC_FURNACE, ElectricFurnaceBlock.ID);
    }
}
