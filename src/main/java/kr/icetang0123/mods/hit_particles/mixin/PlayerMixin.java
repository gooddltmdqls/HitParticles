package kr.icetang0123.mods.hit_particles.mixin;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import kr.icetang0123.mods.hit_particles.HitParticles;
import kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory;
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

import java.util.Random;

@Mixin(Player.class)
public class PlayerMixin {
    @Unique
    private static HolderLookup.Provider registries = VanillaRegistries.createLookup();

    @Inject(method = "attack", at = @At("TAIL"))
    void attack(Entity target, CallbackInfo ci) {
        String particleType = ConfigScreenFactory.particleType.get();

        ParticleOptions result;

        StringReader reader = new StringReader(particleType);

        try {
            result = ParticleArgument.readParticle(reader, registries);
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

            target.level().addParticle(
                    result,
                    target.getX() + xOffset,
                    (spawnAtFeet ? target.getY() : (target.getBbHeight() + (target.getY() * 2)) / 2) + yOffset,
                    target.getZ() + zOffset,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityX,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityY,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityZ
            );
        }
    }
}
