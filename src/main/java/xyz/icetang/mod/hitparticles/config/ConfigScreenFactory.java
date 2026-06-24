package xyz.icetang.mod.hitparticles.config;

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

import java.util.List;
import java.util.Optional;

public abstract class ConfigScreenFactory {
    public static ConfigSerializer serializer = new ConfigSerializer();

    public static Screen getConfigScreen(Screen parent) {
        Configuration.loadConfig();

        HolderLookup.Provider registries = VanillaRegistries.createLookup();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Config"));

        ConfigCategory configCategory = builder.getOrCreateCategory(Component.literal("HitParticles config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        configCategory.addEntry(
                entryBuilder.startStrField(Component.literal("Particle type"), Configuration.particleType.get())
                        .setDefaultValue("minecraft:enchanted_hit")
                        .setTooltip(Component.literal("This is the particle that appears when you hit an entity. When using particles that require additional parameters (i.e., non-simple particles), you must provide the necessary data. For example: minecraft:block{block_state:\"minecraft:stone\"}."))
                        .setErrorSupplier((s) -> {
                            StringReader reader = new StringReader(s);

                            try {
                                ParticleArgument.readParticle(reader, registries);
                            } catch (CommandSyntaxException e) {
                                return Optional.of(Component.nullToEmpty(e.getMessage()));
                            }

                            return Optional.empty();
                        })
                        .setSaveConsumer(Configuration.particleType::set)
                        .build()
        );

        configCategory.addEntry(
                entryBuilder.startIntField(Component.literal("Particle count"), Configuration.particleCount.get())
                        .setDefaultValue(25)
                        .setTooltip(Component.literal("Particle count"))
                        .setSaveConsumer(Configuration.particleCount::set)
                        .build()
        );

        DoubleListEntry radiusXEntry = entryBuilder.startDoubleField(Component.literal("X"), Configuration.radiusX.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.literal("The X-axis radius within which the particle spawns."))
                .setSaveConsumer(Configuration.radiusX::set)
                .build();

        DoubleListEntry radiusYEntry = entryBuilder.startDoubleField(Component.literal("Y"), Configuration.radiusY.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.literal("The Y-axis radius within which the particle spawns."))
                .setSaveConsumer(Configuration.radiusY::set)
                .build();

        DoubleListEntry radiusZEntry = entryBuilder.startDoubleField(Component.literal("Z"), Configuration.radiusZ.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.literal("The Z-axis radius within which the particle spawns."))
                .setSaveConsumer(Configuration.radiusZ::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Component.literal("Radius"), List.of(radiusXEntry, radiusYEntry, radiusZEntry)).build()
        );

        DoubleListEntry offsetXEntry = entryBuilder.startDoubleField(Component.literal("X"), Configuration.offsetX.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.literal("The X-axis offset applied to the particle position."))
                .setSaveConsumer(Configuration.offsetX::set)
                .build();

        DoubleListEntry offsetYEntry = entryBuilder.startDoubleField(Component.literal("Y"), Configuration.offsetY.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.literal("The Y-axis offset applied to the particle position."))
                .setSaveConsumer(Configuration.offsetY::set)
                .build();

        DoubleListEntry offsetZEntry = entryBuilder.startDoubleField(Component.literal("Z"), Configuration.offsetZ.get())
                .setMin(0.0)
                .setDefaultValue(0.0)
                .setTooltip(Component.literal("The Z-axis offset applied to the particle position."))
                .setSaveConsumer(Configuration.offsetZ::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Component.literal("Offset"), List.of(offsetXEntry, offsetYEntry, offsetZEntry)).build()
        );

        DoubleListEntry velocityMinEntry = entryBuilder.startDoubleField(Component.literal("Minimum value"), Configuration.velocityMin.get())
                .setMin(0.0)
                .setDefaultValue(0.001)
                .setTooltip(Component.literal("The minimum value of random velocity."))
                .setSaveConsumer(Configuration.velocityMin::set)
                .build();

        DoubleListEntry velocityMaxEntry = entryBuilder.startDoubleField(Component.literal("Maximum value"), Configuration.velocityMax.get())
                .setMin(0.0)
                .setDefaultValue(1.0)
                .setTooltip(Component.literal("The maximum value of random velocity."))
                .setSaveConsumer(Configuration.velocityMax::set)
                .build();

        configCategory.addEntry(
                entryBuilder.startSubCategory(Component.literal("Velocity"), List.of(velocityMinEntry, velocityMaxEntry)).build()
        );

        configCategory.addEntry(
                entryBuilder.startBooleanToggle(Component.literal("Spawn at feet"), Configuration.spawnAtFeet.get())
                        .setDefaultValue(false)
                        .setTooltip(Component.literal("If enabled, the particle will spawn at the entity’s feet instead of the center."))
                        .setSaveConsumer(Configuration.spawnAtFeet::set)
                        .build()
        );

        builder.setSavingRunnable(() -> serializer.save());

        return builder.build();
    }
}