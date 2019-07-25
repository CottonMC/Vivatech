package vivatech.screen;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.VivatechClient;
import vivatech.block.EnergyBankBlock;
import vivatech.menu.EnergyBankMenu;
import vivatech.util.StringHelper;

public class EnergyBankScreen extends CottonScreen<EnergyBankMenu> {
    public EnergyBankScreen(EnergyBankMenu container, PlayerEntity player) {
        super(container, player);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);

        // Title
        String title = StringHelper.getTranslatableComponent("block", EnergyBankBlock.ID).asString();
        font.draw(title, left + 81 - font.getStringWidth(title) / 2, top, WLabel.DEFAULT_TEXT_COLOR);

        // Decorations
        ScreenDrawing.rect(VivatechClient.ARROW_DOWN, left + 56, top + 28, 14, 14, -1);
        ScreenDrawing.rect(VivatechClient.ARROW_UP, left + 91, top + 28, 14, 14, -1);
    }
}
