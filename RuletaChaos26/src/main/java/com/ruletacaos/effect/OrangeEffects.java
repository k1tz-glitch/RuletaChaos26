package com.ruletacaos.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.Random;

public class OrangeEffects {
    private static final Random random = new Random();

    public static void applyRandom(MinecraftServer server) {
        List<String> effects = com.ruletacaos.RuletaChaos.CONFIG.orangeEffects;
        if (effects == null || effects.isEmpty()) return;

        String chosen = effects.get(random.nextInt(effects.size()));

        switch (chosen) {
            case "moon_gravity" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§6§l[NARANJA] §f¡Gravedad lunar!"), false);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    p.addEffect(new MobEffectInstance(MobEffects.JUMP, 20 * 45, 3));
                    p.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 45, 0));
                }
            }
            case "animal_rain" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§6§l[NARANJA] §f¡Lluvia de animales!"), false);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    ServerLevel level = p.serverLevel();
                    for (int i = 0; i < 8; i++) {
                        double x = p.getX() + (random.nextDouble() - 0.5) * 15;
                        double z = p.getZ() + (random.nextDouble() - 0.5) * 15;
                        double y = p.getY() + 15 + random.nextDouble() * 10;
                        var type = switch (random.nextInt(5)) {
                            case 0 -> EntityType.COW;
                            case 1 -> EntityType.PIG;
                            case 2 -> EntityType.SHEEP;
                            case 3 -> EntityType.CHICKEN;
                            default -> EntityType.RABBIT;
                        };
                        var entity = type.create(level);
                        if (entity != null) {
                            entity.moveTo(x, y, z, 0, 0);
                            level.addFreshEntity(entity);
                        }
                    }
                }
            }
            case "gather_all" -> {
                List<ServerPlayer> players = server.getPlayerList().getPlayers();
                if (players.isEmpty()) return;
                ServerPlayer target = players.get(random.nextInt(players.size()));
                server.getPlayerList().broadcastSystemMessage(Component.literal("§6§l[NARANJA] §f¡Reunión forzosa en " + target.getName().getString() + "!"), false);
                for (ServerPlayer p : players) {
                    if (p != target) {
                        p.teleportTo(target.serverLevel(), target.getX(), target.getY(), target.getZ(), java.util.Set.of(), p.getYRot(), p.getXRot());
                    }
                }
            }
            case "shuffle_inventory" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§6§l[NARANJA] §f¡Inventarios revueltos!"), false);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    var inv = p.getInventory();
                    for (int i = 0; i < 9; i++) {
                        int j = random.nextInt(9);
                        var temp = inv.getItem(i).copy();
                        inv.setItem(i, inv.getItem(j).copy());
                        inv.setItem(j, temp);
                    }
                    p.containerMenu.broadcastChanges();
                }
            }
            case "double_speed" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§6§l[NARANJA] §f¡Doble velocidad!"), false);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 60, 1));
                }
            }
            case "crazy_time" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§6§l[NARANJA] §f¡Tiempo loco!"), false);
                long time = server.overworld().getDayTime();
                server.overworld().setDayTime(time + (random.nextBoolean() ? 12000 : -12000));
            }
            default -> server.getPlayerList().broadcastSystemMessage(Component.literal("§6[NARANJA] Efecto: " + chosen), false);
        }
    }
}
