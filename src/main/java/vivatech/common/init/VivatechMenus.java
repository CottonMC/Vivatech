package vivatech.common.init;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.container.BlockContext;
import vivatech.common.block.*;
import vivatech.common.menu.*;

public class VivatechMenus {
    public static void initialize() {
        ContainerProviderRegistry.INSTANCE.registerFactory(SterlingGeneratorBlock.ID, (syncId, id, player, buf) ->
            new CoalGeneratorMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(CrusherBlock.ID, (syncId, id, player, buf) ->
            new CrusherMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(ElectricFurnaceBlock.ID, (syncId, id, player, buf) ->
            new ElectricFurnaceMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(EnergyBankBlock.ID, (syncId, id, player, buf) ->
            new EnergyBankMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
        ContainerProviderRegistry.INSTANCE.registerFactory(PressBlock.ID, (syncId, id, player, buf) ->
            new PressMenu(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));
    }
}
