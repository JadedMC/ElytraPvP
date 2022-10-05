package net.jadedmc.elytrapvp.game.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Represents a collection of items and stats
 * used in the game.
 */
public abstract class Kit {
    private final String name;
    private String description;
    private final int id;
    private int price;
    private int health;
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final Collection<PotionEffect> effects = new HashSet<>();
    private Material icon;

    /**
     * Registers the kit.
     * @param name Name of the kit.
     * @param id Numerical id of the kit.
     */
    public Kit(String name, int id) {
        this.name = name;
        this.id = id;

        // Set kit defaults.
        health = 20;
        price = 400;
        icon = Material.BARRIER;
        description = "MISSING!";
    }

    /**
     * Apply a kit to a player.
     * @param player Player to apply kit to.
     */
    public void apply(Player player) {
        player.getInventory().clear();
        player.setMaxHealth(health);
        player.setHealth(health);

        // Add the potion effects.
        effects.forEach(player::addPotionEffect);

        // Add the items.
        for(int slot : items.keySet()) {
            ItemStack item = items.get(slot);
            player.getInventory().setItem(slot, item);
        }
    }

    /**
     * Add an effect to the kit.
     * @param effect Potion Effect to add.
     */
    public void addEffect(PotionEffect effect) {
        effects.add(effect);
    }

    /**
     * Add an item to add.
     * @param item Item to add.
     * @param slot Slot the item is in.
     */
    public void addItem(int slot, ItemStack item) {
        items.put(slot, item);
    }

    /**
     * Get the description of the kit.
     * @return Description of the kit.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the potion effects of the kit.
     * @return Potion effects of the kit.
     */
    public Collection<PotionEffect> getEffects() {
        return effects;
    }

    /**
     * Get the health of the kit,
     * @return Health of the kit.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get the icon of the kit.
     * @return Icon of the kit.
     */
    public Material getIcon() {
        return icon;
    }

    /**
     * Get the id of the kit.
     * @return Id of the kit.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the items the kit has.
     * @return Items of the kit.
     */
    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    /**
     * Set the icon of the kit.
     * @param icon New icon.
     */
    public void setIcon(Material icon) {
        this.icon = icon;
    }

    /**
     * Get the name of the kit.
     * @return Name of the kit.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the price of the kit.
     * @return Price of the kit.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Set the description of the kit,
     * @param description New description of the kit.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the health of the kit.
     * @param health New health of the kit.
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Set the price of the kit.
     * @param price New price of the kit.
     */
    public void setPrice(int price) {
        this.price = price;
    }
}