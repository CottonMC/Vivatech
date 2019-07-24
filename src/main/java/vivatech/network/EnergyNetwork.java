package vivatech.network;

import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyAttributeProvider;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import vivatech.Vivatech;
import vivatech.block.EnergyConduitBlock;
import vivatech.entity.EnergyConduitEntity;
import vivatech.util.EnergyConduitConnection;
import vivatech.util.EnergyHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is a modification of corresponding class from
 * <a href="https://github.com/StellarHorizons/Galacticraft-Rewoven">Galacticraft: Rewoven</a>
 */
public class EnergyNetwork implements EnergyAttributeProvider {
    public final static ConcurrentLinkedQueue<EnergyNetwork> networks = new ConcurrentLinkedQueue<>();
    public final List<EnergyConduitEntity> conduits = new ArrayList<>();
    public final UUID id;
    private final SimpleEnergyAttribute energy = new SimpleEnergyAttribute(Integer.MAX_VALUE, Vivatech.INFINITE_VOLTAGE);

    public EnergyNetwork(EnergyConduitEntity sourceConduit) {
        networks.add(this);
        id = UUID.randomUUID();
        conduits.add(sourceConduit);
        networks.forEach(__ -> {
            List<BlockEntity> sourceConduits = new ArrayList<>();
            sourceConduits.add(sourceConduit);
            do {
                for (EnergyConduitEntity conduit : getAdjacentConduits(sourceConduits.get(0).getWorld(), sourceConduits.get(0).getPos())) {
                    if (conduit != null && conduit.networkId != this.getId()) {
                        conduits.add(conduit);
                        EnergyNetwork network = getNetworkFromId(conduit.networkId);
                        if (network != null) {
                            networks.remove(network);
                            network.conduits.forEach(movingConduit -> movingConduit.networkId = this.getId());
                        }

                        conduit.networkId = this.getId();
                        sourceConduits.add(conduit);
                    }
                }
                BlockEntity e = sourceConduits.get(0);
                sourceConduits.remove(e);
            } while (sourceConduits.size() > 0);
        });
        Vivatech.LOGGER.devInfo("Number of energy networks: " + networks.size());
    }

    public UUID getId() {
        return id;
    }

    public void update() {
        List<BlockEntity> adjacentConsumers = new ArrayList<>();
        List<BlockEntity> adjacentProducers = new ArrayList<>();
        AtomicInteger totalEnergyProduced = new AtomicInteger();

        for (EnergyConduitEntity conduit : conduits) {
            if (!(conduit.getWorld().getBlockEntity(conduit.getPos()) instanceof EnergyConduitEntity)) {
                conduits.remove(conduit);
                Vivatech.LOGGER.devInfo("Removed conduit at " + conduit.getPos());
                for (EnergyConduitEntity movingConduit : conduits) {
                    movingConduit.networkId = new EnergyNetwork(movingConduit).getId();
                }
                conduits.clear();
                networks.remove(this);
                return;
            }

            adjacentConsumers.addAll(getAdjacentConsumers(conduit.getPos(), conduit.getWorld()));

            List<BlockEntity> currentAdjacentProducers = getAdjacentProducers(conduit.getPos(), conduit.getWorld());
            adjacentProducers.addAll(currentAdjacentProducers);
            currentAdjacentProducers.forEach(producer ->
                    totalEnergyProduced.getAndAdd(((EnergyAttributeProvider) producer).getEnergyAttribute().getCurrentEnergy()));
        }

        if (totalEnergyProduced.get() == 0) return;
        if (totalEnergyProduced.get() % adjacentProducers.size() != 0) return;

        adjacentProducers.forEach(producer -> {
            EnergyAttribute from = ((EnergyAttributeProvider) producer).getEnergyAttribute();
            EnergyHelper.transfer(from, energy, from.getCurrentEnergy());
        });
        adjacentConsumers.forEach(consumer -> {
            EnergyAttribute to = ((EnergyAttributeProvider) consumer).getEnergyAttribute();
            EnergyHelper.transfer(energy, to, totalEnergyProduced.get() / adjacentProducers.size());
        });
    }

    public static EnergyConduitEntity[] getAdjacentConduits(World world, BlockPos pos) {
        final EnergyConduitEntity[] adjacentConnections = new EnergyConduitEntity[6];

        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(direction));
            if (blockEntity == null) continue;

            if (blockEntity instanceof EnergyConduitEntity) {
                adjacentConnections[direction.getId()] = (EnergyConduitEntity) blockEntity;
            }
        }
        return adjacentConnections;
    }

    public static List<BlockEntity> getAdjacentConsumers(BlockPos pos, World world) {
        final List<BlockEntity> adjacentConnections = new ArrayList<>();

        BlockState state = world.getBlockState(pos);
        if (state.get(EnergyConduitBlock.CONNECTED_NORTH) == EnergyConduitConnection.CONSUMER
                || state.get(EnergyConduitBlock.CONNECTED_NORTH) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.NORTH)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_EAST) == EnergyConduitConnection.CONSUMER
                || state.get(EnergyConduitBlock.CONNECTED_EAST) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.EAST)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_SOUTH) == EnergyConduitConnection.CONSUMER
                || state.get(EnergyConduitBlock.CONNECTED_SOUTH) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.SOUTH)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_WEST) == EnergyConduitConnection.CONSUMER
                || state.get(EnergyConduitBlock.CONNECTED_WEST) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.WEST)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_UP) == EnergyConduitConnection.CONSUMER
                || state.get(EnergyConduitBlock.CONNECTED_UP) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.UP)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_DOWN) == EnergyConduitConnection.CONSUMER
                || state.get(EnergyConduitBlock.CONNECTED_DOWN) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.DOWN)));
        }

        return adjacentConnections;
    }

    public static List<BlockEntity> getAdjacentProducers(BlockPos pos, World world) {
        final List<BlockEntity> adjacentConnections = new ArrayList<>();

        BlockState state = world.getBlockState(pos);
        if (state.get(EnergyConduitBlock.CONNECTED_NORTH) == EnergyConduitConnection.PRODUCER
                || state.get(EnergyConduitBlock.CONNECTED_NORTH) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.NORTH)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_EAST) == EnergyConduitConnection.PRODUCER
                || state.get(EnergyConduitBlock.CONNECTED_EAST) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.EAST)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_SOUTH) == EnergyConduitConnection.PRODUCER
                || state.get(EnergyConduitBlock.CONNECTED_SOUTH) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.SOUTH)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_WEST) == EnergyConduitConnection.PRODUCER
                || state.get(EnergyConduitBlock.CONNECTED_WEST) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.WEST)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_UP) == EnergyConduitConnection.PRODUCER
                || state.get(EnergyConduitBlock.CONNECTED_UP) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.UP)));
        }
        if (state.get(EnergyConduitBlock.CONNECTED_DOWN) == EnergyConduitConnection.PRODUCER
                || state.get(EnergyConduitBlock.CONNECTED_DOWN) == EnergyConduitConnection.ENERGY_BANK) {
            adjacentConnections.add(world.getBlockEntity(pos.offset(Direction.DOWN)));
        }

        return adjacentConnections;
    }

    public static EnergyNetwork getNetworkFromId(UUID id) {
        AtomicReference<EnergyNetwork> result = new AtomicReference<>();
        EnergyNetwork.networks.forEach(network -> {
            if (network.getId() == id) result.set(network);
        });
        return result.get();
    }

    // EnergyAttributeProvider
    @Override
    public EnergyAttribute getEnergyAttribute() {
        return energy;
    }
}
