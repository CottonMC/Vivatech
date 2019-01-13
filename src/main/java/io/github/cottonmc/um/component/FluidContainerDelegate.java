package io.github.cottonmc.um.component;

import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.Direction;

public interface FluidContainerDelegate extends FluidContainer {
	FluidContainer getFluidDelegate();
	
	default int getMaxCapacity() {
		return getFluidDelegate().getMaxCapacity();
	}
	
	default boolean canInsertFluid(Direction fromSide, Fluid fluid, int amount) {
		return getFluidDelegate().canInsertFluid(fromSide, fluid, amount);
	}
	
	default boolean canExtractFluid(Direction fromSide, Fluid fluid, int amount) {
		return getFluidDelegate().canExtractFluid(fromSide, fluid, amount);
	}
	
	default void insertFluid(Direction fromSide, Fluid fluid, int amount) {
		getFluidDelegate().insertFluid(fromSide, fluid, amount);
	}
	
	default void extractFluid(Direction fromSide, Fluid fluid, int amount) {
		getFluidDelegate().extractFluid(fromSide, fluid, amount);
	}

	default void setFluid(Direction fromSide, FluidInstance instance) {
		getFluidDelegate().setFluid(fromSide, instance);
	}

	default FluidInstance[] getFluids(Direction fromSide) {
		return getFluidDelegate().getFluids(fromSide);
	}
}
