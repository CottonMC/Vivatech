package vivatech.util;

public enum MachineTeirs {
	
	MINIMAL(1, 0, "_minimal"), NORMAL(2, 3, "_normal"), ADVANCED(3, 5, "_advanced");
	
	private final int speedMultiplier;
	private final int upgradeSlots;
	private final String append;
	
	public int getSpeedMultiplier() {
		return speedMultiplier;
	}

	public int getUpgradeSlots() {
		return upgradeSlots;
	}

	public String getAppend() {
		return append;
	}

	MachineTeirs(int speedMultiplier, int upgradeSlots, String append) {
		this.speedMultiplier = speedMultiplier;
		this.upgradeSlots = upgradeSlots;
		this.append = append;
	}

}