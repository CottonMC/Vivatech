package vivatech.client.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.common.block.PressBlock;
import vivatech.common.menu.PressMenu;
import vivatech.util.StringHelper;

public class PressScreen extends CottonInventoryScreen<PressMenu> {
    public PressScreen(PressMenu container, PlayerEntity player) {
        super(container, player);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);

        // Title
        String title = StringHelper.getTranslatableComponent("block", PressBlock.ID).asString();
        font.draw(title, x + 81 - font.getStringWidth(title) / 2, y, WLabel.DEFAULT_TEXT_COLOR);
    }
}
