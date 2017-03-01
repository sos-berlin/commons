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
import java.sql.SQLException;
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
import sos.xml.SOSXMLXPath;

/**
 * <p>
 * Description: abstract class for database connection
 * </p>
 * <p>
 * Note that the following entries should be available in the
 * <i>sos_settings.ini</i> file
 * <p>
 * class=&lt;connection class name&gt; : the connection type.<br>
 * <b>Example</b> for Oracle "class=SOSOracleConnection"
 * <p>
 * driver=&lt;name of jdbc driver&gt;<br>
 * <b>Examples:</b><br>
 * <ul>
 * <li>Oracle oracle.jdbc.driver.OracleDriver</li>
 * <li>MSSQL: com.microsoft.jdbc.sqlserver.SQLServerDriver</li>
 * <li>MySQL: org.gjt.mm.mysql.Driver</li>
 * <li>PgSQL: org.postgresql.Driver</li>
 * <li>FbSQL: org.firebirdsql.jdbc.FBDriver</li>
 * <li>DB2: com.ibm.db2.jcc.DB2Driver</li>
 * </ul>
 * <p>
 * url=&lt;url of the Database Server&gt;<br>
 * <b>Examples:</b><br>
 * <ul>
 * <li>Oracle: url=jdbc:oracle:thin:@&lt;host&gt;:1521:&lt;SID&gt;</li>
 * <li>MSSQL:
 * url=jdbc:microsoft:sqlserver://&lt;host&gt;:1433;databaseName=&lt;database
 * &gt;</li>
 * <li>MySQL: url=jdbc:mysql://&lt;host&gt;/&lt;database&gt;</li>
 * <li>PgSQL: url=jdbc:postgresql://&lt;host&gt;/&lt;database&gt;</li>
 * <li>FbSQL: url=jdbc:firebirdsql://&lt;host&gt;/&lt;database&gt;</li>
 * <li>DB2:
 * url=jdbc:db2://&lt;host&gt;:&lt;port&gt;/&lt;database&gt;:driverType=
 * 2;retrieveMessagesFromServerOnGetMessage=true;</li>
 * </ul>
 * <p>
 * user=&lt;username&gt;
 * <p>
 * password=&lt;password&gt;
 *
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SOS GmbH
 * </p>
 * 
 * @author Ghassan Beydoun
 **/
public abstract class SOSConnection {

	protected static final String NLS_DE = "DE";
	protected static final String NLS_ISO = "ISO";
	protected static final String CAST_PATTERN = "(\\s*%cast\\s*)*\\s*(\\()*\\s*(\\s*%cast\\s*)+\\s*(\\(\\s*\\S+\\s*(\\S+?).*?)\\)"
			+ "(\\s*(\\+|\\-)*[0-9]*\\s*\\S*(\\)))*";
	protected Properties configFileProperties = new Properties();
	protected Statement statement;
	protected ResultSet resultSet;
	protected boolean lowerCase = true;
	protected int compatibility = SOSConnectionVersionLimiter.CHECK_OFF;
	protected boolean fieldNameUpperCase = true;
	protected boolean tableNameUpperCase = true;
	protected String driver;
	protected String url;
	protected String dbuser;
	protected String dbpassword;
	protected String dbname;
	protected SOSLogger logger = null;
	private boolean execReturnsResultSet = false;
	private static final String EXEC_COMMENT_RETURN_RESULTSET = "EXECRETURNSRESULTSET";
	private static final String REPLACE_BACKSLASH = "\\\\'";
	private static final String REPLACEMENT_BACKSLASH = "XxxxX";
	private static final String REPLACE_DOUBLE_APOSTROPHE = "''";
	private static final String REPLACEMENT_DOUBLE_APOSTROPHE = "YyyyY";
	private String beginProcedure = "";
	private int majorVersion = -1;
	private int minorVersion = 0;
	private String productVersion = "";
	private boolean useExecuteBatch = false;
	private int batchSize = 100;
	private boolean flgColumnNamesCaseSensitivity = false;
	private long lngMaxNoOfRecordsToProcess = -1;
	public Connection connection;
	public static SOSProfiler profiler = null;

	public SOSConnection() throws Exception {
		//
	}

	public SOSConnection(final Connection connection, final SOSLogger logger) throws Exception {
		this.connection = connection;
		this.logger = logger;
	}

	public SOSConnection(final Connection connection) throws Exception {
		this(connection, new SOSStandardLogger(new NullBufferedWriter(new OutputStreamWriter(System.out)),
				SOSStandardLogger.DEBUG9));
	}

	public SOSConnection(final String configFileName, final SOSLogger logger) throws Exception {
		logger.debug6("calling " + SOSClassUtil.getMethodName());
		logger.debug9(".. configFileName: " + configFileName);

		if (logger == null) {
			throw new Exception("missing logger.");
		}
		File file = new File(configFileName);
		if (!file.exists()) {
			throw new Exception("make sure that the file [" + configFileName + "] does exist!");
		}

		FileInputStream fs = null;
		InputStream in = null;
		try {
			fs = new FileInputStream(configFileName);
			in = new BufferedInputStream(fs);

			if (file.getName().toLowerCase().endsWith(".xml")) {
				SOSXMLXPath xpath = new SOSXMLXPath(in);
				String dialect = getHibernateConfigurationValue(xpath, "hibernate.dialect");
				configFileProperties = new Properties();
				configFileProperties.setProperty("[configuration]", "");
				configFileProperties.setProperty("class", getClassNameByHibernateDialect(dialect));
				configFileProperties.setProperty("driver",getHibernateConfigurationValue(xpath, "hibernate.connection.driver_class"));
				configFileProperties.setProperty("url",	getHibernateConfigurationValue(xpath, "hibernate.connection.url"));
				configFileProperties.setProperty("user",getHibernateConfigurationValue(xpath, "hibernate.connection.username"));
				configFileProperties.setProperty("password",getHibernateConfigurationValue(xpath, "hibernate.connection.password"));
				if (configFileProperties.getProperty("class").equals(SOSMSSQLConnection.class.getSimpleName())) {
					configFileProperties.setProperty("compatibility", "normal");
				} else {
					configFileProperties.setProperty("compatibility", "");
				}
			} else {
				configFileProperties.load(in);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (Exception ex) {
				}
			}
		}
		if (SOSString.isEmpty(configFileProperties.getProperty("driver"))) {
			throw new Exception("driver missing.");
		}
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
		String sCompatibility = configFileProperties.getProperty("compatibility");
		if (!SOSString.isEmpty(sCompatibility)) {
			setCompatibility(getCompatibility(sCompatibility));
		}
		this.logger = logger;

		processPassword();	
	}

	public SOSConnection(final String driver, final String url, final String dbuser, final String dbpassword,
			final SOSLogger logger) throws Exception {
		if (logger == null) {
			throw new Exception(SOSClassUtil.getMethodName() + ": missing logger.");
		}
		this.logger = logger;
		logger.debug6("calling " + SOSClassUtil.getMethodName());
		if (SOSString.isEmpty(driver)) {
			throw new Exception(SOSClassUtil.getMethodName() + ": missing database driver.");
		}
		this.driver = driver;
		this.url = url;
		this.dbuser = dbuser;
		this.dbpassword = dbpassword;
		processPassword();
		logger.debug9(".. driver=" + driver + ", url=" + url + ", dbuser=" + dbuser);
	}

	public SOSConnection(final String driver, final String url, final String dbuser, final String dbpassword,
			final SOSLogger logger, final int compatibility) throws Exception {
		this(driver, url, dbuser, dbpassword, logger);
		setCompatibility(compatibility);
	}

	public SOSConnection(final String configFileName) throws Exception {
		this(configFileName, new SOSStandardLogger(new NullBufferedWriter(new OutputStreamWriter(System.out)),
				SOSStandardLogger.DEBUG9));
	}

	public SOSConnection(final String driver, final String url, final String dbuser, final String dbpassword)
			throws Exception {
		this(driver, url, dbuser, dbpassword, new SOSStandardLogger(
				new NullBufferedWriter(new OutputStreamWriter(System.out)), SOSStandardLogger.DEBUG9));
	}

	private void processPassword() {
		dbpassword = SOSCommandline.getExternalPassword(dbpassword, logger);
	}

	public static String getClassName(final String configFileName, final SOSLogger logger) throws Exception {
		if (logger != null) {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			logger.debug9(".. configuration file: " + configFileName);
		}
		File file = new File(configFileName);
		if (!file.exists()) {
			throw new Exception("configuration file not found: " + configFileName);
		}

		Properties config = new Properties();
		FileInputStream fs = null;
		InputStream in = null;
		try {
			fs = new FileInputStream(configFileName);
			in = new BufferedInputStream(fs);
			if (file.getName().toLowerCase().endsWith(".xml")) {
				SOSXMLXPath xpath = new SOSXMLXPath(in);
				String dialect = getHibernateConfigurationValue(xpath, "hibernate.dialect");
				config.put("class", SOSString.isEmpty(dialect) ? "" : getClassNameByHibernateDialect(dialect));
			} else {
				config.load(in);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
				}
			}
			if (fs != null) {
				try {
					fs.close();
				} catch (Exception ex) {
				}
			}
		}
		if (SOSString.isEmpty(config.getProperty("class"))) {
			throw new Exception("class missing.");
		}
		return config.getProperty("class").trim();
	}

	private static String getHibernateConfigurationValue(SOSXMLXPath xpath, String name) throws Exception {
		return xpath.selectSingleNodeValue("/hibernate-configuration/session-factory/property[@name='" + name + "']");
	}

	private static String getClassNameByHibernateDialect(String dialectClassName) throws Exception {
		if (SOSString.isEmpty(dialectClassName)) {
			throw new Exception("dialectClassName is NULL");
		}
		dialectClassName = dialectClassName.toLowerCase();
		if (dialectClassName.contains("db2")) {
			return SOSDB2Connection.class.getSimpleName();
		} else if (dialectClassName.contains("firebird")) {
			return SOSFbSQLConnection.class.getSimpleName();
		} else if (dialectClassName.contains("sqlserver")) {
			return SOSMSSQLConnection.class.getSimpleName();
		} else if (dialectClassName.contains("mysql")) {
			return SOSMySQLConnection.class.getSimpleName();
		} else if (dialectClassName.contains("oracle")) {
			return SOSOracleConnection.class.getSimpleName();
		} else if (dialectClassName.contains("postgre")) {
			return SOSPgSQLConnection.class.getSimpleName();
		} else if (dialectClassName.contains("sybase")) {
			return SOSSybaseConnection.class.getSimpleName();
		} else {
			return "unknown";
		}
	}

	public static SOSConnection createInstance(final String configFileName, final SOSLogger logger) throws Exception {
		logger.debug6("calling " + SOSClassUtil.getMethodName());
		String className = SOSConnection.getClassName(configFileName, logger);
		logger.debug9(".. creating instance for: " + className);
		Object[] arguments = { configFileName, logger };
		return createInstance(className, arguments);
	}
	public static SOSConnection createInstance(final String className, final Connection connection,
			final SOSLogger logger) throws Exception {
		logger.debug6("calling " + SOSClassUtil.getMethodName());
		Object[] arguments = { connection, logger };
		return createInstance(className, arguments);
	}

	public static SOSConnection createInstance(final String className, final Connection connection) throws Exception {
		Object[] arguments = { connection };
		return createInstance(className, arguments);
	}

	public static SOSConnection createInstance(final String configFileName) throws Exception {
		String className = SOSConnection.getClassName(configFileName, null);
		Object[] arguments = { configFileName };
		return createInstance(className, arguments);
	}

	public static SOSConnection createInstance(final String className, final String driver, final String url,
			final String dbuser, final String dbpassword) throws Exception {
		Object[] arguments = { driver, url, dbuser, dbpassword };
		return createInstance(className, arguments);
	}

	public static SOSConnection createInstance(final String className, final String driver, final String url,
			final String dbuser, final String dbpassword, final SOSLogger logger) throws Exception {
		Object[] arguments = { driver, url, dbuser, dbpassword, logger };
		return createInstance(className, arguments);
	}

	public Connection getConnection() throws Exception {
		if (connection == null) {
			throw new Exception(
					SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established.");
		}
		return connection;
	}

	public HashMap getSingle(String query) throws Exception {
		HashMap results = new LinkedHashMap();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		int columnCount = 0;
		String key = null;
		String value = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			try {
				if (profiler != null) {
					profiler.start(query);
				}
			} catch (Exception e) {
				//
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(i);
					if (SOSString.isEmpty(value)) {
						value = "";
					}
					results.put(normalizeKey(key), value.trim());
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return results;
	}

	public Properties getSingleAsProperties(String query) throws Exception {
		Properties results = new Properties();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		int columnCount = 0;
		String key, value;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();
			if (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(i);
					if (SOSString.isEmpty(value)) {
						value = "";
					}
					logger.debug9(SOSClassUtil.getMethodName() + ", key= " + key + ", value= " + value);
					results.setProperty(normalizeKey(key), value.trim());
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return results;
	}

	public Properties getArrayAsProperties(String query) throws Exception {
		Properties results = new Properties();
		Statement stmt = null;
		ResultSet rs = null;
		ResultSetMetaData meta = null;
		int columnCount = 0;
		String key, value;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();
			while (rs.next()) {
				if (columnCount > 1) {
					key = rs.getString(1);
					value = rs.getString(2);
					if (SOSString.isEmpty(value)) {
						value = "";
					}
					logger.debug9(SOSClassUtil.getMethodName() + ", key= " + key + ", value= " + value);
					results.setProperty(normalizeKey(key), value.trim());
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return results;
	}

	public String getSingleValue(String query) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		String result = "";
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				profiler.start(query);
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				result = rs.getString(1);
				if (SOSString.isEmpty(result)) {
					result = "";
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + ".. successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return result.trim();
	}

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
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(meta.getColumnName(i));
					if (SOSString.isEmpty(value)) {
						value = "";
					}
					record.put(normalizeKey(key), value.trim());
				}
				results.add(record);
				record = new HashMap();
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return results;
	}

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
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			meta = rs.getMetaData();
			columnCount = meta.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnCount; i++) {
					key = normalizeKey(meta.getColumnName(i));
					value = rs.getString(meta.getColumnName(i));
					if (SOSString.isEmpty(value)) {
						value = "";
					}
					record.put(normalizeKey(key), value.trim());
				}
				results.add(record);
				record = new HashMap();
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return results;
	}

	public ArrayList getArrayValue(String query) throws Exception {
		ArrayList results = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		String value;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				profiler.start(query);
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				value = rs.getString(1);
				if (!SOSString.isEmpty(value)) {
					results.add(value.trim());
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": query failed: " + query + ": " + e.getMessage(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return results;
	}

	public long updateBlob(final String tableName, final String columnName, final byte[] data, final String condition)
			throws Exception {
		ByteArrayInputStream in = null;
		PreparedStatement pstmt = null;
		StringBuilder query = null;
		String theQuery = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			if (tableName == null) {
				throw new NullPointerException("tableName is null.");
			}
			if (columnName == null) {
				throw new NullPointerException("columnName is null.");
			}
			if (data == null || data.length <= 0) {
				throw new NullPointerException("missing data.");
			}
			in = new ByteArrayInputStream(data);
			query = new StringBuilder();
			query.append("UPDATE ");
			if (tableNameUpperCase) {
				query.append(tableName.toUpperCase());
			} else {
				query.append(tableName);
			}
			if (fieldNameUpperCase) {
				query.append(" SET \"").append(columnName.toUpperCase()).append("\" = ? ");
			} else {
				query.append(" SET ").append(columnName).append(" = ? ");
			}
			if (!SOSString.isEmpty(condition)) {
				query.append(" WHERE ").append(condition);
			}
			theQuery = this.normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);
			if (profiler != null) {
				try {
					profiler.start(theQuery);
				} catch (Exception e) {
					//
				}
			}
			pstmt = connection.prepareStatement(theQuery);
			pstmt.setBinaryStream(1, in, data.length);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					//
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return data.length;
	}

	public long updateClob(final String tableName, final String columnName, final String data, final String condition)
			throws Exception {
		PreparedStatement pstmt = null;
		StringBuilder query = null;
		long totalBytesWritten = 0;
		String theQuery = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			if (SOSString.isEmpty(tableName)) {
				throw new NullPointerException("tableName is null.");
			}
			if (SOSString.isEmpty(columnName)) {
				throw new NullPointerException("columnName is null.");
			}
			if (SOSString.isEmpty(data)) {
				throw new NullPointerException("missing data.");
			}
			query = new StringBuilder();
			query.append("UPDATE ");
			if (tableNameUpperCase) {
				query.append(tableName.toUpperCase());
			} else {
				query.append(tableName);
			}
			if (fieldNameUpperCase) {
				query.append(" SET \"").append(columnName.toUpperCase()).append("\" = ? ");
			} else {
				query.append(" SET ").append(columnName).append(" = ? ");
			}
			if (!SOSString.isEmpty(condition)) {
				query.append(" WHERE ").append(condition);
			}
			theQuery = this.normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);
			if (profiler != null) {
				try {
					profiler.start(theQuery);
				} catch (Exception e) {
					//
				}
			}
			pstmt = connection.prepareStatement(theQuery);
			totalBytesWritten = data.length();
			pstmt.setCharacterStream(1, new java.io.StringReader(data), (int) totalBytesWritten);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				profiler.stop("ERROR", e.getMessage());
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": " + e.getMessage(), e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return totalBytesWritten;
	}

	public String getClob(String query) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		Reader in = null;
		int bytesRead;
		StringBuilder sb = new StringBuilder();
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " Maybe the connect method was not called.");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			stmt = connection.createStatement();
			try {
				rs = stmt.executeQuery(query);
			} catch (Exception e) {
				logger.debug6(".. query failed: " + query + ": " + e.getMessage());
				throw new Exception(SOSClassUtil.getMethodName() + ": " + e.getMessage());
			}
			if (rs.next()) {
				in = rs.getCharacterStream(1);
				if (in == null) {
					logger.debug6(".. ResultSet returns NULL value.");
					return "";
				}
				if ((bytesRead = in.read()) != -1) {
					sb.append((char) bytesRead);
				} else {
					logger.debug6(".. CLOB column has 0 bytes.");
					return "";
				}
				while ((bytesRead = in.read()) != -1) {
					sb.append((char) bytesRead);
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return sb.toString();
	}

	public long getClob(String query, final String fileName) throws Exception {
		FileOutputStream out = null;
		ResultSet rs = null;
		Statement stmt = null;
		int bytesRead = 0;
		long readBytes = 0;
		Reader in = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				profiler.start(query);
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				in = rs.getCharacterStream(1);
				if (in == null) {
					logger.debug9(".. ResultSet returns NULL value.");
					return readBytes;
				}
				if ((bytesRead = in.read()) != -1) {
					out = new FileOutputStream(fileName);
					out.write(bytesRead);
					readBytes++;
				} else {
					logger.debug9(".. CLOB column has 0 bytes.");
					return readBytes;
				}
				while ((bytesRead = in.read()) != -1) {
					out.write(bytesRead);
					readBytes++;
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				profiler.stop("ERROR", e.getMessage());
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			try {
				out.flush();
			} catch (Exception e) {
				//
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					//
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return readBytes;
	}

	public long updateClob(final String tableName, final String columnName, final File file, final String condition)
			throws Exception {
		PreparedStatement pstmt = null;
		StringBuilder query = null;
		long totalBytesWritten = 0;
		String theQuery = null;
		Reader in = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			if (tableName == null) {
				throw new NullPointerException("tableName is null.");
			}
			if (columnName == null) {
				throw new NullPointerException("columnName is null.");
			}
			if (!file.exists()) {
				throw new Exception("file doesn't exist.");
			}
			query = new StringBuilder();
			query.append("UPDATE ");
			if (tableNameUpperCase) {
				query.append(tableName.toUpperCase()).append(" SET \"");
			} else {
				query.append(tableName).append(" SET ");
			}
			if (fieldNameUpperCase) {
				query.append(columnName.toUpperCase()).append("\" = ? ");
			} else {
				query.append(columnName).append(" = ? ");
			}
			if (condition != null) {
				query.append(" WHERE ").append(condition);
			}
			theQuery = this.normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);
			if (profiler != null) {
				try {
					profiler.start(theQuery);
				} catch (Exception e) {
					//
				}
			}
			pstmt = connection.prepareStatement(theQuery);
			totalBytesWritten = file.length();
			in = new FileReader(file);
			pstmt.setCharacterStream(1, in, (int) totalBytesWritten);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					//
				}
			}
		}
		return totalBytesWritten;
	}

	public long updateBlob(final String tableName, final String columnName, final String fileName,
			final String condition) throws Exception {
		PreparedStatement pstmt = null;
		long totalBytesRead = 0;
		InputStream in = null;
		StringBuilder query = null;
		String theQuery = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			if (SOSString.isEmpty(tableName)) {
				throw new NullPointerException("tableName is null.");
			}
			if (SOSString.isEmpty(columnName)) {
				throw new NullPointerException("columnName is null.");
			}
			if (SOSString.isEmpty(fileName)) {
				throw new Exception("fileName is null.");
			}
			File file = new File(fileName);
			if (!file.exists()) {
				throw new Exception(SOSClassUtil.getMethodName() + ": file doesn't exist.");
			}
			in = new FileInputStream(file);
			query = new StringBuilder();
			query.append("UPDATE ");
			if (tableNameUpperCase) {
				query.append(tableName.toUpperCase());
			} else {
				query.append(tableName);
			}
			if (fieldNameUpperCase) {
				query.append(" SET \"").append(columnName.toUpperCase()).append("\" = ? ");
			} else {
				query.append(" SET ").append(columnName).append(" = ? ");
			}
			if (!SOSString.isEmpty(condition)) {
				query.append(" WHERE ").append(condition);
			}
			theQuery = normalizeStatement(query.toString(), getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + theQuery);
			if (profiler != null) {
				profiler.start(theQuery);
			}
			pstmt = connection.prepareStatement(theQuery);
			totalBytesRead = file.length();
			logger.debug9(".. length: " + totalBytesRead);
			pstmt.setBinaryStream(1, in, (int) totalBytesRead);
			pstmt.executeUpdate();
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			if (connection != null) {
				connection.rollback();
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return totalBytesRead;
	}

	public long getBlob(String query, final String fileName) throws Exception {
		InputStream in = null;
		FileOutputStream out = null;
		ResultSet rs = null;
		Statement stmt = null;
		long readBytes = 0;
		int len = 0;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " Maybe the connect method was not called.");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				in = rs.getBinaryStream(1);
				if (in == null) {
					return readBytes;
				}
				byte[] buff = new byte[1024];
				if ((len = in.read(buff)) > 0) {
					out = new FileOutputStream(fileName);
					out.write(buff, 0, len);
					readBytes += len;
				} else {
					logger.debug9(".. BLOB column has 0 bytes.");
					return readBytes;
				}
				while (0 < (len = in.read(buff))) {
					out.write(buff, 0, len);
					readBytes += len;
				}
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ": " + e.getMessage(), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					//
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					//
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return readBytes;
	}

	public byte[] getBlob(String query) throws Exception {
		ResultSet rs = null;
		Statement stmt = null;
		byte[] result = {};
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				result = rs.getBytes(1);
			}
			if (result == null) {
				logger.debug9(".. BLOB column has 0 bytes.");
				return result;
			}
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					//
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					//
				}
			}
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		}
		return result;
	}

	public void executeQuery(final String query) throws Exception {
		this.executeQuery(query, -1, -1, -1);
	}

	public void executeQuery(final String query, final int resultSetType, final int resultSetConcurrency)
			throws Exception {
		this.executeQuery(query, resultSetType, resultSetConcurrency, -1);
	}

	public void executeQuery(String query, final int resultSetType, final int resultSetConcurrency,
			final int resultSetHoldability) throws Exception {
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " Maybe the connect method was not called.");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				try {
					profiler.start(query);
				} catch (Exception e) {
					//
				}
			}
			if (statement != null) {
				statement.close();
			}
			if (resultSetType > 0 && resultSetConcurrency > 0) {
				if (resultSetHoldability > 0) {
					statement = connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
				} else {
					statement = connection.createStatement(resultSetType, resultSetConcurrency);
				}
			} else {
				statement = connection.createStatement();
			}
			resultSet = statement.executeQuery(query);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		} catch (Exception e) {
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		}
	}

	public void closeQuery() throws Exception {
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (Exception e) {
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		}
	}

	public int executeUpdate(String query) throws Exception {
		int rowCount = 0;
		Statement stmt = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				profiler.start(query);
			}
			stmt = connection.createStatement();
			rowCount = stmt.executeUpdate(query);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
			try {
				if (profiler != null) {
					try {
						profiler.stop("", "");
					} catch (Exception e) {
						//
					}
				}
			} catch (Exception e) {
				//
			}
		} catch (Exception e) {
			try {
				if (profiler != null) {
					try {
						profiler.stop("ERROR", e.getMessage());
					} catch (Exception ex) {
						//
					}
				}
			} catch (Exception ex) {
				//
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			try {
				if (profiler != null) {
					try {
						profiler.stop("", "");
					} catch (Exception e) {
						//
					}
				}
			} catch (Exception e) {
				//
			}
		}
		return rowCount;
	}

	public void execute(String query) throws Exception {
		Statement stmt = null;
		try {
			logger.debug9("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			query = normalizeStatement(query, getReplacement());
			logger.debug6(SOSClassUtil.getMethodName() + ": " + query);
			if (profiler != null) {
				profiler.start(query);
			}
			stmt = connection.createStatement();
			stmt.execute(query);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
			if (profiler != null) {
				try {
					profiler.stop("", "");
				} catch (Exception e) {
					//
				}
			}
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw new Exception(SOSClassUtil.getMethodName() + ":" + e.getMessage(), e);
		} finally {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}
	}

	public void commit() throws Exception {
		logger.debug9("calling " + SOSClassUtil.getMethodName());
		if (connection == null) {
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");
		}
		connection.commit();
		logger.debug6(SOSClassUtil.getMethodName() + " successfully executed.");
	}

	public void rollback() throws Exception {
		logger.debug9("calling " + SOSClassUtil.getMethodName());
		if (connection == null) {
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");
		}
		connection.rollback();
		logger.debug6(SOSClassUtil.getMethodName() + " successfully executed.");
	}

	public void setAutoCommit(final boolean autoCommit) throws Exception {
		if (connection == null) {
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");
		}
		connection.setAutoCommit(autoCommit);
	}

	public boolean getAutoCommit() throws Exception {
		if (connection == null) {
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");
		}
		return connection.getAutoCommit();
	}

	public HashMap<String, String> get() throws Exception {
		HashMap<String, String> record = new LinkedHashMap<String, String>();
		String columnName, columnValue;
		try {
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			if (profiler != null) {
				try {
					profiler.start("");
				} catch (Exception e) {
					//
				}
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
					if (!flgColumnNamesCaseSensitivity) {
						strV = normalizeKey(columnName);
					}
					record.put(strV, columnValue.trim());
				}
			}
		} catch (Exception e) {
			if (profiler != null) {
				try {
					profiler.stop("ERROR", e.getMessage());
				} catch (Exception ex) {
					//
				}
			}
			throw e;
		}
		if (profiler != null) {
			try {
				profiler.stop("", "");
			} catch (Exception e) {
				//
			}
		}
		return record;
	}

	public int fieldCount() throws Exception {
		if (connection == null) {
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");
		}
		return resultSet.getMetaData().getColumnCount();
	}

	public String[] fieldNames() throws Exception {
		int fieldCount = this.fieldCount();
		String[] fieldNames = new String[fieldCount];
		try {
			logger.debug6("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			for (int i = 0; i < fieldCount; i++) {
				fieldNames[i] = normalizeKey(resultSet.getMetaData().getColumnName(i + 1));
			}
			Arrays.sort(fieldNames);
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			throw e;
		}
		return fieldNames;
	}

	public String fieldName(final int index) throws Exception {
		if (connection == null) {
			throw new Exception(SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
					+ " may be the connect method was not called");
		}
		return resultSet.getMetaData().getColumnName(index);
	}

	public HashMap fieldDesc(final int index) throws Exception {
		HashMap fieldDesc = new HashMap();
		try {
			logger.debug6("calling " + SOSClassUtil.getMethodName());
			if (connection == null) {
				throw new Exception(
						SOSClassUtil.getMethodName() + ": sorry, there is no successful connection established."
								+ " may be the connect method was not called");
			}
			fieldDesc.put("columnDisplaySize", String.valueOf(resultSet.getMetaData().getColumnDisplaySize(index)));
			fieldDesc.put("columnLabel", resultSet.getMetaData().getColumnLabel(index));
			fieldDesc.put("columnName", resultSet.getMetaData().getColumnName(index));
			fieldDesc.put("columnType", String.valueOf(resultSet.getMetaData().getColumnType(index)));
			fieldDesc.put("columnTypeName", resultSet.getMetaData().getColumnTypeName(index));
			fieldDesc.put("precision", String.valueOf(resultSet.getMetaData().getPrecision(index)));
			fieldDesc.put("scale", String.valueOf(resultSet.getMetaData().getScale(index)));
			logger.debug9(SOSClassUtil.getMethodName() + " successfully executed.");
		} catch (Exception e) {
			throw e;
		}
		return fieldDesc;
	}

	public String normalizeStatement(final String inputString) throws Exception {
		return normalizeStatement(inputString, getReplacement());
	}

	public String normalizeStatement(final String inputString, final String[] replacement) throws Exception {
		logger.debug9("Calling " + SOSClassUtil.getMethodName());
		logger.debug9("..inputString=" + inputString);
		if (!inputString.matches("(?s).*(%lcase|%ucase|%now|%updlock|%cast|%timestamp).*")) {
			return inputString;
		}
		if (replacement.length < 4) {
			return inputString;
		}
		StringBuilder aPattern;
		StringBuffer buffer;
		Pattern pattern;
		Matcher matcher;
		String replaceString;
		aPattern = new StringBuilder();
		aPattern.append("(%lcase|%ucase|%now|%updlock)|")
				.append("(%timestamp\\('[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{1,4}'\\))|")
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
			if (matcher.group(2) != null || matcher.group(3) != null) {
				replaceString = this.toDate(this.toTimestamp(replaceString, NLS_DE));
			}
			if (matcher.group(4) != null || matcher.group(5) != null) {
				replaceString = this.toDate(this.toTimestamp(replaceString, NLS_ISO));
			}
			matcher.appendReplacement(buffer, replaceString);
		}
		matcher.appendTail(buffer);
		logger.debug9(SOSClassUtil.getMethodName() + ", result [" + buffer.toString() + "]");
		if (inputString.matches("(.*%cast.*)")) {
			return replaceCasts(buffer.toString());
		}
		return buffer.toString();
	}

	public abstract void connect() throws Exception;

	public abstract String toDate(String dateString) throws Exception;

	public String getLastSequenceValue(final String sequence) throws Exception {
		return getSingleValue(getLastSequenceQuery(sequence));
	}

	protected abstract String getLastSequenceQuery(String sequence);

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
			} catch (Exception e) {
				//
			}
		}
	}

	public String toTimestamp(String dateString, final String nlsFormat) throws Exception {
		Pattern p;
		String[] dateTime;
		String[] datePart = null;
		String[] timePart = { "00", "00", "00" };
		String[] timePart2 = {};
		StringBuilder result = new StringBuilder();
		String datePatternSeparator;
		String year = "";
		String month = "";
		String day = "";
		if (nlsFormat.equalsIgnoreCase(SOSConnection.NLS_DE)) {
			datePatternSeparator = "[.]";
		} else if (nlsFormat.equalsIgnoreCase(SOSConnection.NLS_ISO)) {
			datePatternSeparator = "[-]";
		} else {
			throw new Exception("invalid nlsFormat.");
		}
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
		} else if (nlsFormat.equalsIgnoreCase(SOSConnection.NLS_ISO)) {
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

	protected String normalizeKey(final String key) throws Exception {
		if (SOSString.isEmpty(key)) {
			throw new Exception(getClass() + ":" + SOSClassUtil.getMethodName() + ": invalid key.");
		}
		if (lowerCase) {
			return key.toLowerCase();
		} else {
			return key.toUpperCase();
		}
	}

	public void setKeysToLowerCase() throws Exception {
		logger.debug6("calling " + SOSClassUtil.getMethodName());
		logger.debug9(".. now keys set to lower case.");
		lowerCase = true;
	}

	public void setKeysToUpperCase() throws Exception {
		logger.debug6("calling " + SOSClassUtil.getMethodName());
		logger.debug9(".. now keys set to upper case.");
		lowerCase = false;
	}

	protected abstract boolean prepareGetStatements(StringBuffer contentSB, StringBuffer splitSB, StringBuffer endSB)
			throws Exception;

	public ArrayList<String> getStatements(String contentOfClobAttribute) throws Exception {
		if (contentOfClobAttribute == null || contentOfClobAttribute.isEmpty()) {
			throw new Exception(SOSClassUtil.getMethodName() + ": contentOfClobAttribute is empty");
		}
		logger.debug6(SOSClassUtil.getMethodName() + ": contentOfClobAttribute = " + contentOfClobAttribute);
		contentOfClobAttribute = contentOfClobAttribute.replaceAll("\r\n", "\n");
		ArrayList<String> statements = new ArrayList<String>();
		StringBuffer splitSB = new StringBuffer();
		StringBuffer endSB = new StringBuffer();
		StringBuffer contentSB = new StringBuffer(contentOfClobAttribute);
		boolean enableProcedureSearch = true;
		boolean alwaysAddEndSB = this.prepareGetStatements(contentSB, splitSB, endSB);
		contentOfClobAttribute = this.stripOuterComments(contentSB.toString());
		String[] commands = contentOfClobAttribute.replaceAll("\\;[ \\t]", ";").split(splitSB.toString());
		for (String command : commands) {
			if (command == null || command.trim().isEmpty()) {
				continue;
			}
			String statement = command.trim();
			if (enableProcedureSearch) {
				if (statement.toLowerCase().endsWith("end") || statement.toLowerCase().endsWith("end;")) {
					if (this.isProcedureSyntax(statement)) {
						statements.add(statement + endSB.toString());
						logger.debug6(SOSClassUtil.getMethodName() + " : statement =" + statement + endSB.toString());
					} else {
						String s = statement.replaceAll(REPLACE_BACKSLASH, REPLACEMENT_BACKSLASH);
						s = s.replaceAll(REPLACE_DOUBLE_APOSTROPHE, REPLACEMENT_DOUBLE_APOSTROPHE);
						this.splitStatements(statements, new StringBuffer(s.trim()), null, endSB.toString(), true, 0);
						if (!"".equals(beginProcedure)) {
							int posPLSQL = statement.indexOf(beginProcedure);
							String substatement = statement.substring(posPLSQL);
							statements.add(substatement + endSB.toString());
							logger.debug6(
									SOSClassUtil.getMethodName() + " : statement =" + substatement + endSB.toString());
						}
					}
				} else {
					String s = statement.replaceAll(REPLACE_BACKSLASH, REPLACEMENT_BACKSLASH);
					s = s.replaceAll(REPLACE_DOUBLE_APOSTROPHE, REPLACEMENT_DOUBLE_APOSTROPHE);
					String end = alwaysAddEndSB ? endSB.toString() : "";
					this.splitStatements(statements, new StringBuffer(s.trim()), null, end, false, 0);
				}
			} else {
				String s = statement.replaceAll(REPLACE_BACKSLASH, REPLACEMENT_BACKSLASH);
				s = s.replaceAll(REPLACE_DOUBLE_APOSTROPHE, REPLACEMENT_DOUBLE_APOSTROPHE);
				this.splitStatements(statements, new StringBuffer(s.trim()), null, endSB.toString(), false, 0);
			}
		}
		return statements;
	}

	private void splitStatements(final ArrayList statements, final StringBuffer st, final Integer position,
			final String procedurEnd, final boolean returnProcedureBegin, int count) throws Exception {
		beginProcedure = "";
		count += 1;
		StringBuffer sub;
		int semicolon = -1;
		int apostropheFirst = -1;
		if (position == null) {
			semicolon = st.indexOf(";");
			apostropheFirst = st.indexOf("'");
		} else {
			semicolon = st.indexOf(";", position.intValue());
			apostropheFirst = st.indexOf("'", position.intValue());
		}
		if (apostropheFirst > semicolon || apostropheFirst == -1) {
			String value = "";
			if (semicolon == -1) {
				value = st.toString().trim();
			} else {
				value = st.toString().substring(0, semicolon).trim();
			}
			value = value.replaceAll(REPLACEMENT_BACKSLASH, REPLACE_BACKSLASH);
			value = value.replaceAll(REPLACEMENT_DOUBLE_APOSTROPHE, REPLACE_DOUBLE_APOSTROPHE);
			if (this.isProcedureSyntax(value)) {
				if (returnProcedureBegin) {
					beginProcedure = value;
					return;
				} else if (!"".equals(procedurEnd)) {
					value += procedurEnd;
				}
			}
			if (!"".equals(value)) {
				statements.add(value);
				logger.debug6(SOSClassUtil.getMethodName() + ": statement = " + value);
			}
			if (semicolon != -1) {
				sub = new StringBuffer(st.substring(semicolon + 1));
				if (sub != null && sub.length() != 0) {
					this.splitStatements(statements, sub, null, procedurEnd, returnProcedureBegin, count);
				}
			}
		} else {
			int apostropheSecond = st.indexOf("'", apostropheFirst + 1);
			if (apostropheSecond != -1) {
				this.splitStatements(statements, st, new Integer(apostropheSecond + 1), procedurEnd,
						returnProcedureBegin, count);
			} else {
				throw new Exception(
						"Schliessende Hochkomma nicht gefunden !!!!!!!!!!!!!!!! = " + apostropheFirst + " = " + st);
			}
		}
	}

	private boolean isProcedureSyntax(String statement) throws Exception {
		if (statement == null) {
			throw new Exception("statement is empty");
		}
		statement = statement.toLowerCase().trim();
		if (statement.startsWith("procedure") || statement.startsWith("function") || statement.startsWith("declare")
				|| statement.startsWith("begin")) {
			return true;
		}
		StringBuilder patterns = new StringBuilder();
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

	private String stripOuterComments(final String statement) throws Exception {
		if (statement == null) {
			throw new Exception("statement is empty");
		}
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(statement, "\n");
		boolean addRow = true;
		boolean isVersionComment = false;
		while (st.hasMoreTokens()) {
			String row = st.nextToken().trim();
			if (row == null || row.isEmpty()) {
				continue;
			}
			if (row.startsWith("--") || row.startsWith("//") || row.startsWith("#")) {
				continue;
			}
			row = row.replaceAll("^[/][*](?s).*?[*][/][\\s]*;*", "");
			if (row.isEmpty()) {
				continue;
			}
			if (row.startsWith("/*!")) {
				String[] contentArr = row.substring(3).trim().split(" ");
				if (contentArr[0].length() == 5 || contentArr[0].length() == 6) {
					String version = contentArr[0].length() == 5 ? "0" + contentArr[0] : contentArr[0];
					try {
						int major = Integer.parseInt(version.substring(0, 2));
						if (this.getMajorVersion() >= major) {
							logger.debug9("using sql comment : db major version = " + this.getMajorVersion()
									+ " comment major version = " + major + " ");
							int minor = Integer.parseInt(version.substring(2, 4));
							if (this.getMinorVersion() >= minor) {
								isVersionComment = true;
								logger.debug9("using sql comment : db minor version = " + this.getMinorVersion()
										+ " comment minor version = " + minor + " ");
							} else {
								logger.debug9("skip sql comment : db minor version = " + this.getMinorVersion()
										+ " comment minor version = " + minor + " ");
							}
						} else {
							logger.debug9("skip sql comment : db major version = " + this.getMajorVersion()
									+ " comment major version = " + major + " ");
						}
					} catch (Exception e) {
						logger.warn("skip sql comment : no numerical major/minor version in comment = " + version
								+ " database major version = " + this.getMajorVersion() + " minor version = "
								+ this.getMinorVersion());
					}
				} else {
					logger.warn("skip sql comment : invalid comment major version length = " + contentArr[0]
							+ " db major version = " + this.getMajorVersion());
				}
				if (!isVersionComment) {
					addRow = false;
				}
				continue;
			} else if (row.startsWith("/*")) {
				addRow = false;
				continue;
			}
			if (row.endsWith("*/") || row.endsWith("*/;")) {
				if (isVersionComment) {
					if (!addRow) {
						addRow = true;
					} else {
						isVersionComment = false;
					}
					continue;
				}
				if (!addRow) {
					addRow = true;
					continue;
				}
			}
			if (!addRow) {
				continue;
			}
			sb.append(row + "\n");
		}
		return sb.toString();
	}

	public void setMaxNoOfRecordsToProcess(final long plngMaxNoOfRecordsToProcess) {
		lngMaxNoOfRecordsToProcess = plngMaxNoOfRecordsToProcess;
	}

	public void setColumnNamesCaseSensitivity(final boolean pflgColumnNamesCaseSensitivity) {
		flgColumnNamesCaseSensitivity = pflgColumnNamesCaseSensitivity;
	}

	public void executeBatch(ArrayList<String> statements) throws Exception {
		final String methodName = SOSClassUtil.getMethodName();
		Statement st = this.connection.createStatement();
		try {
			long count = 0;
			long countAddBatches = 0;
			for (int i = 0; i < statements.size(); i++) {
				String sql = normalizeStatement(statements.get(i).toString().trim());
				String sqlToLower = sql.toLowerCase();
				if ("commit".equals(sqlToLower)) {
					if (countAddBatches > 0) {
						logger.debug3(String.format("%s: call executeBatch - %s batches", methodName, countAddBatches));
						st.executeBatch();
						countAddBatches = 0;
					}
					logger.debug3(String.format("%s: call connection commit", methodName));
					this.connection.commit();
				} else if ("rollback".equals(sqlToLower)) {
					if (countAddBatches > 0) {
						logger.debug3(String.format("%s: call executeBatch - %s batches", methodName, countAddBatches));
						st.executeBatch();
						countAddBatches = 0;
					}
					this.executeUpdate("ROLLBACK");
				} else if (sqlToLower.startsWith("select")
						|| (sqlToLower.startsWith("exec") && (this.execReturnsResultSet
								|| sqlToLower.contains(EXEC_COMMENT_RETURN_RESULTSET.toLowerCase())))) {
					if (countAddBatches > 0) {
						logger.debug3(String.format("%s: call executeBatch - %s batches", methodName, countAddBatches));
						st.executeBatch();
						countAddBatches = 0;
					}
					try {
						logger.debug3(String.format("%s: call executeQuery - %s", methodName, sql));
						this.statement = st;
						if (this.resultSet != null) {
							this.resultSet.close();
						}
						this.resultSet = st.executeQuery(sql);
					} catch (Exception ex) {
						try {
							if (this.resultSet != null) {
								this.resultSet.close();
							}
						} catch (Exception e) {
							//
						}
						try {
							if (this.statement != null) {
								this.statement.close();
							}
						} catch (Exception e) {
							//
						}
						throw ex;
					}
				} else {
					logger.debug6(String.format("%s: call addBatch - %s", methodName, sql));
					st.addBatch(sql);
					countAddBatches++;
					if (++count % this.batchSize == 0) {
						logger.debug3(String.format("%s: call executeBatch - %s batches", methodName, countAddBatches));
						st.executeBatch();
						countAddBatches = 0;
					}
				}
			}
			if (countAddBatches > 0) {
				logger.debug3(String.format("%s: call executeBatch - %s batches", methodName, countAddBatches));
				st.executeBatch();
			}
		} catch (Exception ex) {
			if (!this.getAutoCommit() && this instanceof SOSPgSQLConnection) {
				try {
					this.executeUpdate("ROLLBACK");
				} catch (Exception e) {
					//
				}
			}
			if (ex instanceof SQLException) {
				SQLException sex = (SQLException) ex;
				if (sex.getNextException() == null) {
					throw ex;
				} else {
					throw sex.getNextException();
				}
			} else {
				throw ex;
			}
		} finally {
			try {
				st.close();
			} catch (Exception ex) {
				//
			}
		}

	}

	public void executeStatements(final String contentOfClobAttribute) throws Exception {
		final String methodName = SOSClassUtil.getMethodName();
		ArrayList<String> statements = null;
		boolean executeBatch = this.getUseExecuteBatch();
		boolean supportsBatchUpdates = false;
		try {
			supportsBatchUpdates = this.connection.getMetaData().supportsBatchUpdates();
		} catch (Exception ex) {
			logger.warn(String.format("%s: %s", methodName, ex.getMessage()));
		}
		if (executeBatch) {
			executeBatch = supportsBatchUpdates;
		}
		logger.info(
				String.format("%s: executeBatch = %s (supportsBatchUpdates = %s, useExecuteBatch = %s, batchSize = %s)",
						methodName, executeBatch, supportsBatchUpdates, this.getUseExecuteBatch(), this.batchSize));
		try {
			statements = this.getStatements(contentOfClobAttribute);
			if (statements == null || statements.isEmpty()) {
				if (logger != null) {
					logger.info("no sql statements found");
				}
			} else {
				if (executeBatch) {
					this.executeBatch(statements);
				} else {
					boolean hasOpenResultSet = false;
					for (int i = 0; i < statements.size(); i++) {
						String statement = statements.get(i).toString().trim();
						String sqlToLower = statement.toLowerCase();
						if (sqlToLower.startsWith("select")
								|| (sqlToLower.startsWith("exec") && (this.execReturnsResultSet
										|| sqlToLower.contains(EXEC_COMMENT_RETURN_RESULTSET.toLowerCase())))) {
							if (hasOpenResultSet) {
								this.closeQuery();
								hasOpenResultSet = false;
							}
							this.executeQuery(statement);
							hasOpenResultSet = true;
						} else {
							this.executeUpdate(statement);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(SOSClassUtil.getMethodName() + " : " + e.getMessage(), e);
		}
	}

	public void setProfiler(final SOSProfiler profiler) {
		this.profiler = profiler;
	}

	public void setFieldNameToUpperCase(final boolean value) {
		fieldNameUpperCase = value;
	}

	public void setTableNameToUpperCase(final boolean value) {
		tableNameUpperCase = value;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public String getUrl() {
		return url;
	}

	public Vector getOutput() throws Exception {
		return new Vector();
	}

	public GregorianCalendar getTimestamp() throws Exception {
		return this.getDateTime("yyyy-MM-dd HH:mm:ss");
	}

	public GregorianCalendar getDate() throws Exception {
		return this.getDateTime("yyyy-MM-dd");
	}

	protected GregorianCalendar getDateTime(final String format) throws Exception {
		return new GregorianCalendar();
	}

	protected abstract String replaceCasts(String inputString) throws Exception;

	public int parseMajorVersion(final String productVersion) throws Exception {
		String dbVersion = productVersion.replaceAll("[^0-9\\.]", "");
		String[] split = dbVersion.split("\\.");
		if (split.length < 2) {
			throw new Exception("Failed to parse major Version from String \"" + productVersion + "\"");
		}
		return Integer.parseInt(split[0]);
	}

	public int parseMinorVersion(final String productVersion) throws Exception {
		String dbVersion = productVersion.replaceAll("[^0-9\\.]", "");
		String[] split = dbVersion.split("\\.");
		if (split.length < 2) {
			throw new Exception("Failed to parse minor Version from String \"" + productVersion + "\"");
		}
		return Integer.parseInt(split[1]);
	}

	public int getCompatibility() {
		return compatibility;
	}

	public void setCompatibility(final int compatibility) {
		this.compatibility = compatibility;
	}

	public void setUseExecuteBatch(final boolean executeBatch) {
		this.useExecuteBatch = executeBatch;
	}

	public boolean getUseExecuteBatch() {
		return this.useExecuteBatch;
	}

	public void setBatchSize(final int size) {
		this.batchSize = size;
	}

	public int getBatchSize() {
		return this.batchSize;
	}

	public static int getCompatibility(final String compatibility) {
		if ("normal".equalsIgnoreCase(compatibility)) {
			return SOSConnectionVersionLimiter.CHECK_NORMAL;
		}
		if ("strict".equalsIgnoreCase(compatibility)) {
			return SOSConnectionVersionLimiter.CHECK_STRICT;
		}
		if ("off".equalsIgnoreCase(compatibility)) {
			return SOSConnectionVersionLimiter.CHECK_OFF;
		}
		return SOSConnectionVersionLimiter.CHECK_OFF;
	}

	private static Class getClass(String name) throws Exception {
		if (name.indexOf('.') == -1) {
			name = "sos.connection." + name;
		}
		return Class.forName(name);
	}

	private static SOSConnection createInstance(final String className, final Object[] arguments) throws Exception {
		try {
			Class connectionClass = getClass(className);
			Class[] parameterTypes = new Class[arguments.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				if (arguments[i] instanceof SOSLogger) {
					parameterTypes[i] = SOSLogger.class;
				} else if (arguments[i] instanceof Connection) {
					parameterTypes[i] = Connection.class;
				} else {
					parameterTypes[i] = arguments[i].getClass();
				}
			}
			Constructor connectionConstructor = connectionClass.getConstructor(parameterTypes);
			return (SOSConnection) connectionConstructor.newInstance(arguments);
		} catch (Exception e) {
			throw new Exception(
					"Error occured creating connection object for class " + className + ": " + e.getMessage(), e);
		}
	}

	public abstract String[] getReplacement();

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

	public boolean isExecReturnsResultSet() {
		return execReturnsResultSet;
	}

	public void setExecReturnsResultSet(final boolean execReturnsResultSet) {
		this.execReturnsResultSet = execReturnsResultSet;
	}

}