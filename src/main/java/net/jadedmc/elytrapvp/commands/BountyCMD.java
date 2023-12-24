package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.jadedcore.JadedAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This class runs the /bounty command.
 * This command manages the bounty system.
 */
public class BountyCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    /**
     * Creates the command.
     * @param plugin Instance of the plugin.
     */
    public BountyCMD(ElytraPvP plugin) {
        super("bounty", "", false);
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

        // Exit if no args
        if(args.length == 0) {
            ChatUtils.chat(player, "&a&lBounty &8» &aThere is currently a bounty of &f" + customPlayer.getBounty() + " &acoins for you.");
            return;
        }

        // Exit if not enough args
        if(args.length < 3 || args[1].equalsIgnoreCase("add")) {
            ChatUtils.chat(player, "&c&lUsage &8» &c/bounty add [player] [amount]");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        int coins;
        try {
            coins = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e) {
            ChatUtils.chat(player, "&c&lUsage &8» &c/bounty add [player] [amount]");
            return;
        }

        // Exit if player is not online
        if(target == null) {
            ChatUtils.chat(player, "&c&lError &8» &cThat player is not online.");
            return;
        }

        // Exit if invalid coin amount.
        if(coins < 1) {
            ChatUtils.chat(player, "&c&lError &8» &cMust be at least 1 coin.");
            return;
        }

        // Exit if not enough coins.
        if(customPlayer.getCoins() < coins) {
            ChatUtils.chat(player, "&c&lError &8» &cYou do not have enough coins.");
            return;
        }

        CustomPlayer customTarget = plugin.customPlayerManager().getPlayer(target);
        customTarget.addBounty(coins);
        customPlayer.removeCoins(coins);

        Bukkit.broadcastMessage(ChatUtils.translate("&a&lBounty &8» &f" + player.getName() + " &ahas placed a bounty of &f" + coins + " &acoins for &f" + target.getName() + "&a."));

        // Checks for the "Murder for Hire" achievement.
        if(coins >= 50) {
            if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_29").unlock(player)) {
                customPlayer.addCoins(15);
            }
        }
    }
}