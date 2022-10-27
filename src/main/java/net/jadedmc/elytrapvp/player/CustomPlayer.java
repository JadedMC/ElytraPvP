package net.jadedmc.elytrapvp.player;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    private boolean autoDeploy = true;
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
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void addBounty(int bounty) {
        setBounty(getBounty() + bounty);
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

    public DeathType getDeathType() {
        return deathType;
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

    public Map<Integer, Integer> getKitEditor(String kit) {
        return kitEditor.get(kit);
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

    public Player getPlayer() {
        return plugin.getServer().getPlayer(uuid);
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

    public void resetBounty() {
        setBounty(0);
    }

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

    private void setBestKillStreak(String kit, int bestKillStreak) {
        this.bestKillStreak.put(kit, bestKillStreak);
        updateKitStatistics(kit);
    }

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

    private void setDeaths(String kit, int deaths) {
        this.deaths.put(kit, deaths);
        updateKitStatistics(kit);
    }

    public void setDeathType(DeathType deathType) {
        this.deathType = deathType;
    }

    private void setDrops(String kit, int drops) {
        this.drops.put(kit, drops);
        updateKitStatistics(kit);
    }

    private void setFireworksUsed(String kit, int fireworksUsed) {
        this.fireworksUsed.put(kit, fireworksUsed);
        updateKitStatistics(kit);
    }

    private void setKills(String kit, int kills) {
        this.kills.put(kit, kills);
        updateKitStatistics(kit);
    }

    private void setKillStreak(String kit, int killStreak) {
        this.killStreak.put(kit, killStreak);
        updateKitStatistics(kit);
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

    public void updateKitStatistics(String kit) {
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
}