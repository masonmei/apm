package com.baidu.oped.apm.common.utils;

import java.text.DecimalFormat;

/**
 * Created by mason on 8/13/15.
 */
public abstract class NumberUtils {
    public static Double format(final Double original, final int precision) {
        if(original == null){
            return null;
        }

        StringBuilder formatBuilder = new StringBuilder("#.");
        int counter = 0;
        while (counter < precision) {
            formatBuilder.append("0");
            counter++;
        }
        DecimalFormat doubleFormat = new DecimalFormat(formatBuilder.toString());
        return Double.valueOf(doubleFormat.format(original));
    }

    public static Double format(final Double original) {
        if(original == null){
            return null;
        }

        StringBuilder formatBuilder = new StringBuilder("#.0000");
        DecimalFormat doubleFormat = new DecimalFormat(formatBuilder.toString());
        return Double.valueOf(doubleFormat.format(original));
    }

    public static Double format(final Long original) {
        if(original == null){
            return null;
        }

        StringBuilder formatBuilder = new StringBuilder("#.0000");
        DecimalFormat doubleFormat = new DecimalFormat(formatBuilder.toString());
        return Double.valueOf(doubleFormat.format(original));
    }

    public static Double nullToZeroFormat(final Number number){
        if(number == null){
            return 0.0;
        }
        return number.doubleValue();
    }

    public static Double calculateRate(final Number number, final Number divider) {
        if(number == null){
            return 0.0;
        }

        if(divider == null || divider.doubleValue() == 0.0){
            return 0.0;
        }

        return number.doubleValue() / divider.doubleValue();
    }

}
