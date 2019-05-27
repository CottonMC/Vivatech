package vivatech.energy;

public class SimpleEnergyConsumer extends SimpleEnergyStorage {
    public SimpleEnergyConsumer(int maxEnergy) {
        super(maxEnergy);
    }

    @Override
    public boolean canGiveEnergy() {
        return false;
    }
}
