package net.jadedmc.elytrapvp.game.parkour.events;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.parkour.ParkourTimer;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.jadedcore.JadedAPI;
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

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        switch (course.toUpperCase()) {
            case "GREEN" -> {
                // Checks for the "First Steps" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_21").unlock(getPlayer())) {
                    customPlayer.addCoins(10);
                }

                // Checks for the "The Beginning" achievement.
                if(timer.toMilliseconds() < (30000)) {
                    if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_40").unlock(getPlayer())) {
                        customPlayer.addCoins(15);
                    }
                }
            }

            case "YELLOW" -> {
                // Checks for the "A Hop, a Skip" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_3").unlock(getPlayer())) {
                    customPlayer.addCoins(20);
                }

                // Checks for the "Seeking Flawlessness" achievement.
                if(timer.toMilliseconds() < (45000)) {
                    if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_37").unlock(getPlayer())) {
                        customPlayer.addCoins(25);
                    }
                }
            }

            case "RED" -> {
                // Checks for the "Tumble and Slide" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_43").unlock(getPlayer())) {
                    customPlayer.addCoins(30);
                }

                // Checks for the "A Faultless Run" achievement.
                if(timer.toMilliseconds() < (60000)) {
                    if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_2").unlock(getPlayer())) {
                        customPlayer.addCoins(35);
                    }
                }
            }

            case "BLUE" -> {
                // Checks for the "An Indoor Mountain" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_6").unlock(getPlayer())) {
                    customPlayer.addCoins(40);
                }

                // Checks for the "Something Like Perfection" achievement.
                if(timer.toMilliseconds() < (180000)) {
                    if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_38").unlock(getPlayer())) {
                        customPlayer.addCoins(50);
                        customPlayer.unlockTrail(plugin.cosmeticManager().getTrail("ground_pro_trail"));
                    }
                }
            }

            case "BEGINNER" -> {
                // Checks for the "Beginner" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_17").unlock(getPlayer())) {
                    customPlayer.addCoins(20);
                }

                // Checks for the "Eyas" achievement.
                if(timer.toMilliseconds() < (60000)) {
                    if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_19").unlock(getPlayer())) {
                        customPlayer.addCoins(30);
                    }
                }
            }

            case "ADVANCED" -> {
                // Checks for the "Advanced" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_4").unlock(getPlayer())) {
                    customPlayer.addCoins(50);
                }

                // Checks for the "Passager" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_31").unlock(getPlayer())) {
                    customPlayer.addCoins(75);
                }
            }

            case "PEERLESS" -> {
                // Checks for the "Peerless" achievement.
                if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_32").unlock(getPlayer())) {
                    customPlayer.addCoins(100);
                }

                // Checks for the "Haggard" achievement.
                if(timer.toMilliseconds() < (60000*5)) {
                    if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_24").unlock(getPlayer())) {
                        customPlayer.addCoins(150);
                        customPlayer.unlockTrail(plugin.cosmeticManager().getTrail("peerless_trail"));
                    }
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