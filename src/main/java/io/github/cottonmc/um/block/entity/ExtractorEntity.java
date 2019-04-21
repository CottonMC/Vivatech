package io.github.cottonmc.um.block.entity;

import io.github.cottonmc.um.block.UMBlocks;
import io.github.cottonmc.um.component.SimpleItemComponent;
import net.minecraft.block.entity.BlockEntity;

public class ExtractorEntity extends BlockEntity {
	SimpleItemComponent items = new SimpleItemComponent(1);
	
	public ExtractorEntity() {
		super(UMBlocks.EXTRACTOR_ENTITY);
		
		items.listen(this::markDirty);
	}
	
}
