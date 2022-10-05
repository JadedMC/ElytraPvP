package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.KitSelectorGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    public KitsCMD(ElytraPvP plugin) {
        super("kits", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new KitSelectorGUI(plugin, player).open(player);
    }
}