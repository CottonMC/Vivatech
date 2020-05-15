package vivatech.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;

public class ScrewdriverItem extends Item {
    public static final Identifier ID = new Identifier(Vivatech.MOD_ID, "screwdriver");

    public ScrewdriverItem() {
        super(Vivatech.getSettings().maxDamage(50));
    }  
}
