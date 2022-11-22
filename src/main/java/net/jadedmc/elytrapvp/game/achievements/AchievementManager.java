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

        achievements.put("parkour_6", new AchievementBuilder(plugin, "parkour_6")
                .setName("A Faultless Run")
                .setDescription("Complete the red parkour course in under a minute.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_3", new AchievementBuilder(plugin, "parkour_3")
                .setName("A Hop, a Skip")
                .setDescription("Complete the yellow parkour course.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_11", new AchievementBuilder(plugin, "parkour_11")
                .setName("Advanced")
                .setDescription("Complete the second Elytra Course.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("windows_1", new AchievementBuilder(plugin, "windows_1")
                .setName("Anger Issues")
                .setDescription("Destroy 1,000 Windows.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_7", new AchievementBuilder(plugin, "parkour_7")
                .setName("An Indoor Mountain")
                .setDescription("Complete the blue parkour course.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("coins_4", new AchievementBuilder(plugin, "coins_4")
                .setName("Banker")
                .setDescription("Have a total of 2.000 coins at one time.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("bounty_1", new AchievementBuilder(plugin, "bounty_1")
                .setName("Bounty Hunter")
                .setDescription("Earn a total of 500 coins from bounties.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("coins_1", new AchievementBuilder(plugin, "coins_1")
                .setName("Cha-Ching")
                .setDescription("Earn a total of 2,500 coins.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_9", new AchievementBuilder(plugin, "parkour_9")
                .setName("Elementary")
                .setDescription("Complete the first Elytra Course.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_10", new AchievementBuilder(plugin, "parkour_10")
                .setName("Eyas")
                .setDescription("Complete the first Elytra Course in under a minute.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_1", new AchievementBuilder(plugin, "parkour)1")
                .setName("First Steps")
                .setDescription("Complete the green parkour course.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_14", new AchievementBuilder(plugin, "parkour_14")
                .setName("Haggard")
                .setDescription("Complete the third Elytra Course in under 20 minutes.")
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

        achievements.put("bounty_3", new AchievementBuilder(plugin, "bounty_3")
                .setName("Murder for Hire")
                .setDescription("Place a bounty of at least 50 coins on someone.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("kills_1", new AchievementBuilder(plugin, "kills_1")
                .setName("Novice")
                .setDescription("Reach 100 kills.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_12", new AchievementBuilder(plugin, "parkour_12")
                .setName("Passager")
                .setDescription("Complete the second Elytra Course in under 5 minutes.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_13", new AchievementBuilder(plugin, "parkour_13")
                .setName("Peerless")
                .setDescription("Complete the third Elytra Course.")
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

        achievements.put("parkour_4", new AchievementBuilder(plugin, "parkour_4")
                .setName("Seeking Flawlessness")
                .setDescription("Complete the yellow parkour course in under 45 seconds.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_8", new AchievementBuilder(plugin, "parkour_8")
                .setName("Something Like Perfection")
                .setDescription("Complete the blue parkour course in under 3 minutes.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("kills_2", new AchievementBuilder(plugin, "kills_2")
                .setName("Student")
                .setDescription("Reach 500 kills.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_2", new AchievementBuilder(plugin, "parkour_1")
                .setName("The Beginning")
                .setDescription("Complete the green parkour course in under 30 seconds.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("parkour_5", new AchievementBuilder(plugin, "parkour_5")
                .setName("Tumble and Slide")
                .setDescription("Complete the red parkour course.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("bounty_2", new AchievementBuilder(plugin, "bounty_2")
                .setName("Wanted")
                .setDescription("Have a bounty of at least 100 coins on you.")
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