package net.jadedmc.elytrapvp.settings;

import net.jadedmc.elytrapvp.ElytraPvP;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsManager {
    private FileConfiguration config;
    private File configFile;
    private FileConfiguration arenas;
    private File arenasFile;

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