package com.ruletacaos.roulette;

import net.minecraft.ChatFormatting;

public enum RouletteColor {
    RED("Rojo", ChatFormatting.RED, "§c", "Desafío"),
    YELLOW("Amarillo", ChatFormatting.YELLOW, "§e", "Cambio Permanente"),
    BLUE("Azul", ChatFormatting.BLUE, "§9", "Cofre Especial"),
    GREEN("Verde", ChatFormatting.GREEN, "§a", "Premio de Items"),
    ORANGE("Naranja", ChatFormatting.GOLD, "§6", "Caos"),
    PINK("Rosa", ChatFormatting.LIGHT_PURPLE, "§d", "Aliados"),
    VIOLET("Violeta", ChatFormatting.DARK_PURPLE, "§5", "Magia"),
    CYAN("Celeste", ChatFormatting.AQUA, "§b", "Clima y Mundo");

    public final String displayName;
    public final ChatFormatting formatting;
    public final String colorCode;
    public final String category;

    RouletteColor(String displayName, ChatFormatting formatting, String colorCode, String category) {
        this.displayName = displayName;
        this.formatting = formatting;
        this.colorCode = colorCode;
        this.category = category;
    }
}
