package vivatech.client.screen;

import io.github.cottonmc.cotton.gui.client.CottonScreen;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.minecraft.entity.player.PlayerEntity;
import vivatech.common.block.ElectricFurnaceBlock;
import vivatech.common.menu.ElectricFurnaceMenu;
import vivatech.util.StringHelper;

public class ElectricFurnaceScreen extends CottonScreen<ElectricFurnaceMenu> {
    public ElectricFurnaceScreen(ElectricFurnaceMenu container, PlayerEntity player) {
        super(container, player);
    }

    @Override
    protected void drawBackground(float partialTicks, int mouseX, int mouseY) {
        super.drawBackground(partialTicks, mouseX, mouseY);

        // Title
        String title = StringHelper.getTranslatableComponent("block", ElectricFurnaceBlock.ID).asString();
        font.draw(title, left + 81 - font.getStringWidth(title) / 2, top, WLabel.DEFAULT_TEXT_COLOR);
    }
}
