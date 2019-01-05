package io.github.cottonmc.um.block;

import net.minecraft.util.StringRepresentable;

/**
 * States for machines to display to the client, and save to the map.
 */
public enum MachineStatus implements StringRepresentable {
	/** The machine is not running. It is reccommended that when displaying this state, no ticks are scheduled. */
	INACTIVE("inactive"),
	/** The machine is waiting for an operation to finish. It is reccommended to check or set this state just before scheduling a tick. */
	ACTIVE("active"),
	/**
	 * Either some important requirements haven't been met, or there was an actual code problem encountered when
	 * doing pulse logic. A tick may or may not be scheduled, depending on whether the error might be recoverable.
	 */
	ERROR("error");
	
	private final String name;
	
	MachineStatus(String name) { this.name = name; }
	
	@Override
	public String asString() {
		return name;
	}
}
