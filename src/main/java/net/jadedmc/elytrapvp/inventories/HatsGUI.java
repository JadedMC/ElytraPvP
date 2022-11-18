package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.hats.Hat;
import net.jadedmc.elytrapvp.game.cosmetics.hats.HatCategory;
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
 * Runs the Hat Selector GUI.
 */
public class HatsGUI extends CustomGUI {

    /**
     * Shows the starting hat GUI.
     * This lists each hat category.
     * @param plugin Instance of the plugin.
     */
    public HatsGUI(ElytraPvP plugin) {
        super(54, "Hats");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        ItemStack animals = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY1NTE4NDA5NTVmNTI0MzY3NTgwZjExYjM1MjI4OTM4YjY3ODYzOTdhOGYyZThjOGNjNmIwZWIwMWI1ZGIzZCJ9fX0=").setDisplayName("&a&lAnimals").build();
        setItem(19, animals, (p,a) -> new HatsGUI(plugin, p, HatCategory.ANIMALS, 1).open(p));

        ItemStack blocks = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2YzZDQ0NTlmMzBmNTMyZTliNzNiMGNjM2M2ZjNhMTBjMjBiM2U2OGYxYWYzZjQ5ZDJlZmUxMjRjYWZjZDIyMiJ9fX0=").setDisplayName("&a&lBlocks").build();
        setItem(21, blocks, (p,a) -> new HatsGUI(plugin, p, HatCategory.BLOCKS, 1).open(p));

        ItemStack food = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E5Yzg3NTM3ODBlYmMzOWMzNTFkYThlZmQ5MWJjZTkwYmQ4Y2NhN2I1MTFmOTNlNzhkZjc1ZjY2MTVjNzlhNiJ9fX0=")
                .setDisplayName("&a&lFood").build();
        setItem(23, food, (p,a) -> new HatsGUI(plugin, p, HatCategory.FOOD, 1).open(p));

        ItemStack flags = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhmZDcxMjZjZDY3MGM3OTcxYTI4NTczNGVkZmRkODAyNTcyYTcyYTNmMDVlYTQxY2NkYTQ5NDNiYTM3MzQ3MSJ9fX0=").setDisplayName("&a&lFlags").build();
        setItem(25, flags, (p,a) ->new HatsGUI(plugin, p, HatCategory.FLAGS, 1).open(p));

        setItem(0, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cBack").build(), (p,a) -> new CosmeticsGUI(plugin).open(p));
    }

    /**
     * Shows the Hat Category GUI.
     * This lists each hat in the category.
     * @param plugin Instance of the plugin.
     * @param player Player to get GUI of.
     * @param category Hat category to get GUI of.
     * @param page Page number of the GUI.
     */
    public HatsGUI(ElytraPvP plugin, Player player, HatCategory category, int page) {
        super(54, "Hats");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        int[] slots = new int[]{10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34};

        ArrayList<Hat> hats = new ArrayList<>(plugin.cosmeticManager().getHats(category));

        int s = 0;
        for(int i = ((page - 1) * 21) + 1; i <= hats.size(); i++) {
            if(i > (page * slots.length) || s > slots.length - 1) {
                break;
            }

            Hat hat = hats.get(i - 1);
            setItem(slots[s], hat.getIcon(player), (p, a) -> {
                CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

                // Sets the chat if the player has it already unlocked.
                if(customPlayer.getUnlockedHats().contains(hat.getId())) {
                    customPlayer.setHat(hat);
                    player.getInventory().setHelmet(hat.toItemStack());
                    player.closeInventory();
                    ChatUtils.chat(player, "&a&lCosmetics &8» &aHat has been equipped.");
                    return;
                }

                // Otherwise, checks the unlock requirements.
                switch (hat.getUnlockType()) {

                    // These are hats purchased with coins.
                    case NORMAL -> {
                        // If the hat has a price of 0, treat it like an unlocked hat.
                        if(hat.getPrice() == 0) {
                            customPlayer.setHat(hat);
                            player.getInventory().setHelmet(hat.toItemStack());
                            player.closeInventory();
                            ChatUtils.chat(player, "&a&lCosmetics &8» &aHat has been equipped.");
                            return;
                        }

                        // Checks if the cosmetic is seasonal.
                        if(plugin.seasonManager().getCurrentSeason() != hat.getSeason() && hat.getSeason() != Season.NONE) {
                            return;
                        }

                        // Make sure the player has enough coins.
                        if(customPlayer.getCoins() < hat.getPrice()) {
                            ChatUtils.chat(p, "&c&lError &8» &cYou do not have enough coins for that.");
                            return;
                        }

                        // Unlocks and equips the hat.
                        customPlayer.removeCoins(hat.getPrice());
                        customPlayer.unlockHat(hat);
                        customPlayer.setHat(hat);
                        player.getInventory().setHelmet(hat.toItemStack());
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aHat has been purchased and equipped.");
                    }
                }
            });
            s++;
        }

        if(page == 1) {
            setItem(38, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cNo more pages!").build());
        }
        else {
            setItem(38, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1MGI3Zjc0ZTllZDc2MzNhYTI3NGVhMzBjYzNkMmU4N2FiYjM2ZDRkMWY0Y2E2MDhjZDQ0NTkwY2NlMGIifX19").setDisplayName("&aPage " + (page - 1)).build(), (p,a) -> new HatsGUI(plugin, player, category, page - 1).open(player));
        }

        if(hats.size() > (page * 21)) {
            setItem(42, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==").setDisplayName("&aPage " + (page + 1)).build(), (p,a) -> new HatsGUI(plugin, player, category, page + 1).open(player));
        }
        else {
            setItem(42, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNmZTg4NDVhOGQ1ZTYzNWZiODc3MjhjY2M5Mzg5NWQ0MmI0ZmMyZTZhNTNmMWJhNzhjODQ1MjI1ODIyIn19fQ==").setDisplayName("&cNo more pages!").build());
        }

        setItem(0, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cBack").build(), (p,a) -> new HatsGUI(plugin).open(p));

        setItem(40, new ItemBuilder(Material.BARRIER).setDisplayName("&cReset").build(), (p, a ) -> {
            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(p);
            customPlayer.setHat(null);
            p.getInventory().clear(39);
            p.closeInventory();
            ChatUtils.chat(p, "&a&lCosmetics &8» &aHat has been reset.");
        });
    }
}