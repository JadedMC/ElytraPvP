package net.jadedmc.elytrapvp.utils.scoreboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a scoreboard that is easy
 * to create and customize.
 * @author crisdev333
 */
public abstract class CustomScoreboard {
    private static final HashMap<UUID, CustomScoreboard> players = new HashMap<>();

    /**
     * Creates the scoreboard.
     * @param player Player the scoreboard is created for.
     */
    public CustomScoreboard(Player player) {
        ScoreHelper.removeScore(player);
        players.put(player.getUniqueId(), this);
    }

    /**
     * Gets all players and their scoreboards.
     * @return Map of each player and their custom scoreboard.
     */
    public static HashMap<UUID, CustomScoreboard> getPlayers() {
        return players;
    }

    /**
     * Updates the scoreboard of a player.
     * @param player Player to update scoreboard of.
     */
    public abstract void update(Player player);
}