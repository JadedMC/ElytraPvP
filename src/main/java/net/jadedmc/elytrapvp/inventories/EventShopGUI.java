package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import net.jadedmc.elytrapvp.game.cosmetics.CosmeticUnlockType;
import net.jadedmc.elytrapvp.game.cosmetics.arrowtrails.ArrowTrail;
import net.jadedmc.elytrapvp.game.cosmetics.hats.Hat;
import net.jadedmc.elytrapvp.game.cosmetics.killmessages.KillMessage;
import net.jadedmc.elytrapvp.game.cosmetics.tags.Tag;
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
 * Runs the Event Shop GUI.
 */
public class EventShopGUI extends CustomGUI {

    /**
     * Shows the Event Shop GUI.
     * @param plugin Instance of the plugin.
     * @param player Player to get GUI of.
     * @param page Page number of the GUI.
     */
    public EventShopGUI(ElytraPvP plugin, Player player, int page) {
        super(54, "Event Shop");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        int[] slots = new int[]{10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34};

        ArrayList<Cosmetic> cosmetics = new ArrayList<>(plugin.cosmeticManager().getCosmetics(plugin.seasonManager().getCurrentSeason()));

        int s = 0;
        for(int i = ((page - 1) * 21) + 1; i <= cosmetics.size(); i++) {
            if(i > (page * slots.length) || s > slots.length - 1) {
                break;
            }

            Cosmetic cosmetic = cosmetics.get(i - 1);
            setItem(slots[s], cosmetic.getIcon(player), (p, a) -> {
                CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

                // Checks if the cosmetic is a hat.
                if(cosmetic instanceof Hat hat) {

                    // Sets the chat if the player has it already unlocked.
                    if(customPlayer.getUnlockedHats().contains(hat.getId())) {
                        customPlayer.setHat(hat);
                        player.getInventory().setHelmet(hat.toItemStack());
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aHat has been equipped.");
                        return;
                    }

                    // Exit if the cosmetic isn't purchasable.
                    if(hat.getUnlockType() != CosmeticUnlockType.NORMAL) {
                        return;
                    }

                    // If the hat has a price of 0, treat it like an unlocked hat.
                    if(hat.getPrice() == 0) {
                        customPlayer.setHat(hat);
                        player.getInventory().setHelmet(hat.toItemStack());
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aHat has been equipped.");
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
                    plugin.achievementManager().getAchievement("seasonal_1").unlock(player);
                }

                // If not, checks if it's a kill message.
                else if(cosmetic instanceof KillMessage killMessage) {

                    // Sets the chat if the player has it already unlocked.
                    if(customPlayer.getUnlockedKillMessages().contains(killMessage.getId())) {
                        customPlayer.setKillMessage(killMessage);
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aKill Message has been equipped.");
                        return;
                    }

                    // Exit if the cosmetic isn't purchasable.
                    if(killMessage.getUnlockType() != CosmeticUnlockType.NORMAL) {
                        return;
                    }

                    // If the hat has a price of 0, treat it like an unlocked kill message.
                    if(killMessage.getPrice() == 0) {
                        customPlayer.setKillMessage(killMessage);
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aKill Message has been equipped.");
                        return;
                    }

                    // Make sure the player has enough coins.
                    if(customPlayer.getCoins() < killMessage.getPrice()) {
                        ChatUtils.chat(p, "&c&lError &8» &cYou do not have enough coins for that.");
                        return;
                    }

                    // Unlocks and equips the kill message.
                    customPlayer.removeCoins(killMessage.getPrice());
                    customPlayer.unlockKillMessage(killMessage);
                    customPlayer.setKillMessage(killMessage);
                    player.closeInventory();
                    ChatUtils.chat(player, "&a&lCosmetics &8» &aKill Message has been purchased and equipped.");
                    plugin.achievementManager().getAchievement("seasonal_1").unlock(player);
                }

                // If not, checks if it's a tag.
                else if(cosmetic instanceof Tag tag) {

                    // Sets the chat if the player has it already unlocked.
                    if(customPlayer.getUnlockedTags().contains(tag.getId())) {
                        customPlayer.setTag(tag);
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aTag has been equipped.");
                        return;
                    }

                    // Exit if the cosmetic isn't purchasable.
                    if(tag.getUnlockType() != CosmeticUnlockType.NORMAL) {
                        return;
                    }

                    // If the hat has a price of 0, treat it like an unlocked hat.
                    if(tag.getPrice() == 0) {
                        customPlayer.setTag(tag);
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aTag has been equipped.");
                        return;
                    }

                    // Make sure the player has enough coins.
                    if(customPlayer.getCoins() < tag.getPrice()) {
                        ChatUtils.chat(p, "&c&lError &8» &cYou do not have enough coins for that.");
                        return;
                    }

                    // Unlocks and equips the hat.
                    customPlayer.removeCoins(tag.getPrice());
                    customPlayer.unlockTag(tag);
                    customPlayer.setTag(tag);
                    player.closeInventory();
                    ChatUtils.chat(player, "&a&lCosmetics &8» &aTag has been purchased and equipped.");
                    plugin.achievementManager().getAchievement("seasonal_1").unlock(player);
                }

                // If not, check if it's an arrow trail.
                else if(cosmetic instanceof ArrowTrail) {
                    ArrowTrail arrowTrail = (ArrowTrail) cosmetic;

                    // Sets the chat if the player has it already unlocked.
                    if(customPlayer.getUnlockedArrowTrails().contains(arrowTrail.getId())) {
                        customPlayer.setArrowTrail(arrowTrail);
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aArrow Trail has been equipped.");
                        return;
                    }

                    // Exit if the cosmetic isn't purchasable.
                    if(arrowTrail.getUnlockType() != CosmeticUnlockType.NORMAL) {
                        return;
                    }

                    // If the hat has a price of 0, treat it like an unlocked hat.
                    if(arrowTrail.getPrice() == 0) {
                        customPlayer.setArrowTrail(arrowTrail);
                        player.closeInventory();
                        ChatUtils.chat(player, "&a&lCosmetics &8» &aArrow Trail has been equipped.");
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
                    plugin.achievementManager().getAchievement("seasonal_1").unlock(player);
                }
            });
            s++;
        }

        if(page == 1) {
            setItem(38, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cNo more pages!").build());
        }
        else {
            setItem(38, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1MGI3Zjc0ZTllZDc2MzNhYTI3NGVhMzBjYzNkMmU4N2FiYjM2ZDRkMWY0Y2E2MDhjZDQ0NTkwY2NlMGIifX19").setDisplayName("&aPage " + (page - 1)).build(), (p,a) -> new EventShopGUI(plugin, player, page - 1).open(p));
        }

        if(cosmetics.size() > (page * 21)) {
            setItem(42, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==").setDisplayName("&aPage " + (page + 1)).build(), (p,a) -> new EventShopGUI(plugin, player, page + 1).open(p));
        }
        else {
            setItem(42, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNmZTg4NDVhOGQ1ZTYzNWZiODc3MjhjY2M5Mzg5NWQ0MmI0ZmMyZTZhNTNmMWJhNzhjODQ1MjI1ODIyIn19fQ==").setDisplayName("&cNo more pages!").build());
        }

        setItem(0, new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName("&cBack").build(), (p,a) -> new CosmeticsGUI(plugin).open(p));

        setItem(40, new ItemBuilder(Material.BARRIER).setDisplayName("&cReset").build(), (p, a ) -> {
            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(p);
            customPlayer.setKillMessage(null);
            p.closeInventory();
            ChatUtils.chat(p, "&a&lCosmetics &8» &aKill Message has been reset.");
        });
    }
}