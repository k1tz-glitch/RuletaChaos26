package com.ruletacaos;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import java.util.Random;

public class RewardHandler {
    private static final Random random = new Random();

    public static void spawnBlueChest(MinecraftServer server) {
        ServerPlayerEntity p = getRandomPlayer(server);
        if (p == null) return;
        BlockPos pos = new BlockPos(p.getX() + random.nextInt(1000)-500, p.getY(), p.getZ() + random.nextInt(1000)-500);
        p.sendMessage(Text.literal("§l§bCOFRE AZUL: §rCofre encontrado en " + pos.toShortString()), false);
    }

    public static void giveGreenPrize(MinecraftServer server) {
        ServerPlayerEntity p = getRandomPlayer(server);
        if (p == null) return;
        p.getInventory().offer(new ItemStack(Items.NETHERITE_INGOT, 5));
        server.getPlayerManager().getPlayerList().forEach(player ->
            player.sendMessage(Text.literal("§l§aPREMIO VERDE: §r¡" + p.getName().getString() + " ganó Netherite!"), false));
    }

    private static ServerPlayerEntity getRandomPlayer(MinecraftServer s) {
        var list = s.getPlayerManager().getPlayerList();
        return list.isEmpty() ? null : list.get(random.nextInt(list.size()));
    }
}
