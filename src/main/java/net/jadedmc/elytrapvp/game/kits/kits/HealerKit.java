package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealerKit extends Kit {

    public HealerKit(ElytraPvP plugin) {
        super(plugin, "Healer", "healer");

        setIcon(Material.GOLDEN_APPLE);
        setDescription("Regenerate health faster to outlive your opponents.");
        setHealth(18);

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aHealer Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 2)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(0, bow);

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());

        addEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0));
    }
}