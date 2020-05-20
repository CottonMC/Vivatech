package vivatech.api.tier;

import net.minecraft.util.Identifier;

public interface Tiered {
    Identifier getTieredId();

    Tier getTier();
}
