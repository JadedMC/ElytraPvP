package net.jadedmc.elytrapvp.utils.scoreboard;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdate extends BukkitRunnable {
    private final ElytraPvP plugin;

    public ScoreboardUpdate(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(p);

            if(!customPlayer.showScoreboard()) {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                return;
            }

            if(ScoreHelper.hasScore(p)) {
                CustomScoreboard.getPlayers().get(p.getUniqueId()).update(p);
            }
        }
    }
}