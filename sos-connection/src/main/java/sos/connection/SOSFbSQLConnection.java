package sos.connection;

import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Driver;
import java.sql.Statement;
import java.sql.Connection;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

/** <p>
 * Title:
 * </p>
 * <p>
 * Description: Implementierung der SOSCOnnection-Klasse
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SOS GmbH
 * </p>
 * 
 * @author <a href="mailto:andreas.pueschel@sos-berlin.com">Andreas Püschel</a>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @resource jaybird-full-2.0.1.jar sos.util.jar */

public class SOSFbSQLConnection extends sos.connection.SOSConnection implements SequenceReader {

    /** Umsetzung für %lcase, %ucase, %now */
    private static final String replacement[] = { "LOWER", "UPPER", "CURRENT_TIMESTAMP", "FOR UPDATE WITH LOCK" };

    private static final SOSConnectionVersionLimiter versionLimiter;

    // Initialisierung des versionLimiter
    static {
        versionLimiter = new SOSConnectionVersionLimiter();
        versionLimiter.addSupportedVersion(1, 5);
        versionLimiter.setExcludedThroughVersion(1, 4);
    }

    /** @param connection
     * @param logger
     * @throws java.lang.Exception */
    public SOSFbSQLConnection(Connection connection, SOSLogger logger) throws Exception {
        super(connection, logger);
    }

    /** @param connection
     * @throws java.lang.Exception */
    public SOSFbSQLConnection(Connection connection) throws Exception {
        super(connection);
    }

    public SOSFbSQLConnection(String configFileName, SOSLogger logger) throws Exception {

        super(configFileName, logger);
    }

    public SOSFbSQLConnection(String configFileName) throws Exception {
        super(configFileName);
    }

    public SOSFbSQLConnection(String driver, String url, String dbuser, String dbpassword, SOSLogger logger) throws Exception {
        super(driver, url, dbuser, dbpassword, logger);
    }

    public SOSFbSQLConnection(String driver, String url, String dbuser, String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
    }

    public void connect() throws Exception {

        Properties properties = new Properties();
        logger.debug6("calling " + SOSClassUtil.getMethodName());

        if (SOSString.isEmpty(url))
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database url.");
        if (SOSString.isEmpty(driver))
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database driver.");
        if (SOSString.isEmpty(dbuser))
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database user.");
        // if (SOSString.isEmpty(dbpassword))
        // throw new Exception(SOSClassUtil.getMethodName()
        // + ": missing database password.");
        if (SOSString.isEmpty(dbpassword))
            dbpassword = "";

        properties.setProperty("user", dbuser);
        properties.setProperty("password", dbpassword);

        Driver driver = (Driver) Class.forName(this.driver).newInstance();
        connection = driver.connect(url, properties);
        if (connection == null)
            throw new Exception("can't connect to database");
        versionLimiter.check(this, logger);
        logger.debug6(".. successfully connected to " + url);

        prepare(connection);

    }

    public void prepare(Connection connection) throws Exception {

        logger.debug6("calling " + SOSClassUtil.getMethodName());
        Statement stmt = null;

        try {
            if (connection == null)
                throw new Exception("can't connect to database");

            connection.setAutoCommit(false);
            connection.setTransactionIsolation(connection.TRANSACTION_REPEATABLE_READ);
            /*
             * stmt = connection.createStatement(); String SQL_DIALECT =
             * "SET SQL DIALECT 3"; stmt.execute(SQL_DIALECT);
             * logger.debug9(".. " + SQL_DIALECT + " successfully set.");
             */
            connection.rollback();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (Exception e) {
            }
        }
    }

    /** returns an FbSQL timestamp function
     * 
     * @param dateString
     * @return Firebird timestamp function
     * @throws java.lang.Exception */
    public String toDate(String dateString) throws Exception {
        if (SOSString.isEmpty(dateString))
            throw new Exception(SOSClassUtil.getMethodName() + ": dateString has no value.");
        return "CAST('" + dateString + "' AS TIMESTAMP)";
    }

    protected GregorianCalendar getDateTime(String format) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();

        String timestamp = this.getSingleValue("select FIRST 1 CURRENT_TIMESTAMP from RDB$DATABASE");

        if (timestamp.length() > 19) {
            timestamp = timestamp.substring(0, 19);
        }

        java.util.Date date = sos.util.SOSDate.getDate(timestamp, format);

        gc.setTime(date);

        return gc;
    }

    protected String replaceCasts(String inputString) throws Exception {

        logger.debug6("Calling " + SOSClassUtil.getMethodName());

        Pattern pattern = Pattern.compile(CAST_PATTERN);
        Matcher matcher = pattern.matcher(inputString);
        StringBuffer buffer = new StringBuffer();
        String replaceString = null;
        String token;

        logger.debug9("..inputString [" + inputString + "]");

        while ((matcher.find())) {

            replaceString = matcher.group().toLowerCase();
            if (matcher.group(1) != null && matcher.group(6) != null) {
                token = matcher.group(6).replaceFirst("\\)", "").trim();

                if (token.matches(".*varchar.*")) {
                    replaceString = replaceString.replaceAll("varchar", " as varchar(1000)");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", " as character");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", " as integer");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*timestamp.*")) {
                    replaceString = replaceString.replaceAll("timestamp", " as timestamp");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*datetime.*")) {
                    replaceString = replaceString.replaceAll("datetime", " as timestamp");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                }
            } // if
            if (matcher.group(3) != null && matcher.group(4) != null) { // group
                                                                        // 4
                                                                        // "VALUE <data_type>"
                token = matcher.group(4).replaceFirst("\\(", "").trim();

                if (token.matches(".*varchar.*")) {
                    replaceString = replaceString.replaceAll("varchar", " as varchar(1000)");
                    replaceString = replaceString.replaceAll("%cast", "cast");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", " as character");
                    replaceString = replaceString.replaceAll("%cast", "cast");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", " as integer");
                    replaceString = replaceString.replaceAll("%cast", "cast");
                } else if (token.matches(".*timestamp.*")) {
                    replaceString = replaceString.replaceAll("timestamp", " as timestamp");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*datetime.*")) {
                    replaceString = replaceString.replaceAll("datetime", " as timestamp");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                }
            }
            replaceString = replaceString.toUpperCase();
            matcher.appendReplacement(buffer, replaceString);
        } // while
        matcher.appendTail(buffer);

        logger.debug6(".. result [" + buffer.toString() + "]");
        return buffer.toString();
    } // pseudoFunctions

    protected String getLastSequenceQuery(String sequence) {
        return "SELECT GEN_ID(" + sequence + ", 0) FROM RDB$DATABASE;";
    }

    public String getNextSequenceValue(String sequence) throws Exception {
        return getSingleValue("SELECT GEN_ID(" + sequence + ", 1) FROM RDB$DATABASE;");
    }

    protected boolean prepareGetStatements(StringBuffer contentSB, StringBuffer splitSB, StringBuffer endSB) throws Exception {
        if (contentSB == null) {
            throw new Exception("contentSB is null");
        }
        if (splitSB == null) {
            throw new Exception("splitSB is null");
        }
        if (endSB == null) {
            throw new Exception("endSB is null");
        }

        StringBuffer patterns = new StringBuffer("set+[\\s]*term[inator]*[\\s]*(.*);");
        Pattern p = Pattern.compile(patterns.toString());
        Matcher matcher = p.matcher(contentSB.toString().toLowerCase().trim());
        if (matcher.find()) {
            // System.out.println("group:" + matcher.group(1) + ":\n");
            // System.out.println("start:" + matcher.start() + "\n");
            // System.out.println("end:" + matcher.end() + "\n");
            splitSB.append("\\" + matcher.group(1));

            String strContent = contentSB.toString().replaceAll("(?i)set+[\\s]*term[inator]*[\\s]*.*\\n", "");
            contentSB.delete(0, contentSB.length());
            contentSB.append(strContent);
        } else {
            splitSB.append("\n/\n");
        }
        endSB.append("");

        return true;
    }

    public String[] getReplacement() {
        return replacement;
    }
}