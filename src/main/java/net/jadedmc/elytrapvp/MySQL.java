package net.jadedmc.elytrapvp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Manages the connection process to MySQL.
 */
public class MySQL {
    private Connection connection;

    /**
     * Loads the MySQL database connection info.
     * @param plugin Instance of the plugin.
     */
    public MySQL(ElytraPvP plugin) {
        String host = plugin.settingsManager().getConfig().getString("MySQL.host");
        String database = plugin.settingsManager().getConfig().getString("MySQL.database");
        String username = plugin.settingsManager().getConfig().getString("MySQL.username");
        String password = plugin.settingsManager().getConfig().getString("MySQL.password");
        int port = plugin.settingsManager().getConfig().getInt("MySQL.port");

        // Runs connection tasks async.
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, ()-> {
            // Attempts to connect to the MySQL database.
            try {
                synchronized(ElytraPvP.class) {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false&characterEncoding=utf8", username, password);
                }

                // Prevents losing connection to MySQL.
                plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, ()-> {
                    try {
                        connection.isValid(0);
                    }
                    catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }, 504000, 504000);
            }
            catch(SQLException | ClassNotFoundException exception) {
                // If the connection fails, logs the error.
                exception.printStackTrace();
                return;
            }

            // Create tables if they do not exist.
            try {
                PreparedStatement elytrapvp_players = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_players (" +
                        "uuid VARCHAR(36)," +
                        "kit VARCHAR(24) DEFAULT 'none'," +
                        "coins INT DEFAULT 0," +
                        "bounty INT DEFAULT 0," +
                        "hat VARCHAR(24) DEFAULT 'none'," +
                        "killMessage VARCHAR(24) DEFAULT 'none'," +
                        "tag VARCHAR(24) DEFAULT 'none'," +
                        "arrowTrail VARCHAR(24) DEFAULT 'none'," +
                        "trail VARCHAR(24) DEFAULT 'none'," +
                        "PRIMARY KEY (uuid)" +
                        ");");
                elytrapvp_players.execute();

                PreparedStatement elytrapvp_kits = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_kits (" +
                        "uuid VARCHAR(36)," +
                        "kit VARCHAR(24)," +
                        "PRIMARY KEY (uuid, kit)" +
                        ");");
                elytrapvp_kits.execute();

                PreparedStatement elytrapvp_kit_editor = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_kit_editor (" +
                        "uuid VARCHAR(36)," +
                        "kit VARCHAR(24)," +
                        "item INT," +
                        "slot INT" +
                        ");");
                elytrapvp_kit_editor.execute();

                PreparedStatement elytrapvp_kit_statistics = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_kit_statistics (" +
                        "uuid VARCHAR(36)," +
                        "kit VARCHAR(24)," +
                        "kills INT DEFAULT 0," +
                        "deaths INT DEFAULT 0," +
                        "killStreak INT DEFAULT 0," +
                        "bestKillStreak INT DEFAULT 0," +
                        "fireworksUsed INT DEFAULT 0," +
                        "drops INT DEFAULT 0," +
                        "arrowsHit INT DEFAULT 0," +
                        "arrowsShot INT DEFAULT 0," +
                        "PRIMARY KEY (uuid, kit)" +
                        ");");
                elytrapvp_kit_statistics.execute();

                PreparedStatement elytrapvp_settings = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_settings (" +
                        "uuid VARCHAR(36)," +
                        "autoDeploy BOOLEAN DEFAULT TRUE," +
                        "showScoreboard BOOLEAN DEFAULT TRUE," +
                        "showAllDeaths BOOLEAN DEFAULT TRUE," +
                        "showParticles BOOLEAN DEFAULT TRUE," +
                        "PRIMARY KEY (uuid)" +
                        ");");
                elytrapvp_settings.execute();

                PreparedStatement elytrapvp_parkour = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_parkour (" +
                        "uuid VARCHAR(36)," +
                        "course VARCHAR(24)," +
                        "bestTime BIGINT DEFAULT 0," +
                        "completions INT DEFAULT 0," +
                        "PRIMARY KEY (uuid, course)" +
                        ");");
                elytrapvp_parkour.execute();

                PreparedStatement elytrapvp_cosmetics = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_cosmetics (" +
                        "uuid VARCHAR(36)," +
                        "type VARCHAR(16)," +
                        "cosmeticID VARCHAR(24)," +
                        "PRIMARY KEY (uuid, type, cosmeticID)" +
                        ");");
                elytrapvp_cosmetics.execute();

                PreparedStatement elytrapvp_statistics = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_statistics (" +
                        "uuid VARCHAR(36)," +
                        "lifetimeCoins INT DEFAULT 0," +
                        "lifetimeBountyHad INT DEFAULT 0," +
                        "lifetimeBountyClaimed INT DEFAULT 0," +
                        "windowsBroken INT DEFAULT 0," +
                        "PRIMARY KEY (uuid)" +
                        ");");
                elytrapvp_statistics.execute();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Close a connection.
     */
    public void closeConnection() {
        if(isConnected()) {
            try {
                connection.close();
            }
            catch(SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Get the connection.
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get if plugin is connected to the database.
     * @return Connected
     */
    private boolean isConnected() {
        return (connection != null);
    }
}