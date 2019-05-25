package io.github.cottonmc.um.item;

import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.DefaultEnergyTypes;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import io.github.cottonmc.um.UnitedManufacturing;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class TaserItem extends Item {

	public static final SimpleEnergyAttribute NO_COMPONENT = new SimpleEnergyAttribute(0);

	public static final int ENERGY_PER_USE = 4;
	public static final int MAX_ENERGY = 16;

	public TaserItem() {
		super(new Item.Settings().itemGroup(UnitedManufacturing.ITEMGROUP_TOOLS).stackSize(1));
	}

	@Override
	public boolean interactWithEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		SimpleEnergyAttribute energy = getEnergyAttribute(stack);
		System.out.println("before: "+energy.getCurrentEnergy());
		if (energy.getCurrentEnergy() < ENERGY_PER_USE) {
			//TODO: change once we have a charger
			if (player.world.isClient) player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
			else {
				player.addChatMessage(new TextComponent("Battery inserted!"), true);
				player.setStackInHand(hand, setEnergyComponent(stack, new SimpleEnergyAttribute(MAX_ENERGY).setCurrentEnergy(MAX_ENERGY)));
			}
			return true;
		}
		if (player.world.isClient) {
			player.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
			return true;
		}
		energy.extractEnergy(DefaultEnergyTypes.LOW_VOLTAGE, ENERGY_PER_USE, Simulation.ACTION);
		player.setStackInHand(hand, setEnergyComponent(stack, energy));
		System.out.println("after: "+energy.getCurrentEnergy());
		LightningEntity lightning = new LightningEntity(target.world, target.getPos().getX(), target.getPos().getY(), target.getPos().getZ(), true);
		lightning.setChanneller((ServerPlayerEntity)player);
		target.onStruckByLightning(lightning);
		player.attack(target);
		target.damage(DamageSource.LIGHTNING_BOLT, 4);
		return super.interactWithEntity(stack, player, target, hand);
	}

	public static SimpleEnergyAttribute getEnergyAttribute(ItemStack stack) {
		if (!stack.hasTag() || !stack.getTag().containsKey("Energy")) return NO_COMPONENT;
		SimpleEnergyAttribute energy = new SimpleEnergyAttribute(MAX_ENERGY);
		energy.fromTag(stack.getTag().getTag("Energy"));
		return energy;
	}

	public static ItemStack setEnergyComponent(ItemStack stack, SimpleEnergyAttribute component) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.containsKey("Energy")) tag.remove("Energy");
		Tag nbt = component.toTag();
		tag.put("Energy", nbt);
		stack.setTag(tag);
		return stack;
	}
}
