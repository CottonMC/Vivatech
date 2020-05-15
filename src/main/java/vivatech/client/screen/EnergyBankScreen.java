package vivatech.client.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.client.VivatechClient;
import vivatech.common.block.EnergyBankBlock;
import vivatech.common.menu.EnergyBankMenu;
import vivatech.util.StringHelper;

public class EnergyBankScreen extends CottonInventoryScreen<EnergyBankMenu> {
    public EnergyBankScreen(EnergyBankMenu container, PlayerEntity player) {
        super(container, player);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);

        // Title
        String title = StringHelper.getTranslatableComponent("block", EnergyBankBlock.ID).asString();
        font.draw(title, x + 81 - font.getStringWidth(title) / 2, y, WLabel.DEFAULT_TEXT_COLOR);
    }
}
