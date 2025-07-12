package kr.icetang0123.mods.hit_particles;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public abstract class HitParticlesCommand {
    public static boolean openConfig = false;

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(
                LiteralArgumentBuilder.<FabricClientCommandSource>literal("hitparticles")
                        .executes((source) -> {
                            openConfig = true;

                            return Command.SINGLE_SUCCESS;
                        })

        )));
    }
}
