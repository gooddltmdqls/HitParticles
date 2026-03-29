package kr.icetang0123.mods.hit_particles.mixin;

import kr.icetang0123.mods.hit_particles.HitParticlesCommand;
import kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @ModifyArg(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    public Screen openConfigScreen(Screen screen) {
        if (HitParticlesCommand.openConfig) {
            HitParticlesCommand.openConfig = false;

            return FabricLoader.getInstance().isModLoaded("cloth-config")
                    ? ConfigScreenFactory.getConfigScreen(null)
                    : new AlertScreen(() -> Minecraft.getInstance().setScreen(null), Component.nullToEmpty("HideMeToast"), Component.nullToEmpty("You must install cloth-config mod to configure HitParticles"));
        }

        return screen;
    }

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setValue(Ljava/lang/String;)V"))
    public void openConfigScreen(KeyEvent input, CallbackInfoReturnable<Boolean> cir) {
        Minecraft mc = Minecraft.getInstance();

        if (HitParticlesCommand.openConfig) {
            HitParticlesCommand.openConfig = false;

            mc.setScreen(
                    FabricLoader.getInstance().isModLoaded("cloth-config")
                            ? ConfigScreenFactory.getConfigScreen(null)
                            : new AlertScreen(() -> Minecraft.getInstance().setScreen(null), Component.nullToEmpty("HideMeToast"), Component.nullToEmpty("You must install cloth-config mod to configure HitParticles"))
            );
        }
    }
}
