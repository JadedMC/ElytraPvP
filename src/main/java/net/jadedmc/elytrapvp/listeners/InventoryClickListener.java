package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * This class runs every time an inventory is clicked.
 * We use this to prevent players from moving items in their inventory.
 */
public class InventoryClickListener implements Listener {
    private final ElytraPvP plugin;

    /**
     * Creates the Listener.
     * @param plugin Instance of the plugin.
     */
    public InventoryClickListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlayerDropItemEvent.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        // Makes sure it was actually a player who clicked the inventory.
        if(!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        // Allow players in creative mode to move items around, in case they are building.
        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        // Check the current status of the player.
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        switch (customPlayer.getStatus()) {
            // Runs if the player is in the lobby.
            case LOBBY -> {
                // Makes sure the clicked inventory is not null.
                if(event.getClickedInventory() == null) {
                    return;
                }

                // Checks if the clicked inventory is their own. If so, cancels the event.
                if(event.getClickedInventory().getType() == InventoryType.PLAYER) {
                    event.setCancelled(true);
                }
            }

            // Runs when the player is in the arena.
            case ARENA -> {
                // Checks if the click action is forbidden. If so, cancels the event.
                switch (event.getAction()) {
                    case PLACE_ONE, PLACE_SOME, PICKUP_ONE, PICKUP_SOME, PICKUP_HALF -> event.setCancelled(true);
                }
            }
        }
    }
}
