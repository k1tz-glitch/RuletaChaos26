package com.ruletacaos.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Random;

public class BlueEffects {
    private static final Random random = new Random();

    public static void spawnChest(MinecraftServer server) {
        if (server.getPlayerList().getPlayers().isEmpty()) return;

        ServerPlayer ref = server.getPlayerList().getPlayers().get(random.nextInt(server.getPlayerList().getPlayers().size()));
        ServerLevel level = ref.serverLevel();

        double ox = (random.nextDouble() - 0.5) * 40;
        double oz = (random.nextDouble() - 0.5) * 40;
        BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(ref.getX() + ox, 0, ref.getZ() + oz));

        level.setBlockAndUpdate(pos, Blocks.CHEST.defaultBlockState());
        if (level.getBlockEntity(pos) instanceof ChestBlockEntity chest) {
            fillChest(chest);
        }

        String coords = pos.getX() + " " + pos.getY() + " " + pos.getZ();
        server.getPlayerList().broadcastSystemMessage(Component.literal("§9§l[AZUL] §f¡Apareció un cofre especial!"), false);
        server.getPlayerList().broadcastSystemMessage(Component.literal("§bCoordenadas: §f" + coords), false);

        if (com.ruletacaos.RuletaChaos.CONFIG.blueTrapChance && random.nextDouble() * 100 < com.ruletacaos.RuletaChaos.CONFIG.blueTrapChancePercent) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c§l¡Cuidado! Puede ser una trampa..."), false);
        }
    }

    private static void fillChest(ChestBlockEntity chest) {
        int rarity = random.nextInt(100);
        if (rarity < 60) {
            chest.setItem(0, new ItemStack(Items.DIAMOND, 3 + random.nextInt(5)));
            chest.setItem(1, new ItemStack(Items.GOLDEN_APPLE, 2 + random.nextInt(3)));
            chest.setItem(2, new ItemStack(Items.EMERALD, 5 + random.nextInt(10)));
        } else if (rarity < 90) {
            chest.setItem(0, new ItemStack(Items.DIAMOND, 8 + random.nextInt(8)));
            chest.setItem(1, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1 + random.nextInt(2)));
            chest.setItem(2, new ItemStack(Items.NETHERITE_INGOT, 1 + random.nextInt(2)));
            chest.setItem(3, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
        } else {
            chest.setItem(0, new ItemStack(Items.NETHERITE_INGOT, 3 + random.nextInt(3)));
            chest.setItem(1, new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 3));
            chest.setItem(2, new ItemStack(Items.TOTEM_OF_UNDYING, 2));
            chest.setItem(3, new ItemStack(Items.NETHERITE_SWORD));
            chest.setItem(4, new ItemStack(Items.DIAMOND_BLOCK, 2));
        }
    }
}
