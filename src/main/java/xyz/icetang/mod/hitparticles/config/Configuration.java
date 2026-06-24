package xyz.icetang.mod.hitparticles.config;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Configuration {
    public static ConfigSerializer serializer = new ConfigSerializer();

    public static AtomicReference<String> particleType;
    public static AtomicReference<Integer> particleCount;
    public static AtomicReference<Double> radiusX;
    public static AtomicReference<Double> radiusY;
    public static AtomicReference<Double> radiusZ;
    public static AtomicReference<Double> offsetX;
    public static AtomicReference<Double> offsetY;
    public static AtomicReference<Double> offsetZ;
    public static AtomicReference<Boolean> spawnAtFeet;
    public static AtomicReference<Double> velocityMin;
    public static AtomicReference<Double> velocityMax;

    public static void loadConfig() {
        Map<String, Object> config = serializer.load();

        particleType = new AtomicReference<>((String) config.get("particle_type"));
        particleCount = new AtomicReference<>(toInt(config.get("particle_count")));
        radiusX = new AtomicReference<>(toDouble(config.get("radius_x")));
        radiusY = new AtomicReference<>(toDouble(config.get("radius_y")));
        radiusZ = new AtomicReference<>(toDouble(config.get("radius_z")));
        offsetX = new AtomicReference<>(toDouble(config.get("offset_x")));
        offsetY = new AtomicReference<>(toDouble(config.get("offset_y")));
        offsetZ = new AtomicReference<>(toDouble(config.get("offset_z")));
        spawnAtFeet = new AtomicReference<>((Boolean) config.get("spawn_at_feet"));
        velocityMin = new AtomicReference<>(toDouble(config.get("velocity_min")));
        velocityMax = new AtomicReference<>(toDouble(config.get("velocity_max")));
    }

    public static int toInt(Object unknown) {
        if (unknown instanceof Double) return ((Double) unknown).intValue();
        if (unknown instanceof Float) return ((Float) unknown).intValue();
        else return (Integer) unknown;
    }

    public static double toDouble(Object unknown) {
        if (unknown instanceof Integer) return ((Integer) unknown).doubleValue();
        if (unknown instanceof Float) return ((Float) unknown).doubleValue();
        else return (Double) unknown;
    }
}
