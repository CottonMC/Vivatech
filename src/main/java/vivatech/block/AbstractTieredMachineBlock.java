package vivatech.block;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormat;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;
import vivatech.util.MachineTier;

public abstract class AbstractTieredMachineBlock extends AbstractMachineBlock {
	
	protected final MachineTier tier;
	protected final String id;

	public AbstractTieredMachineBlock(Settings settings, String id, MachineTier tier) {
		super(settings);
		this.id = id;
		this.tier = tier;
	}
	
	public abstract Identifier getTieredID();

	@Override
	public String getTranslationKey() {
		return "block.vivatech."+id;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void buildTooltip(ItemStack itemStack_1, BlockView blockView_1, List<Component> list_1, TooltipContext tooltipContext_1) {
		
		Component tierLine = new TranslatableComponent("machine_tier.tier", new TranslatableComponent("machine_tier."+(tier.toString().toLowerCase())));
		tierLine.applyFormat(ChatFormat.GRAY);
		list_1.add(tierLine);
		
		super.buildTooltip(itemStack_1, blockView_1, list_1, tooltipContext_1);
	}
}
