package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.inventories.LeaderboardGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaderboardCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    public LeaderboardCMD(ElytraPvP plugin) {
        super("leaderboard", "", false);
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new LeaderboardGUI(plugin, player).open(player);
    }
}
