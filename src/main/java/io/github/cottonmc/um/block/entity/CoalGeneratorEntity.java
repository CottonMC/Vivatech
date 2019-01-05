package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.um.PulseConsumer;
import io.github.cottonmc.um.block.UMBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.LockContainer;
public class CoalGeneratorEntity extends BlockEntity implements PulseConsumer {
	private LockContainer lock;
	private int remainingTicks;
	private int wuBuffer;
	
	public CoalGeneratorEntity() {
		super(UMBlocks.COAL_GENERATOR_ENTITY);
	}

	@Override
	public void pulse() {
		
	}
}
