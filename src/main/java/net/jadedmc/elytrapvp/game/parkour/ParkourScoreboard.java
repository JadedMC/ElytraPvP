package net.jadedmc.elytrapvp.game.parkour;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.scoreboard.CustomScoreboard;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

/**
 * Runs the scoreboard shown when using a parkour course.
 */
public class ParkourScoreboard extends CustomScoreboard {
    private final ElytraPvP plugin;

    /**
     * Creates the scoreboard.
     * @param plugin Instance of the plugin.
     * @param player Player the scoreboard is for.
     */
    public ParkourScoreboard(ElytraPvP plugin, Player player) {
        super(player);
        this.plugin = plugin;
        CustomScoreboard.getPlayers().put(player.getUniqueId(), this);
        update(player);
    }

    /**
     * Updates the scoreboard.
     * @param player Player to update scoreboard of.
     */
    public void update(Player player) {
        ScoreHelper helper;

        // Creates scoreboard if does not exist. If it does, updates it instead.
        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        helper.setTitle("&a&lElytraPvP");
        helper.setSlot(11,  "&7&m------------------");
        helper.setSlot(10, "&a&lLevel");
        helper.setSlot(9, plugin.parkourManager().getCourseName(player));
        helper.setSlot(8, "");
        helper.setSlot(7, "&a&lTime");
        helper.setSlot(6, plugin.parkourManager().getTimer(player).toString());
        helper.setSlot(5, "");
        helper.setSlot(4, "&a&lBest Time");

        // Gets the player's best time for the course.
        long bestTime = customPlayer.getBestParkourTime(plugin.parkourManager().getCourse(player));

        // If it's equal to 0, display it as N/A. Otherwise, show the time.
        if(bestTime == 0) {
            helper.setSlot(3, "N/A");
        }
        else {
            helper.setSlot(3, plugin.parkourManager().getTimerFromTime(bestTime).toString());
        }

        helper.setSlot(2, "&7&m------------------");
        helper.setSlot(1, "&ajadedmc.net");
    }
}