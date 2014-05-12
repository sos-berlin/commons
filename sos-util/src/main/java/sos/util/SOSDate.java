package sos.util;

import java.io.File;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Title: SOSDate
 * 
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun </a>
 * @author Andreas P?schel
 * @author Titus Meyer
 * @version $Id$
 */

public class SOSDate {

    /** Standard Datumsformat (ISO) */
    public static String dateFormat = new String("yyyy-MM-dd");

    /** Standard Zeitformat (ISO) */
    public static String dateTimeFormat = new String("yyyy-MM-dd HH:mm:ss");

    /** Standard Ausgabe Zeitformat */
    private static String outputDateTimeFormat = new String("MM/dd/yy HH:mm:ss");

    /** Constant of java.text.DateFormat for short style pattern */
    public static final int SHORT = DateFormat.SHORT;

    /** Constant of java.text.DateFormat for medium style pattern */
    public static final int MEDIUM = DateFormat.MEDIUM;

    /** Constant of java.text.DateFormat for long style pattern */
    public static final int LONG = DateFormat.LONG;

    /** Constant of java.text.DateFormat for full style pattern */
    public static final int FULL = DateFormat.FULL;

    /** Datumsformat (default Jahr 2.stellig) */
    public static int dateStyle = DateFormat.SHORT;

    /** Zeitformat (default ohne Millisekunden) */
    public static int timeStyle = DateFormat.SHORT;

    /** Intenationalisierung */
    public static Locale locale = Locale.UK;

    /** Beim Fehlerhafte Datums Exception ausl?sen, wenn lenient = false ist. */
    private static boolean lenient = false;

    /**
     * setzt das Datumsformat f?r alle Methoden dieser Klasse (default: ISO-Format
     * yyyy-mm-dd.
     * 
     * @param dateFormat Datumsformat abweichend von ISO
     */
    public void setDateFormat(String dateFormat) {

        SOSDate.dateFormat = dateFormat;
    }

    /**
     * liefert das Datumsformat (default: ISO-Format yyyy-mm-dd).
     */
    public static String getDateFormat() {

        return SOSDate.dateFormat;
    }

    /**
     * setzt das Zeitformat f?r alle Methoden dieser Klasse (default: ISO-Format
     * yyyy-mm-dd HH:mm:ss).
     * 
     * @param dateTimeFormat Zeitformat abweichend von ISO
     */
    public static void setDateTimeFormat(String dateTimeFormat) {

        SOSDate.dateTimeFormat = dateTimeFormat;
    }

    /**
     * liefert das Zeitformat (default: ISO-Format yyyy-mm-dd hh:mm:ss).
     */
    public static String getDateTimeFormat() {

        return SOSDate.dateTimeFormat;
    }

    /**
     * liefert das aktuelle Datum als Date zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getCurrentDate() throws Exception {

        return SOSDate.getDate();
    }

    /**
     * liefert das aktuelle Datum als String im ISO-Format zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getCurrentDateAsString() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    /**
     * liefert das aktuelle Datum als String im angegebenen Format zur?ck.
     * 
     * @param dateFormat Datumsformat abweichend von ISO
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getCurrentDateAsString(String dateFormat)
            throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    /**
     * liefert die aktuelle Uhrzeit als Date zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getCurrentTime() throws Exception {

        return SOSDate.getTime();
    }

    /**
     * liefert die aktuelle Uhrzeit als String im default-Format (ISO) zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getCurrentTimeAsString() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(
                SOSDate.dateTimeFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    /**
     * liefert die aktuelle Uhrzeit als String im angegebenen Format zur?ck.
     * 
     * @param dateTimeFormat Format-String, z.B. yyyy-mm-dd hh:mm:ss
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getCurrentTimeAsString(String dateTimeFormat)
            throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    /**
     * liefert das aktuelle Datum als Date zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getDate() throws Exception {

        SimpleDateFormat formatter;

        try {
            formatter = new SimpleDateFormat(SOSDate.dateFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw (new Exception("invalid date format string: " + e.toString()));
        }

        try {
            Calendar now = Calendar.getInstance();
            return now.getTime();
            // return formatter.parse(now.getTime().toString());
        } catch (Exception e) {
            throw (new Exception("illegal date value: " + e.toString()));
        }
    }

    /**
     * liefert das aktuelle Datum als String im default-Format (ISO) zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getDateAsString() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateFormat);
        formatter.setLenient(lenient);
        return formatter.format(SOSDate.getDate());
    }

    /**
     * liefert das im ISO-Format angegebene Datum als Date zur?ck.
     * 
     * @param dateStr Datum im ISO-Format yyyy-mm-dd
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getDate(String dateStr) throws Exception {

        SimpleDateFormat formatter;

        try {
            formatter = new SimpleDateFormat(SOSDate.dateFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw (new Exception("invalid date format string: " + e.toString()));
        }

        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            throw (new Exception("illegal date value: " + e.toString()));
        }
    }

    /**
     * liefert das angegebene Datum als String im ISO-Format zur?ck.
     * 
     * @param date Datum vom Typ Date
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getDateAsString(Date date) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    /**
     * liefert das im angegebenen Format angegebene Datum als Date zur?ck.
     * 
     * @param dateStr Datum im ISO-Format yyyy-mm-dd bzw. dem angegebenen Format
     * @param dateFormat Format-String, z.B. yyyy-mm-dd
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getDate(String dateStr, String dateFormat)
            throws Exception {

        SimpleDateFormat formatter;

        try {

            formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(lenient);

        } catch (Exception e) {
            throw (new Exception("invalid date format string: " + e.toString()));
        }

        try {

            return formatter.parse(dateStr);
        } catch (Exception e) {
            throw (new Exception("illegal date string: " + e.toString()));
        }
    }

    /**
     * liefert das angegebene Datum vom Typ Date als String im angegebenen Format
     * zur?ck.
     * 
     * @param date Datum vom Typ Date
     * @param dateFormat Format-String, z.B. yyyy-mm-dd
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getDateAsString(Date date, String dateFormat)
            throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    /**
     * liefert die aktuelle Zeit als Date zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getTime() throws Exception {

        try {
            Calendar now = Calendar.getInstance();
            return now.getTime();
        } catch (Exception e) {
            throw (new Exception("illegal date value: " + e.toString()));
        }

    }

    /**
     * liefert die aktuelle Zeit als String im default-Format zur?ck.
     * 
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getTimeAsString() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(
                SOSDate.dateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(SOSDate.getTime());
    }

    /**
     * liefert die im ISO-Format angegebene Zeit als Date zur?ck.
     * 
     * @param dateTimeStr Zeit im ISO-Format yyyy-mm-dd hh:mm:ss
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getTime(String dateTimeStr) throws Exception {

        SimpleDateFormat formatter;

        try {
            formatter = new SimpleDateFormat(SOSDate.dateTimeFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw (new Exception("invalid date format string: " + e.toString()));
        }

        try {
            //Calendar now = Calendar.getInstance();
            return formatter.parse(dateTimeStr);
        } catch (Exception e) {
            throw (new Exception("illegal date value: " + e.toString()));
        }

    }

    /**
     * liefert die angegebene Zeit als String im default-Format zur?ck.
     * 
     * @param date Zeit vom Typ Date
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getTimeAsString(Date date) throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(
                SOSDate.dateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    /**
     * liefert die angegebene Zeit im angegebenen Format als Date zur?ck.
     * 
     * @param dateTimeStr Zeit im ISO-Format yyyy-mm-dd hh:mm:ss bzw. dem
     *         angegebenen Format
     * @param dateTimeFormat Format-String, z.B. yyyy-mm-dd hh:mm:ss
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static Date getTime(String dateTimeStr, String dateTimeFormat)
            throws Exception {

        SimpleDateFormat formatter;

        try {
            formatter = new SimpleDateFormat(dateTimeFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw (new Exception("invalid date format string: " + e.toString()));
        }

        try {
            return formatter.parse(dateTimeStr);
        } catch (Exception e) {
            throw (new Exception("illegal date value: " + e.toString()));
        }

    }

    /**
     * liefert die angegebene Zeit als String im angegebenen Format zur?ck.
     * 
     * @param date Zeit vom Typ Date
     * @param dateTimeFormat Format-String, z.B. yyyy-mm-dd hh:mm:ss
     * @exception Exception wird ausgel?st, falls ein Fehler vorliegt.
     */
    public static String getTimeAsString(Date date, String dateTimeFormat)
            throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    /**
     * Validiert ein Datum anhand der standard Formate
     * 
     * @param text Datum
     * @param dateStyle Format
     * @param locale Lokale
     * @return G&uuml;ltigkeit
     */
    public static boolean isValidDate(String text, int dateStyle, Locale locale) {
        try {
            getDate(text, dateStyle, locale);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validiert eine Uhrzeit anhand der standard Formate
     * 
     * @param text Datum
     * @param locale Lokale
     * @return G&uuml;ltigkeit
     */
    public static boolean isValidTime(String text, int timeStyle, Locale locale) {
        try {
            getTime(text, timeStyle, locale);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validiert eine Datum-Uhrzeit-Angabe anhand der standard Formate
     * 
     * @param text Datum
     * @param dateStyle Format
     * @param locale Lokale
     * @return G&uuml;ltigkeit
     */
    public static boolean isValidDateTime(String text, int dateStyle,
            int timeStyle, Locale locale) {
        try {
            getDateTime(text, dateStyle, timeStyle, locale);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Liefert eine lokale Datumdarstellung als Text anhand der standard Formate
     * 
     * @param date Datum vom Typ Date
     * @param dateStyle Format
     * @param locale Lokale
     * @return Datum als Text
     */
    public static String getDateAsString(Date date, int dateStyle, Locale locale) {
        DateFormat formatter = DateFormat.getDateInstance(dateStyle, locale);
        return formatter.format(date);
    }

    /**
     * Liefert eine lokale Zeitdarstellung als Text anhand der standard Formate
     * 
     * @param date Datum vom Typ Date
     * @param dateStyle Format
     * @param locale Lokale
     * @return Zeit als Text
     */
    public static String getTimeAsString(Date date, int timeStyle, Locale locale) {
        DateFormat formatter = DateFormat.getTimeInstance(timeStyle, locale);
        return formatter.format(date);
    }

    /**
     * Liefert eine lokale Datum-Zeit-Darstellung als Text anhand der standard
     * Formate
     * 
     * @param date Datum vom Typ Date
     * @param dateStyle Format
     * @param locale Lokale
     * @return Datum-Zeit als Text
     */
    public static String getDateTimeAsString(Date date, int dateStyle,
            int timeStyle, Locale locale) {

        DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle,
                timeStyle, locale);

        return formatter.format(date);
    }

    /**
     * Liefert eine lokale Datum-Zeit-Darstellung als Text anhand des SOSDate.outputDateTimeFormat
     * 
     * @param datestr       Datum als String im SOSDate.dateTimeFormat format oder %now f?r aktuelles Datum
     * @return
     * @throws Exception
     */
    public static String getDateTimeAsString(String datestr) throws Exception {

        return getDateTimeAsString(datestr, null);
    }

    /**
     * Liefert eine lokale Datum-Zeit-Darstellung als Text
     * 
     * @param datestr                   Datum als String im SOSDate.dateTimeFormat format oder %now f?r aktuelles Datum
     * @param outputDateTimeFormat  Ausgabe Format<br>
     *                              zB: dd.MM.yyyy HH:mm:ss,dd.MM.yy HH:mm ...
     *                                  MM/dd/yy HH:mm ...
     *                              Die Reihenfolge und Delimiter bei den formatierungs Elementen sind belibig,<br>
     *                              es m?ssen nur g?ltige Java Datum/Zeit Format-Elemente sein
     * @return
     * @throws Exception
     */
    public static String getDateTimeAsString(String datestr,
            String outputDateTimeFormat) throws Exception {

        Date date = null;

        if (datestr.equals("%now")) {
            date = new Date();
        } else {
            date = SOSDate.getTime(datestr);
        }

        if (outputDateTimeFormat == null || outputDateTimeFormat.length() == 0) {
            outputDateTimeFormat = SOSDate.getOutputDateTimeFormat();
        }

        DateFormat formatter = new SimpleDateFormat(outputDateTimeFormat);
        formatter.setLenient(lenient);

        return formatter.format(date);
    }

    /**
     * Liefert eine lokale Datum-Zeit-Darstellung als Text
     * 
     * @param datestr                   Datum als String im SOSDate.dateTimeFormat format oder %now f?r aktuelles Datum
     * @param outputDateTimeFormat  Ausgabe Format<br>
     *                              zB: dd.MM.yyyy HH:mm:ss,dd.MM.yy HH:mm ...
     *                                  MM/dd/yy HH:mm ...
     *                              Die Reihenfolge und Delimiter bei den formatierungs Elementen sind belibig,<br>
     *                              es m?ssen nur g?ltige Java Datum/Zeit Format-Elemente sein
     * @return
     * @throws Exception
     */
    public static String getDateTimeAsString(Date date,
            String outputDateTimeFormat) throws Exception {

        if (outputDateTimeFormat == null || outputDateTimeFormat.length() == 0) {
            outputDateTimeFormat = SOSDate.getOutputDateTimeFormat();
        }

        DateFormat formatter = new SimpleDateFormat(outputDateTimeFormat);
        formatter.setLenient(lenient);

        return formatter.format(date);
    }

    /**
     * Erstellt aus einer lokalen Datumdarstellung ein Date-Objekt anhand von
     * standard Formaten
     * 
     * @param text textuelle Darstellung des Datums
     * @param dateStyle Format
     * @param locale Lokale
     * @return Datum vom Typ Date
     * @throws ParseException
     */
    public static Date getDate(String text, int dateStyle, Locale locale)
            throws ParseException {
        DateFormat formatter = DateFormat.getDateInstance(dateStyle, locale);
        return formatter.parse(text);
    }

    /**
     * Erstellt aus einer lokalen Zeitdarstellung ein Date-Objekt anhand von
     * standard Formaten
     * 
     * @param text textuelle Darstellung der Zeit
     * @param dateStyle Format
     * @param locale Lokale
     * @return Datum vom Typ Date
     * @throws ParseException
     */
    public static Date getTime(String text, int timeStyle, Locale locale)
            throws ParseException {
        DateFormat formatter = DateFormat.getTimeInstance(timeStyle, locale);
        return formatter.parse(text);
    }

    /**
     * Erstellt aus einer lokalen Datum-Zeit-Darstellung ein Date-Objekt anhand
     * von standard Formaten
     * 
     * @param text textuelle Darstellung von Datum-Zeit
     * @param dateStyle Format
     * @param locale Lokale
     * @return Datum vom Typ Date
     * @throws ParseException
     */
    public static Date getDateTime(String text, int dateStyle, int timeStyle,
            Locale locale) throws ParseException {
        DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle,
                timeStyle, locale);
        return formatter.parse(text);
    }

    /**
     * Liefert den lokalen Pattern zum Datum von einem standard Format
     * 
     * @param dateStyle Format
     * @param locale Lokale
     * @return lokaler Pattern
     */
    public static String getDatePattern(int dateStyle, Locale locale) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat
                .getDateInstance(dateStyle, locale);
        formatter.setLenient(lenient);
        return formatter.toLocalizedPattern();
    }

    /**
     * Liefert den lokalen Pattern zur Zeit von einem standard Format
     * 
     * @param dateStyle Format
     * @param locale Lokale
     * @return lokaler Pattern
     */
    public static String getTimePattern(int timeStyle, Locale locale) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat
                .getTimeInstance(timeStyle, locale);
        formatter.setLenient(lenient);
        return formatter.toLocalizedPattern();
    }

    /**
     * Liefert den lokalen Pattern zur Datum-Zeit von einem standard Format
     * 
     * @param dateStyle Format
     * @param locale Lokale
     * @return lokaler Pattern
     */
    public static String getDateTimePattern(int dateStyle, int timeStyle,
            Locale locale) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat
                .getDateTimeInstance(dateStyle, timeStyle, locale);
        formatter.setLenient(lenient);
        return formatter.toLocalizedPattern();
    }

    /**
     * Liefert default Ausgabe Format
     * 
     * @return Returns the outputDateTimeFormat.
     */
    public static String getOutputDateTimeFormat() {
        return outputDateTimeFormat;
    }

    /**
     * Setzt default Ausgabe Format
     * 
     * @param outputDateTimeFormat Ausgabe Format
     */
    public static void setOutputDateTimeFormat(String outputDateTimeFormat) {
        SOSDate.outputDateTimeFormat = outputDateTimeFormat;
    }

    /**
     * Methode erwarten einen String im SOSDate.dateFormat Format (default yyyy-MM-dd)<br>
     * und liefert sprachabh?ngig formatiertes Datum (aktuelle Locale : SOSDate.locale) als String zur?ck<br>
     * <br>
     * Ausgabeformat wird ?ber die Eigenschaft SOSDate.dateStyle bestimmt (default Jahr - 2.stellig) 
     * <br>
     * 
     * @param datestr Datum als String im SOSDate.dateFormat Format
     * @return      sprachabh?ngiges Datum als String
     * @throws Exception
     */
    public static String getLocaleDateAsString(String datestr) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat(SOSDate.dateFormat);
        sdf.setLenient(lenient);
        Date date = sdf.parse(datestr);
        DateFormat formatter = DateFormat.getDateInstance(SOSDate.dateStyle,
                SOSDate.locale);

        return formatter.format(date);
    }

    /**
     * Methode erwarten einen String im SOSDate.dateTimeFormat Format (default yyyy-MM-dd HH:mm:ss)<br>
     * und liefert sprachabh?ngig formatiertes Datum und Zeit (aktuelle Locale : SOSDate.locale) als String zur?ck<br>
     * <br>
     * Ausgabeformat wird ?ber die Eigenschaften SOSDate.dateStyle und SOSDate.timeStyle bestimmt<br>
     * (default Jahr - 2.stellig und die Zeit ohne Millisekunden)<br>
     * 
     * @param datestr Datum und Zeit als String im SOSDate.dateTimeFormat Format
     * @return      sprachabh?ngiges Datum und Zeit als String
     * @throws Exception
     */
    public static String getLocaleDateTimeAsString(String datestr)
            throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat(SOSDate.dateTimeFormat);
        sdf.setLenient(lenient);
        Date date = sdf.parse(datestr);
        DateFormat formatter = DateFormat.getDateTimeInstance(
                SOSDate.dateStyle, SOSDate.timeStyle, SOSDate.locale);

        return formatter.format(date);
    }

    /**
     * Methode erwarten ein Date Objekt<br>
     * und liefert sprachabh?ngig formatiertes Datum (aktuelle Locale : SOSDate.locale) als String zur?ck<br>
     * <br>
     * Ausgabeformat wird ?ber die Eigenschaft SOSDate.dateStyle bestimmt (default Jahr - 2.stellig) 
     * <br>
     * 
     * @param datestr Datum als String im SOSDate.dateFormat Format
     * @return      sprachabh?ngiges Datum als String
     * @throws Exception
     */
    public static String getLocaleDateAsString(Date date) throws Exception {

        DateFormat formatter = DateFormat.getDateInstance(SOSDate.dateStyle,
                SOSDate.locale);
        return formatter.format(date);
    }

    /**
     * Methode erwarten ein Date Objekt<br>
     * und liefert sprachabh?ngig formatiertes Datum und Zeit (aktuelle Locale : SOSDate.locale) als String zur?ck<br>
     * <br>
     * Ausgabeformat wird ?ber die Eigenschaften SOSDate.dateStyle und SOSDate.timeStyle bestimmt<br>
     * (default Jahr - 2.stellig und die Zeit ohne Millisekunden)<br>
     * 
     * @param datestr Datum und Zeit als String im SOSDate.dateTimeFormat Format
     * @return      sprachabh?ngiges Datum und Zeit als String
     * @throws Exception
     */
    public static String getLocaleDateTimeAsString(Date date) throws Exception {

        DateFormat formatter = DateFormat.getDateTimeInstance(
                SOSDate.dateStyle, SOSDate.timeStyle, SOSDate.locale);

        return formatter.format(date);
    }

    /**
     * Formatiert ein GregorianCalendar Objekt in ISO-Format und liefert als String zur?ck
     * 
     * @param date
     * @return
     * @throws Exception
     */
    public static String getISODateTimeAsString(GregorianCalendar date)
            throws Exception {
        SimpleDateFormat iso_format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        iso_format.setLenient(lenient);
        return iso_format.format(date.getTime());
    }

    /**
     * Formatiert ein GregorianCalendar Objekt in ISO-Format und liefert als String zur?ck
     * 
     * @param date
     * @return
     * @throws Exception
     */
    public static String getISODateAsString(GregorianCalendar date)
            throws Exception {
        SimpleDateFormat iso_format = new SimpleDateFormat("yyyy-MM-dd");
        iso_format.setLenient(lenient);
        return iso_format.format(date.getTime());
    }

    public static boolean isLenient() {
        return lenient;
    }

    public static void setLenient(boolean lenient) {
        SOSDate.lenient = lenient;
    }

    /**
     * increment the specified day
     * 
     * @param date is the date to be incremented
     * @return the incremented date
     */
    public static Date incrementDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    /**
     * 
     * @param date to be checked 
     * @return true if the specified date is a weekend otherwise false
     */
    public static boolean isWeekEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated. 
     * @return Date represents the calculated next working day of the week
     */
    public static Date getNextWorkingDay(Date date) {
        Date day = date;
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return day;
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated. 
     * @return String represents the calculated next working day of the week
     */
    public static String getNextWorkingDayAsString(Date date) throws Exception {
        Date day = date;
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return SOSDate.getDateAsString(day);
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param dateStr is the date as string from which the next working day will be calculated. 
     * @return Date object represents the calculated next working day of the week
     */
    public static Date getNextWorkingDay(String dateStr) throws Exception {
        Date day = SOSDate.getDate(dateStr);
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return day;
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param dateStr is the date as string from which the next working day will be calculated. 
     * @return String represents the calculated next working day of the week
     */

    public static String getNextWorkingDayAsString(String dateStr)
            throws Exception {
        Date day = SOSDate.getDate(dateStr);
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return SOSDate.getDateAsString(day);
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated.
     * @param xmlFile is the name of the file which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static String getNextWorkingDayAsString(String dateStr, File xmlFile)
            throws Exception {

        Document doc = null;

        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();

        //parse using builder to get DOM representation of the XML file
        doc = db.parse(xmlFile);

        return getNextWorkingDayAsString(dateStr, doc);

    }// 

    
    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated.
     * @param xmlFile is the name of the file which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static Date getNextWorkingDay(Date date, File xmlFile)
            throws Exception {

        Document doc = null;

        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();

        //parse using builder to get DOM representation of the XML file
        doc = db.parse(xmlFile);

        return getNextWorkingDay(date, doc);

    }// 

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated.
     * @param holidays is the xml object which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static Date getNextWorkingDay(Date date, Document holidays)
            throws Exception {

        Date sortedDate = null;
        long dateDiff = -1;
        Vector dateList = new Vector();
        SOSDateRecord dateRecord = null;

        // we make the date our nextWorkingDay
        Date nextWorkingDay = getNextWorkingDay(date);

        //  get the root elememt "holidays"
        Element docEle = holidays.getDocumentElement();

        //get a nodelist of <holiday> elements
        NodeList nl = docEle.getElementsByTagName("holiday");

        if (nl != null && nl.getLength() > 0) {

            // sort date entries
            for (int i = 0; i < nl.getLength(); i++) {

                // get the holiday element
                Element el = (Element) nl.item(i);

                if (el.getAttribute("date") != null) {
                    dateRecord = new SOSDateRecord();
                    dateRecord.setDate(date);
                    dateList.add(dateRecord.getDate());
                }
            } // for sort date entries
            java.util.Collections.sort(dateList);

            // determine next working day
            for (int i = 0; i < dateList.size(); i++) {

                if (dateList.get(i) != null) {
                    sortedDate = (Date)dateList.get(i);
                    if (sortedDate.before(date))
                        continue;
                    dateDiff = (nextWorkingDay.getTime() - sortedDate.getTime())
                            / (60 * 60 * 1000);
                    if (dateDiff < 0)
                        continue;
                    // is sortedDate == same day 
                    if (nextWorkingDay.getTime() == sortedDate.getTime()
                            || (dateDiff < 24)) {
                        nextWorkingDay = getNextWorkingDay(nextWorkingDay);
                    }
                } // if
            } // for
        } // if     
        return nextWorkingDay;
    }// 

    /**
     * return the next working day of the week from a specified date
     *
     * @param dateStr is the date as string from which the next working day will be calculated.
     * @param holidays is the xml object which contains the holidays to be filtered
     * @return Date represents the calculated next working day of the week
     */

    public static Date getNextWorkingDay(String dateStr, Document holidays)
            throws Exception {
        return getNextWorkingDay(SOSDate.getDate(dateStr), holidays);
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated.
     * @param holidays is the xml object which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static String getNextWorkingDayAsString(Date date, Document holidays)
            throws Exception {
        return SOSDate.getDateAsString(getNextWorkingDay(date, holidays));
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param dateStr is the date as string from which the next working day will be calculated.
     * @param holidays is the xml object which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static String getNextWorkingDayAsString(String dateStr,
            Document holidays) throws Exception {
        return SOSDate.getDateAsString(getNextWorkingDay(SOSDate
                .getDate(dateStr), holidays));
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated.
     * @param holidays is the xml string which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static Date getNextWorkingDay(Date date, String holidays)
            throws Exception {

        Document doc = null;

        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //Using factory get an instance of document builder
        DocumentBuilder db = dbf.newDocumentBuilder();

        //parse using builder to get DOM representation of the XML file
        doc = db.parse((new InputSource(new StringReader(holidays))));

        return getNextWorkingDay(date, doc);
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date as string from which the next working day will be calculated.
     * @param holidays is the xml string which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static Date getNextWorkingDay(String date, String holidays)
            throws Exception {
        return getNextWorkingDay(SOSDate.getDate(date), holidays);
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param date is the date from which the next working day will be calculated.
     * @param holidays is the xml string which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static String getNextWorkingDayAsString(Date date, String holidays)
            throws Exception {
        return SOSDate.getDateAsString(getNextWorkingDay(date, holidays));
    }

    /**
     * return the next working day of the week from a specified date
     *
     * @param dateStr is the date as string from which the next working day will be calculated.
     * @param holidays is the xml string which contains the holidays to be filtered
     * @return String represents the calculated next working day of the week
     */

    public static String getNextWorkingDayAsString(String dateStr,
            String holidays) throws Exception {
        return getNextWorkingDayAsString(SOSDate.getDate(dateStr), holidays);
    }

    /**
     * Test
     */
    public static void main(String[] args) {
        try {
            // TODO Auto-generated method stub
            System.out.println(SOSDate.getDateAsString(SOSDate.getDate(
                    "01.19.2008", "dd.MM.yyyy"), "yyyy-MM-dd"));
        } catch (Exception e) {
            System.err.println("..error: " + e.getMessage());
        }
    }

}