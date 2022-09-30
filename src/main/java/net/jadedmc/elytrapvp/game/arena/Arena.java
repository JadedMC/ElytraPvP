package net.jadedmc.elytrapvp.game.arena;

import net.jadedmc.elytrapvp.ElytraPvP;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an area in which the game is played.
 */
public class Arena {
    private String name;
    private final List<Location> bounds = new ArrayList<>();
    private Location spawn;
    private List<Material> deathBlocks = new ArrayList<>();

    /**
     * Loads an arena from arenas.yml
     * @param plugin Instance of the plugin.
     * @param id Id of the arena being loaded.
     */
    public Arena(ElytraPvP plugin, String id) {
        FileConfiguration settings = plugin.settingsManager().getArenas();
        String path = "Arenas." + id + ".";

        // Name
        name = settings.getString(path + "Name");

        // Spawn
        {
            String location = path + "Spawn.";
            World world = Bukkit.getWorld(settings.getString(location + "World"));
            double x = settings.getDouble(location + "X");
            double y = settings.getDouble(location + "Y");
            double z = settings.getDouble(location + "Z");
            float yaw = (float) settings.getDouble(location + "Yaw");
            float pitch = (float) settings.getDouble(location + "Pitch");;

            spawn = new Location(world, x, y, z, yaw, pitch);
        }

        // Bounds
        // Pos1
        {
            String location = path + "Bounds.Pos1.";
            World world = Bukkit.getWorld(settings.getString(location + "World"));
            double x = settings.getDouble(location + "X");
            double y = settings.getDouble(location + "Y");
            double z = settings.getDouble(location + "Z");
            float yaw = (float) settings.getDouble(location + "Yaw");
            float pitch = (float) settings.getDouble(location + "Pitch");;

            bounds.add(new Location(world, x, y, z, yaw, pitch));
        }

        // Pos2
        {
            String location = path + "Bounds.Pos2.";
            World world = Bukkit.getWorld(settings.getString(location + "World"));
            double x = settings.getDouble(location + "X");
            double y = settings.getDouble(location + "Y");
            double z = settings.getDouble(location + "Z");
            float yaw = (float) settings.getDouble(location + "Yaw");
            float pitch = (float) settings.getDouble(location + "Pitch");;

            bounds.add(new Location(world, x, y, z, yaw, pitch));
        }

        // Death Blocks
        for(String material : settings.getStringList(path + "DeathBlocks")) {
            deathBlocks.add(Material.valueOf(material));
        }
    }

    /**
     * Get the bounds of the Arena.
     * @return Bounds of the Arena.
     */
    public List<Location> getBounds() {
        return bounds;
    }

    /**
     * Get the Death Blocks of the arena.
     * @return Death Blocks.
     */
    public List<Material> getDeathBlocks() {
        return deathBlocks;
    }

    /**
     * Get the name of the Arena.
     * @return Name of the arena.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the spawn of the arena.
     * @return Spawn of the arena.
     */
    public Location getSpawn() {
        return spawn;
    }

    /**
     * Check if a player is in the arena.
     * @param player Player to check.
     * @return Whether or not they are in the arena.
     */
    public boolean hasPlayer(Player player) {
        int x1 = bounds.get(0).getBlockX();
        int y1 = bounds.get(0).getBlockY();
        int z1 = bounds.get(0).getBlockZ();
        int x2 = bounds.get(1).getBlockX();
        int y2 = bounds.get(1).getBlockY();
        int z2 = bounds.get(1).getBlockZ();

        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);

        double px = player.getLocation().getX();
        double py = player.getLocation().getY();
        double pz = player.getLocation().getZ();


        if((px > x1 && px < x2) && (py > y1 && py < y2) && (pz > z1 && pz < z2)) {
            return true;
        }

        return px >= minX && px <= maxX &&
                py >= minY && py <= maxY &&
                pz >= minZ && pz <= maxZ;
    }
}