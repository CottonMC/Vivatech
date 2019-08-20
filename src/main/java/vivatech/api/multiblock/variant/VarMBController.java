package vivatech.api.multiblock.variant;

/**
 * Required for the controller of a variable-size multiblock. Goes on the BE.
 */
public interface VarMBController  extends VarMBComponent {
	/**
	 * Called to mark this multiblock as needing a rescan in the next tick.
	 */
	void requestRescan();
}
