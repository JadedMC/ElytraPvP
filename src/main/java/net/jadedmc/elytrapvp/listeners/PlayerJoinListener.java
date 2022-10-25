package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.GameScoreboard;
import net.jadedmc.elytrapvp.utils.item.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final ElytraPvP plugin;

    public PlayerJoinListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.customPlayerManager().addPlayer(player);

        // Apply the Custom Scoreboard
        new GameScoreboard(plugin, player);

        player.teleport(plugin.arenaManager().getSelectedArena().getSpawn());
        ItemUtils.giveLobbyItems(player);
        player.setGameMode(GameMode.ADVENTURE);
    }
}