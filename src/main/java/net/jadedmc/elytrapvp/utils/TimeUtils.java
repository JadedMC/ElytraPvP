package net.jadedmc.elytrapvp.utils;

public class TimeUtils {

    public static String msToTimer(long time) {
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
