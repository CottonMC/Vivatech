package vivatech.util;

public interface IEnergyConduitConnectable {
    default EnergyConduitConnection getConnection() {
        return EnergyConduitConnection.DISCONNECTED;
    }
}
