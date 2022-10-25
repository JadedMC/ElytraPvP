package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.elytrapvp.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class SpawnCMD extends AbstractCommand {
    private final ElytraPvP plugin;

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
            return;
        }

        ChatUtils.chat(player, "&a&lTeleport &8» &aTeleporting in &f5 &aseconds...");
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            customPlayer.setStatus(Status.OTHER);

            player.closeInventory();
            player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
            ItemUtils.giveLobbyItems(player);
            customPlayer.setStatus(Status.LOBBY);
        }, 100);
    }
}