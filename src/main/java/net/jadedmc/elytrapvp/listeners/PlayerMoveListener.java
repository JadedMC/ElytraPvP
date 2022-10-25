package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final ElytraPvP plugin;

    public PlayerMoveListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Exit if player is dead;
        if(player.isDead()) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        switch (customPlayer.getStatus()) {
            case ARENA -> {

            }

            case  LOBBY -> {
                // Exit if kit is not selected.
                if(plugin.arenaManager().getSelectedArena().hasPlayer(player)) {
                    if(plugin.kitManager().getKit(customPlayer.getKit()) == null) {
                        player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
                        ChatUtils.chat(player, "&c&lError &8» &cYou have not selected a kit yet.");
                        return;
                    }

                    Kit kit = plugin.kitManager().getKit(customPlayer.getKit());
                    customPlayer.setStatus(Status.ARENA);
                    player.getInventory().clear();
                    kit.apply(player);
                    customPlayer.addDrop(kit);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> player.setGliding(true), 20);
                }
            }

            default -> {

            }
        }
    }
}