package io.github.cottonmc.um.client.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WBar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

public class WLEDBar extends WBar {
	Identifier ledOff = new Identifier("united-manufacturing", "textures/gui/green_led.png");
	Identifier ledOn = new Identifier("united-manufacturing", "textures/gui/green_led_on.png");

	public WLEDBar(int field, int maxfield, Direction direction) {
		super(null, null, field, maxfield, direction);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void paintBackground(int x, int y) {
		float percent = properties.get(field) / (float) properties.get(max);
		if (percent < 0) percent = 0f;
		if (percent > 1) percent = 1f;
		
		//int barMax = getWidth();
		//if (direction == Direction.DOWN || direction == Direction.UP) barMax = getHeight();
		//percent = ((int) (percent * barMax)) / (float) barMax; //Quantize to bar size
		
		//int barSize = (int) (barMax * percent);
		//if (barSize <= 0) return;
		
		//Default to a RIGHT configuration
		int curX = 0;
		int curY = 0;
		int deltaX = 14;
		int deltaY = 0;
		
		switch(direction) {
			case UP: {
				curX = 0;
				curY = this.getHeight()-14;
				deltaX = 0;
				deltaY = -14;
				break;
			}
			case RIGHT: {
				//leave everything the way it is
				break;
			}
			case DOWN: {
				curX = 0;
				curY = 0;
				deltaX = 0;
				deltaY = 14;
				break;
			}
			case LEFT: {
				curX = this.getWidth()-14;
				curY = 0;
				deltaX = -14;
				deltaY = 0;
				break;
			}
		}
		
		//draw roundrect
		ScreenDrawing.drawGuiPanel(x+4, y, 14 + deltaX*4 + 8, 14 + deltaY*4 + 8, 0x50FFFFFF, 0x00000000, 0x50000000, 0x00000000);
		
		for(int i=0; i<5; i++) {
			float pct = i/5f;
			Identifier cur = (pct<percent) ? ledOn : ledOff;
			
			ScreenDrawing.rect(cur, 4+ x+4+curX, y+4+curY, 14, 14, 0, 0, 1, 1, 0xFFFFFFFF);
			
			curX += deltaX;
			curY += deltaY;
		}
	}
	
	
	
}
