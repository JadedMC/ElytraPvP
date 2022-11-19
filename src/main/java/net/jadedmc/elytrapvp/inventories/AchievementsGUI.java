package net.jadedmc.elytrapvp.inventories;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.achievements.Achievement;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Runs the achievement gui.
 */
public class AchievementsGUI extends CustomGUI {

    public AchievementsGUI(ElytraPvP plugin, Player player) {
        super(54, "Achievements");

        // Loads filler items.
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            setItem(i, filler);
        }

        // Loop through all loaded achievements and get the icon of each.
        int i = 0;
        for(Achievement achievement : plugin.achievementManager().getAchievements()) {
            setItem(i + 9, achievement.getIcon(player));
            i++;
        }
    }
}
