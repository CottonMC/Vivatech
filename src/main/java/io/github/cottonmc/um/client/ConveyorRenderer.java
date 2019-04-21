package io.github.cottonmc.um.client;

import com.mojang.blaze3d.platform.GlStateManager;

import io.github.cottonmc.um.block.entity.ConveyorEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Direction;

public class ConveyorRenderer extends BlockEntityRenderer<ConveyorEntity> {
	@Override
	public void render(ConveyorEntity blockEntity_1, double double_1, double double_2, double double_3, float float_1, int int_1) {
		// TODO Auto-generated method stub
		super.render(blockEntity_1, double_1, double_2, double_3, float_1, int_1);
		System.out.println("SUCCESS");
		
		/* // CampfireBlockEntityRenderer's item renderer follows:
		Direction dir = Direction.NORTH;
		DefaultedList<ItemStack> items = DefaultedList.create();
		for(int int_2 = 0; int_2 < items.size(); ++int_2) {
			ItemStack itemStack_1 = items.get(int_2);
			if (itemStack_1 != ItemStack.EMPTY) {
				GlStateManager.pushMatrix();
				GlStateManager.translatef((float)double_1 + 0.5F, (float)double_2 + 0.44921875F, (float)double_3 + 0.5F);
				Direction direction_2 = Direction.fromHorizontal((int_2 + dir.getHorizontal()) % 4);
				GlStateManager.rotatef(-direction_2.asRotation(), 0.0F, 1.0F, 0.0F);
				GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translatef(-0.3125F, -0.3125F, 0.0F);
				GlStateManager.scalef(0.375F, 0.375F, 0.375F);
				MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack_1, ModelTransformation.Type.FIXED);
				GlStateManager.popMatrix();
			}
		}*/

	}
}
