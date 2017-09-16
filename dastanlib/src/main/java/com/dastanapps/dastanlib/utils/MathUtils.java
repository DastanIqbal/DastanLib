package com.dastanapps.dastanlib.utils;

import java.math.BigDecimal;

/**
 * Created by dastaniqbal on 09/05/2017.
 * dastanIqbal@marvelmedia.com
 * 09/05/2017 11:54
 */

public class MathUtils {
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static float truncateDigitsFromLast(float value, float truncateDigit) {
        if (truncateDigit == 0) return 0f;
        if (String.valueOf(value).length() >= truncateDigit) {
            float digit = 10f;
            for (int i = 1; i < truncateDigit; i++) digit *= 10f;
            return (value - value % digit) / digit;
        }
        return 0f;
    }
}
