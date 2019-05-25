package io.github.cottonmc.um.block.entity;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.Simulation;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyType;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import io.github.cottonmc.um.block.UMBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class CableEntity extends BlockEntity implements Tickable {
	public static double MAX_INDUCTION = 10;
	public static int MAX_WU    = 8_000;
	
	protected Vec3d induction = Vec3d.ZERO;
	
	public SimpleEnergyAttribute energy = new SimpleEnergyAttribute(MAX_WU);
	
	public CableEntity() {
		super(UMBlocks.CABLE_ENTITY);
	}

	


	@Override
	public CompoundTag toTag(CompoundTag compound) {
		CompoundTag tag = super.toTag(compound);
		tag.put("Energy", energy.toTag());
		return tag;
	}
	
	@Override
	public void fromTag(CompoundTag compound) {
		super.toTag(compound);
		energy.fromTag(compound.getTag("Energy"));
	}
	
	@Override
	public void tick() {
		if (energy.getCurrentEnergy()<=0) {
			decayInduction();
		} else {
			BlockPos pos = new BlockPos(induction);
			Direction flow = Direction.fromVector(pos.getX(), pos.getY(), pos.getZ());
			
			pushEnergyTo(flow);
			
			if (energy.getCurrentEnergy()>0) {
				for(Direction facing : Direction.values()) {
					if (facing==flow || facing==flow.getOpposite()) continue;
					pushEnergyTo(facing);
					if (energy.getCurrentEnergy()<=0) break; 
				}
			
				if (energy.getCurrentEnergy()>0) {
					pushEnergyTo(flow.getOpposite());
					
					if (energy.getCurrentEnergy()>0) {
						decayInduction();
					}
				}
			}
		}
	}
	
	public void decayInduction() {
		if (induction!=Vec3d.ZERO) {
			induction = induction.multiply(0.8);
			if (induction.lengthSquared()<=1) induction = Vec3d.ZERO;
			markDirty();
		}
	}
	
	/**
	 * @param facing which direction to push current in
	 * @return how much energy was successfully pushed
	 */
	private int pushEnergyTo(Direction facing) {
		if (energy.getCurrentEnergy()==0) return 0;
		
		AttributeList<EnergyAttribute> attributes = EnergyAttribute.ENERGY_ATTRIBUTE.getAll(world, pos.offset(facing));
		for(int i=0; i<attributes.getCount(); i++) {
			EnergyAttribute attribute = attributes.get(i);
			if (!attribute.canInsertEnergy()) continue;
			if ( energy.getPreferredType().isCompatibleWith(attribute.getPreferredType()) || attribute.getPreferredType().isCompatibleWith(energy.getPreferredType()) ) {
				//One end or the other seems to think that the energy is compatible.
				int transferSize = Math.min(energy.getPreferredType().getMaximumTransferSize(), energy.getCurrentEnergy());
				
				transferSize = transferSize - attribute.insertEnergy(energy.getPreferredType(), transferSize, Simulation.SIMULATE);
				transferSize = energy.extractEnergy(energy.getPreferredType(), transferSize, Simulation.ACTION); //Transfer actually starts here
				int notTransferred = attribute.insertEnergy(energy.getPreferredType(), transferSize, Simulation.ACTION);
				if (notTransferred>0) {
					//Complain loudly but keep working
					new RuntimeException("Misbehaving EnergyAttribute "+attribute.getClass().getCanonicalName()+" accepted energy in SIMULATE then didn't accept the same or less energy in PERFORM").printStackTrace();
					energy.insertEnergy(energy.getPreferredType(), notTransferred, Simulation.ACTION);
					
					induction = induction.add(new Vec3d(facing.getOffsetX(), facing.getOffsetY(), facing.getOffsetZ()));
					if (induction.length() > MAX_INDUCTION) {
						induction = induction.normalize().multiply(MAX_INDUCTION);
					}
				}
				return transferSize - notTransferred;
			}
			
		}
		return 0;
	}
	
	public class CableEnergyHandler implements EnergyAttribute {
		private Direction incomingFlow = Direction.NORTH;
		
		@Override
		public int getMaxEnergy() {
			return CableEntity.this.energy.getMaxEnergy();
		}
		
		@Override
		public int getCurrentEnergy() {
			return CableEntity.this.energy.getCurrentEnergy();
		}
		
		@Override
		public boolean canInsertEnergy() {
			return true;
		}
		
		@Override
		public int insertEnergy(EnergyType type, int amount, Simulation simulation) {
			if (simulation == Simulation.ACTION) {
				induction = induction.add(new Vec3d(incomingFlow.getOffsetX(), incomingFlow.getOffsetY(), incomingFlow.getOffsetZ()));
				if (induction.length() > MAX_INDUCTION) {
					induction = induction.normalize().multiply(MAX_INDUCTION);
				}
				
			}
			return CableEntity.this.energy.insertEnergy(type, amount, simulation);
		}
		
		@Override
		public boolean canExtractEnergy() {
			return true;
		}
		
		@Override
		public int extractEnergy(EnergyType type, int amount, Simulation simulation) {
			if (simulation == Simulation.ACTION) {
				Direction outgoingFlow = incomingFlow.getOpposite();
				induction = induction.add(new Vec3d(outgoingFlow.getOffsetX(), outgoingFlow.getOffsetY(), outgoingFlow.getOffsetZ()));
				if (induction.length() > MAX_INDUCTION) {
					induction = induction.normalize().multiply(MAX_INDUCTION);
				}
				
			}
			return CableEntity.this.energy.extractEnergy(type, amount, simulation);
		}
		
		@Override
		public EnergyType getPreferredType() {
			return energy.getPreferredType();
		}
		
	}
}