package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.jadedcore.JadedAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AchievementCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    public AchievementCMD(ElytraPvP plugin) {
        super("achievement", "achievement", true);
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            return;
        }

        switch (args[0].toLowerCase()) {
            case "give" -> {
                if(args.length != 3) {
                    ChatUtils.chat(sender, "&c&lUsage &8» &c/achievement give [player] [achievement]");
                    return;
                }

                String achievement = args[2];
                Player player = Bukkit.getPlayer(args[1]);

                // Makes sure the player is online.
                if(player == null) {
                    ChatUtils.chat(sender, "&c&lError &8» &cThat player is not online!");
                    return;
                }

                CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

                switch (achievement) {
                    case "christmas_1" -> {
                        if(JadedAPI.getPlugin().achievementManager().getAchievement("elytrapvp_41").unlock(player)) {
                            customPlayer.addCoins(15);
                            customPlayer.unlockHat(plugin.cosmeticManager().getHat("grinch_hat"));
                        }
                    }
                    default -> {
                        ChatUtils.chat(player, "&c&lInvalid Achievement! Please Report This in our Discord! &f/discord");
                    }
                }

                //plugin.achievementManager().getAchievement(achievement).unlock(player);
            }
        }
    }
}
