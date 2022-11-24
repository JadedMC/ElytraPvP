package net.jadedmc.elytrapvp.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.Kit;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.DeathType;
import net.jadedmc.elytrapvp.utils.chat.ChatUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.awt.*;

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
                if(customKiller.getKillMessage() != null) {
                    deathMessage = "&a&lDeath &8» " + customKiller.getKillMessage().getName().replace("%player%", player.getName()).replace("%killer%", killer.getName());
                }

                Kit killerKit = plugin.kitManager().getKit(customKiller.getKit());
                customKiller.addKill(killerKit);

                if(!killerKit.canRegenerateHealth()) {
                    killer.setHealth(killerKit.getHealth());
                }

                int bonus = (customKiller.getKillStreak("global") / 3);

                // Limits the kill streak bonus to 5 coins.
                if(bonus < 5) {
                    bonus = 5;
                }

                int coins = 5 + bonus + customPlayer.getBounty();
                killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatUtils.translate("&6+ " + coins + " Coins")));
                customKiller.addCoins(coins);
                customKiller.claimBounty(customPlayer.getBounty());

                int killStreak = customKiller.getKillStreak("global");

                Bukkit.getScheduler().runTaskLater(plugin,  () -> {
                    if(customPlayer.getBounty() > 0) {
                        ChatUtils.chat(killer, "&a&lBounty &8» &aYou have killed a wanted player and collected &f" + customPlayer.getBounty() + "&acoins.");
                        customPlayer.resetBounty();
                    }

                    if(killStreak % 3 == 0 && killStreak != 0) {
                        Bukkit.broadcastMessage(ChatUtils.translate("&a&lKill Streak &8» &f" + killer.getName() + " &ais on a kill streak of &f" + killStreak + "&a!"));

                        if(plugin.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
                            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                                EmbedBuilder message = new EmbedBuilder()
                                        .setAuthor(ChatColor.stripColor("Kill Streak » " + killer.getName() + " is on a kill streak of " + killStreak + "!"), null, "https://crafatar.com/avatars/" + killer.getUniqueId())
                                        .setColor(Color.YELLOW);
                                DiscordSRV.getPlugin().getMainTextChannel().sendMessageEmbeds(message.build()).queue();
                            });
                        }
                    }

                    if(killStreak % 5 == 0) {
                        Bukkit.broadcastMessage(ChatUtils.translate("&a&lBounty &8» &aA bounty of &f10 &acoins has been placed on &f" + killer.getName() + " &afor high kill streak."));
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

        if(plugin.getServer().getPluginManager().getPlugin("DiscordSRV") != null) {
            String finalDeathMessage = deathMessage;
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                EmbedBuilder message = new EmbedBuilder()
                        .setAuthor(ChatColor.stripColor(ChatUtils.translate(finalDeathMessage)), null, "https://crafatar.com/avatars/" + player.getUniqueId());
                DiscordSRV.getPlugin().getMainTextChannel().sendMessageEmbeds(message.build()).queue();
            });
        }
    }
}