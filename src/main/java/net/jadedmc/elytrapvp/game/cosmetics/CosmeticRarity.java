package net.jadedmc.elytrapvp.game.cosmetics;

import net.jadedmc.elytrapvp.utils.chat.ChatUtils;

public enum CosmeticRarity {
    COMMON("&f"),
    UNCOMMON("&a"),
    RARE("&9"),
    EPIC("&5"),
    LEGENDARY("&6");

    private final String color;

    CosmeticRarity(String color) {
        this.color = color;
    }

    public String toString() {
        return ChatUtils.translate(color + name());
    }
}
