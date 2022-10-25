package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    private final ElytraPvP plugin;

    public EntityDamageListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer((Player) event.getEntity());

        if(customPlayer.getStatus() != Status.ARENA) {
            event.setCancelled(true);
        }
    }
}
