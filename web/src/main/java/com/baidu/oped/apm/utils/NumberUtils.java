package com.baidu.oped.apm.utils;

import java.text.DecimalFormat;

/**
 * Created by mason on 8/13/15.
 */
public class NumberUtils {
    public static Double format(final double original, final int precision) {
        StringBuilder formatBuilder = new StringBuilder("#.");
        int counter = 0;
        while (counter < precision) {
            formatBuilder.append("0");
            counter++;
        }
        DecimalFormat doubleFormat = new DecimalFormat(formatBuilder.toString());
        return Double.valueOf(doubleFormat.format(original));
    }

    public static Double format(final double original) {
        StringBuilder formatBuilder = new StringBuilder("#.0000");
        DecimalFormat doubleFormat = new DecimalFormat(formatBuilder.toString());
        return Double.valueOf(doubleFormat.format(original));
    }
}
