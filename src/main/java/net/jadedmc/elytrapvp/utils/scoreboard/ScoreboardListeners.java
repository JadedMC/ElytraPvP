package net.jadedmc.elytrapvp.utils.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListeners implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if(ScoreHelper.hasScore(p)) {
            ScoreHelper.removeScore(p);
        }

        CustomScoreboard.getPlayers().remove(p.getUniqueId());
    }
}
