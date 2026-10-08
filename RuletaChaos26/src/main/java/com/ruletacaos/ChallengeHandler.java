package com.ruletacaos;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChallengeHandler {
    private static final Map<UUID, Challenge> activeChallenges = new ConcurrentHashMap<>();
    private static final Random random = new Random();

    public static void startRandomRedChallenge(MinecraftServer server) {
        String[] challenges = {"SUBIR_250", "BAJAR_M50", "DRENAJE_VIDA", "CREEPER_SIDES", "HORDA_MONSTRUOS", "METEORITOS", "NO_QUIETO", "HAMBRE_EXTREMA", "RAYOS"};
        String selected = challenges[random.nextInt(challenges.length)];
        int duration = 180 + random.nextInt(121);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            activeChallenges.put(player.getUuid(), new Challenge(selected, duration));
            player.sendMessage(Text.literal("§l§cDESAFÍO ROJO: " + selected + " §r§7(" + (duration / 60) + "m)").formatted(Formatting.RED), false);
        }
    }

    public static void tick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            Challenge c = activeChallenges.get(player.getUuid());
            if (c == null) continue;
            c.timeLeft--;
            if (isFailing(player, c)) player.damage(2.0, player.getDamageSources().generic());
            if (c.timeLeft <= 0) activeChallenges.remove(player.getUuid());
        }
    }

    private static boolean isFailing(PlayerEntity p, Challenge c) {
        return switch (c.type) {
            case "SUBIR_250" -> p.getY() < 250;
            case "BAJAR_M50" -> p.getY() > -50;
            case "NO_QUIETO" -> p.getVelocity().lengthSquared() < 0.001;
            default -> false;
        };
    }

    public static void stopAll() { activeChallenges.clear(); }
    public static class Challenge {
        public String type; public int timeLeft;
        public Challenge(String t, int l) { this.type = t; this.timeLeft = l; }
    }
}
