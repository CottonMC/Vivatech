package vivatech.common.item;

import io.github.cottonmc.component.energy.CapacitorComponent;
import io.github.cottonmc.component.energy.CapacitorComponentHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vivatech.common.Vivatech;
import vivatech.util.StringHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryItem extends Item {
    public static final Identifier ID = new Identifier(Vivatech.MOD_ID, "battery");

    public static final int MAX_ENERGY = 500;

    public BatteryItem() {
        super(Vivatech.getSettings().maxCount(1).maxDamage(MAX_ENERGY));
    }

    // Item
    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CapacitorComponent capacitor = CapacitorComponentHelper.INSTANCE.getComponent(stack, null);
        if (capacitor != null) {
            tooltip.add(
                StringHelper
                    .getTranslatableComponent(
                        "info",
                        new Identifier(Vivatech.MOD_ID, "energy_with_max"),
                        capacitor.getCurrentEnergy(),
                        MAX_ENERGY
                    )
                    .setStyle(new Style().setColor(Formatting.GRAY))
            );
        }
    }
}
