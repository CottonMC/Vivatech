package vivatech.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.util.Identifier;
import vivatech.common.Vivatech;
import vivatech.common.block.*;
import vivatech.common.menu.*;
import vivatech.client.screen.*;

public class VivatechClient implements ClientModInitializer {
    public static final Identifier ARROW_DOWN = new Identifier(Vivatech.MODID, "textures/gui/arrow_down.png");
    public static final Identifier ARROW_UP = new Identifier(Vivatech.MODID, "textures/gui/arrow_up.png");
    public static final Identifier ENERGY_BAR_BG = new Identifier(Vivatech.MODID, "textures/gui/energy_bar_bg.png");
    public static final Identifier ENERGY_BAR = new Identifier(Vivatech.MODID, "textures/gui/energy_bar.png");
    public static final Identifier PROGRESS_BAR_BG = new Identifier(Vivatech.MODID, "textures/gui/progress_bar_bg.png");
    public static final Identifier PROGRESS_BAR = new Identifier(Vivatech.MODID, "textures/gui/progress_bar.png");
    public static final Identifier FIRE_BAR_BG = new Identifier(Vivatech.MODID, "textures/gui/fire_bar_bg.png");
    public static final Identifier FIRE_BAR = new Identifier(Vivatech.MODID, "textures/gui/fire_bar.png");

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(SterlingGeneratorBlock.ID, (syncId, identifier, player, buf) ->
                new CoalGeneratorScreen(new CoalGeneratorMenu(
                        syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(CrusherBlock.ID, (syncId, identifier, player, buf) ->
                new CrusherScreen(new CrusherMenu(
                        syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(ElectricFurnaceBlock.ID, (syncId, identifier, player, buf) ->
                new ElectricFurnaceScreen(new ElectricFurnaceMenu(
                        syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(EnergyBankBlock.ID, (syncId, identifier, player, buf) ->
                new EnergyBankScreen(new EnergyBankMenu(
                        syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
        ScreenProviderRegistry.INSTANCE.registerFactory(PressBlock.ID, (syncId, identifier, player, buf) ->
                new PressScreen(new PressMenu(
                        syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())), player));
    }
}
