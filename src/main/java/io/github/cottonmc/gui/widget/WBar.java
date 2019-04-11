package io.github.cottonmc.gui.widget;

import java.util.List;

import io.github.cottonmc.gui.client.ScreenDrawing;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.text.TextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;

public class WBar extends WWidget {
	private final Identifier bg;
	private final Identifier bar;
	private final int field;
	private final int max;
	private final PropertyDelegate properties;
	private final Direction direction;
	//private boolean renderTooltip;
	private String tooltipLabel;
	private TextComponent tooltipTextComponent;
	
	public WBar(Identifier bg, Identifier bar, PropertyDelegate properties, int field, int maxfield) {
		this(bg, bar, properties, field, maxfield, Direction.UP);
	}

	public WBar(Identifier bg, Identifier bar, PropertyDelegate properties, int field, int maxfield, Direction dir) {
		this.bg = bg;
		this.bar = bar;
		this.properties = properties;
		this.field = field;
		this.max = maxfield;
		this.direction = dir;
	}

	/**
	 * Adds a tooltip to the WBar.
	 *
	 * Formatting Guide: The tooltip label is passed into String.Format and can recieve two integers
	 * (%d) - the first is the current value of the bar's focused field, and the second is the
	 * bar's focused maximum.
	 * @param label String to render on the tooltip.
	 * @return WBar with tooltip enabled and set.
	 */
	public WBar withTooltip(String label) {
		this.setRenderTooltip(true);
		this.tooltipLabel = label;
		return this;
	}
	
	
	
	public WBar withTooltip(TextComponent label) {
		this.tooltipTextComponent = label;
		return this;
	}
	
	@Override
	public boolean canResize() {
		return true;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void paintBackground(int x, int y) {
		ScreenDrawing.rect(bg, x, y, getWidth(), getHeight(), 0xFFFFFFFF);
		
		float percent = properties.get(field) / (float) properties.get(max);
		if (percent < 0) percent = 0f;
		if (percent > 1) percent = 1f;
		
		int barMax = getWidth();
		if (direction == Direction.DOWN || direction == Direction.UP) barMax = getHeight();
		percent = ((int) (percent * barMax)) / (float) barMax; //Quantize to bar size
		
		int barSize = (int) (barMax * percent);
		if (barSize <= 0) return;
		
		switch(direction) { //anonymous blocks in this switch statement are to sandbox variables
			case UP: {
				int left = x;
				int top = y + getHeight();
				top -= barSize;
				ScreenDrawing.rect(bar, left, top, getWidth(), barSize, 0, 1 - percent, 1, 1, 0xFFFFFFFF);
				break;
			}
			case RIGHT: {
				ScreenDrawing.rect(bar, x, y, barSize, getHeight(), 0, 0, percent, 1, 0xFFFFFFFF);
				break;
			}
			case DOWN: {
				ScreenDrawing.rect(bar, x, y, getWidth(), barSize, 0, 0, 1, percent, 0xFFFFFFFF);
				break;
			}
			case LEFT: {
				int left = x + getWidth();
				int top = y;
				left -= barSize;
				ScreenDrawing.rect(bar, left, top, barSize, getHeight(), 1 - percent, 0, 1, 1, 0xFFFFFFFF);
				break;
			}
		}
	}

	@Override
	public void addInformation(List<String> information) {
		int value = properties.get(field);
		int valMax = properties.get(max);
		String formatted = new TranslatableTextComponent(tooltipLabel, Integer.valueOf(value), Integer.valueOf(valMax)).getFormattedText();
		information.add(String.format(tooltipLabel, value, valMax));
	}

	public static enum Direction {
		UP,
		RIGHT,
		DOWN,
		LEFT;
	}
}