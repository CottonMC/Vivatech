package vivatech.api.tier;

import net.minecraft.util.Identifier;

public interface Tiered {
    Identifier getBaseId();

    Identifier getTieredId();

    Tier getTier();
}
