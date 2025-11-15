package kr.icetang0123.mods.hit_particles.config;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;

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

        RegistryWrapper.WrapperLookup registries = BuiltinRegistries.createWrapperLookup();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("Config"));

        ConfigCategory configCategory = builder.getOrCreateCategory(Text.of("HitParticles config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        configCategory.addEntry(
                entryBuilder.startStrField(Text.of("Particle type"), particleType.get())
                        .setDefaultValue("minecraft:enchanted_hit")
                        .setTooltip(Text.of("This is the particle that appears when you hit an entity. When using particles that require additional parameters (i.e., non-simple particles), you must provide the necessary data. For example: minecraft:block{block_state:\"minecraft:stone\"}."))
                        .setErrorSupplier((s) -> {
                            StringReader reader = new StringReader(s);

                            try {
                                ParticleEffectArgumentType.readParameters(reader, registries);
                            } catch (CommandSyntaxException e) {
                                return Optional.of(Text.of(e.getMessage()));
                            }

                            return Optional.empty();
                        })
                        .setSaveConsumer(particleType::set)
                        .build()
        );

        configCategory.addEntry(
                entryBuilder.startIntField(Text.of("Particle count"), particleCount.get())
                        .setDefaultValue(25)
                        .setTooltip(Text.of("Particle count"))
                        .setSaveConsumer(particleCount::set)
                        .build()
        );

        DoubleListEntry radiusXEntry = entryBuilder.startDoubleField(Text.of("X"), radiusX.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Text.of("The X-axis radius within which the particle spawns."))
                .setSaveConsumer(radiusX::set)
                .build();

        DoubleListEntry radiusYEntry = entryBuilder.startDoubleField(Text.of("Y"), radiusY.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Text.of("The Y-axis radius within which the particle spawns."))
                .setSaveConsumer(radiusY::set)
                .build();

        DoubleListEntry radiusZEntry = entryBuilder.startDoubleField(Text.of("Z"), radiusZ.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Text.of("The Z-axis radius within which the particle spawns."))
                .setSaveConsumer(radiusZ::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Text.of("Radius"), List.of(radiusXEntry, radiusYEntry, radiusZEntry)).build()
        );

        DoubleListEntry offsetXEntry = entryBuilder.startDoubleField(Text.of("X"), offsetX.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Text.of("The X-axis offset applied to the particle position."))
                .setSaveConsumer(offsetX::set)
                .build();

        DoubleListEntry offsetYEntry = entryBuilder.startDoubleField(Text.of("Y"), offsetY.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Text.of("The Y-axis offset applied to the particle position."))
                .setSaveConsumer(offsetY::set)
                .build();

        DoubleListEntry offsetZEntry = entryBuilder.startDoubleField(Text.of("Z"), offsetZ.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Text.of("The Z-axis offset applied to the particle position."))
                .setSaveConsumer(offsetZ::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Text.of("Offset"), List.of(offsetXEntry, offsetYEntry, offsetZEntry)).build()
        );

        DoubleListEntry velocityMinEntry = entryBuilder.startDoubleField(Text.of("Minimum value"), velocityMin.get())
                .setMin(0.0)
                .setDefaultValue(0.001)
                .setTooltip(Text.of("The minimum value of random velocity."))
                .setSaveConsumer(velocityMin::set)
                .build();

        DoubleListEntry velocityMaxEntry = entryBuilder.startDoubleField(Text.of("Maximum value"), velocityMax.get())
                .setMin(0.0)
                .setDefaultValue(1.0)
                .setTooltip(Text.of("The maximum value of random velocity."))
                .setSaveConsumer(velocityMax::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Text.of("Velocity"), List.of(velocityMinEntry, velocityMaxEntry)).build()
        );

        configCategory.addEntry(
                entryBuilder.startBooleanToggle(Text.of("Spawn at feet"), spawnAtFeet.get())
                        .setDefaultValue(false)
                        .setTooltip(Text.of("If enabled, the particle will spawn at the entity’s feet instead of the center."))
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