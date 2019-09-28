package com.nufaza.geotagpaud.util;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

/**
 * Created by Abah on 20/12/2016.
 */
public class Common {

    public static final Integer FORMAT_DATE_SHORT = 1;

    /**
     * Safely convert long to int
     * @param l
     * @return
     */
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    public static String formatRupiah(Integer value) {

        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        // DecimalFormat rupiah = new DecimalFormat("#");
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        rupiah.setDecimalFormatSymbols(formatRp);

        String output = rupiah.format(value);
        output = output.replaceAll(",00", "");

        return output;

    }

    public static String formatDate(Date tglPengambilan, Integer formatDateShort) {
        if (formatDateShort == Common.FORMAT_DATE_SHORT) {
            return android.text.format.DateFormat.format("dd/MM/yyy", tglPengambilan).toString();
        } else {
            return android.text.format.DateFormat.format("dd/MM/yyy", tglPengambilan).toString();
        }
    }
}
