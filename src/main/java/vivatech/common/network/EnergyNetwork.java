package vivatech.common.network;

import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyAttributeProvider;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import vivatech.common.Vivatech;
import vivatech.common.block.entity.EnergyConduitBlockEntity;
import vivatech.util.EnergyHelper;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * This class is a modification of corresponding class from
 * <a href="https://github.com/StellarHorizons/Galacticraft-Rewoven">Galacticraft: Rewoven</a>
 */
public class EnergyNetwork {
    public final static ConcurrentLinkedQueue<EnergyNetwork> networks = new ConcurrentLinkedQueue<>();
    public final List<EnergyConduitBlockEntity> conduits = new ArrayList<>();
    public final UUID id;

    public EnergyNetwork(EnergyConduitBlockEntity sourceConduit) {
        networks.add(this);
        id = UUID.randomUUID();
        conduits.add(sourceConduit);
        networks.forEach(__ -> {
            List<BlockEntity> sourceConduits = new ArrayList<>();
            sourceConduits.add(sourceConduit);
            do {
                for (EnergyConduitBlockEntity conduit : getAdjacentConduits(sourceConduits.get(0).getWorld(), sourceConduits.get(0).getPos())) {
                    if (conduit != null
                            && conduit.getTier() == conduits.get(0).getTier()
                            && conduit.networkId != this.getId()) {
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
        Vivatech.LOGGER.debug("Number of energy networks: " + networks.size());
    }

    public UUID getId() {
        return id;
    }

    public void update() {
        Map<EnergyAttribute, Integer> consumers = new HashMap<>();
        Map<EnergyAttribute, Integer> producers = new HashMap<>();
        int totalEnergyNeeded = 0;
        int totalEnergyAvailable = 0;

        for (EnergyConduitBlockEntity conduit : conduits) {
            if (!(conduit.getWorld().getBlockEntity(conduit.getPos()) instanceof EnergyConduitBlockEntity)) {
                conduits.remove(conduit);
                Vivatech.LOGGER.debug("Removed conduit at " + conduit.getPos());
                for (EnergyConduitBlockEntity movingConduit : conduits) {
                    movingConduit.networkId = new EnergyNetwork(movingConduit).getId();
                }
                conduits.clear();
                networks.remove(this);
                return;
            }

            for (EnergyAttribute consumer : getAdjacentConsumers(conduit.getPos(), conduit.getWorld())) {
                int energyNeeded = consumer.getMaxEnergy() - consumer.getCurrentEnergy();
                if (energyNeeded == 0) continue;
                totalEnergyNeeded += energyNeeded;
                consumers.put(consumer, energyNeeded);
            }

            for (EnergyAttribute producer : getAdjacentProducers(conduit.getPos(), conduit.getWorld())) {
                int energyAvailable = producer.getCurrentEnergy();
                if (energyAvailable == 0) continue;
                totalEnergyAvailable += energyAvailable;
                producers.put(producer, energyAvailable);
            }

        }

        if (totalEnergyNeeded == 0
                || totalEnergyAvailable == 0
                || consumers.size() == 0
                || producers.size() == 0) {
            return;
        }

        consumers = consumers.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        producers = producers.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        SimpleEnergyAttribute networkEnergy = new SimpleEnergyAttribute(conduits.get(0).getTransferRate(), Vivatech.INFINITE_VOLTAGE);
        int totalEnergyTransferred = 0;
        for (Map.Entry<EnergyAttribute, Integer> producer : producers.entrySet()) {
            if (networkEnergy.getCurrentEnergy() == networkEnergy.getMaxEnergy()) {
                return;
            }
            totalEnergyTransferred += EnergyHelper.transfer(producer.getKey(), networkEnergy, producer.getValue(), Simulation.SIMULATE);
        }

        int energyNeed = totalEnergyTransferred / consumers.size();
        if (energyNeed == 0) return;
        for (Map.Entry<EnergyAttribute, Integer> producer : producers.entrySet()) {
            if (networkEnergy.getCurrentEnergy() == networkEnergy.getMaxEnergy()) {
                return;
            }
            EnergyHelper.transfer(producer.getKey(), networkEnergy, producer.getValue());
        }

        for (Map.Entry<EnergyAttribute, Integer> consumer : consumers.entrySet()) {
            if (networkEnergy.getCurrentEnergy() == 0) {
                return;
            }
            int energyTransferred = EnergyHelper.transfer(networkEnergy, consumer.getKey(), energyNeed);
            consumer.setValue(consumer.getValue() - energyTransferred);
        }

        consumers = consumers.entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        for (Map.Entry<EnergyAttribute, Integer> consumer : consumers.entrySet()) {
            if (networkEnergy.getCurrentEnergy() == 0) {
                return;
            }
            EnergyHelper.transfer(networkEnergy, consumer.getKey(), networkEnergy.getCurrentEnergy());
        }
    }

    public static EnergyConduitBlockEntity[] getAdjacentConduits(World world, BlockPos pos) {
        final EnergyConduitBlockEntity[] adjacentConnections = new EnergyConduitBlockEntity[6];

        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(direction));
            if (blockEntity == null) continue;

            if (blockEntity instanceof EnergyConduitBlockEntity) {
                adjacentConnections[direction.getId()] = (EnergyConduitBlockEntity) blockEntity;
            }
        }
        return adjacentConnections;
    }

    public static List<EnergyAttribute> getAdjacentConsumers(BlockPos pos, World world) {
        final List<EnergyAttribute> adjacentConnections = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(direction));
            if (blockEntity instanceof EnergyAttributeProvider) {
                EnergyAttribute attribute = ((EnergyAttributeProvider) blockEntity).getEnergyAttribute();
                if (attribute.canInsertEnergy()) {
                    adjacentConnections.add(attribute);
                }
            }
        }

        return adjacentConnections;
    }

    public static List<EnergyAttribute> getAdjacentProducers(BlockPos pos, World world) {
        final List<EnergyAttribute> adjacentConnections = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = world.getBlockEntity(pos.offset(direction));
            if (blockEntity instanceof EnergyAttributeProvider) {
                EnergyAttribute attribute = ((EnergyAttributeProvider) blockEntity).getEnergyAttribute();
                if (attribute.canExtractEnergy()) {
                    adjacentConnections.add(attribute);
                }
            }
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
}
