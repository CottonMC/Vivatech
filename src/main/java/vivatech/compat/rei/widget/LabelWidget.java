package vivatech.compat.rei.widget;

import java.util.Collections;
import java.util.List;

import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class LabelWidget extends Widget {
	protected int x;
	protected int y;
	protected Text text;
	
	public LabelWidget(int x, int y, Text component) {
		this.text = component;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void render(int mouseX, int mouseY, float delta) {
		font.draw(text.asFormattedString(), x, y, 0x666666);
	}

	
	@Override
	public List<? extends Element> children() {
		return Collections.emptyList();
	}

}
