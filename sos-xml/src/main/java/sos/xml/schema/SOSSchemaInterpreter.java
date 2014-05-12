package sos.xml.schema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import sos.connection.SOSOracleConnection;
import sos.util.SOSClassUtil;
import sos.util.SOSStandardLogger;

/**
 * 
 * <p>
 * Title: SchemaInterpreter.java
 * </p>
 * <p>
 * Description: Die Struktur des Schemas wird in der Datenbank abgebildet und/oder
 * in eine HashTabelle gesammelt.  
 * </p>
 * <p>Dieses Programm interpretiert ein XSD-Schema und erzeugt ein Insert Script.</p>
 * <p>Sechs Parametern müssen übergeben werden wenn das Programm standalone aufgerufen wird </p>
 * <p>1. Parameter: Pfad + Name der XSD-Datei </p>
 * <p>2. Parameter: Content_ID (M, S oder P) </p>
 * <p>3. Parameter: Pfad + Name der script Datei, der hier erzeugt werden soll</p> 
 * <p>4. Parameter: ini-Datei, in der die DB-Einstellungen stehen . </p>
 * <p>5. Parameter: CONTENT_TABLENAME = movement, stock oder price. </p>
 * <p>6. Parameter: Richtung der Datenstrom (INBOUND oder OUTBOUND; Default INBOUND)</p>
 * 
 * <p> verwendete Libraries: sos.connection.jar, sos.util.jar </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SOS GmbH
 * </p>
 * 
 * @author Mürüvet Öksüz
 * @version 1.0
 */
public class SOSSchemaInterpreter implements ContentHandler, ErrorHandler,
        DTDHandler, EntityResolver {

    // Store the locator
    private Locator locator;

    /** Content_id (M, S oder P) wird als Parameter übergeben */
    private static String content_id = new String();

    /** ini datei fuer DB-Einstellungen */
    private static String dbIniFile = new String();

    /** Name der insert Script Datei, der erzeugt werden soll */
    private static String scriptName = new String();

    /** Tabelle der moeglichen Spaltennamen */
    private HashMap hashContentsElements = new HashMap();

    /** Tag Typ definitionen */
    private static HashMap hashTypes = new HashMap();

    /**
     * Eindeutige Schlüssel der Tabelle. Existiert kein Eintrag, so wird es mit
     * 0 angefangen
     */
    private int content_element_id = 0;

    /** Liste für die Parents */
    private ArrayList parent = new ArrayList();

    private String parentID4TechnicalInformation = new String("");;

    /** Hilfsvariable */
    private boolean bNextParent = false;

    /** flag, ob der gerade bearbeitete xsd-tag ein parent ist */
    private boolean bParent = false;

    /**
     * Da die Methode startDocument kein Exception auslösen kann, wird dieser
     * mit Parametern bearbeitet
     */
    private String error = new String();

    /** Bufferwriter */
    private static BufferedWriter output = null;

    /**
     * Die Tabellename kann als Parameter angegeben werden. Defaulteinstellung
     * ist INBOUND_CONTENT_ELEMENTS.
     */
    private static String tablePrefix = "INBOUND";       

    /** Tabellenname CONTENT_MODELS*/
    private String tableContentModel = "CONTENT_MODELS";

    private static String contentTableName = new String("");

    private int content_model_id = 1;

    private int contentElementOrder = 0;

    private boolean bdoc = false;

    private String xsdVersionNumber = new String();

    private SOSOracleConnection conn = null;

    private static SOSStandardLogger sosLogger = null;

    /** Alle Tagnamen zu den Spaltennamen */
    private static HashMap allColumnNamefromTagname = new HashMap();

    /** Alle Spaltennamen zu den Tagnamen */
    private static HashMap allTagnamefromColumnName = new HashMap();

    /** Alle Tag typen zu den Tagnamen */
    private static HashMap allTagTypeForTagName = new HashMap();

    /** Alle Minoccurs werte für Tagname */
    private static HashMap allMinOccurForTagName = new HashMap();

    /** Attribut: beinhaltet den Startknoten: tagname und content_element_id */
    private static HashMap startTag = new HashMap();
    
    /** Attribut: Collect, der besagt, das die XML-Informationen gesammelt werden sollen */
    private static boolean collect = true;
    
    /** Hilfsvariable : LastParentTan */
    private int lastParentTag = 0;
    
    /** Alle Kinder zu einem Vaterknoten werden hier zusammengefaßt*/
    private static HashMap allChildrenFromParent = new HashMap();
    
    /** Tagnamen zu Columnnamen mappen*/
    private static HashMap tagname2columnname = null;
    
    /** Eine Liste von Kinderknoten. Die Kindeknoten ist eine Hashtabelle, die Informationen 
     * über einen Tag hat. Die Liste ist geordnet*/
    private static ArrayList childList = new ArrayList();
    
    /** Name der Ausgabe Scriptdatei*/
    private static String fileName = "";
    
    /** SchemaInterpreter Objekt*/
    private static SOSSchemaInterpreter sample = new SOSSchemaInterpreter();
    
    /** Zähler*/
    private int count = 0;
    
    /** Alle Informationen werden pro Datensatz in einer HashTabelle gesammelt:
     * In der ArrayList sind HashTabelle mit Tag Informationen enthaltem.
     * Die ArrayList ist geordnet */
    private static ArrayList allTags = new ArrayList();
    
    /**
     * Konstruktor
     * 
     * @param fileName_
     * @param content_id_
     * @param scriptName_
     * @param dbIniFile_
     * @param contentTableName_
     * @param tablePrefix_
     * @throws Exception
     */
    public SOSSchemaInterpreter(	String fileName_, 
            					String content_id_, 
            					String scriptName_, 
            					String dbIniFile_, 
            					String contentTableName_, 
            					String tablePrefix_) throws Exception {
        try {
            fileName = fileName_;
			content_id =  content_id_; 
			scriptName = scriptName_;
			dbIniFile = dbIniFile_; 
			contentTableName = contentTableName_;
			tablePrefix = tablePrefix_;
			sample = new SOSSchemaInterpreter();
        } catch (Exception e) {
            throw new Exception ("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
        
    }
    
    /**
     * Konstruktor
     * 
     * Wenn Ohne Datenbank gearbeitet werden soll.
     * 
     * @param fileName_
     * @param content_id_
     * @param scriptName_
     * @param contentTableName_
     * @param tablePrefix_
     * 
     * @throws Exception
     */
    public SOSSchemaInterpreter(	String fileName_, 
            String content_id_, 
            String scriptName_,  
            String contentTableName_, 
            String tablePrefix_) throws Exception {
        try {
            fileName = fileName_;
            content_id =  content_id_; 
            scriptName = scriptName_;
            contentTableName = contentTableName_;
            tablePrefix = tablePrefix_;
            sample = new SOSSchemaInterpreter();
        } catch (Exception e) {
            throw new Exception ("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
        
    }
    
    /**
     * Konstruktor
     */
    public SOSSchemaInterpreter(){
    
    }
    
    /**
    * <p>Dieses Programm interpretiert ein XSD-Schema und erzeugt ein Insert Script.</p>
    * <p>Sechs Parametern müssen übergeben werden wenn das Programm standalone aufgerufen wird </p>
    * <p>1. Parameter: Pfad + Name der XSD-Datei </p>
    * <p>2. Parameter: Content_ID (M, S oder P) </p>
    * <p>3. Parameter: Pfad + Name der script Datei, der hier erzeugt werden soll</p> 
    * <p>4. Parameter: ini-Datei, in der die DB-Einstellungen stehen . </p>
    * <p>5. Parameter: CONTENT_TABLENAME = movement, stock oder price. </p>
    * <p>6. Parameter: Richtung der Datenstrom (INBOUND oder OUTBOUND; Default INBOUND)</p>
    */ 
    static public void main(String[] argv) throws Exception{
        try {
            if (argv.length < 5) {
                // Must pass in the name of the XML file.
                System.err
                        .println("Usage: <Schemafilename.xsd> <Content_id> <scriptname.sql> <db Ini Filename> <Tablename> ");
                System.exit(1);
            }
            //xsdDatei überprüfen
            if (argv[0] != null) {
                if (argv[0].indexOf(".xsd") == -1) {
                    System.out.println("There is no XSD-File: " + argv[0]);
                }
            }
            fileName= argv[0].toString(); 
            //content_id setzen
            if (argv[1] != null) {
                content_id = argv[1].toString();
            }
            //scriptname
            if (argv[2] != null) {
                scriptName = argv[2].toString();
            }
            //inidatei fuer db-connection
            if (argv[3] != null) {
                dbIniFile = argv[3].toString();
            }
            //Tabellenname movement, stock oder price
            if (argv[4] != null) {
                contentTableName = argv[4].toString();
            }
            //Tabellename wurde übergeben. Muß aber nicht sein.
            if (argv.length == 6) {
                tablePrefix = argv[5].toString();
            }
            
            sample = new SOSSchemaInterpreter();
            sample.parse();
        } catch (Exception e) {
            throw new Exception ("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }
    
    /**
     * Parsiert das Schema und schreibt generiert insertstatement
     *
     get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the file and also register this class for call backs
			sp.parse("employees.xml", this);


     */
    public void parse() {
        try {                                   
            try {
                output = new BufferedWriter((new FileWriter(scriptName)));
            } catch (IOException e) {
                System.out.println("Error by creating insert script " + e);
            }


            SAXParser parser = new SAXParser();
            // set validation mode
            //( (SAXParser)
            // parser).setValidationMode(SAXParser.DTD_VALIDATION);
            // Set Handlers in the parser
            //parser.setDocumentHandler(sample);
            parser.setContentHandler(sample);
            parser.setEntityResolver(sample);
            parser.setDTDHandler(sample);
            parser.setErrorHandler(sample);
            // Convert file to URL and parse
            try {
                parser.parse(createURL(fileName).toString());
            } catch (SAXParseException e) {
                //Der Parser meckert weil kein xml-Datei, sondern xsd-Datei
                // genommen wurde.
                //Daher fange ich diese Fehler hier ab.
                if (!(e.getMessage()
                        .equals("Element 'xsd:schema' used but not declared.")))
                        System.out.println(e.getMessage());
            } catch (SAXException e) {
                //Der Parser meckert weil kein xml-Datei, sondern xsd-Datei
                // genommen wurde.
                //Daher fange ich diese Fehler hier ab.
                if (!((e.getMessage()
                        .indexOf("Element 'xsd:schema' used but not declared.")) > 0))
                        System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            if (!((e.getMessage()
                    .indexOf("Element 'xsd:schema' used but not declared.")) > 0))
                    System.out.println(e.toString());
        } finally {
            //Der insert-script wurde erzeugt. Also schließen.
            try {
                output.close();
            } catch (IOException e) {
                System.out.println("Error while Closing schema.sql");
            }
        }

    }

    //////////////////////////////////////////////////////////////////////
    // Sample implementation of DocumentHandler interface.
    //////////////////////////////////////////////////////////////////////
    /**
     * SaxParser API Implementierung.
     */
    public void setDocumentLocator(Locator locator) {       
        this.locator = locator;
    }

    /**
     * SaxParser API Implementierung.
     */
    public void startDocument() {        
        try {
            sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG9);
            parent.add("0");
            getConnection(dbIniFile); //Connection lesen
            getType(); //Typen def. holen
            getContentElementId(); //naechste Content_element holen
            getContentModelID(); //naechste content_model holen
            //output.write(getInsertsIOContentModells() + "\n");

        } catch (SQLException e) {
            System.out.println("Error in Schema.startDocument()" + e);
            error = "Error in Schema.startDocument()" + e;
        } catch (Exception e) {
            System.out.println("Error in Schema.startDocument()" + e);
            error = "Error in Schema.startDocument()" + e;
        }
    }

    /**
     * SaxParser API Implementierung.
     */
    public void endDocument() throws SAXException {
        // System.out.println("EndDocument");
        
        try {
            writeInDB();
            if (collect) {
                this.analyzeListOfAllTags();
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }
        try {
            if (conn != null) {
                conn.disconnect();
            }
        } catch (Exception e) {
            throw new SAXException(e);
        }
    }


    /**
     * SaxParser API Implementierung.
     */
    public void startElement(String string1, String name, String string2,
            Attributes atts) throws SAXException {
        try {
        //Startdokument kann keine Exception auslösen. daher wird hier
        // ausgelöst.
            count++;
            if (count == 46) {
                System.out.println("test");
            }
        if (error.length() > 0) { throw new SAXException(error); }
        //System.out.println("StartElement:"+name);
        //hasttabelle neu initialisieren
        if (name.equalsIgnoreCase("element")) {
            if (hashContentsElements.size() > 1) {
                writeInDB();
            }
            initContentsElements();
        }
        if (name.equalsIgnoreCase("documentation")) {
            bdoc = true;
        }
        if (name.equalsIgnoreCase("simpleType")) {
            hashContentsElements.put("LEAF", "1");
            bParent = false;
        }
        if (name.equalsIgnoreCase("complexType")) {
            bParent = true;
            hashContentsElements.put("LEAF", "0");
            parent.add(String.valueOf(content_element_id));
            if (bNextParent) {
                parentID4TechnicalInformation = parent.get(parent.size() - 1)
                        .toString();
                bNextParent = false;
            }
        }

        for (int i = 0; i < atts.getLength(); i++) {
            //String aname = atts.getName(i);
            String aname = atts.getLocalName(i); //Vorsicht
            String type = atts.getType(i);
            String value = atts.getValue(i);
            //if (value.equalsIgnoreCase("ReportingClients")) { //test lösch mich
            //    System.out.println(" "+aname+"("+type+")"+"="+value);
            //}
            //System.out.println(" "+aname+"("+type+")"+"="+value);
            if (value.equalsIgnoreCase("TechnicalInformation")) {
                bNextParent = true;
                // parentID4TechnicalInformation = parent.get(parent.size() -
                // 1).toString();

            }
            if (name.equalsIgnoreCase("totalDigits")) {
                hashContentsElements.put("TOTAL_DIGITS", value);
            }
            if (name.equalsIgnoreCase("fractionDigits")) {
                hashContentsElements.put("FRACTION_DIGITS", value);
            }

            if (aname.equalsIgnoreCase("version")) {
                xsdVersionNumber = value;
                try {
                    output.write(getInsertsIOContentModells() + "\n");
                } catch (Exception e) {
                    System.out.println("Error while write in insert script: "
                            + e);
                }
            }
            if (aname.equalsIgnoreCase("name")) {
                hashContentsElements.put("TAG_NAME", value);
                //Vorschlag, daß Columnname den gleichen Namen wie der Tag hat
                if (tagname2columnname != null && tagname2columnname.get(value) != null) {
                    hashContentsElements.put("COLUMN_NAME", tagname2columnname.get(value));
                } else {
                    hashContentsElements.put("COLUMN_NAME", value.toUpperCase());    
                }
                
            }            
            if ((name.equalsIgnoreCase("restriction"))
                    && (aname.equalsIgnoreCase("base"))) {                  
                	String stype = value.substring(4, value.length());
                	int itype = 1;
                	if (stype != null && hashTypes.containsKey(stype.toLowerCase())) {
                    	itype = getColumnType((Integer.parseInt(hashTypes.get(stype.toLowerCase()).toString())));
            }

             hashContentsElements.put("COLUMN_TYPE", String.valueOf(itype));
             hashContentsElements.put("TAG_TYPE", hashTypes.get(stype.toLowerCase()));
                
            }
            if ((name.equalsIgnoreCase("maxLength"))
                    && (aname.equalsIgnoreCase("value"))) {
                hashContentsElements.put("COLUMN_LENGTH", value);
                hashContentsElements.put("TAG_MAXLENGTH", value);
            }
            //muß noch spezifiziert werden
            if (name.equalsIgnoreCase("minLength")) {
            }
            if ((name.equalsIgnoreCase("length"))
                    && (aname.equalsIgnoreCase("value"))) {
                hashContentsElements.put("COLUMN_LENGTH", value);
                hashContentsElements.put("TAG_MAXLENGTH", value);
            }
            if ((name.equalsIgnoreCase("element"))
                    && (aname.equalsIgnoreCase("minOccurs"))) {
                hashContentsElements.put("MIN_OCCURS", value);
            }
            if ((name.equalsIgnoreCase("element"))
                    && (aname.equalsIgnoreCase("maxOccurs"))) {
                if (value.equals("unbounded")) {
                    hashContentsElements.put("MAX_OCCURS", "-1");
                } else {
                    hashContentsElements.put("MAX_OCCURS", value);
                }
            }
        }
        } catch (Exception e) {
            throw new SAXException ("\n -> ..error in SchemaInterpreter.starteElement() " + e.getMessage());
        }
    }

    /**
     * SaxParser API Implementierung.
     */
    public void endElement(String string1, String name, String string2)
    throws SAXException {
        //System.out.println("EndElement:"+name);
        try {
            if (name.equalsIgnoreCase("complexType")) {
                writeInDB();
                parent.remove(parent.size() - 1);
                hashContentsElements.put("PARENT", parent.get(parent.size() - 1));
            }
        } catch (Exception e) {
            throw new SAXException ("\n -> ..error in SchemaInterpreter.endElement() " + e.getMessage());
        }
    }

    /**
     * SaxParser API Implementierung.
     */

    public void characters(char[] cbuf, int start, int len) {
        //System.out.print("Characters:");
        //System.out.println(new String(cbuf,start,len));        
        /*if (bdoc) {
            String ch = new String(cbuf, start, len).trim();
            ch = ch.replaceAll("'", "''");
            if (ch != null) {
                if (ch.length() > 2000) {
                    hashContentsElements.put("DESCRIPTION", ch.substring(0,
                            2000));
                } else {
                    hashContentsElements.put("DESCRIPTION", ch.substring(0, ch
                            .length()));
                }
                bdoc = false;
            }
        }*/
    }

    /**
     * SaxParser API Implementierung.
     */

    public void ignorableWhitespace(char[] cbuf, int start, int len) {
        //System.out.println("IgnorableWhiteSpace" + new String(cbuf, start,
        // len));
    }

    /**
     * SaxParser API Implementierung.
     */

    public void processingInstruction(String target, String data)
            throws SAXException {
        //System.out.println("ProcessingInstruction:"+target+" "+data);
    }

    //////////////////////////////////////////////////////////////////////
    // Sample implementation of the EntityResolver interface.
    //////////////////////////////////////////////////////////////////////
    /**
     * SaxParser API Implementierung.
     */
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException {        
        return null;
    }

    //////////////////////////////////////////////////////////////////////
    // Sample implementation of the DTDHandler interface.
    //////////////////////////////////////////////////////////////////////
    /**
     * SaxParser API Implementierung.
     */
    public void notationDecl(String name, String publicId, String systemId) {
        //System.out.println("NotationDecl:"+name+" "+publicId+" "+systemId);
    }

    /**
     * SaxParser API Implementierung.
     */
    public void unparsedEntityDecl(String name, String publicId,
            String systemId, String notationName) {
        //System.out.println("UnparsedEntityDecl:"+name + " "+publicId+" "+
        //                  systemId+" "+notationName);
    }

    //////////////////////////////////////////////////////////////////////
    // Sample implementation of the ErrorHandler interface.
    //////////////////////////////////////////////////////////////////////
    /**
     * SaxParser API Implementierung.
     */
    public void warning(SAXParseException e) throws SAXException {
        //System.out.println("Warning:"+e.getMessage());
    }

    /**
     * SaxParser API Implementierung.
     */
    public void error(SAXParseException e) throws SAXException {
        throw new SAXException(e.getMessage());
    }

    /**
     * SaxParser API Implementierung.
     */
    public void fatalError(SAXParseException e) throws SAXException {
        //System.out.println("Fatal error");
        throw new SAXException(e.getMessage());
    }

    /**
     * SaxParser API Implementierung.
     */
    public void skippedEntity(String string) throws SAXException {
        //System.out.println("skippedEntity: " + string );
    }

    /**
     * SaxParser API Implementierung.
     */
    public void endPrefixMapping(String string) throws SAXException {
        //System.out.println("endPrefix: " + string );
    }

    /**
     * SaxParser API Implementierung.
     */
    public void startPrefixMapping(String string, String string1)
            throws SAXException {
        //System.out.println("StartPrefix: " + string + " " + string1);
    }

    //////////////////////////////////////////////////////////////////////
    // Helper method to create a URL from a file name
    //////////////////////////////////////////////////////////////////////

    /**
     * SaxParser API Implementierung.
     */
    private static URL createURL(String fileName) throws SAXException {
        URL url = null;
        try {
            url = new URL(fileName);
        } catch (MalformedURLException ex) {
            File f = new File(fileName);
            try {
                String path = f.getAbsolutePath();
                // This is a bunch of weird code that is required to
                // make a valid URL on the Windows platform, due
                // to inconsistencies in what getAbsolutePath returns.
                String fs = System.getProperty("file.separator");
                if (fs.length() == 1) {
                    char sep = fs.charAt(0);
                    if (sep != '/') path = path.replace(sep, '/');
                    if (path.charAt(0) != '/') path = '/' + path;
                }
                path = "file://" + path;
                url = new URL(path);
            } catch (MalformedURLException e) {
                throw (new SAXException("Fehler in SAXParseXML.createURL()" + e));
            }
        }
        return url;
    }

    //////////////////////////////////////////////////////////////////////
    // Write in DB
    //////////////////////////////////////////////////////////////////////
    /**
     * Erzeugt ein insert-Statement und screibt dieser in die
     * insert-Script-Datei.
     */
    private void writeInDB() throws Exception  {
        String insStr = "";
        try {
            if (hashContentsElements.get("TAG_NAME") == null) {
                return;
            }
            insStr = getInsertString();
            output.write(insStr + "\n");                    
            hashContentsElements.clear();
        }  catch (Exception e) {
            throw new Exception ("\n ->..error in SchemaInterpreter.writeInDB() "+ e);
        }
    }

    /**
     * Schreibt ein insertstatement
     * @return String
     * @throws Exception
     */
    private String getInsertString() throws Exception {
        String insStr = new String("");
        String sContentID = "";
        HashMap childHash = new HashMap();
        Object jparent = "0";        
        try {
            insStr = "insert into CONTENT_TAGS" + " ( "
                    + "\"CONTENT_ELEMENT_ID\", " + 
                    "\"LEAF\", " +
                    "\"CONTENT_MODEL_ID\", " + 
                    "\"CONTENT_ID\", "
                    + "\"CONTENT_ELEMENT_ORDER\", " + 
                    "\"CONTENT_IS_NODE\", " +
                    "\"PARENT\", " + 
                    "\"TAG_NAME\", " + 
                    "\"TAG_TYPE\", " + 
                    "\"TAG_MAXLENGTH\", " +
                    "\"TOTAL_DIGITS\", " + 
                    "\"FRACTION_DIGITS\", " + 
                    "\"MIN_OCCURS\", " + 
                    "\"MAX_OCCURS\", " + 
                    "\"DESCRIPTION\" " + 
                ") values ( " + content_element_id++  + 
                    "," + "" + hashContentsElements.get("LEAF") + ", " +
                    "" + content_model_id + ",";
            
            if (parentID4TechnicalInformation.equals(parent.get(parent.size() - 1))) {
            	sContentID = "T";
                insStr = insStr + "'T',";
            } else {                
                sContentID = content_id;
                insStr = insStr + "'" + content_id + "',";
            }
            insStr = insStr + contentElementOrder++ + ",";
            if (bParent) {                
                insStr = insStr.concat("1,");
            } else {                
                insStr = insStr.concat("0,");
            }

            if (bParent && parent.size() > 2) {                
                jparent = parent.get(parent.size() - 2);
                insStr = insStr + parent.get(parent.size() - 2) + ", ";                
            } else {
                jparent = parent.get(parent.size() - 1);
                insStr = insStr + " " + parent.get(parent.size() - 1) + ", ";
            }
           
            
            if (bParent && collect) {                
                allChildrenFromParent.put(String.valueOf(lastParentTag), childList.clone());
                childList = new ArrayList();
                lastParentTag = Integer.parseInt(parent.get(parent.size() - 2).toString());
                if (allChildrenFromParent.size() == 1 && startTag.size() == 0) {                       
                	startTag.put("content_element_id", String.valueOf(content_element_id - 1));
                	startTag.put("tag_name", hashContentsElements.get("TAG_NAME"));                	
                }
            }
            
            
            
            insStr = insStr + " '" + hashContentsElements.get("TAG_NAME")
                    + "', " + "" + hashContentsElements.get("TAG_TYPE") + ", "
                    + "" + hashContentsElements.get("TAG_MAXLENGTH") + ", "
                    + "" + hashContentsElements.get("TOTAL_DIGITS") + ", " + ""
                    + hashContentsElements.get("FRACTION_DIGITS") + ", " + ""
                    + hashContentsElements.get("MIN_OCCURS") + ", " + ""
                    + hashContentsElements.get("MAX_OCCURS") + ", " + "'"
                    + hashContentsElements.get("DESCRIPTION") + "');";
            
            if (collect && sContentID.equals(content_id)) {
                childHash = new HashMap();
                childHash.put("tag_name", hashContentsElements.get("TAG_NAME"));
                childHash.put("tag_type", hashContentsElements.get("TAG_TYPE"));
                if (hashContentsElements.get("TAG_TYPE").equals("0"))
                    childHash.put("groupable", "0");
                else 
                    childHash.put("groupable", "1");                
                childHash.put("parent", jparent);                
                childHash.put("content_element_id", String.valueOf(content_element_id -1));                
                childHash.put("tag_maxlength",hashContentsElements.get("TAG_MAXLENGTH")  );
                childHash.put("tag_type", hashContentsElements.get("TAG_TYPE"));
                childHash.put("total_digits", hashContentsElements.get("TOTAL_DIGITS"));
                childHash.put("min_occurs", hashContentsElements.get("MIN_OCCURS")  );
                childHash.put("max_occurs", hashContentsElements.get("MAX_OCCURS") );
                childHash.put("column_name", hashContentsElements.get("COLUMN_NAME") );
                childHash.put("content_id", sContentID);
                allTags.add(childHash.clone());
            
            }
                        
            
        } catch (Exception e) {
            throw new Exception ("\n ->..error in  " + SOSClassUtil.getMethodName() + " " + e);
        }
        return insStr;
    }

    

    /**
     * Erzeugt ein insert-Statement fuer die CONTENT_MODELS.
     * 
     * @return String
     */
    private String getInsertsIOContentModells() throws Exception {
        String insStr = new String();
        try {
            insStr = "insert into CONTENT_TAGS" + " ( "
                    + "\"CONTENT_MODEL_ID\", " + "\"CONTENT_ID\", "
                    + "\"CONTENT_ELEMENT_ORDER\", " + "\"CONTENT_IS_NODE\" , "
                    + "\"SCHEMA_NAME\", " + "\"DESCRIPTION\"" + ") values ( "
                    + content_model_id + "," + "'" + content_id + "'," +

                    "'" + tablePrefix.toLowerCase() + "'," + "'"
                    + xsdVersionNumber + "'," + "'');";

        } catch (Exception e) {
            throw e;
        }
        return insStr;
    }

    /**
     * Bestimmt die Datentypen
     * 
     * @throws SQLException
     */
    private void getType() throws Exception {
        String selStr = "";
        try {
            if (conn != null) {
            selStr = " SELECT LOWER(\"TITLE\"), \"VALUE\" " + " FROM SETTINGS "
                    + " WHERE \"APPLICATION\" = 'standard_tables' "
                    + " AND  \"SECTION\" = 'tag_types' "
                    + " AND \"VALUE\" <> ' ' ORDER BY \"VALUE\" ";           
            hashTypes.putAll(conn.getArrayAsProperties(selStr));
            }
            //System.out.println(selStr);
            
        } catch (Exception e) {
            System.out.println("ERROR in Schema.getTypes()" + e + " Statement called: " + selStr);
            throw (e);
        }
    }

    

    /**
     * Bestimmt die Content_element_id aus der Tabelle Inbound_content_elements
     */
    private void getContentElementId() throws Exception {
        try {            
            if (conn != null) {
                String result = conn
                .getSingleValue(" SELECT MAX(\"CONTENT_ELEMENT_ID\") FROM \"CONTENT_TAGS\"");
                if (result != null && result.length() > 0) {
                    content_element_id = Integer.parseInt(result);
                }
            } else {
                content_element_id  = 1;
            }
            
        } catch (Exception e) {
            System.out.println("ERROR in Schema.getContentElementId()" + e);
            throw (e);
        }
    }
    
    /**
     * Initialisiert der Spaltennamen .
     */
    private void initContentsElements() {
        hashContentsElements.clear();
        hashContentsElements.put("CONTENT_ELEMENT_ID", content_id);
        hashContentsElements.put("LEAF", "0");
        hashContentsElements.put("PARENT", "0");
        hashContentsElements.put("TAG_NAME", "NULL");
        hashContentsElements.put("TAG_TYPE", "0");
        hashContentsElements.put("TAG_MAXLENGTH", "0");
        hashContentsElements.put("COLUMN_NAME", "NULL");
        hashContentsElements.put("COLUMN_TYPE", "0");
        hashContentsElements.put("COLUMN_LENGTH", "0");
        hashContentsElements.put("TOTAL_DIGITS", "0");
        hashContentsElements.put("FRACTION_DIGITS", "0");
        hashContentsElements.put("MIN_OCCURS", "0");
        hashContentsElements.put("MAX_OCCURS", "0");
        hashContentsElements.put("CONTENT_MODEL_ID", "0");
        hashContentsElements.put("DESCRIPTION", "NULL");
    }

    /**
     * stellt die Datenbankverbindung her
     * 
     * @return connection Objekt der Datenbank
     * 
     * @exception NullPointerException
     *                wird ausgelöst, falls <code>dbinifile</code> Fehlerhafte
     *                Parameter enthält.
     * @exception IOException
     *                wird ausgelöst, falls ein Fehler beim Lesen der
     *                <code>dbinifile</code> vorliegt.
     * @exception ClassNotFoundException
     *                wird ausgelöst, falls ein Fehler beim Laden des
     *                JDBC-Treibes auftritt.
     * @exception SQLException
     *                wird ausgelöst, falls ein Datenbankfehler vorliegt.
     * @exception Exception
     *                wird ausgelöst, falls ein unbekannter Fehler auftritt.
     */
    private void getConnection(String iniFile) {
        try {           
            if (iniFile == null || (iniFile != null && iniFile.length() == 0)) {
                return;
            }
            conn = new SOSOracleConnection(iniFile, sosLogger);
            conn.connect();
        } catch (Exception e) {
            System.err.println("Error in TestHistory.getcoonetion: " + e);
        }
    } // getConnection

    /**
     * liest die Verbindungsparameter aus der eingegeben Datei in
     * property-Objekt.
     * 
     * @param filename
     *            Dateiname, die die Verbindungsparameter enthält.
     * @return Properties
     * 
     * @exception IOException
     *                wird ausgelöst, falls Dateilesefehler beim Einlesen der
     *                <code>filename</code> vorliegt.
     *  
     */
    /*public static Properties aloadParamsa(String filename) throws IOException {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(filename));
            return props;
        } catch (IOException e) {
            throw (new IOException("ConnectionSettings:loadParams: " + e));
        }
    } */// loadParams

    /**
     * Liefert von Tag Typ den entsprechende Column Typ.
     */
    private int getColumnType(int tagType) {
        int retVal = -1;
        switch (tagType) {
        case 1:
            //String in Varchar
            retVal = 1;
            break;
        case 2:
            //Integer in Number
            retVal = 2;
            hashContentsElements.put("TOTAL_DIGITS", "16");//Voreinstellung,
                                                           // abgesprochen mit
                                                           // Herrn Kramer
            hashContentsElements.put("FRACTION_DIGITS", "0");//Voreinstellung,
                                                             // abgesprochen mit
                                                             // Herrn Kramer
            break;
        case 3:
            //Date in Date
            retVal = 3;
            break;
        case 4:
            //Double in Number
            retVal = 2;
            hashContentsElements.put("TOTAL_DIGITS", "16");//Voreinstellung,
                                                           // abgesprochen mit
                                                           // Herrn Kramer
            hashContentsElements.put("FRACTION_DIGITS", "4");//Voreinstellung,
                                                             // abgesprochen mit
                                                             // Herrn Kramer
            break;
        case 5:
            //Datetime in Date
            retVal = 3;
            break;
        }
        return retVal;
    }

    /**
     * Liefert die content_model_id, wenn SOSConnectoin Objekt übergeben wurde. Sonst 
     * ist content_model_id = 1;
     * @throws Exception
     */
    private void getContentModelID() throws Exception {
        try {
            if (conn != null) {
                String selStr = conn.getSingleValue("SELECT MAX(\"CONTENT_MODEL_ID\") FROM "+ tableContentModel);
                if (selStr != null && selStr.length() > 0) {
                    content_model_id = Integer.parseInt(selStr);
                }
            } 
            
        } catch (SQLException e) {
            System.out.println("ERROR in Schema.getContentModelID()" + e);
            throw (e);
        }
    }

    /**
     * @return Returns the allColumnNamefromTagname.
     */
    public HashMap getAllColumnNamefromTagname() {
        return allColumnNamefromTagname;
    }
    /**
     * @return Returns the allMinOccurForTagName.
     */
    public HashMap getAllMinOccurForTagName() {
        return allMinOccurForTagName;
    }
    /**
     * @return Returns the allTagnamefromColumnName.
     */
    public HashMap getAllTagnamefromColumnName() {
        return allTagnamefromColumnName;
    }
    /**
     * @return Returns the allTagTypeForTagName.
     */
    public HashMap getAllTagTypeForTagName() {
        return allTagTypeForTagName;
    }
    /**
     * @return Returns the allChildrenFromParent.
     */
    public HashMap getAllChildrenFromParent() {
        return allChildrenFromParent;
    }
    
    /**
     * HashTabelle übergeben, die Tagnamen zu Columnnamen mapp
     * @param tagname2columnname The tagname2columnname to set.
     */
    public void mapTagname2columnname(HashMap tagname2columnname) {
        SOSSchemaInterpreter.tagname2columnname = tagname2columnname;
    }
    /**
     * @param collect The collect to set.
     */
    public void setCollect(boolean collect) {
        SOSSchemaInterpreter.collect = collect;
    }
    /**
     * @return Returns the startTag.
     */
    public HashMap getStartTag() {
        return startTag;
    }
    /**
     * Alle Informationen werden pro Datensatz in einer HashTabelle gesammelt:
     * In der ArrayList sind HashTabelle mit Tag Informationen enthaltem.
     * Die ArrayList ist geordnet 
     * @return Returns the allTags.
     */
    public ArrayList getAllTags() {
        return allTags;
    }
    
    /**
     * 
     * Die Methode bildet die HAshTabellen:
     * allMinOccurForTagName
     * allColumnNamefromTagname
     * allTagnamefromColumnName
     * allTagTypeForTagName
     *            
     * allChildrenFromParent
     * 
     * 
     * 
     * @throws Exception
     */
    private void analyzeListOfAllTags() throws Exception{        
        HashMap childHash = new HashMap();
        HashMap childHash2 = new HashMap();
        ArrayList childList = new ArrayList();
        Object contentElementId = "0";
        Object tag_type = "0";
        Object lastContentElementId = "0";
        ArrayList list = null;
        try {
            list = getAllTags();
           for (int i = 0; i < list.size(); i++) {
               childHash = (HashMap)list.get(i);                              
               allMinOccurForTagName.put(childHash.get("tag_name"), childHash.get("min_occurs"));
               allColumnNamefromTagname.put(childHash.get("tag_name"),childHash.get("column_name"));//Nur sinnvoll, wennes gemappt wurde
               allTagnamefromColumnName.put(childHash.get("column_name"),childHash.get("tag_name"));
               allTagTypeForTagName.put(childHash.get("tag_name"), childHash.get("tag_type"));
           }
           
           
           ArrayList parentList = new ArrayList();
           for (int i = 0; i < list.size(); i++) {
               childHash = (HashMap)list.get(i);               
               if (childHash.get("tag_type").equals("0")) {
                   parentList.add(childHash.get("content_element_id"));
               }               
           }
           
           Object parentId = "0";
           for (int i = 0; i< parentList.size(); i++) {
                parentId = parentList.get(i);                
                for (int j = 0; j < list.size(); j++) {
                   childHash = (HashMap)list.get(j);                   
                   childHash2 = new HashMap();
                   if (childHash.get("parent").equals(parentId)) {
                       if (!childHash.get("content_element_id").equals(parentId)) {
                           childHash2.put("TAG_NAME", childHash.get("tag_name"));
                           childHash2.put("TAG_TYPE", childHash.get("tag_type"));
                           childHash2.put("GROUPABLE", childHash.get("groupable"));               
                           childHash2.put("CONTENT_ELEMENT_ID", childHash.get("content_element_id"));
                           childHash.put("PARENT", childHash.get("parent"));
                                                      
                           childList.add(childHash2.clone());
                       }
                   }                               
                }
                if (allChildrenFromParent.size() == 0) {
                    HashMap st = new HashMap();                    
                    st.put("TAG_NAME", startTag.get("tag_name"));
                    st.put("TAG_TYPE", "0");
                    st.put("GROUPABLE", "0");               
                    st.put("CONTENT_ELEMENT_ID", startTag.get("content_element_id"));
                    st.put("PARENT", "0");
                    allChildrenFromParent.put("0", st.clone());
                } 
                allChildrenFromParent.put(parentId, childList.clone());
                
                childList.clear();
               }                             
           
        } catch (Exception e) {
            throw new Exception("\n ->..error in getTestDaten() " + e);
    }
    }
    
    /**
     * Typen auslesen. Z.B. String ist 0 etc
     * @return Returns the hashTypes.
     */
    public HashMap getTypes() {
        return hashTypes;
    }
    /**
     * Typen setzen. Z.B. String ist 0 etc
     * @param hashTypes The hashTypes to set.
     */
    public void setTypes(HashMap hashTypes_) {
        hashTypes = hashTypes_;
    }
}