package sos.connection;

import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Driver;

import java.sql.Connection;
import java.sql.Statement;

import sos.util.SOSClassUtil;
import sos.util.SOSString;

/** @author Ghassan Beydoun */
public class SOSMySQLConnection extends sos.connection.SOSConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSMySQLConnection.class);
    private static final String REPLACEMENT[] = { "LCASE", "UCASE", "NOW()", "FOR UPDATE" };
    private static final SOSConnectionVersionLimiter VERSION_LIMITER;
    static {
        VERSION_LIMITER = new SOSConnectionVersionLimiter();
        VERSION_LIMITER.setExcludedThroughVersion(3, 999);
        VERSION_LIMITER.setMinSupportedVersion(4, 0);
        VERSION_LIMITER.setMaxSupportedVersion(5, 1);
    }

    public SOSMySQLConnection(Connection connection) throws Exception {
        super(connection);
    }

    public SOSMySQLConnection(String configFileName) throws Exception {
        super(configFileName);
    }

    public SOSMySQLConnection(String driver, String url, String dbuser, String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
    }

    public void connect() throws Exception {
        Properties properties = new Properties();
        if (SOSString.isEmpty(url)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database url.");
        }
        if (SOSString.isEmpty(driver)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database driver.");
        }
        if (!SOSString.isEmpty(dbuser) && !"[SSO]".equalsIgnoreCase(dbuser)) {
            if (SOSString.isEmpty(dbpassword)) {
                dbpassword = "";
            }
            properties.setProperty("user", dbuser);
            properties.setProperty("password", dbpassword);
        }
        try {
            LOGGER.debug(SOSClassUtil.getMethodName());
            Driver driver = (Driver) Class.forName(this.driver).newInstance();
            connection = driver.connect(url, properties);
            if (connection == null) {
                throw new Exception(SOSClassUtil.getMethodName() + ": can't connect to database");
            }
            LOGGER.debug(".. successfully connected to " + url);
            VERSION_LIMITER.check(this);
            prepare(connection);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString(), e);
        }
    }

    public void prepare(Connection connection) throws Exception {
        Statement stmt = null;
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        try {
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            connection.rollback();
            try {
                stmt = connection.createStatement();
                stmt.execute("SET SESSION SQL_MODE='ANSI_QUOTES'");
            } catch (Exception ex) {
                //
            } finally {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
        }
    }

    public String toDate(String dateString) throws Exception {
        if (SOSString.isEmpty(dateString)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": dateString has no value.");
        }
        return "'" + dateString + "'";
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
        LOGGER.debug("Calling " + SOSClassUtil.getMethodName());
        Pattern pattern = Pattern.compile(CAST_PATTERN);
        Matcher matcher = pattern.matcher(inputString);
        StringBuffer buffer = new StringBuffer();
        String replaceString;
        String token;
        while (matcher.find()) {
            replaceString = matcher.group().toLowerCase();
            if (matcher.group(1) != null && matcher.group(6) != null) {
                token = matcher.group(6).replaceFirst("\\)", "").trim();
                if (token.matches(".*varchar.*")) {
                    replaceString = replaceString.replaceAll("varchar", " as char)");
                    replaceString = replaceString.replaceFirst("%cast", "trim(cast");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", " as char");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", " as signed");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*timestamp.*")) {
                    replaceString = replaceString.replaceAll("timestamp", " as datetime");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*datetime.*")) {
                    replaceString = replaceString.replaceAll("datetime", " as datetime");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                }
            }
            if (matcher.group(3) != null && matcher.group(4) != null) {
                token = matcher.group(4).replaceFirst("\\(", "").trim();
                if (token.matches(".*varchar.*")) {
                    replaceString = replaceString.replaceAll("varchar", " as char)");
                    replaceString = replaceString.replaceAll("%cast", "trim(cast");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", " as char");
                    replaceString = replaceString.replaceAll("%cast", "cast");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", " as signed");
                    replaceString = replaceString.replaceAll("%cast", "cast");
                } else if (token.matches(".*timestamp.*")) {
                    replaceString = replaceString.replaceAll("timestamp", " as datetime");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*datetime.*")) {
                    replaceString = replaceString.replaceAll("datetime", " as datetime");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                }
            }
            replaceString = replaceString.toUpperCase();
            matcher.appendReplacement(buffer, replaceString);
        }
        matcher.appendTail(buffer);
        LOGGER.debug(".. result [" + buffer.toString() + "]");
        return buffer.toString();
    }

    protected String getLastSequenceQuery(String sequence) {
        return "SELECT LAST_INSERT_ID();";
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
        splitSB.append("\n\\\\g\n");
        endSB.append("");
        return true;
    }

    public String[] getReplacement() {
        return REPLACEMENT;
    }

}
