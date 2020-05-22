package vivatech.api.tier;

import io.github.cottonmc.component.energy.type.EnergyType;
import io.github.cottonmc.component.energy.type.EnergyTypes;

public enum Tier {
	MINIMAL(1, 1F, 0, "minimal", EnergyTypes.LOW_VOLTAGE),
	NORMAL(2, 2F, 3, "normal", EnergyTypes.MEDIUM_VOLTAGE),
	ADVANCED(4, 4F, 5, "advanced", EnergyTypes.HIGH_VOLTAGE);
	
	private int energyMultiplier;
	private float speedMultiplier;
	private int upgradeSlots;
	private final String affix;
	private final EnergyType energyType;
	
	Tier(int energyMultiplier, float speedMultiplier, int upgradeSlots, String affix, EnergyType energyType) {
		this.energyMultiplier = energyMultiplier;
		this.speedMultiplier = speedMultiplier;
		this.upgradeSlots = upgradeSlots;
		this.affix = affix;
		this.energyType = energyType;
	}

	public int getEnergyMultiplier() {
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