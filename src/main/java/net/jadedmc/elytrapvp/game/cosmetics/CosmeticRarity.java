package net.jadedmc.elytrapvp.game.cosmetics;

import net.jadedmc.elytrapvp.utils.chat.ChatUtils;

/**
 * Represents how rare a cosmetic is.
 * Used as a way to explain differences in prices between cosmetics.
 */
public enum CosmeticRarity {
    COMMON("&f"),
    UNCOMMON("&a"),
    RARE("&9"),
    EPIC("&5"),
    LEGENDARY("&6");

    private final String color;

    /**
     * Creates the rarity.
     * @param color Color code of the rarity.
     */
    CosmeticRarity(String color) {
        this.color = color;
    }

    /**
     * Gets the color code of the rarity.
     * @return Color code of the rarity.
     */
    public String getColor() {
        return color;
    }

    /**
     * Converts the rarity to a string.
     * @return String form of the rarity.
     */
    public String toString() {
        return ChatUtils.translate(color + name());
    }
}
