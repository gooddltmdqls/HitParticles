package kr.icetang0123.mods.hit_particles.mixin;

import kr.icetang0123.mods.hit_particles.HitParticlesCommand;
import kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @ModifyArg(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    public Screen openConfigScreen(Screen screen) {
        if (HitParticlesCommand.openConfig) {
            HitParticlesCommand.openConfig = false;

            return FabricLoader.getInstance().isModLoaded("cloth-config")
                    ? ConfigScreenFactory.getConfigScreen(null)
                    : new NoticeScreen(() -> MinecraftClient.getInstance().setScreen(null), Text.of("HideMeToast"), Text.of("You must install cloth-config mod to configure HitParticles"));
        }

        return screen;
    }
}
