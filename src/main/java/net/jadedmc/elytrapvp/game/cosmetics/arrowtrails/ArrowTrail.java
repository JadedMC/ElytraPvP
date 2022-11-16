package net.jadedmc.elytrapvp.game.cosmetics.arrowtrails;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import net.jadedmc.elytrapvp.game.cosmetics.CosmeticType;
import net.jadedmc.elytrapvp.game.seasons.Season;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import net.jadedmc.elytrapvp.utils.item.ItemBuilder;
import net.jadedmc.elytrapvp.utils.item.SkullBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a particle effect shown after shooting an arrow.
 */
public class ArrowTrail extends Cosmetic {
    private final ElytraPvP plugin;
    private int step = 1;
    private final List<ArrowTrailStep> steps = new ArrayList<>();
    private final Material iconMaterial;
    private final String texture;
    private final ArrowTrailCategory category;

    /**
     * Creates the arrow trail.
     * @param plugin Instance of the plugin.
     * @param id Id of the arrow trail.
     * @param config configuration of the arrow trail.
     */
    public ArrowTrail(ElytraPvP plugin, String id, ConfigurationSection config) {
        super(id);
        this.plugin = plugin;

        // Load the price of the trail.
        if(config.isSet("price")) {
            setPrice(config.getInt("price"));
        }
        else {
           setPrice(400);
        }

        // Load the unlock type of the trail.
        if(config.isSet("unlockType")) {
            setUnlockType(CosmeticType.valueOf(config.getString("unlockType")));
        }

        // Load the required season of the cosmetic if set.
        if(config.isSet("season")) {
            setSeason(Season.valueOf(config.getString("season")));
        }

        // Load the icon material.
        this.iconMaterial = Material.valueOf(config.getString("icon.material"));

        // Loads the player head texture if applicable.
        if(iconMaterial == Material.PLAYER_HEAD) {
            this.texture = config.getString("icon.texture");
        }
        else {
            this.texture = "";
        }

        // Loads the category
        this.category = ArrowTrailCategory.valueOf(config.getString("category"));

        // Loads the arrow trail name
        setName(config.getString("name"));

        // --- Load the trail steps ---

        // Makes sure there are steps in the arrow trail.
        ConfigurationSection stepsConfig = config.getConfigurationSection("steps");
        if(stepsConfig == null) {
            return;
        }

        // Loops through each step in the arrow trail.
        for(String stepID : stepsConfig.getKeys(false)) {
            // Creates the step object.
            ArrowTrailStep arrowTrailStep = new ArrowTrailStep();
            steps.add(arrowTrailStep);

            // Makes sure there are particles in the step.
            ConfigurationSection particlesConfig = stepsConfig.getConfigurationSection(stepID + ".particles");
            if(particlesConfig == null) {
                continue;
            }

            // Loops through each particle in the step.
            for(String particleId : particlesConfig.getKeys(false)) {
                ConfigurationSection particleConfig = particlesConfig.getConfigurationSection(particleId);

                // Makes sure the particle is not null.
                if(particleConfig == null) {
                    continue;
                }

                // Gets the bukkit particle effect.
                Particle particle = Particle.valueOf(particleConfig.getString("particle"));

                // Creates the Arrow Trail Particle
                if(particleConfig.isSet("color")) {
                    int r = particleConfig.getInt("color.r");
                    int g = particleConfig.getInt("color.g");
                    int b = particleConfig.getInt("color.b");

                    arrowTrailStep.addParticle(new ArrowTrailParticle(particle, r, g, b));
                }
                else {
                    arrowTrailStep.addParticle(new ArrowTrailParticle(particle));
                }
            }
        }
    }

    /**
     * Get the arrow trail's category.
     * @return Category of the arrow trail.
     */
    public ArrowTrailCategory getCategory() {
        return category;
    }

    /**
     * Get the current step the arrow trail is on.
     * @return Arrow trail's current step.
     */
    public ArrowTrailStep getCurrentStep() {
        return steps.get(step - 1);
    }

    /**
     * Get the cosmetic gui icon of the arrow trail.
     * @param player Player to get icon for.
     * @return Icon for the arrow trail.
     */
    @Override
    public ItemStack getIcon(Player player) {
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        // Checks if the player has unlocked the hat.
        if(customPlayer.getUnlockedArrowTrails().contains(getId()) || (getUnlockType() == CosmeticType.NORMAL && getPrice() == 0)) {

            // Checks if the icon is a player head. If so, add texture.
            if(iconMaterial == Material.PLAYER_HEAD) {
                return new SkullBuilder(texture)
                        .setDisplayName("&a" + getName())
                        .addLore("&8Arrow Trail")
                        .addLore("")
                        .addLore("&7Click to equip")
                        .build();
            }

            // If not, return normal item.
            return new ItemBuilder(iconMaterial)
                    .addLore("&8Arrow Trail")
                    .addLore("")
                    .setDisplayName("&a" + getName())
                    .addLore("&7Click to equip")
                    .build();
        }

        // Check if the item is seasonal.
        if(getUnlockType() == CosmeticType.SEASONAL && plugin.seasonManager().getCurrentSeason() != getSeason()) {
            // If not, shows the purchase icon.
            ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                    .setDisplayName("&c" + getName())
                    .addLore("&8Arrow Trail")
                    .addLore("")
                    .addLore(ChatPaginator.wordWrap("&7This item can only be purchased during the " + getSeason().getName() + " &7event.", 35), "&7");
            return builder.build();
        }

        // If not, shows the purchase icon.
        ItemBuilder builder = new ItemBuilder(Material.GRAY_DYE)
                .setDisplayName("&c" + getName())
                .addLore("&8Arrow Trail")
                .addLore("")
                .addLore("&6Price: " + getPrice())
                .addLore("&7Click to purchase");
        return builder.build();
    }

    /**
     * Move to the next step of the arrow trail.
     */
    public void nextStep() {
        this.step++;

        if(step > steps.size()) {
            this.step = 1;
        }
    }

    /**
     * Represents a group of particles to be displayed at the same time.
     */
    public class ArrowTrailStep {
        private final List<ArrowTrailParticle> particles = new ArrayList<>();

        /**
         * Adds a particle to the step.
         * @param particle Particle to add to the step.
         */
        public void addParticle(ArrowTrailParticle particle) {
            particles.add(particle);
        }

        /**
         * Spawn all the particles in the step.
         * @param location Location to spawn particles.
         */
        public void spawnParticles(Location location) {
            for(ArrowTrailParticle particle : particles) {
                particle.spawnParticle(location);
            }
        }
    }

    /**
     * Represents a singular particle in a step.
     */
    private class ArrowTrailParticle {
        private final Particle particle;
        private final int r;
        private final int g;
        private final int b;
        private final boolean colored;

        /**
         * Creates a non-colored particle.
         * @param particle Particle.
         */
        public ArrowTrailParticle(Particle particle) {
            this.particle = particle;
            this.r = 0;
            this.g = 0;
            this.b = 0;
            colored = false;
        }

        /**
         * Creates a colored particle.
         * @param particle Particle
         * @param r Red Color
         * @param g Green Color
         * @param b Blue Color
         */
        public ArrowTrailParticle(Particle particle, int r, int g, int b) {
            this.particle = particle;
            this.r = r;
            this.g = g;
            this.b = b;
            colored = true;
        }

        /**
         * Spawns the particle at a specific location.
         * @param location Location to spawn particle.
         */
        public void spawnParticle(Location location) {
            if(colored) {
                for(CustomPlayer customPlayer : plugin.customPlayerManager().getCustomPlayers()) {
                    if(!customPlayer.showParticles()) {
                        continue;
                    }

                    location.getWorld().spawnParticle(particle, location, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(r, g, b), 1));
                }
            }
            else {
                for(CustomPlayer customPlayer : plugin.customPlayerManager().getCustomPlayers()) {
                    if(!customPlayer.showParticles()) {
                        continue;
                    }

                    location.getWorld().spawnParticle(particle, location, 0, 0, 0, 0, 1);
                }
            }
        }
    }
}