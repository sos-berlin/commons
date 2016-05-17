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

/** @author Ghassan Beydoun */
public class SOSMSSQLConnection extends sos.connection.SOSConnection {

    private static final String REPLACEMENT[] = { "LOWER", "UPPER", "GETDATE()", "WITH (UPDLOCK)" };
    private static final SOSConnectionVersionLimiter VERSION_LIMITER;
    private int lockTimeout = 3000;

    static {
        VERSION_LIMITER = new SOSConnectionVersionLimiter();
        VERSION_LIMITER.setMinSupportedVersion(8, 0);
        VERSION_LIMITER.setMaxSupportedVersion(9, 0);
    }

    public SOSMSSQLConnection(Connection connection, SOSLogger logger) throws Exception {
        super(connection, logger);
    }

    public SOSMSSQLConnection(Connection connection) throws Exception {
        super(connection);
    }

    public SOSMSSQLConnection(String configFileName, SOSLogger logger) throws Exception {
        super(configFileName, logger);
        String sLockTimeout = configFileProperties.getProperty("lock_timeout");
        if (!SOSString.isEmpty(sLockTimeout)) {
            try {
                setLockTimeout(Integer.parseInt(sLockTimeout));
            } catch (Exception e) {
                throw new Exception("Bad value for lock_timeout: " + e.getMessage(), e);
            }
        }
    }

    public SOSMSSQLConnection(String configFileName) throws Exception {
        super(configFileName);
        String sLockTimeout = configFileProperties.getProperty("lock_timeout");
        if (!SOSString.isEmpty(sLockTimeout)) {
            try {
                setLockTimeout(Integer.parseInt(sLockTimeout));
            } catch (Exception e) {
                throw new Exception("Bad value for lock_timeout: " + e.getMessage(), e);
            }
        }
    }

    public SOSMSSQLConnection(String driver, String url, String dbuser, String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
    }

    public SOSMSSQLConnection(String driver, String url, String dbuser, String dbpassword, SOSLogger logger) throws Exception {
        super(driver, url, dbuser, dbpassword, logger);
    }

    public SOSMSSQLConnection(String driver, String url, String dbname, String dbuser, String dbpassword, SOSLogger logger) throws Exception {
        super(driver, url, dbuser, dbpassword, logger);
        if (dbname == null) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database name.");
        }
        this.dbname = dbname;
    }

    public SOSMSSQLConnection(String driver, String url, String dbname, String dbuser, String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
        if (dbname == null) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database name.");
        }
        this.dbname = dbname;
    }

    public void connect() throws Exception {
        Properties properties = new Properties();
        if (SOSString.isEmpty(url)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database url.");
        }
        if (SOSString.isEmpty(driver)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database driver.");
        }
        if (!SOSString.isEmpty(dbname)) {
            properties.setProperty("databasename", dbname);
        }
        if (dbuser != null && !dbuser.isEmpty() && !"[SSO]".equalsIgnoreCase(dbuser)) {
            properties.setProperty("user", dbuser);
            properties.setProperty("password", dbpassword);
        }
        properties.setProperty("selectMethod", "cursor");
        try {
            logger.debug9("calling " + SOSClassUtil.getMethodName());
            Driver driver = (Driver) Class.forName(this.driver).newInstance();
            connection = driver.connect(url, properties);
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            VERSION_LIMITER.check(this, logger);
            logger.debug6(".. successfully connected to " + url);
            prepare(connection);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString(), e);
        }
    }

    public void prepare(Connection connection) throws Exception {
        logger.debug6("calling " + SOSClassUtil.getMethodName());
        Statement stmt = null;
        String isoDateFormat = "set DATEFORMAT ymd";
        String defaultLanguage = "set LANGUAGE British";
        String lockTimeout = "set LOCK_TIMEOUT " + getLockTimeout();
        try {
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            stmt.execute(isoDateFormat + ";" + defaultLanguage + ";" + lockTimeout);
            logger.debug9(".. " + defaultLanguage + " successfully set.");
            logger.debug9(".. " + isoDateFormat + " successfully set.");
            logger.debug9(".. " + lockTimeout + " successfully set.");
            connection.rollback();
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString(), e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                //
            }
        }
    }

    public String toDate(String dateString) throws Exception {
        if (SOSString.isEmpty(dateString)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": dateString has no value.");
        }
        return "{ ts '" + dateString + "'}";
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
        String replaceString;
        String token;
        logger.debug9("..inputString [" + inputString + "]");
        while (matcher.find()) {
            replaceString = matcher.group().toLowerCase();
            if (matcher.group(1) != null && matcher.group(6) != null) {
                token = matcher.group(6).replaceFirst("\\)", "").trim();
                if (token.matches(".*varchar.*")) {
                    replaceString = replaceString.replaceAll("varchar", " as varchar");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", " as character");
                    replaceString = replaceString.replaceFirst("%cast", "cast");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", " as integer");
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
                    replaceString = replaceString.replaceAll("varchar", " as varchar");
                    replaceString = replaceString.replaceAll("%cast", "cast");
                } else if (token.matches(".*character.*")) {
                    replaceString = replaceString.replaceAll("character", " as character");
                    replaceString = replaceString.replaceAll("%cast", "cast");
                } else if (token.matches(".*integer.*")) {
                    replaceString = replaceString.replaceAll("integer", " as integer");
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
        logger.debug6(".. result [" + buffer.toString() + "]");
        return buffer.toString();
    }

    protected String getLastSequenceQuery(String sequence) {
        return "SELECT @@IDENTITY";
    }

    public int parseMajorVersion(String productVersion) throws Exception {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("[0-9]*\\.[0-9]*\\.[0-9]*");
        matcher = pattern.matcher(productVersion);
        if (matcher.find()) {
            return super.parseMajorVersion(matcher.group());
        } else {
            throw new Exception("Failed to parse major Version from String \"" + productVersion + "\"");
        }
    }

    public int parseMinorVersion(String productVersion) throws Exception {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("[0-9]*\\.[0-9]*\\.[0-9]*");
        matcher = pattern.matcher(productVersion);
        if (matcher.find()) {
            return super.parseMinorVersion(matcher.group());
        } else {
            throw new Exception("Failed to parse minor Version from String \"" + productVersion + "\"");
        }
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
        splitSB.append("(?i)\nGO\\s*\n|\n/\n");
        endSB.append("");
        return true;
    }

    public String[] getReplacement() {
        return REPLACEMENT;
    }

    public int getLockTimeout() {
        return lockTimeout;
    }

    public void setLockTimeout(int lockTimeout) {
        this.lockTimeout = lockTimeout;
    }

}