package net.jadedmc.elytrapvp.game.kits.kits;

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

    public BomberKit() {
        super("Bomber", "bomber");

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
                .build();
        addItem(1, crossbow);
        addItem(2, crossbow);
        addItem(3, crossbow);

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(Color.RED)
                .with(FireworkEffect.Type.BALL_LARGE)
                .build();

        ItemStack fireworks = new FireworkBuilder()
                .addEffect(effect)
                .setPower(3)
                .setDisplayName("&aExplosive Fireworks")
                .setAmount(64)
                .build();
        addItem(40, fireworks);
        addItem(4, new ItemBuilder(Material.FIREWORK_ROCKET, 64).build());

        PotionEffect slowFalling = new PotionEffect(PotionEffectType.SLOW_FALLING, 9999, 0);
        addEffect(slowFalling);

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
    }
}