package vivatech.api.multiblock.variant;

import net.minecraft.util.Identifier;

/**
 * Required for any component of a variable-size multiblock, including the controller. Goes on the block.
 */
public interface VarMBComponent {
	/**
	 * @return The ID of this multiblock for scanning.
	 */
	Identifier getMultiblockName();
}
