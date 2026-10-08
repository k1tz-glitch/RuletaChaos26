package com.ruletacaos;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.time.TimeOfDay;

public class WorldHandler {

    public static void changeWorldClimate(MinecraftServer server) {
        int effect = (int)(Math.random() * 3);

        switch (effect) {
            case 0 -> {
                server.getWorld(World.DIMENSION).setWeather(true); // Lluvia
                broadcast(server, "§l§bCLIMA: §r¡Ha comenzado una tormenta!", Formatting.AQUA);
            }
            case 1 -> {
                server.getWorld(World.DIMENSION).setWeather(false); // Despejado
                broadcast(server, "§l§bCLIMA: §r¡El cielo se ha despejado!", Formatting.AQUA);
            }
            case 2 -> {
                broadcast(server, "§l§bMUNDO: §r¡Minería Doble activada por 5 minutos!", Formatting.AQUA);
                // Lógica de minería doble se implementaría en un Mixin de Block.onBroken
            }
        }
    }

    private static void broadcast(MinecraftServer server, String message, Formatting format) {
        server.getPlayerManager().getPlayerList().forEach(p ->
            p.sendMessage(Text.literal(message).formatted(format), false)
        );
    }
}
