package vivatech.util;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;

public class StringHelper {
    public static String getTranslationKey(String type, Identifier identifier) {
        return String.format("%s.%s.%s", type, identifier.getNamespace(), identifier.getPath());
    }
    public static TranslatableComponent getTranslatableComponent(String type, Identifier identifier, Object... params) {
        return new TranslatableComponent(getTranslationKey(type, identifier), params);
    }
}
