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
import vivatech.api.tier.Tier;
import vivatech.api.tier.Tiered;

public abstract class AbstractTieredMachineBlock extends AbstractMachineBlock implements Tiered {
	protected final Tier tier;
	protected final Identifier id;

	public AbstractTieredMachineBlock(Settings settings, Identifier id, Tier tier) {
		super(settings);
		this.id = id;
		this.tier = tier;
	}

	@Override
	public String getTranslationKey() {
		return "block.vivatech." + id.getPath();
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void buildTooltip(ItemStack stack, BlockView view, List<Text> lines, TooltipContext context) {
		
		Text tierLine = new TranslatableText("info.vivatech.tier",
				new TranslatableText("info.vivatech.tier." + tier.toString().toLowerCase()));
		tierLine.formatted(Formatting.GRAY);
		lines.add(tierLine);
		
		super.buildTooltip(stack, view, lines, context);
	}

	// Tiered
	@Override
	public Tier getTier() {
		return tier;
	}

	@Override
	public Identifier getBaseId() {
		return null;
	}
}
