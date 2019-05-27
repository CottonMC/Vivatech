package vivatech.energy;

import io.github.cottonmc.energy.api.EnergyType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import vivatech.Vivatech;
import vivatech.util.StringUtil;

public class InfiniteEnergyType implements EnergyType {
    private static final Identifier i18nId = new Identifier(Vivatech.MODID, "energy_units");

    @Override
    public int getMaximumTransferSize() {
        return Integer.MAX_VALUE;
    }

    @Override
    public TranslatableComponent getDisplayAmount(int i) {
        return StringUtil.getTranslatableComponent("info", i18nId);
    }

    @Override
    public boolean isCompatibleWith(EnergyType type) {
        return true;
    }
}
