package vivatech.energy;

import io.github.cottonmc.energy.api.EnergyAttribute;
import net.minecraft.item.ItemStack;

public interface EnergyAttributeProviderItem {
    EnergyAttribute getEnergyAttribute(ItemStack itemStack);
}
