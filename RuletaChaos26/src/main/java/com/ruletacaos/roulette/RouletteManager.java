package com.ruletacaos.roulette;

import com.ruletacaos.RuletaChaos;
import com.ruletacaos.config.ModConfig;
import com.ruletacaos.effect.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.ChatFormatting;

import java.util.*;

public class RouletteManager {
    private final MinecraftServer server;
    private final Random random = new Random();

    private long nextSpinTick = 0;
    private boolean spinning = false;

    private ActiveChallenge currentChallenge = null;
    private ServerBossEvent challengeBar = null;

    private final Set<String> activeYellowEffects = new HashSet<>();

    public RouletteManager(MinecraftServer server) {
        this.server = server;
        scheduleNextSpin();
    }

    public void start() {
        broadcast(Component.literal("§6[Ruleta Chaos] §f¡La ruleta está activa! Gira automáticamente cada 5-20 minutos."));
    }

    public void tick() {
        long currentTick = server.overworld().getGameTime();

        if (RuletaChaos.CONFIG.autoSpinEnabled && currentTick >= nextSpinTick && !spinning) {
            spin();
        }

        if (currentChallenge != null) {
            currentChallenge.tick(server);
            if (currentChallenge.isFinished()) {
                endChallenge();
            } else if (challengeBar != null) {
                float progress = currentChallenge.getProgress();
                challengeBar.setProgress(Math.max(0f, Math.min(1f, progress)));
            }
        }
    }

    public void spin() {
        spinning = true;
        RouletteColor color = pickRandomEnabledColor();
        if (color == null) {
            broadcast(Component.literal("§c[Ruleta] No hay colores activos en la configuración."));
            spinning = false;
            scheduleNextSpin();
            return;
        }

        showNotification(color);
        applyColor(color);

        spinning = false;
        scheduleNextSpin();
    }

    private RouletteColor pickRandomEnabledColor() {
        List<RouletteColor> enabled = new ArrayList<>();
        ModConfig cfg = RuletaChaos.CONFIG;

        if (cfg.redEnabled) enabled.add(RouletteColor.RED);
        if (cfg.yellowEnabled) enabled.add(RouletteColor.YELLOW);
        if (cfg.blueEnabled) enabled.add(RouletteColor.BLUE);
        if (cfg.greenEnabled) enabled.add(RouletteColor.GREEN);
        if (cfg.orangeEnabled) enabled.add(RouletteColor.ORANGE);
        if (cfg.pinkEnabled) enabled.add(RouletteColor.PINK);
        if (cfg.violetEnabled) enabled.add(RouletteColor.VIOLET);
        if (cfg.cyanEnabled) enabled.add(RouletteColor.CYAN);

        if (enabled.isEmpty()) return null;
        return enabled.get(random.nextInt(enabled.size()));
    }

    private void showNotification(RouletteColor color) {
        if (!RuletaChaos.CONFIG.showNotification) return;

        Component title = Component.literal("§f§l¡RULETA!").withStyle(ChatFormatting.BOLD);
        Component subtitle = Component.literal(color.colorCode + "§l" + color.displayName + " §r§7- " + color.category);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal("§8§m                                        "));
            player.sendSystemMessage(Component.literal("  §f§lNOTIFICACIÓN DE RULETA").withStyle(ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal("  " + color.colorCode + "▶ " + color.displayName + " §7(" + color.category + ")"));
            player.sendSystemMessage(Component.literal("§8§m                                        "));
            player.sendSystemMessage(Component.literal(""));

            player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket(title));
            player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket(subtitle));
            player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket(10, 70, 20));
        }
    }

    private void applyColor(RouletteColor color) {
        switch (color) {
            case RED -> startRedChallenge();
            case YELLOW -> YellowEffects.apply(server, RuletaChaos.CONFIG.yellowDifficulty, activeYellowEffects);
            case BLUE -> BlueEffects.spawnChest(server);
            case GREEN -> GreenEffects.giveItems(server);
            case ORANGE -> OrangeEffects.applyRandom(server);
            case PINK -> PinkEffects.spawnAlly(server);
            case VIOLET -> VioletEffects.applyRandom(server);
            case CYAN -> CyanEffects.applyRandom(server);
        }
    }

    private void startRedChallenge() {
        if (currentChallenge != null) {
            endChallenge();
        }

        List<String> challenges = new ArrayList<>(RuletaChaos.CONFIG.redChallenges);
        if (challenges.isEmpty()) {
            challenges.add("life_drain");
        }
        String chosen = challenges.get(random.nextInt(challenges.size()));

        int duration = RuletaChaos.CONFIG.redMinDurationSeconds +
                random.nextInt(Math.max(1, RuletaChaos.CONFIG.redMaxDurationSeconds - RuletaChaos.CONFIG.redMinDurationSeconds + 1));

        currentChallenge = RedChallenges.create(chosen, duration, server);
        if (currentChallenge == null) {
            broadcast(Component.literal("§c[Ruleta Roja] Desafío desconocido: " + chosen));
            return;
        }

        challengeBar = new ServerBossEvent(
                Component.literal("§c§lDESAFÍO: " + currentChallenge.getName()),
                BossEvent.BossBarColor.RED,
                BossEvent.BossBarOverlay.PROGRESS
        );
        challengeBar.setProgress(1.0f);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            challengeBar.addPlayer(player);
        }

        broadcast(Component.literal("§c§l[DESAFÍO ROJO] §f" + currentChallenge.getName() + " §7(" + duration + "s)"));
        currentChallenge.start(server);
    }

    public void endChallenge() {
        if (currentChallenge != null) {
            currentChallenge.end(server);
            currentChallenge = null;
        }
        if (challengeBar != null) {
            challengeBar.removeAllPlayers();
            challengeBar = null;
        }
        broadcast(Component.literal("§a[Ruleta] El desafío ha terminado."));
    }

    public void forceSpin() {
        spin();
    }

    public void setYellowDifficulty(int level) {
        RuletaChaos.CONFIG.yellowDifficulty = Math.max(1, Math.min(3, level));
        RuletaChaos.CONFIG.save();
        broadcast(Component.literal("§e[Ruleta] Dificultad amarilla fijada a nivel " + RuletaChaos.CONFIG.yellowDifficulty));
    }

    private void scheduleNextSpin() {
        List<Integer> intervals = RuletaChaos.CONFIG.intervalMinutes;
        if (intervals == null || intervals.isEmpty()) {
            intervals = Arrays.asList(5, 10, 20);
        }
        int minutes = intervals.get(random.nextInt(intervals.size()));
        nextSpinTick = server.overworld().getGameTime() + (minutes * 60L * 20L);
        RuletaChaos.LOGGER.info("Próximo giro de ruleta en {} minutos.", minutes);
    }

    private void broadcast(Component message) {
        server.getPlayerList().broadcastSystemMessage(message, false);
    }

    public boolean isChallengeActive() {
        return currentChallenge != null;
    }
}
