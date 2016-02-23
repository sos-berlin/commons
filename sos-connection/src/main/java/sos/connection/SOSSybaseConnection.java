package sos.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

/** Title:
 * <p>
 * Description: Implementation of SOSConnection-class for Sybase Adaptive
 * Enterprise Server (15)
 * </p>
 * Copyright: Copyright (c) 2007 Company: SOS GmbH
 * 
 * @author <a href="mailto:andreas.pueschel@sos-berlin.com">Andreas Püschel</a>
 * @version $Id: SOSSybaseConnection.java 2887 2007-08-07 15:53:12Z al $ */
public class SOSSybaseConnection extends sos.connection.SOSConnection {

    /** replacements for %lcase, %ucase, %now, %updlock */
    private static final String replacement[] = { "LOWER", "UPPER", "GETDATE()", "holdlock" };
    private static final SOSConnectionVersionLimiter versionLimiter;

    // initialize versionLimiter
    static {
        versionLimiter = new SOSConnectionVersionLimiter();
        versionLimiter.setMinSupportedVersion(12, 0);
        versionLimiter.setMaxSupportedVersion(15, 0);
    }

    public SOSSybaseConnection(final Connection connection, final SOSLogger logger) throws Exception {
        super(connection, logger);
    }

    public SOSSybaseConnection(final Connection connection) throws Exception {
        super(connection);
    }

    public SOSSybaseConnection(final String configFileName, final SOSLogger logger) throws Exception {
        super(configFileName, logger);
    }

    public SOSSybaseConnection(final String configFileName) throws Exception {
        super(configFileName);
    }

    /** @see SOSConnection#SOSConnection(String, String, String, String) */
    public SOSSybaseConnection(final String driver, final String url, final String dbuser,
            final String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
    }

    /** @see #SOSConnection(String, String, String, String, SOSLogger) */
    public SOSSybaseConnection(final String driver, final String url, final String dbuser,
            final String dbpassword, final SOSLogger logger) throws Exception {
        super(driver, url, dbuser, dbpassword, logger);
    }

    public SOSSybaseConnection(final String driver, final String url, final String dbname,
            final String dbuser, final String dbpassword, final SOSLogger logger)
            throws Exception {
        super(driver, url, dbuser, dbpassword, logger);
        if (dbname == null) {
            throw new Exception(SOSClassUtil.getMethodName()
                    + ": missing database name.");
        }
        this.dbname = dbname;
    }

    public SOSSybaseConnection(final String driver, final String url, final String dbname,
            final String dbuser, final String dbpassword) throws Exception {
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
            logger.debug9("calling " + SOSClassUtil.getMethodName());
            Driver driver = (Driver) Class.forName(this.driver).newInstance();
            connection = driver.connect(url, properties);
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            versionLimiter.check(this, logger);
            logger.debug6(".. successfully connected to " + url);
            prepare(connection);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString(), e);
        }
    }

    @Override
    public void prepare(final Connection connection) throws Exception {
        logger.debug6("calling " + SOSClassUtil.getMethodName());
        Statement stmt = null;
        String ISOLATION_LEVEL = "set TRANSACTION ISOLATION LEVEL READ COMMITTED";
        String CHAINED_ON = "set CHAINED ON";
        String QUOTED_IDENTIFIER = "set QUOTED_IDENTIFIER ON";
        String LOCK_TIMEOUT = "set LOCK WAIT 3";
        String CLOSE_ON_ENDTRAN = "set CLOSE ON ENDTRAN ON";
        String DATEFIRST = "set DATEFIRST 1";
        String ISO_DATE_FORMAT = "set DATEFORMAT 'ymd'";
        String DEFAULT_LANGUAGE = "set LANGUAGE us_english";
        String TEXTSIZE = "set TEXTSIZE 2048000";
        try {
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.setAutoCommit(false);
            connection.rollback();
            stmt = connection.createStatement();
            setSessionVariable(stmt, ISOLATION_LEVEL);
            setSessionVariable(stmt, CHAINED_ON);
            setSessionVariable(stmt, QUOTED_IDENTIFIER);
            setSessionVariable(stmt, LOCK_TIMEOUT);
            setSessionVariable(stmt, CLOSE_ON_ENDTRAN);
            setSessionVariable(stmt, DATEFIRST);
            setSessionVariable(stmt, ISO_DATE_FORMAT);
            setSessionVariable(stmt, DEFAULT_LANGUAGE);
            setSessionVariable(stmt, TEXTSIZE);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString(), e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void setSessionVariable(final Statement stmt, final String command) {
        try {
            stmt.execute(command);
            logger.debug9(String.format(".. %1$s successfully set", command));
        } catch (Exception e) {
            try {
                logger.warn(e.toString());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /** returns Sybase timestamp function
     * 
     * @param dateString
     * @return Sybase timestamp function
     * @throws java.lang.Exception */
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
        java.util.Date date = sos.util.SOSDate.getDate(timestamp, format);
        gc.setTime(date);
        return gc;
    }

    @Override
    protected String replaceCasts(final String inputString) throws Exception {
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

    @Override
    protected String getLastSequenceQuery(final String sequence) {
        return "SELECT @@IDENTITY";
    }

    /*
     * Sybase Adaptive Server Enterprise returns as productVersion e.g. Adaptive
     * Server Enterprise/15.0.2/EBF 14332/P/NT (IX86)/Windows
     * 2000/ase1502/2486/32-bit/OPT/Thu May 24 04:10:36 2007 Adaptive Server
     * Enterprise/15.0/EBF 13446 ESD#2/P/RS6000/AIX
     * 5.2/ase150/2193/64-bit/FBO/Wed May 17 18:47:27 2006 Dec 17 2002 14:22:05
     * Copyright (c) 1988-2003 Microsoft Corporation Developer Edition on
     * Windows NT 5.2 (Build 3790: ) or: 8.00.760
     */
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
        return replacement;
    }
}
