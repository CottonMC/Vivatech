package vivatech.energy;

import io.github.cottonmc.energy.api.EnergyType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import vivatech.Vivatech;
import vivatech.util.StringHelper;

public class InfiniteEnergyType implements EnergyType {
    public static final Identifier energyI18nId = new Identifier(Vivatech.MODID, "energy");
    public static final Identifier energyWithMaxI18nId = new Identifier(Vivatech.MODID, "energy_with_max");

    @Override
    public int getMaximumTransferSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public TranslatableComponent getDisplayAmount(int i) {
        return StringHelper.getTranslatableComponent("info", energyI18nId, i);
    }

    @Override
    public boolean isCompatibleWith(EnergyType type) {
        return true;
    }
}
