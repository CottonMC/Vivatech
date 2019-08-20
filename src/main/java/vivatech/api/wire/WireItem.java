package vivatech.api.wire;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.UseAction;
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
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.NONE;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		ItemStack stack = ctx.getStack();
		if (world.getBlockEntity(pos) instanceof WireConnector) {
			WireConnector be = (WireConnector)world.getBlockEntity(pos);
			if (be.canAcceptWire(type)) {
				CompoundTag tag = new CompoundTag();
				tag.putInt("x", pos.getX());
				tag.putInt("y", pos.getY());
				tag.putInt("z", pos.getZ());
				stack.putSubTag("ConnectTo", tag);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		return super.finishUsing(stack, world, user);
	}
}
