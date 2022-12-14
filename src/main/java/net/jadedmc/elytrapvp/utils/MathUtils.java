package net.jadedmc.elytrapvp.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A group of math-related utilities.
 */
public class MathUtils {

    /**
     * Rounds a double to a set number of decimal places.
     * @param value The double to be rounded.
     * @param places Number of decimal places.
     * @return Rounded double.
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Calculate the percentage obtained compared to a total.
     * @param obtained Obtained amount.
     * @param total Total amount.
     * @return Percentage of total.
     */
    public static int calculatePercentage(int obtained, int total) {
        return obtained * 100 / total;
    }
}
