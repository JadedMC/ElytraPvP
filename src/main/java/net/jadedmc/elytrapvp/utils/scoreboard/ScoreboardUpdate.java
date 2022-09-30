package net.jadedmc.elytrapvp.utils.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdate extends BukkitRunnable {
    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(ScoreHelper.hasScore(p)) {
                CustomScoreboard.getPlayers().get(p.getUniqueId()).update(p);
            }
        }
    }
}