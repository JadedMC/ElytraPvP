package net.jadedmc.elytrapvp.game.cosmetics;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.hats.Hat;
import net.jadedmc.elytrapvp.game.cosmetics.hats.HatCategory;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * Manages all cosmetics.
 */
public class CosmeticManager {
    private final ElytraPvP plugin;
    private final Map<String, Hat> hats = new LinkedHashMap();

    /**
     * Creates the manager.
     * @param plugin Instance of the plugin.
     */
    public CosmeticManager(ElytraPvP plugin) {
        this.plugin = plugin;

        // Load all cosmetics from the config.
        loadHats();
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
}