package vivatech.client.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.common.menu.CrusherMenu;
import vivatech.data.BlockData;
import vivatech.util.StringHelper;

public class CrusherScreen extends CottonInventoryScreen<CrusherMenu> {
    public CrusherScreen(CrusherMenu container, PlayerEntity player) {
        super(container, player);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);

        // Title
        String title = StringHelper.getTranslatableComponent("block", BlockData.Ids.CRUSHER).asString();
        font.draw(title, x + 81 - font.getStringWidth(title) / 2, y, WLabel.DEFAULT_TEXT_COLOR);
    }
}
