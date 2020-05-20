package vivatech.api.component.energy;

import io.github.cottonmc.component.UniversalComponents;
import io.github.cottonmc.component.energy.impl.SimpleCapacitorComponent;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.extension.CopyableComponent;

public class CopyableCapacitorComponent extends SimpleCapacitorComponent implements CopyableComponent {
    public CopyableCapacitorComponent(int maxEnergy) {
        super(maxEnergy);
    }

    @Override
    public ComponentType getComponentType() {
        return UniversalComponents.CAPACITOR_COMPONENT;
    }
}
