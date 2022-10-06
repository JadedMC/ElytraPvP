package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class KnightKit extends Kit {

    public KnightKit() {
        super("Knight", "knight");

        setPrice(0);
        setIcon(Material.STONE_SWORD);
        setDescription("Slash your way into battle with a stone sword.");

        ItemStack sword = new ItemBuilder(Material.STONE_SWORD)
                .setDisplayName("&aKnight Sword")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.DAMAGE_ALL, 1)
                .build();
        addItem(0, sword);

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aSniper Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(1, bow);

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}