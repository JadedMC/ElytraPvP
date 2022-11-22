package net.jadedmc.elytrapvp.player;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.arrowtrails.ArrowTrail;
import net.jadedmc.elytrapvp.game.cosmetics.hats.Hat;
import net.jadedmc.elytrapvp.game.cosmetics.killmessages.KillMessage;
import net.jadedmc.elytrapvp.game.cosmetics.tags.Tag;
import net.jadedmc.elytrapvp.game.cosmetics.trails.Trail;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.game.parkour.ParkourCourse;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    private final List<String> achievements = new ArrayList<>();

    // Kits
    private final List<String> unlockedKits = new ArrayList<>();

    // Cosmetics
    private String hat = "none";
    private String killMessage = "none";
    private String tag = "none";
    private String arrowTrail = "none";
    private String trail = "none";
    private final List<String> unlockedHats = new ArrayList<>();
    private final List<String> unlockedKillMessages = new ArrayList<>();
    private final List<String> unlockedTags = new ArrayList<>();
    private final List<String> unlockedArrowTrails = new ArrayList<>();
    private final List<String> unlockedTrails = new ArrayList<>();
    // Statistics
    private int coins = 0;
    private int bounty = 0;
    private int lifetimeCoins = 0;
    private int lifetimeBountyHad = 0;
    private int lifetimeBountyClaimed = 0;
    private int windowsBroken = 0;
    private final Map<String, Integer> kills = new HashMap<>();
    private final Map<String, Integer> deaths = new HashMap<>();
    private final Map<String, Integer> killStreak = new HashMap<>();
    private final Map<String, Integer> bestKillStreak = new HashMap<>();
    private final Map<String, Integer> fireworksUsed = new HashMap<>();
    private final Map<String, Integer> drops = new HashMap<>();
    private final Map<String, Integer> arrowsShot = new HashMap<>();
    private final Map<String, Integer> arrowsHit = new HashMap<>();
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
                        killMessage = resultSet.getString("killMessage");
                        tag = resultSet.getString("tag");
                        arrowTrail = resultSet.getString("arrowTrail");
                        trail = resultSet.getString("trail");

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
                        arrowsShot.put(kit, resultSet.getInt("arrowsShot"));
                        arrowsHit.put(kit, resultSet.getInt("arrowsHit"));
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
                            case "KILL_MESSAGE" -> unlockedKillMessages.add(resultSet.getString("cosmeticID"));
                            case "TAG" -> unlockedTags.add(resultSet.getString("cosmeticID"));
                            case "ARROW_TRAIL" -> unlockedArrowTrails.add(resultSet.getString("cosmeticID"));
                            case "TRAIL" -> unlockedTrails.add(resultSet.getString("cosmeticID"));
                        }
                    }
                }

                // elytrapvp_statistics
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_statistics WHERE uuid = ? LIMIT 1");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    if(resultSet.next()) {
                        lifetimeCoins = resultSet.getInt("lifetimeCoins");
                        lifetimeBountyHad = resultSet.getInt("lifetimeBountyHad");
                        lifetimeBountyClaimed = resultSet.getInt("lifetimeBountyClaimed");
                        windowsBroken = resultSet.getInt("windowsBroken");
                    }
                    else {
                        PreparedStatement insert = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_statistics (uuid) VALUES (?)");
                        insert.setString(1, uuid.toString());
                        insert.executeUpdate();
                    }
                }

                // elytrapvp_achievements
                {
                    PreparedStatement retrieve = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM elytrapvp_achievements WHERE uuid = ?");
                    retrieve.setString(1, uuid.toString());
                    ResultSet resultSet = retrieve.executeQuery();

                    while(resultSet.next()) {
                        achievements.add(resultSet.getString("achievement"));
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
     * Adds an arrow hit to the arrows hit counter.
     * @param kit Kit to add hit arrow to.
     */
    public void addArrowHit(Kit kit) {
        setArrowsHit("global", getArrowsHit("global") + 1);
        setArrowsHit(kit.getId(), getArrowsHit(kit.getId()) + 1);

        // Checks for the "Sharp Shooter" achievement.
        if(getArrowsHit("global") >= 2500) {
            plugin.achievementManager().getAchievement("arrows_hit)1").unlock(getPlayer());
        }
    }

    /**
     * Adds an arrow shot to the arrows shot counter.
     * @param kit Kit to add shot arrow to.
     */
    public void addArrowShot(Kit kit) {
        setArrowsShot("global", getArrowsShot("global") + 1);
        setArrowsHit(kit.getId(), getArrowsShot(kit.getId()) + 1);
    }

    /**
     * Adds a bounty to the player.
     * @param bounty Bounty to add to the player.
     */
    public void addBounty(int bounty) {
        setBounty(getBounty() + bounty);
        setLifetimeBountyHad(getLifetimeBountyHad() + bounty);

        // Checks for the "Wanted" achievement.
        if(getBounty() >= 100) {
            plugin.achievementManager().getAchievement("bounty_2").unlock(getPlayer());
        }

        // Adds a temporary glowing effect.
        getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 40, 0));
    }

    /**
     * Adds coins to the player.
     * @param coins Amount of coins to add to the player.
     */
    public void addCoins(int coins) {
        setCoins(getCoins() + coins);
        setLifetimeCoins(getLifetimeCoins() + coins);

        // Checks for the "Cha-Ching" achievement.
        if(lifetimeCoins >= 2500) {
            plugin.achievementManager().getAchievement("coins_1").unlock(getPlayer());
        }

        // Checks for the "Ooh, Money" achievement.
        if(lifetimeCoins >= 5000) {
            plugin.achievementManager().getAchievement("coins_2").unlock(getPlayer());
        }

        // Checks for the "Money Maker" achievement.
        if(lifetimeCoins >= 10000) {
            plugin.achievementManager().getAchievement("coins_3").unlock(getPlayer());
        }

        // Checks for the "Banker" achievement.
        if(this.coins >= 2000) {
            plugin.achievementManager().getAchievement("coins_4").unlock(getPlayer());
        }
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
     * Adds a firework to the used fireworks counter.
     * @param kit Kit to add used fireworks to.
     */
    public void addFireworkUsed(Kit kit) {
        setFireworksUsed("global", getFireworksUsed("global") + 1);
        setFireworksUsed(kit.getId(), getFireworksUsed(kit.getId()) + 1);
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

        int kills = getKills("global");

        // Checks for the "Novice" achievement.
        if(kills >= 100) {
            plugin.achievementManager().getAchievement("kills_1").unlock(getPlayer());
        }

        // Checks for the "Student" achievement.
        if(kills >= 500) {
            plugin.achievementManager().getAchievement("kills_2").unlock(getPlayer());
        }

        // Checks for the "Master" achievement.
        if(kills >= 1000) {
            plugin.achievementManager().getAchievement("kills_3").unlock(getPlayer());
        }

        // Checks for the "Obsessed" achievement.
        if(kills >= 2500) {
            plugin.achievementManager().getAchievement("kills_4").unlock(getPlayer());
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
     * Adds a broken window to the broken window counter.
     */
    public void addWindowBroken() {
        setWindowsBroken(getWindowsBroken() + 1);

        // Checks if the player qualifies for the Anger Issues achievement.
        if(windowsBroken >= 1000) {
            plugin.achievementManager().getAchievement("windows_1").unlock(getPlayer());
        }
    }

    /**
     * Get if the player's elytra should automatically deploy.
     * @return Whether the elytra should open on it's own.
     */
    public boolean autoDeploy() {
        return autoDeploy;
    }

    /**
     * Add a bounty to the player's lifetime bounty claims.
     * @param bounty Bounty to add to total claims.
     */
    public void claimBounty(int bounty) {
        setLifetimeBountyClaimed(getLifetimeBountyClaimed() + bounty);

        // Checks for the "Bounty Hunter" achievement.
        if(getLifetimeBountyClaimed() >= 500) {
            plugin.achievementManager().getAchievement("bounty_1").unlock(getPlayer());
        }
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
     * Get the number of times the player has shot someone with an arrow.
     * @param kit Kit to get arrows hit count of.
     * @return Number of times they hit someone with an arrow.
     */
    public int getArrowsHit(String kit) {
        if(arrowsHit.containsKey(kit)) {
            return arrowsHit.get(kit);
        }

        return 0;
    }

    /**
     * Get the number of arrows the player has shot using a kit.
     * @param kit Kit to get arrows shot count of.
     * @return Number of arrows shot using that kit.
     */
    public int getArrowsShot(String kit) {
        if(arrowsShot.containsKey(kit)) {
            return arrowsShot.get(kit);
        }

        return 0;
    }

    /**
     * Get the player's current arrow trail.
     * @return Currently selected arrow trail.
     */
    public ArrowTrail getArrowTrail() {
        if(arrowTrail == null || arrowTrail.equalsIgnoreCase("none")) {
            return null;
        }

        return plugin.cosmeticManager().getArrowTrail(arrowTrail);
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
     * Get the player's current kill message.
     * @return Currently selected kill message.
     */
    public KillMessage getKillMessage() {
        if(killMessage == null || killMessage.equalsIgnoreCase("none")) {
            return null;
        }

        return plugin.cosmeticManager().getKillMessage(killMessage);
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
     * Get the player's current tag.
     * @return Currently selected tag.
     */
    public Tag getTag() {
        if(tag == null || tag.equalsIgnoreCase("none")) {
            return null;
        }

        return plugin.cosmeticManager().getTag(tag);
    }

    /**
     * Get the player's current trail.
     * @return Currently selected trail.
     */
    public Trail getTrail() {
        if(trail == null || trail.equalsIgnoreCase("none")) {
            return null;
        }

        return plugin.cosmeticManager().getTrail(trail);
    }

    /**
     * Gets a list of the arrow trails the player has unlocked.
     * @return List of the ids of the arrow trails.
     */
    public List<String> getUnlockedArrowTrails() {
        return unlockedArrowTrails;
    }

    /**
     * Gets a list of the hats the player has unlocked.
     * @return List of the ids of hats unlocked.
     */
    public List<String> getUnlockedHats() {
        return unlockedHats;
    }

    /**
     * Gets a list of the kill messages the player has unlocked.
     * @return List of the ids of kill messages unlocked.
     */
    public List<String> getUnlockedKillMessages() {
        return unlockedKillMessages;
    }

    /**
     * Gets a list of the kits the player has unlocked.
     * @return List of the ids of kits unlocked.
     */
    public List<String> getUnlockedKits() {
        return unlockedKits;
    }

    /**
     * Gets a list of the tags the player has unlocked.
     * @return List of the ids of unlocked tags.
     */
    public List<String> getUnlockedTags() {
        return unlockedTags;
    }

    /**
     * Gets a list of the trails the player has unlocked.
     * @return List of the ids of the trails.
     */
    public List<String> getUnlockedTrails() {
        return unlockedTrails;
    }

    /**
     * Get the number of windows the player has broken.
     * @return Number of windows broken.
     */
    public int getWindowsBroken() {
        return windowsBroken;
    }

    /**
     * Check if the player has unlocked a specific achievement.
     * @param achievement Id of the achievement.
     * @return Whether it has been unlocked.
     */
    public boolean hasAchievement(String achievement) {
        return achievements.contains(achievement);
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
     * Set the amount of players the player has hit with arrows.
     * @param kit Kit to set arrow hit count of.
     * @param arrowsHit New number of players hit.
     */
    private void setArrowsHit(String kit, int arrowsHit) {
        this.arrowsHit.put(kit, arrowsHit);
    }

    /**
     * Set the amount of arrows the player has shot.
     * @param kit Kit to set arrows shot count of.
     * @param arrowsShot New number of arrows shot.
     */
    private void setArrowsShot(String kit, int arrowsShot) {
        this.arrowsShot.put(kit, arrowsShot);
    }

    /**
     * Set the arrow trail the player is currently using.
     * @param arrowTrail New arrow trail the player is using.
     */
    public void setArrowTrail(ArrowTrail arrowTrail) {
        String arrowTrailID = "none";

        if(arrowTrail != null) {
            arrowTrailID = arrowTrail.getId();
        }

        this.arrowTrail = arrowTrailID;

        String finalArrowTrailID = arrowTrailID;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET arrowTrail = ? WHERE uuid = ?");
                statement.setString(1, finalArrowTrailID);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
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
     * Set the kill message the player is currently using.
     * @param killMessage New kill message the player is using.
     */
    public void setKillMessage(KillMessage killMessage) {
        String killMessageId = "none";

        if(killMessage != null) {
            killMessageId = killMessage.getId();
        }

        this.killMessage = killMessageId;

        String finalKillMessageId = killMessageId;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET killMessage = ? WHERE uuid = ?");
                statement.setString(1, finalKillMessageId);
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
     * Set the total bounty the player has claimed.
     * @param lifetimeBountyClaimed new total bounty claimed.
     */
    private void setLifetimeBountyClaimed(int lifetimeBountyClaimed) {
        this.lifetimeBountyClaimed = lifetimeBountyClaimed;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_statistics SET lifetimeBountyClaimed = ? WHERE uuid = ?");
                statement.setInt(1, lifetimeBountyClaimed);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the total bounty the player has had.
     * @param lifetimeBountyHad new total bounty.
     */
    private void setLifetimeBountyHad(int lifetimeBountyHad) {
        this.lifetimeBountyHad = lifetimeBountyHad;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_statistics SET lifetimeBountyHad = ? WHERE uuid = ?");
                statement.setInt(1, lifetimeBountyHad);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the total number of coins the player has earned.
     * @param lifetimeCoins new total coins earned.
     */
    private void setLifetimeCoins(int lifetimeCoins) {
        this.lifetimeCoins = lifetimeCoins;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_statistics SET lifetimeCoins = ? WHERE uuid = ?");
                statement.setInt(1, lifetimeCoins);
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
     * Set the tag the player is currently using.
     * @param tag New tag the player is using.
     */
    public void setTag(Tag tag) {
        String tagId = "none";

        if(tag != null) {
            tagId = tag.getId();
        }

        this.tag = tagId;

        String finalTagId = tagId;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET tag = ? WHERE uuid = ?");
                statement.setString(1, finalTagId);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the trail the player is currently using.
     * @param trail New trail the player is using.
     */
    public void setTrail(Trail trail) {
        String trailID = "none";

        if(trail != null) {
            trailID = trail.getId();
        }

        this.trail = trailID;

        String finalTrailID = trailID;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_players SET trail = ? WHERE uuid = ?");
                statement.setString(1, finalTrailID);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of windows the player has broken.
     * @param windowsBroken New amount of windows broken.
     */
    private void setWindowsBroken(int windowsBroken) {
        this.windowsBroken = windowsBroken;

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE elytrapvp_statistics SET windowsBroken = ? WHERE uuid = ?");
                statement.setInt(1, windowsBroken);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
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
     * Unlock an achievement for the player.
     * @param achievementID ID of the achievement to unlock.
     */
    public void unlockAchievement(String achievementID) {
        achievements.add(achievementID);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_achievements (uuid,achievement) VALUES (?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, achievementID);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Allows a player to use an arrow trail.
     * @param arrowTrail Arrow trail to unlock.
     */
    public void unlockArrowTrail(ArrowTrail arrowTrail) {
        unlockedArrowTrails.add(arrowTrail.getId());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_cosmetics (uuid,type,cosmeticID) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, "ARROW_TRAIL");
                statement.setString(3, arrowTrail.getId());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
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
     * Allows a player to use a kill message.
     * @param killMessage Kill Message to unlock.
     */
    public void unlockKillMessage(KillMessage killMessage) {
        unlockedKillMessages.add(killMessage.getId());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_cosmetics (uuid,type,cosmeticID) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, "KILL_MESSAGE");
                statement.setString(3, killMessage.getId());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Allows a player to use a tag.
     * @param tag Tag to unlock.
     */
    public void unlockTag(Tag tag) {
        unlockedTags.add(tag.getId());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_cosmetics (uuid,type,cosmeticID) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, "TAG");
                statement.setString(3, tag.getId());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Allows a player to use an trail.
     * @param trail Trail to unlock.
     */
    public void unlockTrail(Trail trail) {
        unlockedTrails.add(trail.getId());

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO elytrapvp_cosmetics (uuid,type,cosmeticID) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, "TRAIL");
                statement.setString(3, trail.getId());
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
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,kills,deaths,killStreak,bestKillStreak,fireworksUsed,drops,arrowsHit,arrowsShot) VALUES (?,?,?,?,?,?,?,?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, getKills(kit));
                statement.setInt(4, getDeaths(kit));
                statement.setInt(5, getKillStreak(kit));
                statement.setInt(6, getBestKillStreak(kit));
                statement.setInt(7, getFireworksUsed(kit));
                statement.setInt(8, getDrops(kit));
                statement.setInt(9, getArrowsHit(kit));
                statement.setInt(10, getArrowsShot(kit));
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