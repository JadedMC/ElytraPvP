package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.inventories.SettingsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    public SettingsCMD(ElytraPvP plugin) {
        super("settings", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new SettingsGUI(plugin, player).open(player);
    }
}