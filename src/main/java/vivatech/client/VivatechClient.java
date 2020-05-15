package vivatech.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.container.BlockContext;
import net.minecraft.util.Identifier;
import vivatech.client.init.VivatechScreens;
import vivatech.client.screen.*;
import vivatech.common.Vivatech;
import vivatech.common.block.*;
import vivatech.common.init.VivatechBlocks;
import vivatech.common.menu.*;

@Environment(EnvType.CLIENT)
public class VivatechClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(
            RenderLayer.getCutout(),
            VivatechBlocks.ENERGY_CONDUIT.get(0),
            VivatechBlocks.ENERGY_CONDUIT.get(1),
            VivatechBlocks.ENERGY_CONDUIT.get(2)
        );

        VivatechScreens.initialize();
    }
}
