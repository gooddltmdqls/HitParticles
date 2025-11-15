package kr.icetang0123.mods.hit_particles.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigSerializer {
    public Map<String, Object> load() {
        try {
            File configFile = new File(Paths.get(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "config", "hit_particles.json").toUri());
            Gson gson = new Gson();

            if (!configFile.exists()) {
                if (configFile.createNewFile())
                    Files.writeString(configFile.toPath(), gson.toJson(getDefaultConfig()));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));
            StringBuilder file = new StringBuilder();

            String line = null;
            do {
                if (line == null) continue;
                file.append("\n");
                file.append(line);
            } while ((line = br.readLine()) != null);

            br.close();

            return mergeMap(gson.<Map<String, Object>>fromJson(file.toString(), Map.class), getDefaultConfig());
        } catch (IOException ignored) {}

        return getDefaultConfig();
    }

    public void save() {
        Map<String, Object> file = new HashMap<>();
        GsonBuilder gson = new GsonBuilder();

        file.put("particle_type", ConfigScreenFactory.particleType.get());
        file.put("particle_count", ConfigScreenFactory.particleCount.get());
        file.put("radius_x", ConfigScreenFactory.radiusX.get());
        file.put("radius_y", ConfigScreenFactory.radiusY.get());
        file.put("radius_z", ConfigScreenFactory.radiusZ.get());
        file.put("offset_x", ConfigScreenFactory.offsetX.get());
        file.put("offset_y", ConfigScreenFactory.offsetY.get());
        file.put("offset_z", ConfigScreenFactory.offsetZ.get());
        file.put("spawn_at_feet", ConfigScreenFactory.spawnAtFeet.get());
        file.put("velocity_min", ConfigScreenFactory.velocityMin.get());
        file.put("velocity_max", ConfigScreenFactory.velocityMax.get());

        try {
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(Paths.get(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "config", "hit_particles.json").toUri())), StandardCharsets.UTF_8));

            br.write(gson.setPrettyPrinting().create().toJson(file).toCharArray());

            br.close();
        } catch (IOException ignored) {}
    }

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> config = new HashMap<>(Map.of());

        config.put("particle_type", "minecraft:enchanted_hit");
        config.put("particle_count", 25);
        config.put("radius_x", 0.0);
        config.put("radius_y", 0.0);
        config.put("radius_z", 0.0);
        config.put("offset_x", 0.0);
        config.put("offset_y", 0.0);
        config.put("offset_z", 0.0);
        config.put("spawn_at_feet", false);
        config.put("velocity_min", 0.001);
        config.put("velocity_max", 1.0);

        return config;
    }

    private static <K, V> Map<K, V> mergeMap(Map<K, V> map, Map<K, V> defaultMap) {
        Map<K, V> merged = new HashMap<>(defaultMap);
        merged.putAll(map);
        return merged;
    }
}