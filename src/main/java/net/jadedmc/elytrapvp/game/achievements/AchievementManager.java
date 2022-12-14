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
                .addReward(Achievement.RewardType.COINS, 15)
                .build());

        achievements.put("parkour_6", new AchievementBuilder(plugin, "parkour_6")
                .setName("A Faultless Run")
                .setDescription("Complete the red parkour course in under a minute.")
                .addReward(Achievement.RewardType.COINS, 35)
                .build());

        achievements.put("parkour_3", new AchievementBuilder(plugin, "parkour_3")
                .setName("A Hop, a Skip")
                .setDescription("Complete the yellow parkour course.")
                .addReward(Achievement.RewardType.COINS, 20)
                .build());

        achievements.put("parkour_11", new AchievementBuilder(plugin, "parkour_11")
                .setName("Advanced")
                .setDescription("Complete the second Elytra Course.")
                .addReward(Achievement.RewardType.COINS, 50)
                .build());

        achievements.put("windows_1", new AchievementBuilder(plugin, "windows_1")
                .setName("Anger Issues")
                .setDescription("Destroy 1,000 Windows.")
                .addReward(Achievement.RewardType.COINS, 15)
                .build());

        achievements.put("parkour_7", new AchievementBuilder(plugin, "parkour_7")
                .setName("An Indoor Mountain")
                .setDescription("Complete the blue parkour course.")
                .addReward(Achievement.RewardType.COINS, 40)
                .build());

        achievements.put("coins_4", new AchievementBuilder(plugin, "coins_4")
                .setName("Banker")
                .setDescription("Have a total of 2.000 coins at one time.")
                .addReward(Achievement.RewardType.COINS, 25)
                .build());

        achievements.put("bounty_1", new AchievementBuilder(plugin, "bounty_1")
                .setName("Bounty Hunter")
                .setDescription("Earn a total of 500 coins from bounties.")
                .addReward(Achievement.RewardType.COINS, 30)
                .build());

        achievements.put("kill_streak_3", new AchievementBuilder(plugin, "kill_streak_3")
                .setName("Bloodbath")
                .setDescription("Get a kill streak of 15.")
                .addReward(Achievement.RewardType.COINS, 45)
                .addReward(Achievement.RewardType.COSMETIC, plugin.cosmeticManager().getCosmetic("blood_ash_trail"))
                .build());

        achievements.put("kill_streak_1", new AchievementBuilder(plugin, "kill_streak_1")
                .setName("Bloodlust")
                .setDescription("Get a kill streak of 5.")
                .addReward(Achievement.RewardType.COINS, 15)
                .build());

        achievements.put("kill_streak_2", new AchievementBuilder(plugin, "kill_streak_2")
                .setName("Bloodthirst")
                .setDescription("Get a kill streak of 10.")
                .addReward(Achievement.RewardType.COINS, 30)
                .build());

        achievements.put("tree_1", new AchievementBuilder(plugin, "tree_1")
                .setName("Branches")
                .setDescription("Reach the top of the tree.")
                .addReward(Achievement.RewardType.COINS, 30)
                .build());

        achievements.put("target_2", new AchievementBuilder(plugin, "target_2")
                .setName("Bullseye")
                .setDescription("Get a bullseye in the target practice 10 times.")
                .addReward(Achievement.RewardType.COINS, 20)
                .build());

        achievements.put("coins_1", new AchievementBuilder(plugin, "coins_1")
                .setName("Cha-Ching")
                .setDescription("Earn a total of 2,500 coins.")
                .addReward(Achievement.RewardType.COINS, 25)
                .build());

        achievements.put("kill_messages_1", new AchievementBuilder(plugin, "kill_messages_1")
                .setName("Creative Killing")
                .setDescription("Unlock your first kill message.")
                .addReward(Achievement.RewardType.COINS, 15)
                .build());

        achievements.put("kits_3", new AchievementBuilder(plugin, "kits_3")
                .setName("Diversified")
                .setDescription("Get 100 kills with every kit.")
                .addReward(Achievement.RewardType.COINS, 75)
                .build());

        achievements.put("parkour_9", new AchievementBuilder(plugin, "parkour_9")
                .setName("Elementary")
                .setDescription("Complete the first Elytra Course.")
                .addReward(Achievement.RewardType.COINS, 20)
                .build());

        achievements.put("target_3", new AchievementBuilder(plugin, "target_3")
                .setName("Eternal Storage")
                .setDescription("Find the hidden ender chest.")
                .addReward(Achievement.RewardType.COINS, 25)
                .addReward(Achievement.RewardType.COSMETIC, plugin.cosmeticManager().getCosmetic("ender_chest_hat"))
                .build());

        achievements.put("parkour_10", new AchievementBuilder(plugin, "parkour_10")
                .setName("Eyas")
                .setDescription("Complete the first Elytra Course in under a minute.")
                .addReward(Achievement.RewardType.COINS, 30)
                .build());

        achievements.put("hats_2", new AchievementBuilder(plugin, "hats_2")
                .setName("Fashionista")
                .setDescription("Unlock 20 different hats.")
                .addReward(Achievement.RewardType.COINS, 50)
                .build());

        achievements.put("parkour_1", new AchievementBuilder(plugin, "parkour_1")
                .setName("First Steps")
                .setDescription("Complete the green parkour course.")
                .addReward(Achievement.RewardType.COINS, 10)
                .build());

        achievements.put("tags_2", new AchievementBuilder(plugin, "tags_2")
                .setName("I go by Lots of Names")
                .setDescription("Unlock 10 different tags.")
                .addReward(Achievement.RewardType.COINS, 10)
                .build());

        achievements.put("kits_1", new AchievementBuilder(plugin, "kits_1")
                .setName("Getting an Upgrade")
                .setDescription("Unlock your first kit.")
                .addReward(Achievement.RewardType.COINS, 25)
                .build());

        achievements.put("parkour_14", new AchievementBuilder(plugin, "parkour_14")
                .setName("Haggard")
                .setDescription("Complete the third Elytra Course in under 20 minutes.")
                .addReward(Achievement.RewardType.COINS, 150)
                .addReward(Achievement.RewardType.COSMETIC, plugin.cosmeticManager().getCosmetic("peerless_trail"))
                .build());

        achievements.put("kits_2", new AchievementBuilder(plugin, "kits_2")
                .setName("Kit Collector")
                .setDescription("Unlock every kit.")
                .addReward(Achievement.RewardType.COINS, 50)
                .build());

        achievements.put("hats_1", new AchievementBuilder(plugin, "hats_1")
                .setName("Looking Sharp")
                .setDescription("Unlock your first hat.")
                .addReward(Achievement.RewardType.COINS, 10)
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
                .addReward(Achievement.RewardType.COINS, 15)
                .build());

        achievements.put("kills_1", new AchievementBuilder(plugin, "kills_1")
                .setName("Novice")
                .setDescription("Reach 100 kills.")
                .addReward(Achievement.RewardType.COINS, 25)
                .build());

        achievements.put("parkour_12", new AchievementBuilder(plugin, "parkour_12")
                .setName("Passager")
                .setDescription("Complete the second Elytra Course in under 5 minutes.")
                .addReward(Achievement.RewardType.COINS, 75)
                .build());

        achievements.put("parkour_13", new AchievementBuilder(plugin, "parkour_13")
                .setName("Peerless")
                .setDescription("Complete the third Elytra Course.")
                .addReward(Achievement.RewardType.COINS, 100)
                .build());

        achievements.put("kills_4", new AchievementBuilder(plugin, "kills_4")
                .setName("Obsessed")
                .setDescription("Reach 2,500 kills.")
                .addReward(Achievement.RewardType.COINS, 200)
                .build());

        achievements.put("parkour_15", new AchievementBuilder(plugin, "parkour_15")
                .setName("Of Carbon and Lilies")
                .setDescription("Obtain a Diamond in the Path of Lilies")
                .addReward(Achievement.RewardType.COINS, 30)
                .addReward(Achievement.RewardType.COSMETIC, plugin.cosmeticManager().getCosmetic("lilies_trail"))
                .build());

        achievements.put("coins_2", new AchievementBuilder(plugin, "coins_2")
                .setName("Ooh, Money")
                .setDescription("Earn a total of 5,000 coins.")
                .addReward(Achievement.RewardType.COINS, 50)
                .build());

        achievements.put("arrows_hit_1", new AchievementBuilder(plugin, "arrows_hit_1")
                .setName("Sharp Shooter")
                .setDescription("Hit players with an arrow 2,500 times.")
                .addReward(Achievement.RewardType.COINS, 50)
                .build());

        achievements.put("parkour_4", new AchievementBuilder(plugin, "parkour_4")
                .setName("Seeking Flawlessness")
                .setDescription("Complete the yellow parkour course in under 45 seconds.")
                .addReward(Achievement.RewardType.COINS, 25)
                .build());

        achievements.put("parkour_8", new AchievementBuilder(plugin, "parkour_8")
                .setName("Something Like Perfection")
                .setDescription("Complete the blue parkour course in under 3 minutes.")
                .addReward(Achievement.RewardType.COINS, 50)
                .addReward(Achievement.RewardType.COSMETIC, plugin.cosmeticManager().getCosmetic("ground_pro_trail"))
                .build());

        achievements.put("kills_2", new AchievementBuilder(plugin, "kills_2")
                .setName("Student")
                .setDescription("Reach 500 kills.")
                .addReward(Achievement.RewardType.COINS, 60)
                .build());

        achievements.put("parkour_2", new AchievementBuilder(plugin, "parkour_1")
                .setName("The Beginning")
                .setDescription("Complete the green parkour course in under 30 seconds.")
                .addReward(Achievement.RewardType.COINS, 15)
                .build());

        achievements.put("christmas_1", new AchievementBuilder(plugin, "christmas_builder")
                .setName("Three Sizes That Day")
                .setDescription("Talk to the Grinch.")
                .addReward(Achievement.RewardType.COINS, 15)
                .addReward(Achievement.RewardType.COSMETIC, plugin.cosmeticManager().getCosmetic("grinch_hat"))
                .build());

        achievements.put("arrow_trails_2", new AchievementBuilder(plugin, "arrow_trails_2")
                .setName("Trails Mix")
                .setDescription("Unlock 5 different arrow trails.")
                .addReward(Achievement.RewardType.COINS, 50)
                .build());

        achievements.put("parkour_5", new AchievementBuilder(plugin, "parkour_5")
                .setName("Tumble and Slide")
                .setDescription("Complete the red parkour course.")
                .addReward(Achievement.RewardType.COINS, 30)
                .build());

        achievements.put("bounty_2", new AchievementBuilder(plugin, "bounty_2")
                .setName("Wanted")
                .setDescription("Have a bounty of at least 100 coins on you.")
                .addReward(Achievement.RewardType.COINS, 10)
                .build());

        achievements.put("target_1", new AchievementBuilder(plugin, "target_1")
                .setName("Well Trained")
                .setDescription("Hit the target practice target 500 times.")
                .addReward(Achievement.RewardType.COINS, 25)
                .addReward(Achievement.RewardType.COSMETIC, plugin.cosmeticManager().getCosmetic("target_block_hat"))
                .build());

        achievements.put("tags_1", new AchievementBuilder(plugin, "tags_1")
                .setName("Who am I?")
                .setDescription("Unlock your first tag.")
                .addReward(Achievement.RewardType.COINS, 25)
                .build());

        achievements.put("trails_1", new AchievementBuilder(plugin, "trails_1")
                .setName("Who's Following Me?")
                .setDescription("Unlock your first trail.")
                .addReward(Achievement.RewardType.COINS, 20)
                .build());
    }

    /**
     * Get an achievement based off its id.
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