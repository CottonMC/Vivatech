package vivatech.item;

import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vivatech.Vivatech;
import vivatech.util.StringHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ScrewdriverItem extends Item {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "screwdriver");

    public BatteryItem() {
        super(Vivatech.ITEM_SETTINGS.stackSize(1).durability(50));
    }  
}
