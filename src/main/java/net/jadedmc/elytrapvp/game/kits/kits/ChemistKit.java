package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.PotionBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ChemistKit extends Kit {

    public ChemistKit(ElytraPvP plugin) {
        super(plugin, "Chemist", "chemist");

        setIcon(Material.NETHER_WART);
        setDescription("Force your opponents into submission with a splash potion.");

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aChemist Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(0, bow);

        ItemStack harmPotions = new PotionBuilder(Material.SPLASH_POTION, 111)
                .addEffect(new PotionEffect(PotionEffectType.HARM, 1, 0))
                .setDisplayName("&aChemist Potion")
                .build();
        addItem(1, harmPotions);

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}