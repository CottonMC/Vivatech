package vivatech.api.block.entity;

public interface Conduit extends TieredBlockEntity {
    int BASE_TRANSFER_RATE = 50;

    default int getTransferRate() {
        return (int) (BASE_TRANSFER_RATE * getTier().getEnergyMultiplier());
    }
}
