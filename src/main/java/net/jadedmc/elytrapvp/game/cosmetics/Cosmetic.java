package net.jadedmc.elytrapvp.game.cosmetics;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.seasons.Season;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

/**
 * Represents an unlockable item that has no gameplay changes.
 */
public abstract class Cosmetic {
    private ElytraPvP plugin;
    private final String id;
    private String name;
    private CosmeticUnlockType unlockType = CosmeticUnlockType.NORMAL;
    private String type = "UNKNOWN";
    private Material iconMaterial = Material.BARRIER;
    private String texture = "";
    private String description = "";

    // Unlock Values
    private int price = 0;
    private Season season = Season.NONE;

    /**
     * Creates a cosmetic.
     * @param plugin Instance of the plugin.
     * @param id Id of the
     * @param config Configuration of the cosmetic.
     */
    public Cosmetic(ElytraPvP plugin, String id, ConfigurationSection config) {
        this.plugin = plugin;
        this.id = id;
        this.name = config.getString("name");

        // Load the price of the cosmetic if it's set.
        if(config.isSet("price")) {
            price = config.getInt("price");
        }

        // Load the unlock type of the cosmetic if it's set.
        if(config.isSet("unlockType")) {
            unlockType = CosmeticUnlockType.valueOf(config.getString("unlockType"));
        }

        // Load the required season of the cosmetic if set.
        if(config.isSet("season")) {
            season = Season.valueOf(config.getString("season"));
        }

        // Load the icon material.
        if(config.isSet("icon.material")) {
            this.iconMaterial = Material.valueOf(config.getString("icon.material"));

            // Loads the player head texture if applicable.
            if(iconMaterial == Material.PLAYER_HEAD && config.isSet("icon.texture")) {
                this.texture = config.getString("icon.texture");
            }
            else {
                this.texture = "";
            }
        }

        // Set the description of the cosmetic.
        if(config.isSet("description")) {
            this.description = config.getString("description");
        }
    }

    /**
     * Creates a Cosmetic.
     * @param id ID of the cosmetic.
     * @param name Name of the cosmetic.
     * @param unlockType Type of the cosmetic.
     */
    public Cosmetic(String id, String name, CosmeticUnlockType unlockType) {
        this.id = id;
        this.name = name;
        this.unlockType = unlockType;
    }

    /**
     * Gets the GUI icon for the cosmetic.
     * @param player Player to get icon for.
     * @return GUI icon ItemStack.
     */
    public ItemStack getIcon(Player player) {
        if(isUnlocked(player)) {
            // Checks if the icon is a player head. If so, add texture.
            if(iconMaterial == Material.PLAYER_HEAD) {
                SkullBuilder builder = new SkullBuilder(texture)
                        .setDisplayName("&a" + getName())
                        .addLore("&8" + type)
                        .addLore("");

                        if(!description.equals("")) {
                            builder.addLore(ChatPaginator.wordWrap(description, 35), "&7").addLore("");
                        }

                        builder.addLore("&aClick to equip")
                        .build();
                return builder.build();
            }

            // If not, return normal item.
            return new ItemBuilder(iconMaterial)
                    .addLore("&8" + type)
                    .addLore("")
                    .setDisplayName("&a" + getName())
                    .addLore("&aClick to equip")
                    .build();
        }

        // If not, shows the purchase icon.
        ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                .setDisplayName("&c" + getName())
                .addLore("&8" + type)
                .addLore("")
                .addLore("&6Price: " + getPrice());

        if( getSeason() != Season.NONE && plugin.seasonManager().getCurrentSeason() != getSeason()) {
            builder.addLore(ChatPaginator.wordWrap("&7This item can only be purchased during the " + getSeason().getName() + " &7event.", 35), "&7");
        }
        else {
            builder.addLore("&7Click to purchase");
        }

        return builder.build();
    }

    /**
     * Check if a player has the cosmetic unlocked.
     * @param player Player to check.
     * @return Whether they have the cosmetic unlocked.
     */
    public boolean isUnlocked(Player player) {
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        if(unlockType == CosmeticUnlockType.NORMAL && price == 0) {
            return true;
        }

        switch (type) {
            case "Arrow Trail" -> {
                return customPlayer.getUnlockedArrowTrails().contains(id);
            }

            case "Hat" -> {
                return customPlayer.getUnlockedHats().contains(id);
            }

            case "Kill Message" -> {
                return customPlayer.getUnlockedKillMessages().contains(id);
            }

            case "Tag" -> {
                return customPlayer.getUnlockedTags().contains(id);
            }

            case "Trail" -> {
                return customPlayer.getUnlockedTrails().contains(id);
            }
        }

        return false;
    }

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
     * Get the type of the cosmetic.
     * @return Cosmetic Type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get the way the cosmetic should be unlocked.
     * @return Unlock type.
     */
    public CosmeticUnlockType getUnlockType() {
        return unlockType;
    }

    /**
     * Set the icon material of the player.
     * @param iconMaterial Icon material.
     */
    public void setIconMaterial(Material iconMaterial) {
        this.iconMaterial = iconMaterial;
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
     * Sets the texture of the cosmetic's shop icon.
     * @param texture Texture of the cosmetic's icon.
     */
    public void setTexture(String texture) {
        this.texture = texture;
    }

    /**
     * Sets the type of the cosmetic.
     * @param type Type of the cosmetic.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set the unlock type of the cosmetic.
     * @param unlockType Unlock type of the cosmetic.
     */
    public void setUnlockType(CosmeticUnlockType unlockType) {
        this.unlockType = unlockType;
    }
}