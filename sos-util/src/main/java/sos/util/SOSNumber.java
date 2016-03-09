package sos.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/** <p>
 * Title: SOSNumber
 * </p>
 * <p>
 * Description: Statischer Methodensatz zum Formatieren und Parsen von Zahlen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: SOS-Berlin GmbH
 * </p>
 * 
 * @author Titus Meyer
 * @version 1.0.0 */
public class SOSNumber {

    /** Pr&uuml;ft, ob ein Text eine g&uuml;ltige Zahl darstellt. Es sind nur die
     * Zeichen [0-9,.] zugelassen.
     * 
     * @param text Textdarstellung einer Zahl
     * @param locale Lokale
     * @return Ob der Text eine g&uuml;ltige Zahl darstellt */
    public static boolean isValidNumber(final String text, final Locale locale) {
        try {
            if (!text.matches("^[0-9,.]+$"))
                return false;
            asNumber(text, locale);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /** Pr&uuml;ft, ob ein Text eine g&uuml;ltige Zahl darstellt. Es sind nur die
     * Zeichen [0-9,.] zugelassen.
     * 
     * @param text Textdarstellung einer Zahl
     * @return Ob der Text eine g&uuml;ltige Zahl darstellt */
    public static boolean isValidNumber(final String text) {
        return isValidNumber(text, Locale.getDefault());
    }

    /** Pr&uuml;ft, ob ein Text eine g&uuml;ltige Zahl darstellt. Dabei wird auch
     * die Anzahl der Vor- und Nachkommastellen ber&uuml;cksichtigt. Es sind nur
     * die Zeichen [0-9,.] zugelassen.
     * 
     * @param text Textdarstellung einer Zahl
     * @param digits Max. Anzahl der Vorkommastellen
     * @param fractionDigits Max. Anzahl der Nachkommastellen
     * @param locale Lokale
     * @return Ob der Text eine g&uuml;ltige Zahl darstellt */
    public static boolean isValidNumber(final String text, final int digits, final int fractionDigits, final Locale locale) {
        try {
            if (!text.matches("^[0-9,.]+$"))
                return false;
            Number num = asNumber(text, locale);
            String str = stringValue(num, Locale.ENGLISH, false, 305);
            String[] spl = str.split("\\.");
            if (spl[0].length() > digits)
                return false;
            if (spl.length == 2 && spl[1].length() > fractionDigits)
                return false;
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /** Pr&uuml;ft, ob ein Text eine g&uuml;ltige Zahl darstellt. Dabei wird auch
     * die Anzahl der Vor- und Nachkommastellen ber&uuml;cksichtigt. Es sind nur
     * die Zeichen [0-9,.] zugelassen.
     * 
     * @param text Textdarstellung einer Zahl
     * @param digits Max. Anzahl der Vorkommastellen
     * @param fractionDigits Max. Anzahl der Nachkommastellen
     * @return Ob der Text eine g&uuml;ltige Zahl darstellt */
    public static boolean isValidNumber(final String text, final int digits, final int fractionDigits) {
        return isValidNumber(text, digits, fractionDigits, Locale.getDefault());
    }

    /** Pr&uuml;ft, ob ein Text eine g&uuml;ltige Zahl darstellt. Dabei wird auch
     * die Anzahl der Ziffern ber&uuml;cksichtigt. Es sind nur die Zeichen
     * [0-9,.] zugelassen.
     * 
     * @param text Textdarstellung einer Zahl
     * @param digits Max. Anzahl der Vor- und Nachkommastellen
     * @param locale Lokale
     * @return Ob der Text eine g&uuml;ltige Zahl darstellt */
    public static boolean isValidNumber(final String text, final int digits, final Locale locale) {
        try {
            if (!text.matches("^[0-9,.]+$"))
                return false;
            Number num = asNumber(text, locale);
            String str = stringValue(num, Locale.ENGLISH, false, 305);
            String[] spl = str.split("\\.");
            if (spl.length == 1 && spl[0].length() > digits)
                return false;
            if (spl.length == 2 && spl[0].length() + spl[1].length() > digits)
                return false;
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /** Pr&uuml;ft, ob ein Text eine g&uuml;ltige Zahl darstellt. Dabei wird auch
     * die Anzahl der Ziffern ber&uuml;cksichtigt. Es sind nur die Zeichen
     * [0-9,.] zugelassen.
     * 
     * @param text Textdarstellung einer Zahl
     * @param digits Max. Anzahl der Vor- und Nachkommastellen
     * @return Ob der Text eine g&uuml;ltige Zahl darstellt */
    public static boolean isValidNumber(final String text, final int digits) {
        return isValidNumber(text, digits, Locale.getDefault());
    }

    /** Formatiert eine Zahl mit dem angegebenen Formatierungsobjekt in einen
     * Text.
     * 
     * @param nf Formatierung
     * @param number Zahl
     * @return formatierte Darstellung der Zahl */
    private static String format(final NumberFormat nf, final Number number) {
        StringBuffer sb = new StringBuffer();
        sb = nf.format(number, sb, new FieldPosition(0));
        return sb.toString();
    }

    /** Liefert den formatierten Text eine Zahl.
     * 
     * @param number zu formatierende Zahl
     * @param locale zu verwendende Lokalisierung
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @param maxFractionDigits Runde auf x Nachkommastellen
     * @return formatierte Darstellung der Zahl */
    public static String stringValue(final Number number, final Locale locale, final boolean grouping, final int maxFractionDigits) {
        NumberFormat nf = NumberFormat.getInstance(locale);
        nf.setGroupingUsed(grouping);
        nf.setMaximumFractionDigits(maxFractionDigits);
        return format(nf, number);
    }

    /** Liefert den formatierten Text eine Zahl. Es wird die Default-Locale
     * verwendet.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @param maxFractionDigits Runde auf x Nachkommastellen
     * @return formatierte Darstellung der Zahl */
    public static String stringValue(final Number number, final boolean grouping, final int maxFractionDigits) {
        return stringValue(number, Locale.getDefault(), grouping, maxFractionDigits);
    }

    /** Liefert den formatierten Text eine Zahl. Es wird die Default-Locale und
     * eine Tausendertrennung verwendet.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte Darstellung der Zahl */
    public static String stringValue(final Number number, final boolean grouping) {
        return stringValue(number, Locale.getDefault(), grouping, 309);
    }

    /** Liefert den formatierten Text einer Zahl. Zur Formatierung wird die
     * Default-Locale verwendet.
     * 
     * @see java.text.DecimalFormat
     * @param number zu formatierende Zahl
     * @param pattern Formatierungsvorlage
     * @return formatierte Darstellung der Zahl */
    public static String stringValue(final Number number, final String pattern) {
        return format(new DecimalFormat(pattern), number);
    }

    /** Liefert den formatierten Text einer byte Zahl anhand der Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte Darstellung der byte Zahl */
    public static String stringValue(final byte number, final boolean grouping) {
        return stringValue(new Byte(number), grouping);
    }

    /** Liefert den formatierten Text einer short Zahl anhand der Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte Darstellung der short Zahl */
    public static String stringValue(final short number, final boolean grouping) {
        return stringValue(new Short(number), grouping);
    }

    /** Liefert den formatierten Text einer integer Zahl anhand der
     * Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte Darstellung der integer Zahl */
    public static String stringValue(final int number, final boolean grouping) {
        return stringValue(new Integer(number), grouping);
    }

    /** Liefert den formatierten Text einer long Zahl anhand der Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte Darstellung der long Zahl */
    public static String stringValue(final long number, final boolean grouping) {
        return stringValue(new Long(number), grouping);
    }

    /** Liefert den formatierten Text einer float Zahl anhand der Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte Darstellung der float Zahl */
    public static String stringValue(final float number, final boolean grouping) {
        return stringValue(new Float(number), grouping);
    }

    /** Liefert den formatierten Text einer float Zahl anhand der Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @param fractionDigits Runde auf x Nachkommastellen
     * @return formatierte Darstellung der float Zahl */
    public static String stringValue(final float number, final boolean grouping, final int fractionDigits) {
        return stringValue(new Float(number), grouping, fractionDigits);
    }

    /** Liefert den formatierten Text einer double Zahl anhand der
     * Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte Darstellung der double Zahl */
    public static String stringValue(final double number, final boolean grouping) {
        return stringValue(new Double(number), grouping);
    }

    /** Liefert den formatierten Text einer double Zahl anhand der
     * Default-Locale.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @param fractionDigits Runde auf x Nachkommastellen
     * @return formatierte Darstellung der double Zahl */
    public static String stringValue(final double number, final boolean grouping, final int fractionDigits) {
        return stringValue(new Double(number), grouping, fractionDigits);
    }

    /** Formatiert eine Zahl anhand der angegebenen Lokalisierung in eine
     * W&auml;hrungsdarstellung. Es wird das W&auml;hrungszeichen der
     * L&auml;ndereinstellung verwendet.
     * 
     * @param number zu formatierende Zahl
     * @param locale zu verwendende Lokalisierung
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @param fractionDigits Runde auf x Nachkommastellen
     * @return formatierte W&auml;hrungsdarstellung der Zahl */
    public static String asCurrency(final Number number, final Locale locale, final boolean grouping, final int fractionDigits) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setGroupingUsed(grouping);
        nf.setMaximumFractionDigits(fractionDigits);
        return format(nf, number);
    }

    /** Formatiert eine Zahl anhand der angegebenen Lokalisierung in eine
     * W&auml;hrungsdarstellung. Es wird die Default-Locale verwendet.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @param fractionDigits Runde auf x Nachkommastellen
     * @return formatierte W&auml;hrungsdarstellung der Zahl */
    public static String asCurrency(final Number number, final boolean grouping, final int fractionDigits) {
        return asCurrency(number, Locale.getDefault(), grouping, fractionDigits);
    }

    /** Formatiert eine Zahl anhand der angegebenen Lokalisierung in eine
     * W&auml;hrungsdarstellung. Es wird die Default-Locale verwendet und auf
     * zwei Nachkommastellen gerundet.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte W&auml;hrungsdarstellung der Zahl */
    public static String asCurrency(final Number number, final boolean grouping) {
        return asCurrency(number, Locale.getDefault(), grouping, 2);
    }

    /** Formatiert eine float anhand der angegebenen Lokalisierung in eine
     * W&auml;hrungsdarstellung. Es wird die Default-Locale verwendet und auf
     * zwei Nachkommastellen gerundet.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte W&auml;hrungsdarstellung der Zahl */
    public static String asCurrency(final float number, final boolean grouping) {
        return asCurrency(new Float(number), grouping);
    }

    /** Formatiert eine double anhand der angegebenen Lokalisierung in eine
     * W&auml;hrungsdarstellung. Es wird die Default-Locale verwendet und auf
     * zwei Nachkommastellen gerundet.
     * 
     * @param number zu formatierende Zahl
     * @param grouping Verwendung eines Tausendertrennzeichens
     * @return formatierte W&auml;hrungsdarstellung der Zahl */
    public static String asCurrency(final double number, final boolean grouping) {
        return asCurrency(new Double(number), grouping);
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Lokalisierung in eine
     * Zahl zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @param locale zu verwendende Lokalisierung
     * @return die Textdarstellung als Zahl
     * @throws ParseException */
    public static Number asNumber(final String text, final Locale locale) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance(locale);
        return nf.parse(text);
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Default-Locale in
     * eine Zahl zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @return die Textdarstellung als Zahl
     * @throws ParseException */
    public static Number asNumber(final String text) throws ParseException {
        NumberFormat nf = NumberFormat.getInstance();
        return nf.parse(text);
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Default-Locale in ein
     * byte zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @return die Textdarstellung als byte
     * @throws ParseException */
    public static byte asByte(final String text) throws ParseException {
        return asNumber(text).byteValue();
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Default-Locale in ein
     * short zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @return die Textdarstellung als short
     * @throws ParseException */
    public static short asShort(final String text) throws ParseException {
        return asNumber(text).shortValue();
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Default-Locale in ein
     * integer zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @return die Textdarstellung als integer
     * @throws ParseException */
    public static int asInteger(final String text) throws ParseException {
        return asNumber(text).intValue();
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Default-Locale in ein
     * long zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @return die Textdarstellung als long
     * @throws ParseException */
    public static long asLong(final String text) throws ParseException {
        return asNumber(text).longValue();
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Default-Locale in ein
     * float zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @return die Textdarstellung als float
     * @throws ParseException */
    public static float asFloat(final String text) throws ParseException {
        return asNumber(text).floatValue();
    }

    /** Versucht eine Textdarstellung einer Zahl anhand der Default-Locale in ein
     * double zu konvertieren.
     * 
     * @param text Textdarstellung der Zahl
     * @return die Textdarstellung als double
     * @throws ParseException */
    public static double asDouble(final String text) throws ParseException {
        return asNumber(text).doubleValue();
    }
}