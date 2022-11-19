package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.arrowtrails.ArrowTrail;
import net.jadedmc.elytrapvp.game.cosmetics.arrowtrails.ArrowTrailCategory;
import net.jadedmc.elytrapvp.game.seasons.Season;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Runs the Arrow Trail Selector GUI.
 */
public class ArrowTrailsGUI extends CustomGUI {

    /**
     * Shows the starting arrow trail GUI.
     * This lists each hat category.
     * @param plugin Instance of the plugin.
     */
    public ArrowTrailsGUI(ElytraPvP plugin) {
        super(54, "Arrow Trails");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        ItemStack color = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDI5MzJiNjZkZWNhZWZmNmViZGM3YzViZTZiMjQ2N2FhNmYxNGI3NDYzODhhMDZhMmUxZTFhODQ2M2U5ZTEyMiJ9fX0=").setDisplayName("&a&lSolid Color").build();
        setItem(19, color, (p,a) -> new ArrowTrailsGUI(plugin, p, ArrowTrailCategory.COLOR, 1).open(p));

        ItemStack mixed = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg3ZmQyM2E3ODM2OWJkMzg3NWRhODg5NmYxNTBjNGFmOWYyMzM3NGUwNDhlMzA5MTM5MDBlM2ZkZDc3ODU5YSJ9fX0=").setDisplayName("&a&lMixed Color").build();
        setItem(21, mixed, (p,a) -> new ArrowTrailsGUI(plugin, p, ArrowTrailCategory.MIXED, 1).open(p));

        ItemStack classic = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWVhOThlOGQ2YzU2NDJlMzJhYjU2OWRhYmY5M2NkNjkwNDYwYzQ5NDc3Y2E5YjlkYmY3MjU5YjM3OTUxZjJhZCJ9fX0=")
                .setDisplayName("&a&lClassic").build();
        setItem(23, classic, (p,a) -> new ArrowTrailsGUI(plugin, p, ArrowTrailCategory.CLASSIC, 1).open(p));

        ItemStack seasonal = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhhYzE5OGI4ODUwMWM5NDhhOTMzZDUyYjExMzNlM2Y2NTAyY2M1ZmY1YWNlM2Q1YzYwYWNlYjU0NDk3NzZkIn19fQ==").setDisplayName("&a&lSeasonal").build();
        setItem(25, seasonal, (p,a) ->new ArrowTrailsGUI(plugin, p, ArrowTrailCategory.SEASONAL, 1).open(p));

        setItem(0, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cBack").build(), (p,a) -> new CosmeticsGUI(plugin).open(p));
    }

    /**
     * Shows the Arrow Trail Category GUI.
     * This lists each hat in the category.
     * @param plugin Instance of the plugin.
     * @param player Player to get GUI of.
     * @param category Arrow Trail category to get GUI of.
     * @param page Page number of the GUI.
     */
    public ArrowTrailsGUI(ElytraPvP plugin, Player player, ArrowTrailCategory category, int page) {
        super(54, "Arrow Trails");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        int[] slots = new int[]{10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34};

        ArrayList<ArrowTrail> arrowTrails = new ArrayList<>(plugin.cosmeticManager().getArrowTrails(category));

        int s = 0;
        for(int i = ((page - 1) * 21) + 1; i <= arrowTrails.size(); i++) {
            if(i > (page * slots.length) || s > slots.length - 1) {
                break;
            }

            ArrowTrail arrowTrail = arrowTrails.get(i - 1);
            setItem(slots[s], arrowTrail.getIcon(player), (p, a) -> {
                CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

                // Sets the chat if the player has it already unlocked.
                if(customPlayer.getUnlockedArrowTrails().contains(arrowTrail.getId())) {
                    customPlayer.setArrowTrail(arrowTrail);
                    player.closeInventory();
                    ChatUtils.chat(player, "&a&lCosmetics &8» &aArrow Trail has been equipped.");
                    return;
                }

                // Otherwise, checks the unlock requirements.
                switch (arrowTrail.getUnlockType()) {

                    // These are hats purchased with coins.
                    case NORMAL -> {
                        // If the hat has a price of 0, treat it like an unlocked hat.
                        if(arrowTrail.getPrice() == 0) {
                            customPlayer.setArrowTrail(arrowTrail);
                            player.closeInventory();
                            ChatUtils.chat(player, "&a&lCosmetics &8» &aArrow Trail has been equipped.");
                            return;
                        }

                        // Checks if the cosmetic is seasonal.
                        if(plugin.seasonManager().getCurrentSeason() != arrowTrail.getSeason() && arrowTrail.getSeason() != Season.NONE) {
                            return;
                        }

                        // Make sure the player has enough coins.
                        if(customPlayer.getCoins() < arrowTrail.getPrice()) {
                            ChatUtils.chat(p, "&c&lError &8» &cYou do not have enough coins for that.");
                            return;
                        }

                        // Unlocks and equips the hat.
                        customPlayer.removeCoins(arrowTrail.getPrice());
                        customPlayer.unlockArrowTrail(arrowTrail);
                        customPlayer.setArrowTrail(arrowTrail);
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aArrow Trail has been purchased and equipped.");

                        // Checks for the "'Tis the Season" achievement.
                        if(arrowTrail.getSeason() != Season.NONE) {
                            plugin.achievementManager().getAchievement("seasonal_1").unlock(player);
                        }
                    }
                }
            });
            s++;
        }

        if(page == 1) {
            setItem(38, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cNo more pages!").build());
        }
        else {
            setItem(38, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1MGI3Zjc0ZTllZDc2MzNhYTI3NGVhMzBjYzNkMmU4N2FiYjM2ZDRkMWY0Y2E2MDhjZDQ0NTkwY2NlMGIifX19").setDisplayName("&aPage " + (page - 1)).build(), (p,a) -> new ArrowTrailsGUI(plugin, player, category, page - 1).open(player));
        }

        if(arrowTrails.size() > (page * 21)) {
            setItem(42, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==").setDisplayName("&aPage " + (page + 1)).build(), (p,a) -> new ArrowTrailsGUI(plugin, player, category, page + 1).open(player));
        }
        else {
            setItem(42, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNmZTg4NDVhOGQ1ZTYzNWZiODc3MjhjY2M5Mzg5NWQ0MmI0ZmMyZTZhNTNmMWJhNzhjODQ1MjI1ODIyIn19fQ==").setDisplayName("&cNo more pages!").build());
        }

        setItem(0, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cBack").build(), (p,a) -> new ArrowTrailsGUI(plugin).open(player));

        setItem(40, new ItemBuilder(Material.BARRIER).setDisplayName("&cReset").build(), (p, a ) -> {
            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(p);
            customPlayer.setArrowTrail(null);
            p.closeInventory();
            ChatUtils.chat(p, "&a&lCosmetics &8» &aArrow Trail has been reset.");
        });
    }
}