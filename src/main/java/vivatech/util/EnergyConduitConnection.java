package vivatech.util;

import net.minecraft.util.StringIdentifiable;

public enum EnergyConduitConnection implements StringIdentifiable {
    DISCONNECTED("disconnected"),
    DISABLED("disabled"),
    CONDUIT("conduit"),
    CONSUMER("consumer"),
    PRODUCER("producer"),
    ENERGY_BANK("energy_bank");

    private final String name;

    EnergyConduitConnection(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return name;
    }
}
