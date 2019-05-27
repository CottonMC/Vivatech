package vivatech.screen;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.controller.CoalGeneratorController;

public class CoalGeneratorScreen extends CottonScreen<CoalGeneratorController> {
    public CoalGeneratorScreen(CoalGeneratorController container, PlayerEntity player) {
        super(container, player);
    }
}
