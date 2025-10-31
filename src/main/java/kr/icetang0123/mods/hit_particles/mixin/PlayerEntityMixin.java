package kr.icetang0123.mods.hit_particles.mixin;

import kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory;
import kr.icetang0123.mods.hit_particles.config.ConfigSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Random;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "attack", at = @At("TAIL"))
    void attack(Entity target, CallbackInfo ci) {
        Map<String, Object> config = new ConfigSerializer().load();

        double velocityMin = ConfigScreenFactory.toDouble(config.get("velocity_min"));
        double velocityMax = ConfigScreenFactory.toDouble(config.get("velocity_max"));

        for (int i = 0; i < ConfigScreenFactory.toInt(config.get("particle_count")); i++) {
            Random isPlusOrMinus = new Random();

            double velocityX = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;
            double velocityY = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;
            double velocityZ = isPlusOrMinus.nextBoolean() ? 1.0 : -1.0;

            target.getEntityWorld().addParticleClient(
                    ParticleTypes.ENCHANTED_HIT,
                    target.getX(),
                    (target.getHeight() + (target.getY() * 2)) / 2,
                    target.getZ(),
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityX,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityY,
                    (Math.random() * (velocityMax - velocityMin) + velocityMin) * velocityZ
            );
        }
    }
}
