package net.jadedmc.elytrapvp.listeners;

import io.papermc.paper.event.block.TargetHitEvent;
import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This class runs a listener that is called every time a target block is hit with a projectile.
 * This is used to keep track of the number of targets hit and bullseyes hit.
 */
public class TargetHitListener implements Listener {
    private final ElytraPvP plugin;

    /**
     * Creates the listener.
     * @param plugin Instance of the plugin.
     */
    public TargetHitListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event TargetHitListener
     */
    @EventHandler
    public void onTargetHit(TargetHitEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        customPlayer.addTargetHit();

        if(event.getEntity().getLocation().distance(player.getLocation()) < 18) {
            System.out.println(player.getName() + " hit a bullseye from too close!");
            return;
        }

        if(event.getSignalStrength() >= 14) {
            customPlayer.addBullseye();
        }
    }
}
