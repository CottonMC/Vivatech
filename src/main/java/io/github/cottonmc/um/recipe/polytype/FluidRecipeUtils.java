package io.github.cottonmc.um.recipe.polytype;

import io.github.prospector.silk.fluid.FluidInstance;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class FluidRecipeUtils {

	public static int getFluidId(FluidInstance fluid) {
		return Registry.FLUID.getRawId(fluid.getFluid());
	}

	public static void writeFluidInstance(PacketByteBuf buf, FluidInstance inst) {
		if (inst.isEmpty()) {
			buf.writeBoolean(false);
		} else {
			buf.writeBoolean(true);
			Fluid fluid = inst.getFluid();
			buf.writeVarInt(Registry.FLUID.getRawId(fluid));
			buf.writeInt(inst.getAmount());
			buf.writeCompoundTag(inst.getTag());
		}
	}

	public static FluidInstance readFluidInstance(PacketByteBuf buf) {
		if (!buf.readBoolean()) {
			return FluidInstance.EMPTY;
		} else {
			int id = buf.readVarInt();
			int amount = buf.readInt();
			Fluid fluid = Registry.FLUID.get(id);
			FluidInstance inst = new FluidInstance(fluid, amount);
			inst.setTag(buf.readCompoundTag());
			return inst;
		}
	}
}
