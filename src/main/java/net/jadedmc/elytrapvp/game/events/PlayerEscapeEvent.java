package net.jadedmc.elytrapvp.game.events;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.player.DeathType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerEscapeEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerEscapeEvent(ElytraPvP plugin, Player player) {
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        if(customPlayer.getDeathType() != DeathType.NONE) {
            return;
        }

        customPlayer.setDeathType(DeathType.ESCAPE);
        player.setHealth(0);
    }

    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}