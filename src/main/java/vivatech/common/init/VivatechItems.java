package vivatech.common.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.api.wire.WireItem;
import vivatech.common.Vivatech;
import vivatech.common.block.SterlingGeneratorBlock;
import vivatech.common.block.EnergyBankBlock;
import vivatech.common.item.BatteryItem;
import vivatech.common.item.ScrewdriverItem;
import vivatech.util.TierHelper;

public class VivatechItems {
    public static final Item MINIMAL_MACHINE_CHASSIS;
    public static final Item NORMAL_MACHINE_CHASSIS;
    public static final Item ADVANCED_MACHINE_CHASSIS;
    public static final Item COAL_GENERATOR;
    public static final Item ENERGY_BANK;

    public static final Item[] CRUSHER;
    public static final Item[] ELECTRIC_FURNACE;
    public static final Item[] ENERGY_CONDUIT;
    public static final Item[] PRESS;

    public static final Item BATTERY;
    public static final Item SCREWDRIVER;

    public static final Item LV_WIRE;
    public static final Item MV_WIRE;
    public static final Item HV_WIRE;

    static {
        MINIMAL_MACHINE_CHASSIS = new BlockItem(VivatechBlocks.MINIMAL_MACHINE_CHASSIS, Vivatech.getSettings());
        NORMAL_MACHINE_CHASSIS = new BlockItem(VivatechBlocks.NORMAL_MACHINE_CHASSIS, Vivatech.getSettings());
        ADVANCED_MACHINE_CHASSIS = new BlockItem(VivatechBlocks.ADVANCED_MACHINE_CHASSIS, Vivatech.getSettings());
        COAL_GENERATOR = new BlockItem(VivatechBlocks.COAL_GENERATOR, Vivatech.getSettings());
        ENERGY_BANK = new BlockItem(VivatechBlocks.ENERGY_BANK, Vivatech.getSettings());

        CRUSHER = TierHelper.fillTieredBlockItemArray(VivatechBlocks.CRUSHER);
        ELECTRIC_FURNACE = TierHelper.fillTieredBlockItemArray(VivatechBlocks.ELECTRIC_FURNACE);
        ENERGY_CONDUIT = TierHelper.fillTieredBlockItemArray(VivatechBlocks.ENERGY_CONDUIT);
        PRESS = TierHelper.fillTieredBlockItemArray(VivatechBlocks.PRESS);

        BATTERY = new BatteryItem();
        SCREWDRIVER = new ScrewdriverItem();

        LV_WIRE = new WireItem(VivatechWires.LOW_VOLTAGE, Vivatech.getSettings());
        MV_WIRE = new WireItem(VivatechWires.MEDIUM_VOLTAGE, Vivatech.getSettings());
        HV_WIRE = new WireItem(VivatechWires.HIGH_VOLTAGE, Vivatech.getSettings());
    }

    public static void initialize() {
        Registry.register(Registry.ITEM, VivatechBlocks.MINIMAL_MACHINE_CHASSIS_ID, MINIMAL_MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, VivatechBlocks.NORMAL_MACHINE_CHASSIS_ID, NORMAL_MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, VivatechBlocks.ADVANCED_MACHINE_CHASSIS_ID, ADVANCED_MACHINE_CHASSIS);
        Registry.register(Registry.ITEM, SterlingGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.ITEM, EnergyBankBlock.ID, ENERGY_BANK);

        TierHelper.registerTieredBlockItems(VivatechBlocks.CRUSHER, CRUSHER);
        TierHelper.registerTieredBlockItems(VivatechBlocks.ELECTRIC_FURNACE, ELECTRIC_FURNACE);
        TierHelper.registerTieredBlockItems(VivatechBlocks.ENERGY_CONDUIT, ENERGY_CONDUIT);
        TierHelper.registerTieredBlockItems(VivatechBlocks.PRESS, PRESS);

        Registry.register(Registry.ITEM, BatteryItem.ID, BATTERY);
        Registry.register(Registry.ITEM, ScrewdriverItem.ID, SCREWDRIVER);

        Registry.register(Registry.ITEM, new Identifier(Vivatech.MODID, "lv_wire"), LV_WIRE);
        Registry.register(Registry.ITEM, new Identifier(Vivatech.MODID, "mv_wire"), MV_WIRE);
        Registry.register(Registry.ITEM, new Identifier(Vivatech.MODID, "hv_wire"), HV_WIRE);
    }
}
