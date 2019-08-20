package vivatech.common.block.entity;

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.DefaultEnergyTypes;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyType;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
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
import net.minecraft.util.math.Direction;
import vivatech.api.tier.Tier;
import vivatech.api.tier.Tiered;
import vivatech.api.wire.WireConnector;
import vivatech.api.wire.WireType;
import vivatech.common.block.EnergyConnectorBlock;
import vivatech.common.init.VivatechEntities;
import vivatech.common.init.VivatechWires;
import vivatech.api.wire.WireItem;

import java.util.*;

public class EnergyConnectorBlockEntity extends BlockEntity implements Tickable, Tiered, BlockEntityClientSerializable, WireConnector {
	private Set<BlockPos> peers = new HashSet<>();
	private SimpleEnergyAttribute energy;
	private EnergyType type;
	private WireType wire;

	public EnergyConnectorBlockEntity() {
		super(VivatechEntities.CONNECTOR);
	}

	public EnergyConnectorBlockEntity(Tier tier) {
		this();
		this.energy = new SimpleEnergyAttribute(200*tier.getEnergyMultiplier(), tier.getEnergyType());
		this.type = tier.getEnergyType();
		energy.listen(this::markDirty);
		this.wire = getForEnergyType(type);
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
		energy.fromTag(tag.getCompound("Energy"));
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
		tag.put("Energy", energy.toTag());
		return tag;
	}

	@Override
	public void fromClientTag(CompoundTag compoundTag) {

	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		return null;
	}

	@Override
	public void tick() {
		//TODO: sparking
		for (BlockPos pos : peers) {
			EnergyAttribute peer = EnergyAttribute.ENERGY_ATTRIBUTE.getFirst(world, pos);
			if (peer != EnergyAttribute.EMPTY_ENERGY) {
				if (peer.getPreferredType().isCompatibleWith(energy.getPreferredType())) {
					if (energy.getCurrentEnergy() > peer.getCurrentEnergy()) {
						int tryExtract = energy.extractEnergy(type, type.getMaximumTransferSize(), Simulation.SIMULATE);
						int tryInsert = peer.insertEnergy(type, tryExtract, Simulation.SIMULATE);
						if (tryInsert > 0) {
							energy.extractEnergy(type, tryInsert, Simulation.ACTION);
							peer.insertEnergy(type, tryInsert, Simulation.ACTION);
						}
					}
				}
			}
		}
		Direction offset = world.getBlockState(getPos()).get(EnergyConnectorBlock.FACING);
		EnergyAttribute insertInto = EnergyAttribute.ENERGY_ATTRIBUTE.getFirst(world, pos.offset(offset), SearchOptions.inDirection(offset.getOpposite()));
		if (insertInto != EnergyAttribute.EMPTY_ENERGY) {
			if (insertInto.getPreferredType().isCompatibleWith(energy.getPreferredType())) {
				if (energy.getCurrentEnergy() > insertInto.getCurrentEnergy()) {
					int tryExtract = energy.extractEnergy(type, type.getMaximumTransferSize(), Simulation.SIMULATE);
					int tryInsert = insertInto.insertEnergy(type, tryExtract, Simulation.SIMULATE);
					if (tryInsert > 0) {
						energy.extractEnergy(type, tryInsert, Simulation.ACTION);
						insertInto.insertEnergy(type, tryInsert, Simulation.ACTION);
					}
				}
			}
		}
	}

	@Override
	public Map<WireType, Collection<BlockPos>> getAllPeers() {
		Map<WireType, Collection<BlockPos>> ret = new HashMap<>();
		ret.put(wire, peers);
		return ret;
	}

	@Override
	public Collection<BlockPos> getPeers(WireType type) {
		if (type.equals(wire)) return peers;
		else return new HashSet<>();
	}

	@Override
	public boolean addPeer(BlockPos pos, WireType type) {
		if (type.equals(wire) && !peers.contains(pos)) {
			peers.add(pos);
			return true;
		}
		else return false;
	}

	@Override
	public boolean removePeer(BlockPos pos, WireType type) {
		if (type.equals(wire) && peers.contains(pos)) {
			peers.remove(pos);
			return true;
		}
		else return false;
	}

	@Override
	public void dropAllPeers() {
		dropPeers(wire);
	}

	@Override
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

	@Override
	public boolean canAcceptWire(WireType type) {
		return type.equals(getForEnergyType(this.type));
	}

	@Override
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
	public Identifier getTierId() {
		Block block = this.getWorld().getBlockState(this.getPos()).getBlock();
		if (block instanceof Tiered) {
			return ((Tiered) block).getTierId();
		} else {
			return null;
		}
	}

	public EnergyAttribute getEnergy() {
		return energy;
	}

	public static WireType getForEnergyType(EnergyType type) {
		//TODO: move somewhere else?
		if (type == DefaultEnergyTypes.LOW_VOLTAGE) return VivatechWires.LOW_VOLTAGE;
		else if (type == DefaultEnergyTypes.MEDIUM_VOLTAGE) return VivatechWires.MEDIUM_VOLTAGE;
		else if (type == DefaultEnergyTypes.HIGH_VOLTAGE) return VivatechWires.HIGH_VOLTAGE;
		else return null;
	}
}
