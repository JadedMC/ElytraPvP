package net.jadedmc.elytrapvp.game.parkour.events;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.parkour.ParkourTimer;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player completes a parkour course.
 */
public class ParkourCompleteEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final String course;
    private final ParkourTimer timer;

    /**
     * Creates the event.
     * @param plugin Instance of the plugin.
     * @param player Player who completed a parkour course.
     * @param course Course they completed.
     * @param timer Their parkour timer.
     */
    public ParkourCompleteEvent(ElytraPvP plugin, Player player, String course, ParkourTimer timer) {
        this.player = player;
        this.course = course;
        this.timer = timer;

        switch (course.toUpperCase()) {
            case "GREEN" -> {
                // Checks for the "First Steps" achievement.
                plugin.achievementManager().getAchievement("parkour_1").unlock(player);

                // Checks for the "The Beginning" achievement.
                if(timer.toMilliseconds() < (30000)) {
                    plugin.achievementManager().getAchievement("parkour_2").unlock(player);
                }
            }

            case "YELLOW" -> {
                // Checks for the "A Hop, a Skip" achievement.
                plugin.achievementManager().getAchievement("parkour_3").unlock(player);

                // Checks for the "Seeking Flawlessness" achievement.
                if(timer.toMilliseconds() < (45000)) {
                    plugin.achievementManager().getAchievement("parkour_4").unlock(player);
                }
            }

            case "RED" -> {
                // Checks for the "Tumble and Slide" achievement.
                plugin.achievementManager().getAchievement("parkour_5").unlock(player);

                // Checks for the "A Faultless Run" achievement.
                if(timer.toMilliseconds() < (60000)) {
                    plugin.achievementManager().getAchievement("parkour_6").unlock(player);
                }
            }

            case "BLUE" -> {
                // Checks for the "An Indoor Mountain" achievement.
                plugin.achievementManager().getAchievement("parkour_7").unlock(player);

                // Checks for the "Something Like Perfection" achievement.
                if(timer.toMilliseconds() < (180000)) {
                    plugin.achievementManager().getAchievement("parkour_8").unlock(player);
                }
            }

            case "BEGINNER" -> {
                // Checks for the "Beginner" achievement.
                plugin.achievementManager().getAchievement("parkour_9").unlock(player);

                // Checks for the "Eyas" achievement.
                if(timer.toMilliseconds() < (60000)) {
                    plugin.achievementManager().getAchievement("parkour_10").unlock(player);
                }
            }

            case "ADVANCED" -> {
                // Checks for the "Advanced" achievement.
                plugin.achievementManager().getAchievement("parkour_11").unlock(player);

                // Checks for the "Haggard" achievement.
                if(timer.toMilliseconds() < (60000*5)) {
                    plugin.achievementManager().getAchievement("parkour_12").unlock(player);
                }
            }

            case "PEERLESS" -> {
                // Checks for the "Peerless" achievement.
                plugin.achievementManager().getAchievement("parkour_13").unlock(player);

                // Checks for the "Haggard" achievement.
                if(timer.toMilliseconds() < (60000*20)) {
                    plugin.achievementManager().getAchievement("parkour_14").unlock(player);
                }
            }
        }
    }

    /**
     * Get the player who completed the course.
     * @return Player who completed the parkour course.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the course the player completed.
     * @return Completed course.
     */
    public String getCourse() {
        return course;
    }

    /**
     * Get the player's timer.
     * @return Parkour course timer.
     */
    public ParkourTimer getTimer() {
        return timer;
    }

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}