package net.jadedmc.elytrapvp.game.cosmetics;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an unlockable item that has no gameplay changes.
 */
public abstract class Cosmetic {
    private final String id;
    private final String name;
    private final CosmeticType unlockType;

    // Unlock Values
    private int price = 0;

    /**
     * Creates a Cosmetic.
     * @param id ID of the cosmetic.
     * @param name Name of the cosmetic.
     * @param unlockType Type of the cosmetic.
     */
    public Cosmetic(String id, String name, CosmeticType unlockType) {
        this.id = id;
        this.name = name;
        this.unlockType = unlockType;
    }

    /**
     * Gets the GUI icon for the cosmetic.
     * @param player Player to get icon for.
     * @return GUI icon ItemStack.
     */
    public abstract ItemStack getIcon(Player player);

    /**
     * Get the id of the cosmetic.
     * @return Cosmetic id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the name of the cosmetic.
     * @return Cosmetic name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the price of the cosmetic.
     * @return Cosmetic price.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Get the way the cosmetic should be unlocked.
     * @return Unlock type.
     */
    public CosmeticType getUnlockType() {
        return unlockType;
    }

    /**
     * Set the price of the cosmetic.
     * @param price New price of the cosmetic.
     */
    public void setPrice(int price) {
        this.price = price;
    }
}