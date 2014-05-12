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

/**
 * <p>Title: </p>
 * <p>Description: Implementation of SOSConnection for DB2</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:andreas.pueschel@sos-berlin.com">Andreas Püschel</a>
 * @resource postgresql*jdbc3.jar sos.util.jar
 */

public class SOSDB2Connection extends sos.connection.SOSConnection implements SequenceReader{

    /** replacements for %lcase, %ucase, %now, %updlock, %cast */
    private static final String replacement[] = { "LOWER", "UPPER", "CURRENT TIMESTAMP", "FOR UPDATE"};

    private static final SOSConnectionVersionLimiter versionLimiter;
    
    // init versionLimiter
    static {
    	versionLimiter = new SOSConnectionVersionLimiter();
    	versionLimiter.setMinSupportedVersion(8,0);
    	versionLimiter.setMaxSupportedVersion(8,2);
    	versionLimiter.setExcludedThroughVersion(7,999);
    }
    
    public SOSDB2Connection(Connection connection, SOSLogger logger)
            throws Exception {
        super(connection, logger);
    }

    public SOSDB2Connection(Connection connection) throws Exception {
        super(connection);
    }


    public SOSDB2Connection(String configFileName, SOSLogger logger)
            throws Exception {
        super(configFileName, logger);
    }


    public SOSDB2Connection(String configFileName) throws Exception {
        super(configFileName);
    }


    public SOSDB2Connection(String driver, String url, String dbuser,
            String dbpassword, SOSLogger logger) throws Exception {
        super(driver, url, dbuser, dbpassword, logger);
    }

 
    public SOSDB2Connection(String driver, String url, String dbuser,
            String dbpassword) throws Exception {
        super(driver, url, dbuser, dbpassword);
    }

    public void connect() throws Exception {

        Properties properties = new Properties();
        logger.debug6("calling " + SOSClassUtil.getMethodName());

        if (SOSString.isEmpty(url))
                throw new Exception(SOSClassUtil.getMethodName()
                        + ": missing database url.");
        if (SOSString.isEmpty(driver))
                throw new Exception(SOSClassUtil.getMethodName()
                        + ": missing database driver.");
        if (SOSString.isEmpty(dbuser))
                throw new Exception(SOSClassUtil.getMethodName()
                        + ": missing database user.");
        if (SOSString.isEmpty(dbpassword))
                throw new Exception(SOSClassUtil.getMethodName()
                        + ": missing database password.");

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
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            connection.rollback();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
            }
        }
    }


    /**
     * returns DB2 timestamp function
     * 
     * @param dateString
     * @return DB2 timestamp function
     * @throws java.lang.Exception
     */
    public String toDate(String dateString) throws Exception {
        if (SOSString.isEmpty(dateString))
                throw new Exception(SOSClassUtil.getMethodName()
                        + ": dateString has no value.");
        return "timestamp('" + dateString + "')";
    }


    /**
     * current date as GregorianCalendar
     * 
     * @return
     */
    protected GregorianCalendar getDateTime(String format) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();

        String timestamp = this.getSingleValue("SELECT CURRENT TIMESTAMP FROM SYSIBM.SYSDUMMY1");

        if (timestamp.length() > 19) {
            timestamp = timestamp.substring(0, 19);
        }

        java.util.Date date = sos.util.SOSDate.getDate(timestamp, format);

        gc.setTime(date);

        return gc;
    } // getDateTime
    
    /**
     * 
     * @param inputString
     * @return
     */
    protected String replaceCasts( String inputString) throws Exception {
        
        logger.debug6("Calling " + SOSClassUtil.getMethodName());
        
        Pattern pattern = Pattern.compile(CAST_PATTERN);
        Matcher matcher = pattern.matcher(inputString);
        StringBuffer buffer = new StringBuffer();
        String replaceString;
        String token;
       
        while ((matcher.find())) {

            replaceString = matcher.group();
            if ( matcher.group(1) != null &&  matcher.group(6) != null) {
                token = matcher.group(6).replaceFirst("\\)","").trim();

                if (token.matches(".*varchar.*")) {
                  replaceString = replaceString.replaceAll("varchar",")");
                  replaceString = replaceString.replaceFirst("%cast","rtrim(char");
                } else if ( token.matches(".*character.*")) {
                      replaceString = replaceString.replaceAll("character"," AS CHAR");
                      replaceString = replaceString.replaceFirst("%cast","rtrim(");
                } else if (token.matches(".*integer.*")) {
                  replaceString = replaceString.replaceAll("integer"," AS INTEGER");
                  replaceString = replaceString.replaceFirst("%","");
                }
                else if (token.matches(".*timestamp.*")) {
                	replaceString = replaceString.replaceAll("timestamp"," AS TIMESTAMP");
                    replaceString = replaceString.replaceFirst("%","");
                }
                else if (token.matches(".*datetime.*")) {
                	replaceString = replaceString.replaceAll("datetime"," AS TIMESTAMP");
                    replaceString = replaceString.replaceFirst("%","");
                }
            } // if
            if ( matcher.group(3) != null && matcher.group(4) != null) { // group 4 "VALUE <data_type>"
                token = matcher.group(4).replaceFirst("\\(","").trim();
                
                if (token.matches(".*varchar.*")) {
                   replaceString = replaceString.replaceAll("varchar",")");
                   replaceString = replaceString.replaceAll("%cast","rtrim(char");
                } else if ( token.matches(".*character.*")) {
                       replaceString = replaceString.replaceAll("character",")");
                       replaceString = replaceString.replaceAll("%cast","rtrim(char");
                } else if (token.matches(".*integer.*")) {
                   replaceString = replaceString.replaceAll("integer"," AS INTEGER");
                   replaceString = replaceString.replaceAll("%","");
               }
                else if (token.matches(".*timestamp.*")) {
                	replaceString = replaceString.replaceAll("timestamp"," AS TIMESTAMP");
                    replaceString = replaceString.replaceFirst("%","");
                }
                else if (token.matches(".*datetime.*")) {
                	replaceString = replaceString.replaceAll("datetime"," AS TIMESTAMP");
                    replaceString = replaceString.replaceFirst("%","");
                }
            }
            matcher.appendReplacement(buffer, replaceString);
        }
        matcher.appendTail(buffer);
        logger.debug6(".. result [" + buffer.toString() + "]");
        return buffer.toString();
    } // pseudoFunctions

	
	protected String getLastSequenceQuery(String sequence) {
		//return "VALUES IDENTITY_VAL_LOCAL();";	
		return "SELECT IDENTITY_VAL_LOCAL() AS INSERT_ID FROM SYSIBM.SYSDUMMY1";
	}
	
		
	public String getNextSequenceValue(String sequence) throws Exception {
		return getSingleValue("SELECT NEXTVAL FOR "+sequence+" FROM SYSIBM.SYSDUMMY1");
	}
	
	/*
	 * DB2 returns e.g.: SQL08023	 
	 */
    public int parseMajorVersion(String productVersion) throws Exception{
    	String dbVersion = productVersion.replaceAll("[^0-9]","");
    	if (dbVersion.length()<4) throw new Exception("Failed to parse major Version from String \""+productVersion+"\"");
    	int major = Integer.parseInt(dbVersion.substring(0,2));
    	return major;
    }
    

    public int parseMinorVersion(String productVersion) throws Exception{
    	String dbVersion = productVersion.replaceAll("[^0-9]","");
    	if (dbVersion.length()<4) throw new Exception("Failed to parse major Version from String \""+productVersion+"\"");
    	int minor = Integer.parseInt(dbVersion.substring(2,4));
    	return minor;
    }
    
    /**
     * a slash in a separate line concatenates ; to the preceeding line to
     * comply with procedural statements
     * Oracle ends PL/SQL statements with / in a separate line
     * MySQL ends procedural statements with \g in a separate line
     * 
     * @param contentSB     String containing the SQL Statements
     * @param splitSB		String which is used to split statements
     * @param endSB			String which is used to close procedural statements
     * @return				true if all procedural statements need endSB, or false
     * 						if only statements with END; (e.g. functions) need endSB
     * @throws Exception
     */
    protected boolean prepareGetStatements(StringBuffer contentSB,StringBuffer splitSB,StringBuffer endSB) throws Exception{
    	if(contentSB == null){ throw new Exception("contentSB is null");}
    	if(splitSB == null){ throw new Exception("splitSB is null");}
    	if(endSB == null){ throw new Exception("endSB is null");}
    	
        splitSB.append("\n@\n");
        endSB.append("");
    
    return true;
    }

    public String[] getReplacement() {
		return replacement;
	}
}