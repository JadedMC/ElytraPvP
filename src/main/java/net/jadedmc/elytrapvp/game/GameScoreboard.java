package net.jadedmc.elytrapvp.game;

import net.jadedmc.elytrapvp.utils.scoreboard.CustomScoreboard;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

public class GameScoreboard extends CustomScoreboard {

    public GameScoreboard(Player player) {
        CustomScoreboard.getPlayers().put(player.getUniqueId(), this);
        update(player);
    }

    public void update(Player player) {
        ScoreHelper helper;

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        helper.setTitle("&a&lElytraPvP");
        helper.setSlot(14,  "&7&m------------------");
        helper.setSlot(13, "&a&lCoins");
        helper.setSlot(12, "&f" + 0);
        helper.setSlot(11, "");
        helper.setSlot(10, "&a&lKills");
        helper.setSlot(9, "&f" + 0);
        helper.setSlot(8, "");
        helper.setSlot(7, "&a&lDeaths");
        helper.setSlot(6, "&f" + 0);
        helper.setSlot(5, "");
        helper.setSlot(4, "&a&lKill Streak");
        helper.setSlot(3, "&f" + 0);
        helper.setSlot(2, "&7&m------------------");
        helper.setSlot(1, "&ajadedmc.net");
    }
}