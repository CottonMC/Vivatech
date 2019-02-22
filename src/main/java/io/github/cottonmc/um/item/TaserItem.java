package io.github.cottonmc.um.item;

import io.github.cottonmc.um.UnitedManufacturing;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class TaserItem extends Item {

	public TaserItem() {
		super(new Item.Settings().itemGroup(UnitedManufacturing.ITEMGROUP_TOOLS).stackSize(1));
	}

	@Override
	public boolean interactWithEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (player.world.isClient) {
			player.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYER, 1.0F, random.nextFloat() * 0.4F + 0.8F);
			return true;
		}
		LightningEntity lightning = new LightningEntity(target.world, target.getPos().getX(), target.getPos().getY(), target.getPos().getZ(), true);
		lightning.setChanneller((ServerPlayerEntity)player);
		target.onStruckByLightning(lightning);
		player.attack(target);
		target.damage(DamageSource.LIGHTNING_BOLT, 4);
		return super.interactWithEntity(stack, player, target, hand);
	}
}
