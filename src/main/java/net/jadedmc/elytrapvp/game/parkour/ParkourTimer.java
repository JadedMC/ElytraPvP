package net.jadedmc.elytrapvp.game.parkour;

import net.jadedmc.elytrapvp.ElytraPvP;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Creates a timer that counts up.
 * Used for the parkour.
 */
public class ParkourTimer {
    private final ElytraPvP plugin;
    private final BukkitRunnable task;
    private long startTime;
    private long endTime;

    /**
     * Creates a parkour timer starting at 0.
     * @param plugin Instance of the plugin.
     */
    public ParkourTimer(ElytraPvP plugin) {
        this.plugin = plugin;
        startTime = 0;
        endTime = 0;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                endTime = System.currentTimeMillis();
            }
        };
    }

    /**
     * Creates a parkour timer with a set time.
     * @param plugin Instance of the plugin.
     * @param time Starting amount of time.
     */
    public ParkourTimer(ElytraPvP plugin, long time) {
        this.plugin = plugin;

        startTime = 0;
        endTime = time;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                endTime = System.currentTimeMillis();
            }
        };
    }

    /**
     * Start the timer.
     */
    public void start() {
        startTime = System.currentTimeMillis();
        task.runTaskTimer(plugin, 0, 5);
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        task.cancel();
    }

    /**
     * Convert the timer to milliseconds.
     * @return Milliseconds the timer ran for.
     */
    public long toMilliseconds() {
        return endTime - startTime;
    }

    /**
     * Resets the timer.
     */
    public void reset() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Converts the timer into a String
     * @return String version of timer.
     */
    public String toString() {
        long time = endTime - startTime;
        long seconds = (time/1000) % 60;
        long minutes = (time/60000);
        long milliseconds = (time % 1000)/10;

        String minute = "";
        if(minutes < 10) {
            minute += "0";
        }
        minute += "" + minutes;

        String second = "";

        if(seconds < 10) {
            second += "0";
        }
        second += "" + seconds;

        String millisecond = "";
        if(milliseconds < 10) {
            millisecond += "0";
        }
        millisecond += milliseconds;

        return minute + ":" + second + "." + millisecond;
    }
}