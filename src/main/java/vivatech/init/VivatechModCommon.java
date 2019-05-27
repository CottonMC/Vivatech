package vivatech.init;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.container.BlockContext;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.controller.CoalGeneratorController;
import vivatech.controller.ElectricFurnaceController;

public class VivatechModCommon implements ModInitializer {

    @Override
    public void onInitialize() {
        new VivatechBlocks().initialize();
        new VivatechEntities().initialize();
        new VivatechItems().initialize();

        ContainerProviderRegistry.INSTANCE.registerFactory(CoalGeneratorBlock.ID, (syncId, id, player, buf) ->
                new CoalGeneratorController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(ElectricFurnaceBlock.ID, (syncId, id, player, buf) ->
                new ElectricFurnaceController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
    }
}
