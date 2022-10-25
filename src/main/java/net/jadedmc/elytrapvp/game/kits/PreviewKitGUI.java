package net.jadedmc.elytrapvp.game.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Represents the menu where a kit is previewed.
 */
public class PreviewKitGUI extends CustomGUI {
    private final Kit kit;

    /**
     * Creates a GUI to preview kits.
     * @param kit Kit to preview.
     */
    public PreviewKitGUI(ElytraPvP plugin, Kit kit) {
        super(54, "Preview");

        this.kit = kit;
        fillers();

        for(int i : kit.getItems().keySet()) {
            setItem(slotToGUI(i), kit.getItems().get(i));
        }

        ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (player, action) -> new KitSelectorGUI(plugin, player).open(player));

        setItem(24, getHealth());
        setItem(25, getPotionEffects());
    }

    /**
     * Fills the gui with empty items. Used for details.
     */
    private void fillers() {
        List<Integer> slots = Arrays.asList(1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        slots.forEach(i -> setItem(i, filler));

        List<Integer> emptySlots = Arrays.asList(28,29,30,31,32,33,34,23,19,20,21);
        ItemStack emptyItem = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(" ").build();
        emptySlots.forEach(i -> setItem(i, emptyItem));
    }

    /**
     * Convert kit item slot to GUI slot.
     * @param slot Kit item slot.
     * @return GUI slot.
     */
    private int slotToGUI(int slot) {
        switch (slot) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return slot + 28;

            case 17:
                return 23;

            case 38:
                return 19;

            case 37:
                return 20;

            case 36:
                return 21;

            case 40:
                return 34;

            default:
                return slot;
        }
    }

    private ItemStack getPotionEffects() {
        ItemBuilder builder = new ItemBuilder(Material.POTION)
                .setDisplayName("&aPotion Effects")
                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addFlag(ItemFlag.HIDE_POTION_EFFECTS);

        for(PotionEffect effect : kit.getEffects()) {
            //String name = WordUtils.capitalize(effect.getType().getName().replace("_", " ").toLowerCase());
            String name = effect.getType().getName().replace("_", "").toLowerCase(Locale.ROOT);
            builder.addLore("&7" + name);
        }

        return builder.build();
    }

    private ItemStack getHealth() {
        int hearts = kit.getHealth()/2;

        String health = "&c";
        for(int i = 0; i < hearts; i++) {
            health+= "❤";
        }

        ItemBuilder builder = new ItemBuilder(Material.APPLE)
                .setDisplayName("&cHealth")
                .addLore(health);

        return builder.build();
    }
}