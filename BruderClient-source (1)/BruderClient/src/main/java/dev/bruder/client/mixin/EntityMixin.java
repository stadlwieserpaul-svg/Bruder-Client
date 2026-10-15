package dev.bruder.client.mixin;

import dev.bruder.client.CameraControl;
import dev.bruder.client.Esp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void bruder$glow(CallbackInfoReturnable<Boolean> cir) {
        if (Esp.shouldGlow((Entity) (Object) this)) cir.setReturnValue(true);
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void bruder$look(double dx, double dy, CallbackInfo ci) {
        if ((Object) this == MinecraftClient.getInstance().player && CameraControl.active()) {
            CameraControl.look(dx, dy);
            ci.cancel();
        }
    }
}
