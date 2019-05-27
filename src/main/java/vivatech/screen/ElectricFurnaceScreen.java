package vivatech.screen;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.controller.ElectricFurnaceController;

public class ElectricFurnaceScreen extends CottonScreen<ElectricFurnaceController> {
    public ElectricFurnaceScreen(ElectricFurnaceController container, PlayerEntity player) {
        super(container, player);
    }
}
