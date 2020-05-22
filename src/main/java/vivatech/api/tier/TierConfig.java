package vivatech.api.tier;

public class TierConfig {
	//TODO: have a way to load this from a data pack or such
	private int energyMultiplier;
	private int speedMultiplier;
	private int upgradeSlots;

	public TierConfig(int energyMultiplier, int speedMultiplier, int upgradeSlots) {
		this.energyMultiplier = energyMultiplier;
		this.speedMultiplier = speedMultiplier;
		this.upgradeSlots = upgradeSlots;
	}

	public int getEnergyMultiplier() {
		return energyMultiplier;
	}

	public int getSpeedMultiplier() {
		return speedMultiplier;
	}

	public int getUpgradeSlots() {
		return upgradeSlots;
	}
}
