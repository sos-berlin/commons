package sos.settings.converter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:robert.ehrlich@sos-berlin.com">Robert Ehrlich</a>
 * @resource sos.util.jar
 * @since 2006-12-06
 * @version 1.1
 */


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import sos.util.SOSLogger;
import sos.util.SOSClassUtil;

public class SOSSettingsProfile2XMLConverter {

	/** Source INI Datei */
	private String source = "";

	/** Encoding der neuen XML Datei */
	private String xmlEncoding = "ISO-8859-1";
	
	/** Name der Stylesheet Datei */
	private String xmlStylesheet	  = "";
	
	/** section pattern */
	private static final Pattern SECTION_PATTERN = Pattern
			.compile("^\\s*\\[([^\\]]*)\\].*$");

	/** entry pattern */
	private static final Pattern ENTRY_PATTERN = Pattern
			.compile("^([; a-z A-Z 0-9_]+)[ \t\n]*=(.*)$");
	
	private static final String INDENT = "  ";

	private String newLine = System.getProperty("line.separator");
	
	/** Logger Objekt */
	private SOSLogger logger;

	/** stellt sections mit allen Einträgen in der INI-Datei dar */
	private LinkedHashMap sections = new LinkedHashMap();

	/** stellt alle Eintrag-Kommentare in der INI-Datei dar */
	private LinkedHashMap entryNotes = new LinkedHashMap();

		
	/**
	 * Konstruktor
	 * 
	 * @param source
	 *            Name der Datenquelle
	 * 
	 * @throws java.lang.Exception
	 */
	public SOSSettingsProfile2XMLConverter(String source) throws Exception {

		this.source = source;
		this.load();
	}

	/**
	 * Konstruktor
	 * 
	 * @param source
	 *            Name der Datenquelle
	 * @param logger
	 *            Das Logger-Objekt
	 * 
	 * @throws java.lang.Exception
	 */
	public SOSSettingsProfile2XMLConverter(String source, SOSLogger logger) throws Exception {

		this.source = source;
		this.logger = logger;

		this.load();
	}

	/**
	 * Lädt die INI-Datei in den Speicher
	 * 
	 * @throws java.lang.Exception
	 */
	private void load() throws Exception {

		String 			sectionName = null;
		BufferedReader 	in 			= null;
		String 			key 		= null;
		String 			value 		= null;
		String 			line 		= null;
		LinkedHashMap 	entries 	= null;
		ArrayList 		notes 		= null;

		try {

			File file = new File(source);
			if (!file.exists())
				throw (new Exception("couldn't find source [" + source + "]."));

			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));

			sections 	= new LinkedHashMap();
			entries 	= new LinkedHashMap();
			entryNotes 	= new LinkedHashMap();
			notes 		= new ArrayList();

			while (true) {
				line = in.readLine();
				if (line == null)
					break;
				Matcher matcher = SECTION_PATTERN.matcher(line);
				if (matcher.matches()) {
					entries 	= new LinkedHashMap();
					notes 		= new ArrayList();
					sectionName = matcher.group(1);
				} 
				else {
					matcher = ENTRY_PATTERN.matcher(line);
					if (matcher.matches()) {
						key 	= matcher.group(1).trim();
						value 	= matcher.group(2).trim();
						entries.put(key, value);
						if (notes.size() > 0) {
							entryNotes.put(key, notes);
							notes = new ArrayList();
						}
					} 
					else {
						line = line.trim();
						if (line.startsWith(";") && line.length() > 1) {
							notes.add(line.substring(1).trim());
						}
					}

				}
				sections.put(sectionName, entries);

			}// while
			if (logger != null)
				logger.debug3(SOSClassUtil.getMethodName() + ": profile ["
						+ source + "] successfully loaded.");
		} catch (Exception e) {
			throw (new Exception(SOSClassUtil.getMethodName() + ": "
					+ e.toString()));
		} finally {
			if (in == null)
				try {
					in.close();
				} catch (Exception e) {
				}
		}
	}

	
	/**
	 * XML Datei schreiben
	 * 
	 * @param application   Name der Anwendung
	 * @param xmlFilename    Name|Pfad der XML Datei
	 * @throws Exception
	 */
	public void process(String application, String xmlFilename) throws Exception {

		FileWriter f = new FileWriter(xmlFilename);
		f.write("<?xml version=\"1.0\" encoding=\"" + this.xmlEncoding + "\" ?>"	+ newLine);
		if(this.xmlStylesheet != null && this.xmlStylesheet.length() > 0){
			f.write("<?xml-stylesheet type=\"text/xsl\" href=\""+this.xmlStylesheet+"\" ?>"+newLine);
			f.write(newLine);
		}
		f.write("<settings>" + newLine);
		f.write(newLine);
		f.write(INDENT + "<application name=\"" + application + "\">"+ newLine);
		f.write(this.writeNote("de", "", INDENT + INDENT));
		f.write(this.writeNote("en", "", INDENT + INDENT));
		f.write(newLine);

		f.write(INDENT + INDENT + "<sections>" + newLine);

		Iterator secIt = sections.entrySet().iterator();
		while (secIt.hasNext()) {
			Map.Entry secMapEntry = (Map.Entry) secIt.next();
			String sectionName = (String) secMapEntry.getKey();
			LinkedHashMap entries = (LinkedHashMap) secMapEntry.getValue();

			f.write(INDENT + INDENT + INDENT + "<section name=\"" + sectionName	+ "\">" + newLine);
			f.write(this.writeNote("de", "", INDENT + INDENT + INDENT+ INDENT));
			f.write(this.writeNote("en", "", INDENT + INDENT + INDENT+ INDENT));
			f.write(newLine);

			f.write(INDENT + INDENT + INDENT + INDENT + "<entries>" + newLine);

			Iterator entIt = entries.entrySet().iterator();
			while (entIt.hasNext()) {
				Map.Entry entMapEntry = (Map.Entry) entIt.next();
				String entryName = (String) entMapEntry.getKey();
				String entryValue = (String) entMapEntry.getValue();
				String disabled = "";
				String noteValue = "";
				if (entryName.startsWith(";")) {
					entryName = entryName.substring(1);
					disabled = "disabled=\"true\"";
				}

				f.write(INDENT + INDENT + INDENT + INDENT + INDENT + "<entry name=\"" + entryName + "\" " + disabled + ">"
						+ newLine);
				f.write(INDENT + INDENT + INDENT + INDENT + INDENT + INDENT
						+ "<value>" + newLine);
				f.write(INDENT + INDENT + INDENT + INDENT + INDENT + INDENT
						+ INDENT + this.writeCDATA(entryValue) + newLine);
				f.write(INDENT + INDENT + INDENT + INDENT + INDENT + INDENT
						+ "</value>" + newLine);

				if (entryNotes != null && entryNotes.containsKey(entryName)) {
					ArrayList notes = (ArrayList) entryNotes.get(entryName);
					int notesSize = notes.size();
					StringBuffer sb = new StringBuffer("");
					for (int i = 0; i < notesSize; i++) {
						String before = (i > 0) ? INDENT + INDENT + INDENT
								+ INDENT + INDENT + INDENT + INDENT + INDENT
								+ INDENT + INDENT + INDENT : "";
						String after = (i == (notesSize - 1)) ? "" : newLine;
						sb.append(before + notes.get(i).toString() + after);
					}
					noteValue = sb.toString();
				}
				f.write(this.writeNote("de", noteValue, INDENT + INDENT
						+ INDENT + INDENT + INDENT + INDENT));
				f.write(this.writeNote("en", noteValue, INDENT + INDENT
						+ INDENT + INDENT + INDENT + INDENT));

				f.write(INDENT + INDENT + INDENT + INDENT + INDENT + "</entry>"
						+ newLine);
				f.write(newLine);
			}
			f.write(INDENT + INDENT + INDENT + INDENT + "</entries>" + newLine);
			f.write(INDENT + INDENT + INDENT + "</section>" + newLine);

			f.write(newLine);

		}
		f.write(INDENT + INDENT + "</sections>" + newLine);
		f.write(newLine);
		f.write(INDENT + "</application>" + newLine);
		f.write(newLine);
		f.write("</settings>");

		f.close();

	}

	/**
	 * Application|Section|Entry Notiz schreiben
	 * 
	 * @param language
	 * @param value
	 * @param indent
	 * @return
	 */
	private String writeNote(String language, String value, String indent) {
		StringBuffer sb = new StringBuffer();

		sb.append(indent+"<note language=\"" + language + "\">"+ newLine)
		.append(indent+INDENT+"<div xmlns=\"http://www.w3.org/1999/xhtml\">"+newLine)
		.append(indent+INDENT+INDENT+"<p>"+newLine)
		.append(indent+INDENT+INDENT+INDENT+this.writeCDATA(value)+newLine)
		.append(indent+INDENT+INDENT+"</p>"+newLine)
		.append(indent+INDENT+"</div>"+newLine)
		.append(indent+"</note>"+newLine);

		return sb.toString();
	}

	/**
	 * 
	 * @param val  Zeichenkette
	 * @return
	 */
	private String writeCDATA(String val) {

		return "<![CDATA[" + val + "]]>";
	}

	/**
	 * 
	 * @return XML Encoding
	 */
	public String getXMLEncoding() {
		return xmlEncoding;
	}

	/**
	 * Setzt XML Encoding
	 * 
	 * @param encoding  XML Encoding
	 */
	public void setXMLEncoding(String encoding) {
		this.xmlEncoding = encoding;
	}

	/**
	 * 
	 * @return Name|Href der stylesheet Datei
	 */
	public String getXmlStylesheet() {
		return xmlStylesheet;
	}

	/**
	 * Setzt Name|Href der stylesheet Datei
	 * 
	 * @param xmlStylesheet Name|Href der stylesheet Datei
	 */
	public void setXmlStylesheet(String xmlStylesheet) {
		this.xmlStylesheet = xmlStylesheet;
	}

}
