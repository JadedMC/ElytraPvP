package net.jadedmc.elytrapvp.game.cosmetics;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.hats.Hat;
import net.jadedmc.elytrapvp.game.cosmetics.hats.HatCategory;
import net.jadedmc.elytrapvp.game.cosmetics.killmessages.KillMessage;
import net.jadedmc.elytrapvp.game.cosmetics.tags.Tag;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * Manages all cosmetics.
 */
public class CosmeticManager {
    private final ElytraPvP plugin;
    private final Map<String, Hat> hats = new LinkedHashMap();
    private final Map<String, KillMessage> killMessages = new LinkedHashMap<>();
    private final Map<String, Tag> tags = new LinkedHashMap<>();

    /**
     * Creates the manager.
     * @param plugin Instance of the plugin.
     */
    public CosmeticManager(ElytraPvP plugin) {
        this.plugin = plugin;

        // Load all cosmetics from the config.
        loadHats();
        loadKillMessages();
        loadTags();
    }

    /**
     * Load all hats from the config.
     */
    private void loadHats() {
        ConfigurationSection section = plugin.settingsManager().getHats().getConfigurationSection("Hats");

        if(section != null) {
            // Loop through each hat.
            for(String hatID : section.getKeys(false)) {
                ConfigurationSection hatConfig = section.getConfigurationSection(hatID);
                String name = hatConfig.getString("name");
                String texture = hatConfig.getString("texture");
                HatCategory category = HatCategory.valueOf(hatConfig.getString("category"));

                CosmeticType unlockType = CosmeticType.NORMAL;
                if(hatConfig.isSet("unlockType")) {
                    unlockType = CosmeticType.valueOf(hatConfig.getString("unlockType"));
                }

                int price = 50;
                if(hatConfig.isSet("price")) {
                    price = hatConfig.getInt("price");
                }

                // Loads the hat.
                Hat hat = new Hat(plugin, hatID, name, unlockType, texture, category);
                hat.setPrice(price);

                // Caches the hat.
                hats.put(hatID, hat);
            }
        }
    }

    /**
     * Load all kill messages from the config.
     */
    private void loadKillMessages() {
        ConfigurationSection section = plugin.settingsManager().getKillMessages().getConfigurationSection("KillMessages");

        if(section != null) {
            // Loop through each kill message.
            for(String id : section.getKeys(false)) {
                ConfigurationSection config = section.getConfigurationSection(id);
                String message = config.getString("Message");

                CosmeticType unlockType = CosmeticType.NORMAL;
                if(config.isSet("unlockType")) {
                    unlockType = CosmeticType.valueOf(config.getString("unlockType"));
                }

                KillMessage killMessage = new KillMessage(plugin, id, message, unlockType);

                if(config.isSet("Price")) {
                    killMessage.setPrice(config.getInt("Price"));
                }

                killMessages.put(id, killMessage);
            }
        }
    }

    /**
     * Load all tags from the config.
     */
    private void loadTags() {
        ConfigurationSection section = plugin.settingsManager().getTags().getConfigurationSection("Tags");

        if(section != null) {
            // Loop through each kill message.
            for(String id : section.getKeys(false)) {
                ConfigurationSection config = section.getConfigurationSection(id);
                String tagText = config.getString("Tag");

                CosmeticType unlockType = CosmeticType.NORMAL;
                if(config.isSet("unlockType")) {
                    unlockType = CosmeticType.valueOf(config.getString("unlockType"));
                }

                Tag tag = new Tag(plugin, id, tagText, unlockType);

                if(config.isSet("Tag")) {
                    tag.setPrice(config.getInt("Price"));
                }

                tags.put(id, tag);
            }
        }
    }

    /**
     * Get a hat based off its id.
     * @param id Id of the hat.
     * @return Corresponding hat.
     */
    public Hat getHat(String id) {
        return hats.get(id);
    }

    /**
     * Get a collection of all hats.
     * @return All hats.
     */
    public Collection<Hat> getHats() {
        return hats.values();
    }

    /**
     * Get all hats from a specific category.
     * @param category Category to get hats of.
     * @return All hats in that category.
     */
    public Collection<Hat> getHats(HatCategory category) {
        List<Hat> hatList = new ArrayList<>();

        for(Hat hat : getHats()) {
            if(hat.getCategory() == category) {
                hatList.add(hat);
            }
        }

        return hatList;
    }

    /**
     * Get a kill message based off its id.
     * @param id id of the kill message.
     * @return Corresponding kill message.
     */
    public KillMessage getKillMessage(String id) {
        return killMessages.get(id);
    }

    /**
     * Get a collection of all kill messages.
     * @return All kill messages.
     */
    public Collection<KillMessage> getKillMessages() {
        return killMessages.values();
    }

    /**
     * Get a tag based off its id.
     * @param id Id of the tag.
     * @return Corresponding tag.
     */
    public Tag getTag(String id) {
        return tags.get(id);
    }

    /**
     * Get a collection of all tags.
     * @return All tags.
     */
    public Collection<Tag> getTags() {
        return tags.values();
    }
}