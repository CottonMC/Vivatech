package io.github.cottonmc.um.block;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

public class ExtractorBlock extends Block implements BlockEntityProvider, InventoryProvider {

	public ExtractorBlock() {
		super(FabricBlockSettings
				.of(Material.METAL, DyeColor.MAGENTA)
				.hardness(4)
				.resistance(10)
				.build());
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		
		return null;
	}

	@Override
	public SidedInventory getInventory(BlockState var1, IWorld var2, BlockPos var3) {
		
		return null;
	}
	
	
	
}
