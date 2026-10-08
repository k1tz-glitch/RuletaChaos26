package com.ruletacaos.effect;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Random;

public class CyanEffects {
    private static final Random random = new Random();

    public static void applyRandom(MinecraftServer server) {
        List<String> effects = com.ruletacaos.RuletaChaos.CONFIG.cyanEffects;
        if (effects == null || effects.isEmpty()) return;

        String chosen = effects.get(random.nextInt(effects.size()));

        switch (chosen) {
            case "storm_or_clear" -> {
                boolean storm = random.nextBoolean();
                if (storm) {
                    server.overworld().setWeatherParameters(0, 6000, true, true);
                    server.getPlayerList().broadcastSystemMessage(Component.literal("§b§l[CELESTE] §f¡Tormenta activada!"), false);
                } else {
                    server.overworld().setWeatherParameters(6000, 0, false, false);
                    server.getPlayerList().broadcastSystemMessage(Component.literal("§b§l[CELESTE] §f¡Clima despejado!"), false);
                }
            }
            case "item_rain" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§b§l[CELESTE] §f¡Lluvia de items!"), false);
                ItemStack[] items = {
                        new ItemStack(Items.IRON_INGOT, 3),
                        new ItemStack(Items.GOLD_INGOT, 2),
                        new ItemStack(Items.DIAMOND, 1),
                        new ItemStack(Items.ARROW, 16),
                        new ItemStack(Items.BREAD, 4),
                        new ItemStack(Items.EXPERIENCE_BOTTLE, 3)
                };
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    ServerLevel level = p.serverLevel();
                    for (int i = 0; i < 6; i++) {
                        ItemStack stack = items[random.nextInt(items.length)].copy();
                        double x = p.getX() + (random.nextDouble() - 0.5) * 8;
                        double z = p.getZ() + (random.nextDouble() - 0.5) * 8;
                        ItemEntity entity = new ItemEntity(level, x, p.getY() + 10, z, stack);
                        level.addFreshEntity(entity);
                    }
                }
            }
            case "double_mining" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§b§l[CELESTE] §f¡Minería doble por 2 minutos!"), false);
            }
        }
    }
}
