package com.ruletacaos;

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.server.MinecraftServer;
import java.util.Random;

public class EventManager {
    private static final String[] COLORS = {"Rojo", "Amarillo", "Azul", "Verde", "Naranja", "Rosa", "Violeta", "Celeste"};
    private static final Random random = new Random();

    public static String spin(MinecraftServer server) {
        String selectedColor = COLORS[random.nextInt(COLORS.length)];
        sendNotification(server, selectedColor);
        executeEvent(server, selectedColor);
        return selectedColor;
    }

    private static void sendNotification(MinecraftServer server, String color) {
        Formatting format = getFormattingForColor(color);
        Text message = Text.literal("§lNOTIFICACION: §r")
                .append(Text.literal("Salió el color " + color).formatted(format));
        server.getPlayerManager().getPlayerList().forEach(p -> p.sendMessage(message, false));
    }

    public static void executeEvent(MinecraftServer server, String color) {
        switch (color) {
            case "Rojo" -> ChallengeHandler.startRandomRedChallenge(server);
            case "Amarillo" -> ModifierHandler.applyYellowModifiers(server);
            case "Azul" -> RewardHandler.spawnBlueChest(server);
            case "Verde" -> RewardHandler.giveGreenPrize(server);
            case "Naranja" -> ChaosHandler.triggerOrangeChaos(server);
            case "Rosa" -> ChaosHandler.spawnPinkAllies(server);
            case "Violeta" -> ChaosHandler.applyVioletMagic(server);
            case "Celeste" -> WorldHandler.changeWorldClimate(server);
        }
    }

    private static Formatting getFormattingForColor(String color) {
        return switch (color) {
            case "Rojo" -> Formatting.RED;
            case "Amarillo" -> Formatting.YELLOW;
            case "Azul" -> Formatting.BLUE;
            case "Verde" -> Formatting.GREEN;
            case "Naranja" -> Formatting.GOLD;
            case "Rosa" -> Formatting.LIGHT_PURPLE;
            case "Violeta" -> Formatting.DARK_PURPLE;
            case "Celeste" -> Formatting.AQUA;
            default -> Formatting.WHITE;
        };
    }
}
