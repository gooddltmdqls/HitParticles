package kr.icetang0123.mods.hit_particles.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.network.chat.Component;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config"))
            return kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory::getConfigScreen;

        return (parent) -> new AlertScreen(() -> Minecraft.getInstance().setScreen(parent), Component.nullToEmpty("HitParticles"), Component.nullToEmpty("You must install cloth-config mod to configure HitParticles."));
    }
}