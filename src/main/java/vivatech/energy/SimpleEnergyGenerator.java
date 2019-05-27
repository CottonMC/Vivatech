package vivatech.energy;

public class SimpleEnergyGenerator extends SimpleEnergyStorage {
    public SimpleEnergyGenerator(int maxEnergy) {
        super(maxEnergy);
    }

    @Override
    public boolean canTakeEnergy() {
        return false;
    }
}
