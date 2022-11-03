package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class LeaderboardGUI extends CustomGUI {

    public LeaderboardGUI(ElytraPvP plugin, Player player) {
        super(27, "Leaderboards");

        int[] fillers = {0,1,2,3,4,5,6,7,8,18,19,20,21,22,23,24,25,26};
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        for(int i : fillers) {
            setItem(i, filler);
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        {
            ItemBuilder kills = new ItemBuilder(Material.IRON_SWORD)
                    .setDisplayName("&a&lKills Leaderboard")
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES);

            int place = 0;
            for(String lbPlayer : plugin.leaderboardManager().getKillsLeaderboard().keySet()) {
                place++;

                kills.addLore("&a#" + place + ". &f" + lbPlayer + "&a: " + plugin.leaderboardManager().getKillsLeaderboard().get(lbPlayer));
            }

            kills.addLore("").addLore("&7Your Rank: &a" + customPlayer.getKillsRank("global"));
            setItem(11, kills.build());
        }

        {
            ItemBuilder killStreak = new ItemBuilder(Material.BOW)
                    .setDisplayName("&a&lKill Streak Leaderboard")
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES);

            int place = 0;
            for(String lbPlayer : plugin.leaderboardManager().getKillStreakLeaderboard().keySet()) {
                place++;

                killStreak.addLore("&a#" + place + ". &f" + lbPlayer + "&a: " + plugin.leaderboardManager().getKillStreakLeaderboard().get(lbPlayer));
            }

            killStreak.addLore("").addLore("&7Your Rank: &a" + customPlayer.getKillStreakRank("global"));
            setItem(15, killStreak.build());
        }
    }
}