package vivatech.util;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class StringHelper {
    public static String getTranslationKey(String type, Identifier identifier) {
        return String.format("%s.%s.%s", type, identifier.getNamespace(), identifier.getPath());
    }
    public static TranslatableText getTranslatableComponent(String type, Identifier identifier, Object... params) {
        return new TranslatableText(getTranslationKey(type, identifier), params);
    }
}
