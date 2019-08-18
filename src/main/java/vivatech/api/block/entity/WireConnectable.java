package vivatech.api.block.entity;

import net.minecraft.util.math.BlockPos;
import vivatech.api.tier.Tiered;

import java.util.Collection;

public interface WireConnectable extends Tiered {

	/**
	 * @return The positions of all peers connected to this connectable.
	 */
	Collection<BlockPos> getPeers();

	/**
	 * Add a peer to this connectable.
	 * @param pos The position of the peer to add.
	 */
	void addPeer(BlockPos pos);

	/**
	 * Remove a peer from this connectable.
	 * @param pos The position of the peer to remove.
	 */
	void removePeer(BlockPos pos);

	/**
	 * Inform all peers to remove this position, and drop any wires between them.
	 */
	void dropPeers();

	/**
	 * @param pos The position of the peer this connector is attempting to connect to.
	 * @return Whether these peers can connect.
	 */
	boolean canConnectTo(BlockPos pos);
}
