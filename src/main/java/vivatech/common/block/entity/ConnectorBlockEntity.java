package vivatech.common.block.entity;

import alexiil.mc.lib.attributes.SearchOptions;
import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyAttributeProvider;
import io.github.cottonmc.energy.api.EnergyType;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import vivatech.api.tier.Tier;
import vivatech.api.tier.Tiered;
import vivatech.common.block.ConnectorBlock;
import vivatech.common.init.VivatechEntities;

import java.util.HashSet;
import java.util.Set;


public class ConnectorBlockEntity extends BlockEntity implements Tickable, Tiered, BlockEntityClientSerializable, EnergyAttributeProvider {
	private Set<BlockPos> peers = new HashSet<>();
	private SimpleEnergyAttribute energy;
	private EnergyType type;

	public ConnectorBlockEntity() {
		super(VivatechEntities.CONNECTOR);
	}

	public ConnectorBlockEntity(Tier tier) {
		this();
		this.energy = new SimpleEnergyAttribute(200*tier.getEnergyMultiplier(), tier.getEnergyType());
		this.type = tier.getEnergyType();
		energy.listen(this::markDirty);
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
				} //TODO sparking
			}
		}
		Direction offset = world.getBlockState(getPos()).get(ConnectorBlock.FACING);
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
			} //TODO sparking
		}
	}

	public void addPeer(BlockPos pos) {
		peers.add(pos);
	}

	public void removePeer(BlockPos pos) {
		peers.remove(pos);
	}

	public void dropPeers() {
		int dropCables = 0;
		for (BlockPos pos : peers) {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof ConnectorBlockEntity) {
				ConnectorBlockEntity connector = (ConnectorBlockEntity)be;
				connector.removePeer(this.getPos());
				dropCables++;
			}
		}
		//TODO: drop items
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

	@Override
	public EnergyAttribute getEnergyAttribute() {
		return energy;
	}
}
