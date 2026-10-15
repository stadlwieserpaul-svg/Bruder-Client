package dev.bruder.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {
    private static final int[] ESP_RANGES = {32, 64, 128, 256};
    private static final int[] RADII = {8, 16, 24, 32, 48};
    private static final double[] SPEEDS = {0.5, 1.0, 2.0, 4.0};

    public ClickGuiScreen() { super(Text.literal("BruderClient")); }

    private static Text label(Module m) {
        return Text.literal(m.label + ": " + (m.enabled ? "ON" : "OFF"));
    }

    @Override
    protected void init() {
        int w = 150, h = 20, gap = 4;
        int x0 = this.width / 2 - w - gap / 2;
        int x1 = this.width / 2 + gap / 2;
        int y0 = 40;
        Module[] mods = Module.values();
        for (int i = 0; i < mods.length; i++) {
            Module m = mods[i];
            int x = (i % 2 == 0) ? x0 : x1;
            int y = y0 + (i / 2) * (h + gap);
            this.addDrawableChild(ButtonWidget.builder(label(m), b -> {
                m.toggle();
                b.setMessage(label(m));
            }).dimensions(x, y, w, h).build());
        }
        int sy = y0 + ((mods.length + 1) / 2) * (h + gap) + 10;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("ESP range: " + Config.data.espRange), b -> {
            Config.data.espRange = next(ESP_RANGES, Config.data.espRange);
            Config.save();
            b.setMessage(Text.literal("ESP range: " + Config.data.espRange));
        }).dimensions(x0, sy, w, h).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Finder radius: " + Config.data.finderRadius), b -> {
            Config.data.finderRadius = next(RADII, Config.data.finderRadius);
            Config.save();
            b.setMessage(Text.literal("Finder radius: " + Config.data.finderRadius));
        }).dimensions(x1, sy, w, h).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Freecam speed: " + Config.data.freecamSpeed), b -> {
            double cur = Config.data.freecamSpeed;
            int idx = 0;
            for (int i = 0; i < SPEEDS.length; i++) if (SPEEDS[i] == cur) idx = i;
            Config.data.freecamSpeed = SPEEDS[(idx + 1) % SPEEDS.length];
            Config.save();
            b.setMessage(Text.literal("Freecam speed: " + Config.data.freecamSpeed));
        }).dimensions(x0, sy + h + gap, w, h).build());
    }

    private static int next(int[] arr, int cur) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == cur) return arr[(i + 1) % arr.length];
        return arr[0];
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(this.textRenderer, "BruderClient", this.width / 2, 15, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(this.textRenderer,
                "Keybinds: Options > Controls > Key Binds > BruderClient   |   Block list: config/bruderclient.json",
                this.width / 2, this.height - 20, 0xFFAAAAAA);
    }
}
