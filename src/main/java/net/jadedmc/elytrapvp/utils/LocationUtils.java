package net.jadedmc.elytrapvp.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * A collection of tools to help deal with locations.
 */
public class LocationUtils {

    /**
     * Gets a location from a config file.
     * @param config File to get location from.
     * @param path Path to the location.
     * @return Location stored in the file.
     */
    public static Location fromConfig(FileConfiguration config, String path) {
        String world = config.getString(path + ".World");
        double x = config.getDouble(path + ".X");
        double y = config.getDouble(path + ".Y");
        double z = config.getDouble(path + ".Z");

        // Exists if there are no rotation values.
        if(!config.isSet(path + ".Yaw")) {
            return new Location(Bukkit.getWorld(world), x, y, z);
        }

        float yaw = (float) config.getDouble(path + ".Yaw");
        float pitch = (float) config.getDouble(path + ".Pitch");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}