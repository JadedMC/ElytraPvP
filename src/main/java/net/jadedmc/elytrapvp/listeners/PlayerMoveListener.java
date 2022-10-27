package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.events.PlayerDrownEvent;
import net.jadedmc.elytrapvp.game.events.PlayerEscapeEvent;
import net.jadedmc.elytrapvp.game.events.PlayerLandEvent;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.Status;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final ElytraPvP plugin;

    public PlayerMoveListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Exit if player is dead;
        if(player.isDead()) {
            return;
        }

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        switch (customPlayer.getStatus()) {
            case ARENA -> {
                // Check if the player tries to escape.
                if(!plugin.arenaManager().getSelectedArena().hasPlayer(player)) {
                    Bukkit.getPluginManager().callEvent(new PlayerEscapeEvent(plugin, player));
                    return;
                }

                // Exit if player is gliding.
                if(player.isGliding()) {
                    return;
                }

                Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
                Block block2 = player.getLocation().getBlock();
                // Calls PlayerDrownEvent if player is touching the water.
                if(block2.getType() == Material.WATER) {
                    if(block.getBlockData() instanceof Levelled data) {
                        if(data.getLevel() > 8) {
                            return;
                        }
                    }
                    Bukkit.getPluginManager().callEvent(new PlayerDrownEvent(plugin, player));
                    return;
                }

                // Calls PlayerLandEvent is the player touches the ground.
                if(plugin.arenaManager().getSelectedArena().getDeathBlocks().contains(block.getType())) {
                    Bukkit.getPluginManager().callEvent(new PlayerLandEvent(plugin, player));
                }
            }

            case  LOBBY -> {
                // Exit if kit is not selected.
                if(plugin.arenaManager().getSelectedArena().hasPlayer(player)) {
                    if(plugin.kitManager().getKit(customPlayer.getKit()) == null) {
                        player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
                        ChatUtils.chat(player, "&c&lError &8» &cYou have not selected a kit yet.");
                        return;
                    }

                    Kit kit = plugin.kitManager().getKit(customPlayer.getKit());
                    customPlayer.setStatus(Status.ARENA);
                    player.getInventory().clear();
                    kit.apply(player);
                    customPlayer.addDrop(kit);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if(customPlayer.autoDeploy()) {
                            player.setGliding(true);
                        }
                    }, 20);
                }
            }

            default -> {

            }
        }
    }
}