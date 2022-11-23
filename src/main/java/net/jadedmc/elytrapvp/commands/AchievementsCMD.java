package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.inventories.AchievementsGUI;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AchievementsCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    public AchievementsCMD(ElytraPvP plugin) {
        super("achievements", "", false);
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new AchievementsGUI(plugin, player, 1).open(player);
    }
}