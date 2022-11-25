package net.jadedmc.elytrapvp.game.cosmetics.arrowtrails;

import net.jadedmc.elytrapvp.ElytraPvP;
import net.jadedmc.elytrapvp.game.cosmetics.Cosmetic;
import net.jadedmc.elytrapvp.game.cosmetics.trails.Trail;
import net.jadedmc.elytrapvp.player.CustomPlayer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a particle effect shown after shooting an arrow.
 */
public class ArrowTrail extends Cosmetic {
    private final ElytraPvP plugin;
    private int step = 1;
    private final List<ArrowTrailStep> steps = new ArrayList<>();

    /**
     * Creates the arrow trail.
     * @param plugin Instance of the plugin.
     * @param id Id of the arrow trail.
     * @param config configuration of the arrow trail.
     */
    public ArrowTrail(ElytraPvP plugin, String id, ConfigurationSection config) {
        super(plugin, id, config);
        this.plugin = plugin;

        setType("Arrow Trail");
        setDescription("Displays a group of particles behind every arrow you shoot.");

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

                    if(particleConfig.isSet("size")) {
                        arrowTrailStep.addParticle(new ArrowTrailParticle(particle, r, g, b, (float) particleConfig.getDouble("size")));
                    }
                    else {
                        arrowTrailStep.addParticle(new ArrowTrailParticle(particle, r, g, b));
                    }
                }
                else {
                    arrowTrailStep.addParticle(new ArrowTrailParticle(particle));
                }
            }
        }
    }

    /**
     * Get the current step the arrow trail is on.
     * @return Arrow trail's current step.
     */
    public ArrowTrailStep getCurrentStep() {
        return steps.get(step - 1);
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
        private final float size;

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
            this.size = 1;
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
            this.size = 1;
        }

        /**
         * Creates a colored particle with a different size.
         * @param particle Particle
         * @param r Red Color
         * @param g Green Color
         * @param b Blue Color
         * @param size Size
         */
        public ArrowTrailParticle(Particle particle, int r, int g, int b, float size) {
            this.particle = particle;
            this.r = r;
            this.g = g;
            this.b = b;
            colored = true;
            this.size = size;
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

                    location.getWorld().spawnParticle(particle, location, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(r, g, b), size));
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