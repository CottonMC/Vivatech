package io.github.cottonmc.um.component;

import javax.annotation.Nonnull;

import io.github.prospector.silk.fluid.FluidInstance;
import io.github.prospector.silk.util.ActionType;

/**
 * A component describing one or more tanks of fluid. Most components will contain only one "slot", storing some
 * quantity of fluid. A multi-fluid tank might decide to expose all fluids inside it via slots, or a component might
 * represent two separate fluid tanks using two slots. It's also permitted to expose two fluid tanks as single-tank
 * components under different ECS keys.
 */
public interface FluidComponent {
	/** Gets the number of available fluid-slots in this Component */
	public int slotCount();
	
	/** Returns true if there is no fluid in this Component */
	public boolean isEmpty();
	
	/**
	 * Returns the FluidInstance in the indicated slot. DO NOT MODIFY THE RETURNED INSTANCE.
	 * If a "typed empty" is returned, that is, an empty stack that has a Fluid set, callers should assume the slot can
	 * only accept that kind of fluid.
	 */
	@Nonnull
	public FluidInstance get(int slotIndex);
	
	/**
	 * Attempts to extract the specified amount of fluid from the specified slot.
	 * 
	 * The caller is permitted to modify the returned FluidInstance.
	 * @param slotIndex the index of the slot to extract from.
	 * @param amount the maximum amount to extract.
	 * @param action whether to just simulate, or to actually modify the component by performing the extraction.
	 * @return if action is SIMULATE, the FluidInstance that would be extracted. If it's PERFORM, the instance that *was*
	 *         extracted. Returns FluidInstance.EMPTY if nothing could/would be extracted.
	 */
	@Nonnull
	public FluidInstance extract(int slotIndex, int amount, ActionType action);
	
	/**
	 * Attempts to extract all fluid from the indicated slot. This is a convenience version of
	 * {@link #extract(int, int, ActionType)}
	 * 
	 * As with that method, the caller is permitted to modify the returned FluidInstance.
	 */
	@Nonnull
	public default FluidInstance extract(int slotIndex, ActionType action) {
		return extract(slotIndex, get(slotIndex).getAmount(), action);
	}
	
	
	/**
	 * Attempts to insert a FluidInstance into the indicated slot of this Component.
	 * @param slotIndex the index of the slot to insert into.
	 * @param fluidInstance the fluid to insert.
	 * @param action whether to just simulate, or to actually modify the component by performing the insert.
	 * @return any fluid left over that wasn't able to be inserted, or in SIMULATE mode, any fluid that *would be* left over.
	 */
	@Nonnull
	public FluidInstance insert(int slotIndex, FluidInstance fluidInstance, ActionType action);
	
	/**
	 * Attempts to insert some fluid into any available slot in this Component. The Component MAY at its discretion
	 * split the fluid between multiple slots or tanks.
	 * @param fluidInstance the fluid to insert.
	 * @param action whether to just simulate, or to actually modify the component by performing the insert.
	 * @return any fluid left over that wasn't able to be inserted, or in SIMULATE mode, any fluid that *would be* left over.
	 */
	@Nonnull
	public FluidInstance insert(FluidInstance fluidInstance, ActionType action);
	
	/**
	 * Gets the maximum amount of fluid the specified slot can store.
	 */
	public int getMaxAmount(int slotIndex);
}
