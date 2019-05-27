package vivatech.init;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import vivatech.block.CoalGeneratorBlock;
import vivatech.block.ElectricFurnaceBlock;
import vivatech.entity.CoalGeneratorEntity;
import vivatech.entity.ElectricFurnaceEntity;

public class VivatechEntities implements Initializable {
    public static final BlockEntityType<CoalGeneratorEntity> COAL_GENERATOR;
    public static final BlockEntityType<ElectricFurnaceEntity> ELECTRIC_FURNACE;

    static {
        COAL_GENERATOR = BlockEntityType.Builder.create(CoalGeneratorEntity::new, VivatechBlocks.COAL_GENERATOR).build(null);
        ELECTRIC_FURNACE = BlockEntityType.Builder.create(ElectricFurnaceEntity::new, VivatechBlocks.ELECTRIC_FURNACE).build(null);
    }

    @Override
    public void initialize() {
        Registry.register(Registry.BLOCK_ENTITY, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK_ENTITY, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
    }
}
