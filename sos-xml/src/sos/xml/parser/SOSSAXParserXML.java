package sos.xml.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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

import sos.connection.SOSConnection;
import sos.util.SOSClassUtil;
import sos.util.SOSStandardLogger;
import sos.util.SOSString;


/**
 * <p>Title: SOSSAXParseXML</p>
 * <p>Description: Parsieren der XML-Datei und generieren von Insertstatements.
 * Diese  Insertstatements werden direkt in die Datenbank geschrieben, wenn 
 * SOSConnection) übergeben wurde und/oder in eine Datei geschrieben, wenn 
 * eine Ausgabe Dateiname angegeben wurde.   </p>
 * 
 * 
 * 
 * * <p>Eine XML-Datei in der Form:</p>
 * <p>-------------------------------------</p>
 * <p>root                                </p>
 * <p>	  Familien </p>
 * <p>		- Vater</p>
 * <p>		- Mutter</p>
 * <p>      - Kinder</p>
 * <p>			- Kind 1</p>
 * <p>			- Kind 2</p>
 * 
 * <p>-------------------------------------</p>
 * <p> wird interpretiert als:</p>
 * <p> 1. Vater, Mutter, Kind 1</p>
 * <p> 2. Vater, Mutter, Kind 2</p>
 * 
 * <p>Die XML-Datei sollte so aufgebaut sein, das Komplextyps immer in der unteren
 * Ebenen liegen.</p>
 * 
 * z.B. Falsch wäre das Beispiel:  
 *  * <p>-------------------------------------</p>
 * <p>root                                </p>
 * <p>	  Familien </p>
 * <p>		- Vater</p>
 * <p>      - Kinder</p>
 * <p>			- Kind 1</p>
 * <p>			- Kind 2</p>
 * <p>		- Mutter</p>
 * 
 * <p> -------------------------------------</p>
 * <p> wird interpretiert als:</p>
 * <p> 1. Vater, Kind 1</p>
 * <p> 2. Vater, Kind 2, Mutter</p>
 * <p> Somit hat Kind 1 keine Mutter und das wäre ja traurig. </p>    
 * 
 * <p>verwendetet Libraries: xerces.jar, sos.util.jar, sos.connection.jar </p>
 * 
 * <p>TestProgramm lieg unter J:\E\java\apps\samples\sos\xmlparser\TestSAXParserXML.java.</p>
 * 
 * 
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GMBH</p>
 * @author Mueruevet Oeksuez
 * @version 1.0
 */
public class SOSSAXParserXML
    implements ContentHandler, ErrorHandler, DTDHandler, EntityResolver {
  /** Attribut: Store the locator*/
  private Locator locator;
  
  /** Attribut:  SOSStandardLogger Objekt */
  private static SOSStandardLogger sosLogger;
    
  /** Attribut:  Name der Tabelle, in der insertet werden*/
  private static String tableName = new String();
  
  /** Attribut:  HashTable für insert Tagsname und Tagsvalues*/
  private HashMap insertTags = new HashMap();
  
  /** Attribut: Name der letzten Tag.*/
  private String lastTagname = "";
  
  /** Attribut: Wert der letzten Tag.*/
  private String lastTagvalue = "";
  
  /** Attribut: Eine Liste der Tagnamen*/
  private ArrayList listOfTags = null;
  
  /** Attribut: Wenn die Methode startelement() zweimal hintereinander aufgerufen wird,
   * dannist die lastTagname ein Komplextyp */
  private boolean startTag = true;
  
  /** Hilfsvariable: Die Methode character kann kein Exception auslösen. daher wird in der 
   * Methode endelement nachträglich ein Exception ausgelöst. 
   * */
  private String errorText = "";

	/** Attribut: Beim bilden der insertStament können defaults übergeben werden */
    private static HashMap defaultFields = new HashMap();
    
    /** SOSString Objekt */
    private SOSString sosString = null;
    
	/**
     * Attribut: Wenn counter gesetz ist, dann wird ein Zähler zu den Statement
     * generiert
     */
    private static boolean counter = false;
    
    /** Attribut: Zähler, wenn counter=true ist, dann wird zu den Statement ein Zähler geschrieben */
    private static int count = 0;
    
    /** Attribut: Es soll eine Ausgabe Script Datei erzeugt werden */
    private String outputScripFilename = "";

    private static BufferedWriter output 	= null;
    
    /** Mappen der Tagnamen */
    private static HashMap mappingTagNames = new HashMap();
    
    /** Attribut: Liste der Vaterknoten */
    private ArrayList parent = new ArrayList();
    
    /** sos.connection.SOSConnection Objekt */
    private static SOSConnection connection = null;
    
    /** Attribut: soll das XML-Datei gegen das Schema validiert werden ? */
    private static boolean checkValidate = false;
    
    /** Attribut: wenn die STaments mit Angabe der Absoluten Pfad angegeben werden sollen. 
     * Das ist sinn voll, wenn in der XML-Datei eine Tagname in unterschiedlichen Pfaden mehrfach vorkommen.
     * Default ist false  
     */
   // private static boolean tagPath = false;  
       
  /**
   * Konstruktor.
   * @param sos.spooler.Log
   * @throws Exception
   */
  public SOSSAXParserXML(SOSStandardLogger spooler_log_) {
      sosLogger = spooler_log_;
  }

  /**
   * Konstruktor
   * @throws Exception
   */
  public SOSSAXParserXML() {
  }

  /**
   * Parsen der uebergebende XML File und Validieren mit dem Schema
   * @param xmlFileName
   */
  public void parseXMLFile(String xmlFileName) throws Exception {  
    try {      
      if (xmlFileName.length() == 1) {
        throw (new Exception("xml Filename not exist!!"));
      }
      
      SAXParser parser;
      SOSSAXParserXML sample;
      try {
        // Create a new handler for the parser
        sample = new SOSSAXParserXML();
        // Get an instance of the parser
        parser = new SAXParser();
      }
      catch (Exception e) {
          sosLogger.warn("SAXParser Not found " + e);
        throw (new Exception("SAXParser Not found " + e));
      }
      // set validation mode
      if (isCheckValidate()) {
          parser.setFeature("http://xml.org/sax/features/validation", true);
      }
      // Set Handlers in the parser
      parser.setContentHandler(sample);
      parser.setEntityResolver(sample);
      parser.setDTDHandler(sample);
      parser.setErrorHandler(sample);
      // Convert file to URL and parse
      try {
        //spooler_log.info("createURL(xmlFileName): " + createURL(xmlFileName));
        parser.parse(createURL(xmlFileName).toString());
        //parser.parse(new org.xml.sax.InputSource(inputStream));
      }
      catch (SAXParseException e) {
          sosLogger.debug5(
            "Exception by Parsing/Validation - SAXParseXML.parseXMLFile() " + e);
        throw (new SAXException("Error in SAXParseXML.parseXMLFile() 1" + e));
      }
    }
    catch (Exception e) {
        sosLogger.debug5(
          "Exception by Parsing/Validation - SAXParseXML.parseXMLFile() " + e);
      throw (new Exception("Fehler im SAXParseXML.parseXMLFile()" + e));
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
  public void startPrefixMapping(String string, String string1) throws
      SAXException {
      
  }

  /**
   * SaxParser API Implementierung.
   */
  public void endPrefixMapping(String string) throws SAXException {
  	
  }

  /**
   * SaxParser API Implementierung.
   */
  public void skippedEntity(String string) throws SAXException {
     
  }

  /**
   * SaxParser API Implementierung.
   * Initialisierung zu Begin des Dokumentes.
   */
  public void startDocument() {
      sosString = new SOSString();
      listOfTags = new ArrayList();
     count = 0;
    
  }

  /**
   * SaxParser API Implementierung.
   */
  public void endDocument() throws SAXException {
      try {
          writeInsertStatement();
          if (output != null) {
              output.close();
          }
      } catch (Exception e) {
          throw new SAXException("\n -> ..error in SAXParseXML.endDocument() " + e);
      }
  }

  /**
   * SaxParser API Implementierung.
   * 
   */
  public void startElement(String string1, String name, String string2,
                           Attributes atts) throws SAXException {
      String[] split = null;
      //String absolutPath = "";
      try {   
          //Das Element wurde bereits durchlaufen. Also schreibe ein Statement auf.
          //und lösche aus der Liste ab dem letzten Komplextyp.
          if (listOfTags.contains(name)) {
              writeInsertStatement();
              updateListOfTags(name);
          }
          if (startTag) {
              parent.add(lastTagname);
              sosLogger.debug5("~~~~~~~~~~~~~~~~> mein Vaterknoten ist ein Komplextyp: " + lastTagname);              
          } else {              
              
             /* if (tagPath) {
                  sosLogger.debug5("~~~~~~~~~~~~~~~~> alle Vaterknoten von dem Tag ausgeben : " + lastTagname);
                  for (int i = 0; i < parent.size(); i++) {
                      sosLogger.debug5(parent.get(i).toString());
                      absolutPath = absolutPath + parent.get(i).toString() + "_@_";
                  }
              }*/
              

              
              listOfTags.add(lastTagname);            
              insertTags.put(lastTagname, lastTagvalue); 
              sosLogger.debug5(lastTagname +"="+ lastTagvalue); 
              sosLogger.debug5("startelement: " + name);
              ///test Attribute lesen
              for (int i = 0; i < atts.getLength(); i++) {
                  System.out.println(atts.getQName(i) + " = "  +atts.getValue(i));
              }
              //
              
          }
          startTag= true;
          lastTagname = name;
          lastTagvalue = "";
      } catch (Exception e) {
          throw new SAXException ("\n -> error in SAXParseXML.startelement " + e);
      }
    
  }

  /**
   * SaxParser API Implementierung.
   */
  public void endElement(String string1, String name, String string2) throws
      SAXException {
      //Die Methode charachter kann kein Exception auslösen. Daher hier nachträglich
      try {
          if (errorText.length() > 0) {
              throw new SAXException (errorText);
          }
          if (name.equals(parent.get(parent.size() -1))) {
              sosLogger.debug5("~~~~~~~~~~~~~~~~> Vaterknoten wird jetzt gelöscht: " + name);
              parent.remove(parent.size()-1);
          }
          sosLogger.debug5("endelement: " + name);    
          startTag = false;
      } catch (Exception e) {
          throw new SAXException ("\n ->..error in SAXParseXML.endElement " + e.toString());
      }
  }

  /**
   * SaxParser API Implementierung.
   * 
   */
  public void characters(char[] cbuf, int start, int len) {
      try {
          String ch = new String(cbuf, start, len);
    
    ch = ch.replaceAll("'", "''");   
    ch = ch.replaceAll("&", "&' ||'" );
    ch = ch.replaceAll("\n", "" );
    ch = ch.replaceAll("\t", "" );
    sosLogger.debug5("Char: " + ch);
    if (startTag) {                
            lastTagvalue = ch;
     }
   
      } catch (Exception e) {
          errorText = "\n -> ..error in SAXParseXML.characters " + e;  
      }
  }

  /**
   * SaxParser API Implementierung.
   */
  public void ignorableWhitespace(char[] cbuf, int start, int len) {}

  /**
   * SaxParser API Implementierung.
   */
  public void processingInstruction(String target, String data) throws
      SAXException {}

  //////////////////////////////////////////////////////////////////////
  // Sample implementation of the EntityResolver interface.
  //////////////////////////////////////////////////////////////////////

  /**
   * SaxParser API Implementierung.
   */
  public InputSource resolveEntity(String publicId, String systemId) throws
      SAXException {
    
    return null;
  }

  //////////////////////////////////////////////////////////////////////
  // Sample implementation of the DTDHandler interface.
  //////////////////////////////////////////////////////////////////////

  /**
   * SaxParser API Implementierung.
   */
  public void notationDecl(String name, String publicId, String systemId) {}

  /**
   * SaxParser API Implementierung.
   */
  public void unparsedEntityDecl(String name, String publicId,
                                 String systemId, String notationName) {
    //System.out.println(name + " " + systemId);
  }

  //////////////////////////////////////////////////////////////////////
  // Sample implementation of the ErrorHandler interface.
  //////////////////////////////////////////////////////////////////////

  /**
   * SaxParser API Implementierung.
   */
  public void warning(SAXParseException e) throws SAXException {}

  /**
   * SaxParser API Implementierung.
   */
  public void error(SAXParseException e) throws SAXException {
    //spooler_log.warn("error in SAXParserXML.error(): " + e.getMessage());
    throw new SAXException(e.getMessage());
  }

  /**
   * SaxParser API Implementierung.
   */
  public void fatalError(SAXParseException e) throws SAXException {
    //spooler_log.warn("fatal ERROR in SAXParserXML.fatalerror(): " +
    //                e.getMessage());
    throw new SAXException(e.getMessage());
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
    }
    catch (MalformedURLException ex) {
      File f = new File(fileName);
      try {
        String path = f.getAbsolutePath();
        // This is a bunch of weird code that is required to
        // make a valid URL on the Windows platform, due
        // to inconsistencies in what getAbsolutePath returns.
        String fs = System.getProperty("file.separator");
        if (fs.length() == 1) {
          char sep = fs.charAt(0);
          if (sep != '/')
            path = path.replace(sep, '/');
          if (path.charAt(0) != '/')
            path = '/' + path;
        }
        path = "file://" + path;
        url = new URL(path);
      }
      catch (MalformedURLException e) {
        //spooler_log.warn("Exception in SAXParserXML.createURL()" + e);
        throw (new SAXException("Fehler in SAXParseXML.createURL()" + e));
      }
    }
    return url;
  }
  
/////////////////////////////////////////////////////////
    
  
  /**
   * Die Liste der Tagname wird aktualsiert. Hier wird die Elemente der 
   * der kleinste Komplextype aus der Liste gelöscht.
   */
  private void updateListOfTags(String tagname) throws Exception {
      
      try {          
          for (int i = listOfTags.size() -1; i != 0; i--) {
              if (listOfTags.get(i).equals(tagname)) {
                  insertTags.remove(listOfTags.get(i));
                  listOfTags.remove(i);                  
                  break;
              } else {
                  insertTags.remove(listOfTags.get(i));
                  listOfTags.remove(i);                  
              }
          }
          //ZUm testen
          /*for (int i =0 ; i < listOfTags.size() ; i++) {                                
             System.out.println("neue Listeneintrag " + listOfTags.get(i));              
          }*/
      } catch (Exception e) {
          throw new Exception ("\n ..error in " + SOSClassUtil.getMethodName() + " " +e);
      }
  }
  
  /**
   * Formulierung einer insertStatement.
   * Die Stament wird in die Datenbank geschrieben, wenn SOSConnection übergeben wurde.
   * Die Stament wird in eine Datei geschrieben, wenn Dateiname übergeben wurde. 
   * @throws Exception
   */
  private void writeInsertStatement() throws Exception {

      try {
          HashMap currHash = new HashMap();
          Iterator keys = this.insertTags.keySet().iterator();
          Iterator vals = insertTags.values().iterator();
          String key = "";
          String val = "";
          String insStr = " insert into " + tableName + " ( ";
          String insStr2 = " values ( ";

          //defaults ausschreiben:
          Iterator dkeys = defaultFields.keySet().iterator();
          Iterator dvals = defaultFields.values().iterator();
          String dkey = "";
          String dval = "";
          while (dkeys.hasNext()) {
              dkey = sosString.parseToString(dkeys.next());
              dval = sosString.parseToString(dvals.next());
              insStr = insStr + dkey;
              insStr2 = insStr2 + "'" + dval + "'";
              if (dkeys.hasNext()) {
                  insStr = insStr + ", ";
                  insStr2 = insStr2 + ", ";
              }
              currHash.put(key, val);
          }
          //          ende defaults schreiben
          if (this.isCounter()) {
              if (insStr.length() > 0 ){
                  insStr = insStr + ", ";
                  insStr2 = insStr2 + ", ";
              }
              
              if (mappingTagNames.containsKey("counter") ) {
                  insStr = insStr + mappingTagNames.get("counter");
              } else {
                  insStr = insStr + " counter ";
              }
              insStr2 = insStr2 + count++;
              currHash.put(key, val);
          }
          if (insStr.length() > 16 ){
              insStr = insStr + ", ";
              insStr2 = insStr2 + ", ";
          }

          while (keys.hasNext()) {
              key = sosString.parseToString(keys.next());
              val = sosString.parseToString(vals.next());
              if (mappingTagNames.containsKey(sosString.parseToString(key).toLowerCase())){
                  key = sosString.parseToString(mappingTagNames, sosString.parseToString(key).toLowerCase());
              }
              //System.out.println(".. " + key + "=" + val);
              insStr = insStr + key;
              insStr2 = insStr2 + "'" + val + "'";
              if (keys.hasNext()) {
                  insStr = insStr + ", ";
                  insStr2 = insStr2 + ", ";
              }
              //currHash.put(key, val);
          }
          insStr = insStr + " ) " + insStr2 + " ) ";
          sosLogger.debug4(" \n~~~~~~~~~~~~~~~~~~InsertStament: " + insStr + "\n");
          if (output != null) {
              output.write(insStr + ";\n");
          }
          
          if (connection != null) {
              connection.executeQuery(insStr);
          }
          
          //insertStatement.add(insStr);
          //listOfXMLPath.add(currHash.clone());          

      } catch (Exception e) {
          throw new Exception("\n ->..error in "
                  + SOSClassUtil.getMethodName() + " " + e);
      }
  }
  
   /**
     * Auslesen der Hashtabelle, die mit in der insert-Stament stehen sollen.
	 * Hier wurden Defaults übergeben , wie z.B. created, create_by etc.
     * 
     * @return Returns the defaultFields.
     */
    public HashMap getDefaultFields() {
        return defaultFields;
    }

    /**
     * Hier können Werte mitübergeben werden, die nicht in der XML-Datei sind,
     * aber mit in der insert-Stament stehen sollen. 
     * z.B. created, create_by etc.
     * 
     * @param defaultFields
     *            The defaultFields to set.
     */
    public void setDefaultFields(HashMap defaultFields_) {
        defaultFields = defaultFields_;
    }
    
    /**
     * Auslesen, ob ein Zähler mitgeschrieben werden soll.
     * 
     * @return Returns the counter.
     */
    public boolean isCounter() {
        return counter;
    }

    /**
     * Setzen einer Zähler für den Statement
     * 
     * @param counter
     *            The counter to set.
     */
    public void setCounter(boolean counter_) {
        counter = counter_;
    }
    
    /**
     * Auslesen Ausgabe Script Dateiname.
     * Die Ausgabescriptdateiname wird nur erzeugt, wenn auch Dateiname angegeben wurde.
     * @return Returns the outputScripFilename.
     */
    public String getOutputScripFilename() {
        return outputScripFilename;
    }
    
    /**
     * Setzen der Ausgabe Script Dateiname.
     * Die Ausgabescriptdateiname wird nur erzeugt, wenn auch Dateiname angegeben wurde.
     * @param outputScripFilename The outputScripFilename to set.
     */
    public void setOutputScripFilename(String outputScripFilename) throws Exception {
        try {
        this.outputScripFilename = outputScripFilename;
        if (outputScripFilename != null && outputScripFilename.length() > 0) {                       
            output = new BufferedWriter((new FileWriter(outputScripFilename)));                                                                  
        }
        } catch (Exception e) {
            throw new Exception ("\n ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }
    
    /**
     * Liefert eine HashTabelle, die den Tagname mappen soll.
     * Z.B. Wenn der key=name und der value=myname ist, dann wird beim insertstatement
     * schreiben der Tagname=name als myname geschrieben. 
     * Diese Methode ist notwendig, wenn die Tagname anders sein sollen als die 
     * Tabellenfeldnamen.
     * @return Returns the mappingTagNames.
     */
    public HashMap getMappingTagNames() {
        return mappingTagNames;
    }

    /**
     * Alle Großschreibungen von key werden in Kleinschreibung umgewandelt.
     * @param mappingTagNames
     *            The mappingTagNames to set.
     */
    public void setMappingTagNames(HashMap mappingTagNames_) throws Exception {
        try {
            Iterator keys = mappingTagNames_.keySet().iterator();
            Iterator vals = mappingTagNames_.values().iterator();
            while (keys.hasNext()) {
                mappingTagNames.put(keys.next().toString().toLowerCase(), vals.next());
            }
        } catch (Exception e) {
            throw new Exception ("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }
    
    /**
     * Liefert den Namen der Tabelle
     * @return Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Tabellenname, in die die Datensätze insertet werden soll
     * @param tableName_
     *            The tableName to set.
     */
    public void setTableName(String tableName_) {
        tableName = tableName_;
    }

    /**
     * Liefert SOSConnection Object
     * @return Returns the connection.
     */
    public SOSConnection getConnection() {
        return connection;
    }
    
    /**
     * Setzt SOSConnection Object.
     * Nur wenn SOSConnection Object gesetzt wird, werden die Statements
     * in die Datenbank insertet.
     * 
     * @param connection The connection to set.
     */
    public void setConnection(SOSConnection connection_) {
        connection = connection_;
    }
    
    /**
     * Soll das XML-Datei gegen das Schema validieren?
     * @return Returns the checkValidate.
     */
    public boolean isCheckValidate() {
        return checkValidate;
    }
    
    /**
     * Soll das XML-Datei gegen das Schema validiert werden?
     * @param checkValidate The checkValidate to set.
     */
    public void setCheckValidate(boolean checkValidate) {
        SOSSAXParserXML.checkValidate = checkValidate;
    }
    
    /**
     * Liefert die Anzahl der Insertstatement, die hier erstellt wurden.
     * @return int
     */
    public int getCountOfInsertStatement() {
    	return count;
    }
    /**
     * 
     * Werden die Staments mit Angabe der Absoluten Pfad als Columnname angegeben. 
     * Das ist sinn voll, wenn in der XML-Datei eine Tagname in unterschiedlichen Pfaden mehrfach vorkommen.
     * Default ist false 
     * @return Returns the tagPath.
     */
    /*public boolean isTagPath() {
        return tagPath;
    }*/
    
    /**
     * Wenn die Staments mit Angabe der Absoluten Pfad angegeben werden sollen. 
     * Das ist sinn voll, wenn in der XML-Datei eine Tagname in unterschiedlichen Pfaden mehrfach vorkommen.
     * Default ist false 
     * @param tagPath The tagPath to set.
     */
    /*public void setTagPath(boolean tagPath) {
        SAXParseXML.tagPath = tagPath;
    }*/
    
    public static void main(String[] args) {
        try {
        	sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG9);
            
            SOSSAXParserXML saxXML = new SOSSAXParserXML(sosLogger);
            
            /*
            getConnection();
            
//			Tagnamen zu Columnnamen mappen             
            HashMap mappingTagNames = new HashMap();                                    
            mappingTagNames.put("counter", "RECORD_ID"); //count ist der Zähler, wenn setCounter = true ist            
            Properties p = conn.getArrayAsProperties("select tag_name, COLUMN_NAME from content_tags, content_columns where content_id = 'S' and content_tags.column_id=content_columns.column_id group by COLUMN_NAME, tag_name");            
            mappingTagNames.putAll(p);                                    
            saxXML.setMappingTagNames(mappingTagNames);
            
//			Beim generieren der Statements werden Defaultwerte mitgeschrieben
            HashMap defaults = getTestDefaults();
            saxXML.setDefaultFields(defaults);
            saxXML.setCounter(true);
            */
//			Name der Ausgabedatei            
            saxXML.setOutputScripFilename("C:/temp/saxscript.sql");
            /*
//			Tabellenname, in die insertet wird            
            saxXML.setTableName("mo_test_2");
            
//			SOSConnection Objekt. Es 
//			wird in die Datenbank erst dann insertet, wenn SOSConnection Objekt übergeben wurde                         
            saxXML.setConnection(conn);
            */
            
            //saxXML.parseXMLFile("C:/eclipse/workspace/sos.util/sos/util/testsicher.xml");
            //saxXML.parseXMLFile("C:/eclipse/workspace/sos.util/sos/util/log_move.xml");
            //saxXML.parseXMLFile("C:/eclipse/workspace/sos.util/sos/util/log_stck.xml");
            saxXML.parseXMLFile("C:/temp/b.xml");
            
            sosLogger.debug("es wurden insgesamt "+ saxXML.getCountOfInsertStatement() + " insert Statement erstellt");
            
        } catch(Exception e) {
            	System.err.print(e);
        } finally {
        try {
        	connection.commit();
        	connection.disconnect();
         } catch (Exception ex) {}
        }
    }
}
