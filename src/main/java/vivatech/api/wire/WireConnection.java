package vivatech.api.wire;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class WireConnection {
	private BlockPos to;
	private WireType type;
	private boolean render;

	public WireConnection(BlockPos to, WireType type, boolean render) {
		this.to = to;
		this.type = type;
		this.render = render;
	}

	public BlockPos getDestination() {
		return to;
	}

	public WireType getType() {
		return type;
	}

	public boolean shouldRender() {
		return render;
	}

	public CompoundTag toTag() {
		CompoundTag ret = new CompoundTag();
		CompoundTag pos = new CompoundTag();
		pos.putInt("x", to.getX());
		pos.putInt("y", to.getY());
		pos.putInt("z", to.getZ());
		ret.put("Pos", pos);
		ret.putString("Type", WireType.WIRE_TYPES.getId(type).toString());
		ret.putBoolean("Render", render);
		return ret;
	}

	public static WireConnection fromTag(CompoundTag tag) {
		CompoundTag pos = tag.getCompound("Pos");
		return new WireConnection(new BlockPos(pos.getInt("x"), pos.getInt("y"), pos.getInt("z")),
				WireType.WIRE_TYPES.get(new Identifier(tag.getString("Type"))),
				tag.getBoolean("Render"));
	}
}
