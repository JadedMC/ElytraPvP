package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.parkour.ParkourTimer;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.MathUtils;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

/**
 * Displays the gamemode statistics of the player.
 */
public class StatsGUI extends CustomGUI {

    /**
     * Creates the GUI.
     * @param plugin Instance of the plugin.
     * @param player
     */
    public StatsGUI(ElytraPvP plugin, Player player) {
        super(54, "Stats");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        int kills = customPlayer.getKills("global");
        int deaths = customPlayer.getDeaths("global");

        ItemBuilder pvp = new ItemBuilder(Material.IRON_SWORD)
                .setDisplayName("&aKills: &f" + kills + " &a(" + customPlayer.getKillsRank("global") + "&a)")
                .addLore("&aDeaths: &f" + deaths);

        if(deaths == 0) {
            pvp.addLore("&aWLR: &f" + kills);
        }
        else {
            pvp.addLore("&aWLR: &f" + MathUtils.round(((double) kills)/((double) deaths), 3));
        }

        pvp.addFlag(ItemFlag.HIDE_ATTRIBUTES);
        setItem(20, pvp.build());

        ItemBuilder coins = new ItemBuilder(Material.GOLD_INGOT)
                .setDisplayName("&aCoins: &f" + customPlayer.getCoins())
                .addLore("&aBounty: &f" + customPlayer.getBounty());
        setItem(24, coins.build());

        ItemBuilder kits = new ItemBuilder(Material.BOW).setDisplayName("&a&lKit Stats")
                .addLore("")
                .addLore("&cComing Soon!");
        setItem(30, kits.build());

        ItemBuilder parkour = new ItemBuilder(Material.RED_WOOL).setDisplayName("&a&lParkour Stats")
                .addLore("")
                .addLore("&aClick to View!");
        setItem(32, parkour.build(), (p,a) -> new ParkourStatsGUI(plugin, p).open(p));

        ItemBuilder achievements = new ItemBuilder(Material.DIAMOND).setDisplayName("&a&lAchievements");
        setItem(40, achievements.build(), (p,a) -> new AchievementsGUI(plugin, p, 1).open(p));
    }

    /**
     * Displays the kit statistics of a player.
     */
    private static class KitsStatsGUI extends CustomGUI {

        /**
         * Creates the GUI
         * @param plugin Instance of the plugin.
         * @param player Player to get statistics of.
         */
        public KitsStatsGUI(ElytraPvP plugin, Player player) {
            super(54, "Stats");

            ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
            int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
            for(int i : fillers) {
                setItem(i, filler);
            }

            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        }

    }

    /**
     * Displays the Parkour statistics of a player.
     */
    private static class ParkourStatsGUI extends CustomGUI {

        /**
         * Creates the GUI.
         * @param plugin Instance of the plugin.
         * @param player Player to get stats of.
         */
        public ParkourStatsGUI(ElytraPvP plugin, Player player) {
            super(54, "Stats");

            ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
            int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
            for(int i : fillers) {
                setItem(i, filler);
            }

            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

            // Displays the Green course statistics.
            {
                ItemBuilder green = new ItemBuilder(Material.GREEN_WOOL)
                        .setDisplayName("&a&lGreen Course");
                if(customPlayer.getBestParkourTime("GREEN") != 0) {
                    green.addLore("&aBest Time: &f" + (new ParkourTimer(plugin, customPlayer.getBestParkourTime("GREEN")))  + " &a(" + customPlayer.getParkourRank("GREEN") + "&a)");
                }
                else {
                    green.addLore("&aBest Time: &cN/A");
                }
                green.addLore("&aCompletions: &f" + customPlayer.getParkourCompletions("GREEN"));
                setItem(19, green.build());
            }

            // Displays the Yellow course statistics.
            {
                ItemBuilder yellow = new ItemBuilder(Material.YELLOW_WOOL)
                        .setDisplayName("&e&lYellow &a&lCourse");
                if(customPlayer.getBestParkourTime("YELLOW") != 0) {
                    yellow.addLore("&aBest Time: &f" + (new ParkourTimer(plugin, customPlayer.getBestParkourTime("YELLOW")))  + " &a(" + customPlayer.getParkourRank("YELLOW") + "&a)");
                }
                else {
                    yellow.addLore("&aBest Time: &cN/A");
                }
                yellow.addLore("&aCompletions: &f" + customPlayer.getParkourCompletions("YELLOW"));
                setItem(21, yellow.build());
            }

            // Displays the Red course statistics.
            {
                ItemBuilder red = new ItemBuilder(Material.RED_WOOL)
                        .setDisplayName("&c&lRed &a&lCourse");
                if(customPlayer.getBestParkourTime("RED") != 0) {
                    red.addLore("&aBest Time: &f" + (new ParkourTimer(plugin, customPlayer.getBestParkourTime("RED")))  + " &a(" + customPlayer.getParkourRank("RED") + "&a)");
                }
                else {
                    red.addLore("&aBest Time: &cN/A");
                }
                red.addLore("&aCompletions: &f" + customPlayer.getParkourCompletions("RED"));
                setItem(23, red.build());
            }

            // Displays the Blue course statistics.
            {
                ItemBuilder blue = new ItemBuilder(Material.BLUE_WOOL)
                        .setDisplayName("&9&lBlue &a&lCourse");
                if(customPlayer.getBestParkourTime("BLUE") != 0) {
                    blue.addLore("&aBest Time: &f" + (new ParkourTimer(plugin, customPlayer.getBestParkourTime("BLUE")))  + " &a(" + customPlayer.getParkourRank("BLUE") + "&a)");
                }
                else {
                    blue.addLore("&aBest Time: &cN/A");
                }
                blue.addLore("&aCompletions: &f" + customPlayer.getParkourCompletions("BLUE"));
                setItem(25, blue.build());
            }

            // Displays the Beginner course statistics.
            {
                ItemBuilder beginner = new ItemBuilder(Material.RED_GLAZED_TERRACOTTA)
                        .setDisplayName("&c&lBeginner &aCourse");
                if(customPlayer.getBestParkourTime("BEGINNER") != 0) {
                    beginner.addLore("&aBest Time: &f" + (new ParkourTimer(plugin, customPlayer.getBestParkourTime("BEGINNER")))  + " &a(" + customPlayer.getParkourRank("BEGINNER") + "&a)");
                }
                else {
                    beginner.addLore("&aBest Time: &cN/A");
                }
                beginner.addLore("&aCompletions: &f" + customPlayer.getParkourCompletions("BEGINNER"));
                setItem(29, beginner.build());
            }

            // Displays the Advanced course statistics.
            {
                ItemBuilder advanced = new ItemBuilder(Material.PURPLE_GLAZED_TERRACOTTA)
                        .setDisplayName("&5&lAdvanced &aCourse");
                if(customPlayer.getBestParkourTime("ADVANCED") != 0) {
                    advanced.addLore("&aBest Time: &f" + (new ParkourTimer(plugin, customPlayer.getBestParkourTime("ADVANCED")))  + " &a(" + customPlayer.getParkourRank("ADVANCED") + "&a)");
                }
                else {
                    advanced.addLore("&aBest Time: &cN/A");
                }
                advanced.addLore("&aCompletions: &f" + customPlayer.getParkourCompletions("ADVANCED"));
                setItem(31, advanced.build());
            }

            // Displays the Peerless course statistics.
            {
                ItemBuilder peerless = new ItemBuilder(Material.BLUE_GLAZED_TERRACOTTA)
                        .setDisplayName("&9&lPeerless &aCourse");
                if(customPlayer.getBestParkourTime("PEERLESS") != 0) {
                    peerless.addLore("&aBest Time: &f" + (new ParkourTimer(plugin, customPlayer.getBestParkourTime("PEERLESS")))  + " &a(" + customPlayer.getParkourRank("PEERLESS") + "&a)");
                }
                else {
                    peerless.addLore("&aBest Time: &cN/A");
                }
                peerless.addLore("&aCompletions: &f" + customPlayer.getParkourCompletions("PEERLESS"));
                setItem(33, peerless.build());

                ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                        .setDisplayName("&cBack")
                        .build();
                setItem(0, back, (p, a) -> new StatsGUI(plugin, p).open(p));
            }
        }
    }
}