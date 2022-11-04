package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.inventories.StatsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class runs the /stats command, which opens a gui displaying the player's game statistics.
 */
public class StatsCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    /**
     * Registers the command.
     * @param plugin  Instance of the plugin.
     */
    public StatsCMD(ElytraPvP plugin) {
        super("stats", "", false);
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new StatsGUI(plugin, player).open(player);
    }
}