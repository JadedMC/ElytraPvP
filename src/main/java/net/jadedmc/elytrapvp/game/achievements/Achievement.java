package net.jadedmc.elytrapvp.game.achievements;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import net.jadedmc.elytrapvp.game.cosmetics.arrowtrails.ArrowTrail;
import net.jadedmc.elytrapvp.game.cosmetics.hats.Hat;
import net.jadedmc.elytrapvp.game.cosmetics.killmessages.KillMessage;
import net.jadedmc.elytrapvp.game.cosmetics.tags.Tag;
import net.jadedmc.elytrapvp.game.cosmetics.trails.Trail;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.util.List;

/**
 * Represents a completable goal.
 */
public class Achievement {
    private final ElytraPvP plugin;
    private final String id;
    private final String name;
    private final String description;
    private final List<Reward> rewards;

    /**
     * Creates an achievement object.
     * @param plugin Instance of the plugin.
     * @param id ID of the achievement.
     * @param name Name of the achievement.
     * @param description Description of the achievement.
     * @param rewards Rewards of the achievement.
     */
    public Achievement(ElytraPvP plugin, String id, String name, String description, List<Reward> rewards) {
        this.plugin = plugin;
        this.id = id;
        this.name = name;
        this.description = description;
        this.rewards = rewards;
    }

    /**
     * Get the Achievement GUI icon of the Achievement.
     * @param player Player to get icon of.
     * @return Icon.
     */
    public ItemStack getIcon(Player player) {
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        ItemBuilder builder = new ItemBuilder(Material.COAL)
                .setDisplayName("&c" + name)
                .addLore(ChatPaginator.wordWrap(description, 35), "&7")
                .addLore("")
                .addLore("&7Rewards:");

        for(Reward reward : rewards) {
            builder.addLore(reward.toString());
        }

        if(customPlayer.hasAchievement(id)) {
            builder.setMaterial(Material.DIAMOND);
            builder.setDisplayName("&a" + name);
            builder.addLore("");
            builder.addLore("&aCompleted!");
        }

        return builder.build();
    }

    /**
     * Unlocks the achievement for a player.
     * @param player Player to unlock achievement for.
     */
    public void unlock(Player player) {
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Loop through rewards to apply cosmetics.
        // Useful is cosmetic rewards are added after the achievement is unlocked.
        for(Reward reward : rewards) {

            // Make sure the reward is a cosmetic.
            if(reward.getCosmetic() != null) {
                continue;
            }

            Cosmetic cosmetic = reward.getCosmetic();

            // Unlock the cosmetic.
            if(cosmetic instanceof Hat) {
                customPlayer.unlockHat((Hat) cosmetic);
            }
            else if(cosmetic instanceof KillMessage) {
                customPlayer.unlockKillMessage((KillMessage) cosmetic);
            }
            else if(cosmetic instanceof Tag) {
                customPlayer.unlockTag((Tag) cosmetic);
            }
            else if(cosmetic instanceof ArrowTrail) {
                customPlayer.unlockArrowTrail((ArrowTrail) cosmetic);
            }
            else if(cosmetic instanceof Trail) {
                customPlayer.unlockTrail((Trail) cosmetic);
            }
        }

        // Makes sure the player does not have the achievement already.
        if(customPlayer.hasAchievement(id)) {
            return;
        }

        customPlayer.unlockAchievement(id);

        // Loops through each reward
        for(Reward reward : rewards) {
            int coins = reward.getCoins();

            if(coins > 0) {
                customPlayer.addCoins(coins);
            }
        }

        ChatUtils.chat(player, "&e&k#&a>> Achievement Unlocked: &6" + name + "&a <<&e&k#");
    }

    /**
     * Represents the type of reward an achievement reward is.
     */
    public enum RewardType {
        COINS,
        COSMETIC
    }

    /**
     * Represents a prize for completing an achievement.
     */
    public static class Reward {
        private final ElytraPvP plugin;
        private final RewardType type;
        private final int coins;
        private final Cosmetic cosmetic;

        /**
         * Creates the reward.
         * @param plugin Instance of the plugin.
         * @param type Type of the reward.
         * @param coins Amount of coins being rewarded.
         */
        public Reward(ElytraPvP plugin, RewardType type, int coins) {
            this.plugin = plugin;
            this.type = type;
            this.coins = coins;
            this.cosmetic = null;
        }

        /**
         * Creates the reward.
         * @param plugin Instance of the plugin.
         * @param type Type of the reward.
         * @param cosmetic Cosmetic being rewarded.
         */
        public Reward(ElytraPvP plugin, RewardType type, Cosmetic cosmetic) {
            this.plugin = plugin;
            this.type = type;
            this.cosmetic = cosmetic;
            this.coins = 0;
        }

        /**
         * Gets the number of coins being rewarded.
         * @return Amount of coins being rewarded.
         */
        public int getCoins() {
            return coins;
        }

        /**
         * Get the cosmetic being rewarded.
         * @return Cosmetic being rewarded.
         */
        public Cosmetic getCosmetic() {
            return cosmetic;
        }

        /**
         * Get the type of reward the object is.
         * @return Reward type.
         */
        public RewardType getType() {
            return type;
        }

        /**
         * Convert the reward to a string.
         * @return String version of the reward.
         */
        public String toString() {
            switch (type) {
                case COINS -> {
                    return "  &6+" + coins + " Coins";
                }
                case COSMETIC -> {
                    return "  &a" + cosmetic.getName() + " " + cosmetic.getType();
                }
            }

            return "";
        }
    }
}