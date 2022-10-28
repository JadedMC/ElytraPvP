package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.GameScoreboard;
import net.jadedmc.elytrapvp.game.parkour.ParkourScoreboard;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.LocationUtils;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This command runs the /parkour command.
 * Used with the "PsudoCommand" plugin to make the parkour system work.
 */
public class ParkourCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    /**
     * Creates the command /parkour with permission "parkour".
     * @param plugin Instance of the plugin.
     */
    public ParkourCMD(ElytraPvP plugin) {
        super("parkour", "parkour", true);
        this.plugin = plugin;
    }

    /**
     * Executes when a command sender uses the command.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        // Sends the help command if command is not used correctly.
        if(args.length < 2) {
            ChatUtils.chat(sender, "&8&m+-----------------------***-----------------------+");
            ChatUtils.centeredChat(sender, "&a&lParkour");
            ChatUtils.chat(sender, "  &8» &a/parkour cancel");
            ChatUtils.chat(sender, "  &8» &a/parkour checkpoint [course] [player]");
            ChatUtils.chat(sender, "  &8» &a/parkour start [course] [player]");
            ChatUtils.chat(sender, "  &8» &a/parkour tp [course]");
            ChatUtils.chat(sender, "&8&m+-----------------------***-----------------------+");
            return;
        }

        switch (args[0].toLowerCase()) {
            // Executes the start sub command, which runs when a player starts a course.
            case "start" -> {
                if(args.length != 3) {
                    ChatUtils.chat(sender, "&c&lUsage &8» &c/parkour start [course] [player]");
                    return;
                }

                String course = args[1];
                Player player = Bukkit.getPlayer(args[2]);

                // Makes sure the player is online.
                if(player == null) {
                    ChatUtils.chat(sender, "&c&lError &8» &cThat player is not online!");
                    return;
                }

                plugin.parkourManager().addPlayer(player, course);
                new ParkourScoreboard(plugin, player);

                player.teleport(LocationUtils.fromConfig(plugin.settingsManager().getConfig(), "Parkour." + course.toUpperCase() + ".Location"));
            }

            // Executes the checkpoint sub command, which runs when a player finishes a course.
            case "checkpoint" -> {
                if(args.length != 3) {
                    ChatUtils.chat(sender, "&c&lUsage &8» &c/parkour checkpoint [course] [player]");
                    return;
                }

                Player player = Bukkit.getPlayer(args[2]);

                // Makes sure the player is online.
                if(player == null) {
                    ChatUtils.chat(sender, "&c&lError &8» &cThat player is not online!");
                    return;
                }

                plugin.parkourManager().getTimer(player).stop();

                ChatUtils.chat(player, "&aYou completed the &f" + plugin.parkourManager().getCourseName(player) + " &acourse in &f" + plugin.parkourManager().getTimer(player).toString() + "&a!");
                new GameScoreboard(plugin, player);

                String course = plugin.parkourManager().getCourse(player);

                CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
                customPlayer.addParkourCompletion(course);

                if(customPlayer.getBestParkourTime(course) > 0) {
                    if(customPlayer.getBestParkourTime(course) > plugin.parkourManager().getTimer(player).toMilliseconds()) {
                        ChatUtils.chat(player, "&eYou beat your previous best time of &f" + plugin.parkourManager().getTimerFromTime(customPlayer.getBestParkourTime(course)) + "&e!");
                        customPlayer.setBestParkourTime(course, plugin.parkourManager().getTimer(player).toMilliseconds());
                    }
                }
                else {
                    customPlayer.setBestParkourTime(course, plugin.parkourManager().getTimer(player).toMilliseconds());
                }

                plugin.parkourManager().removePlayer(player);
            }

            // Processes the cancel sub command, which runs when a player leaves a course.
            case "cancel" -> {
                Player player = Bukkit.getPlayer(args[1]);

                // Makes sure the player is online.
                if(player == null) {
                    ChatUtils.chat(sender, "&c&lError &8» &cThat player is not online!");
                    return;
                }

                new GameScoreboard(plugin, player);
                plugin.parkourManager().removePlayer(player);
                player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
            }

            // Executes the teleport command, is used to teleport to a specific course.
            case "teleport", "tp" -> {
                if(!(sender instanceof Player player)) {
                    ChatUtils.chat(sender, "&c&lError &8» &cOnly players can use that command!");
                    return;
                }

                player.teleport(LocationUtils.fromConfig(plugin.settingsManager().getConfig(), "Parkour." + args[1].toUpperCase() + ".Location"));
            }
        }
    }
}