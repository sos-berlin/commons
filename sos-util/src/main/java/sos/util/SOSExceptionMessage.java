package sos.util;

import java.sql.SQLException;

/** SOSExceptionHandler.java
 * 
 * Diese Klasse faßt eine Fehlermeldung/Exception in String um. Berücksichtigt
 * werden insbesondere die Verschachtelte Exceptions, wie z.B.
 * org.apache.commons.dbcp.SQLNestedException. Die
 * org.apache.commons.dbcp.SQLNestedException werden iteriert und zu einem
 * String zusammengefaßt.
 * 
 * @author Mürüvet Öksüz
 *
 * @version 1.0 */
public class SOSExceptionMessage extends Throwable {

    /** @param exception
     * @return */
    public static String getExceptionMessage(final Exception exception) {
        String msg = "";
        try {
            if (exception instanceof SQLException) {
                // || exception instanceof javax.mail.MessagingException) {
                SQLException sqlExcep = (SQLException) exception;
                while (sqlExcep != null) {
                    if (sqlExcep.equals(sqlExcep.getNextException())) {
                        break;
                    }
                    msg = sqlExcep.toString();
                    if (sqlExcep.getCause() != null) {
                        msg = msg + "\n" + sqlExcep.getCause();
                    }
                    sqlExcep = sqlExcep.getNextException();
                }
            } else {
                msg = exception.toString();
                if (exception.getCause() != null) {
                    msg = exception.toString() + " " + exception.getCause();
                }
            }
        } catch (Exception e) {
            System.out.print(e);
        }
        return msg;
    }

    /** Testprogramm
     * 
     * Zum Testen der Klasse SOSExceptionMessage werden Fehlermeldungen
     * generiert.
     * 
     * test = 0 -> NullPointerException test = 1 -> division by 0
     * 
     * @param args */
    public static void main(final String[] args) {
        String s = null;
        int i = 0;
        int test = 0;
        try {
            if (test == 0) {
                @SuppressWarnings({ "unused", "null" })
                boolean w = s.equals("a");
            } else if (test == 1) {
                i = 1 / 0;
            }
        } catch (Exception e) {
            String a = getExceptionMessage(e);
            System.out.print("..error: " + a);
        }
    }
}
