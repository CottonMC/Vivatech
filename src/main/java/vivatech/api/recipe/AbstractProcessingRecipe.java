package vivatech.api.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.context.LootContext;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.World;
import vivatech.api.tier.Tier;

import java.util.Collections;
import java.util.List;

import net.minecraft.util.JsonHelper;

/**
 * Implements a generic machine processing recipe with optional loot table for bonus items.
 *
 * Example JSON:
 *
 * {
 *     "type": "c:example",
 *     "ingredient": {
 *         "item": "minecraft:iron_ore"
 *     },
 *     "tier": "minimal",
 *     "processtime": 200,
 *     "experience": 0.35,
 *     "result": {
 *         "item": "example:iron_dust",
 *         "count": 2
 *     },
 *     "bonus": "example:iron_ore_to_dust_bonuses"
 * }
 *
 * @see vivatech.common.recipe.CrushingRecipe for an example implementation
 */
public abstract class AbstractProcessingRecipe implements ProcessingRecipe {

	protected final Identifier id;
	protected final Ingredient input;
	protected final ItemStack output;
	protected final Tier minTier;
	protected final float exp;
	protected final int processTime;
	protected final Identifier bonusLootTable;

	public AbstractProcessingRecipe(Identifier id, Ingredient input, ItemStack output, Tier minTier, float exp, int processTime, Identifier bonusLootTable) {
		this.id = id;
		this.input = input;
		this.output = output;
		this.minTier = minTier;
		this.exp = exp;
		this.processTime = processTime;
		this.bonusLootTable = bonusLootTable;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	public Ingredient getInput() {
		return input;
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Tier getMinTier() {
		return minTier;
	}

	@Override
	public float getExperience() {
		return exp;
	}

	@Override
	public int getProcessTime() {
		return processTime;
	}

	@Override
	public Identifier getBonusLootTable() {
		return bonusLootTable;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return input.test(inventory.getInvStack(0));
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return output.copy();
	}

	public List<ItemStack> craftBonus(LootManager lootManager, LootContext lootContext) {
		if (bonusLootTable == null)
			return Collections.emptyList();
		return lootManager.getSupplier(bonusLootTable).getDrops(lootContext);
	}

	@Override
	public boolean fits(int w, int h) {
		return w >= 1 && h >= 1;
	}

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		return DefaultedList.ofSize(1, input);
	}

	@FunctionalInterface
	public interface Factory<R extends AbstractProcessingRecipe> {
		R create(Identifier id, Ingredient input, ItemStack output, Tier minTier, float exp, int processTime, Identifier bonusLootTable);
	}

	public static class Serializer<R extends AbstractProcessingRecipe> implements RecipeSerializer<R> {

		private final Factory<R> factory;
		private final int defaultProcessTime;

		public Serializer(Factory<R> factory, int defaultProcessTime) {
			this.factory = factory;
			this.defaultProcessTime = defaultProcessTime;
		}

		@Override
		public R read(Identifier id, JsonObject jsonObject) {
			Ingredient input = Ingredient.fromJson(
					JsonHelper.hasArray(jsonObject, "ingredient")
							? JsonHelper.getArray(jsonObject, "ingredient")
							: JsonHelper.getObject(jsonObject, "ingredient")
			);

			ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
			
			Tier minTier = JsonHelper.hasString(jsonObject, "tier")
					? Tier.forAffix(JsonHelper.getString(jsonObject, "tier"))
					: Tier.MINIMAL;
			
			float exp = JsonHelper.getFloat(jsonObject, "experience", 0.0F);
			int processTime = JsonHelper.getInt(jsonObject, "processtime", this.defaultProcessTime);

			String bonusLoot = JsonHelper.getString(jsonObject, "bonus", null);
			Identifier bonusLootId = bonusLoot == null ? null : new Identifier(bonusLoot);

			return factory.create(id, input, output, minTier, exp, processTime, bonusLootId);
		}

		@Override
		public R read(Identifier id, PacketByteBuf buffer) {
			Ingredient input = Ingredient.fromPacket(buffer);
			ItemStack output = buffer.readItemStack();
			Tier minTier = Tier.MINIMAL;
			int tierInt = buffer.readInt();
			if (tierInt>=0 && tierInt< Tier.values().length) minTier = Tier.values()[tierInt];
			float exp = buffer.readFloat();
			int processTime = buffer.readInt();
			Identifier bonusLoot = buffer.readBoolean() ? buffer.readIdentifier() : null;

			return factory.create(id, input, output, minTier, exp, processTime, bonusLoot);
		}

		@Override
		public void write(PacketByteBuf buffer, R recipe) {
			recipe.input.write(buffer);
			buffer.writeItemStack(recipe.output);
			buffer.writeInt(recipe.minTier.ordinal());
			buffer.writeFloat(recipe.exp);
			buffer.writeInt(recipe.processTime);

			Identifier bonusLoot = recipe.getBonusLootTable();
			buffer.writeBoolean(bonusLoot != null);
			if (bonusLoot != null) {
				buffer.writeIdentifier(bonusLoot);
			}
		}
	}
}
