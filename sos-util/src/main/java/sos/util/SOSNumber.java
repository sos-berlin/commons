package sos.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/** @author Titus Meyer */
public class SOSNumber {

    public static boolean isValidNumber(final String text, final Locale locale) {
        try {
            if (!text.matches("^[0-9,.]+$")) {
                return false;
            }
            asNumber(text, locale);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidNumber(final String text) {
        return isValidNumber(text, Locale.getDefault());
    }

    public static boolean isValidNumber(final String text, final int digits, final int fractionDigits, final Locale locale) {
        try {
            if (!text.matches("^[0-9,.]+$")) {
                return false;
            }
            Number num = asNumber(text, locale);
            String str = stringValue(num, Locale.ENGLISH, false, 305);
            String[] spl = str.split("\\.");
            if (spl[0].length() > digits) {
                return false;
            }
            if (spl.length == 2 && spl[1].length() > fractionDigits) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidNumber(final String text, final int digits, final int fractionDigits) {
        return isValidNumber(text, digits, fractionDigits, Locale.getDefault());
    }

    public static boolean isValidNumber(final String text, final int digits, final Locale locale) {
        try {
            if (!text.matches("^[0-9,.]+$")) {
                return false;
            }
            Number num = asNumber(text, locale);
            String str = stringValue(num, Locale.ENGLISH, false, 305);
            String[] spl = str.split("\\.");
            if (spl.length == 1 && spl[0].length() > digits) {
                return false;
            }
            if (spl.length == 2 && spl[0].length() + spl[1].length() > digits) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidNumber(final String text, final int digits) {
        return isValidNumber(text, digits, Locale.getDefault());
    }

    private static String format(final NumberFormat nf, final Number number) {
        StringBuffer sb = new StringBuffer();
        sb = nf.format(number, sb, new FieldPosition(0));
        return sb.toString();
    }

    public static String stringValue(final Number number, final Locale locale, final boolean grouping, final int maxFractionDigits) {
        NumberFormat nf = NumberFormat.getInstance(locale);
        nf.setGroupingUsed(grouping);
        nf.setMaximumFractionDigits(maxFractionDigits);
        return format(nf, number);
    }

    public static String stringValue(final Number number, final boolean grouping, final int maxFractionDigits) {
        return stringValue(number, Locale.getDefault(), grouping, maxFractionDigits);
    }

    public static String stringValue(final Number number, final boolean grouping) {
        return stringValue(number, Locale.getDefault(), grouping, 309);
    }

    public static String stringValue(final Number number, final String pattern) {
        return format(new DecimalFormat(pattern), number);
    }

    public static String stringValue(final byte number, final boolean grouping) {
        return stringValue(new Byte(number), grouping);
    }

    public static String stringValue(final short number, final boolean grouping) {
        return stringValue(new Short(number), grouping);
    }

    public static String stringValue(final int number, final boolean grouping) {
        return stringValue(new Integer(number), grouping);
    }

    public static String stringValue(final long number, final boolean grouping) {
        return stringValue(new Long(number), grouping);
    }

    public static String stringValue(final float number, final boolean grouping) {
        return stringValue(new Float(number), grouping);
    }

    public static String stringValue(final float number, final boolean grouping, final int fractionDigits) {
        return stringValue(new Float(number), grouping, fractionDigits);
    }

    public static String stringValue(final double number, final boolean grouping) {
        return stringValue(new Double(number), grouping);
    }

    public static String stringValue(final double number, final boolean grouping, final int fractionDigits) {
        return stringValue(new Double(number), grouping, fractionDigits);
    }

    public static String asCurrency(final Number number, final Locale locale, final boolean grouping, final int fractionDigits) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setGroupingUsed(grouping);
        nf.setMaximumFractionDigits(fractionDigits);
        return format(nf, number);
    }

    public static String asCurrency(final Number number, final boolean grouping, final int fractionDigits) {
        return asCurrency(number, Locale.getDefault(), grouping, fractionDigits);
    }

    public static String asCurrency(final Number number, final boolean grouping) {
        return asCurrency(number, Locale.getDefault(), grouping, 2);
    }

    public static String asCurrency(final float number, final boolean grouping) {
        return asCurrency(new Float(number), grouping);
    }

    public static String asCurrency(final double number, final boolean grouping) {
        return asCurrency(new Double(number), grouping);
    }

    public static Number asNumber(final String text, final Locale locale) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance(locale);
        return nf.parse(text);
    }

    public static Number asNumber(final String text) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.parse(text);
    }

    public static byte asByte(final String text) throws ParseException {
        return asNumber(text).byteValue();
    }

    public static short asShort(final String text) throws ParseException {
        return asNumber(text).shortValue();
    }

    public static int asInteger(final String text) throws ParseException {
        return asNumber(text).intValue();
    }

    public static long asLong(final String text) throws ParseException {
        return asNumber(text).longValue();
    }

    public static float asFloat(final String text) throws ParseException {
        return asNumber(text).floatValue();
    }

    public static double asDouble(final String text) throws ParseException {
        return asNumber(text).doubleValue();
    }

}