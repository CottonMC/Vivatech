package io.github.cottonmc.um.client.gui;

import io.github.cottonmc.gui.client.CottonScreen;
import io.github.cottonmc.um.block.container.CoalGeneratorController;
import net.minecraft.entity.player.PlayerEntity;

public class CoalGeneratorScreen extends CottonScreen<CoalGeneratorController> {

	public CoalGeneratorScreen(CoalGeneratorController container, PlayerEntity player) {
		super(container, player);
	}

}
