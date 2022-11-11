package net.jadedmc.elytrapvp.player;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.hats.Hat;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.game.parkour.ParkourCourse;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Manages and caches plugin data about a player.
 */
public class CustomPlayer {
    private final ElytraPvP plugin;
    private final UUID uuid;
    private String kit;
    private Status status = Status.LOBBY;
    private DeathType deathType = DeathType.NONE;

    // Achievements
    private final List<String> challengeAchievements = new ArrayList<>();
    private final Map<String, Integer> tieredAchievements = new HashMap<>();

    // Kits
    private final List<String> unlockedKits = new ArrayList<>();

    // Cosmetics
    private String hat = "none";
    private final List<String> unlockedHats = new ArrayList<>();

    // Statistics
    private int coins;
    private int bounty;
    private int lifetimeCoins;
    private int lifetimeBountyHad;
    private int lifetimeBountyClaimed;
    private final Map<String, Integer> kills = new HashMap<>();
    private final Map<String, Integer> deaths = new HashMap<>();
    private final Map<String, Integer> killStreak = new HashMap<>();
    private final Map<String, Integer> bestKillStreak = new HashMap<>();
    private final Map<String, Integer> fireworksUsed = new HashMap<>();
    private final Map<String, Integer> drops = new HashMap<>();
    private final Map<String, Long> parkourTimes = new HashMap<>();
    private final Map<String, Integer> parkourCompletions = new HashMap<>();
    private final Map<String, Integer> parkourRank = new HashMap<>();
    private final Map<String, Integer> killsRank = new HashMap<>();
    private final Map<String, Integer> killStreakRank = new HashMap<>();

    // Kit Editor
    private final Map<String, Map<Integer, Integer>> kitEditor = new HashMap<>();

    // Settings
    private boolean autoDeploy = true;
    private boolean showAllDeaths = true;
    private boolean showParticles = true;
    private boolean showScoreboard = true;

    /**
     * Creates the CactusPlayer object and loads data from MySQL if it exists.
     * If not, creates new entries in MySQL.
     * @param plugin Instance of the plugin.
     * @param uuid UUID of the player representing the CactusPlayer.
     */
    public CustomPlayer(ElytraPvP plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;

        // Run database operations async.
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {

                // elytrapvp_players
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * from elytrapvp_players WHERE uuid = ? LIMIT 1");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    if(resultSet.next()) {
                        kit = resultSet.getString(2);
                        coins = resultSet.getInt(3);
                        bounty = resultSet.getInt(4);
                        hat = resultSet.getString("hat");

                        // Give the player their selected hat once loaded.
                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            if(hat != null && !hat.equals("none")) {
                                getPlayer().getInventory().setHelmet(plugin.cosmeticManager().getHat(hat).toItemStack());
                            }
                        });
                    }
                    else {
                        PreparedStatement insert = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_players (uuid) VALUES (?)");
                        insert.setString(1, uuid.toString());
                        insert.executeUpdate();
                    }
                }

                // elytrapvp_kits
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * from elytrapvp_kits WHERE uuid = ?");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    while(resultSet.next()) {
                        unlockedKits.add(resultSet.getString(2));
                    }
                }

                // elytrapvp_kit_editor
                {
                    for(Kit kit : plugin.kitManager().getKits().values()) {
                        kitEditor.put(kit.getId(), new HashMap<>());

                        PreparedStatement statement3 = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_kit_editor WHERE uuid = ? AND kit = ?");
                        statement3.setString(1, uuid.toString());
                        statement3.setString(2, kit.getId());
                        ResultSet results3 = statement3.executeQuery();

                        while(results3.next()) {
                            kitEditor.get(kit.getId()).put(results3.getInt("item"), results3.getInt("slot"));
                        }
                    }
                }

                // elytrapvp_kit_statistics
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_kit_statistics WHERE uuid = ?");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    while(resultSet.next()) {
                        String kit = resultSet.getString(2);
                        kills.put(kit, resultSet.getInt(3));
                        deaths.put(kit, resultSet.getInt(4));
                        killStreak.put(kit, resultSet.getInt(5));
                        bestKillStreak.put(kit, resultSet.getInt(6));
                        fireworksUsed.put(kit, resultSet.getInt(7));
                        drops.put(kit, resultSet.getInt(8));
                    }
                }

                // elytrapvp_parkour
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_parkour WHERE uuid = ?");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    while(resultSet.next()) {
                        String course = resultSet.getString("course");
                        parkourCompletions.put(course, resultSet.getInt("completions"));
                        parkourTimes.put(course, resultSet.getLong("bestTime"));
                    }
                }

                // elytrapvp_settings
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_settings WHERE uuid = ? LIMIT 1");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    if(resultSet.next()) {
                        autoDeploy = resultSet.getBoolean("autoDeploy");
                        showAllDeaths = resultSet.getBoolean("showAllDeaths");
                        showParticles = resultSet.getBoolean("showParticles");
                        showScoreboard = resultSet.getBoolean("showScoreboard");
                    }
                    else {
                        PreparedStatement insert = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_settings (uuid) VALUES (?)");
                        insert.setString(1, uuid.toString());
                        insert.executeUpdate();
                    }
                }

                // elytrapvp_cosmetics
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_cosmetics WHERE uuid = ?");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    while(resultSet.next()) {
                        switch (resultSet.getString("type")) {
                            case "HAT" -> unlockedHats.add(resultSet.getString("cosmeticID"));
                        }
                    }
                }

                // Updates leaderboard ranks.
                updateLeaderboardRanks();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Adds a bounty to the player.
     * @param bounty Bounty to add to the player.
     */
    public void addBounty(int bounty) {
        setBounty(getBounty() + bounty);
    }

    /**
     * Adds coins to the player.
     * @param coins Amount of coins to add to the player.
     */
    public void addCoins(int coins) {
        setCoins(getCoins() + coins);
    }

    /**
     * Adds a death to the player.
     * @param kit Kit the death was earned in.
     */
    public void addDeath(Kit kit) {
        setDeaths("global", getDeaths("global") + 1);
        setDeaths(kit.getId(), getDeaths(kit.getId()) + 1);

        setKillStreak("global", 0);
        setKillStreak(kit.getId(), 0);
    }

    /**
     * Adds a drop to the player.
     * @param kit Kit the drop was earned in.
     */
    public void addDrop(Kit kit) {
        setDrops("global", getDrops("global") + 1);
        setDrops(kit.getId(), getDrops(kit.getId()) + 1);
    }

    /**
     * Adds a kill to the player.
     * @param kit Kit the killed was earned in.
     */
    public void addKill(Kit kit) {
        // Increments kill counter.
        setKills("global", getKills("global") + 1);
        setKills(kit.getId(), getKills(kit.getId()) + 1);

        // Increments kill streak.
        setKillStreak("global", getKillStreak("global") + 1);
        setKillStreak(kit.getId(), getKillStreak(kit.getId()) + 1);

        // Updates best kill streak.
        if(getKillStreak("global") > getBestKillStreak("global")) {
            setBestKillStreak("global", getKillStreak("global"));
        }

        // Updates the best kill streak.
        if(getKillStreak(kit.getId()) > getBestKillStreak(kit.getId())) {
            setBestKillStreak(kit.getId(), getKillStreak(kit.getId()));
        }
    }

    /**
     * Increments the number of times a parkour course has been completed by the player.
     * @param course Course that was completed.
     */
    public void addParkourCompletion(String course) {
        if(parkourCompletions.containsKey(course.toUpperCase())) {
            parkourCompletions.put(course.toUpperCase(), parkourCompletions.get(course.toUpperCase()) + 1);
            return;
        }

        parkourCompletions.put(course.toUpperCase(), 1);

        updateParkourStatistics(course);
    }

    /**
     * Get if the player's elytra should automatically deploy.
     * @return Whether the elytra should open on it's own.
     */
    public boolean autoDeploy() {
        return autoDeploy;
    }

    /**
     * Clear the kit editor of a kit.
     * @param kit Kit to clear.
     */
    private void cleanKitEditor(String kit) {
        try {
            PreparedStatement statement3 = plugin.mySQL().getConnection().prepareStatement("DELETE FROM elytrapvp_kit_editor WHERE uuid = ? AND kit = ?");
            statement3.setString(1, uuid.toString());
            statement3.setString(2, kit);
            statement3.executeUpdate();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get the best kill streak earned with a kit.
     * @param kit Kit the kill streak was earned in.
     * @return Best kill streak achieved.
     */
    public int getBestKillStreak(String kit) {
        if(bestKillStreak.containsKey(kit)) {
            return bestKillStreak.get(kit);
        }

        return 0;
    }

    /**
     * Gets the best time achieved on a parkour course.
     * @param course Course to get best time of.
     * @return Fastest the course has been completed.
     */
    public Long getBestParkourTime(String course) {
        course = course.toUpperCase();

        if(parkourTimes.containsKey(course)) {
            return parkourTimes.get(course);
        }

        return (long) 0;
    }

    /**
     * Gets the player's current bounty.
     * @return Number of coins the bounty is worth.
     */
    public int getBounty() {
        return bounty;
    }

    /**
     * Gets the player's current coin count.
     * @return Number of coins the player currently has.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Get the number of deaths the player has in a kit.
     * @param kit Kit to get death count of.
     * @return Number of times the player died with the kit.
     */
    public int getDeaths(String kit) {
        if(deaths.containsKey(kit)) {
            return deaths.get(kit);
        }

        return 0;
    }

    /**
     * Gets the way the player died.
     * Only useful while the player is dead.
     * @return The way the player died. NONE if they are alive.
     */
    public DeathType getDeathType() {
        return deathType;
    }

    /**
     * Get the number of times the player has entered the arena with a kit.
     * @param kit Kit to get number of drops of.
     * @return Number of drops with that kit.
     */
    public int getDrops(String kit) {
        if(drops.containsKey(kit)) {
            return drops.get(kit);
        }

        return 0;
    }

    /**
     * Get the amount of fireworks used with a kit.
     * @param kit Kit to get firework use count of.
     * @return Number of times fireworks have been used with that kit.
     */
    public int getFireworksUsed(String kit) {
        if(fireworksUsed.containsKey(kit)) {
            return fireworksUsed.get(kit);
        }

        return 0;
    }

    /**
     * Get the player's current hat.
     * @return Currently selected hat.
     */
    public Hat getHat() {
        if(hat == null || hat.equalsIgnoreCase("none")) {
            return null;
        }

        return plugin.cosmeticManager().getHat(hat);
    }

    /**
     * Get the number of kills the played has earned with a kit.
     * @param kit Kit to get kill count of.
     * @return Number of kills achieved with that kit.
     */
    public int getKills(String kit) {
        if(kills.containsKey(kit)) {
            return kills.get(kit);
        }

        return 0;
    }

    /**
     * Get the rank of the player on the kills leaderboard.
     * @param kit Kit to get kills rank of.
     * @return Rank of the player, or N/A if not completed.
     */
    public String getKillsRank(String kit) {

        if(killsRank.get(kit) == -1) {
            return "&cN/A";
        }

        else {
            return "#" + killsRank.get(kit);
        }
    }

    /**
     * Get a player's current kill streak with a kit.
     * @param kit Kit to get kill streak of.
     * @return Current kill streak with that kit.
     */
    public int getKillStreak(String kit) {
        if(killStreak.containsKey(kit)) {
            return killStreak.get(kit);
        }

        return 0;
    }

    /**
     * Get the rank of the player on the kill streak leaderboard.
     * @param kit Kit to get kill streak rank of.
     * @return Rank of the player, or N/A if not completed.
     */
    public String getKillStreakRank(String kit) {

        if(killStreakRank.get(kit) == -1) {
            return "&cN/A";
        }

        else {
            return "#" + killStreakRank.get(kit);
        }
    }

    /**
     * Get the id of the current kit the player is using.
     * @return Kit id of the kit currently in use.
     */
    public String getKit() {
        return kit;
    }

    /**
     * Get the kit editor cache of the player.
     * @param kit Kit to get cache of.
     * @return Remapped items for the kit.
     */
    public Map<Integer, Integer> getKitEditor(String kit) {
        return kitEditor.get(kit);
    }

    /**
     * Get the total bounty amount the player has claimed.
     * @return Amount of coins earned through bounties.
     */
    public int getLifetimeBountyClaimed() {
        return lifetimeBountyClaimed;
    }

    /**
     * Get the total amount of bounty the player has had on them.
     * @return Amount of coins worth of bounty the player has had on them.
     */
    public int getLifetimeBountyHad() {
        return lifetimeBountyHad;
    }

    /**
     * Get the lifetime amount of coins earned.
     * @return Total amount of coins earned.
     */
    public int getLifetimeCoins() {
        return lifetimeCoins;
    }

    /**
     * Get the number of times the player has finished a specific parkour course.
     * @param course Course to get the count of.
     * @return Number of times the player has finished it.
     */
    public int getParkourCompletions(String course) {
        course = course.toUpperCase();

        if(parkourCompletions.containsKey(course)) {
            return parkourCompletions.get(course);
        }

        return 0;
    }

    /**
     * Get the rank of the player on the parkour leaderboard.
     * @param course Course to get rank of.
     * @return Rank of the player, or N/A if not completed.
     */
    public String getParkourRank(String course) {
        course = course.toUpperCase();

        if(parkourRank.get(course) == -1) {
            return "&cN/A";
        }

        else {
            return "#" + parkourRank.get(course);
        }
    }

    /**
     * Gets the Player object the CustomPlayer represents.
     * @return Player object of the player.
     */
    public Player getPlayer() {
        return plugin.getServer().getPlayer(uuid);
    }

    /**
     * Gets the current status of the player.
     * @return Player's status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Gets a list of the kits the player has unlocked.
     * @return List of the ids of hats unlocked.
     */
    public List<String> getUnlockedHats() {
        return unlockedHats;
    }

    /**
     * Gets a list of the kits the player has unlocked.
     * @return List of the ids of kits unlocked.
     */
    public List<String> getUnlockedKits() {
        return unlockedKits;
    }

    /**
     * Remove a number of coins from the player.
     * @param coins Amount of coins to remove.
     */
    public void removeCoins(int coins) {
        setCoins(getCoins() - coins);
    }

    /**
     * Reset's the player's bounty.
     */
    public void resetBounty() {
        setBounty(0);
    }

    /**
     * Set if the player's elytra should automatically deploy.
     * @param autoDeploy Whether the elytra should open on its own.
     */
    public void setAutoDeploy(boolean autoDeploy) {
        this.autoDeploy = autoDeploy;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_settings SET autoDeploy = ? WHERE uuid = ?");
                statement.setBoolean(1, autoDeploy);
                statement.setString(2, uuid.toString());
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the best kill streak the player has earned with a kit.
     * @param kit Kit to set best kill streak of.
     * @param bestKillStreak New best kill streak.
     */
    private void setBestKillStreak(String kit, int bestKillStreak) {
        this.bestKillStreak.put(kit, bestKillStreak);
        updateKitStatistics(kit);
    }

    /**
     * Set the best finish time of a parkour course.
     * @param course Course to change best time of.
     * @param time New best time.
     */
    public void setBestParkourTime(String course, long time) {
        parkourTimes.put(course, time);
        updateParkourStatistics(course);
    }

    /**
     * Set the bounty of the player.
     * @param bounty New bounty.
     */
    private void setBounty(int bounty) {
        this.bounty = bounty;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET bounty = ? WHERE uuid = ?");
                statement.setInt(1, bounty);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of coins the player has.
     * @param coins New amount of coins.
     */
    private void setCoins(int coins) {
        this.coins = coins;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET coins = ? WHERE uuid = ?");
                statement.setInt(1, coins);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of deaths the player has.
     * @param kit Kit to set death count of.
     * @param deaths New amount of deaths in that kit.
     */
    private void setDeaths(String kit, int deaths) {
        this.deaths.put(kit, deaths);
        updateKitStatistics(kit);
    }

    /**
     * Set the player's death type.
     * @param deathType New death type.
     */
    public void setDeathType(DeathType deathType) {
        this.deathType = deathType;
    }

    /**
     * Set the number of drops the player has in a kit.
     * @param kit Kit to set drop count of.
     * @param drops New number of drops.
     */
    private void setDrops(String kit, int drops) {
        this.drops.put(kit, drops);
        updateKitStatistics(kit);
    }

    /**
     * Set the amount of fireworks used by the player.
     * @param kit Kit to set firework count of.
     * @param fireworksUsed New number of fireworks used.
     */
    private void setFireworksUsed(String kit, int fireworksUsed) {
        this.fireworksUsed.put(kit, fireworksUsed);
        updateKitStatistics(kit);
    }

    /**
     * Set the hat the player is currently using.
     * @param hat New hat the player is using.
     */
    public void setHat(Hat hat) {
        String hatId = "none";

        if(hat != null) {
            hatId = hat.getId();
        }

        this.hat = hatId;

        String finalHatId = hatId;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET hat = ? WHERE uuid = ?");
                statement.setString(1, finalHatId);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of kills the player has gained with a kit.
     * @param kit Kit to set kill count of.
     * @param kills New number of kills earned.
     */
    private void setKills(String kit, int kills) {
        this.kills.put(kit, kills);
        updateKitStatistics(kit);
    }

    /**
     * Set the player's current kill streak with a kit.
     * @param kit Kit to set the kill streak of.
     * @param killStreak New kill streak.
     */
    private void setKillStreak(String kit, int killStreak) {
        this.killStreak.put(kit, killStreak);
        updateKitStatistics(kit);
    }

    /**
     * Set the kit the player is currently using.
     * @param kit New kit the player is using.
     */
    public void setKit(Kit kit) {
        this.kit = kit.getId();

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET kit = ? WHERE uuid = ?");
                statement.setString(1, kit.getId());
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set if the player should see all death messages in chat.
     * @param showAllDeaths Whether they should be able to see other player's deaths.
     */
    public void setShowAllDeaths(boolean showAllDeaths) {
        this.showAllDeaths = showAllDeaths;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_settings SET showAllDeaths = ? WHERE uuid = ?");
                statement.setBoolean(1, showAllDeaths);
                statement.setString(2, uuid.toString());
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set if the player should be able to see particles in-game.
     * @param showParticles Whether particles should be sent to the player.
     */
    public void setShowParticles(boolean showParticles) {
        this.showParticles = showParticles;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_settings SET showParticles = ? WHERE uuid = ?");
                statement.setBoolean(1, showParticles);
                statement.setString(2, uuid.toString());
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set if the player should be able to see the scoreboard.
     * @param showScoreboard Whether they can see the scoreboard.
     */
    public void setShowScoreboard(boolean showScoreboard) {
        this.showScoreboard = showScoreboard;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_settings SET showScoreboard = ? WHERE uuid = ?");
                statement.setBoolean(1, showScoreboard);
                statement.setString(2, uuid.toString());
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Change the status of the player.
     * @param status New status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Get if the player should be able to see all death messages.
     * @return Whether they can see other player's deaths.
     */
    public boolean showAllDeaths() {
        return showAllDeaths;
    }

    /**
     * Get if the player should be able to see particles from cosmetics.
     * @return Whether they can see particles.
     */
    public boolean showParticles() {
        return showParticles;
    }

    /**
     * Get if the player should be able to see the scoreboard.
     * @return Whether they can see the scoreboard.
     */
    public boolean showScoreboard() {
        return showScoreboard;
    }

    /**
     * Allows a player to use a hat.
     * @param hat Hat to unlock.
     */
    public void unlockHat(Hat hat) {
        unlockedHats.add(hat.getId());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_cosmetics (uuid,type,cosmeticID) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, "HAT");
                statement.setString(3, hat.getId());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Allows a player to use a kit.
     * @param kit Kit to unlock.
     */
    public void unlockKit(Kit kit) {
        unlockedKits.add(kit.getId());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_kits (uuid,kit) VALUES (?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit.getId());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Update the kit editor.
     * @param kit Kit to update.
     */
    public void updateKitEditor(String kit) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            cleanKitEditor(kit);

            try {
                Map<Integer, Integer> map = getKitEditor(kit);

                for(int item : map.keySet()) {
                    int slot = map.get(item);

                    PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_kit_editor (uuid,kit,item,slot) VALUES (?,?,?,?)");
                    statement.setString(1, uuid.toString());
                    statement.setString(2, kit);
                    statement.setInt(3, item);
                    statement.setInt(4, slot);
                    statement.executeUpdate();
                }
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Updates per-kit statistics.
     * In a separate method to prevent statistics from resetting.
     * @param kit Kit to update.
     */
    private void updateKitStatistics(String kit) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, ()-> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,kills,deaths,killStreak,bestKillStreak,fireworksUsed,drops) VALUES (?,?,?,?,?,?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, getKills(kit));
                statement.setInt(4, getDeaths(kit));
                statement.setInt(5, getKillStreak(kit));
                statement.setInt(6, getBestKillStreak(kit));
                statement.setInt(7, getFireworksUsed(kit));
                statement.setInt(8, getDrops(kit));
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Updates the player's leaderboard ranks.
     */
    private void updateLeaderboardRanks() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Parkour
                {
                    for (ParkourCourse course : ParkourCourse.values()) {
                        if (!parkourTimes.containsKey(course.toString())) {
                            parkourRank.put(course.toString(), -1);
                            continue;
                        }

                        PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM (SELECT *,ROW_NUMBER() OVER (ORDER BY bestTime) AS parkourRank FROM elytrapvp_parkour WHERE course = ?) AS temp WHERE temp.uuid = ?");
                        retrieve.setString(1, course.toString());
                        retrieve.setString(2, uuid.toString());
                        ResultSet resultSet = retrieve.executeQuery();

                        if (resultSet.next()) {
                            parkourRank.put(course.toString(), resultSet.getInt("parkourRank"));
                        }
                    }
                }

                // Kits
                {
                    List<String> temp = new ArrayList<>();
                    plugin.kitManager().getKits().values().forEach(kit -> temp.add(kit.getId()));
                    temp.add("global");

                    for(String kit : temp) {
                        if(!kills.containsKey(kit)) {
                            killsRank.put(kit, -1);
                            killStreakRank.put(kit, -1);
                            continue;
                        }

                        // Kills
                        {
                            PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM (SELECT *,ROW_NUMBER() OVER (ORDER BY kills DESC) AS killsRank FROM elytrapvp_kit_statistics WHERE kit = ?) AS temp WHERE temp.uuid = ?");
                            retrieve.setString(1, kit);
                            retrieve.setString(2, uuid.toString());
                            ResultSet resultSet = retrieve.executeQuery();

                            if (resultSet.next()) {
                                killsRank.put(kit, resultSet.getInt("killsRank"));
                            }
                        }

                        // Kill Streak
                        {
                            PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM (SELECT *,ROW_NUMBER() OVER (ORDER BY bestKillStreak DESC) AS killStreakRank FROM elytrapvp_kit_statistics WHERE kit = ?) AS temp WHERE temp.uuid = ?");
                            retrieve.setString(1, kit);
                            retrieve.setString(2, uuid.toString());
                            ResultSet resultSet = retrieve.executeQuery();

                            if (resultSet.next()) {
                                killStreakRank.put(kit, resultSet.getInt("killStreakRank"));
                            }
                        }
                    }
                }
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Updates parkour statistics.
     * In another method to prevent statistics from resetting.
     * @param course Course to update.
     */
    private void updateParkourStatistics(String course) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
           try {
               PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_parkour (uuid,course,bestTime,completions) VALUES (?,?,?,?)");
               statement.setString(1, uuid.toString());
               statement.setString(2, course.toUpperCase());
               statement.setLong(3, getBestParkourTime(course));
               statement.setInt(4, getParkourCompletions(course));
               statement.executeUpdate();
           }
           catch (SQLException exception) {
               exception.printStackTrace();
           }
        });
    }
}