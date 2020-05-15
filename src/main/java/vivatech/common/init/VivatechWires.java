package vivatech.common.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.api.tier.Tier;
import vivatech.api.wire.WireType;
import vivatech.common.Vivatech;
import vivatech.common.wire.EnergyWireType;

public class VivatechWires {
	public static final WireType LOW_VOLTAGE;
	public static final WireType MEDIUM_VOLTAGE;
	public static final WireType HIGH_VOLTAGE;
//	public static final WireType ULTRA_HIGH_VOLTAGE;

	static {
		LOW_VOLTAGE = new EnergyWireType(Tier.MINIMAL);
		MEDIUM_VOLTAGE = new EnergyWireType(Tier.NORMAL);
		HIGH_VOLTAGE = new EnergyWireType(Tier.ADVANCED);
	}

	public static void init() {
		Registry.register(WireType.WIRE_TYPES, new Identifier(Vivatech.MOD_ID, "low_voltage"), LOW_VOLTAGE);
		Registry.register(WireType.WIRE_TYPES, new Identifier(Vivatech.MOD_ID, "medium_voltage"), MEDIUM_VOLTAGE);
		Registry.register(WireType.WIRE_TYPES, new Identifier(Vivatech.MOD_ID, "high_voltage"), HIGH_VOLTAGE);
	}
}
