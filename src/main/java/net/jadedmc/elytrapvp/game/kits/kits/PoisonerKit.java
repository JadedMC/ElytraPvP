package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class PoisonerKit extends Kit {

    public PoisonerKit(ElytraPvP plugin) {
        super(plugin, "Poisoner", "poisoner");
        setDescription("Poison your opponents with tipped arrows.");
        setIcon(Material.SPIDER_EYE);

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aPoisoner Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(0, bow);

        ItemStack arrow = new ItemStack(Material.TIPPED_ARROW, 64);
        PotionMeta arrowMeta = (PotionMeta) arrow.getItemMeta();
        arrowMeta.setBasePotionData(new PotionData(PotionType.POISON));
        arrow.setItemMeta(arrowMeta);
        addItem(17, arrow);
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}