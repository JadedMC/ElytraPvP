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
                        "PRIMARY KEY (uuid)" +
                        ");");
                elytrapvp_players.execute();

                PreparedStatement elytrapvp_kits = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_kits (" +
                        "uuid VARCHAR(36)," +
                        "kit VARCHAR(24)," +
                        "PRIMARY KEY (uuid)" +
                        ");");
                elytrapvp_kits.execute();

                PreparedStatement elytrapvp_kit_editor = connection.prepareStatement("CREATE TABLE IF NOT EXISTS elytrapvp_kit_editor (" +
                        "uuid VARCHAR(36)," +
                        "item INT," +
                        "slot INT," +
                        "PRIMARY KEY (uuid)" +
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
                        "drops INT DEFAULT 0" +
                        ");");
                elytrapvp_kit_statistics.execute();
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