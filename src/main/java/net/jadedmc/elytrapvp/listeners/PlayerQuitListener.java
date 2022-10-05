package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final ElytraPvP plugin;

    public PlayerQuitListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.customPlayerManager().removePlayer(player);
    }
}