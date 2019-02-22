package io.github.cottonmc.um.recipe.polytype;

import com.google.common.collect.Lists;
import com.google.gson.*;
import io.github.cottonmc.um.mixin.FluidTagsContainerMixin;
import io.github.prospector.silk.fluid.FluidInstance;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.fluid.Fluid;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class FluidIngredient implements Predicate<FluidInstance> {
	private static final Predicate<? super FluidIngredient.Entry> NON_EMPTY = (entry) -> !entry.getFluids().stream().allMatch(FluidInstance::isEmpty);
	public static final FluidIngredient EMPTY = new FluidIngredient(Stream.empty());
	private final FluidIngredient.Entry[] entries;
	private FluidInstance[] stackArray;
	private IntList ids;

	private FluidIngredient(Stream<? extends FluidIngredient.Entry> stream_1) {
		this.entries = stream_1.filter(NON_EMPTY).toArray(Entry[]::new);
	}

	@Environment(EnvType.CLIENT)
	public FluidInstance[] getInstanceArray() {
		this.createInstanceArray();
		return this.stackArray;
	}

	private void createInstanceArray() {
		if (this.stackArray == null) {
			this.stackArray = Arrays.stream(this.entries).flatMap((entry) -> entry.getFluids().stream()).distinct().toArray(FluidInstance[]::new);
		}

	}

	public boolean matches(@Nullable FluidInstance inst) {
		if (inst == null) {
			return false;
		} else if (this.entries.length == 0) {
			return inst.isEmpty();
		} else {
			this.createInstanceArray();
			FluidInstance[] var2 = this.stackArray;
			int var3 = var2.length;

			for(int var4 = 0; var4 < var3; ++var4) {
				FluidInstance fluidInstance_2 = var2[var4];
				if (fluidInstance_2.getFluid() == inst.getFluid()) {
					return true;
				}
			}

			return false;
		}
	}

	public IntList getIds() {
		if (this.ids == null) {
			this.createInstanceArray();
			this.ids = new IntArrayList(this.stackArray.length);
			FluidInstance[] var1 = this.stackArray;
			int var2 = var1.length;

			for(int var3 = 0; var3 < var2; ++var3) {
				FluidInstance fluidInstance_1 = var1[var3];
				this.ids.add(FluidRecipeUtils.getFluidId(fluidInstance_1));
			}

			this.ids.sort(IntComparators.NATURAL_COMPARATOR);
		}

		return this.ids;
	}

	public void write(PacketByteBuf buf) {
		this.createInstanceArray();
		buf.writeVarInt(this.stackArray.length);

		for(int int_1 = 0; int_1 < this.stackArray.length; ++int_1) {
			FluidRecipeUtils.writeFluidInstance(buf, this.stackArray[int_1]);
		}

	}

	public JsonElement toJson() {
		if (this.entries.length == 1) {
			return this.entries[0].toJson();
		} else {
			JsonArray json = new JsonArray();
			FluidIngredient.Entry[] var2 = this.entries;
			int var3 = var2.length;

			for(int var4 = 0; var4 < var3; ++var4) {
				FluidIngredient.Entry ingredient$Entry_1 = var2[var4];
				json.add(ingredient$Entry_1.toJson());
			}

			return json;
		}
	}

	public boolean isEmpty() {
		return this.entries.length == 0 && (this.stackArray == null || this.stackArray.length == 0) && (this.ids == null || this.ids.isEmpty());
	}

	private static FluidIngredient ofEntries(Stream<? extends FluidIngredient.Entry> stream_1) {
		FluidIngredient ingredient = new FluidIngredient(stream_1);
		return ingredient.entries.length == 0 ? EMPTY : ingredient;
	}

	public static FluidIngredient ofFluids(FluidProvider... providers) {
		return ofEntries(Arrays.stream(providers).map((provider) -> {
			return new FluidIngredient.InstanceEntry(new FluidInstance(provider.getFluid()));
		}));
	}

	@Environment(EnvType.CLIENT)
	public static FluidIngredient ofInstances(FluidInstance... instances) {
		return ofEntries(Arrays.stream(instances).map((inst) -> new InstanceEntry(inst)));
	}

	public static FluidIngredient fromTag(Tag<Fluid> tag_1) {
		return ofEntries(Stream.of(new FluidIngredient.TagEntry(tag_1)));
	}

	public static FluidIngredient fromPacket(PacketByteBuf buf) {
		int len = buf.readVarInt();
		return ofEntries(Stream.generate(() -> new InstanceEntry(FluidRecipeUtils.readFluidInstance(buf))).limit((long)len));
	}

	public static FluidIngredient fromJson(@Nullable JsonElement element) {
		if (element != null && !element.isJsonNull()) {
			if (element.isJsonObject()) {
				return ofEntries(Stream.of(entryFromJson(element.getAsJsonObject())));
			} else if (element.isJsonArray()) {
				JsonArray json = element.getAsJsonArray();
				if (json.size() == 0) {
					throw new JsonSyntaxException("Fluid array cannot be empty, at least one item must be defined");
				} else {
					return ofEntries(StreamSupport.stream(json.spliterator(), false).map((elementx) -> entryFromJson(JsonHelper.asObject(elementx, "item"))));
				}
			} else {
				throw new JsonSyntaxException("Expected item to be object or array of objects");
			}
		} else {
			throw new JsonSyntaxException("Fluid cannot be null");
		}
	}

	public static FluidIngredient.Entry entryFromJson(JsonObject json) {
		if (json.has("item") && json.has("tag")) {
			throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
		} else {
			Identifier id;
			if (json.has("fluid")) {
				id = new Identifier(JsonHelper.getString(json, "fluid"));
				Fluid fluid = Registry.FLUID.getOrEmpty(id).orElseThrow(() -> new JsonSyntaxException("Unknown fluid '" + id + "'"));
				return new FluidIngredient.InstanceEntry(new FluidInstance(fluid));
			} else if (json.has("tag")) {
				id = new Identifier(JsonHelper.getString(json, "tag"));
				Tag<Fluid> tag = FluidTagsContainerMixin.getContainer().get(id);
				if (tag == null) {
					throw new JsonSyntaxException("Unknown fluid tag '" + id + "'");
				} else {
					return new FluidIngredient.TagEntry(tag);
				}
			} else {
				throw new JsonParseException("A fluid ingredient entry needs either a tag or a fluid");
			}
		}
	}

	@Override
	public boolean test(FluidInstance fluidInstance) {
		return false;
	}

	static class TagEntry implements Entry {
		private final Tag<Fluid> tag;

		private TagEntry(Tag<Fluid> tag) {
			this.tag = tag;
		}

		public Collection<FluidInstance> getFluids() {
			List<FluidInstance> fluids = Lists.newArrayList();
			for (Fluid fluid : this.tag.values()) {
				fluids.add(new FluidInstance(fluid));
			}
			return fluids;
		}

		@Override
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("tag", this.tag.getId().toString());
			return json;
		}
	}

	static class InstanceEntry implements Entry {
		private final FluidInstance fluid;

		private InstanceEntry(FluidInstance fluid) {
			this.fluid = fluid;
		}

		@Override
		public Collection<FluidInstance> getFluids() {
			return Collections.singleton(this.fluid);
		}

		@Override
		public JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.addProperty("fluid", Registry.FLUID.getId(this.fluid.getFluid()).toString());
			return json;
		}
	}

	public interface Entry {
		Collection<FluidInstance> getFluids();

		JsonObject toJson();
	}
}
