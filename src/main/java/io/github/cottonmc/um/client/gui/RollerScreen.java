package io.github.cottonmc.um.client.gui;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import io.github.cottonmc.um.block.container.RollerController;
import net.minecraft.entity.player.PlayerEntity;

public class RollerScreen extends CottonScreen<RollerController> {
	public RollerScreen(RollerController container, PlayerEntity player) {
		super(container, player);
	}
}
