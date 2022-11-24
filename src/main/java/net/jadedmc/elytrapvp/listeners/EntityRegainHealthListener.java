package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthListener implements Listener {
    private final ElytraPvP plugin;

    public EntityRegainHealthListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(EntityRegainHealthEvent event) {
        if(!(event.getEntity() instanceof Player player)) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        if(customPlayer.getStatus() != Status.ARENA) {
            return;
        }

        Kit kit = plugin.kitManager().getKit(customPlayer.getKit());
        if(kit != null && !kit.canRegenerateHealth()) {
            event.setCancelled(true);
        }
    }
}