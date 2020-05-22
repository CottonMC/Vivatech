package vivatech.data;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

public class BlockData {
    public static final Block.Settings MACHINE_BLOCK_SETTINGS = FabricBlockSettings.copyOf(Blocks.IRON_BLOCK);

    public static class Ids {
        public static final Identifier CRUSHER = id("crusher");
        public static final Identifier ELECTRIC_FURNACE = id("electric_furnace");
        public static final Identifier PRESS = id("press");

        private static Identifier id(String id) {
            return new Identifier(Meta.MOD_ID, id);
        }
    }
}
