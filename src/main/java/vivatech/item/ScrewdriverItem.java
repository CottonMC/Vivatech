package vivatech.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import vivatech.Vivatech;

public class ScrewdriverItem extends Item {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "screwdriver");

    public BatteryItem() {
        super(Vivatech.ITEM_SETTINGS.stackSize(1).durability(50));
    }  
}
