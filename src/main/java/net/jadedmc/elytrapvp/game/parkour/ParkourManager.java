package net.jadedmc.elytrapvp.game.parkour;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages players who are currently using a parkour course.
 */
public class ParkourManager {
    private final ElytraPvP plugin;
    private final Map<Player, ParkourData> players = new HashMap<>();

    /**
     * Creates the parkour manager.
     * @param plugin Instance of the plugin.
     */
    public ParkourManager(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Adds a player to a parkour course.
     * @param player Player to add.
     * @param course Course the player is using.
     */
    public void addPlayer(Player player, String course) {
        players.put(player, new ParkourData(plugin, course));
    }

    /**
     * Gets the current course a player is using.
     * @param player Player to get course of.
     * @return Course they are in. Returns "NONE" if not in a course.
     */
    public String getCourse(Player player) {
        if(players.containsKey(player)) {
            return players.get(player).getCourse();
        }

        return "NONE";
    }

    /**
     * Gets the formatted name of the course a player is in.
     * Used with chat and the scoreboard
     * @param player Player to get course of.
     * @return Course they are in.
     */
    public String getCourseName(Player player) {
        return ChatUtils.translate(plugin.settingsManager().getConfig().getString("Parkour." + getCourse(player).toUpperCase() + ".Name"));
    }

    /**
     * Get the player's current parkour timer.
     * @param player Player to get timer of.
     * @return The player's parkour timer.
     */
    public ParkourTimer getTimer(Player player) {
        if(players.containsKey(player)) {
            return players.get(player).getTimer();
        }

        return null;
    }

    /**
     * Gets a parkour timer from a set time.
     * Used for displaying best course times.
     * @param time Time in milliseconds.
     * @return Parkour Timer displaying that amount.
     */
    public ParkourTimer getTimerFromTime(long time) {
        return new ParkourTimer(plugin, time);
    }

    /**
     * Removes a player from the parkour manager.
     * Done when the player leaves the server.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    /**
     * Stores related player parkour data grouped together.
     */
    private static class ParkourData {
        private final String course;
        private final ParkourTimer timer;

        /**
         * Creates the cache object.
         * @param plugin Instance of the plugin.
         * @param course Course the player is in.
         */
        public ParkourData(ElytraPvP plugin, String course) {
            this.course = course;
            this.timer = new ParkourTimer(plugin);

            // Starts the player's parkour timer.
            timer.start();
        }

        /**
         * Gets the course the player is currently in.
         * @return Course the player is in.
         */
        public String getCourse() {
            return course;
        }

        /**
         * Gets the player's current parkour timer.
         * @return Parkour timer of the player.
         */
        public ParkourTimer getTimer() {
            return timer;
        }
    }
}