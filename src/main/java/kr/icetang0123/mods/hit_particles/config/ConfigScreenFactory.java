package kr.icetang0123.mods.hit_particles.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ConfigScreenFactory {
    public static ConfigSerializer serializer = new ConfigSerializer();
    public static AtomicReference<Integer> particleCount;
    public static AtomicReference<Double> velocityMin;
    public static AtomicReference<Double> velocityMax;

    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Config"));

        ConfigCategory configCategory = builder.getOrCreateCategory(Text.of("HitParticles config"));

        Map<String, Object> config = serializer.load();

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        particleCount = new AtomicReference<>(toInt(config.get("particle_count")));
        velocityMin = new AtomicReference<>(toDouble(config.get("velocity_min")));
        velocityMax = new AtomicReference<>(toDouble(config.get("velocity_max")));

        configCategory.addEntry(
                entryBuilder.startIntField(Text.of("Particle count"), particleCount.get())
                        .setDefaultValue(25)
                        .setTooltip(Text.of("Particle count"))
                        .setSaveConsumer(particleCount::set)
                        .build()
        );

        configCategory.addEntry(
                entryBuilder.startDoubleField(Text.of("Velocity min value"), velocityMin.get())
                        .setDefaultValue(0.001)
                        .setTooltip(Text.of("Velocity min value"))
                        .setSaveConsumer(velocityMin::set)
                        .build()
        );

        configCategory.addEntry(
                entryBuilder.startDoubleField(Text.of("Velocity max value"), velocityMax.get())
                        .setDefaultValue(1.0)
                        .setTooltip(Text.of("Velocity max value"))
                        .setSaveConsumer(velocityMax::set)
                        .build()
        );

        builder.setSavingRunnable(() -> serializer.save());

        return builder.build();
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