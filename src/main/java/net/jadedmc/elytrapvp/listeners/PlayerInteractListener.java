package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.GameScoreboard;
import net.jadedmc.elytrapvp.game.kits.KitSelectorGUI;
import net.jadedmc.elytrapvp.inventories.CosmeticsGUI;
import net.jadedmc.elytrapvp.inventories.SettingsGUI;
import net.jadedmc.elytrapvp.inventories.StatsGUI;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import net.jadedmc.elytrapvp.utils.LocationUtils;
import net.jadedmc.elytrapvp.utils.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * This class runs a listener that is called whenever a player interacts with their surroundings.
 * This is used to power the lobby/parkour items and detect when a player reaches a parkour checkpoint.
 */
public class PlayerInteractListener implements Listener {
    private final ElytraPvP plugin;

    /**
     * Creates the Listener.
     * @param plugin Instance of the plugin.
     */
    public PlayerInteractListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event PlayerInteractEvent.
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Powers the parkour checkpoints.
        if(event.getAction() == Action.PHYSICAL && plugin.parkourManager().getData(player) != null) {
            if(event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                plugin.parkourManager().getData(player).setCheckpoint(player.getLocation());
                return;
            }
        }

        // Check for the "Eternal Storage" command.
        if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST && player.getGameMode() == GameMode.ADVENTURE && customPlayer.getStatus() == Status.LOBBY) {
            plugin.achievementManager().getAchievement("target_3").unlock(player);
        }

        // Prevent players from moving items around in their inventory.
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

            if(event.getItem().getItemMeta() != null) {
                String item = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());

                if(item.equalsIgnoreCase("Explosive Fireworks")) {
                    event.setCancelled(true);
                    return;
                }
            }

            event.getItem().setAmount(64);

            // Add to fireworks used counter if the player is in the arena.
            if(customPlayer.getStatus() == Status.ARENA) {
                customPlayer.addFireworkUsed(plugin.kitManager().getKit(customPlayer.getKit()));
            }
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
            case "Settings" -> {
                new SettingsGUI(plugin, player).open(player);
                event.setCancelled(true);
            }
            case "Stats" -> {
                new StatsGUI(plugin, player).open(player);
                event.setCancelled(true);
            }
            case "Cosmetics" -> {
                new CosmeticsGUI(plugin, player).open(player);
                event.setCancelled(true);
            }
            case "Back to Checkpoint" -> {
                if(plugin.parkourManager().getData(player).getCheckpoint() == null) {
                    plugin.parkourManager().getTimer(player).reset();
                    player.teleport(LocationUtils.fromConfig(plugin.settingsManager().getConfig(), "Parkour." + plugin.parkourManager().getCourse(player).toUpperCase() + ".Location"));
                    return;
                }

                player.teleport(plugin.parkourManager().getData(player).getCheckpoint());
            }
            case "Leave" -> {
                new GameScoreboard(plugin, player);
                plugin.parkourManager().removePlayer(player);
                player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
                ItemUtils.giveLobbyItems(plugin, player);
            }
        }
    }
}