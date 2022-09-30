package net.jadedmc.elytrapvp;

import net.jadedmc.elytrapvp.utils.gui.GUIListeners;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreboardListeners;
import net.jadedmc.elytrapvp.utils.scoreboard.ScoreboardUpdate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElytraPvP extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic


        // Utility Listeners
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ScoreboardListeners(), this);

        // Utility Tasks
        new ScoreboardUpdate().runTaskTimer(this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
