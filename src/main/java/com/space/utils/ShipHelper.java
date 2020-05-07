package com.space.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class ShipHelper {

    final private static int CURRENT_YEAR = 3019;

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int yearFromDate(Long dateLong) {
        Calendar calendar =  Calendar.getInstance(/*TimeZone.getTimeZone("UTC")*/);
        calendar.setTimeInMillis(dateLong);
        return calendar.get(Calendar.YEAR);
    }

    public static Double getRating(Long prodDate, boolean used, Double speed) {
        int year = yearFromDate(prodDate);
        Double k = used ? 0.5 : 1;
        return (80 * round(speed, 2) * k) / (CURRENT_YEAR - year +1);
    }
}
