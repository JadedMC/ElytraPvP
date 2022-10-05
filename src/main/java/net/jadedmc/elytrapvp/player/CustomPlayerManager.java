package net.jadedmc.elytrapvp.player;

import net.jadedmc.elytrapvp.ElytraPvP;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the creation and access of
 * Custom Player objects.
 */
public class CustomPlayerManager {
    private final ElytraPvP plugin;
    private final Map<UUID, CustomPlayer> players;

    /**
     * Creates the Manager.
     * @param plugin Plugin instance.
     */
    public CustomPlayerManager(ElytraPvP plugin) {
        this.plugin = plugin;
        this.players = new HashMap<>();
    }

    /**
     * Creates a Custom Player from a Player.
     * @param player Player.
     */
    public void addPlayer(Player player) {
        this.players.put(player.getUniqueId(), new CustomPlayer(plugin, player.getUniqueId()));
    }

    /**
     * Get the Custom Player version of a Player.
     * @param player Player.
     * @return Custom Player.
     */
    public CustomPlayer getPlayer(Player player) {
        if(this.players.containsKey(player.getUniqueId())) {
            return this.players.get(player.getUniqueId());
        }

        return null;
    }

    /**
     * Get the Custom Player version of an Offline Player.
     * @param player Offline Player.
     * @return Custom Player.
     */
    public CustomPlayer getPlayer(OfflinePlayer player) {
        if(this.players.containsKey(player.getUniqueId())) {
            return getPlayer(player);
        }

        return new CustomPlayer(plugin, player.getUniqueId());
    }

    /**
     * Remove a player from the custom player list.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        this.players.remove(player.getUniqueId());
    }
}