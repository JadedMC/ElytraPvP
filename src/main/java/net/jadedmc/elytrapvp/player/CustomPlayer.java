package net.jadedmc.elytrapvp.player;

import net.jadedmc.elytrapvp.ElytraPvP;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A wrapper for the player object, including
 * custom data.
 */
public class CustomPlayer {
    private final ElytraPvP plugin;
    public final UUID uuid;

    private int kit, hat, killMessage, tag, arrowTrail = 0;
    private final Set<Integer> kits = new HashSet<>();
    private int kills, deaths, killStreak, bestKillStreak, coins, bounty = 0;

    /**
     * Load the player's data.
     * @param plugin Instance of the plugin.
     * @param uuid UUID of the player.
     */
    public CustomPlayer(ElytraPvP plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    /**
     * Add bounty to the player.
     * @param bounty Bounty to add.
     */
    public void addBounty(int bounty) {
        setBounty(getBounty() + bounty);
    }

    /**
     * Add coins to the player.
     * @param coins Coins to add.
     */
    public void addCoins(int coins) {
        setCoins(getCoins() + coins);
    }

    /**
     * Add a death to the player.
     */
    public void addDeath() {
        setDeaths(getDeaths() + 1);
        setKillStreak(0);
    }

    /**
     * Add a kill to the player.
     */
    public void addKill() {
        setKills(getKills() + 1);
        setKillStreak(getKillStreak() + 1);
    }

    /**
     * Get the best kill streak.
     * @return Best kill streak.
     */
    public int getBestKillStreak() {
        return bestKillStreak;
    }

    /**
     * Get the current bounty.
     * @return Bounty.
     */
    public int getBounty() {
        return bounty;
    }

    /**
     * Get the current coins.
     * @return Coins.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Get the current deaths.
     * @return Current deaths.
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Get the current kills of the player.
     * @return Kills.
     */
    public int getKills() {
        return kills;
    }

    /**
     * Get the current kill streak.
     * @return Current kill streak.
     */
    public int getKillStreak() {
        return killStreak;
    }

    /**
     * Get the kit the player is using.
     * @return Kit player is using.
     */
    public int getKit() {
        return kit;
    }

    /**
     * Get unlocked kits.
     * @return All unlocked kits.
     */
    public Set<Integer> getKits() {
        return kits;
    }

    /**
     * Set the bounty of the player.
     * @param bounty New bounty.
     */
    public void setBounty(int bounty) {
        this.bounty = bounty;
    }

    /**
     * Set the coins of the player.
     * @param coins New coins.
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * Set the best kill streak of the player.
     * @param bestKillStreak New best kill streak.
     */
    public void setBestKillStreak(int bestKillStreak) {
        this.bestKillStreak = bestKillStreak;
    }

    /**
     * Set the deaths of the player.
     * @param deaths New deaths.
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * Set the kills of the player.
     * @param kills New kills.
     */
    public void setKills(int kills) {
        this.kills = kills;
    }

    /**
     * Set the kill streak of the player.
     * @param killStreak New kill streak.
     */
    public void setKillStreak(int killStreak) {
        this.killStreak = killStreak;

        if(killStreak > bestKillStreak) {
            setBestKillStreak(killStreak);
        }
    }

    /**
     * Set the kit the player is using.
     * @param kit New kit.
     */
    public void setKit(int kit) {
        this.kit = kit;
    }

    /**
     * Remove coins from the player.
     * @param coins Coins to be removed.
     */
    public void removeCoins(int coins) {
        setCoins(getCoins() - coins);
    }

    /**
     * Unlock a kit.
     * @param kit Kit to unlock.
     */
    public void unlockKit(int kit) {
        kits.add(kit);
    }
}