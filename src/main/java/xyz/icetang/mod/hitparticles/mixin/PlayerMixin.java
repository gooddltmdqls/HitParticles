package xyz.icetang.mod.hitparticles.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import xyz.icetang.mod.hitparticles.HitParticles;
import xyz.icetang.mod.hitparticles.config.Configuration;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Random;

@Mixin(Player.class)
public class PlayerMixin {
    @Unique
    private static String previousParticleType = null;
    @Unique
    private static ParticleOptions particleOptions = null;
    @Unique
    private static HolderLookup.Provider registries = VanillaRegistries.createLookup();

    @Inject(method = "attack", at = @At("TAIL"))
    void attack(Entity entity, CallbackInfo ci) {
        String particleType = Configuration.particleType.get();

        if (!Objects.equals(previousParticleType, particleType) || particleType == null) {
            ParticleOptions result;

            StringReader reader = new StringReader(particleType);

            try {
                result = ParticleArgument.readParticle(reader, registries);
            } catch (CommandSyntaxException ignored) {
                HitParticles.LOGGER.warn("Failed to parse particle. Ignoring!");

                result = null;
            }

            previousParticleType = particleType;
            particleOptions = result;
        }

        if (particleOptions == null) return;

        double radiusX = Configuration.radiusX.get();
        double radiusY = Configuration.radiusY.get();
        double radiusZ = Configuration.radiusZ.get();

        double offsetX = Configuration.offsetX.get();
        double offsetY = Configuration.offsetY.get();
        double offsetZ = Configuration.offsetZ.get();

        double velocityMin = Configuration.velocityMin.get();
        double velocityMax = Configuration.velocityMax.get();

        boolean spawnAtFeet = Configuration.spawnAtFeet.get();

        for (int i = 0; i < Configuration.toInt(Configuration.particleCount.get()); i++) {
            Random isPlusOrMinus = new Random();

            double xOffset = (isPlusOrMinus.nextBoolean() ? 1.0 : -1.0) * (Math.random() * radiusX) + offsetX;
            double yOffset = (isPlusOrMinus.nextBoolean() ? 1.0 : -1.0) * (Math.random() * radiusY) + offsetY;
            double zOffset = (isPlusOrMinus.nextBoolean() ? 1.0 : -1.0) * (Math.random() * radiusZ) + offsetZ;

            double velocityX = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;
            double velocityY = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;
            double velocityZ = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;

            entity.level().addParticle(
                    particleOptions,
                    entity.getX() + xOffset,
                    (spawnAtFeet ? entity.getY() : (entity.getBbHeight() + (entity.getY() * 2)) / 2) + yOffset,
                    entity.getZ() + zOffset,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityX,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityY,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityZ
            );
        }
    }
}
