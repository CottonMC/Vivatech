package vivatech.api.tier;

import io.github.cottonmc.energy.api.DefaultEnergyTypes;
import io.github.cottonmc.energy.api.EnergyType;

public enum Tier {
	
	MINIMAL(1F, 1F, 0, "minimal", DefaultEnergyTypes.LOW_VOLTAGE),
	NORMAL(2F, 2F, 3, "normal", DefaultEnergyTypes.MEDIUM_VOLTAGE),
	ADVANCED(4F, 4F, 5, "advanced", DefaultEnergyTypes.HIGH_VOLTAGE);
	
	private float energyMultiplier;
	private float speedMultiplier;
	private int upgradeSlots;
	private final String affix;
	private final EnergyType energyType;
	
	Tier(float energyMultiplier, float speedMultiplier, int upgradeSlots, String affix, EnergyType energyType) {
		this.energyMultiplier = energyMultiplier;
		this.speedMultiplier = speedMultiplier;
		this.upgradeSlots = upgradeSlots;
		this.affix = affix;
		this.energyType = energyType;
	}

	public float getEnergyMultiplier() {
		return energyMultiplier;
	}

	public float getSpeedMultiplier() {
		return speedMultiplier;
	}

	public int getUpgradeSlots() {
		return upgradeSlots;
	}

	public String getAffix() {
		return affix;
	}

	public EnergyType getEnergyType() {
		return energyType;
	}

	public void loadFromConfig(TierConfig config) {
		this.energyMultiplier = config.getEnergyMultiplier();
		this.speedMultiplier = config.getSpeedMultiplier();
		this.upgradeSlots = config.getUpgradeSlots();
	}
	
	public static Tier forAffix(String affix) {
		for(Tier tier : Tier.values()) {
			if (tier.affix.equals(affix)) return tier;
		}
		
		return Tier.MINIMAL;
	}
}