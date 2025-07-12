package kr.icetang0123.mods.hit_particles.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config"))
            return kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory::getConfigScreen;

        return (parent) -> new NoticeScreen(() -> MinecraftClient.getInstance().setScreen(parent), Text.of("HitParticles"), Text.of("You must install cloth-config mod to configure HitParticles."));
    }
}