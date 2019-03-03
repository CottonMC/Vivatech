package io.github.cottonmc.gui;

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
	protected RecipeType<? extends Inventory> recipeType;
	protected World world;
	protected PropertyDelegate propertyDelegate;
	
	protected WPanel rootPanel;
	protected boolean shouldDrawPanel = true;
	protected int color = 0xFFC6C6C6;
	protected int titleColor = 0xFF404040;
	protected float bevelStrength = 0.72f;
	
	public CottonScreenController(ContainerType<?> containerType,  RecipeType<? extends Inventory> recipeType, int syncId, PlayerInventory playerInventory) {
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
	
	public boolean shouldDrawPanel() {
		return shouldDrawPanel;
	}
	
	public int getColor() {
		return color;
	}
	
	public int getTitleColor() {
		return titleColor;
	}
	
	public float getBevelStrength() {
		return bevelStrength;
	}
	
	public CottonScreenController setRootPanel(WPanel panel) {
		this.rootPanel = panel;
		return this;
	}
	
	public CottonScreenController setShouldDrawPanel(boolean drawPanel) {
		this.shouldDrawPanel = drawPanel;
		return this;
	}
	
	public CottonScreenController setColor(int color) {
		this.color = color;
		return this;
	}
	
	public CottonScreenController setTitleColor(int color) {
		this.titleColor = color;
		return this;
	}
	
	//TODO: port ValidatedSlot
	/*public void initContainerSlot(int slot, int x, int y) {
		this.addSlot(new ValidatedSlot(inventory, slot, x * 18, y * 18));
	}*/
	
	
	
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
