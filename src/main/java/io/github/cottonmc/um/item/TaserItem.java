package io.github.cottonmc.um.item;

import io.github.cottonmc.energy.impl.SimpleEnergyComponent;
import io.github.cottonmc.um.UnitedManufacturing;
import io.github.prospector.silk.util.ActionType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.Hand;

public class TaserItem extends Item {

	public static final SimpleEnergyComponent NO_COMPONENT = new SimpleEnergyComponent(0);

	public static final int ENERGY_PER_USE = 4;
	public static final int MAX_ENERGY = 16;

	public TaserItem() {
		super(new Item.Settings().itemGroup(UnitedManufacturing.ITEMGROUP_TOOLS).stackSize(1));
	}

	@Override
	public boolean interactWithEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		SimpleEnergyComponent energy = getEnergyComponent(stack);
		System.out.println("before: "+energy.getCurrentEnergy());
		if (energy.getCurrentEnergy() < ENERGY_PER_USE) {
			//TODO: change once we have a charger
			if (player.world.isClient) player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYER, 1.0F, random.nextFloat() * 0.4F + 0.8F);
			else {
				player.addChatMessage(new StringTextComponent("Battery inserted!"), true);
				player.setStackInHand(hand, setEnergyComponent(stack, new SimpleEnergyComponent(MAX_ENERGY).setCurrentEnergy(MAX_ENERGY).setSaveMax(true)));
			}
			return true;
		}
		if (player.world.isClient) {
			player.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYER, 1.0F, random.nextFloat() * 0.4F + 0.8F);
			return true;
		}
		energy.extractEnergy(ENERGY_PER_USE, ActionType.PERFORM);
		player.setStackInHand(hand, setEnergyComponent(stack, energy));
		System.out.println("after: "+energy.getCurrentEnergy());
		LightningEntity lightning = new LightningEntity(target.world, target.getPos().getX(), target.getPos().getY(), target.getPos().getZ(), true);
		lightning.setChanneller((ServerPlayerEntity)player);
		target.onStruckByLightning(lightning);
		player.attack(target);
		target.damage(DamageSource.LIGHTNING_BOLT, 4);
		return super.interactWithEntity(stack, player, target, hand);
	}

	public static SimpleEnergyComponent getEnergyComponent(ItemStack stack) {
		if (!stack.hasTag() || !stack.getTag().containsKey("Energy")) return NO_COMPONENT;
		CompoundTag tag = stack.getTag().getCompound("Energy");
		SimpleEnergyComponent energy = new SimpleEnergyComponent(tag.getInt("MaxEnergy"));
		energy.fromTag(tag);
		return energy;
	}

	public static ItemStack setEnergyComponent(ItemStack stack, SimpleEnergyComponent component) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.containsKey("Energy")) tag.remove("Energy");
		Tag nbt = component.toTag();
		tag.put("Energy", nbt);
		stack.setTag(tag);
		return stack;
	}
}
