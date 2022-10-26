package net.jadedmc.elytrapvp.game.kits;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.kits.kits.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class KitManager {
    private final Map<Integer, Kit> kits = new LinkedHashMap<>();

    public KitManager(ElytraPvP plugin) {
        kits.put(1, new SniperKit(plugin));
        kits.put(2, new KnightKit(plugin));
        kits.put(3, new PyroKit(plugin));
        kits.put(4, new TankKit(plugin));
        kits.put(5, new ChemistKit(plugin));
        kits.put(6, new LauncherKit(plugin));
        kits.put(7, new BomberKit(plugin));
        kits.put(8, new HealerKit(plugin));
        kits.put(9, new TeleporterKit(plugin));
        kits.put(10, new SpectralKit(plugin));
    }

    /**
     * Get a kit based of its id.
     * @param id Id of the kit.
     * @return The kit.
     */
    public Kit getKit(int id) {
        switch (id) {
            case -1 -> {
                System.out.println("Spectator");
                return null;
            }
            case 0 -> {
                return null;
            }
            default -> {
                return kits.get(id);
            }
        }
    }

    public Kit getKit(String id) {
        for(Kit kit : kits.values()) {
            if(kit.getId().equalsIgnoreCase(id)) {
                return kit;
            }
        }

        return null;
    }

    /**
     * Get a map of all kits.
     * @return All kits.
     */
    public Map<Integer, Kit> getKits() {
        return kits;
    }
}
