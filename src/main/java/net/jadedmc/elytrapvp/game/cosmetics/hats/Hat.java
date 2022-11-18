package net.jadedmc.elytrapvp.game.cosmetics.hats;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an item that can be worn on a player's head.
 */
public class Hat extends Cosmetic {
    private final String texture;
    private final HatCategory category;

    /**
     * Creates a hat.
     * @param plugin Instance of the plugin.
     * @param id ID of the hat.
     * @param config Configuration of the hat.
     */
    public Hat(ElytraPvP plugin, String id, ConfigurationSection config) {
        super(plugin, id, config);
        setType("Hat");

        // Sets up the hat as an item.
        this.texture = config.getString("texture");
        setIconMaterial(Material.PLAYER_HEAD);
        setTexture(texture);

        // Loads the category of the hat.
        this.category = HatCategory.valueOf(config.getString("category"));
    }

    /**
     * Get the category of the hat.
     * @return Hat category.
     */
    public HatCategory getCategory() {
        return category;
    }

    /**
     * Get the texture the hat uses.
     * @return Texture the hat uses.
     */
    public String getTexture() {
        return texture;
    }

    /**
     * Get the ItemStack of the Hat.
     * @return ItemStack version of the hat.
     */
    public ItemStack toItemStack() {
        return new SkullBuilder(texture).setDisplayName("&a" + getName()).build();
    }
}