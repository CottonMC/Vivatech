package io.github.cottonmc.gui;

import io.github.cottonmc.gui.client.BackgroundPainter;
import io.github.cottonmc.gui.widget.WGridPanel;
import io.github.cottonmc.gui.widget.WPanel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.container.ArrayPropertyDelegate;
import net.minecraft.container.ContainerType;
import net.minecraft.container.CraftingContainer;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;

public abstract class CottonScreenController extends CraftingContainer<Inventory> {
	protected Inventory inventory;
	protected PlayerInventory playerInventory;
	protected RecipeType<?> recipeType;
	protected World world;
	protected PropertyDelegate propertyDelegate;
	
	protected WPanel rootPanel = new WGridPanel();
	protected int titleColor = 0xFF404040;
	
	public CottonScreenController(ContainerType<?> containerType,  RecipeType<?> recipeType, int syncId, PlayerInventory playerInventory) {
		super(containerType, syncId);
		this.inventory = null;
		this.playerInventory = playerInventory;
		this.recipeType = recipeType;
		this.world = playerInventory.player.world;
		this.propertyDelegate = new ArrayPropertyDelegate(1);
	}
	
	public CottonScreenController(ContainerType<?> containerType,  RecipeType<? extends Inventory> recipeType, int syncId, PlayerInventory playerInventory, Inventory blockInventory, PropertyDelegate propertyDelegate) {
		super(containerType, syncId);
		this.inventory = null;
		this.playerInventory = playerInventory;
		this.recipeType = recipeType;
		this.world = playerInventory.player.world;
	}
	
	public WPanel getRootPanel() {
		return rootPanel;
	}
	
	public int getTitleColor() {
		return titleColor;
	}
	
	public CottonScreenController setRootPanel(WPanel panel) {
		this.rootPanel = panel;
		return this;
	}
	
	public CottonScreenController setTitleColor(int color) {
		this.titleColor = color;
		return this;
	}
	
	@Environment(EnvType.CLIENT)
	public void addPainters() {
		if (this.rootPanel!=null) {
			this.rootPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
		}
	}
	
	//TODO: Is this one needed?
	/*
	public void initContainerSlot(int slot, int x, int y) {
		this.addSlot(new ValidatedSlot(inventory, slot, x * 18, y * 18));
	}*/
	
	public void addSlotPeer(ValidatedSlot slot) {
		this.addSlot(slot);
	}
	
	//extends CraftingContainer<Inventory> {
		@Override
		public void populateRecipeFinder(RecipeFinder recipeFinder) {
			if (this.inventory instanceof RecipeInputProvider) {
				((RecipeInputProvider)this.inventory).provideRecipeInputs(recipeFinder);
			}
		}
		
		@Override
		public void clearCraftingSlots() {
			if (this.inventory!=null) this.inventory.clear();
		}
		
		@Override
		public boolean matches(Recipe<? super Inventory> recipe) {
			if (inventory==null || world==null) return false;
			return false; //TODO recipe support
		}
		
		@Override
		public abstract int getCraftingResultSlotIndex();

		@Override
		public int getCraftingWidth() {
			return 1;
		}

		@Override
		public int getCraftingHeight() {
			return 1;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public int getCraftingSlotCount() {
			return 1;
		}
		
		//(implied) extends Container {
			@Override
			public boolean canUse(PlayerEntity entity) {
				return (inventory!=null) ? inventory.canPlayerUseInv(entity) : true;
			}
		//}
	//}
}
