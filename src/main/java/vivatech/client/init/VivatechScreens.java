package vivatech.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.BlockContext;
import vivatech.client.screen.*;
import vivatech.common.block.*;
import vivatech.common.menu.*;
import vivatech.data.BlockData;

@Environment(EnvType.CLIENT)
public class VivatechScreens {
    public static void initialize() {
        ScreenProviderRegistry.INSTANCE.registerFactory(SterlingGeneratorBlock.ID, (syncId, identifier, player, buf) ->
            new CoalGeneratorScreen(new CoalGeneratorMenu(
                syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockData.Ids.CRUSHER, (syncId, identifier, player, buf) ->
            new CrusherScreen(new CrusherMenu(
                syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockData.Ids.ELECTRIC_FURNACE, (syncId, identifier, player, buf) ->
            new ElectricFurnaceScreen(new ElectricFurnaceMenu(
                syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(EnergyBankBlock.ID, (syncId, identifier, player, buf) ->
            new EnergyBankScreen(new EnergyBankMenu(
                syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(BlockData.Ids.PRESS, (syncId, identifier, player, buf) ->
            new PressScreen(new PressMenu(
                syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }
}
