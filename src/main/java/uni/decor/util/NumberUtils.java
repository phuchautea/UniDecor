package uni.decor.util;

import java.text.DecimalFormat;

public final class NumberUtils {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    public NumberUtils() {}

//    public static String formatNumber(double number) {
//        return DECIMAL_FORMAT.format(number);
//    }
    public static String formatNumber(double number) {
        String formattedNumber = DECIMAL_FORMAT.format(number);
        return formattedNumber.endsWith(".00") ? formattedNumber.substring(0, formattedNumber.length() - 3) : formattedNumber;
    }
    public static String formatNumber(String number) {
        double parsedNumber = Double.parseDouble(number);
        String formattedNumber = DECIMAL_FORMAT.format(parsedNumber);
        return formattedNumber.endsWith(".00") ? formattedNumber.substring(0, formattedNumber.length() - 3) : formattedNumber;
    }
}
