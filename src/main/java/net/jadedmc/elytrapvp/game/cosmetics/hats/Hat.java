package net.jadedmc.elytrapvp.game.cosmetics.hats;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import net.jadedmc.elytrapvp.game.cosmetics.CosmeticType;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

/**
 * Represents an item that can be worn on a player's head.
 */
public class Hat extends Cosmetic {
    private final ElytraPvP plugin;
    private final String texture;
    private final HatCategory category;

    /**
     * Creates a hat.
     * @param id ID of the hat.
     * @param name Name of the hat.
     * @param unlockType Unlock type of the hat.
     * @param texture Texture the hat uses.
     * @param category Category of the hat.
     */
    public Hat(ElytraPvP plugin, String id, String name, CosmeticType unlockType, String texture, HatCategory category) {
        super(id, name, unlockType);
        this.plugin = plugin;
        this.texture = texture;
        this.category = category;
    }

    /**
     * Get the category of the hat.
     * @return Hat category.
     */
    public HatCategory getCategory() {
        return category;
    }

    /**
     * Gets the GUI icon for the hat.
     * @param player Player to get icon for.
     * @return Icon of the hat.
     */
    @Override
    public ItemStack getIcon(Player player) {
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Checks if the player has unlocked the hat.
        if(customPlayer.getUnlockedHats().contains(getId()) || (getUnlockType() == CosmeticType.NORMAL && getPrice() == 0)) {
            ItemStack hat = new SkullBuilder(texture)
                    .setDisplayName("&a" + getName())
                    .addLore("&7Click to equip")
                    .build();
            return hat;
        }

        // Check if the item is seasonal.
        if(getUnlockType() == CosmeticType.SEASONAL && plugin.seasonManager().getCurrentSeason() != getSeason()) {
            // If not, shows the purchase icon.
            ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                    .setDisplayName("&c" + getName())
                    .addLore(ChatPaginator.wordWrap("&7This item can only be purchased during the " + getSeason().getName() + " &7event.", 35), "&7");
            return builder.build();
        }

        // If not, shows the purchase icon.
        ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                .setDisplayName("&c" + getName())
                .addLore("&6Price: " + getPrice())
                .addLore("&7Click to purchase");
        return builder.build();
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
