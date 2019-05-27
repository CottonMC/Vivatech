package vivatech.energy;

import javax.annotation.Nonnull;

public interface IEnergyHolder {
    @Nonnull
    IEnergyStorage getEnergyStorage();
}
