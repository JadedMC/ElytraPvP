package net.jadedmc.elytrapvp.game.seasons;

import net.jadedmc.elytrapvp.ElytraPvP;

public class SeasonManager {
    private final Season currentSeason;

    public SeasonManager(ElytraPvP plugin) {
        currentSeason = Season.valueOf(plugin.settingsManager().getConfig().getString("Season"));
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }
}
