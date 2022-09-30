package net.jadedmc.elytrapvp.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class GUIListeners implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        UUID pU = p.getUniqueId();
        UUID iU = CustomGUI.getOpenInventories().get(pU);

        if(iU == null) {
            return;
        }

        e.setCancelled(true);
        CustomGUI gui = CustomGUI.getInventories().get(iU);
        CustomGUI.ClickAction action = gui.getActions().get(e.getSlot());

        if(action != null) {
            action.click(p, e.getClick());
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        UUID pU = p.getUniqueId();

        if(CustomGUI.getOpenInventories().containsKey(pU)) {
            UUID iU = CustomGUI.getOpenInventories().get(pU);
            CustomGUI gui = CustomGUI.getInventories().get(iU);
            gui.delete();

            CustomGUI.getOpenInventories().remove(pU);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID u = CustomGUI.getOpenInventories().get(e.getPlayer().getUniqueId());
        CustomGUI gui = CustomGUI.getInventories().get(u);

        if(gui != null) {
            gui.delete();
        }

        CustomGUI.getInventories().remove(e.getPlayer().getUniqueId());
    }
}