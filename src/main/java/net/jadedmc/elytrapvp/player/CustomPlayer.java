package net.jadedmc.elytrapvp.player;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CustomPlayer {
    private final ElytraPvP plugin;
    private final UUID uuid;
    private String kit;
    private Status status = Status.LOBBY;

    // Achievements
    private final List<String> challengeAchievements = new ArrayList<>();
    private final Map<String, Integer> tieredAchievements = new HashMap<>();

    // Kits
    private final List<String> unlockedKits = new ArrayList<>();

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

    // Kit Editor
    private final Map<String, Map<Integer, Integer>> kitEditor = new HashMap<>();

    // Settings
    private boolean showAllDeaths = true;
    private boolean showParticles = true;
    private boolean showScoreboard = true;

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
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void addCoins(int coins) {
        setCoins(getCoins() + coins);
    }

    public void addDeath(Kit kit) {
        setDeaths("global", getDeaths("global") + 1);
        setDeaths(kit.getId(), getDeaths(kit.getId()) + 1);

        setKillStreak("global", 0);
        setKillStreak(kit.getId(), 0);
    }

    public void addDrop(Kit kit) {
        setDrops("global", getDrops("global") + 1);
        setDrops(kit.getId(), getDrops(kit.getId()) + 1);
    }

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

        if(getKillStreak(kit.getId()) > getBestKillStreak(kit.getId())) {
            setBestKillStreak(kit.getId(), getKillStreak(kit.getId()));
        }
    }

    public int getBestKillStreak(String kit) {
        if(bestKillStreak.containsKey(kit)) {
            return bestKillStreak.get(kit);
        }

        return 0;
    }

    public int getBounty() {
        return bounty;
    }

    public int getCoins() {
        return coins;
    }

    public int getDeaths(String kit) {
        if(deaths.containsKey(kit)) {
            return deaths.get(kit);
        }

        return 0;
    }

    public int getDrops(String kit) {
        if(drops.containsKey(kit)) {
            return drops.get(kit);
        }

        return 0;
    }

    public int getFireworksUsed(String kit) {
        if(fireworksUsed.containsKey(kit)) {
            return fireworksUsed.get(kit);
        }

        return 0;
    }

    public int getKills(String kit) {
        if(kills.containsKey(kit)) {
            return kills.get(kit);
        }

        return 0;
    }

    public int getKillStreak(String kit) {
        if(killStreak.containsKey(kit)) {
            return killStreak.get(kit);
        }

        return 0;
    }

    public String getKit() {
        return kit;
    }

    public int getLifetimeBountyClaimed() {
        return lifetimeBountyClaimed;
    }

    public int getLifetimeBountyHad() {
        return lifetimeBountyHad;
    }

    public int getLifetimeCoins() {
        return lifetimeCoins;
    }

    public Status getStatus() {
        return status;
    }

    public List<String> getUnlockedKits() {
        return unlockedKits;
    }

    public void removeCoins(int coins) {
        setCoins(getCoins() - coins);
    }

    public void setBestKillStreak(String kit, int bestKillStreak) {
        this.bestKillStreak.put(kit, bestKillStreak);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,bestKillStreak) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, bestKillStreak);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setCoins(int coins) {
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

    public void setDeaths(String kit, int deaths) {
        this.deaths.put(kit, deaths);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,deaths) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, deaths);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setDrops(String kit, int drops) {
        this.drops.put(kit, drops);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,drops) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, drops);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setFireworksUsed(String kit, int fireworksUsed) {
        this.fireworksUsed.put(kit, fireworksUsed);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,fireworksUsed) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, fireworksUsed);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setKills(String kit, int kills) {
        this.kills.put(kit, kills);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,kills) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, kills);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void setKillStreak(String kit, int killStreak) {
        this.killStreak.put(kit, killStreak);

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("REPLACE INTO elytrapvp_kit_statistics (uuid,kit,killStreak) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, kit);
                statement.setInt(3, killStreak);
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

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

    public void setShowAllDeaths(boolean showAllDeaths) {
        this.showAllDeaths = showAllDeaths;

        // TODO: MySQL
    }

    public void setShowParticles(boolean showParticles) {
        this.showParticles = showParticles;

        // TODO: MySQL
    }

    public void setShowScoreboard(boolean showScoreboard) {
        this.showScoreboard = showScoreboard;

        // TODO: MySQL
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean showAllDeaths() {
        return showAllDeaths;
    }

    public boolean showParticles() {
        return showParticles;
    }

    public boolean showScoreboard() {
        return showScoreboard;
    }

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
}