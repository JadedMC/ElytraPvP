package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.GameScoreboard;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.elytrapvp.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Runs the /spawn command, which teleports the player to the selected arena's spawn.
 */
public class SpawnCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    /**
     * Creates the command with no permissions.
     * @param plugin Instance of the plugin.
     */
    public SpawnCMD(ElytraPvP plugin) {
        super("spawn", "", false);
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Run if player is not in arena.
        if(customPlayer.getStatus() != Status.ARENA) {
            player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());

            // Removes the player from parkour mode.

            if(plugin.parkourManager().getTimer(player) != null) {
                plugin.parkourManager().getTimer(player).stop();
                new GameScoreboard(plugin, player);
                plugin.parkourManager().removePlayer(player);
            }

            return;
        }

        // Sends a teleporting message to the player.
        ChatUtils.chat(player, "&a&lTeleport &8» &aTeleporting in &f5 &aseconds...");

        // Creates a scheduler that runs 5 seconds after running the command.
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            customPlayer.setStatus(Status.OTHER);

            player.closeInventory();
            player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
            ItemUtils.giveLobbyItems(plugin, player);
            customPlayer.setStatus(Status.LOBBY);
        }, 100);
    }
}