package vivatech.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;

public class ScrewdriverItem extends Item {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "screwdriver");

    public ScrewdriverItem() {
        super(Vivatech.ITEM_SETTINGS.maxDamage(50));
    }  
}
