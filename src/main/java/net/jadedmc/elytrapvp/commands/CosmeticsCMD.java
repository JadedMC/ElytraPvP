package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.inventories.CosmeticsGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CosmeticsCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    public CosmeticsCMD(ElytraPvP plugin) {
        super("cosmetics", "", false);
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        new CosmeticsGUI(plugin).open(player);
    }
}