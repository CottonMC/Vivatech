package vivatech.api.block.entity;

public interface IConduitBlockEntity extends ITieredBlockEntity {
    int BASE_TRANSFER_RATE = 50;

    default int getTransferRate() {
        return (int) (BASE_TRANSFER_RATE * getTier().getEnergyMultiplier());
    }
}
