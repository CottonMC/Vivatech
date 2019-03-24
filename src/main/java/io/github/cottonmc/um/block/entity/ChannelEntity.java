package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.SimpleFluidComponent;
import io.github.prospector.silk.fluid.DropletValues;
import io.github.prospector.silk.fluid.FluidContainer;
import io.github.prospector.silk.fluid.FluidContainerProvider;
import io.github.prospector.silk.fluid.FluidInstance;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ChannelEntity extends BlockEntity implements Tickable {

	private SimpleFluidComponent fluids = new SimpleFluidComponent(1, 2*DropletValues.BOTTLE);

	public ChannelEntity() {
		super(UMBlocks.CHANNEL_ENTITY);
		fluids.listen(this::markDirty);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		CompoundTag result = super.toTag(tag);

		result.put("Fluids", fluids.toTag());

		return result;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);

		fluids.fromTag(tag.getTag("Fluids"));
	}

	public void tick() {
		if (world==null || fluids.isEmpty() || world.isClient) return;
		FluidInstance self = fluids.get(0);
		for (Direction dir : Direction.values()) {
			BlockPos checkPos = pos.offset(dir);
			BlockState checkState = world.getBlockState(checkPos);
			if (checkState.getBlock() == UMBlocks.CHANNEL) {
				ChannelEntity channel = (ChannelEntity)world.getBlockEntity(checkPos);
				FluidContainer otherFluids = channel.getFluids();
				FluidInstance other = otherFluids.getFluids(dir.getOpposite())[0];
				if (shouldSendFluidsToChannel(self, other)) {
					int transfer = (self.getAmount() - other.getAmount()) / 2;
					otherFluids.insertFluid(dir.getOpposite(), self.getFluid(), transfer);
					channel.markDirty();
					self.subtractAmount(transfer);
				}
			} else if (world.getBlockState(checkPos).getBlock() instanceof FluidContainerProvider) {
				FluidContainer container = ((FluidContainerProvider)checkState.getBlock()).getContainer(checkState, world, checkPos);
				int amountToTransfer = Math.min(DropletValues.INGOT, self.getAmount());
				if (container.canInsertFluid(dir.getOpposite(), self.getFluid(), amountToTransfer)) {
					int transferred = container.tryPartialInsertFluid(dir.getOpposite(), self.getFluid(), amountToTransfer, ActionType.PERFORM);
					self.subtractAmount(transferred);
				}
			}
		}
	}

	public FluidContainer getFluids() {
		return fluids;
	}

	public static boolean shouldSendFluidsToChannel(FluidInstance self, FluidInstance other) {
		return self.getFluid() == other.getFluid() && self.getAmount() > other.getAmount();
	}
}
