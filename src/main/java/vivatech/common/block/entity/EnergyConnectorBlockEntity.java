package vivatech.common.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import vivatech.api.tier.Tier;
import vivatech.api.tier.Tiered;
import vivatech.api.wire.WireType;
import vivatech.common.init.VivatechBlockEntities;
import vivatech.api.wire.WireItem;

import java.util.*;

public class EnergyConnectorBlockEntity extends BlockEntity implements Tickable, Tiered, BlockEntityClientSerializable {
	private Set<BlockPos> peers = new HashSet<>();
	private WireType wire;

	public EnergyConnectorBlockEntity() {
		super(VivatechBlockEntities.CONNECTOR);
	}

	public EnergyConnectorBlockEntity(Tier tier) {
		this();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		peers.clear();
		ListTag peerTag = tag.getList("Peers", NbtType.COMPOUND);
		for (Tag peer : peerTag) {
			CompoundTag comp = (CompoundTag)peer;
			BlockPos pos = new BlockPos(comp.getInt("x"), comp.getInt("y"), comp.getInt("z"));
			peers.add(pos);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		ListTag peerTag = new ListTag();
		for (BlockPos pos : peers) {
			CompoundTag peer = new CompoundTag();
			peer.putInt("x", pos.getX());
			peer.putInt("y", pos.getY());
			peer.putInt("z", pos.getZ());
			peerTag.add(peer);
		}
		tag.put("Peers", peerTag);
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}

	@Override
	public void tick() {
	}

	public Map<WireType, Collection<BlockPos>> getAllPeers() {
		Map<WireType, Collection<BlockPos>> ret = new HashMap<>();
		ret.put(wire, peers);
		return ret;
	}

	public Collection<BlockPos> getPeers(WireType type) {
		if (type.equals(wire)) return peers;
		else return new HashSet<>();
	}

	public boolean addPeer(BlockPos pos, WireType type) {
		if (type.equals(wire) && !peers.contains(pos)) {
			peers.add(pos);
			return true;
		}
		else return false;
	}

	public boolean removePeer(BlockPos pos, WireType type) {
		if (type.equals(wire) && peers.contains(pos)) {
			peers.remove(pos);
			return true;
		}
		else return false;
	}

	public void dropAllPeers() {
		dropPeers(wire);
	}

	public void dropPeers(WireType type) {
		int dropCables = 0;
		for (BlockPos pos : peers) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof EnergyConnectorBlockEntity) {
				EnergyConnectorBlockEntity connector = (EnergyConnectorBlockEntity)be;
				connector.removePeer(this.getPos(), wire);
				dropCables++;
			}
		}
		if (dropCables > 0) {
			ItemEntity stack = new ItemEntity(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), new ItemStack(WireItem.TYPE_TO_WIRE.get(wire), dropCables));
			world.spawnEntity(stack);
		}
	}

	public boolean canConnectTo(BlockPos pos, WireType type) {
		return false;
	}

	@Override
	public Tier getTier() {
		Block block = this.getWorld().getBlockState(this.getPos()).getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getTier();
		} else {
			return Tier.MINIMAL;
		}
	}

	@Override
	public Identifier getBaseId() {
		Block block = getCachedState().getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getBaseId();
		} else {
			return null;
		}
	}
	@Override
	public Identifier getTieredId() {
		Block block = this.getWorld().getBlockState(this.getPos()).getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getTieredId();
		} else {
			return null;
		}
	}
}
