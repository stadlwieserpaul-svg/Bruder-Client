package dev.bruder.client.mixin;

import dev.bruder.client.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    /** While Freecam is on, the real player stays put. */
    @Inject(method = "tickMovement", at = @At("HEAD"), cancellable = true)
    private void bruder$freeze(CallbackInfo ci) {
        if (Module.FREECAM.enabled) ci.cancel();
    }
}
