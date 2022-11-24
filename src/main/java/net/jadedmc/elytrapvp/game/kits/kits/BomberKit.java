package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.FireworkBuilder;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BomberKit extends Kit {

    public BomberKit(ElytraPvP plugin) {
        super(plugin, "Bomber", "bomber");

        setIcon(Material.TNT);
        setDescription("Blow your competition away with explosive fireworks.");

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aBomber Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 1)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(0, bow);

        ItemStack crossbow = new ItemBuilder(Material.CROSSBOW)
                .setDisplayName("&aBomber Crossbow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.QUICK_CHARGE, 2)
                .build();
        addItem(2, crossbow);

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(Color.RED)
                .with(FireworkEffect.Type.BALL_LARGE)
                .build();

        ItemStack fireworks = new FireworkBuilder()
                .addEffect(effect)
                .setPower(3)
                .setDisplayName("&aExplosive Fireworks")
                .addLore("&cCan't use to fly!")
                .setAmount(64)
                .build();
        addItem(40, fireworks);
        addItem(1, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());

        PotionEffect slowFalling = new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0);
        addEffect(slowFalling);

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
    }
}