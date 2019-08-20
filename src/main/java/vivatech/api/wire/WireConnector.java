package vivatech.api.wire;

import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Map;

/**
 * Interface for connecting wires between valid connectors. Goes on the block entity.
 */
public interface WireConnector {

	/**
	 * @return A map of all the peers of all types connected to this connector.
	 */
	Map<WireType, Collection<BlockPos>> getAllPeers();

	/**
	 * @param type The type of wire to get peers for.
	 * @return The positions of all peers of this type connected to this connector.
	 */
	Collection<BlockPos> getPeers(WireType type);

	/**
	 * Add a peer to this connector.
	 * @param pos The position of the peer to add.
	 * @param type The type of wire to add as a peer.
	 * @return Whether the wire was successfully added.
	 */
	boolean addPeer(BlockPos pos, WireType type);

	/**
	 * Remove a peer from this connector.
	 * @param pos The position of the peer to remove.
	 * @param type The type of wire to remove as a peer.
	 * @return Whether the wire was successfully removed.
	 */
	boolean removePeer(BlockPos pos, WireType type);

	/**
	 * Inform all peers to remove this position, and drop all wires between them.
	 * This block should be the only one dropping the wires.
	 */
	void dropAllPeers();

	/**
	 * Inform peers to remove this position with this wire type, and drop wires of this type between them.
	 * This block should be the only one dropping the wires.
	 * @param type The type to drop.
	 */
	void dropPeers(WireType type);

	/**
	 * @param type The type of wire the player is trying to put on this connector.
	 * @return Whether this type of wire can be put on this connector.
	 */
	boolean canAcceptWire(WireType type);

	/**
	 * @param pos The position of the peer this connector is attempting to connect to.
	 * @param type The type of the wire that is trying to connect
	 * @return Whether these peers can connect.
	 */
	boolean canConnectTo(BlockPos pos, WireType type);
}
