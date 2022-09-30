package net.jadedmc.elytrapvp.game.arena;

import net.jadedmc.elytrapvp.ElytraPvP;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {
    private Arena selectedArena;
    private final List<Arena> arenas = new ArrayList<>();

    public ArenaManager(ElytraPvP plugin) {
        FileConfiguration arenasConfig = plugin.settingsManager().getArenas();
        ConfigurationSection section = arenasConfig.getConfigurationSection("Arenas");

        for(String arena : section.getKeys(false)) {
            arenas.add(new Arena(plugin, arena));
        }

        selectedArena = getArena(plugin.settingsManager().getConfig().getString("Arena"));
    }

    /**
     * Get an arena by its name.
     * @param name Name of the arena.
     * @return Arena object.
     */
    public Arena getArena(String name) {
        for(Arena arena : arenas) {
            if(arena.getName().equalsIgnoreCase(name)) {
                return arena;
            }
        }

        return null;
    }

    /**
     * Get all registered arenas.
     * @return All arenas.
     */
    public List<Arena> getArenas() {
        return arenas;
    }

    /**
     * Get the currently selected arena.
     * @return Selected Arena.
     */
    public Arena getSelectedArena() {
        return selectedArena;
    }

    /**
     * Sets the current arena selected for use.
     * @param selectedArena Arena selected for use.
     */
    public void setSelectedArena(Arena selectedArena) {
        this.selectedArena = selectedArena;
    }
}