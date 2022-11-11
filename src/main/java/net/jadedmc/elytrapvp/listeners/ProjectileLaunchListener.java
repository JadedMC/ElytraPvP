package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * This class runs a listener that is called every time a projectile is launched.
 * This is used to keep track of when arrows are shot by players.
 */
public class ProjectileLaunchListener implements Listener {
    private final ElytraPvP plugin;

    /**
     * Creates the listener.
     * @param plugin Instance of the plugin.
     */
    public ProjectileLaunchListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event ProjectileLaunchEvent
     */
    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {

        // Makes sure it was an arrow that was shot
        if(!(event.getEntity() instanceof Arrow arrow)) {
            return;
        }

        // Makes sure the arrow shooter was a player.
        if(!(arrow.getShooter() instanceof Player player)) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Makes sure the player is in the arena.
        if(customPlayer.getStatus() != Status.ARENA) {
            return;
        }

        // Adds an arrow shot to the counter.
        customPlayer.addArrowShot(plugin.kitManager().getKit(customPlayer.getKit()));
    }
}
