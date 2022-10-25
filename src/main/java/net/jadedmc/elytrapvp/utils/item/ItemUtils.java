package net.jadedmc.elytrapvp.utils.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ItemUtils {
    public static void giveLobbyItems(Player player) {
        player.getInventory().clear();

        ItemBuilder cosmetics = new ItemBuilder(Material.EMERALD).setDisplayName("&a&lCosmetics");
        ItemBuilder kits = new ItemBuilder(Material.NETHER_STAR).setDisplayName("&a&lKits");
        ItemBuilder stats = new ItemBuilder(Material.PAPER).setDisplayName("&a&lStats");
        ItemBuilder settings = new ItemBuilder(Material.COMPARATOR).setDisplayName("&a&lSettings");

        player.getInventory().setItem(1, cosmetics.build());
        player.getInventory().setItem(4, kits.build());
        player.getInventory().setItem(7, stats.build());
        player.getInventory().setItem(8, settings.build());

        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }
}