package io.github.cottonmc.um.block.container;

import io.github.cottonmc.gui.CottonScreenController;
import io.github.cottonmc.gui.client.BackgroundPainter;
import io.github.cottonmc.gui.widget.WGridPanel;
import io.github.cottonmc.gui.widget.WItemSlot;
import io.github.cottonmc.um.block.entity.CoalGeneratorEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.container.BlockContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;

public class CoalGeneratorController extends CottonScreenController {
	private Inventory playerInventory;
	private Inventory blockInventory;

	public CoalGeneratorController(int syncId, PlayerInventory playerInventory, BlockContext context) {
		super(null, RecipeType.SMELTING, syncId, playerInventory);
		
		this.playerInventory = playerInventory;
		blockInventory = context.run((world, pos) -> {
			BlockState state = world.getBlockState(pos);
			Block b = state.getBlock();
			
			if (b instanceof InventoryProvider) {
				System.out.println("Retrieved inventory.");
				return ((InventoryProvider)b).getInventory(state, world, pos);
			}
			System.out.println("Did not retrieve inventory.");
			return null;
		}).orElse(null);
		
		
		WGridPanel rootPanel = (WGridPanel) getRootPanel();
		if (blockInventory!=null) {
			WItemSlot inputSlot = WItemSlot.of(blockInventory, 0);
			rootPanel.add(inputSlot, 1, 1);
		} else {
			System.out.println("IVNENTORY WAS NULL");
		}
		
		rootPanel.validate(this);
	}

	@Override
	public int getCraftingResultSlotIndex() {
		return CoalGeneratorEntity.SLOT_FUEL; //Not really, but there technically is none.
	}
	
	@Override
	public void addPainters() {
		this.getRootPanel().setBackgroundPainter(BackgroundPainter.createColorful(0xFF075745, 0.10f));
	}
}
