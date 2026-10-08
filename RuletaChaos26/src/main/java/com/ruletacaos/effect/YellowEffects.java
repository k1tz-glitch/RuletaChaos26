package com.ruletacaos.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.util.Set;

public class YellowEffects {
    public static void apply(MinecraftServer server, int difficulty, Set<String> active) {
        String levelName = switch (difficulty) {
            case 1 -> "Suave";
            case 2 -> "Medio";
            case 3 -> "Hardcore";
            default -> "Normal";
        };

        server.getPlayerList().broadcastSystemMessage(
                Component.literal("§e§l[AMARILLO] §fCambios permanentes activados (dificultad: §e" + levelName + "§f)"),
                false
        );

        active.add("creepers_faster");
        active.add("zombies_stronger");
        active.add("mobs_more_health");
        active.add("more_mobs");
        active.add("no_burn_day");
        active.add("slow_regen");
        active.add("double_xp");

        server.getPlayerList().broadcastSystemMessage(Component.literal("§7• Creepers más rápidos y explosivos"), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("§7• Zombies con más fuerza"), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("§7• Monstruos con más vida y visión"), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("§7• Más monstruos y con armadura"), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("§7• Zombies/esqueletos no se queman con el sol"), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("§7• Regeneración más lenta + Doble XP"), false);
    }
}
