package vivatech.api.block;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;
import vivatech.api.util.MachineTier;

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
	public void buildTooltip(ItemStack itemStack_1, BlockView blockView_1, List<Text> list_1, TooltipContext tooltipContext_1) {
		
		Text tierLine = new TranslatableText("info.vivatech.tier",
				new TranslatableText("info.vivatech.tier." + tier.toString().toLowerCase()));
		tierLine.formatted(Formatting.GRAY);
		list_1.add(tierLine);
		
		super.buildTooltip(itemStack_1, blockView_1, list_1, tooltipContext_1);
	}

	public MachineTier getMachineTier() {
		return tier;
	}
}
