package com.ruletacaos.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class RuletaChaosClient implements ClientModInitializer {

    private static KeyMapping hideNotificationKey;
    public static boolean hideNotifications = false;

    @Override
    public void onInitializeClient() {
        hideNotificationKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.ruletacaos.hide",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "category.ruletacaos"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (hideNotificationKey.consumeClick()) {
                hideNotifications = !hideNotifications;
                if (client.player != null) {
                    client.player.displayClientMessage(
                            net.minecraft.network.chat.Component.literal(hideNotifications ?
                                    "§7Notificaciones de ruleta: §cOCULTAS" :
                                    "§7Notificaciones de ruleta: §aVISIBLES"),
                            true
                    );
                }
            }
        });
    }
}
