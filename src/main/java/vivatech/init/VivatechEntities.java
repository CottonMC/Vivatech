package vivatech.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.CrusherBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.block.PressBlock;
import vivatech.entity.CoalGeneratorEntity;
import vivatech.entity.CrusherEntity;
import vivatech.entity.ElectricFurnaceEntity;
import vivatech.entity.PressEntity;

public class VivatechEntities implements Initializable {
    public static final BlockEntityType<CoalGeneratorEntity> COAL_GENERATOR;
    public static final BlockEntityType<CrusherEntity> CRUSHER;
    public static final BlockEntityType<ElectricFurnaceEntity> ELECTRIC_FURNACE;
    public static final BlockEntityType<PressEntity> PRESS;

    static {
        COAL_GENERATOR = BlockEntityType.Builder.create(CoalGeneratorEntity::new, VivatechBlocks.COAL_GENERATOR).build(null);
        CRUSHER = BlockEntityType.Builder.create(CrusherEntity::new, VivatechBlocks.CRUSHER).build(null);
        ELECTRIC_FURNACE = BlockEntityType.Builder.create(ElectricFurnaceEntity::new, VivatechBlocks.ELECTRIC_FURNACE).build(null);
        PRESS = BlockEntityType.Builder.create(PressEntity::new, VivatechBlocks.PRESS).build(null);
    }

    @Override
    public void initialize() {
        Registry.register(Registry.BLOCK_ENTITY, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK_ENTITY, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK_ENTITY, PressBlock.ID, PRESS);
    }
}
