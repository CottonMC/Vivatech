package io.github.cottonmc.um.component.wrapper;

import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyType;
import io.github.prospector.silk.util.ActionType;

public class EnergyView implements EnergyAttribute {
	private final EnergyAttribute proxy;
	private final boolean insert;
	private final boolean extract;
	
	public EnergyView(EnergyAttribute proxy, boolean insert, boolean extract) {
		this.proxy = proxy;
		this.insert = insert;
		this.extract = extract;
	}

	@Override
	public int getMaxEnergy() {
		return proxy.getMaxEnergy();
	}

	@Override
	public int getCurrentEnergy() {
		return proxy.getCurrentEnergy();
	}

	@Override
	public boolean canInsertEnergy() {
		return proxy.canInsertEnergy() && insert;
	}

	@Override
	public int insertEnergy(EnergyType type, int amount, ActionType actionType) {
		if (insert) return proxy.insertEnergy(type, amount, actionType);
		return amount;
	}

	@Override
	public boolean canExtractEnergy() {
		return proxy.canExtractEnergy() && extract;
	}

	@Override
	public int extractEnergy(EnergyType type, int amount, ActionType actionType) {
		if (extract) return proxy.extractEnergy(type, amount, actionType);
		return 0;
	}

	@Override
	public EnergyType getPreferredType() {
		return proxy.getPreferredType();
	}
	
	public static EnergyView insertOnly(EnergyAttribute proxy) {
		return new EnergyView(proxy, true, false);
	}
	
	public static EnergyView extractOnly(EnergyAttribute proxy) {
		return new EnergyView(proxy, false, true);
	}
	
	public static EnergyView immutable(EnergyAttribute proxy) {
		return new EnergyView(proxy, false, false);
	}
}
