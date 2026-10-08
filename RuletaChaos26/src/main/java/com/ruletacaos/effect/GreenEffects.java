package com.ruletacaos.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Random;

public class GreenEffects {
    private static final Random random = new Random();

    public static void giveItems(MinecraftServer server) {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        if (players.isEmpty()) return;

        ServerPlayer winner = players.get(random.nextInt(players.size()));

        ItemStack[] possible = {
                new ItemStack(Items.TOTEM_OF_UNDYING, 1 + random.nextInt(2)),
                new ItemStack(Items.DIAMOND, 8 + random.nextInt(16)),
                new ItemStack(Items.NETHERITE_INGOT, 1 + random.nextInt(3)),
                new ItemStack(Items.GOLDEN_APPLE, 4 + random.nextInt(6)),
                new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1 + random.nextInt(2)),
                new ItemStack(Items.EMERALD, 16 + random.nextInt(32))
        };

        ItemStack prize = possible[random.nextInt(possible.length)];
        winner.addItem(prize.copy());

        server.getPlayerList().broadcastSystemMessage(
                Component.literal("§a§l[VERDE] §f¡" + winner.getName().getString() + " recibió un premio enorme!"),
                false
        );
        winner.sendSystemMessage(Component.literal("§a¡Has ganado: §f" + prize.getCount() + "x " + prize.getHoverName().getString()));

        for (ServerPlayer p : players) {
            if (p != winner) {
                p.addItem(new ItemStack(Items.GOLDEN_APPLE, 1 + random.nextInt(2)));
            }
        }
    }
}
