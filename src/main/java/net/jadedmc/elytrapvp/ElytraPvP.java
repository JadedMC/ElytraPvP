package net.jadedmc.elytrapvp;

import net.jadedmc.elytrapvp.commands.AbstractCommand;
import net.jadedmc.elytrapvp.game.LeaderboardManager;
import net.jadedmc.elytrapvp.game.arena.ArenaManager;
import net.jadedmc.elytrapvp.game.cosmetics.CosmeticManager;
import net.jadedmc.elytrapvp.game.kits.KitManager;
import net.jadedmc.elytrapvp.game.parkour.ParkourManager;
import net.jadedmc.elytrapvp.game.seasons.SeasonManager;
import net.jadedmc.elytrapvp.listeners.*;
import net.jadedmc.elytrapvp.player.CustomPlayerManager;
import net.jadedmc.elytrapvp.settings.SettingsManager;
import net.jadedmc.elytrapvp.utils.gui.GUIListeners;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreboardListeners;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreboardUpdate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElytraPvP extends JavaPlugin {
    private ArenaManager arenaManager;
    private CosmeticManager cosmeticManager;
    private CustomPlayerManager customPlayerManager;
    private KitManager kitManager;
    private LeaderboardManager leaderboardManager;
    private ParkourManager parkourManager;
    private SeasonManager seasonManager;
    private SettingsManager settingsManager;
    private MySQL mySQL;

    /**
     * Runs when the server first runs the plugin.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);
        mySQL = new MySQL(this);
        arenaManager = new ArenaManager(this);
        kitManager = new KitManager(this);
        customPlayerManager = new CustomPlayerManager(this);
        parkourManager = new ParkourManager(this);
        leaderboardManager = new LeaderboardManager(this);
        cosmeticManager = new CosmeticManager(this);
        seasonManager = new SeasonManager(this);

        // Commands
        AbstractCommand.registerCommands(this);

        // Game Listeners
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileLaunchListener(this), this);

        // Utility Listeners
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListeners(), this);

        // Utility Tasks
        new ScoreboardUpdate(this).runTaskTimer(this, 20L, 20L);

        // Registers placeholders.
        new Placeholders(this).register();
    }

    /**
     * Retrieves the object managing Arenas.
     * @return Arena Manager.
     */
    public ArenaManager arenaManager() {
        return arenaManager;
    }

    /**
     * Retrieves the object managing cosmetics.
     * @return Cosmetic manager.
     */
    public CosmeticManager cosmeticManager() {
        return cosmeticManager;
    }

    /**
     * Retrieves the object managing Custom Players.
     * @return Custom Player manager.
     */
    public CustomPlayerManager customPlayerManager() {
        return customPlayerManager;
    }

    /**
     * Retrieves the object managing kits.
     * @return Kit manager.
     */
    public KitManager kitManager() {
        return kitManager;
    }

    /**
     * Retrieves the object managing leaderboards.
     * @return Leaderboard Manager.
     */
    public LeaderboardManager leaderboardManager() {
        return leaderboardManager;
    }

    /**
     * Retrieves the object managing MySQL connections.
     * @return MySQL manager.
     */
    public MySQL mySQL() {
        return mySQL;
    }

    /**
     * Retrieves the object managing parkour courses.
     * @return Parkour Manager.
     */
    public ParkourManager parkourManager() {
        return parkourManager;
    }

    /**
     * Retrieves the object managing seasons.
     * @return Season manager.
     */
    public SeasonManager seasonManager() {
        return seasonManager;
    }

    /**
     * Retrieves the object managing plugin configuration files.
     * @return Settings Manager.
     */
    public SettingsManager settingsManager() {
        return settingsManager;
    }
}
