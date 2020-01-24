package sos.util;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** @author Mürüvet Öksüz */
public class SOSExceptionMessage extends Throwable {

    private static final long serialVersionUID = -4037454049983347779L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SOSExceptionMessage.class);

    public static String getExceptionMessage(final Exception exception) {
        String msg = "";
        try {
            if (exception instanceof SQLException) {
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
            LOGGER.debug(e.getMessage(), e);
        }
        return msg;
    }

    public static void main(final String[] args) {
        try {
            int i = 1 / 0;
            LOGGER.info("" + i);
        } catch (Exception e) {
            System.out.print("..error: " + getExceptionMessage(e));
        }
    }
}
