package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SniperKit extends Kit {

    public SniperKit() {
        super("Sniper", 1);

        setPrice(0);
        setHealth(12);
        setIcon(Material.BOW);
        setDescription("A strong bow to snipe your opponents.");

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aSniper Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 5)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(0, bow);

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}
