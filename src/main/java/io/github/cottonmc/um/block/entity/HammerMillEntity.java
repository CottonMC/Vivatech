package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.cotton.registry.CommonItems;
import io.github.cottonmc.energy.CottonEnergy;
import io.github.cottonmc.energy.api.DefaultEnergyTypes;
import io.github.cottonmc.energy.impl.EnergyHandler;
import io.github.cottonmc.energy.impl.EnergySerializer;
import io.github.cottonmc.resources.CottonResources;
import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.SimpleItemComponent;
import io.github.cottonmc.um.component.wrapper.SidedItemView;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.LockContainer;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class HammerMillEntity extends BlockEntity implements InventoryProvider {

	EnergyHandler energy = new EnergyHandler(32);
	private LockContainer lock; //TODO: Awaiting design direction, to be decided alongside Locky
	int remainingTicks;
	int totalTicks;
	private SimpleItemComponent itemStorage = new SimpleItemComponent(2);

	public HammerMillEntity() {
		super(UMBlocks.HAMMER_MILL_ENTITY);
		itemStorage.addObserver(this::markDirty); //TODO: on markDirty, check if we need to schedule a tick!
		energy.listen(this::markDirty);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);

		result.put("Items", itemStorage.toTag());

		result.put("Energy", EnergySerializer.serialize(energy));

		result.putInt("ProcessTime", remainingTicks);
		result.putInt("ProcessTimeTotal", totalTicks);

		return result;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);

		if (tag.containsKey("Items", 9)) {
			itemStorage.fromTag(tag.getList("Items", 10)); //10 is CompoundTag
		}

		if (tag.containsKey("Energy", 10)) {
			EnergySerializer.deserialize(energy, tag.getCompound("Energy"));
		}

		remainingTicks = tag.getInt("ProcessTime");
		totalTicks = tag.getInt("ProcessTimeTotal");
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (!world.getBlockTickScheduler().isScheduled(pos, UMBlocks.HAMMER_MILL)) {
			//Check if we need to start pulsing again
			if (needsTick()) world.getBlockTickScheduler().schedule(pos, UMBlocks.HAMMER_MILL, 1);
		}
	}

	public void tick() {
		//unfinished

		//placeholder
		ItemStack output = new ItemStack(CommonItems.getItem("copper_dust"), 1);
		//[continue to] spend stored energy to process the input material
		if (remainingTicks > 0) {
			remainingTicks -= 1;
			markDirty();
		} else {
			itemStorage.extract(0, ActionType.PERFORM);
			itemStorage.insert(1, output, ActionType.PERFORM);
		}

		/* Decide if we should schedule a followup pulse. We should followup if:
		 * - If we have any energy left, so we can keep  consuming it
		 * - If we have an item in the input slot, so we can process it
		 *
		 */
		if (needsTick()) {
			world.getBlockTickScheduler().schedule(pos, UMBlocks.COAL_GENERATOR, 1);
		}
	}

	public boolean needsTick() {
		return
				energy.getCurrentEnergy() > 0
					&& !itemStorage.getInvStack(0).isEmpty();
	}

	@Override
	public SidedInventory getInventory(BlockState blockState, IWorld iWorld, BlockPos blockPos) {
		return new SidedItemView(itemStorage);
	}
}
