package com.boleiros.bazart.util;

/**
 * Created by Filipe Ramos on 12/07/14.
 */

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Numeric range Filter.
 */
public class NumericRangeFilter implements InputFilter {
    /**
     * Maximum value.
     */
    private final double maximum;
    /**
     * Minimum value.
     */
    private final double minimum;

    /**
     * Creates a new filter between 0.00 and 999,999.99.
     */
    NumericRangeFilter() {
        this(0.00, 999999.99);
    }

    /**
     * Creates a new filter.
     *
     * @param pMin Minimum value.
     * @param pMax Maximum value.
     */


    public NumericRangeFilter(double pMin, double pMax) {
        maximum = pMax;
        minimum = pMin;
    }

    @Override
    public CharSequence filter(
            CharSequence pSource, int pStart,
            int pEnd, Spanned pDest, int pDstart, int pDend
    ) {
        try {
            String vValueStr = pDest.toString().concat(pSource.toString());
            double vValue = Double.parseDouble(vValueStr);
            if (vValue <= maximum && vValue >= minimum) {
                // Returning null will make the EditText to accept more values.
                return null;
            }
        } catch (NumberFormatException pEx) {
            System.out.println("Number format exception");
        }
        // Value is out of range - return empty string.
        return "";
    }
}
