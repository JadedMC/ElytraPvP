package net.jadedmc.elytrapvp.game.achievements;

import net.jadedmc.elytrapvp.ElytraPvP;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages the ElytraPvP achievements.
 */
public class AchievementManager {
    private final Map<String, Achievement> achievements = new LinkedHashMap<>();

    /**
     * Creates the manager.
     * @param plugin Instance of the plugin.
     */
    public AchievementManager(ElytraPvP plugin) {

        // Loads each achievement
        achievements.put("seasonal_1", new AchievementBuilder(plugin, "seasonal_1")
                .setName("'Tis the Season")
                .setDescription("Buy a seasonal cosmetic.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("windows_1", new AchievementBuilder(plugin, "windows_1")
                .setName("Anger Issues")
                .setDescription("Destroy 1,000 Windows.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("coins_4", new AchievementBuilder(plugin, "coins_4")
                .setName("Banker")
                .setDescription("Have a total of 2.000 coins at one time.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("coins_1", new AchievementBuilder(plugin, "coins_1")
                .setName("Cha-Ching")
                .setDescription("Earn a total of 2,500 coins.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("kills_3", new AchievementBuilder(plugin, "kills_3")
                .setName("Master")
                .setDescription("Reach 1,000 kills.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("coins_3", new AchievementBuilder(plugin, "coins_3")
                .setName("Money Maker")
                .setDescription("Earn a total of 10,000 coins.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("kills_1", new AchievementBuilder(plugin, "kills_1")
                .setName("Novice")
                .setDescription("Reach 100 kills.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("kills_4", new AchievementBuilder(plugin, "kills_4")
                .setName("Obsessed")
                .setDescription("Reach 2,500 kills.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("coins_2", new AchievementBuilder(plugin, "coins_2")
                .setName("Ooh, Money")
                .setDescription("Earn a total of 5,000 coins.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("arrows_hit_1", new AchievementBuilder(plugin, "arrows_hit_1")
                .setName("Sharp Shooter")
                .setDescription("Hit players with an arrow 2,500 times.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("kills_2", new AchievementBuilder(plugin, "kills_2")
                .setName("Student")
                .setDescription("Reach 500 kills.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());
    }

    /**
     * Get an achievement based off it's id.
     * @param id ID of the achievement.
     * @return The achievement with that id.
     */
    public Achievement getAchievement(String id) {
        return achievements.get(id);
    }

    /**
     * Gets a collection of all loaded achievements.
     * @return Collection of achievements.
     */
    public Collection<Achievement> getAchievements() {
        return achievements.values();
    }
}