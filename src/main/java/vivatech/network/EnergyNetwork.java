package vivatech.network;

import vivatech.Vivatech;
import vivatech.entity.EnergyConduitEntity;

import java.util.ArrayList;
import java.util.List;

public class EnergyNetwork {
    private final static List<List<EnergyConduitEntity>> networks = new ArrayList<>();

    public static void addConduit(EnergyConduitEntity conduit) {
        if (conduit.getWorld().isClient()) return;


//        conduits.addConduit(conduit);

        Vivatech.LOGGER.devInfo("Added " + conduit.getWorld() + " " + conduit.getPos());
    }

    public static void removeConduit(EnergyConduitEntity conduit) {
        if (conduit.getWorld().isClient()) return;

//        conduits.removeConduit(conduit);

        Vivatech.LOGGER.devInfo("Removed " + conduit.getWorld() + " " + conduit.getPos());
    }
}
