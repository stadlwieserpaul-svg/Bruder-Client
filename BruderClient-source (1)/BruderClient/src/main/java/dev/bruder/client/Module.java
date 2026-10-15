package dev.bruder.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.effect.StatusEffects;

public enum Module {
    FULLBRIGHT("Fullbright"),
    PLAYER_ESP("Player ESP"),
    ENTITY_ESP("Entity ESP"),
    LOOK_ESP("Look ESP"),
    BLOCK_FINDER("Block Finder"),
    CHUNK_FINDER("Chunk Finder"),
    FREE_LOOK("Free Look"),
    FREECAM("Freecam"),
    COORDS("XYZ"),
    FPS("FPS");

    public final String label;
    public boolean enabled;
    public KeyBinding key;

    Module(String label) { this.label = label; }

    public void toggle() { set(!enabled); }

    public void set(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if ((this == FREECAM || this == FREE_LOOK) && value) CameraControl.capture();
        if (this == FULLBRIGHT && !value) {
            var p = MinecraftClient.getInstance().player;
            if (p != null) p.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
        Config.save();
    }
}
