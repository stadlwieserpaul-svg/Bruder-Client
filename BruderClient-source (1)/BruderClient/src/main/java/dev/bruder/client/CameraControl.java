package dev.bruder.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/** Shared state for Free Look (rotation only) and Freecam (rotation + position). */
public final class CameraControl {
    public static float yaw, pitch;
    public static Vec3d pos = Vec3d.ZERO;

    public static boolean active() { return Module.FREECAM.enabled || Module.FREE_LOOK.enabled; }

    public static void capture() {
        var p = MinecraftClient.getInstance().player;
        if (p == null) return;
        yaw = p.getYaw();
        pitch = p.getPitch();
        pos = p.getEyePos();
    }

    public static void look(double dx, double dy) {
        yaw += (float) dx * 0.15F;
        pitch = MathHelper.clamp(pitch + (float) dy * 0.15F, -90F, 90F);
    }

    public static void tick(MinecraftClient mc) {
        if (!Module.FREECAM.enabled || mc.player == null) return;
        var o = mc.options;
        Vec3d fwd = Vec3d.fromPolar(pitch, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90F);
        Vec3d move = Vec3d.ZERO;
        if (o.forwardKey.isPressed()) move = move.add(fwd);
        if (o.backKey.isPressed()) move = move.subtract(fwd);
        if (o.rightKey.isPressed()) move = move.add(right);
        if (o.leftKey.isPressed()) move = move.subtract(right);
        if (o.jumpKey.isPressed()) move = move.add(0, 1, 0);
        if (o.sneakKey.isPressed()) move = move.subtract(0, 1, 0);
        double speed = 0.6 * Config.data.freecamSpeed * (o.sprintKey.isPressed() ? 2.0 : 1.0);
        if (move.lengthSquared() > 0) pos = pos.add(move.normalize().multiply(speed));
    }
}
