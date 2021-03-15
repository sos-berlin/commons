package sos.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.util.SOSClassUtil;
import sos.util.SOSString;

/** @author Andreas Püschel
 * @author Ghassan Beydoun */
public class SOSPgSQLConnection extends sos.connection.SOSConnection implements SequenceReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSPgSQLConnection.class);
    private static final String REPLACEMENT[] = { "LOWER", "UPPER", "CURRENT_TIMESTAMP", "FOR UPDATE" };
    private static final SOSConnectionVersionLimiter VERSION_LIMITER;
    static {
        VERSION_LIMITER = new SOSConnectionVersionLimiter();
        VERSION_LIMITER.addSupportedVersion(8, 0);
        VERSION_LIMITER.addSupportedVersion(8, 1);
        VERSION_LIMITER.setExcludedThroughVersion(7, 999);
    }

    public SOSPgSQLConnection(Connection connection) throws Exception {
        super(connection);
        prepare(connection);
    }

    public SOSPgSQLConnection(String configFileName) throws Exception {
        super(configFileName);
    }

    public SOSPgSQLConnection(String driver, String url, String dbuser, String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
    }

    public void connect() throws Exception {
        Properties properties = new Properties();
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
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
        Driver driver = (Driver) Class.forName(this.driver).newInstance();
        connection = driver.connect(url, properties);
        if (connection == null) {
            throw new Exception("can't connect to database");
        }
        VERSION_LIMITER.check(this);
        LOGGER.debug(".. successfully connected to " + url);
        prepare(connection);
    }

    public void prepare(Connection connection) throws Exception {
        LOGGER.debug("calling " + SOSClassUtil.getMethodName());
        Statement stmt = null;
        try {
            if (connection == null) {
                throw new Exception("can't connect to database");
            }
            connection.setAutoCommit(false);
            connection.rollback();
            stmt = connection.createStatement();
            String numericCharacters = "SELECT set_config('lc_numeric', '', true)";
            String dateStyle = "SELECT set_config('datestyle', 'ISO, YMD', true)";
            String defaultTransactionIsolation = "SELECT set_config('default_transaction_isolation', 'repeatable read', true)";
            stmt.execute(numericCharacters);
            LOGGER.trace(".. " + numericCharacters + " successfully set.");
            stmt.execute(dateStyle);
            LOGGER.trace(".. " + dateStyle + " successfully set.");
            stmt.execute(defaultTransactionIsolation);
            LOGGER.trace(".. " + defaultTransactionIsolation + " successfully set.");
        } catch (Exception e) {
            throw e;
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
        LOGGER.debug("Calling " + SOSClassUtil.getMethodName());
        Pattern pattern = Pattern.compile(CAST_PATTERN);
        Matcher matcher = pattern.matcher(inputString);
        StringBuffer buffer = new StringBuffer();
        String replaceString = null;
        String token;
        LOGGER.trace("..inputString [" + inputString + "]");
        while (matcher.find()) {
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
            }
            if (matcher.group(3) != null && matcher.group(4) != null) {
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
        }
        matcher.appendTail(buffer);
        LOGGER.debug(".. result [" + buffer.toString() + "]");
        return buffer.toString();
    }

    protected String getLastSequenceQuery(String sequence) {
        return "SELECT currval('" + sequence + "');";
    }

    public String getNextSequenceValue(String sequence) throws Exception {
        return getSingleValue("SELECT nextval('" + sequence + "')");
    }

    public Vector<String> getOutput() throws Exception {
        Vector<String> out = new Vector<>();
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
        splitSB.append("\\$\\${1}[\\s]+(LANGUAGE|language){1}[\\s]+(plpgsql|PLPGSQL){1}[\\s]*;");
        endSB.append("$$ LANGUAGE plpgsql;");
        return false;
    }

    public String[] getReplacement() {
        return REPLACEMENT;
    }

}