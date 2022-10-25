package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.KitSelectorGUI;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    private final ElytraPvP plugin;

    public InventoryClickListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    public void onClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if(customPlayer.getStatus() == Status.ARENA) {
            switch (event.getAction()) {
                case PLACE_ONE, PLACE_SOME, PICKUP_ONE, PICKUP_SOME, PICKUP_HALF -> {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if(event.getCurrentItem() == null) {
            return;
        }

        switch (event.getCurrentItem().getType()) {
            case PLAYER_HEAD:
            case EMERALD:
            case BOOK:
            case NETHER_STAR:
                new KitSelectorGUI(plugin, player).open(player);
                break;

            default:
                return;
        }

        if(customPlayer.getStatus() == Status.LOBBY) {
            event.setCancelled(true);
        }
    }
}
