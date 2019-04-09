package io.github.cottonmc.gui;

import alexiil.mc.lib.attributes.item.impl.EmptyFixedItemInv;
import alexiil.mc.lib.attributes.item.impl.FixedInventoryVanillaWrapper;
import io.github.cottonmc.gui.client.BackgroundPainter;
import io.github.cottonmc.gui.widget.WGridPanel;
import io.github.cottonmc.gui.widget.WItemSlot;
import io.github.cottonmc.gui.widget.WPanel;
import io.github.cottonmc.gui.widget.WPlainPanel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.ArrayPropertyDelegate;
import net.minecraft.container.BlockContext;
import net.minecraft.container.CraftingContainer;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;

public abstract class CottonScreenController extends CraftingContainer<Inventory> {
	
	protected Inventory blockInventory;
	protected PlayerInventory playerInventory;
	protected RecipeType<?> recipeType;
	protected World world;
	protected PropertyDelegate propertyDelegate;
	
	protected WPanel rootPanel = new WGridPanel();
	protected int titleColor = 0xFF404040;
	
	public CottonScreenController(RecipeType<?> recipeType, int syncId, PlayerInventory playerInventory) {
		super(null, syncId);
		this.blockInventory = null;
		this.playerInventory = playerInventory;
		this.recipeType = recipeType;
		this.world = playerInventory.player.world;
		this.propertyDelegate = new ArrayPropertyDelegate(1);
	}
	
	public CottonScreenController(RecipeType<?> recipeType, int syncId, PlayerInventory playerInventory, Inventory blockInventory, PropertyDelegate propertyDelegate) {
		super(null, syncId);
		this.blockInventory = blockInventory;
		this.playerInventory = playerInventory;
		this.recipeType = recipeType;
		this.world = playerInventory.player.world;
		this.propertyDelegate = propertyDelegate;
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
	
	@Override
	public ItemStack onSlotClick(int x, int y, SlotActionType action, PlayerEntity player) {
		System.out.println("SlotClick: "+action.name()+" pos:"+x+","+y);
		if (action==SlotActionType.QUICK_MOVE) {
			//TODO: Implement TransferStackInSlot. This override keeps it from crashing for now.
			
			return ItemStack.EMPTY;
		} else {
			return super.onSlotClick(x, y, action, player);
		}
	}
	
	public WPanel createPlayerInventoryPanel() {
		WPlainPanel inv = new WPlainPanel();
		inv.add(WItemSlot.ofPlayerStorage(playerInventory), 0, 0);
		inv.add(WItemSlot.of(playerInventory, 0, 9, 1), 0, 16*4 - 6);
		return inv;
	}
	
	public static Inventory getBlockInventory(BlockContext ctx) {
		return ctx.run((world, pos) -> {
			BlockState state = world.getBlockState(pos);
			Block b = state.getBlock();
			
			if (b instanceof InventoryProvider) {
				return ((InventoryProvider)b).getInventory(state, world, pos);
			}
			return EmptyInventory.INSTANCE;
		}).orElse(EmptyInventory.INSTANCE);
	}
	
	public static PropertyDelegate getBlockPropertyDelegate(BlockContext ctx) {
		return ctx.run((world, pos) -> {
			BlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if (block instanceof PropertyDelegateHolder) {
				return ((PropertyDelegateHolder)block).getPropertyDelegate();
			}
			BlockEntity be = world.getBlockEntity(pos);
			if (be!=null && be instanceof PropertyDelegateHolder) {
				return ((PropertyDelegateHolder)be).getPropertyDelegate();
			}
			
			return new ArrayPropertyDelegate(0);
		}).orElse(new ArrayPropertyDelegate(0));
	}
	
	//extends CraftingContainer<Inventory> {
		@Override
		public void populateRecipeFinder(RecipeFinder recipeFinder) {
			if (this.blockInventory instanceof RecipeInputProvider) {
				((RecipeInputProvider)this.blockInventory).provideRecipeInputs(recipeFinder);
			}
		}
		
		@Override
		public void clearCraftingSlots() {
			if (this.blockInventory!=null) this.blockInventory.clear();
		}
		
		@Override
		public boolean matches(Recipe<? super Inventory> recipe) {
			if (blockInventory==null || world==null) return false;
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
				return (blockInventory!=null) ? blockInventory.canPlayerUseInv(entity) : true;
			}
		//}
	//}
}
