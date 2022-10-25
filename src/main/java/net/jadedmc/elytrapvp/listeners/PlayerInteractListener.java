package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.KitSelectorGUI;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private final ElytraPvP plugin;

    public PlayerInteractListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        if(player.getOpenInventory().getType() != InventoryType.CRAFTING && player.getOpenInventory().getType() != InventoryType.CREATIVE) {
            if(player.getName().contains("*")) {
                if(player.getOpenInventory().getType() != InventoryType.PLAYER) {
                    event.setCancelled(true);
                    return;
                }
            }

            event.setCancelled(true);
            return;
        }

        // Exit if player is in other mode.
        if(customPlayer.getStatus() == Status.OTHER)
            return;

        // Exit if the item is null.
        if(event.getItem() == null)
            return;

        // Replace fireworks when used.
        if(event.getItem().getType() == Material.FIREWORK_ROCKET) {
            event.getItem().setAmount(64);
        }

        // Exit if item meta is null.
        if(event.getItem().getItemMeta() == null)
            return;

        String item = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());
        switch (item) {
            case "Kits" -> {
                new KitSelectorGUI(plugin, player).open(player);
                event.setCancelled(true);
            }
            case "Stats" -> {
                event.setCancelled(true);
            }
            case "Cosmetics" -> {
                event.setCancelled(true);
            }
        }
    }
}