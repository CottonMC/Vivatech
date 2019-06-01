package vivatech.energy;

import io.github.cottonmc.energy.api.EnergyType;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import vivatech.Vivatech;

public class VivatechEnergyAttribute extends SimpleEnergyAttribute {
    public VivatechEnergyAttribute(int maxEnergy) {
        super(maxEnergy);
        this.energyType = Vivatech.ENERGY;
    }

    private VivatechEnergyAttribute(int maxEnergy, EnergyType type) {
        super(maxEnergy, type);
    }
}
