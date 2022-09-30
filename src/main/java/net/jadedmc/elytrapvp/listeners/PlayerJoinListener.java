package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.game.GameScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Apply the Custom Scoreboard
        new GameScoreboard(player);
    }
}