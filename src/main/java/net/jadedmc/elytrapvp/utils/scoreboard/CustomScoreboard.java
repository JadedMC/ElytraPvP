package net.jadedmc.elytrapvp.utils.scoreboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CustomScoreboard {
    private static HashMap<UUID, CustomScoreboard> players = new HashMap<>();

    public CustomScoreboard() {

    }

    public static HashMap<UUID, CustomScoreboard> getPlayers() {
        return players;
    }

    public void addPlayer(Player p) {
        getPlayers().put(p.getUniqueId(), this);
    }

    public void update(Player p) {}
}