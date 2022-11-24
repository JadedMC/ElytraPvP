package net.jadedmc.elytrapvp.game.kits.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PoseidonKit extends Kit {
    public PoseidonKit(ElytraPvP plugin) {
        super(plugin, "Poseidon", "poseidon");

        setDescription("Obliterate your opponents with a trident.");

        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        ItemStack trident = new ItemBuilder(Material.TRIDENT)
                .setDisplayName("&aPoseidon's Trident")
                .addEnchantment(Enchantment.LOYALTY, 5)
                .addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier)
                .setUnbreakable(true)
                .build();
        addItem(0, trident);

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setDisplayName("&aPoseidon's Bow")
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        addItem(1, bow);

        setHealth(20);
        addItem(38, new ItemBuilder(Material.ELYTRA).setDisplayName("&aElytra").setUnbreakable(true).build());
        addItem(17, new ItemBuilder(Material.ARROW, 64).setDisplayName("&aArrow").build());
        addItem(40, new ItemBuilder(Material.FIREWORK_ROCKET, 64).setDisplayName("&aFirework").build());
    }
}