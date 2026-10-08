package com.ruletacaos.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.ruletacaos.RuletaChaos;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class RuletaCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("ruleta")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("girar")
                        .executes(ctx -> {
                            if (RuletaChaos.ROULETTE != null) {
                                RuletaChaos.ROULETTE.forceSpin();
                                ctx.getSource().sendSuccess(() -> Component.literal("§aRuleta forzada a girar."), true);
                            } else {
                                ctx.getSource().sendFailure(Component.literal("La ruleta aún no está inicializada."));
                            }
                            return 1;
                        }))
                .then(Commands.literal("parar")
                        .executes(ctx -> {
                            if (RuletaChaos.ROULETTE != null && RuletaChaos.ROULETTE.isChallengeActive()) {
                                RuletaChaos.ROULETTE.endChallenge();
                                ctx.getSource().sendSuccess(() -> Component.literal("§aDesafío detenido."), true);
                            } else {
                                ctx.getSource().sendFailure(Component.literal("No hay desafío activo."));
                            }
                            return 1;
                        }))
                .then(Commands.literal("amarillo")
                        .then(Commands.argument("nivel", IntegerArgumentType.integer(1, 3))
                                .executes(ctx -> {
                                    int nivel = IntegerArgumentType.getInteger(ctx, "nivel");
                                    if (RuletaChaos.ROULETTE != null) {
                                        RuletaChaos.ROULETTE.setYellowDifficulty(nivel);
                                        ctx.getSource().sendSuccess(() -> Component.literal("§eDificultad amarilla fijada a " + nivel), true);
                                    }
                                    return 1;
                                })))
                .then(Commands.literal("recargar")
                        .executes(ctx -> {
                            RuletaChaos.CONFIG = com.ruletacaos.config.ModConfig.load();
                            ctx.getSource().sendSuccess(() -> Component.literal("§aConfiguración recargada."), true);
                            return 1;
                        }))
                .then(Commands.literal("estado")
                        .executes(ctx -> {
                            boolean active = RuletaChaos.ROULETTE != null && RuletaChaos.ROULETTE.isChallengeActive();
                            ctx.getSource().sendSuccess(() -> Component.literal("§7Desafío activo: " + (active ? "§cSí" : "§aNo")), false);
                            ctx.getSource().sendSuccess(() -> Component.literal("§7Auto-spin: " + (RuletaChaos.CONFIG.autoSpinEnabled ? "§aActivado" : "§cDesactivado")), false);
                            return 1;
                        }))
        );
    }
}
