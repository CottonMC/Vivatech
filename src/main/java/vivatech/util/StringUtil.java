package vivatech.util;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;

public class StringUtil {
    public static TranslatableComponent getTranslatableComponent(String type, Identifier identifier) {
        return new TranslatableComponent(String.format("%s.%s.%s", type, identifier.getNamespace(), identifier.getPath()));
    }
}
