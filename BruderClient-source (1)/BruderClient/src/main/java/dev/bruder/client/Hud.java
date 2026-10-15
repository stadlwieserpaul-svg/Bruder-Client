package dev.bruder.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import java.util.ArrayList;
import java.util.List;

public final class Hud {
    public static void render(DrawContext ctx, RenderTickCounter tc) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;
        List<String> lines = new ArrayList<>();
        if (Module.FPS.enabled) lines.add("FPS: " + mc.getCurrentFps());
        if (Module.COORDS.enabled) {
            var p = Module.FREECAM.enabled ? CameraControl.pos : mc.player.getPos();
            lines.add(String.format("XYZ: %.1f / %.1f / %.1f%s", p.x, p.y, p.z, Module.FREECAM.enabled ? " (cam)" : ""));
        }
        if (Module.BLOCK_FINDER.enabled) {
            lines.add("Blocks:");
            for (var h : Finder.blockHits)
                lines.add(String.format("  %d %d %d  (%.0fm)", h.pos().getX(), h.pos().getY(), h.pos().getZ(), h.dist()));
        }
        if (Module.CHUNK_FINDER.enabled) {
            lines.add("Chunks:");
            for (var h : Finder.chunkHits)
                lines.add(String.format("  %d, %d  x%d", h.chunk().x, h.chunk().z, h.count()));
        }
        int y = 4;
        for (String s : lines) {
            ctx.drawTextWithShadow(mc.textRenderer, s, 4, y, 0xFFFFFFFF);
            y += 10;
        }
    }
}
