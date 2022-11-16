package net.jadedmc.elytrapvp.game.cosmetics.tags;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import net.jadedmc.elytrapvp.game.cosmetics.CosmeticType;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

public class Tag extends Cosmetic {
    private final ElytraPvP plugin;

    public Tag(ElytraPvP plugin, String id, String name, CosmeticType unlockType) {
        super(id, name, unlockType);
        this.plugin = plugin;

        setPrice(250);
    }

    /**
     * Gets the GUI icon for the hat.
     * @param player Player to get icon for.
     * @return Icon of the hat.
     */
    @Override
    public ItemStack getIcon(Player player) {
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Checks if the player has unlocked the hat.
        if(customPlayer.getUnlockedTags().contains(getId()) || (getUnlockType() == CosmeticType.NORMAL && getPrice() == 0)) {
            ItemStack tag = new ItemBuilder(Material.NAME_TAG)
                    .setDisplayName(getName())
                    .addLore("&8Tag")
                    .addLore("")
                    .addLore("&7Click to equip")
                    .build();
            return tag;
        }

        // Check if the item is seasonal.
        if(getUnlockType() == CosmeticType.SEASONAL && plugin.seasonManager().getCurrentSeason() != getSeason()) {
            // If not, shows the purchase icon.
            ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                    .setDisplayName("&c" + getName())
                    .addLore("&8Tag")
                    .addLore("")
                    .addLore(ChatPaginator.wordWrap("&7This item can only be purchased during the " + getSeason().getName() + " &7event.", 35), "&7");
            return builder.build();
        }

        // If not, shows the purchase icon.
        ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                .setDisplayName("&c" + getName())
                .addLore("&8Tag")
                .addLore("")
                .addLore("&6Price: " + getPrice())
                .addLore("&7Click to purchase");
        return builder.build();
    }
}