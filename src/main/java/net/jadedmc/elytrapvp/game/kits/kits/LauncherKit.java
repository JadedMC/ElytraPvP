package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class LauncherKit extends Kit {

    public LauncherKit(ElytraPvP plugin) {
        super(plugin, "Launcher", "launcher");

        setIcon(Material.SLIME_BALL);
        setDescription("Dominate the planes with a knockback stick.");

        ItemBuilder stick = new ItemBuilder(Material.STICK)
                .setDisplayName("&aLauncher Stick")
                .addEnchantment(Enchantment.KNOCKBACK, 10);
        addItem(0, stick.build());

        ItemBuilder bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aStickman Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .addEnchantment(Enchantment.ARROW_KNOCKBACK, 10);
        addItem(1, bow.build());

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}