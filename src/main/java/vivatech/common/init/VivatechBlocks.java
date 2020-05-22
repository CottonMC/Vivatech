package vivatech.common.init;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.common.Vivatech;
import vivatech.common.block.EnergyBankBlock;
import vivatech.common.block.EnergyConnectorBlock;
import vivatech.common.block.SimpleProcessingMachineBlock;
import vivatech.common.block.SterlingGeneratorBlock;
import vivatech.common.block.entity.SimpleProcessingMachineBlockEntity;
import vivatech.data.BlockData;
import vivatech.util.TierHelper;

public class VivatechBlocks {
    public static final Identifier MINIMAL_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MOD_ID, "minimal_machine_chassis");
    public static final Identifier NORMAL_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MOD_ID, "normal_machine_chassis");
    public static final Identifier ADVANCED_MACHINE_CHASSIS_ID = new Identifier(Vivatech.MOD_ID, "advanced_machine_chassis");

    public static final Block MINIMAL_MACHINE_CHASSIS;
    public static final Block NORMAL_MACHINE_CHASSIS;
    public static final Block ADVANCED_MACHINE_CHASSIS;
    public static final SterlingGeneratorBlock STERLING_GENERATOR;
    public static final EnergyBankBlock ENERGY_BANK;

    public static final ImmutableList<SimpleProcessingMachineBlock> CRUSHER;
    public static final ImmutableList<SimpleProcessingMachineBlock> ELECTRIC_FURNACE;
    public static final ImmutableList<SimpleProcessingMachineBlock> PRESS;
    public static final ImmutableList<EnergyConnectorBlock> CONNECTOR;

    static {
        MINIMAL_MACHINE_CHASSIS = new Block(FabricBlockSettings.copyOf(Blocks.STONE));
        NORMAL_MACHINE_CHASSIS = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK));
        ADVANCED_MACHINE_CHASSIS = new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).hardness(7f).resistance(10f));
        STERLING_GENERATOR = new SterlingGeneratorBlock();
        ENERGY_BANK = new EnergyBankBlock();
        CONNECTOR = TierHelper.fillTieredBlockArray(EnergyConnectorBlock::new);

        CRUSHER = TierHelper.fillTieredBlockArray(tier -> new SimpleProcessingMachineBlock(
            BlockData.Ids.CRUSHER, tier, TierHelper.getTieredID(BlockData.Ids.CRUSHER, tier),
            world -> new SimpleProcessingMachineBlockEntity(
                VivatechBlockEntities.CRUSHER,
                VivatechRecipes.CRUSHING
            )
        ));
        ELECTRIC_FURNACE = TierHelper.fillTieredBlockArray(tier -> new SimpleProcessingMachineBlock(
            BlockData.Ids.ELECTRIC_FURNACE, tier, TierHelper.getTieredID(BlockData.Ids.ELECTRIC_FURNACE, tier),
            world -> new SimpleProcessingMachineBlockEntity(
                VivatechBlockEntities.ELECTRIC_FURNACE,
                VivatechRecipes.SMELTING_RECIPE_PROXY
            )
        ));
        PRESS = TierHelper.fillTieredBlockArray(tier -> new SimpleProcessingMachineBlock(
            BlockData.Ids.PRESS, tier, TierHelper.getTieredID(BlockData.Ids.PRESS, tier),
            world -> new SimpleProcessingMachineBlockEntity(
                VivatechBlockEntities.PRESS,
                VivatechRecipes.PRESSING
            )
        ));
    }

    public static void initialize() {
        Registry.register(Registry.BLOCK, MINIMAL_MACHINE_CHASSIS_ID, MINIMAL_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, NORMAL_MACHINE_CHASSIS_ID, NORMAL_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, ADVANCED_MACHINE_CHASSIS_ID, ADVANCED_MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, SterlingGeneratorBlock.ID, STERLING_GENERATOR);
        Registry.register(Registry.BLOCK, EnergyBankBlock.ID, ENERGY_BANK);
        TierHelper.registerTieredBlocks(CRUSHER);
        TierHelper.registerTieredBlocks(ELECTRIC_FURNACE);
        TierHelper.registerTieredBlocks(PRESS);
        TierHelper.registerTieredBlocks(CONNECTOR);
    }
}
