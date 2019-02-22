package io.github.cottonmc.um.recipe.polytype;

import com.google.gson.JsonObject;
import io.github.cottonmc.um.recipe.UMRecipes;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public interface PolyTypeRecipeSerializer<T extends PolyTypeRecipe<?>> {
	T read(Identifier var1, JsonObject var2);

	T read(Identifier var1, PacketByteBuf var2);

	void write(PacketByteBuf var1, T var2);

	static <S extends PolyTypeRecipeSerializer<T>, T extends PolyTypeRecipe<?>> S register(String id, S serializer) {
		return Registry.register(UMRecipes.POLY_TYPE_RECIPE_SERIALIZER, id, serializer);
	}
}
