package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TeleporterKit extends Kit {

    public TeleporterKit(ElytraPvP plugin) {
        super(plugin, "Teleporter", "teleporter");

        setIcon(Material.ENDER_PEARL);
        setDescription("Confuse your enemies with ender pearls.");

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aTeleporter Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(0, bow);

        ItemStack enderpearls = new ItemBuilder(Material.ENDER_PEARL, 64)
                .setDisplayName("&aEnder pearl")
                .build();
        addItem(2, enderpearls);

        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}