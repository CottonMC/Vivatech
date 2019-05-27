package vivatech.init;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.BlockContext;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.controller.CoalGeneratorController;
import vivatech.controller.ElectricFurnaceController;
import vivatech.screen.CoalGeneratorScreen;
import vivatech.screen.ElectricFurnaceScreen;

public class VivatechModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(CoalGeneratorBlock.ID, (syncId, identifier, player, buf) ->
                new CoalGeneratorScreen(new CoalGeneratorController(
                        syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(ElectricFurnaceBlock.ID, (syncId, identifier, player, buf) ->
                new ElectricFurnaceScreen(new ElectricFurnaceController(
                        syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }
}
