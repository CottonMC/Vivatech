package vivatech.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vivatech.common.network.EnergyNetwork;

import java.util.function.BooleanSupplier;

/**
 * This class is a modification of corresponding class from
 * <a href="https://github.com/StellarHorizons/Galacticraft-Rewoven">Galacticraft: Rewoven</a>
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(BooleanSupplier supplier, CallbackInfo ci) {
        EnergyNetwork.networks.forEach(EnergyNetwork::update);
    }

    public void close() {
        EnergyNetwork.networks.clear();
    }
}
