package vivatech.screen;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.block.CrusherBlock;
import vivatech.menu.CrusherMenu;
import vivatech.util.StringHelper;

public class CrusherScreen extends CottonScreen<CrusherMenu> {
    public CrusherScreen(CrusherMenu container, PlayerEntity player) {
        super(container, player);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);

        // Title
        String title = StringHelper.getTranslatableComponent("block", CrusherBlock.ID).asString();
        font.draw(title, left + 81 - font.getStringWidth(title) / 2, top, WLabel.DEFAULT_TEXT_COLOR);
    }
}
