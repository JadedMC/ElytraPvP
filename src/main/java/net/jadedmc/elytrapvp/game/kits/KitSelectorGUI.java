package net.jadedmc.elytrapvp.game.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.elytrapvp.utils.gui.CustomGUI;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.util.Arrays;
import java.util.List;

public class KitSelectorGUI extends CustomGUI {

    public KitSelectorGUI(ElytraPvP plugin, Player player) {
        super(54, "Kits");

        int[] slots = new int[]{11,12,13,14,15,19,20,21,22,23,24,25,29,30,31,32,33};
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        int i = 0;
        for(Kit kit : plugin.kitManager().getKits().values()) {
            ItemBuilder builder = new ItemBuilder(kit.getIcon())
                    .setDisplayName("&a" + kit.getName())
                    .addLore(ChatPaginator.wordWrap("&7" + kit.getDescription(), 35))
                    .addLore("");

            if(customPlayer.getUnlockedKits().contains(kit.getId()) || kit.getPrice() == 0) {
                builder.addLore("&7Left Click to Select")
                        .addLore("&7Right Click to Preview");
            }
            else {
                builder.setMaterial(Material.GRAY_DYE)
                        .addLore("&6Price: " + kit.getPrice() + " Coins")
                        .addLore("&7Left Click to Purchase")
                        .addLore("&7Right Click to Preview");
            }

            setItem(slots[i], builder.build(), (p,action) -> {
                if(action == ClickType.RIGHT) {
                    new PreviewKitGUI(plugin, kit).open(player);
                    return;
                }

                if(customPlayer.getUnlockedKits().contains(kit.getId()) || kit.getPrice() == 0) {
                    player.closeInventory();
                    customPlayer.setKit(kit);
                    ChatUtils.chat(player, "&a&lKit &8» &aYou have selected Kit &7" + kit.getName() + "&a.");
                    return;
                }

                if(customPlayer.getCoins() >= kit.getPrice()) {
                    customPlayer.removeCoins(kit.getPrice());
                    customPlayer.setKit(kit);
                    customPlayer.unlockKit(kit);
                    player.closeInventory();
                    ChatUtils.chat(player, "&a&lKit &8» &aYou have purchased and equipped Kit &7" + kit.getName() + "&a.");
                    return;
                }

                ChatUtils.chat(player, "&c&lError &8» &cYou do not have enough coins.");
                player.closeInventory();
            });

            i++;
        }
    }

    private void fillers() {
        List<Integer> slots = Arrays.asList(0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);
        ItemStack filler = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build();
        slots.forEach(i -> setItem(i, filler));
    }
}
