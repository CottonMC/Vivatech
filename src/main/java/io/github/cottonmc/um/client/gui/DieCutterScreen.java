package io.github.cottonmc.um.client.gui;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import io.github.cottonmc.um.block.container.DieCutterController;
import net.minecraft.entity.player.PlayerEntity;

public class DieCutterScreen extends CottonScreen<DieCutterController> {
	public DieCutterScreen(DieCutterController container, PlayerEntity player) {
		super(container, player);
	}
}
