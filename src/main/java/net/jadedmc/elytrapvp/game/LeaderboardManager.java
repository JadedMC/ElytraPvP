package net.jadedmc.elytrapvp.game;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.parkour.ParkourCourse;
import net.jadedmc.elytrapvp.utils.TimeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages leaderboards for various statistics.
 */
public class LeaderboardManager {
    private final ElytraPvP plugin;

    private final Map<ParkourCourse, Map<String, String>> parkourCourses = new HashMap<>();

    /**
     * Creates the manager.
     * @param plugin Instance of the plugin.
     */
    public LeaderboardManager(ElytraPvP plugin) {
        this.plugin = plugin;

        // Creates a task that updates the leaderboards every 20 minutes
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::update, 20*3, 20*60*20);
    }

    /**
     * Updates the leaderboards.
     */
    public void update() {
        updateParkour();
    }

    /**
     * Updates the parkour leaderboards.
     */
    private void updateParkour() {
        try {
            for(ParkourCourse course : ParkourCourse.values()) {
                Map<String, String> courseTimes = new LinkedHashMap<>();

                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_parkour WHERE course = ? ORDER BY bestTime ASC LIMIT 10");
                statement.setString(1, course.toString());
                ResultSet results = statement.executeQuery();

                while(results.next()) {
                    PreparedStatement statement2 = plugin.mySQL().getConnection().prepareStatement("SELECT * from player_info WHERE uuid = ? LIMIT 1");
                    statement2.setString(1, results.getString(1));
                    ResultSet results2 = statement2.executeQuery();

                    if(results2.next()) {
                        courseTimes.put(results2.getString(2), TimeUtils.msToTimer(results.getLong("bestTime")));
                    }
                    else {
                        courseTimes.put("null", TimeUtils.msToTimer(results.getLong("bestTime")));
                    }
                }

                parkourCourses.put(course, courseTimes);
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets the leaderboard for a specific parkour course.
     * @param course Course to get leaderboard of.
     * @return Course leaderboard.
     */
    public Map<String, String> getParkourLeaderboard(ParkourCourse course) {
        return parkourCourses.get(course);
    }
}
