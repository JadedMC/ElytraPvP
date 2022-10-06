package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SpectralKit extends Kit {

    public SpectralKit() {
        super("Spectral", "spectral");

        setIcon(Material.SPECTRAL_ARROW);
        setDescription("Keep track of your enemies with spectral arrows.");

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aSpectral Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(0, bow);

        addItem(17, new ItemBuilder(Material.SPECTRAL_ARROW, 64).setDisplayName("&aSpectral Arrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}