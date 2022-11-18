package net.jadedmc.elytrapvp.game.cosmetics.killmessages;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents the message shown when a player kills another player.
 */
public class KillMessage extends Cosmetic {

    /**
     * Creates the kill message.
     * @param plugin Instance of the plugin/
     * @param id Id of the kill message.
     * @param config Configuration for the kill message.
     **/
    public KillMessage(ElytraPvP plugin, String id, ConfigurationSection config) {
        super(plugin, id, config);
        setType("Kill Message");
        setIconMaterial(Material.OAK_SIGN);
    }
}
