package io.github.cottonmc.um.block;

import java.util.Random;

import alexiil.mc.lib.attributes.AttributeList;
import alexiil.mc.lib.attributes.AttributeProvider;
import io.github.cottonmc.gui.PropertyDelegateHolder;
import io.github.cottonmc.um.block.entity.CoalGeneratorEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class CoalGeneratorBlock extends AbstractMachineBlock implements BlockEntityProvider, InventoryProvider, AttributeProvider {
	public static final Identifier ID = new Identifier("united-manufacturing", "coal_generator");
	
	public CoalGeneratorBlock() {
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		//System.out.println("BlockEntity created.");
		return new CoalGeneratorEntity();
	}
	
	@Override
	public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.isClient) {
			//System.out.println("Client tick");
		} else {
			System.out.println("Server tick");
			
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof CoalGeneratorEntity) {
				((CoalGeneratorEntity)entity).pulse();
			} else {
				setStatus(world, pos, MachineStatus.ERROR);
			}
		}
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerHorizontalFacing().getOpposite());
	}

	@Override
	public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be!=null && be instanceof CoalGeneratorEntity) {
			return ((CoalGeneratorEntity)be).getInventory(state, world, pos);
		} else {
			return null;
		}
	}

	@Override
	public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
		if (world.isClient) return true;
		
		BlockEntity be = world.getBlockEntity(pos);
		if (be!=null && be instanceof CoalGeneratorEntity) {
			System.out.println("Opening container");
			
			ContainerProviderRegistry.INSTANCE.openContainer(ID, player, (buf)->{
				buf.writeBlockPos(pos);
			});
			//TODO: Maybe `player.increaseStat( ?? );`
		}
		
		return true;
	}

	@Override
	public void addAllAttributes(World world, BlockPos pos, BlockState state, AttributeList<?> to) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be!=null && be instanceof CoalGeneratorEntity) {
			to.offer(((CoalGeneratorEntity)be).getEnergy());
		}
	}
}
