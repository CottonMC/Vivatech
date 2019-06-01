package vivatech.item;

import com.google.common.collect.Lists;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormat;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import vivatech.Vivatech;
import vivatech.energy.EnergyAttributeProviderItem;
import vivatech.energy.InfiniteEnergyType;
import vivatech.util.StringHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BatteryItem extends Item implements EnergyAttributeProviderItem {
    public static final Identifier ID = new Identifier(Vivatech.MODID, "battery");

    private static final int MAX_ENERGY = 5_000;

    public BatteryItem() {
        super(Vivatech.ITEM_SETTINGS.stackSize(1).durability(MAX_ENERGY + 1));
    }

    // Item
    @Environment(EnvType.CLIENT)
    @Override
    public void buildTooltip(ItemStack itemStack, @Nullable World world, List<Component> list, TooltipContext context) {
        list.add(
                StringHelper.getTranslatableComponent("info", InfiniteEnergyType.energyWithMaxI18nId,
                        ((EnergyAttributeProviderItem) itemStack.getItem()).getEnergyAttribute(itemStack).getCurrentEnergy(),
                        MAX_ENERGY)
                .setStyle(new Style().setColor(ChatFormat.GRAY)));
    }

    // EnergyAttributeProviderItem
    @Override
    public EnergyAttribute getEnergyAttribute(ItemStack stack) {
        SimpleEnergyAttribute energy = new SimpleEnergyAttribute(MAX_ENERGY, Vivatech.ENERGY);
        if (!stack.hasTag() || !stack.getTag().containsKey("Energy")) {
            setEnergyAttribute(stack, energy);
        } else {
            energy.fromTag(stack.getTag().getTag("Energy"));
        }
        return energy;
    }

    public void setEnergyAttribute(ItemStack stack, EnergyAttribute energy) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("Energy", new IntTag(energy.getCurrentEnergy()));
        stack.setDamage(getDurability() - energy.getCurrentEnergy());
    }
}
