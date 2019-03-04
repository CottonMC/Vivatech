package io.github.cottonmc.um.mixin;

import io.github.cottonmc.um.component.wrapper.SidedInventoryView;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ChestBlock.class)
public abstract class ChestInventoryMixin extends BlockWithEntity implements InventoryProvider {

	protected ChestInventoryMixin(Settings block$Settings_1) {
		super(block$Settings_1);
	}

	@Override
	public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
		return new SidedInventoryView(ChestBlock.method_17458(state, (World) world, pos, true));
	}

}
