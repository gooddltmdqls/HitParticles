package kr.icetang0123.mods.hit_particles;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HitParticles implements ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger(HitParticles.class);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initialized!");

        HitParticlesCommand.registerCommands();
    }
}
