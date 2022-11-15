package net.jadedmc.elytrapvp.game.cosmetics;

import net.jadedmc.elytrapvp.game.seasons.Season;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an unlockable item that has no gameplay changes.
 */
public abstract class Cosmetic {
    private final String id;
    private String name;
    private CosmeticType unlockType;

    // Unlock Values
    private int price = 0;
    private Season season = Season.NONE;

    public Cosmetic(String id) {
        this.id = id;
        this.name = "";
        this.unlockType = CosmeticType.NORMAL;
    }

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
     * Get the season required to purchase the cosmetic.
     * @return Required season.
     */
    public Season getSeason() {
        return season;
    }

    /**
     * Get the way the cosmetic should be unlocked.
     * @return Unlock type.
     */
    public CosmeticType getUnlockType() {
        return unlockType;
    }

    /**
     * Set the name of the cosmetic.
     * @param name Name of the cosmetic.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the price of the cosmetic.
     * @param price New price of the cosmetic.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Sets the season needed to be able to purchase a cosmetic.
     * @param season Season needed to unlock cosmetic.
     */
    public void setSeason(Season season) {
        this.season = season;
    }

    /**
     * Set the unlock type of the cosmetic.
     * @param unlockType Unlock type of the cosmetic.
     */
    public void setUnlockType(CosmeticType unlockType) {
        this.unlockType = unlockType;
    }
}