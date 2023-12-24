package net.jadedmc.elytrapvp;

import net.jadedmc.elytrapvp.commands.AbstractCommand;
import net.jadedmc.elytrapvp.game.LeaderboardManager;
import net.jadedmc.elytrapvp.game.achievements.AchievementManager;
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
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedcore.features.games.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElytraPvP extends JavaPlugin {
    private AchievementManager achievementManager;
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
        achievementManager = new AchievementManager(this);

        // Commands
        AbstractCommand.registerCommands(this);

        // Game Listeners
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityRegainHealthListener(this), this);
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
        Bukkit.getPluginManager().registerEvents(new TargetHitListener(this), this);

        // Utility Listeners
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListeners(), this);

        // Utility Tasks
        new ScoreboardUpdate(this).runTaskTimer(this, 20L, 10L);

        // Registers placeholders.
        new Placeholders(this).register();

        // Register Achievements

        // Misc
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_1", "'Tis the Season", "Buy a seasonal cosmetic.", 5, "<gold>15 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_5", "Anger Issues", "Destroy 1000 Windows.", 5, "<gold>15 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_12", "Branches", "Reach the top of the tree.", 15, "<gold>30 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_13", "Bullseye", "Get a bullseye in the target practice 10 times.", 5, "<gold>20 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_18", "Eternal Storage", "Find the hidden ender chest.", 5, "<gold>25 ElytraPvP Coins", "<green>Ender Chest Hat");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_23", "Getting an Upgrade", "Unlock your first kit.", 5, "<gold>25 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_25", "Kit Collector", "Unlock every kit.", 20, "<gold>50 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_29", "Murder for Hire", "Place a bounty of at least 50 coins on someone.", 5, "<gold>15 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_36", "Sharp Shooter", "Hit players with an arrow 2,500 times.", 10, "<gold>50 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_41", "Three Sizes That Day", "Talk to the Grinch.", 5, "<gold>15 ElytraPvP Coins", "<green>Grinch Hat");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_44", "Wanted", "Have a bounty of at least 100 coins on you.", 5, "<gold>10 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_45", "Well Trained", "Hit the target practice target 500 times.", 5, "<gold>25 ElytraPvP Coins", "<green>Target Block Hat");

        // Parkour
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_2", "A Faultless Run", "Complete the red parkour course in under a minute.", 15, "<gold>35 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_3", "A hop, a skip", "Complete the yellow parkour course.", 5, "<gold>20 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_4", "Advanced", "Complete the second Elytra Course.", 15, "<gold>50 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_6", "An Indoor Mountain", "Complete the blue parkour course.", 10, "<gold>40 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_17", "Elementary", "Complete the first Elytra Course.", 5, "<gold>20 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_19", "Eyas", "Complete the first Elytra Course in under a minute.", 10, "<gold>30 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_21", "First Steps", "Complete the green parkour course.", 5, "<gold>10 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_24", "Haggard", "Complete the third Elytra Course in under 20 minutes.", 25, "<gold>150 ElytraPvP Coins", "<gold>Peerless Trail");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_31", "Passager", "Complete the second Elytra Course in under 5 minutes.", 20, "<gold>100 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_32", "Peerless", "Complete the third Elytra Course.", 20, "<gold>100 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_34", "Of Carbon and Lilies", "Obtain a Diamond in the Path of Lilies.", 15, "<gold>30 ElytraPvP Coins", "<dark_purple>Path of Lilies Trail");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_37", "Seeking Flawlessness", "Complete the yellow parkour course in under 45 seconds.", 5, "<gold>25 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_38", "Something Like Perfection", "Complete the blue parkour course in under 3 minutes.", 15, "<gold>50 ElytraPvP Coins", "<dark_purple>Ground Pro Trail");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_40", "The Beginning", "Complete the green parkour course in under 30 seconds.", 5, "<gold>15 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_43", "Tumble and Slide", "Complete the red parkour course.", 10, "<gold>30 ElytraPvP Coins");

        // Coins
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_7", "Banker", "Have a total of 2000 coins at one time.", 5, "<gold>25 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_8", "Bounty Hunter", "Earn a total of 500 coins from bounties.", 5, "<gold>30 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_14", "Cha-Ching", "Earn a total of 2,500 coins.", 5, "<gold>25 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_28", "Money Maker", "Earn a total of 10,000 coins.", 20, "<gold>100 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_35", "Ooh, Money", "Earn a total of 5,000 coins.", 10, "<gold>50 ElytraPvP Coins");

        // Kills
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_9", "Bloodbath", "Get a kill streak of 15.", 15, "<gold>45 ElytraPvP Coins", "<gold>Blood Ash Trail");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_10", "Bloodlust", "Get a kill streak of 5.", 5, "<gold>5 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_11", "Bloodthirst", "Get a kill streak of 10.", 10, "<gold>30 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_16", "Diversified", "Get 100 kills with every kit.", 20, "<gold>75 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_27", "Master", "Reach 1000 kills", 15, "<gold>100 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_30", "Novice", "Reach 100 kills.", 5, "<gold>25 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_33", "Obsessed", "Reach 2,500 kills.", 20, "<gold>200 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_39", "Student", "Reach 500 kills.", 10, "<gold>60 ElytraPvP Coins");

        // Cosmetics
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_15", "Creative Killing", "Unlock your first kill message.", 5, "<gold>15 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_20", "Fashionista", "Unlock 20 different hats.", 10, "<gold>10 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_22", "I go by Lots of Names", "Unlock 10 different tags.", 10, "<gold>10 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_26", "Looking Sharp", "Unlock your first hat.", 5, "<gold>10 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_42", "Trails Mix", "Unlock 5 different arrow trails.", 10, "<gold>50 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_46", "Who am I?", "Unlock your first tag.", 5, "<gold>25 ElytraPvP Coins");
        JadedAPI.getPlugin().achievementManager().createAchievement(Game.ELYTRAPVP, "elytrapvp_47", "Who's following me?", "Unlock your first trail.", 5, "<gold>25 ElytraPvP Coins");
    }

    /**
     * Retrieves the object managing Achievements.
     * @return Achievement manager.
     */
    public AchievementManager achievementManager() {
        return achievementManager;
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
