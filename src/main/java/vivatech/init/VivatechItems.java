package vivatech.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.EnergyBankBlock;
import vivatech.block.EnergyConduitBlock;
import vivatech.item.BatteryItem;
import vivatech.item.ScrewdriverItem;
import vivatech.util.TierHelper;

public class VivatechItems {
    public static final Item MINIMAL_MACHINE_CHASSIS;
    public static final Item NORMAL_MACHINE_CHASSIS;
    public static final Item ADVANCED_MACHINE_CHASSIS;
    public static final Item ENERGY_CONDUIT;
    public static final Item COAL_GENERATOR;
    public static final Item[] CRUSHER;
    public static final Item[] ELECTRIC_FURNACE;
    public static final Item ENERGY_BANK;
    public static final Item[] PRESS;
    public static final Item BATTERY;
    public static final Item SCREWDRIVER;

    static {
        MINIMAL_MACHINE_CHASSIS = new BlockItem(VivatechBlocks.MINIMAL_MACHINE_CHASSIS, Vivatech.ITEM_SETTINGS);
        NORMAL_MACHINE_CHASSIS = new BlockItem(VivatechBlocks.NORMAL_MACHINE_CHASSIS, Vivatech.ITEM_SETTINGS);
        ADVANCED_MACHINE_CHASSIS = new BlockItem(VivatechBlocks.ADVANCED_MACHINE_CHASSIS, Vivatech.ITEM_SETTINGS);
        ENERGY_CONDUIT = new BlockItem(VivatechBlocks.ENERGY_CONDUIT, Vivatech.ITEM_SETTINGS);
        COAL_GENERATOR = new BlockItem(VivatechBlocks.COAL_GENERATOR, Vivatech.ITEM_SETTINGS);
        ENERGY_BANK = new BlockItem(VivatechBlocks.ENERGY_BANK, Vivatech.ITEM_SETTINGS);
        BATTERY = new BatteryItem();
        SCREWDRIVER = new ScrewdriverItem();
        
        ELECTRIC_FURNACE = TierHelper.fillTieredBlockItemArray(VivatechBlocks.ELECTRIC_FURNACE);
        PRESS = TierHelper.fillTieredBlockItemArray(VivatechBlocks.PRESS);
        CRUSHER = TierHelper.fillTieredBlockItemArray(VivatechBlocks.CRUSHER);
    }

    public static void initialize() {
        Registry.register(Registry.ITEM, VivatechBlocks.MINIMAL_MACHINE_CHASSIS_ID, MINIMAL_MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, VivatechBlocks.NORMAL_MACHINE_CHASSIS_ID, NORMAL_MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, VivatechBlocks.ADVANCED_MACHINE_CHASSIS_ID, ADVANCED_MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, EnergyConduitBlock.ID, ENERGY_CONDUIT);
        Registry.register(Registry.ITEM, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.ITEM, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.ITEM, BatteryItem.ID, BATTERY);
        
        
        TierHelper.registerTieredBlockItems(VivatechBlocks.ELECTRIC_FURNACE, ELECTRIC_FURNACE);
        TierHelper.registerTieredBlockItems(VivatechBlocks.PRESS, PRESS);
        TierHelper.registerTieredBlockItems(VivatechBlocks.CRUSHER, CRUSHER);
    }
}
