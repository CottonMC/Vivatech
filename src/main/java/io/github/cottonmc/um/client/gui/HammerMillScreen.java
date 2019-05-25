package io.github.cottonmc.um.client.gui;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import io.github.cottonmc.um.block.container.HammerMillController;
import net.minecraft.entity.player.PlayerEntity;

public class HammerMillScreen extends CottonScreen<HammerMillController> {
	public HammerMillScreen(HammerMillController container, PlayerEntity player) {
		super(container, player);
	}
}
