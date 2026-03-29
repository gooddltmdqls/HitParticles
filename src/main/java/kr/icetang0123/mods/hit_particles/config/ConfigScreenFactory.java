package kr.icetang0123.mods.hit_particles.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.network.chat.Component;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ConfigScreenFactory {
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

    public static Screen getConfigScreen(Screen parent) {
        loadConfig();

        HolderLookup.Provider registries = VanillaRegistries.createLookup();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.nullToEmpty("Config"));

        ConfigCategory configCategory = builder.getOrCreateCategory(Component.nullToEmpty("HitParticles config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        configCategory.addEntry(
                entryBuilder.startStrField(Component.nullToEmpty("Particle type"), particleType.get())
                        .setDefaultValue("minecraft:enchanted_hit")
                        .setTooltip(Component.nullToEmpty("This is the particle that appears when you hit an entity. When using particles that require additional parameters (i.e., non-simple particles), you must provide the necessary data. For example: minecraft:block{block_state:\"minecraft:stone\"}."))
                        .setErrorSupplier((s) -> {
                            StringReader reader = new StringReader(s);

                            try {
                                ParticleArgument.readParticle(reader, registries);
                            } catch (CommandSyntaxException e) {
                                return Optional.of(Component.nullToEmpty(e.getMessage()));
                            }

                            return Optional.empty();
                        })
                        .setSaveConsumer(particleType::set)
                        .build()
        );

        configCategory.addEntry(
                entryBuilder.startIntField(Component.nullToEmpty("Particle count"), particleCount.get())
                        .setDefaultValue(25)
                        .setTooltip(Component.nullToEmpty("Particle count"))
                        .setSaveConsumer(particleCount::set)
                        .build()
        );

        DoubleListEntry radiusXEntry = entryBuilder.startDoubleField(Component.nullToEmpty("X"), radiusX.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.nullToEmpty("The X-axis radius within which the particle spawns."))
                .setSaveConsumer(radiusX::set)
                .build();

        DoubleListEntry radiusYEntry = entryBuilder.startDoubleField(Component.nullToEmpty("Y"), radiusY.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.nullToEmpty("The Y-axis radius within which the particle spawns."))
                .setSaveConsumer(radiusY::set)
                .build();

        DoubleListEntry radiusZEntry = entryBuilder.startDoubleField(Component.nullToEmpty("Z"), radiusZ.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.nullToEmpty("The Z-axis radius within which the particle spawns."))
                .setSaveConsumer(radiusZ::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Component.nullToEmpty("Radius"), List.of(radiusXEntry, radiusYEntry, radiusZEntry)).build()
        );

        DoubleListEntry offsetXEntry = entryBuilder.startDoubleField(Component.nullToEmpty("X"), offsetX.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.nullToEmpty("The X-axis offset applied to the particle position."))
                .setSaveConsumer(offsetX::set)
                .build();

        DoubleListEntry offsetYEntry = entryBuilder.startDoubleField(Component.nullToEmpty("Y"), offsetY.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.nullToEmpty("The Y-axis offset applied to the particle position."))
                .setSaveConsumer(offsetY::set)
                .build();

        DoubleListEntry offsetZEntry = entryBuilder.startDoubleField(Component.nullToEmpty("Z"), offsetZ.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.nullToEmpty("The Z-axis offset applied to the particle position."))
                .setSaveConsumer(offsetZ::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Component.nullToEmpty("Offset"), List.of(offsetXEntry, offsetYEntry, offsetZEntry)).build()
        );

        DoubleListEntry velocityMinEntry = entryBuilder.startDoubleField(Component.nullToEmpty("Minimum value"), velocityMin.get())
                .setMin(0.0)
                .setDefaultValue(0.001)
                .setTooltip(Component.nullToEmpty("The minimum value of random velocity."))
                .setSaveConsumer(velocityMin::set)
                .build();

        DoubleListEntry velocityMaxEntry = entryBuilder.startDoubleField(Component.nullToEmpty("Maximum value"), velocityMax.get())
                .setMin(0.0)
                .setDefaultValue(1.0)
                .setTooltip(Component.nullToEmpty("The maximum value of random velocity."))
                .setSaveConsumer(velocityMax::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Component.nullToEmpty("Velocity"), List.of(velocityMinEntry, velocityMaxEntry)).build()
        );

        configCategory.addEntry(
                entryBuilder.startBooleanToggle(Component.nullToEmpty("Spawn at feet"), spawnAtFeet.get())
                        .setDefaultValue(false)
                        .setTooltip(Component.nullToEmpty("If enabled, the particle will spawn at the entity’s feet instead of the center."))
                        .setSaveConsumer(spawnAtFeet::set)
                        .build()
        );

        builder.setSavingRunnable(() -> serializer.save());

        return builder.build();
    }

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