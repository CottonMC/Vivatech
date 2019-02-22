package io.github.cottonmc.um.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.TagContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidTags.class)
public abstract class FluidTagsContainerMixin {

	@Shadow private static TagContainer<Fluid> container;

	@Accessor
	public static TagContainer<Fluid> getContainer() {
		return container;
	}
}
