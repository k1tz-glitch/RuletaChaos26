package com.ruletacaos;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.*;
import net.minecraft.server.MinecraftServer;

public class ModifierHandler {
    public static void applyYellowModifiers(MinecraftServer server) {
        server.getWorlds().forEach(world -> {
            world.getEntities().forEach(entity -> {
                if (entity instanceof ZombieEntity z) {
                    z.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(5.0);
                } else if (entity instanceof CreeperEntity c) {
                    c.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
                }
            });
        });
    }
}
