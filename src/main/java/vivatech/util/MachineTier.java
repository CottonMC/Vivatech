package vivatech.util;

public enum MachineTier {
	
	MINIMAL(1F, 0, "minimal"), NORMAL(2F, 3, "normal"), ADVANCED(3F, 5, "advanced");
	
	private final float speedMultiplier;
	private final int upgradeSlots;
	private final String affix;
	
	MachineTier(float speedMultiplier, int upgradeSlots, String affix) {
		this.speedMultiplier = speedMultiplier;
		this.upgradeSlots = upgradeSlots;
		this.affix = affix;
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
	
	public static MachineTier forAffix(String affix) {
		for(MachineTier tier : MachineTier.values()) {
			if (tier.affix.equals(affix)) return tier;
		}
		
		return MachineTier.MINIMAL;
	}
}