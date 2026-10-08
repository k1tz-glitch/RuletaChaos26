package com.ruletacaos;

import net.minecraft.entity.effect.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import java.util.Random;

public class ChaosHandler {
    private static final Random random = new Random();

    public static void triggerOrangeChaos(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(p -> {
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 200, 0));
            p.sendMessage(Text.literal("§l§6CAOS: §r¡Gravedad Lunar!"), false);
        });
    }

    public static void spawnPinkAllies(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(p ->
            p.sendMessage(Text.literal("§l§dALIADOS: §r¡Un aliado ha aparecido!"), false));
    }

    public static void applyVioletMagic(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(p -> {
            p.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 600, 0));
            p.sendMessage(Text.literal("§l§5MAGIA: §r¡Efecto de Brillo!"), false);
        });
    }
}
