package com.ruletacaos.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.Random;

public class VioletEffects {
    private static final Random random = new Random();

    public static void applyRandom(MinecraftServer server) {
        List<String> effects = com.ruletacaos.RuletaChaos.CONFIG.violetEffects;
        if (effects == null || effects.isEmpty()) return;

        String chosen = effects.get(random.nextInt(effects.size()));

        switch (chosen) {
            case "levitation" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§5§l[VIOLETA] §f¡Levitación!"), false);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    p.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20 * 8, 1));
                }
            }
            case "random_teleport" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§5§l[VIOLETA] §f¡Teletransporte al azar!"), false);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    double x = p.getX() + (random.nextDouble() - 0.5) * 50;
                    double z = p.getZ() + (random.nextDouble() - 0.5) * 50;
                    BlockPos pos = p.serverLevel().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(x, 0, z));
                    p.teleportTo(p.serverLevel(), pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, java.util.Set.of(), p.getYRot(), p.getXRot());
                }
            }
            case "mystery_potions" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§5§l[VIOLETA] §f¡Pociones misteriosas!"), false);
                MobEffectInstance[] potions = {
                        new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20 * 30, 1),
                        new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 30, 1),
                        new MobEffectInstance(MobEffects.REGENERATION, 20 * 20, 1),
                        new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20 * 30, 0),
                        new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20 * 40, 0),
                        new MobEffectInstance(MobEffects.INVISIBILITY, 20 * 20, 0),
                        new MobEffectInstance(MobEffects.CONFUSION, 20 * 10, 0),
                        new MobEffectInstance(MobEffects.BLINDNESS, 20 * 8, 0)
                };
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    p.addEffect(potions[random.nextInt(potions.length)]);
                }
            }
            case "glowing" -> {
                server.getPlayerList().broadcastSystemMessage(Component.literal("§5§l[VIOLETA] §f¡Todos brillan!"), false);
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    p.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 60, 0));
                }
            }
        }
    }
}
