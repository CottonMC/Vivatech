package io.github.cottonmc.um.component;

import java.util.Set;

import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EnergySubnet {
	protected World world;
	protected EnergyType voltageTier;
	protected EnergyAttribute attribute;
	protected int withheld = 0;
	protected Set<BlockPos> activeNodes;
	protected Set<BlockPos> passiveNodes;
	
	public boolean definitelyOwns(World world, BlockPos pos) {
		if (this.world!=world) return false;
		if (this.activeNodes.contains(pos)) return true;
		if (this.passiveNodes.contains(pos)) return true;
		return false;
	}
	
	protected void addActiveNode(World world, BlockPos pos) {
		if (this.world!=world) return;
		activeNodes.add(pos);
	}
	
	protected void addPassiveNode(World world, BlockPos pos) {
		if (this.world!=world) return;
		passiveNodes.add(pos);
	}
	
	
}
