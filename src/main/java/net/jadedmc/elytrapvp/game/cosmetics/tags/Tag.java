package net.jadedmc.elytrapvp.game.cosmetics.tags;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class Tag extends Cosmetic {

    public Tag(ElytraPvP plugin, String id, ConfigurationSection config) {
        super(plugin, id, config);
        setType("Tag");
        setDescription("Shows a fancy prefix next to your name in chat.");
        setIconMaterial(Material.NAME_TAG);
    }
}