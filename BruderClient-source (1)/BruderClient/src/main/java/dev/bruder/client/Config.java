package dev.bruder.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Config {
    public static class Data {
        public Map<String, Boolean> enabled = new HashMap<>();
        public int espRange = 64;
        public int finderRadius = 24;
        public double freecamSpeed = 1.0;
        public List<String> blocks = new ArrayList<>(List.of(
                "minecraft:diamond_ore", "minecraft:deepslate_diamond_ore", "minecraft:ancient_debris"));
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("bruderclient.json");
    public static Data data = new Data();

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                Data d = GSON.fromJson(Files.readString(FILE), Data.class);
                if (d != null) data = d;
            }
        } catch (Exception e) {
            System.err.println("[BruderClient] Could not read config: " + e);
        }
        for (Module m : Module.values()) m.enabled = data.enabled.getOrDefault(m.name(), false);
    }

    public static void save() {
        try {
            for (Module m : Module.values()) data.enabled.put(m.name(), m.enabled);
            Files.writeString(FILE, GSON.toJson(data));
        } catch (Exception e) {
            System.err.println("[BruderClient] Could not save config: " + e);
        }
    }
}
