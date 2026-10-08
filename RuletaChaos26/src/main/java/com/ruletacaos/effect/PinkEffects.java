package com.ruletacaos.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Random;

public class PinkEffects {
    private static final Random random = new Random();

    public static void spawnAlly(MinecraftServer server) {
        List<String> allies = com.ruletacaos.RuletaChaos.CONFIG.pinkAllies;
        if (allies == null || allies.isEmpty()) return;

        String chosen = allies.get(random.nextInt(allies.size()));
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return;

        ServerPlayer target = players.get(random.nextInt(players.size()));
        ServerLevel level = target.serverLevel();

        switch (chosen) {
            case "tamed_wolf" -> {
                Wolf wolf = EntityType.WOLF.create(level);
                if (wolf != null) {
                    wolf.moveTo(target.getX(), target.getY(), target.getZ(), 0, 0);
                    wolf.tame(target);
                    level.addFreshEntity(wolf);
                    server.getPlayerList().broadcastSystemMessage(Component.literal("§d§l[ROSA] §f¡" + target.getName().getString() + " recibió un lobo domado!"), false);
                }
            }
            case "horse_with_saddle" -> {
                Horse horse = EntityType.HORSE.create(level);
                if (horse != null) {
                    horse.moveTo(target.getX() + 1, target.getY(), target.getZ(), 0, 0);
                    horse.equipSaddle(new ItemStack(Items.SADDLE), null);
                    horse.setTamed(true);
                    level.addFreshEntity(horse);
                    server.getPlayerList().broadcastSystemMessage(Component.literal("§d§l[ROSA] §f¡" + target.getName().getString() + " recibió un caballo con montura!"), false);
                }
            }
            case "iron_golem" -> {
                IronGolem golem = EntityType.IRON_GOLEM.create(level);
                if (golem != null) {
                    golem.moveTo(target.getX(), target.getY(), target.getZ(), 0, 0);
                    golem.setPlayerCreated(true);
                    level.addFreshEntity(golem);
                    server.getPlayerList().broadcastSystemMessage(Component.literal("§d§l[ROSA] §f¡Apareció un Golem de Hierro aliado!"), false);
                }
            }
            case "tamed_cat" -> {
                Cat cat = EntityType.CAT.create(level);
                if (cat != null) {
                    cat.moveTo(target.getX(), target.getY(), target.getZ(), 0, 0);
                    cat.tame(target);
                    level.addFreshEntity(cat);
                    server.getPlayerList().broadcastSystemMessage(Component.literal("§d§l[ROSA] §f¡" + target.getName().getString() + " recibió un gato domado!"), false);
                }
            }
            case "allay" -> {
                Allay allay = EntityType.ALLAY.create(level);
                if (allay != null) {
                    allay.moveTo(target.getX(), target.getY() + 1, target.getZ(), 0, 0);
                    level.addFreshEntity(allay);
                    server.getPlayerList().broadcastSystemMessage(Component.literal("§d§l[ROSA] §f¡Apareció un Allay!"), false);
                }
            }
        }
    }
}
