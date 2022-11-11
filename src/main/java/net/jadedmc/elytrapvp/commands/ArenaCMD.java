package net.jadedmc.elytrapvp.commands;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.arena.Arena;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.jadedmc.elytrapvp.utils.chat.StringUtils;
import net.jadedmc.elytrapvp.utils.item.ItemUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * This class runs the /arena command.
 * This command manages the multi-arena system.
 */
public class ArenaCMD extends AbstractCommand {
    private final ElytraPvP plugin;

    /**
     * Creates the command.
     * @param plugin Instance of the plugin.
     */
    public ArenaCMD(ElytraPvP plugin) {
        super("arena", "", false);

        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender The Command Sender.
     * @param args Arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        // Makes sure the command has arguments.
        // Adds a default if none are provided.
        if (args.length == 0) {
            args = new String[]{"help"};
        }

        switch (args[0]) {
            // Displays a help page by default.
            default -> {
                ChatUtils.chat(sender, "&8&m+-----------------------***-----------------------+");
                ChatUtils.centeredChat(sender, "&a&lArena");
                ChatUtils.chat(sender, "  &8» &a/arena list");
                ChatUtils.chat(sender, "  &8» &a/arena set <arena>");
                ChatUtils.chat(sender, "  &8» &a/arena tp <arena>");
                ChatUtils.chat(sender, "&8&m+-----------------------***-----------------------+");
            }

            // Lists the current available arenas.
            case "list" -> {
                ChatUtils.chat(sender, "&8&m+-----------------------***-----------------------+");
                ChatUtils.centeredChat(sender, "&a&lAvailable Arenas");
                for (Arena arena : plugin.arenaManager().getArenas()) {
                    ChatUtils.chat(sender, "  &8» &a" + arena.getName());
                }
                ChatUtils.chat(sender, "&8&m+-----------------------***-----------------------+");
            }

            // Allows the player to change what arena is currently active.
            case "set" -> {
                // Makes sure the command is being used properly.
                if (args.length == 1) {
                    ChatUtils.chat(sender, "&c&lUsage &8» &c/arena set <arena>");
                    return;
                }

                // Gets the arena being requested.
                String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
                Arena arena = plugin.arenaManager().getArena(name);

                // Makes sure the arena exists.
                if (arena == null) {
                    ChatUtils.chat(sender, "&c&lError &8» &cThat arena does not exist!");
                    return;
                }

                // Makes sure all players are safely moved being changing the arena.
                for(CustomPlayer customPlayer : plugin.customPlayerManager().getCustomPlayers()) {
                    customPlayer.setStatus(Status.LOBBY);
                    customPlayer.getPlayer().teleport(arena.getSpawn());
                    ItemUtils.giveLobbyItems(plugin, customPlayer.getPlayer());

                    ChatUtils.chat(customPlayer.getPlayer(), "&a&lArena &8» &aThe arena has been changed to &f" + arena.getName() + "&a.");
                }

                // Sets the new selected arena.
                plugin.arenaManager().setSelectedArena(arena);
                plugin.settingsManager().getConfig().set("SelectedArena", arena.getName());
                plugin.settingsManager().reloadConfig();
            }

            // Lets the player teleport to a set arena.
            case "tp", "teleport" -> {
                // Makes sure the command is being used properly.
                if (args.length == 1) {
                    ChatUtils.chat(sender, "&c&lUsage &8» &c/arena tp <arena>");
                    return;
                }

                // Gets the arena being requested.
                String name = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
                Arena arena = plugin.arenaManager().getArena(name);

                // Makes sure the arena exists.
                if (arena == null) {
                    ChatUtils.chat(sender, "&c&lError &8» &cThat arena does not exist!");
                    return;
                }

                // Teleports the player to the arena.
                Player player = (Player) sender;
                player.teleport(arena.getSpawn());
                ChatUtils.chat(sender, "&a&lArena &8» &aTeleporting to &7" + arena.getName() + "&a.");
            }
        }
    }
}