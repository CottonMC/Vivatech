package vivatech.rei.widget;

import io.github.cottonmc.cotton.gui.widget.WBar;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.util.Identifier;

/**
 * A {@link WBar} subclass that sets the property delegate through the constructor.
 */
final class RenderWBar extends WBar {
    RenderWBar(Identifier bg, Identifier bar, int field, int maxfield, Direction dir, PropertyDelegate properties) {
        super(bg, bar, field, maxfield, dir);
        this.properties = properties;
    }
}
