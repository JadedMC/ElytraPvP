package net.jadedmc.elytrapvp;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.jadedmc.elytrapvp.game.parkour.ParkourCourse;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
class Placeholders extends PlaceholderExpansion {
    private final ElytraPvP plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public Placeholders(ElytraPvP plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "elytrapvp";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if(identifier.contains("parkour_rank_")) {
            String[] args = identifier.split("_");

            ParkourCourse course = ParkourCourse.valueOf(args[2].toUpperCase());

            return plugin.customPlayerManager().getPlayer(player).getParkourRank(course.toString());
        }

        if(identifier.contains("parkour_top_name_")) {
            String[] args = identifier.split("_");

            ParkourCourse course = ParkourCourse.valueOf(args[3].toUpperCase());
            int place = Integer.parseInt(identifier.replaceAll("\\D+","")) - 1;

            ArrayList<String> temp = new ArrayList<>(plugin.leaderboardManager().getParkourLeaderboard(course).keySet());

            if(temp.size() < place + 1) {
                return "---";
            }

            return temp.get(place);
        }

        if(identifier.contains("parkour_top_time_")) {
            String[] args = identifier.split("_");

            ParkourCourse course = ParkourCourse.valueOf(args[3].toUpperCase());
            int place = Integer.parseInt(identifier.replaceAll("\\D+","")) - 1;

            ArrayList<String> temp = new ArrayList<>(plugin.leaderboardManager().getParkourLeaderboard(course).values());

            if(temp.size() < place + 1) {
                return "---";
            }

            return temp.get(place);
        }

        return null;
    }
}