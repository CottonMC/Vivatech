package vivatech.screen;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.block.CoalGeneratorBlock;
import vivatech.controller.CoalGeneratorController;
import vivatech.util.StringHelper;

public class CoalGeneratorScreen extends CottonScreen<CoalGeneratorController> {
    public CoalGeneratorScreen(CoalGeneratorController container, PlayerEntity player) {
        super(container, player);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);

        // Title
        String title = StringHelper.getTranslatableComponent("block", CoalGeneratorBlock.ID).asString();
        font.draw(title, left + 81 - font.getStringWidth(title) / 2, top, WLabel.DEFAULT_TEXT_COLOR);
    }
}
