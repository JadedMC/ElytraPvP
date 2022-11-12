package net.jadedmc.elytrapvp.settings;

import net.jadedmc.elytrapvp.ElytraPvP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Manages various configuration files.
 */
public class SettingsManager {
    private FileConfiguration config;
    private File configFile;
    private FileConfiguration arenas;
    private File arenasFile;
    private FileConfiguration hats;
    private File hatsFile;
    private FileConfiguration killMessages;
    private File killMessagesFile;
    private FileConfiguration tags;
    private File tagsFile;
    private FileConfiguration arrowTrails;
    private File arrowTrailsFile;

    /**
     * Loads the configuration files.
     * @param plugin Instance of the plugin.
     */
    public SettingsManager(ElytraPvP plugin) {
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(plugin.getDataFolder(), "config.yml");
        plugin.saveConfig();

        arenasFile = new File(plugin.getDataFolder(), "arenas.yml");
        arenas = YamlConfiguration.loadConfiguration(arenasFile);

        if(!arenasFile.exists()) {
            plugin.saveResource("arenas.yml", false);
        }

        hatsFile = new File(plugin.getDataFolder(), "hats.yml");
        hats = YamlConfiguration.loadConfiguration(hatsFile);

        if(!hatsFile.exists()) {
            plugin.saveResource("hats.yml", false);
        }

        killMessagesFile = new File(plugin.getDataFolder(), "killmessages.yml");
        killMessages = YamlConfiguration.loadConfiguration(killMessagesFile);

        if(!killMessagesFile.exists()) {
            plugin.saveResource("killmessages.yml", false);
        }

        tagsFile = new File(plugin.getDataFolder(), "tags.yml");
        tags = YamlConfiguration.loadConfiguration(tagsFile);

        if(!tagsFile.exists()) {
            plugin.saveResource("tags.yml", false);
        }

        arrowTrailsFile = new File(plugin.getDataFolder(), "arrowtrails.yml");
        arrowTrails = YamlConfiguration.loadConfiguration(arrowTrailsFile);

        if(!arrowTrailsFile.exists()) {
            plugin.saveResource("arrowtrails.yml", false);
        }
    }

    /**
     * Get the arenas configuration file.
     * @return Arenas configuration file.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the main configuration file.
     * @return Main configuration file.
     */
    public FileConfiguration getArenas() {
        return arenas;
    }

    /**
     * Get the arrow trail configuration file.
     * @return Arrow trail configuration file.
     */
    public FileConfiguration getArrowTrails() {
        return arrowTrails;
    }

    /**
     * Get the hats configuration file.
     * @return Hats configuration file.
     */
    public FileConfiguration getHats() {
        return hats;
    }

    /**
     * Get the kill messages configuration file.
     * @return Kill Messages configuration.
     */
    public FileConfiguration getKillMessages() {
        return killMessages;
    }

    /**
     * Get the tags configuration file.
     * @return Tags configuration.
     */
    public FileConfiguration getTags() {
        return tags;
    }

    /**
     * Allows us to save the arenas file after changes are made.
     */
    public void saveArenas() {
        try {
            arenas.save(arenasFile);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Allows us to save the config file after changes are made.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        }
        catch(IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Updates the arenas configuration in case changes are made.
     */
    public void reloadArenas() {
        saveArenas();
        arenas = YamlConfiguration.loadConfiguration(arenasFile);
    }

    /**
     * This updates the config in case changes are made.
     */
    public void reloadConfig() {
        saveConfig();
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}