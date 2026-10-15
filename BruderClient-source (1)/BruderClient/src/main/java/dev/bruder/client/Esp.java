package dev.bruder.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

/** ESP is implemented through the vanilla glow outline (visible through walls). */
public final class Esp {
    public static boolean shouldGlow(Entity e) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || e == mc.player) return false;
        if (e.squaredDistanceTo(mc.player) > (double) Config.data.espRange * Config.data.espRange) return false;
        if (Module.PLAYER_ESP.enabled && e instanceof PlayerEntity) return true;
        if (Module.ENTITY_ESP.enabled && e instanceof LivingEntity && !(e instanceof PlayerEntity)) return true;
        return Module.LOOK_ESP.enabled && e == mc.targetedEntity;
    }
}
