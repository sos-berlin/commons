package sos.connection;

import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Driver;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Connection;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

/** <p>
 * Title:
 * </p>
 * <p>
 * Description: Implementation of SOSConnection for PostgreSQL
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
 * @resource postgresql*jdbc3.jar sos.util.jar */

public class SOSPgSQLConnection extends sos.connection.SOSConnection implements SequenceReader {

    /** Replacements for %lcase, %ucase, %now */
    private static final String replacement[] = { "LOWER", "UPPER", "CURRENT_TIMESTAMP", "FOR UPDATE" };

    private static final SOSConnectionVersionLimiter versionLimiter;

    // intialize versionLimiter
    static {
        versionLimiter = new SOSConnectionVersionLimiter();
        versionLimiter.addSupportedVersion(8, 0);
        versionLimiter.addSupportedVersion(8, 1);
        versionLimiter.setExcludedThroughVersion(7, 999);
    }

    public SOSPgSQLConnection(Connection connection, SOSLogger logger) throws Exception {
        super(connection, logger);
        // prepare( connection) ;
    }

    public SOSPgSQLConnection(Connection connection) throws Exception {
        super(connection);
        prepare(connection);
    }

    public SOSPgSQLConnection(String configFileName, SOSLogger logger) throws Exception {

        super(configFileName, logger);
    }

    public SOSPgSQLConnection(String configFileName) throws Exception {
        super(configFileName);
    }

    public SOSPgSQLConnection(String driver, String url, String dbuser, String dbpassword, SOSLogger logger) throws Exception {
        super(driver, url, dbuser, dbpassword, logger);
    }

    public SOSPgSQLConnection(String driver, String url, String dbuser, String dbpassword) throws Exception {
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
            connection.rollback();
            stmt = connection.createStatement();
            String NUMERIC_CHARACTERS = "SELECT set_config('lc_numeric', '', true)";
            String DATE_STYLE = "SELECT set_config('datestyle', 'ISO, YMD', true)";
            String DEFAULT_TRANSACTION_ISOLATION = "SELECT set_config('default_transaction_isolation', 'repeatable read', true)";
            /*
             * stmt.addBatch(NUMERIC_CHARACTERS); stmt.addBatch(DATE_STYLE);
             * stmt.addBatch(DEFAULT_TRANSACTION_ISOLATION);
             * stmt.executeBatch();
             */
            stmt.execute(NUMERIC_CHARACTERS);
            logger.debug9(".. " + NUMERIC_CHARACTERS + " successfully set.");
            stmt.execute(DATE_STYLE);
            logger.debug9(".. " + DATE_STYLE + " successfully set.");
            stmt.execute(DEFAULT_TRANSACTION_ISOLATION);
            logger.debug9(".. " + DEFAULT_TRANSACTION_ISOLATION + " successfully set.");
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

    /** returns PgSQL timestamp function
     * 
     * @param dateString
     * @return PostgreSQL timestamp function
     * @throws java.lang.Exception */
    public String toDate(String dateString) throws Exception {
        if (SOSString.isEmpty(dateString))
            throw new Exception(SOSClassUtil.getMethodName() + ": dateString has no value.");
        return "TO_TIMESTAMP('" + dateString + "','YYYY-MM-DD HH24:MI:SS')";
    }

    protected GregorianCalendar getDateTime(String format) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();

        String timestamp = this.getSingleValue("select CURRENT_TIMESTAMP");

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
                    replaceString = replaceString.replaceAll("varchar", ",'999999999999999999')");
                    replaceString = replaceString.replaceFirst("%cast", "TRIM(TO_CHAR");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", ",'999999999999999999'");
                    replaceString = replaceString.replaceFirst("%cast", "TO_CHAR");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", ",'999999999999999999'");
                    replaceString = replaceString.replaceFirst("%cast", "TO_NUMBER");
                } else if (token.matches(".*timestamp.*")) {
                    replaceString = replaceString.replaceAll("timestamp", ",'yyyy-mm-dd HH24:MI:SS'");
                    replaceString = replaceString.replaceFirst("%cast", "TO_TIMESTAMP");
                } else if (token.matches(".*datetime.*")) {
                    replaceString = replaceString.replaceAll("datetime", ",'yyyy-mm-dd HH24:MI:SS'");
                    replaceString = replaceString.replaceFirst("%cast", "TO_TIMESTAMP");
                }
            } // if
            if (matcher.group(3) != null && matcher.group(4) != null) { // group
                                                                        // 4
                                                                        // "VALUE <data_type>"
                token = matcher.group(4).replaceFirst("\\(", "").trim();

                if (token.matches(".*varchar.*")) {
                    replaceString = replaceString.replaceAll("varchar", ",'999999999999999999')");
                    replaceString = replaceString.replaceAll("%cast", "TRIM(TO_CHAR");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", ",'999999999999999999'");
                    replaceString = replaceString.replaceAll("%cast", "TO_CHAR");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", ",'999999999999999999'");
                    replaceString = replaceString.replaceAll("%cast", "TO_NUMBER");
                } else if (token.matches(".*timestamp.*")) {
                    replaceString = replaceString.replaceAll("timestamp", ",'yyyy-mm-dd HH24:MI:SS'");
                    replaceString = replaceString.replaceFirst("%cast", "TO_TIMESTAMP");
                } else if (token.matches(".*datetime.*")) {
                    replaceString = replaceString.replaceAll("datetime", ",'yyyy-mm-dd HH24:MI:SS'");
                    replaceString = replaceString.replaceFirst("%cast", "TO_TIMESTAMP");
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
        return "SELECT currval('" + sequence + "');";
    }

    public String getNextSequenceValue(String sequence) throws Exception {
        return getSingleValue("SELECT nextval('" + sequence + "')");
    }

    /*
     * (non-Javadoc)
     * @see sos.connection.SOSConnection#getOutput()
     */
    public Vector getOutput() throws Exception {
        Vector out = new Vector();
        SQLWarning warning = getConnection().getWarnings();
        while (warning != null) {
            out.add(warning.getMessage());
            warning = warning.getNextWarning();
        }
        return out;
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

        // splitString = "\n\\\\g\n";
        // endString = "\n\\g\n";

        // gesplittet wird danach wie der endString aussieht
        splitSB.append("\\$\\${1}[\\s]+(LANGUAGE|language){1}[\\s]+(plpgsql|PLPGSQL){1}[\\s]*;");

        endSB.append("$$ LANGUAGE plpgsql;");
        // endSB wird nur bei denen, die END; haben hinzugefügt(zB : functions),
        // sonst leer
        return false;
    }

    public String[] getReplacement() {
        return replacement;
    }
}