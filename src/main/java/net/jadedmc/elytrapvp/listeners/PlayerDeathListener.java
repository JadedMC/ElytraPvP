package net.jadedmc.elytrapvp.listeners;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.DeathType;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static net.jadedmc.elytrapvp.player.DeathType.*;

public class PlayerDeathListener implements Listener {
    private final ElytraPvP plugin;

    public PlayerDeathListener(ElytraPvP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        String deathMessage = "&a&lDeath &8» &f" + player.getName() + " &adied.";

        player.setBedSpawnLocation(plugin.arenaManager().getSelectedArena().getSpawn());

        customPlayer.addDeath(plugin.kitManager().getKit(customPlayer.getKit()));

        if(player.getKiller() != null) {
            Player killer = player.getKiller();

            // Check if player is their own killer.
            if(player.getUniqueId().equals(killer.getUniqueId())) {
                deathMessage = "&a&lDeath &8» &f" + player.getName() + " &aattacked themselves.";
            }
            else {
                CustomPlayer customKiller = plugin.customPlayerManager().getPlayer(killer);

                deathMessage = "&a&lDeath &8» &f" + player.getName() + " &awas rekt by &f" + killer.getName() + "&a.";
                customKiller.addKill(plugin.kitManager().getKit(customKiller.getKit()));

                int coins = 5 + (customKiller.getKillStreak("global") / 3) + customPlayer.getBounty();
                killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.translate("&6+ " + coins + " Coins")));
                customKiller.addCoins(coins);

                int killStreak = customKiller.getKillStreak("global");

                Bukkit.getScheduler().runTaskLater(plugin,  () -> {
                    if(customPlayer.getBounty() > 0) {
                        ChatUtils.chat(killer, "&a&lBounty &8» &aYou have killed a wanted player and collected &f" + customPlayer.getBounty() + "&acoins.");
                        customPlayer.setBounty(0);
                    }

                    if(killStreak % 3 == 0 && killStreak != 0) {
                        Bukkit.broadcastMessage(ChatUtils.translate("&a&lKill Streak &8» &f" + killer.getName() + " &ais on a kill streak of &f" + killStreak + "&a!"));
                    }

                    if(killStreak % 5 == 0) {
                        Bukkit.broadcastMessage(ChatUtils.translate("&a&lBounty &8» &aA bounty of &f10 &acoin has been placed on &f" + killer.getName() + " &afor high kill streak."));
                        customKiller.addBounty(10);
                    }
                }, 1);
            }
        }

        if(customPlayer.getDeathType() != DeathType.NONE) {
            switch (customPlayer.getDeathType()) {
                case ESCAPE -> deathMessage = "&a&lDeath &8» &f" + player.getName() + " &atried to escape.";
                case GROUND -> deathMessage = "&a&lDeath &8» &f" + player.getName() + " &atried to land.";
                case WATER -> deathMessage = "&a&lDeath &8» &f" + player.getName() + " &adrowned.";
            }
        }

        for(CustomPlayer viewer : plugin.customPlayerManager().getCustomPlayers()) {
            if(viewer.getPlayer() == player) {
                player.sendMessage(ChatUtils.translate(deathMessage));
                continue;
            }

            if(player.getKiller() != null && viewer.getPlayer() == player.getKiller()) {
                player.getKiller().sendMessage(ChatUtils.translate(deathMessage));
                continue;
            }

            if(viewer.showAllDeaths()) {
                viewer.getPlayer().sendMessage(ChatUtils.translate(deathMessage));
            }
        }

        event.setDeathMessage(null);
    }
}