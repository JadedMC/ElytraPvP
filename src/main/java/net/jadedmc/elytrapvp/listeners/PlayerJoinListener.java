package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.GameScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final ElytraPvP plugin;

    public PlayerJoinListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.customPlayerManager().addPlayer(player);

        // Apply the Custom Scoreboard
        new GameScoreboard(plugin, player);
    }
}