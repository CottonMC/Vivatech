package vivatech.api.util;

public enum BlockTier {
	
	MINIMAL(1F, 1F, 0, "minimal"),
	NORMAL(5F, 2F, 3, "normal"),
	ADVANCED(15F, 3F, 5, "advanced");
	
	private final float energyMultiplier;
	private final float speedMultiplier;
	private final int upgradeSlots;
	private final String affix;
	
	BlockTier(float energyMultiplier, float speedMultiplier, int upgradeSlots, String affix) {
		this.energyMultiplier = energyMultiplier;
		this.speedMultiplier = speedMultiplier;
		this.upgradeSlots = upgradeSlots;
		this.affix = affix;
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
	
	public static BlockTier forAffix(String affix) {
		for(BlockTier tier : BlockTier.values()) {
			if (tier.affix.equals(affix)) return tier;
		}
		
		return BlockTier.MINIMAL;
	}
}