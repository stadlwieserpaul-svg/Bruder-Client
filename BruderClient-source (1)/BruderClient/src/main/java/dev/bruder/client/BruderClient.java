package dev.bruder.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class BruderClient implements ClientModInitializer {
    public static final String ID = "bruderclient";
    private static KeyBinding openGui;

    @Override
    public void onInitializeClient() {
        Config.load();

        KeyBinding.Category cat = KeyBinding.Category.create(Identifier.of(ID, "main"));
        openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + ID + ".gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, cat));
        for (Module m : Module.values()) {
            m.key = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key." + ID + "." + m.name().toLowerCase(), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, cat));
        }

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS,
                Identifier.of(ID, "hud"), Hud::render);
        if (Module.FREECAM.enabled || Module.FREE_LOOK.enabled) Module.FREE_LOOK.enabled = Module.FREECAM.enabled = false;
    }

    private void tick(MinecraftClient mc) {
        while (openGui.wasPressed()) {
            if (mc.currentScreen == null) mc.setScreen(new ClickGuiScreen());
        }
        for (Module m : Module.values()) {
            while (m.key.wasPressed()) m.toggle();
        }
        if (mc.player == null || mc.world == null) return;

        if (Module.FULLBRIGHT.enabled) {
            mc.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1000, 0, false, false, false));
        }
        CameraControl.tick(mc);
        Finder.tick(mc);
    }
}
