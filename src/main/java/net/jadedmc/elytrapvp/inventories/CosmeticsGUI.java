package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Displays the main cosmetics GUI, which lets you navagate to each type of cosmetic.
 */
public class CosmeticsGUI extends CustomGUI {

    /**
     * Creates the GUI
     * @param plugin Instance of the plugin.
     */
    public CosmeticsGUI(ElytraPvP plugin) {
        super(54, "Cosmetics");

        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        ItemBuilder hats = new ItemBuilder(Material.PLAYER_HEAD).setDisplayName("&a&lHats");
        setItem(19, hats.build(), (p,a) -> new HatsGUI(plugin).open(p));

        ItemBuilder killMessages = new ItemBuilder(Material.OAK_SIGN).setDisplayName("&a&lKill Messages");
        setItem(21, killMessages.build(), (p, a) -> new KillMessagesGUI(plugin, p, 1).open(p));

        ItemBuilder tags = new ItemBuilder(Material.NAME_TAG).setDisplayName("&a&lTags");
        setItem(23, tags.build());

        ItemBuilder arrowTrails = new ItemBuilder(Material.ARROW).setDisplayName("&a&lArrow Trails");
        setItem(25, arrowTrails.build());

        ItemBuilder trails = new ItemBuilder(Material.NETHER_STAR).setDisplayName("&a&lTrails");
        setItem(29, trails.build());

        SkullBuilder comingSoon = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ==").setDisplayName("&c&lComing Soon");
        setItem(31, comingSoon.build());
        setItem(33, comingSoon.build());
    }

}