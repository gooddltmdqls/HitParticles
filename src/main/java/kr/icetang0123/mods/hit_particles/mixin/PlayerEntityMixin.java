package kr.icetang0123.mods.hit_particles.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kr.icetang0123.mods.hit_particles.HitParticles;
import kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Unique
    private static RegistryWrapper.WrapperLookup registries = BuiltinRegistries.createWrapperLookup();

    @Inject(method = "attack", at = @At("TAIL"))
    void attack(Entity target, CallbackInfo ci) {
        String particleType = ConfigScreenFactory.particleType.get();

        ParticleEffect result;

        StringReader reader = new StringReader(particleType);

        try {
            result = ParticleEffectArgumentType.readParameters(reader, registries);
        } catch (CommandSyntaxException ignored) {
            HitParticles.LOGGER.warn("Failed to parse particle. Ignoring!");

            result = null;
        }

        if (result == null) return;

        double radiusX = ConfigScreenFactory.radiusX.get();
        double radiusY = ConfigScreenFactory.radiusY.get();
        double radiusZ = ConfigScreenFactory.radiusZ.get();

        double offsetX = ConfigScreenFactory.offsetX.get();
        double offsetY = ConfigScreenFactory.offsetY.get();
        double offsetZ = ConfigScreenFactory.offsetZ.get();

        double velocityMin = ConfigScreenFactory.velocityMin.get();
        double velocityMax = ConfigScreenFactory.velocityMax.get();

        boolean spawnAtFeet = ConfigScreenFactory.spawnAtFeet.get();

        for (int i = 0; i < ConfigScreenFactory.toInt(ConfigScreenFactory.particleCount.get()); i++) {
            Random isPlusOrMinus = new Random();

            double xOffset = (isPlusOrMinus.nextBoolean() ? 1.0 : -1.0) * (Math.random() * radiusX) + offsetX;
            double yOffset = (isPlusOrMinus.nextBoolean() ? 1.0 : -1.0) * (Math.random() * radiusY) + offsetY;
            double zOffset = (isPlusOrMinus.nextBoolean() ? 1.0 : -1.0) * (Math.random() * radiusZ) + offsetZ;

            double velocityX = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;
            double velocityY = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;
            double velocityZ = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;

            target.getEntityWorld().addParticleClient(
                    result,
                    target.getX() + xOffset,
                    (spawnAtFeet ? target.getY() : (target.getHeight() + (target.getY() * 2)) / 2) + yOffset,
                    target.getZ() + zOffset,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityX,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityY,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityZ
            );
        }
    }
}
