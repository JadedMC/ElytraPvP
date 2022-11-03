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
    private final Map<String, Integer> kills = new LinkedHashMap<>();
    private final Map<String, Integer> killStreak = new LinkedHashMap<>();

    /**
     * Creates the manager.
     * @param plugin Instance of the plugin.
     */
    public LeaderboardManager(ElytraPvP plugin) {
        this.plugin = plugin;

        // Creates a task that updates the leaderboards every 20 minutes
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::update, 20*4, 20*60*20);
    }

    /**
     * Updates the leaderboards.
     */
    public void update() {
        updateParkour();
        updateKills();
        updateKillStreak();
    }

    /**
     * Updates the kills leaderboard.
     */
    public void updateKills() {
        try {
            kills.clear();

            PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_kit_statistics WHERE kit = ? ORDER BY kills DESC LIMIT 10");
            statement.setString(1, "global");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PreparedStatement statement2 = plugin.mySQL().getConnection().prepareStatement("SELECT * from player_info WHERE uuid = ? LIMIT 1");
                statement2.setString(1, resultSet.getString(1));
                ResultSet results2 = statement2.executeQuery();

                if(results2.next()) {
                    kills.put(results2.getString(2), resultSet.getInt("kills"));
                }
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Updates the kill streak leaderboard.
     */
    public void updateKillStreak() {
        try {
            killStreak.clear();

            PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_kit_statistics WHERE kit = ? ORDER BY bestKillStreak DESC LIMIT 10");
            statement.setString(1, "global");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PreparedStatement statement2 = plugin.mySQL().getConnection().prepareStatement("SELECT * from player_info WHERE uuid = ? LIMIT 1");
                statement2.setString(1, resultSet.getString(1));
                ResultSet results2 = statement2.executeQuery();

                if(results2.next()) {
                    killStreak.put(results2.getString(2), resultSet.getInt("bestKillStreak"));
                }
            }
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Updates the parkour leaderboards.
     */
    private void updateParkour() {
        try {
            parkourCourses.clear();

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
     * Gets the kill leaderboard.
     * @return Map with the kill leaderboard.
     */
    public Map<String, Integer> getKillsLeaderboard() {
        return kills;
    }

    /**
     * Gets the kill streak leaderboard.
     * @return Map with the kill streak leaderboard.
     */
    public Map<String, Integer> getKillStreakLeaderboard() {
        return killStreak;
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