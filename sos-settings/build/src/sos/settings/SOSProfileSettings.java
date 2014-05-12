package sos.settings;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @resource sos.util.jar
 * @version $Id: SOSProfileSettings.java,v 1.1.1.1 2003/09/23 11:48:15 gb Exp $
 * @author <a href="mailto:andreas.pueschel@sos-berlin.com">Andreas Püschel</a>
 * @since 2005-01-25
 * @version 1.1
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSString;

public class SOSProfileSettings extends sos.settings.SOSSettings {

	/** section pattern */
	private static final Pattern	SECTION_PATTERN	= Pattern.compile("^\\s*\\[([^\\]]*)\\].*$");

	/** entry pattern */
	// private static final Pattern ENTRY_PATTERN = Pattern.compile("^(.*)=(.*)$");
	// private static final Pattern ENTRY_PATTERN = Pattern.compile("^([a-z A-Z 0-9_]+)[ \t\n]*=(.*)$");
	private static final Pattern	ENTRY_PATTERN	= Pattern.compile("^([^=#]+)[ \t\n]*=(.*)$");

	/** stellt sections in der INI-Datei dar */
	private ArrayList				sections		= new ArrayList();

	/** stellt alle Einträge in der INI-Datei dar */
	private Properties				entries			= new Properties();

	/** Feldnamen in Kleinschreibung (default) */
	protected boolean				lowerCase		= true;

	/**
	 * Konstruktor
	 *
	 * @param source Name der Datenquelle
	 *
	 * @throws java.lang.Exception
	 */
	public SOSProfileSettings(String source) throws Exception {

		super(source);
		this.load();
	}

	/**
	 * Konstruktor
	 *
	 * @param source Name der Datenquelle
	 * @param logger Das Logger-Objekt
	 *
	 * @throws java.lang.Exception
	 */
	public SOSProfileSettings(String source, SOSLogger logger) throws Exception {

		super(source, logger);
		this.load();
	}

	/**
	 * Konstruktor
	 *
	 * @param source Name der Datenquelle
	 * @param section Name der Sektion
	 *
	 * @throws java.lang.Exception
	 */
	public SOSProfileSettings(String source, String section) throws Exception {

		super(source, section);
		this.load();
	}

	/**
	 * Konstruktor
	 *
	 * @param source Name der Datenquelle
	 * @param section Name der Sektion
	 * @param logger Das Logger-Objekt
	 *
	 * @throws java.lang.Exception
	 */
	public SOSProfileSettings(String source, String section, SOSLogger logger) throws Exception {

		super(source, section, logger);
		this.load();
	}

	/**
	 * 
	 *
	 * @throws java.lang.Exception
	 */
	private void load() throws Exception {

		String sectionName = null;
		BufferedReader in = null;
		String key = null;
		String value = null;
		String line = null;

		try {

			File file = new File(source);
			if (!file.exists())
				throw (new Exception("couldn't find settings-file [" + file.getAbsolutePath() + "]."));

			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

			while (true) {
				line = in.readLine();
				if (line == null)
					break;
				Matcher matcher = SECTION_PATTERN.matcher(line);
				if (matcher.matches()) {
					sectionName = matcher.group(1);
					sections.add(sectionName);
				}
				else {
					matcher = ENTRY_PATTERN.matcher(line);
					if (matcher.matches()) {
						key = matcher.group(1).trim();
						value = matcher.group(2).trim();
						entries.put(sectionName + "#" + key, value);

					}
				}
			}
			if (logger != null)
				logger.debug3(SOSClassUtil.getMethodName() + ": profile [" + source + "] successfully loaded.");
		}
		catch (Exception e) {
			throw (new Exception(SOSClassUtil.getMethodName() + ": " + e.toString()));
		}
		finally {
			if (in == null)
				try {
					in.close();
				}
				catch (Exception e) {
				}
		}
	}

	/**
	 * returns the value of the specified entry
	 *
	 * @param section Name der Sektion
	 * @param entry Name des Eintrages
	 * @return String der Wert eines Eintrages
	 * @throws java.lang.Exception
	 * @see #getSectionEntry( String )
	 */
	public String getSectionEntry(String section, String entry) throws Exception {

		String value;
		try {
			value = entries.getProperty(section + "#" + entry);
			return value;
		}
		catch (Exception e) {
			throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
		}
	}

	/**
	 * returns the value of the specified entry
	 *
	 * @param entry Name des Eintrages
	 * @return String der Wert eines Eintrages
	 * @throws java.lang.Exception
	 * @see #getSectionEntry( String, String )
	 */
	public String getSectionEntry(String entry) throws Exception {

		try {
			return getSectionEntry(section, entry);
		}
		catch (Exception e) {
			throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
		}
	}

	/**
	 * Liefert alle Einträge der eingegebenen Sektion zurück.
	 *
	 * @return Properties Objekt, das alle Einträge der Sektion darstellt.
	 * @throws java.lang.Exception
	 * @see #getSection( String )
	 */
	public Properties getSection(String section) throws Exception {
		try {
			Properties properties = new Properties();

			Pattern p = Pattern.compile("[#]");
			String[] keyValue = null;
			java.util.Enumeration enuma = entries.keys();

			while (enuma.hasMoreElements()) {
				keyValue = p.split(enuma.nextElement().toString());
				if (getSectionEntry(section, keyValue[1]) != null)
					properties.put(normalizeKey(keyValue[1]), this.getSectionEntry(section, keyValue[1]));
			}
			return properties;
		}
		catch (Exception e) {
			throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
		}
	}

	/**
	 * Liefert alle Einträge einer Sektion zurück.
	 *
	 * @return Properties Objekt, das alle Einträge der Sektion darstellt.
	 * @exception Exception
	 * @see #getSection( String )
	 */
	public Properties getSection() throws Exception {
		try {
			return this.getSection(this.section);
		}
		catch (Exception e) {
			throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
		}
	}

	/**
	 * Liefert alle Einträge einer Sektion zurück.
	 *
	 * @return Properties Objekt, das alle Einträge der Sektion darstellt.
	 * @exception Exception
	 * @see #getSection( String )
	 */
	public Properties getSection(String application, String section) throws Exception {
		try {
			return this.getSection(this.section);
		}
		catch (Exception e) {
			throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
		}
	}

	/**
	 * Liefert alle Sektionen einer Anwendung zurück
	 *
	 * @return ArrayList die alle Sektionen beinhaltet
	 * @throws java.lang.Exception
	 */
	public ArrayList getSections() throws Exception {
		return sections;
	}

	/**
	 * Liefert nach Inkrementierung den Wert der eingegebenen Variable zurück.
	 *
	 * @param section Name des Sektions
	 * @param entry Name des Eintrags
	 * @return int Wert der inkrementierten Variable bei Erfolg, sonst -1
	 *
	 * @exception Exception wird ausgelï¿½st falls ein Datenbankfehler vorliegt.
	 */
	synchronized public int getSequence(String section, String entry) throws Exception {

		String sectionName = null;
		String line = null;
		File inFile = null;
		File outFile = null;
		OutputStream out = null;
		BufferedReader in = null;
		int result = -1;

		try {
			inFile = new File(source);
			if (!inFile.exists())
				throw (new Exception("couldn't find profile [" + source + "]."));
			outFile = new File(source + "~");
			out = new FileOutputStream(outFile);
			in = new BufferedReader(new InputStreamReader(new FileInputStream(source)));

			while (true) {
				line = in.readLine();
				if (line == null)
					break;
				Matcher sectionMatcher = SECTION_PATTERN.matcher(line);
				Matcher entryMatcher = ENTRY_PATTERN.matcher(line);
				if (sectionMatcher.matches()) {
					sectionName = sectionMatcher.group(1);
					out.write(line.getBytes());
				}
				else
					if (entryMatcher.matches()) {
						String name = entryMatcher.group(1).trim();
						String value = entryMatcher.group(2).trim();
						if (name.equals(entry) && sectionName.equals(section) && value.matches("[0-9]+")) {
							result = Integer.valueOf(value.trim()).intValue() + 1;
							out.write(new String(entry + "=" + result).getBytes());
						}
						else
							out.write(line.getBytes());
					}
					else
						out.write(line.getBytes());
				out.write(10);
				out.flush();
			}
		}
		catch (Exception e) {
			try {
				outFile.delete();
			}
			catch (Exception ex) {
			}
			throw (new Exception(SOSClassUtil.getMethodName() + ": " + e.toString()));
		}
		finally {
			if (outFile != null)
				try {
					outFile.renameTo(inFile);
				}
				catch (Exception ex) {
				}
			if (in != null)
				try {
					in.close();
				}
				catch (Exception ex) {
				}
			if (out != null)
				try {
					out.close();
				}
				catch (Exception ex) {
				}
			this.load();
		}
		return result;
	}

	/**
	 * Aktiviert die Kleinschreibung fï¿½r Feldnamen
	 *
	 * @see #setKeysToUpperCase
	 */
	public void setKeysToLowerCase() throws Exception {
		if (logger != null)
			logger.debug3("calling " + SOSClassUtil.getMethodName());
		if (logger != null)
			logger.debug3(".. now keys set to lower case.");
		lowerCase = true;
	}

	/**
	 * Aktiviert die Grossschreibung fï¿½r Feldnamen
	 *
	 * @see #setKeysToLowerCase
	 */
	public void setKeysToUpperCase() throws Exception {
		if (logger != null)
			logger.debug3("calling " + SOSClassUtil.getMethodName());
		if (logger != null)
			logger.debug3(".. now keys set to upper case.");
		lowerCase = false;
	}

	/**
	 * Liefert den eingegebenen String in Kleinschreibung als default.
	 *
	 * @param key
	 * @return String
	 * @throws java.lang.Exception
	 * @see #setKeysToLowerCase
	 * @see #setKeysToUpperCase
	 */
	protected String normalizeKey(String key) throws Exception {
		try {
			if (SOSString.isEmpty(key))
				throw new Exception(SOSClassUtil.getMethodName() + ": invalid key.");
			if (this.getIgnoreCase())
				return key;
			if (this.lowerCase)
				return key.toLowerCase();
			else
				return key.toUpperCase();

		}
		catch (Exception e) {
			throw new Exception("error occurred in " + SOSClassUtil.getMethodName() + ": " + e);
		}
	}

	/**
	 * Setzt den Schalter fï¿½r die Berï¿½cksichtigung von Groï¿½/Kleinschreibung
	 * @param ignoreCase
	 */
	public void setIgnoreCase(boolean ignoreCase) {

		this.ignoreCase = ignoreCase;
	}

	/**
	 * liefert den Schalter fï¿½r die Berï¿½cksichtigung von Groï¿½/Kleinschreibung
	 * @param ignoreCase
	 */
	public boolean getIgnoreCase() {

		return this.ignoreCase;
	}

}
