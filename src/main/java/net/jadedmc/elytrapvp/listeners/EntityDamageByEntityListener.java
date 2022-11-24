package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * This class runs a listener that is called every time an entity is damaged by another entity.
 * This is used to keep track of when arrows do damage to players.
 */
public class EntityDamageByEntityListener implements Listener {
    private final ElytraPvP plugin;

    /**
     * Creates the Listener.
     * @param plugin Instance of the plugin.
     */
    public EntityDamageByEntityListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event EntityDamageByEntityEvent
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        // Makes sure it was a player that was damaged.
        if(!(event.getEntity() instanceof Player player)) {
            return;
        }

        // Makes sure it was an arrow that did the damage.
        if(!(event.getDamager() instanceof Arrow arrow)) {

            if(event.getDamager() instanceof Trident trident) {
                if((trident.getShooter() instanceof Player shooter)) {
                    CustomPlayer customShooter = plugin.customPlayerManager().getPlayer(shooter);

                    if(customShooter.getStatus() == Status.ARENA) {
                        event.setDamage(100);
                        return;
                    }
                    return;
                }

            }

            return;
        }

        // Makes sure the arrow shooter was a player.
        if(!(arrow.getShooter() instanceof Player shooter)) {
            return;
        }

        // Makes sure the player and shooter are not the same person.
        if(player.equals(shooter)) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(shooter);

        // Makes sure the player is in the arena.
        if(customPlayer.getStatus() != Status.ARENA) {
            return;
        }

        // Adds an arrow hit to the counter.
        customPlayer.addArrowHit(plugin.kitManager().getKit(customPlayer.getKit()));
    }
}
