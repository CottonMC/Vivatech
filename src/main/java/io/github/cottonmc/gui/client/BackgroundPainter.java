package io.github.cottonmc.gui.client;

import io.github.cottonmc.gui.widget.WPanel;

public interface BackgroundPainter {
	/**
	 * Paint the specified panel to the screen.
	 * @param left The absolute position of the left of the panel, in gui-screen coordinates
	 * @param top The absolute position of the top of the panel, in gui-screen coordinates
	 * @param panel The panel being painted
	 */
	public void paintBackground(int left, int top, WPanel panel);
	
	
	
	public static BackgroundPainter VANILLA = (left, top, panel) -> {
		ScreenDrawing.drawGuiPanel(left, top, panel.getWidth(), panel.getHeight());
		
	};
	
	
	
	public static BackgroundPainter createColorful(int panelColor) {
		return (left, top, panel) -> {
			ScreenDrawing.drawGuiPanel(left, top, panel.getWidth(), panel.getHeight(), panelColor);
		};
	}
	
	public static BackgroundPainter createColorful(int panelColor, float contrast) {
		return (left, top, panel) -> {
			int shadowColor = ScreenDrawing.multiplyColor(panelColor, 1.0f - contrast);
			int hilightColor = ScreenDrawing.multiplyColor(panelColor, 1.0f + contrast);
			
			ScreenDrawing.drawGuiPanel(left, top, panel.getWidth(), panel.getHeight(), shadowColor, panelColor, hilightColor, 0xFF000000);
		};
	}
}
