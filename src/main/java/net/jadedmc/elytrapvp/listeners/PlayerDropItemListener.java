package net.jadedmc.elytrapvp.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * This class runs a listener that is called every time a player drops an item.
 * This is used to prevent them from dropping lobby/game items.
 */
public class PlayerDropItemListener implements Listener {

    /**
     * Runs when the event is called.
     * @param event PlayerDropItemEvent.
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {

        // Allows players in creative mode to still drops items.
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        event.setCancelled(true);
    }
}