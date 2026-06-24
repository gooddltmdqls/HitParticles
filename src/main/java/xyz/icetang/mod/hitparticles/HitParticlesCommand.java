package xyz.icetang.mod.hitparticles;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public abstract class HitParticlesCommand {
    public static boolean openConfig = false;

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, _) -> dispatcher.register(
                LiteralArgumentBuilder.<FabricClientCommandSource>literal("hitparticles")
                        .executes((_) -> {
                            openConfig = true;

                            return Command.SINGLE_SUCCESS;
                        })

        )));
    }
}
