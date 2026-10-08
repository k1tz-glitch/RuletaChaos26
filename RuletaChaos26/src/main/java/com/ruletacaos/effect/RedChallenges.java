package com.ruletacaos.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class RedChallenges {

    public static ActiveChallenge create(String id, int durationSeconds, MinecraftServer server) {
        return switch (id) {
            case "height_250" -> new HeightChallenge(durationSeconds, 250);
            case "y_minus_50" -> new DepthChallenge(durationSeconds, -50);
            case "life_drain" -> new LifeDrainChallenge(durationSeconds);
            case "creeper_spawn" -> new CreeperSpawnChallenge(durationSeconds);
            case "zombie_skeleton_horde" -> new HordeChallenge(durationSeconds);
            case "meteor_rain" -> new MeteorChallenge(durationSeconds);
            case "no_standing_still" -> new NoStillChallenge(durationSeconds);
            case "extreme_hunger" -> new HungerChallenge(durationSeconds);
            case "lightning_storm" -> new LightningChallenge(durationSeconds);
            default -> null;
        };
    }

    static class HeightChallenge extends ActiveChallenge {
        private final int targetY;
        public HeightChallenge(int seconds, int targetY) {
            super("Sube a altura " + targetY, seconds);
            this.targetY = targetY;
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Debes alcanzar Y=" + targetY + " o perderás vida!"), false);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 40 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    if (p.getY() < targetY) {
                        p.hurt(p.damageSources().generic(), 1.0f);
                        p.sendSystemMessage(Component.literal("§c¡Sube a Y=" + targetY + "!"), true);
                    }
                }
            }
        }
        @Override public void end(MinecraftServer server) {}
    }

    static class DepthChallenge extends ActiveChallenge {
        private final int targetY;
        public DepthChallenge(int seconds, int targetY) {
            super("Baja a Y=" + targetY, seconds);
            this.targetY = targetY;
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Debes bajar a Y=" + targetY + "!"), false);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 40 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    if (p.getY() > targetY) {
                        p.hurt(p.damageSources().generic(), 1.0f);
                        p.sendSystemMessage(Component.literal("§c¡Baja a Y=" + targetY + "!"), true);
                    }
                }
            }
        }
        @Override public void end(MinecraftServer server) {}
    }

    static class LifeDrainChallenge extends ActiveChallenge {
        public LifeDrainChallenge(int seconds) {
            super("Drenaje de vida", seconds);
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Tu vida se drena lentamente!"), false);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 30 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    p.hurt(p.damageSources().magic(), 1.0f);
                }
            }
        }
        @Override public void end(MinecraftServer server) {}
    }

    static class CreeperSpawnChallenge extends ActiveChallenge {
        private final Random rand = new Random();
        public CreeperSpawnChallenge(int seconds) {
            super("Creepers a tu lado", seconds);
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Aparecen creepers cerca de ti!"), false);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 100 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    ServerLevel level = p.serverLevel();
                    for (int i = 0; i < 2; i++) {
                        double x = p.getX() + (rand.nextDouble() - 0.5) * 8;
                        double z = p.getZ() + (rand.nextDouble() - 0.5) * 8;
                        BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(x, 0, z));
                        Creeper creeper = EntityType.CREEPER.create(level);
                        if (creeper != null) {
                            creeper.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                            level.addFreshEntity(creeper);
                        }
                    }
                }
            }
        }
        @Override public void end(MinecraftServer server) {}
    }

    static class HordeChallenge extends ActiveChallenge {
        private final Random rand = new Random();
        public HordeChallenge(int seconds) {
            super("Horda de zombies y esqueletos", seconds);
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Horda entrante!"), false);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 80 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    ServerLevel level = p.serverLevel();
                    for (int i = 0; i < 4; i++) {
                        double x = p.getX() + (rand.nextDouble() - 0.5) * 12;
                        double z = p.getZ() + (rand.nextDouble() - 0.5) * 12;
                        BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(x, 0, z));
                        if (rand.nextBoolean()) {
                            Zombie z = EntityType.ZOMBIE.create(level);
                            if (z != null) {
                                z.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                                level.addFreshEntity(z);
                            }
                        } else {
                            Skeleton s = EntityType.SKELETON.create(level);
                            if (s != null) {
                                s.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 0, 0);
                                level.addFreshEntity(s);
                            }
                        }
                    }
                }
            }
        }
        @Override public void end(MinecraftServer server) {}
    }

    static class MeteorChallenge extends ActiveChallenge {
        private final Random rand = new Random();
        public MeteorChallenge(int seconds) {
            super("Lluvia de meteoritos", seconds);
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Lluvia de meteoritos!"), false);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 25 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    ServerLevel level = p.serverLevel();
                    double x = p.getX() + (rand.nextDouble() - 0.5) * 20;
                    double z = p.getZ() + (rand.nextDouble() - 0.5) * 20;
                    double y = p.getY() + 30 + rand.nextDouble() * 10;
                    level.explode(null, x, y, z, 2.5f, false, net.minecraft.world.level.Level.ExplosionInteraction.TNT);
                }
            }
        }
        @Override public void end(MinecraftServer server) {}
    }

    static class NoStillChallenge extends ActiveChallenge {
        public NoStillChallenge(int seconds) {
            super("No quedarte quieto", seconds);
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡No te quedes quieto o te hará daño!"), false);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 20 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    Vec3 vel = p.getDeltaMovement();
                    if (vel.horizontalDistanceSqr() < 0.01) {
                        p.hurt(p.damageSources().generic(), 2.0f);
                        p.sendSystemMessage(Component.literal("§c¡Muévete!"), true);
                    }
                }
            }
        }
        @Override public void end(MinecraftServer server) {}
    }

    static class HungerChallenge extends ActiveChallenge {
        public HungerChallenge(int seconds) {
            super("Hambre extrema", seconds);
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Hambre extrema!"), false);
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                p.addEffect(new MobEffectInstance(MobEffects.HUNGER, durationTicks, 4));
                p.getFoodData().setFoodLevel(6);
            }
        }
        @Override public void onTick(MinecraftServer server) {}
        @Override public void end(MinecraftServer server) {
            for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                p.removeEffect(MobEffects.HUNGER);
            }
        }
    }

    static class LightningChallenge extends ActiveChallenge {
        private final Random rand = new Random();
        public LightningChallenge(int seconds) {
            super("Tormenta de rayos", seconds);
        }
        @Override public void start(MinecraftServer server) {
            server.getPlayerList().broadcastSystemMessage(Component.literal("§c¡Tormenta de rayos!"), false);
            server.overworld().setWeatherParameters(0, 6000, true, true);
        }
        @Override public void onTick(MinecraftServer server) {
            if (ticksLeft % 40 == 0) {
                for (ServerPlayer p : server.getPlayerList().getPlayers()) {
                    ServerLevel level = p.serverLevel();
                    double x = p.getX() + (rand.nextDouble() - 0.5) * 16;
                    double z = p.getZ() + (rand.nextDouble() - 0.5) * 16;
                    BlockPos pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(x, 0, z));
                    LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
                    if (bolt != null) {
                        bolt.moveTo(pos.getX(), pos.getY(), pos.getZ());
                        level.addFreshEntity(bolt);
                    }
                }
            }
        }
        @Override public void end(MinecraftServer server) {
            server.overworld().setWeatherParameters(6000, 0, false, false);
        }
    }
}
