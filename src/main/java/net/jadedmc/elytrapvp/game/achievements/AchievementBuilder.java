package net.jadedmc.elytrapvp.game.achievements;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows easier creation of Achievement objects.
 */
public class AchievementBuilder {
    private final ElytraPvP plugin;
    private final String id;
    private String description = "Achievement Description";
    private String name = "Achievement Name";
    private final List<Achievement.Reward> rewards = new ArrayList<>();

    /**
     * Creates the builder.
     * @param plugin Instance of the plugin.
     * @param id ID of the achievement.
     */
    public AchievementBuilder(ElytraPvP plugin, String id) {
        this.plugin = plugin;
        this.id = id;
    }

    /**
     * Adds a coin reward to the cosmetic.
     * @param type Type of the reward. Should be "COINS"
     * @param coins Number of coins the reward is for.
     * @return Achievement builder.
     */
    public AchievementBuilder addReward(Achievement.RewardType type, int coins) {
        rewards.add(new Achievement.Reward(plugin, type, coins));
        return this;
    }

    /**
     * Adds a cosmetic reward to the cosmetic.
     * @param type Type of the reward. Should be "COSMETIC".
     * @param cosmetic Cosmetic to be rewarded.
     * @return Achievement builder.
     */
    public AchievementBuilder addReward(Achievement.RewardType type, Cosmetic cosmetic) {
        rewards.add(new Achievement.Reward(plugin, type, cosmetic));
        return this;
    }

    /**
     * Sets the description of the achievement.
     * @param description Description of the achievement.
     * @return Achievement builder.
     */
    public AchievementBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the name of the achievement.
     * @param name Name of the achievement.
     * @return Achievement builder.
     */
    public AchievementBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Creates the achievement object,
     * @return Built achievement object.
     */
    public Achievement build() {
        return new Achievement(plugin, id, name, description, rewards);
    }
}