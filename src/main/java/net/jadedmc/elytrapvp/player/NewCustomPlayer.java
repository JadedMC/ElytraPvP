package net.jadedmc.elytrapvp.player;

import net.jadedmc.elytrapvp.game.kits.Kit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewCustomPlayer {
    private final Player player;

    // Achievements
    private final List<String> challengeAchievements = new ArrayList<>();
    private final Map<String, Integer> tieredAchievements = new HashMap<>();

    // Kits
    private final List<Kit> unlockedKits = new ArrayList<>();

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
    private boolean showAllDeaths;
    private boolean showParticles;
    private boolean showScoreboard;

    public NewCustomPlayer(Player player) {
        this.player = player;
    }

    public Player toPlayer() {
        return player;
    }
}