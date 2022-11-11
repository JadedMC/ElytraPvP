package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.DeathType;
import net.jadedmc.elytrapvp.player.Status;
import net.jadedmc.elytrapvp.utils.item.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * This class runs a listener that is called whenever a player respawns.
 * This sets the player to lobby mode and updates their inventory.
 */
public class PlayerRespawnListener implements Listener {
    private final ElytraPvP plugin;

    /**
     * Creates the Listener.
     * @param plugin Instance of the plugin.
     */
    public PlayerRespawnListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlayerRespawnEvent.
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        customPlayer.setStatus(Status.LOBBY);
        customPlayer.setDeathType(DeathType.NONE);
        event.setRespawnLocation(plugin.arenaManager().getSelectedArena().getSpawn());

        ItemUtils.giveLobbyItems(plugin, player);
    }
}