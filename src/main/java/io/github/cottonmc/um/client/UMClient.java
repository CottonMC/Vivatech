package io.github.cottonmc.um.client;

import io.github.cottonmc.um.block.CoalGeneratorBlock;
import io.github.cottonmc.um.block.container.CoalGeneratorController;
import io.github.cottonmc.um.block.entity.ConveyorEntity;
import io.github.cottonmc.um.client.gui.CoalGeneratorScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.BlockContext;

public class UMClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenProviderRegistry.INSTANCE.registerFactory(CoalGeneratorBlock.ID, (syncId, identifier, player, buf) -> new CoalGeneratorScreen(new CoalGeneratorController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
		
		BlockEntityRendererRegistry.INSTANCE.register(ConveyorEntity.class, new ConveyorRenderer());
	}
}
