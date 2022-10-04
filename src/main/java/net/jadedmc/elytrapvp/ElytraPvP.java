package net.jadedmc.elytrapvp;

import net.jadedmc.elytrapvp.commands.AbstractCommand;
import net.jadedmc.elytrapvp.game.arena.ArenaManager;
import net.jadedmc.elytrapvp.listeners.PlayerJoinListener;
import net.jadedmc.elytrapvp.settings.SettingsManager;
import net.jadedmc.elytrapvp.utils.gui.GUIListeners;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreboardListeners;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreboardUpdate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElytraPvP extends JavaPlugin {
    private ArenaManager arenaManager;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);
        arenaManager = new ArenaManager(this);

        // Commands
        AbstractCommand.registerCommands(this);

        // Game Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        // Utility Listeners
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListeners(), this);

        // Utility Tasks
        new ScoreboardUpdate().runTaskTimer(this, 20L, 20L);
    }

    public ArenaManager arenaManager() {
        return arenaManager;
    }

    public SettingsManager settingsManager() {
        return settingsManager;
    }
}
