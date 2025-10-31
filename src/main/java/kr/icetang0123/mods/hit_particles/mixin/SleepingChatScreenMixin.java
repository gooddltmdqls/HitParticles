package kr.icetang0123.mods.hit_particles.mixin;

import kr.icetang0123.mods.hit_particles.HitParticlesCommand;
import kr.icetang0123.mods.hit_particles.config.ConfigScreenFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SleepingChatScreen.class)
public class SleepingChatScreenMixin {
    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setText(Ljava/lang/String;)V"))
    public void openConfigScreen(KeyInput input, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient mc = MinecraftClient.getInstance();

        if (HitParticlesCommand.openConfig) {
            HitParticlesCommand.openConfig = false;

            mc.setScreen(
                    FabricLoader.getInstance().isModLoaded("cloth-config")
                            ? ConfigScreenFactory.getConfigScreen(null)
                            : new NoticeScreen(() -> MinecraftClient.getInstance().setScreen(null), Text.of("HideMeToast"), Text.of("You must install cloth-config mod to configure HitParticles"))
            );
        }
    }
}
