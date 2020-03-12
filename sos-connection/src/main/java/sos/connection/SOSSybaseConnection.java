package sos.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.Statement;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSClassUtil;
import sos.util.SOSString;

/** @author Andreas Püschel */
public class SOSSybaseConnection extends sos.connection.SOSConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSSybaseConnection.class);
    private static final String REPLACEMENT[] = { "LOWER", "UPPER", "GETDATE()", "holdlock" };
    private static final SOSConnectionVersionLimiter VERSION_LIMITER;

    static {
        VERSION_LIMITER = new SOSConnectionVersionLimiter();
        VERSION_LIMITER.setMinSupportedVersion(12, 0);
        VERSION_LIMITER.setMaxSupportedVersion(15, 0);
    }

    public SOSSybaseConnection(final Connection connection) throws Exception {
        super(connection);
    }

    public SOSSybaseConnection(final String configFileName) throws Exception {
        super(configFileName);
    }

    public SOSSybaseConnection(final String driver, final String url, final String dbuser, final String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
    }

    public SOSSybaseConnection(final String driver, final String url, final String dbname, final String dbuser, final String dbpassword)
            throws Exception {
        super(driver, url, dbuser, dbpassword);
        if (dbname == null) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database name.");
        }
        this.dbname = dbname;
    }

    @Override
    public void connect() throws Exception {
        Properties properties = new Properties();
        if (SOSString.isEmpty(url)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database url.");
        }
        if (SOSString.isEmpty(driver)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database driver.");
        }
        if (SOSString.isEmpty(dbuser)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database user.");
        }
        if (SOSString.isEmpty(dbpassword)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": missing database password.");
        }
        if (!SOSString.isEmpty(dbname)) {
            properties.setProperty("databasename", dbname);
        }
        properties.setProperty("user", dbuser);
        properties.setProperty("password", dbpassword);
        try {
            LOGGER.trace("calling " + SOSClassUtil.getMethodName());
            Driver driver = (Driver) Class.forName(this.driver).newInstance();
            connection = driver.connect(url, properties);
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            VERSION_LIMITER.check(this);
            LOGGER.debug(".. successfully connected to " + url);
            prepare(connection);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString(), e);
        }
    }

    @Override
    public void prepare(final Connection connection) throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        Statement stmt = null;
        String isolationLevel = "set TRANSACTION ISOLATION LEVEL READ COMMITTED";
        String chainedOn = "set CHAINED ON";
        String quotedIdentifier = "set QUOTED_IDENTIFIER ON";
        String lockTimeout = "set LOCK WAIT 3";
        String closeOnEndtran = "set CLOSE ON ENDTRAN ON";
        String datefirst = "set DATEFIRST 1";
        String isoDateFormat = "set DATEFORMAT 'ymd'";
        String defaultLanguage = "set LANGUAGE us_english";
        String textsize = "set TEXTSIZE 2048000";
        try {
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            connection.rollback();
            stmt = connection.createStatement();
            setSessionVariable(stmt, isolationLevel);
            setSessionVariable(stmt, chainedOn);
            setSessionVariable(stmt, quotedIdentifier);
            setSessionVariable(stmt, lockTimeout);
            setSessionVariable(stmt, closeOnEndtran);
            setSessionVariable(stmt, datefirst);
            setSessionVariable(stmt, isoDateFormat);
            setSessionVariable(stmt, defaultLanguage);
            setSessionVariable(stmt, textsize);
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

    private void setSessionVariable(final Statement stmt, final String command) {
        try {
            stmt.execute(command);
            LOGGER.trace(String.format(".. %1$s successfully set", command));
        } catch (Exception e) {
            LOGGER.warn(e.toString(), e);
        }
    }

    @Override
    public String toDate(final String dateString) throws Exception {
        if (SOSString.isEmpty(dateString)) {
            throw new Exception(SOSClassUtil.getMethodName() + ": dateString has no value.");
        }
        return "'" + dateString + "'";
    }

    @Override
    protected GregorianCalendar getDateTime(final String format) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        String timestamp = this.getSingleValue("select GETDATE()");
        if (timestamp.length() > 19) {
            timestamp = timestamp.substring(0, 19);
        }
        Date date = sos.util.SOSDate.getDate(timestamp, format);
        gc.setTime(date);
        return gc;
    }

    @Override
    protected String replaceCasts(final String inputString) throws Exception {
        LOGGER.debug("Calling " + SOSClassUtil.getMethodName());
        Pattern pattern = Pattern.compile(CAST_PATTERN);
        Matcher matcher = pattern.matcher(inputString);
        StringBuffer buffer = new StringBuffer();
        String replaceString;
        String token;
        LOGGER.trace("..inputString [" + inputString + "]");
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
        LOGGER.debug(".. result [" + buffer.toString() + "]");
        return buffer.toString();
    }

    @Override
    protected String getLastSequenceQuery(final String sequence) {
        return "SELECT @@IDENTITY";
    }

    @Override
    public int parseMajorVersion(final String productVersion) throws Exception {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("[0-9]+\\.[0-9]+\\.?[0-9]*");
        matcher = pattern.matcher(productVersion);
        if (matcher.find()) {
            return super.parseMajorVersion(matcher.group());
        } else {
            throw new Exception("Failed to parse major Version from String \"" + productVersion + "\"");
        }
    }

    @Override
    public int parseMinorVersion(final String productVersion) throws Exception {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("[0-9]+\\.[0-9]+\\.?[0-9]*");
        matcher = pattern.matcher(productVersion);
        if (matcher.find()) {
            return super.parseMinorVersion(matcher.group());
        } else {
            throw new Exception("Failed to parse minor Version from String \"" + productVersion + "\"");
        }
    }

    protected boolean prepareGetStatements(final StringBuffer contentSB, final StringBuffer splitSB, final StringBuffer endSB) throws Exception {
        if (contentSB == null) {
            throw new Exception("contentSB is null");
        }
        if (splitSB == null) {
            throw new Exception("splitSB is null");
        }
        if (endSB == null) {
            throw new Exception("endSB is null");
        }
        splitSB.append("\ngo\n");
        endSB.append("");
        return true;
    }

    public String[] getReplacement() {
        return REPLACEMENT;
    }
}
