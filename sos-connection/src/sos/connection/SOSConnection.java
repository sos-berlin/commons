package sos.connection;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sos.connection.util.SOSProfiler;
import sos.util.NullBufferedWriter;
import sos.util.SOSClassUtil;
import sos.util.SOSCommandline;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;
import sos.util.SOSString;

/**
 * <p>Title: </p>
 * <p>Description: abstract class for database connection </p>
 * <p>Note that the following entries should be available in the <i>sos_settings.ini</i> file
 * <p>class=&lt;connection class name&gt; : the connection type.<br>
 * <b>Example</b> for Oracle "class=SOSOracleConnection"
 * <p>driver=&lt;name of jdbc driver&gt;<br>
 * <b>Examples:</b><br>
 * <ul>
 * <li>Oracle oracle.jdbc.driver.OracleDriver</li>
 * <li>MSSQL: com.microsoft.jdbc.sqlserver.SQLServerDriver</li>
 * <li>MySQL: org.gjt.mm.mysql.Driver</li>
 * <li>PgSQL: org.postgresql.Driver</li>
 * <li>FbSQL: org.firebirdsql.jdbc.FBDriver</li>
 * <li>DB2: com.ibm.db2.jcc.DB2Driver</li>
 * </ul>
 * <p>url=&lt;url of the Database Server&gt;<br>
 * <b>Examples:</b><br>
 * <ul>
 * <li>Oracle: url=jdbc:oracle:thin:@&lt;host&gt;:1521:&lt;SID&gt;</li>
 * <li>MSSQL: url=jdbc:microsoft:sqlserver://&lt;host&gt;:1433;databaseName=&lt;database&gt;</li>
 * <li>MySQL: url=jdbc:mysql://&lt;host&gt;/&lt;database&gt;</li>
 * <li>PgSQL: url=jdbc:postgresql://&lt;host&gt;/&lt;database&gt;</li>
 * <li>FbSQL: url=jdbc:firebirdsql://&lt;host&gt;/&lt;database&gt;</li>
 * <li>DB2: url=jdbc:db2://&lt;host&gt;:&lt;port&gt;/&lt;database&gt;:driverType=2;retrieveMessagesFromServerOnGetMessage=true;</li>
 * </ul>
 * <p>user=&lt;username&gt;
 * <p>password=&lt;password&gt;
 *
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @resource ojdbc14.jar sos.util.jar
 * $Id$
 */

public abstract class SOSConnection {

	protected Properties			configFileProperties					= new Properties();

	protected Statement				statement;

	protected ResultSet				resultSet;

	protected static final String	NLS_DE									= "DE";

	protected static final String	NLS_ISO									= "ISO";

	/** default case for column names */
	protected boolean				lowerCase								= true;

	/** flag if a resultset is expected by exec **/
	protected boolean				currentStatementsExecReturnsResultset	= false;
	private boolean					execReturnsResultSet					= false;

	/** JDBC Driver */
	protected String				driver;

	/** url of the database */
	protected String				url;

	/** database user */
	protected String				dbuser;

	/** password of the database user */
	protected String				dbpassword;

	/** name of the database */
	protected String				dbname;

	/** connection-objekt of the current database connection */
	public Connection				connection;

	protected SOSLogger				logger									= null;

	public static SOSProfiler		profiler								= null;

	protected boolean				fieldNameUpperCase						= true;

	protected boolean				tableNameUpperCase						= true;

	/** internal use for getStatements */
	private String					beginProcedure							= "";

	/**
	 * used for masking quotes when doing replacements
	 */
	private final String			replaceBackslash						= "\\\\'";

	private final String			replacementBackslash					= "XxxxX";

	private final String			replaceDoubleApostrophe					= "''";

	private final String			replacementDoubleApostrophe				= "YyyyY";

	// protected final String CAST_PATTERN  = "(%cast)*(\\()*(%cast)+\\s*(\\(\\s*\\S+\\s*(\\S+?).*?)\\)(\\s*(\\+|\\-)*[0-9]*\\s*\\S*(\\)))*";
	protected final String			CAST_PATTERN							= "(\\s*%cast\\s*)*\\s*(\\()*\\s*(\\s*%cast\\s*)+\\s*(\\(\\s*\\S+\\s*(\\S+?).*?)\\)(\\s*(\\+|\\-)*[0-9]*\\s*\\S*(\\)))*";
	// please, Do NOT remove this line _____(__1__)_______(__3__)_____(_____________4______________)(____(___7___)_______6__________)

	// default compatibility level for database version check
	protected int					compatibility							= SOSConnectionVersionLimiter.CHECK_OFF;

	/** db version*/
	private int						majorVersion							= -1;
	private int						minorVersion							= 0;
	private String					productVersion							= "";

	/**
	 * @throws java.lang.Exception
	 */
	public SOSConnection() throws Exception {
	}

	/**
	 * @param connection connected Connection object (from a connection pool)
	 * @param logger SOSLogger instance
	 * @throws java.lang.Exception
	 */
	public SOSConnection(final Connection connection, final SOSLogger logger) throws Exception {
		this.connection = connection;
		this.logger = logger;
	}

	/**
	 * @param connection connected Connection object (from a connection pool)
	 * @throws java.lang.Exception
	 */
	public SOSConnection(final Connection connection) throws Exception {

		this(connection, new SOSStandardLogger(new NullBufferedWriter(new OutputStreamWriter(System.out)), SOSStandardLogger.DEBUG9));

	}

	/**
	 * Constructor which may be used by subclasses
	 *
	 * @param configFileName
	 *            Name of the configuration file
	 * @param logger
	 *            SOSLogger instance
	 * @throws java.lang.Exception
	 * @see #SOSConnection(String, String, String, String, SOSLogger)
	 */
	public SOSConnection(final String configFileName, final SOSLogger logger) throws Exception {

		InputStream in = null;

		try {

			logger.debug6("calling " + SOSClassUtil.getMethodName());
			if (logger == null)
				throw new Exception("missing logger.");

			File file = new File(configFileName);
			if (!file.exists())
				throw new Exception("make sure that the file [" + configFileName + "] does exist!");

			in = new BufferedInputStream(new FileInputStream(configFileName));
			logger.debug9(".. configFileName: " + configFileName);
			configFileProperties.load(in);

			if (SOSString.isEmpty(configFileProperties.getProperty("driver")))
				throw new Exception("driver missing.");

			driver = configFileProperties.getProperty("driver");
			if (!SOSString.isEmpty(driver)) {
				driver = driver.trim();
			}

			url = configFileProperties.getProperty("url");
			if (!SOSString.isEmpty(url)) {
				url = url.trim();
			}

			dbname = configFileProperties.getProperty("db");
			if (!SOSString.isEmpty(dbname)) {
				dbname = dbname.trim();
			}

			dbuser = configFileProperties.getProperty("user");
			if (!SOSString.isEmpty(dbuser)) {
				dbuser = dbuser.trim();
			}

			dbpassword = configFileProperties.getProperty("password");
			if (!SOSString.isEmpty(dbpassword)) {
				dbpassword = dbpassword.trim();
			}
			this.logger = logger;
			processPassword();

			String sCompatibility = configFileProperties.getProperty("compatibility");
			if (!SOSString.isEmpty(sCompatibility)) {
				setCompatibility(getCompatibility(sCompatibility));
			}

		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (in != null)
				in.close();
		}
	}

	/**
	 * Constructor which may be used by subclasses
	 *
	 * @param driver
	 *            jdbc class
	 * @param url
	 *            jdbc url
	 * @param dbuser
	 *            database user
	 * @param dbpassword
	 *            database password
	 * @throws java.lang.Exception
	 * @see #SOSConnection(String, SOSLogger)
	 */
	public SOSConnection(final String driver, final String url, final String dbuser, final String dbpassword, final SOSLogger logger) throws Exception {

		if (logger == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": missing logger.");
		this.logger = logger;

		logger.debug6("calling " + SOSClassUtil.getMethodName());

		if (SOSString.isEmpty(driver))
			throw new Exception(SOSClassUtil.getMethodName() + ": missing database driver.");

		this.driver = driver;
		this.url = url;
		this.dbuser = dbuser;
		this.dbpassword = dbpassword;
		processPassword();

		logger.debug9(".. driver=" + driver + ", url=" + url + ", dbuser=" + dbuser);
	}

	/**
	 * Constructor which may be used by subclasses
	 *
	 * @param driver
	 *            jdbc class
	 * @param url
	 *            jdbc url
	 * @param dbuser
	 *            database user
	 * @param dbpassword
	 *            database password
	 * @param compatibility compatibility check level
	 * @throws java.lang.Exception
	 * @see #SOSConnection(String, SOSLogger)
	 */
	public SOSConnection(final String driver, final String url, final String dbuser, final String dbpassword, final SOSLogger logger, final int compatibility)
			throws Exception {
		this(driver, url, dbuser, dbpassword, logger);
		setCompatibility(compatibility);
	}

	/**
	 * Constructor which may be used by subclasses
	 *
	 * @param configFileName
	 *            Name of the configuration file
	 * @throws java.lang.Exception
	 * @see #SOSConnection(String, String, String, String, SOSLogger)
	 * @see #SOSConnection(String, String, String, String)
	 */
	public SOSConnection(final String configFileName) throws Exception {
		this(configFileName, new SOSStandardLogger(new NullBufferedWriter(new OutputStreamWriter(System.out)), SOSStandardLogger.DEBUG9));
	}

	/**
	 * Constructor which may be used by subclasses
	 *
	 * @param driver
	 *            jdbc class
	 * @param url
	 *            jdbc url
	 * @param dbuser
	 *            database user
	 * @param dbpassword
	 *            database password
	 * @throws java.lang.Exception
	 */
	public SOSConnection(final String driver, final String url, final String dbuser, final String dbpassword) throws Exception {
		this(driver, url, dbuser, dbpassword, new SOSStandardLogger(new NullBufferedWriter(new OutputStreamWriter(System.out)), SOSStandardLogger.DEBUG9));
	}

	/**
	 * Checks if a command needs to be executed to get the password
	 */
	private void processPassword() {

		dbpassword = SOSCommandline.getExternalPassword(dbpassword, logger);
	}

	/**
	 * returns the class name of the connection class from the configuration file
	 *
	 * @param configFileName
	 *            configuration file
	 * @param logger
	 *            SOSLogger instance
	 * @throws java.lang.Exception
	 * @see #SOSConnection(String, String, String, String, SOSLogger)
	 */
	public static String getClassName(final String configFileName, final SOSLogger logger) throws Exception {

		Properties config = new Properties();
		InputStream in = null;
		String className = null;

		try {

			if (logger != null) {
				logger.debug9("calling " + SOSClassUtil.getMethodName());
			}
			// if (logger == null) throw new Exception("missing logger.");

			File file = new File(configFileName);
			if (!file.exists())
				throw new Exception("configuration file not found: " + configFileName);

			in = new BufferedInputStream(new FileInputStream(configFileName));
			if (logger != null) {
				logger.debug9(".. configuration file: " + configFileName);
			}
			config.load(in);

			if (SOSString.isEmpty(config.getProperty("class")))
				throw new Exception("class missing.");

			className = config.getProperty("class").trim();

		}
		catch (Exception e) {
			throw e;
		}
		finally {
			if (in != null)
				try {
					in.close();
				}
				catch (Exception ignore) {
				}
		}
		return className;
	}

	/**
	 * Creates a new SOSConnection object for the given configuration file
	 * @param configFileName configuration file
	 * @return connection new database connection
	 */
	public static SOSConnection createInstance(final String configFileName, final SOSLogger logger) throws Exception {

		logger.debug6("calling " + SOSClassUtil.getMethodName());
		String className = SOSConnection.getClassName(configFileName, logger);
		logger.debug9(".. creating instance for: " + className);
		Object[] arguments = { configFileName, logger };
		return createInstance(className, arguments);
	}

	/**
	 * Creates a new SOSConnection object for the given coneection
	 *
	 * @param className name of the SOSConnection subclass (fully qualified if not in sos.connection)
	 * @param connection SOSConnection object
	 * @param logger SOSLogger instance
	 * @return connection new database connection
	 * @throws java.lang.Exception
	 */
	public static SOSConnection createInstance(final String className, final Connection connection, final SOSLogger logger) throws Exception {

		logger.debug6("calling " + SOSClassUtil.getMethodName());
		Object[] arguments = { connection, logger };
		return createInstance(className, arguments);

	}

	/**
	 * Creates a new SOSConnection object for the given coneection
	 *
	 * @param className name of the SOSConnection subclass (fully qualified if not in sos.connection)
	 * @param connection SOSConnection Object
	 * @return connection new database connection
	 * @throws java.lang.Exception
	 */
	public static SOSConnection createInstance(final String className, final Connection connection) throws Exception {
		Object[] arguments = { connection };
		return createInstance(className, arguments);
	}

	/**
	 * Creates a new SOSConnection object for the given configuration file
	 * @param configFileName configuration file
	 *
	 * @return connection new database connection

	 */
	public static SOSConnection createInstance(final String configFileName) throws Exception {

		String className = SOSConnection.getClassName(configFileName, null);
		Object[] arguments = { configFileName };
		return createInstance(className, arguments);
	}

	/**
	 * Creates a new SOSConnection object
	 *
	 * @param className name of the SOSConnection subclass (fully qualified if not in sos.connection)
	 * @param driver jdbc driver class
	 * @param url
	 *            jdbc url
	 * @param dbuser
	 *            database user
	 * @param dbpassword
	 *            database password
	 * @return database new database connection
	 * @throws java.lang.Exception
	 */
	public static SOSConnection createInstance(final String className, final String driver, final String url, final String dbuser, final String dbpassword)
			throws Exception {
		Object[] arguments = { driver, url, dbuser, dbpassword };
		return createInstance(className, arguments);
	}

	/**
	 * Creates a new SOSConnection object
	 *
	 * @param className name of the SOSConnection subclass (fully qualified if not in sos.connection)
	 * @param driver jdbc driver class
	 * @param url
	 *            jdbc url
	 * @param dbuser
	 *            database user
	 * @param dbpassword
	 *            database password
	 * @return connection der aktuellen Datenbankverbindung.
	 * @throws java.lang.Exception
	 */
	public static SOSConnection createInstance(final String className, final String driver, final String url, final String dbuser, final String dbpassword,
			final SOSLogger logger) throws Exception {
		Object[] arguments = { driver, url, dbuser, dbpassword, logger };
		return createInstance(className, arguments);
	}

	/**
	 * returns the java.sql.connection object
	 * @return database connection
	 */
	public Connection getConnection() throws Exception {

		if (connection == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established.");

		return connection;
	}

	/**
	 * returns a row as HashMap
	 *
	 * @param query query for one row
	 * @return HashMap with column names in lower case
	 * @throws java.lang.Exception
	 * @see #getSingleAsProperties
	 */
	public HashMap getSingle(String query) throws Exception {

		HashMap results = new LinkedHashMap();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		int columnCount = 0;
		String key, value;

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			try {
				if (profiler != null)
					profiler.start(query);
			}
			catch (Exception e) {
			}

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();

			if (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(i);
					if (SOSString.isEmpty(value))
						value = "";
					results.put(normalizeKey(key), value.trim());
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.toString(), e);
		}
		finally {
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return results;
	}

	/**
	 * returns a row as Properties
	 *
	 * @param query SQL query for a row
	 * @return Properties with column names in lower case
	 * @throws java.lang.Exception
	 * @see #getSingle( String )
	 */
	public Properties getSingleAsProperties(String query) throws Exception {

		Properties results = new Properties();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		int columnCount = 0;
		String key, value;

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();

			if (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(i);
					if (SOSString.isEmpty(value))
						value = "";
					logger.debug9(SOSClassUtil.getMethodName() + ", key= " + key + ", value= " + value);
					results.setProperty(normalizeKey(key), value.trim());
				}
			}

			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.toString(), e);
		}
		finally {
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return results;
	}

	/**
	 * reutrns multiple rows with two columns as properties
	 *
	 * @param query SQL query
	 * @return Properties with values of the first column as key and values of the
	 * second column as value
	 * @throws java.lang.Exception
	 * @see #getSingle( String )
	 */
	public Properties getArrayAsProperties(String query) throws Exception {

		Properties results = new Properties();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		int columnCount = 0;
		String key, value;

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();
			while (rs.next()) {

				if (columnCount > 1) {
					key = rs.getString(1);
					value = rs.getString(2);
					if (SOSString.isEmpty(value))
						value = "";
					logger.debug9(SOSClassUtil.getMethodName() + ", key= " + key + ", value= " + value);
					results.setProperty(normalizeKey(key), value.trim());
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.toString(), e);
		}
		finally {
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return results;
	}

	/**
	 * returns a scalar value. The statement may only return one column
	 *
	 * @param query query for only one column
	 * @return String result of the query
	 * @throws java.lang.Exception
	 */
	public String getSingleValue(String query) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				profiler.start(query);

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				result = rs.getString(1);
				if (SOSString.isEmpty(result))
					result = "";
			}
			logger.debug9(SOSClassUtil.getMethodName() + ".. successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.toString(), e);
		}
		finally {
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return result.trim();
	}

	/**
	 * returns the resultset as an ArrayList of HashMaps. Fieldnames
	 * inside the HashMap are lower case
	 *
	 * @param query SQL query
	 * @return ArrayList of HashMaps
	 * @throws java.lang.Exception
	 */
	public ArrayList getArray(String query) throws Exception {

		ArrayList results = new ArrayList();
		HashMap record = new LinkedHashMap();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		String key, value;
		int columnCount = 0;

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName() + ": " + query);

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();

			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(meta.getColumnName(i));
					if (SOSString.isEmpty(value))
						value = "";
					record.put(normalizeKey(key), value.trim());
				}
				results.add(record);
				record = new HashMap();
			} // while

			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.toString(), e);
		}
		finally {
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return results;
	}

	/**
	 * returns the resultset as a Vector of HashMaps. Fieldnames
	 * inside the HashMap are lower case
	 *
	 * @param query SQL query
	 * @return Vector of HashMaps
	 * @throws java.lang.Exception
	 */
	public Vector getArrayAsVector(String query) throws Exception {

		Vector results = new Vector();
		HashMap record = new LinkedHashMap();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		String key, value;
		int columnCount = 0;

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName() + ": " + query);

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();

			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(meta.getColumnName(i));
					if (SOSString.isEmpty(value))
						value = "";
					record.put(normalizeKey(key), value.trim());
				}
				results.add(record);
				record = new HashMap();
			} // while

			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.toString(), e);
		}
		finally {
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return results;
	}

	/**
	 * returns the result as ArrayList. The query may only fetch one column per
	 * row
	 *
	 * @param query SQL query
	 * @return ArrayList containing one element per row (only first column)
	 * @throws java.lang.Exception
	 * @see #getArray( String )
	 */
	public ArrayList getArrayValue(String query) throws Exception {

		ArrayList results = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		String value;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null)
				profiler.start(query);
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				value = rs.getString(1);
				if (!SOSString.isEmpty(value))
					results.add(value.trim());
			} // while
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.toString(), e);
		}
		finally {
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return results;
	}

	/**
	 * updates a blob
	 *
	 * @param tableName
	 *            taget table
	 * @param columnName
	 *            target column
	 * @param data
	 *            blob data byte-array
	 * @param condition
	 *            condition(s) (excluding WHERE) of the SQL statement
	 * @return number bytes written
	 * @throws java.lang.Exception
	 * @see #getBlob( String )
	 * @see #getBlob( String, String )
	 */
	public long updateBlob(final String tableName, final String columnName, final byte[] data, final String condition) throws Exception {

		ByteArrayInputStream in = null;
		PreparedStatement pstmt = null;
		StringBuffer query = null;
		String theQuery = null;
		try {

			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			if (tableName == null)
				throw new NullPointerException("tableName is null.");
			if (columnName == null)
				throw new NullPointerException("columnName is null.");
			if (data == null || data.length <= 0)
				throw new NullPointerException("missing data.");

			in = new ByteArrayInputStream(data);

			query = new StringBuffer("UPDATE ");

			if (tableNameUpperCase)
				query.append(tableName.toUpperCase());
			else
				query.append(tableName);

			if (fieldNameUpperCase) {
				query.append(" SET \"");
				query.append(columnName.toUpperCase());
				query.append("\" = ? ");
			}
			else {
				query.append(" SET ");
				query.append(columnName);
				query.append(" = ? ");
			}

			if (!SOSString.isEmpty(condition)) {
				query.append(" WHERE ");
				query.append(condition);
			}

			theQuery = this.normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);

			if (profiler != null)
				try {
					profiler.start(theQuery);
				}
				catch (Exception e) {
				}

			pstmt = connection.prepareStatement(theQuery);
			pstmt.setBinaryStream(1, in, data.length);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (in != null)
				try {
					in.close();
				}
				catch (Exception e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return data.length;
	}

	/**
	 * updates a clob
	 *
	 * @param tableName
	 *            taget table
	 * @param columnName
	 *            target column
	 * @param data
	 *            clob String
	 * @param condition
	 *            condition(s) (excluding WHERE) of the SQL statement
	 * @return number bytes written
	 * @throws java.lang.Exception
	 * @see #getClob( String )
	 * @see #getClob( String, String )
	 */
	public long updateClob(final String tableName, final String columnName, final String data, final String condition) throws Exception {

		PreparedStatement pstmt = null;
		StringBuffer query = null;
		long totalBytesWritten = 0;
		String theQuery = null;

		try {

			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			if (SOSString.isEmpty(tableName))
				throw new NullPointerException("tableName is null.");
			if (SOSString.isEmpty(columnName))
				throw new NullPointerException("columnName is null.");
			if (SOSString.isEmpty(data))
				throw new NullPointerException("missing data.");

			query = new StringBuffer("UPDATE ");
			if (tableNameUpperCase)
				query.append(tableName.toUpperCase());
			else
				query.append(tableName);

			if (fieldNameUpperCase) {
				query.append(" SET \"");
				query.append(columnName.toUpperCase());
				query.append("\" = ? ");
			}
			else {
				query.append(" SET ");
				query.append(columnName);
				query.append(" = ? ");
			}

			if (!SOSString.isEmpty(condition)) {
				query.append(" WHERE ");
				query.append(condition);
			}
			theQuery = this.normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);

			if (profiler != null)
				try {
					profiler.start(theQuery);
				}
				catch (Exception e) {
				}

			pstmt = connection.prepareStatement(theQuery);
			totalBytesWritten = data.length();
			pstmt.setCharacterStream(1, new java.io.StringReader(data), (int) totalBytesWritten);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				profiler.stop("ERROR", e.getMessage());
			throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString(), e);
		}
		finally {
			if (pstmt != null)
				try {
					pstmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return totalBytesWritten;
	}

	/**
	 * returns content of a clob
	 *
	 * @param query
	 *            SQL query
	 * @return content of the clob or empty string
	 * @throws java.lang.Exception
	 * @see #updateClob( String, String, String, String )
	 * @see #updateClob( String, String, File, String )
	 * @see #getClob( String, String )
	 */
	public String getClob(String query) throws Exception {

		ResultSet rs = null;
		Statement stmt = null;
		Reader in = null;
		int bytesRead;
		StringBuffer sb = new StringBuffer();
		;

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}
			stmt = connection.createStatement();
			try {
				rs = stmt.executeQuery(query);
			}
			catch (Exception e) {
				logger.debug6(".. query failed: " + query + ": " + e.toString());
				throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
			}

			if (rs.next()) {
				in = rs.getCharacterStream(1);
				if (in == null) {
					logger.debug6(".. ResultSet returns NULL value.");
					return "";
				}

				if ((bytesRead = in.read()) != -1) { // hat CLOB-Feld 0 bytes?
					sb.append((char) bytesRead);
				}
				else {
					logger.debug6(".. CLOB column has 0 bytes.");
					return "";
				}

				while ((bytesRead = in.read()) != -1) {
					sb.append((char) bytesRead);
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (in != null)
				try {
					in.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return sb.toString();
	}

	/**
	 * writes the contents of a clob to a file
	 *
	 * @param query
	 *            SQL query
	 * @return number of bytes written
	 * @throws java.lang.Exception
	 * @see #updateClob( String, String, String, String )
	 * @see #updateClob( String, String, File, String )
	 * @see #getClob( String )
	 */
	public long getClob(String query, final String fileName) throws Exception {

		FileOutputStream out = null;
		ResultSet rs = null;
		Statement stmt = null;
		int bytesRead = 0;
		long readBytes = 0;
		Reader in = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				profiler.start(query);

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {

				in = rs.getCharacterStream(1);

				if (in == null) {
					logger.debug9(".. ResultSet returns NULL value.");
					return readBytes;
				}

				if ((bytesRead = in.read()) != -1) { // hat CLOB-Feld 0 bytes?
					out = new FileOutputStream(fileName);
					out.write(bytesRead);
					readBytes++;
				}
				else {
					logger.debug9(".. CLOB column has 0 bytes.");
					return readBytes;
				}

				while ((bytesRead = in.read()) != -1) {
					out.write(bytesRead);
					readBytes++;
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				profiler.stop("ERROR", e.getMessage());
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			try {
				out.flush();
			}
			catch (Exception e) {
			}
			if (out != null)
				try {
					out.close();
				}
				catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (in != null)
				try {
					in.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return readBytes;
	}

	/**
	 * updates a clob from a file
	 *
	 * @param tableName
	 *            taget table
	 * @param columnName
	 *            target column
	 * @param file
	 *            source of the clob data
	 * @param condition
	 *            condition(s) (excluding WHERE) of the SQL statement
	 * @return number of bytes written
	 * @throws java.lang.Exception
	 * @see #updateClob( String, String, String, String )
	 * @see #getClob( String, String )
	 * @see #getClob( String )
	 */
	public long updateClob(final String tableName, final String columnName, final File file, final String condition) throws Exception {

		PreparedStatement pstmt = null;
		StringBuffer query = null;
		long totalBytesWritten = 0;
		String theQuery = null;
		Reader in = null;

		try {

			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			if (tableName == null)
				throw new NullPointerException("tableName is null.");
			if (columnName == null)
				throw new NullPointerException("columnName is null.");
			if (!file.exists())
				throw new Exception("file doesn't exist.");

			query = new StringBuffer("UPDATE ");
			if (tableNameUpperCase) {
				query.append(tableName.toUpperCase());
				query.append(" SET \"");
			}
			else {
				query.append(tableName);
				query.append(" SET ");
			}
			if (fieldNameUpperCase) {
				query.append(columnName.toUpperCase());
				query.append("\" = ? ");
			}
			else {
				query.append(columnName);
				query.append(" = ? ");
			}
			if (condition != null) {
				query.append(" WHERE ");
				query.append(condition);
			}

			theQuery = this.normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);

			if (profiler != null)
				try {
					profiler.start(theQuery);
				}
				catch (Exception e) {
				}

			pstmt = connection.prepareStatement(theQuery);
			totalBytesWritten = file.length();
			in = new FileReader(file);
			// pstmt.setCharacterStream(1, new FileReader(file), (int)
			// totalBytesWritten);
			pstmt.setCharacterStream(1, in, (int) totalBytesWritten);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (pstmt != null)
				try {
					pstmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
			if (in != null)
				try {
					in.close();
				}
				catch (Exception e) {
				}

		}
		return totalBytesWritten;
	}

	/**
	 * updates a blob from a file
	 *
	 * @param tableName
	 *            taget table
	 * @param columnName
	 *            target column
	 * @param file
	 *            source of the blob data
	 * @param condition
	 *            condition(s) (excluding WHERE) of the SQL statement
	 * @return number of bytes written
	 * @throws java.lang.Exception
	 */
	public long updateBlob(final String tableName, final String columnName, final String fileName, final String condition) throws Exception {

		PreparedStatement pstmt = null;
		long totalBytesRead = 0;
		InputStream in = null;
		StringBuffer query = null;
		String theQuery = null;

		try {

			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			if (SOSString.isEmpty(tableName))
				throw new NullPointerException("tableName is null.");
			if (SOSString.isEmpty(columnName))
				throw new NullPointerException("columnName is null.");
			if (SOSString.isEmpty(fileName))
				throw new Exception("fileName is null.");

			File file = new File(fileName);
			if (!file.exists())
				throw new Exception(SOSClassUtil.getMethodName() + ": file doesn't exist.");

			in = new FileInputStream(file);
			query = new StringBuffer("UPDATE ");

			if (tableNameUpperCase)
				query.append(tableName.toUpperCase());
			else
				query.append(tableName);

			if (fieldNameUpperCase) {
				query.append(" SET \"");
				query.append(columnName.toUpperCase());
				query.append("\" = ? ");
			}
			else {
				query.append(" SET ");
				query.append(columnName);
				query.append(" = ? ");
			}

			if (!SOSString.isEmpty(condition)) {
				query.append(" WHERE ");
				query.append(condition);
			}

			theQuery = normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);

			if (profiler != null)
				profiler.start(theQuery);

			pstmt = connection.prepareStatement(theQuery);
			totalBytesRead = file.length();
			logger.debug9(".. length: " + totalBytesRead);
			pstmt.setBinaryStream(1, in, (int) totalBytesRead);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			if (connection != null)
				connection.rollback();
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (pstmt != null)
				try {
					pstmt.close();
				}
				catch (Exception e) {
				}
			if (in != null)
				try {
					in.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return totalBytesRead;

	}

	/**
	 * writes the contents of a clob to a file
	 *
	 * @param query
	 *            SQL query
	 * @param filename the file to write
	 * @return number of bytes written
	 * @throws java.lang.Exception
	 * @see #updateBlob( String, String, byte[], String)
	 * @see #updateBlob( String, String, String, String)
	 * @see #getBlob( String )
	 */
	public long getBlob(String query, final String fileName) throws Exception {

		InputStream in = null;
		FileOutputStream out = null;
		ResultSet rs = null;
		Statement stmt = null;
		long readBytes = 0;
		int len = 0;

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				in = rs.getBinaryStream(1);
				if (in == null) {
					return readBytes;
				}

				byte[] buff = new byte[1024];
				if ((len = in.read(buff)) > 0) { // hat BLOB-Feld 0 byte??
					out = new FileOutputStream(fileName);
					out.write(buff, 0, len);
					readBytes += len;
				}
				else {
					logger.debug9(".. BLOB column has 0 bytes.");
					return readBytes;
				}

				while (0 < (len = in.read(buff))) {
					out.write(buff, 0, len);
					readBytes += len;
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (out != null)
				try {
					out.close();
				}
				catch (Exception e) {
				}
			if (in != null)
				try {
					in.close();
				}
				catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return readBytes;
	}

	/**
	 * returns the content of a blob as byte array
	 *
	 * @param query
	 *            SQL query
	 * @throws java.lang.Exception
	 * @see #getBlob( String, String )
	 * @see #updateBlob( String, String, byte[], String )
	 * @see #updateBlob( String, String, String, String )
	 */
	public byte[] getBlob(String query) throws Exception {

		ResultSet rs = null;
		Statement stmt = null;
		byte[] result = {};

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}

			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next())
				result = rs.getBytes(1);
			if (result == null) {
				logger.debug9(".. BLOB column has 0 bytes.");
				return result;
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (stmt != null)
				try {
					stmt.close();
				}
				catch (Exception e) {
				}
			if (rs != null)
				try {
					rs.close();
				}
				catch (Exception e) {
				}
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		return result;
	}

	/**
	 * executes a statement and keeps the ResultSet for retrieval with get()
	 * @param query SQL query
	 * @throws java.lang.Exception
	 * @see #execute( String )
	 * @see #get()
	 */
	public void executeQuery(final String query) throws Exception {
		this.executeQuery(query, -1, -1, -1);
	}

	/**
	 * executes a statement and keeps the ResultSet for retrieval with get()
	 * @param query SQL query
	 * @param resultSetType	 result set type
	 * @param resultSetConcurrency result set concurrency
	 * @throws Exception
	 * @see #execute( String )
	 * @see #get()
	 */
	public void executeQuery(final String query, final int resultSetType, final int resultSetConcurrency) throws Exception {
		this.executeQuery(query, resultSetType, resultSetConcurrency, -1);
	}

	/**
	 * executes a statement and keeps the ResultSet for retrieval with get()
	 * @param query SQL query
	 * @param resultSetType	 result set type
	 * @param resultSetConcurrency result set concurrency
	 * @param resultSetHoldability result set holdability
	 * @throws Exception
	 * @see #execute( String )
	 * @see #get()
	 */
	public void executeQuery(String query, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws Exception {

		try {

			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				try {
					profiler.start(query);
				}
				catch (Exception e) {
				}
			if (statement != null)
				statement.close();

			if (resultSetType > 0 && resultSetConcurrency > 0) {
				if (resultSetHoldability > 0) {
					statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
				}
				else {
					statement = connection.createStatement(resultSetType, resultSetConcurrency);
				}
			}
			else {
				statement = connection.createStatement();
			}

			resultSet = statement.executeQuery(query);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");

			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}

		}
		catch (Exception e) {
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}

	}

	/**
	 * closes a query and the ResultSet (for calls of executeQuery() and get())
	 *
	 * @throws java.lang.Exception
	 * @see #executeQuery( String )
	 * @see #get()
	 */
	public void closeQuery() throws Exception {

		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (resultSet != null) {
				resultSet.close();
			}

		}
		catch (Exception e) {
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}

	}

	/**
	 * Executes an INSERT, UPDATE or DELETE statement
	 *
	 * @param query SQL query
	 * @return number of changed row for (INSERT, UPDATE oder DELETE)
	 *         statements, else 0
	 * @throws java.lang.Exception
	 *
	 */
	public int executeUpdate(String query) throws Exception {
		int rowCount = 0;
		Statement stmt = null;
		try {

			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);

			if (profiler != null)
				profiler.start(query);
			stmt = connection.createStatement();
			rowCount = stmt.executeUpdate(query);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
			try {
				if (profiler != null)
					try {
						profiler.stop("", "");
					}
					catch (Exception e) {
					}
			}
			catch (Exception e) {
			}

		}
		catch (Exception e) {
			try {
				if (profiler != null)
					try {
						profiler.stop("ERROR", e.getMessage());
					}
					catch (Exception ex) {
					}
			}
			catch (Exception ex) {
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			try {
				if (profiler != null)
					try {
						profiler.stop("", "");
					}
					catch (Exception e) {
					}
			}
			catch (Exception e) {
			}
		}
		return rowCount;

	}

	/**
	 * executes a statement
	 * @param query SQL query
	 * @throws java.lang.Exception
	 * @see #executeQuery( String )
	 * @see #get()
	 */
	public void execute(String query) throws Exception {

		Statement stmt = null;
		try {

			logger.debug9("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null)
				profiler.start(query);
			stmt = connection.createStatement();
			stmt.execute(query);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
			if (profiler != null)
				try {
					profiler.stop("", "");
				}
				catch (Exception e) {
				}
		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.toString(), e);
		}
		finally {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}
	}

	/**
	 * commit transaction
	 *
	 * @throws Exception
	 */
	public void commit() throws Exception {

		logger.debug9("calling " + SOSClassUtil.getMethodName());

		if (connection == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");

		connection.commit();

		logger.debug6(SOSClassUtil.getMethodName() + " successfully executed.");

	}

	/**
	 * rollback transaction
	 *
	 * @throws Exception
	 */
	public void rollback() throws Exception {

		logger.debug9("calling " + SOSClassUtil.getMethodName());

		if (connection == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");

		connection.rollback();

		logger.debug6(SOSClassUtil.getMethodName() + " successfully executed.");

	}

	/**
	 * sets autocommit
	 *
	 * @param autoCommit
	 * @throws Exception
	 */
	public void setAutoCommit(final boolean autoCommit) throws Exception {

		if (connection == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");
		connection.setAutoCommit(autoCommit);
	}

	/**
	 * @return boolean the current AutoCommit-status
	 * @throws Exception
	 */
	public boolean getAutoCommit() throws Exception {

		if (connection == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");

		return connection.getAutoCommit();
	}

	/**
	 * Reads the next row of a resultset. This requires the ResultSet to be
	 * created with executeQuery().
	 * @return HashMap on success, else null
	 * @throws java.lang.Exception
	 * @see #executeQuery(String)
	 * @see #execute(String)
	 */
	public HashMap<String, String> get() throws Exception {

		HashMap<String, String> record = new LinkedHashMap<String, String>();
		String columnName, columnValue;
		try {
			//erzeugt zu viele Log einträge:
			//logger.debug6("Calling " + SOSClassUtil.getMethodName());
			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			if (profiler != null)
				try {
					profiler.start("");
				}
				catch (Exception e) {
				}
			if (resultSet.next()) {
				ResultSetMetaData objMeta = resultSet.getMetaData();
				long lngNoOfRecordsToProcess = objMeta.getColumnCount();
				if (lngMaxNoOfRecordsToProcess > 0) {
					lngNoOfRecordsToProcess = lngMaxNoOfRecordsToProcess;
				}
				for (int i = 1; i <= lngNoOfRecordsToProcess; i++) {
					columnName = objMeta.getColumnName(i);
					columnValue = resultSet.getString(columnName);
					if (SOSString.isEmpty(columnValue)) {
						columnValue = "";
					}
					String strV = columnName;
					if (flgColumnNamesCaseSensitivity == false) {
						strV = normalizeKey(columnName);
					}
					record.put(strV, columnValue.trim());
				}
			}

		}
		catch (Exception e) {
			if (profiler != null)
				try {
					profiler.stop("ERROR", e.getMessage());
				}
				catch (Exception ex) {
				}
			throw e;
		}
		if (profiler != null)
			try {
				profiler.stop("", "");
			}
			catch (Exception e) {
			}
		return record;
	}

	/**
	 * returns the number of rows of the current ResultSet
	 *
	 * @return int number of rows of the current ResultSet
	 * @throws Exception
	 * @see #execute( String )
	 */
	public int fieldCount() throws Exception {

		if (connection == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");

		return resultSet.getMetaData().getColumnCount();
	}

	/**
	 * returns the column names of the current ResultSet
	 *
	 * @return String[] of column names
	 * @throws java.lang.Exception
	 * @see #execute( String )
	 */
	public String[] fieldNames() throws Exception {

		int fieldCount = this.fieldCount();
		String[] fieldNames = new String[fieldCount];
		try {
			logger.debug6("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			for (int i = 0; i < fieldCount; i++) {
				fieldNames[i] = normalizeKey(resultSet.getMetaData().getColumnName(i + 1));
			}
			Arrays.sort(fieldNames);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			throw e;
		}
		return fieldNames;
	}

	/**
	 * returns the column name for the given index of the ResultSet
	 *
	 * @param index
	 *            column index
	 * @return String column name
	 * @throws java.lang.Exception
	 * @see #execute( String )
	 */
	public String fieldName(final int index) throws Exception {

		if (connection == null)
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");

		return resultSet.getMetaData().getColumnName(index);
	}

	/**
	 * returns the field description of the column for the given index of the ResultSet
	 *
	 * @param index
	 *            column index
	 * @return HashMap field description
	 * @throws java.lang.Exception
	 * @see #execute( String )
	 */
	public HashMap fieldDesc(final int index) throws Exception {

		HashMap fieldDesc = new HashMap();
		try {

			logger.debug6("calling " + SOSClassUtil.getMethodName());

			if (connection == null)
				throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
						+ " may be the connect method was not called");

			fieldDesc.put("columnDisplaySize", String.valueOf(resultSet.getMetaData().getColumnDisplaySize(index)));
			fieldDesc.put("columnLabel", resultSet.getMetaData().getColumnLabel(index));
			fieldDesc.put("columnName", resultSet.getMetaData().getColumnName(index));
			fieldDesc.put("columnType", String.valueOf(resultSet.getMetaData().getColumnType(index)));
			fieldDesc.put("columnTypeName", resultSet.getMetaData().getColumnTypeName(index));
			fieldDesc.put("precision", String.valueOf(resultSet.getMetaData().getPrecision(index)));
			fieldDesc.put("scale", String.valueOf(resultSet.getMetaData().getScale(index)));
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		}
		catch (Exception e) {
			throw e;
		}
		return fieldDesc;
	}

	/**
	 * returns a normalized SQL statement. The meta-functions
	 * %lcase, %ucase, %now, %updlock, %timestamp, %timestamp_iso are translated
	 * to the SQL-Syntax of the current database
	 *
	 * @param inputString SQL Query
	 * @return String normalized SQL statement
	 * @throws java.lang.Exception
	 */
	public String normalizeStatement(final String inputString) throws Exception {

		return normalizeStatement(inputString, getReplacement());
	}

	/**
	 * returns a normalized SQL statement. The meta-functions
	 * %lcase, %ucase, %now, %updlock, %timestamp, %timestamp_iso are translated
	 * to the SQL-Syntax of the current database
	 *
	 * @param replacement
	 * @param inputString SQL Query
	 * @return String normalized SQL statement
	 * @throws java.lang.Exception
	 */
	public String normalizeStatement(final String inputString, final String[] replacement) throws Exception {

		logger.debug9("Calling " + SOSClassUtil.getMethodName());

		logger.debug9("..inputString=" + inputString);

		// (?s) to match newlines with .*
		// skip processing if no group matches!!!
		if (!inputString.matches("(?s).*(%lcase|%ucase|%now|%updlock|%cast|%timestamp).*")) {
			return inputString;
		}

		if (replacement.length < 4)
			return inputString;

		StringBuffer aPattern, buffer;
		Pattern pattern;
		Matcher matcher;
		String replaceString;

		aPattern = new StringBuffer("(%lcase|%ucase|%now|%updlock)|").append("(%timestamp\\('[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{1,4}'\\))|")
				.append("(%timestamp\\('[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{1,4}[ \t]?[0-9]{2}:[0-9]{2}(?::[0-9]{2}\\.?[0-9]*)?'\\))|")
				.append("(%timestamp_iso\\('[0-9]{1,4}[-]?[0-9]{1,2}[-]?[0-9]{1,2}'\\))|")
				.append("(%timestamp_iso\\('[0-9]{1,4}[-]?[0-9]{1,2}[-]?[0-9]{1,2}[ \t]?[0-9]{2}:[0-9]{2}(?::[0-9]{2}\\.?[0-9]*)?'\\))");

		pattern = Pattern.compile(aPattern.toString());
		matcher = pattern.matcher(inputString);

		buffer = new StringBuffer();

		while (matcher.find()) {
			replaceString = matcher.group();

			if (matcher.group(1) != null) {
				replaceString = replaceString.replaceAll("%lcase", replacement[0]);
				replaceString = replaceString.replaceAll("%ucase", replacement[1]);
				replaceString = replaceString.replaceAll("%now", replacement[2]);
				replaceString = replaceString.replaceAll("%updlock", replacement[3]);
			}

			if (matcher.group(2) != null || matcher.group(3) != null)
				replaceString = this.toDate(this.toTimestamp(replaceString, NLS_DE));

			if (matcher.group(4) != null || matcher.group(5) != null) {
				replaceString = this.toDate(this.toTimestamp(replaceString, NLS_ISO));
			}

			matcher.appendReplacement(buffer, replaceString);
		}
		matcher.appendTail(buffer);
		logger.debug9(SOSClassUtil.getMethodName() + ", result [" + buffer.toString() + "]");

		if (inputString.matches("(.*%cast.*)")) { // need casting??
			return replaceCasts(buffer.toString());
		}

		return buffer.toString();
	}

	/**
	 * connects to the database
	 *
	 * @throws java.lang.Exception
	 *
	 */
	abstract public void connect() throws Exception;

	/**
	 * returns timestamp string for the current database
	 *
	 * @param dateString
	 * @return oracle
	 * @throws java.lang.Exception
	 */
	abstract public String toDate(String dateString) throws Exception;

	/**
	 * returns the most recent value of a sequence (generator). May
	 * also be used to get the latest value of an auto-increment field.
	 *
	 * @param sequence name of the sequence (ingnored for MySQL)
	 * @return latest value of the sequence
	 * @throws Exception
	 */
	public String getLastSequenceValue(final String sequence) throws Exception {
		String value = getSingleValue(getLastSequenceQuery(sequence));
		return value;
	}

	/**
	 * returns the String to query the most recent value of a sequence
	 *
	 * @param sequence name of the sequence (ingnored for MySQL)
	 * @return query string
	 */
	protected abstract String getLastSequenceQuery(String sequence);

	/**
	 * closes all open resources
	 *
	 * @throws Exception
	 */
	public void disconnect() throws Exception {

		if (statement != null) {
			statement.close();
			statement = null;
		}
		if (resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
		if (connection != null) {
			connection.rollback();
			connection.close();
			connection = null;
			try {
				logger.debug6(SOSClassUtil.getMethodName() + ": successfully disconnected.");
			}
			catch (Exception e) {
			}
		}
	}

	/**
	 * Destructor
	 *
	 * @throws Throwable
	 */
	@Override
	protected void finalize() throws Throwable {

		try {
			this.disconnect();
		}
		catch (Exception e) {
		}
		super.finalize();
	}

	/**
	 * returns a timestamp
	 *
	 * @param dateString
	 * @param nlsFormat
	 * @return String Timestamp
	 * @throws java.lang.Exception
	 */
	public String toTimestamp(String dateString, final String nlsFormat) throws Exception {
		Pattern p;
		String[] dateTime;
		String[] datePart = null;
		String[] timePart = { "00", "00", "00" };
		String[] timePart2 = {};
		StringBuffer result = new StringBuffer();
		String datePatternSeparator;
		String year = "";
		String month = "";
		String day = "";

		if (nlsFormat.equalsIgnoreCase(SOSConnection.NLS_DE))
			datePatternSeparator = "[.]";
		else
			if (nlsFormat.equalsIgnoreCase(SOSConnection.NLS_ISO)) {
				datePatternSeparator = "[-]";
			}
			else
				throw new Exception("invalid nlsFormat.");

		dateString = dateString.substring(dateString.indexOf(39) + 1, dateString.lastIndexOf(39));
		p = Pattern.compile("[ ]");

		dateTime = p.split(dateString);
		p = Pattern.compile(datePatternSeparator);
		datePart = p.split(dateTime[0]);
		if (dateTime.length > 1) {
			p = Pattern.compile("[:]");
			timePart2 = p.split(dateTime[1]);
		}

		for (int i = 0; i < timePart2.length; i++) {
			timePart[i] = timePart2[i];
		}

		if (nlsFormat.equalsIgnoreCase(SOSConnection.NLS_DE)) {
			year = datePart[2];
			month = datePart[1];
			day = datePart[0];
		}
		else
			if (nlsFormat.equalsIgnoreCase(SOSConnection.NLS_ISO)) {
				year = datePart[0];
				month = datePart[1];
				day = datePart[2];
			}

		switch (year.length()) {
			case 1:
				year = "200" + year;
				break;

			case 2:
				year = "20" + year;
				break;

			case 3:
				year = "2" + year;
				break;
		}

		result.append(year).append("-").append(month).append("-").append(day);

		result.append(" ").append(timePart[0]).append(":").append(timePart[1]).append(":").append(timePart[2]);

		return result.toString();
	}

	/**
	 * returns the input string in the selected case
	 *
	 * @param key
	 * @return String
	 * @throws java.lang.Exception
	 * @see #setKeysToLowerCase
	 * @see #setKeysToUpperCase
	 */
	protected String normalizeKey(final String key) throws Exception {

		if (SOSString.isEmpty(key))
			throw new Exception(getClass() + ":" + SOSClassUtil.getMethodName() + ": invalid key.");
		if (lowerCase)
			return key.toLowerCase();
		else
			return key.toUpperCase();
	}

	/**
	 * activates lower case for fieldnames
	 *
	 * @see #setKeysToUpperCase
	 */
	public void setKeysToLowerCase() throws Exception {

		logger.debug6("calling " + SOSClassUtil.getMethodName());
		logger.debug9(".. now keys set to lower case.");
		lowerCase = true;
	}

	/**
	 * activates upper case for fieldnames
	 *
	 * @see #setKeysToLowerCase
	 */
	public void setKeysToUpperCase() throws Exception {

		logger.debug6("calling " + SOSClassUtil.getMethodName());
		logger.debug9(".. now keys set to upper case.");
		lowerCase = false;
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
	abstract protected boolean prepareGetStatements(StringBuffer contentSB, StringBuffer splitSB, StringBuffer endSB) throws Exception;

	/**
	 * Extract multiple statements from a String.<br>
	 * Statements need to be separated by ;\n<br>
	 * Existing single- or multi-line comments are removed from
	 * the statement
	 *
	 * @param contentOfClobAttribute
	 *            String containing the SQL Statements
	 * @return statements ArrayList with one listitem per statement
	 * @throws Exception
	 */

	public ArrayList getStatements(String contentOfClobAttribute) throws Exception {

		if (contentOfClobAttribute == null || contentOfClobAttribute.length() == 0) {
			throw new Exception(SOSClassUtil.getMethodName() + ": contentOfClobAttribute is empty");
		}

		logger.debug6(SOSClassUtil.getMethodName() + ": contentOfClobAttribute = " + contentOfClobAttribute);

		contentOfClobAttribute = contentOfClobAttribute.replaceAll("\r\n", "\n");

		//contentOfClobAttribute = contentOfClobAttribute.replaceAll(
		//        "[/][*](?s).*?[*][/]", "");

		ArrayList statements = new ArrayList();

		StringBuffer splitSB = new StringBuffer();
		StringBuffer endSB = new StringBuffer();
		StringBuffer contentSB = new StringBuffer(contentOfClobAttribute);

		boolean enableProcedureSearch = true;
		// endSB wird nicht immer bei PL/SQL Syntax hinzugefügt
		boolean alwaysAddEndSB = this.prepareGetStatements(contentSB, splitSB, endSB);

		if (contentSB.toString().toLowerCase().contains("execreturnsresultset")) {
			currentStatementsExecReturnsResultset = true;
			logger.debug6(SOSClassUtil.getMethodName() + " : found EXECRETURNSRESULTSET");
		}
		contentOfClobAttribute = this.stripOuterComments(contentSB.toString());

		String[] commands = contentOfClobAttribute.replaceAll("\\;[ \\t]", ";").split(splitSB.toString());

		for (String command : commands) {

			if (command == null || command.trim().length() == 0) {
				continue;
			}

			String statement = command.trim();

			if (enableProcedureSearch) {
				// eigentlich nicht richtig - kann END [Prozedurenname]
				// vorkommen
				// Es handelt sich um ein PL/SQL Block
				if (statement.toLowerCase().endsWith("end") || statement.toLowerCase().endsWith("end;")) {
					//Prï¿½fen ob mit PL/SQL anfï¿½ngt
					if (this.isProcedureSyntax(statement)) {
						// PL/SQL Block
						statements.add(statement + endSB.toString());
						logger.debug6(SOSClassUtil.getMethodName() + " : statement =" + statement + endSB.toString());
					}
					else {
						//kein PL/SQL Anfang
						String s = statement.replaceAll(replaceBackslash, replacementBackslash);
						s = s.replaceAll(replaceDoubleApostrophe, replacementDoubleApostrophe);

						this.splitStatements(statements, new StringBuffer(s.trim()), null, endSB.toString(), true, 0);

						if (!beginProcedure.equals("")) {
							int posPLSQL = statement.indexOf(beginProcedure);
							String substatement = statement.substring(posPLSQL);

							statements.add(substatement + endSB.toString());
							logger.debug6(SOSClassUtil.getMethodName() + " : statement =" + substatement + endSB.toString());

						}
					}

				} // if endWith(END)
				else {
					// entweder SQL oder Pl/SQL zB drop procedure
					String s = statement.replaceAll(replaceBackslash, replacementBackslash);
					s = s.replaceAll(replaceDoubleApostrophe, replacementDoubleApostrophe);

					String end = alwaysAddEndSB ? endSB.toString() : "";

					this.splitStatements(statements, new StringBuffer(s.trim()), null, end, false, 0);

				}
			}// enable PL/SQLsearch
			else {
				String s = statement.replaceAll(replaceBackslash, replacementBackslash);
				s = s.replaceAll(replaceDoubleApostrophe, replacementDoubleApostrophe);

				this.splitStatements(statements, new StringBuffer(s.trim()), null, endSB.toString(), false, 0);
			}
		}

		return statements;
	}

	/**
	 * @param statements
	 * @param stmt
	 * @param position
	 * @param procedurEnd
	 * @param returnProcedureBegin
	 * @throws Exception
	 */
	private void splitStatements(final ArrayList statements, final StringBuffer st, final Integer position, final String procedurEnd,
			final boolean returnProcedureBegin, int count) throws Exception {

		//System.gc();

		//System.out.println("count : "+count);

		beginProcedure = "";

		count += 1;

		StringBuffer sub;

		int semicolon = -1;
		int apostrophe_first = -1;

		if (position == null) {
			semicolon = st.indexOf(";");
			apostrophe_first = st.indexOf("'");
		}
		else {
			semicolon = st.indexOf(";", position.intValue());
			apostrophe_first = st.indexOf("'", position.intValue());
		}

		// Hochkommas hinter dem ; oder gar nicht gefunden
		if (apostrophe_first > semicolon || apostrophe_first == -1) {

			String value = "";

			if (semicolon == -1) {
				value = st.toString().trim();
			}
			else {
				value = st.toString().substring(0, semicolon).trim();
			}

			value = value.replaceAll(replacementBackslash, replaceBackslash);
			value = value.replaceAll(replacementDoubleApostrophe, replaceDoubleApostrophe);

			if (this.isProcedureSyntax(value)) {

				if (returnProcedureBegin) {
					beginProcedure = value;
					return;
				}
				else {
					// hier nur die einfache Proceduren
					// create, drop usw
					if (!procedurEnd.equals("")) {
						value += procedurEnd;
					}
				}
			}

			if (!value.equals("")) {
				statements.add(value);
				logger.debug6(SOSClassUtil.getMethodName() + ": statement = " + value);
			}

			if (semicolon != -1) {
				sub = new StringBuffer(st.substring(semicolon + 1));

				if (sub != null && sub.length() != 0) {
					this.splitStatements(statements, sub, null, procedurEnd, returnProcedureBegin, count);
				}
			}

		}
		else {
			// Hochkommas gefunden
			int apostrophe_second = st.indexOf("'", apostrophe_first + 1);
			// egal ob Hochkomma vor ; oder danach
			if (apostrophe_second != -1) {
				this.splitStatements(statements, st, new Integer(apostrophe_second + 1), procedurEnd, returnProcedureBegin, count);
			}
			else {
				throw new Exception("Schliessende Hochkomma nicht gefunden !!!!!!!!!!!!!!!! = " + apostrophe_first + " = " + st);
			}
		}
	}

	/**
	 * @param statement
	 * @return @throws
	 *         Exception
	 */
	private boolean isProcedureSyntax(String statement) throws Exception {

		if (statement == null) {
			throw new Exception("statement is empty");
		}

		statement = statement.toLowerCase().trim();

		if (statement.startsWith("procedure") || statement.startsWith("function") || statement.startsWith("declare") || statement.startsWith("begin")) {
			return true;
		}

		StringBuffer patterns = new StringBuffer();
		patterns.append("^(re)?create+[\\s]*procedure");
		patterns.append("|^create+[\\s]*function");
		patterns.append("|^create+[\\s]*operator");
		patterns.append("|^create+[\\s]*package");
		patterns.append("|^create+[\\s]*trigger");
		patterns.append("|^drop+[\\s]*function");
		patterns.append("|^drop+[\\s]*operator");
		patterns.append("|^drop+[\\s]*package");
		patterns.append("|^drop+[\\s]*procedure");
		patterns.append("|^drop+[\\s]*trigger");
		patterns.append("|^create+[\\s]*or+[\\s]*replace+[\\s]*procedure");
		patterns.append("|^create+[\\s]*or+[\\s]*replace+[\\s]*function");
		patterns.append("|^create+[\\s]*or+[\\s]*replace+[\\s]*package");
		patterns.append("|^create+[\\s]*or+[\\s]*replace+[\\s]*operator");
		patterns.append("|^create+[\\s]*or+[\\s]*replace+[\\s]*trigger");

		Pattern p = Pattern.compile(patterns.toString());
		Matcher matcher = p.matcher(statement);
		if (matcher.find()) {
			return true;
		}

		return false;
	}

	/**
	 * removes comments which don't belong to a statement, e.g.
	 * single-line comments with ##,-- or multi-line comments
	 *
	 * @param statement SQL statement
	 * @return string without comments
	 * @throws Exception
	 */
	private String stripOuterComments(final String statement) throws Exception {
		if (statement == null) {
			throw new Exception("statement is empty");
		}

		StringBuffer sb = new StringBuffer("");

		StringTokenizer st = new StringTokenizer(statement, "\n");

		boolean addRow = true;
		boolean isVersionComment = false;
		while (st.hasMoreTokens()) {
			String row = st.nextToken().trim();
			if (row == null || row.length() == 0)
				continue;

			if (row.startsWith("--") || row.startsWith("//") || row.startsWith("#")) {
				continue;
			}
			// wir entfernen nur die Kommentare, die am Zeilebeginn stehen()
			row = row.replaceAll("^[/][*](?s).*?[*][/][\\s]*;*", "");

			// Das machen wir nicht , weil kann in value vorkommen
			//if(row.indexOf("//") != -1) {
			//    row = row.substring(0, row.indexOf("//"));
			//}

			if (row.length() == 0)
				continue;

			if (row.startsWith("/*!")) {
				// Beispielkommentar: /*!32300 ... sql code ... */ entspricht Version 3.23.00
				String[] contentArr = row.substring(3).trim().split(" ");
				if (contentArr[0].length() == 5 || contentArr[0].length() == 6) {
					String version = contentArr[0].length() == 5 ? "0" + contentArr[0] : contentArr[0];
					try {
						int major = Integer.parseInt(version.substring(0, 2));
						if (this.getMajorVersion() >= major) {
							logger.debug9("using sql comment : db major version = " + this.getMajorVersion() + " comment major version = " + major + " ");

							int minor = Integer.parseInt(version.substring(2, 4));
							if (this.getMinorVersion() >= minor) {
								isVersionComment = true;
								logger.debug9("using sql comment : db minor version = " + this.getMinorVersion() + " comment minor version = " + minor + " ");
							}
							else {
								logger.debug9("skip sql comment : db minor version = " + this.getMinorVersion() + " comment minor version = " + minor + " ");
							}
						}
						else {
							logger.debug9("skip sql comment : db major version = " + this.getMajorVersion() + " comment major version = " + major + " ");
						}
					}
					catch (Exception e) {
						logger.warn("skip sql comment : no numerical major/minor version in comment = " + version + " database major version = "
								+ this.getMajorVersion() + " minor version = " + this.getMinorVersion());
					}
				}
				else {
					logger.warn("skip sql comment : invalid comment major version length = " + contentArr[0] + " db major version = " + this.getMajorVersion());
				}

				if (isVersionComment == false) {
					addRow = false;
				}
				continue;
			}
			else
				if (row.startsWith("/*")) {
					addRow = false;
					continue;
				}
			//Verschachtelter Syntax wird bei der Kommentaren nicht
			// unterschtï¿½tzt,
			//daswegen ruhig endWith, sonst kï¿½nnte */ innerhalb von /* */
			// vorkommen
			if (row.endsWith("*/") || row.endsWith("*/;")) {
				if (isVersionComment == true) {
					if (addRow == false) {
						addRow = true;
					}
					else {
						isVersionComment = false;
					}
					continue;
				}
				if (addRow == false) {
					addRow = true;
					continue;
				}
			}

			if (addRow == false) {
				continue;
			}

			sb.append(row + "\n");
		}

		return sb.toString();
	}

	private boolean	flgColumnNamesCaseSensitivity	= false;
	private long lngMaxNoOfRecordsToProcess = -1;
	
	public void setMaxNoOfRecordsToProcess (final long plngMaxNoOfRecordsToProcess) {
		lngMaxNoOfRecordsToProcess = plngMaxNoOfRecordsToProcess;
	}
	public void setColumnNamesCaseSensitivity(final boolean pflgColumnNamesCaseSensitivity) {

		flgColumnNamesCaseSensitivity = pflgColumnNamesCaseSensitivity;
	}

	/**
	 * extract statements from a string and execute these
	 *
	 * @param contentOfClobAttribute
	 *            string containing sql statements
	 * @throws Exception
	 */
	public void executeStatements(final String contentOfClobAttribute) throws Exception {
		currentStatementsExecReturnsResultset = this.isExecReturnsResultSet();
		ArrayList statements = null;

		logger.debug6(SOSClassUtil.getMethodName());

		/*
		logger.debug6(SOSClassUtil.getMethodName()
		        + " : contentOfClobAttribute = " + contentOfClobAttribute);
		*/
		try {
			statements = this.getStatements(contentOfClobAttribute);

			if (statements == null || statements.size() == 0) {
				if (logger != null) {
					logger.info("no sql statements found");
				}
			}
			else {
				boolean hasOpenResultSet = false;

				for (int i = 0; i < statements.size(); i++) {

					String statement = statements.get(i).toString().trim();

					if (statement.toLowerCase().startsWith("select") || statement.toLowerCase().startsWith("exec") && currentStatementsExecReturnsResultset) {
						if (hasOpenResultSet) {
							this.closeQuery();
							hasOpenResultSet = false;
						}
						this.executeQuery(statement);
						hasOpenResultSet = true;
					}
					else {
						this.executeUpdate(statement);
					}
				}
			}

		}
		catch (Exception e) {
			throw new Exception(SOSClassUtil.getMethodName() + " : " + e, e);
		}
		finally {
			currentStatementsExecReturnsResultset = this.isExecReturnsResultSet();
		}
	}

	/**
	 * set the profiler. If the profiler is null, then the profiler is not
	 * activated
	 *
	 * @param profiler_
	 */
	public void setProfiler(final SOSProfiler profiler_) {
		profiler = profiler_;
	}

	/**
	 * sets field name synthax to upper case
	 *
	 * @param value upper case, if true (default)
	 */
	public void setFieldNameToUpperCase(final boolean value) {
		fieldNameUpperCase = value;
	}

	/**
	 * sets table name synthax to upper case
	 *
	 * @param value upper case, if true (default)
	 */
	public void setTableNameToUpperCase(final boolean value) {
		tableNameUpperCase = value;
	}

	/**
	 * returns the resultset of a query with execute()
	 *
	 * @return Returns the resultSet.
	 * @see #execute(String)
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * @return Returns the url of the database
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * returns output of the database sertver (not the ResultSet)
	 *
	 * @return Vector of Strings for each line of the output
	 */
	public Vector getOutput() throws Exception {
		Vector out = new Vector();
		return out;
	}

	/**
	 * current date and time as GregorianCalendar
	 *
	 * @return
	 */
	public GregorianCalendar getTimestamp() throws Exception {

		return this.getDateTime("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * current date as GregorianCalendar
	 *
	 * @return
	 */
	public GregorianCalendar getDate() throws Exception {

		return this.getDateTime("yyyy-MM-dd");
	}

	/**
	 * returns database time as GregorianCalender
	 *
	 * @param format
	 * @return
	 */
	protected GregorianCalendar getDateTime(final String format) throws Exception {
		GregorianCalendar gc = new GregorianCalendar();

		return gc;
	}

	/**
	 * Implements cast functions for each database.<br>
	 * The function is expected to return the query but replace occurences of<br>
	 * (\\s*%cast\\s*)*\\s*(\\()*\\s*(\\s*%cast\\s*)+\\s*(\\(\\s*\\S+\\s*(\\S+?).*?)\\)(\\s*(\\+|\\-)*[0-9]*\\s*\\S*(\\)))*<br>
	 * using the types <code>varchar, character, integer</code>
	 * with the cast syntax and datatypes of the current database, e.g. <br>
	 * <code>%cast("VALUE" integer)</code><br>
	 * will be transformed into<br>
	 * <code>CAST("VALUE"  AS SIGNED)</code><br>
	 * for MySQL Databases
	 * @param inputString query string
	 * @return
	 */
	abstract protected String replaceCasts(String inputString) throws Exception;

	/**
	 * parses the Major version from the database version string returned
	 * by the jdbc driver
	 * @param productVersion description of the database version as return by jdbc
	 * @return version number before the "."
	 */
	public int parseMajorVersion(final String productVersion) throws Exception {
		String dbVersion = productVersion.replaceAll("[^0-9\\.]", "");
		String[] split = dbVersion.split("\\.");
		if (split.length < 2)
			throw new Exception("Failed to parse major Version from String \"" + productVersion + "\"");
		int major = Integer.parseInt(split[0]);
		return major;
	}

	/**
	 * parses the Minor version from the database version string returned
	 * by the jdbc driver
	 * @param productVersion description of the database version as return by jdbc
	 * @return version number after the "."
	 */
	public int parseMinorVersion(final String productVersion) throws Exception {
		String dbVersion = productVersion.replaceAll("[^0-9\\.]", "");
		String[] split = dbVersion.split("\\.");
		if (split.length < 2)
			throw new Exception("Failed to parse minor Version from String \"" + productVersion + "\"");
		int minor = Integer.parseInt(split[1]);
		return minor;
	}

	/**
	 * @return Returns the compatibility.
	 */
	public int getCompatibility() {
		return compatibility;
	}

	/**
	 * sets the compatibility level for the database version check
	 * @param compatibility one of the following values of SOSConnectionVersionLimiter:<br/>
	 * CHECK_OFF - Only logging on INFO level<br/>
	 * CHECK_NORMAL - connect cannot be performed for unsupported versions<br/>
	 * CHECK_STRICT - connect cannot be performed for unsupported and untested versions<br/>
	 */
	public void setCompatibility(final int compatibility) {
		this.compatibility = compatibility;
	}

	public static int getCompatibility(final String compatibility) {
		if (compatibility.equalsIgnoreCase("normal"))
			return SOSConnectionVersionLimiter.CHECK_NORMAL;
		if (compatibility.equalsIgnoreCase("strict"))
			return SOSConnectionVersionLimiter.CHECK_STRICT;
		if (compatibility.equalsIgnoreCase("off"))
			return SOSConnectionVersionLimiter.CHECK_OFF;
		return SOSConnectionVersionLimiter.CHECK_OFF;
	}

	private static Class getClass(String name) throws Exception {
		if (name.indexOf('.') == -1) { // no package specified, append sos.connection
			name = "sos.connection." + name;
		}
		Class connectionClass = Class.forName(name);
		return connectionClass;
	}

	private static SOSConnection createInstance(final String className, final Object[] arguments) throws Exception {
		try {
			Class connectionClass = getClass(className);
			Constructor[] constr = connectionClass.getConstructors();
			Class[] parameterTypes = new Class[arguments.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				if (arguments[i] instanceof SOSLogger) {
					parameterTypes[i] = SOSLogger.class;
				}
				else
					if (arguments[i] instanceof Connection) {
						parameterTypes[i] = Connection.class;
					}
					else {
						parameterTypes[i] = arguments[i].getClass();
					}
			}
			Constructor connectionConstructor = connectionClass.getConstructor(parameterTypes);
			SOSConnection conn = (SOSConnection) connectionConstructor.newInstance(arguments);
			return conn;
		}
		catch (Exception e) {
			throw new Exception("Error occured creating connection object for class " + className + ": " + e);
		}
	}

	/**
	 * SOSConnection provides functions to encapsulate the syntax to do
	 * standard tasks in different SQL dialects. These functions are <br>
	 * <ul>
	 *   <li>%lcase - convert String to lower case</li>
	 *   <li>%ucase - convert String to upper case</li>
	 *   <li>%now - get the current database time</li>
	 *   <li>%updlock - lock the result of a select statement for update</li>
	 * </ul>
	 * @return a String array that consists of the function names of the
	 * given functions in the dialect of the current database. E.G. for MySQL:<br>
	 * { "LCASE", "UCASE", "NOW()", "FOR UPDATE"}
	 */
	public abstract String[] getReplacement();

	/**
	 * implement default behavior after connect or
	 * after a connection is returned by a connection pool
	 * (e.g. set transaction isolation level...)
	 */
	public abstract void prepare(Connection connection) throws Exception;

	public int getMajorVersion() {
		return majorVersion;
	}

	protected void setMajorVersion(final int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	protected void setMinorVersion(final int minorVersion) {
		this.minorVersion = minorVersion;
	}

	public String getProductVersion() {
		return productVersion;
	}

	protected void setProductVersion(final String productVersion) {
		this.productVersion = productVersion;
	}

	/**
	 * \brief getexecReturnsResultSet
	 *
	 * \details
	 * getter 
	 *
	 * @return the execReturnsResultSet
	 */
	public boolean isExecReturnsResultSet() {
		return execReturnsResultSet;
	}

	/**
	 * \brief setexecReturnsResultSet - if stored procedures return a result set, this needs to be set to true in order to run the stored procedure as a query
	 *
	 * \details
	 * setter 
	 *
	 * @param execReturnsResultSet the value for execReturnsResultSet to set
	 */
	public void setExecReturnsResultSet(final boolean execReturnsResultSet) {
		this.execReturnsResultSet = execReturnsResultSet;
	}

}
