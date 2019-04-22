package io.github.cottonmc.um.client;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.cottonmc.um.block.entity.ConveyorEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

public class ConveyorRenderer extends BlockEntityRenderer<ConveyorEntity> {
	@Override
	public void render(ConveyorEntity conveyor, double x, double y, double z, float float_1, int int_1) {
		// TODO Auto-generated method stub
		super.render(conveyor, x, y, z, float_1, int_1);
		//System.out.println("SUCCESS");
		
		SidedInventory inv = conveyor.getInventory();
		
		ItemStack stack = inv.getInvStack(0);
		
		if (!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			
			GlStateManager.translatef((float)x + 0.5f, (float)y + 0.6f, (float)z + 0.5f);
			GlStateManager.rotatef(90, 1, 0, 0);
			//GlStateManager.scalef(0.375F, 0.375F, 0.375F);
			GlStateManager.scalef(0.8f, 0.8f, 0.8f);
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Type.FIXED);
			GlStateManager.popMatrix();
			
		}
	}
}
