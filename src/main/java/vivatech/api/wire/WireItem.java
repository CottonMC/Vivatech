package vivatech.api.wire;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class WireItem extends Item {
	public static final Map<WireType, Item> TYPE_TO_WIRE = new HashMap<>();
	private WireType type;

	public WireItem(WireType type, Settings settings) {
		super(settings);
		this.type = type;
		TYPE_TO_WIRE.put(type, this);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		//TODO: better messages
		PlayerEntity player = ctx.getPlayer();
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		ItemStack stack = ctx.getStack();
		if (world.getBlockEntity(pos) instanceof WireConnector) {
			WireConnector be = (WireConnector)world.getBlockEntity(pos);
			if (be.canAcceptWire(type)) {
				if (!stack.getOrCreateTag().contains("ConnectTo")) {
					CompoundTag tag = new CompoundTag();
					tag.putInt("x", pos.getX());
					tag.putInt("y", pos.getY());
					tag.putInt("z", pos.getZ());
					stack.putSubTag("ConnectTo", tag);
					return ActionResult.SUCCESS;
				} else {
					CompoundTag tag = stack.getSubTag("ConnectTo");
					BlockPos fromPos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
					double distance = pos.getSquaredDistance(fromPos);
					if (distance < 16d) {
						BlockEntity from = world.getBlockEntity(fromPos);
						if (from instanceof WireConnector) {
							WireConnector fromCon = (WireConnector)from;
							if (fromCon.canAcceptWire(type)) {
								fromCon.addPeer(pos, type);
								be.addPeer(fromPos, type);
								player.addChatMessage(new LiteralText("Linked connectors!"), true);
								return ActionResult.SUCCESS;
							} else {
								player.addChatMessage(new LiteralText("Stored connector cannot accept this wire type!"), true);
							}
						} else {
							player.addChatMessage(new LiteralText("Stored connector has been removed!"), true);
						}
					} else {
						player.addChatMessage(new LiteralText("Connectors are too far away!"), true);
					}
					stack.getTag().remove("ConnectTo");
					return ActionResult.FAIL;
				}
			}
		}
		return ActionResult.PASS;
	}
}
