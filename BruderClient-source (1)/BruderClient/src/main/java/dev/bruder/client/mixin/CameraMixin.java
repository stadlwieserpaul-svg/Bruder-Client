package dev.bruder.client.mixin;

import dev.bruder.client.CameraControl;
import dev.bruder.client.Module;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setRotation(float yaw, float pitch);
    @Shadow protected abstract void setPos(Vec3d pos);

    @Inject(method = "update", at = @At("TAIL"))
    private void bruder$update(CallbackInfo ci) {
        if (Module.FREECAM.enabled) {
            setPos(CameraControl.pos);
            setRotation(CameraControl.yaw, CameraControl.pitch);
        } else if (Module.FREE_LOOK.enabled) {
            setRotation(CameraControl.yaw, CameraControl.pitch);
        }
    }
}
