package net.jadedmc.elytrapvp.listeners;

import com.viaversion.viaversion.api.Via;
import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.GameScoreboard;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.elytrapvp.utils.item.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This class runs a listener that is called whenever a player joins.
 * This teleports the player to spawn, reads and caches data from MySQL, and other tasks.
 */
public class PlayerJoinListener implements Listener {
    private final ElytraPvP plugin;

    /**
     * Creates the Listener.
     * @param plugin Instance of the plugin.
     */
    public PlayerJoinListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlayerJoinEvent.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.customPlayerManager().addPlayer(player);

        // Apply the Custom Scoreboard
        new GameScoreboard(plugin, player);

        player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
        ItemUtils.giveLobbyItems(plugin, player);
        player.setGameMode(GameMode.ADVENTURE);

        // If the player is on an older version, warn them that the mode wont work.
        if(Via.getAPI().getPlayerVersion(player.getUniqueId()) < 316) {
            ChatUtils.chat(player, "&c&lThis mode requires 1.11.2 or newer!");
        }
    }
}