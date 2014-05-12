package sos.xml;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

//xerces for jdk 1.4 
//import org.apache.xerces.parsers.SAXParser;
//xerces for 1.5
//jdk 1.5 xerces
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
//import javax.xml.parsers.SAXParser;
import org.apache.xalan.xsltc.cmdline.getopt.GetOpt;


import org.xml.sax.Locator;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;

import sos.util.SOSFile;

/**
 *
 * <p>Title: Schema</p>
 * <p>Description: Die Struktur des Schemas wird in der Datenbank abgebildet</p>
 * Dieses Programm interpretiert ein XSD-Schema und erzeugt ein Insert Script.
 * Sechs Parametern müssen übergeben werden.
 * 1. Parameter: Pfad + Name der XSD-Datei
 * 2. Parameter: Content_ID (M, S oder P)
 * 3. Parameter: Pfad + Name der script Datei, der hier erzeugt werden soll
 * 4. Parameter: ini-Datei, in der die DB-Einstellungen stehen .
 * 5. Parameter: CONTENT_TABLENAME  = movement, stock oder price.
 * 6. Parameter: Richtung der Datenstrom (INBOUND oder OUTBOUND; Default INBOUND)
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author Mürüvet Öksüz
 * @version 1.0
 * @resource sos.util.jar
 */
public class SOSXMLValidator implements ContentHandler, ErrorHandler, DTDHandler, EntityResolver {

    // Store the locator
    private Locator locator;

    private String[] args;

    private String inputFile;

    private String schemaFile;

    private final static String USAGE = "\nUSAGE: java SOSXMLValidator" +
    " -i <xml-file> | <dir>" +
    " [-s schema]" +
    " [-h]" +
    "\nvalidate the specified xml file or the xml files in the specified directory.";
    
    
    /**
     * Konstruktor
     *
     * @param args
     */
    public SOSXMLValidator() {}

    /**
     * Konstruktor
     *
     * @param args
     */
    public SOSXMLValidator(String[] args) {
        
      this.args = args;
    }


    /**
	 * Validiert die übergebene XML-Datei. Ist die übergebene XML-Datei nicht valide,
	 * dann wird eine Exception ausgelöst.
	 * @param  fileName
	 * @throws Exception
	 */
	public void validate() throws Exception {
      
	    if (this.inputFile == null || this.inputFile.length() == 0) {
	        throw new Exception("no input file was given.");
	    }
        validate(this.inputFile);
	}

    /**
	 * Validiert die übergebene XML-Datei. Ist die übergebene XML-Datei nicht valide,
	 * dann wird eine Exception ausgelöst.
	 * @param  fileName
	 * @throws Exception
	 */
	public static void validate(File file) throws Exception {
      
      validate(file.getAbsolutePath());
	}

  /**
   * Validiert die übergebene XML-Datei. Ist die übergebene XML-Datei nicht valide,
   * dann wird eine Exception ausgelöst.
   * @param  fileName
   * @throws Exception
   */
  public static void validate(String fileName) throws Exception {

    try {
      // Create a new handler for the parser
      SOSXMLValidator valXML = new SOSXMLValidator();

      // Get an instance of the parser
      SAXParser parser = new SAXParser();

      // set validation mode
      parser.setFeature("http://xml.org/sax/features/validation", true);
      parser.setFeature("http://apache.org/xml/features/validation/schema", true);

      parser.setContentHandler(valXML);
      parser.setEntityResolver(valXML);
      parser.setDTDHandler(valXML);
      parser.setErrorHandler(valXML);

      // Convert file to URL and parse
      try {
        parser.parse(createURL(fileName).toString());
      }
      catch (SAXParseException e) {
          throw new Exception(e.getMessage());
      }
      catch (SAXException e) {
          throw new Exception(e.getMessage());
      }
    } catch (Exception e) {
        throw new Exception("error occurred in SOSXMLValidator.validate(): " + e.getMessage());
    }
  }
  
  /**
   * Validiert die übergebene InputSource. Ist sie nicht valide,
   * dann wird eine Exception ausgelöst.
   * @param  is InputSource
   * @throws Exception
   */
  public static void validate(InputSource is) throws Exception {

    try {
      // Create a new handler for the parser
      SOSXMLValidator valXML = new SOSXMLValidator();

      // Get an instance of the parser
      SAXParser parser = new SAXParser();

      // set validation mode
      parser.setFeature("http://xml.org/sax/features/validation", true);
      parser.setFeature("http://apache.org/xml/features/validation/schema", true);
      parser.setContentHandler(valXML);
      parser.setEntityResolver(valXML);
      parser.setDTDHandler(valXML);
      parser.setErrorHandler(valXML);

      // Convert file to URL and parse
      try {
        parser.parse(is);
      }
      catch (SAXParseException e) {
          throw new Exception("Error validating xml: " + e);
      }
      catch (SAXException e) {
          throw new Exception("Error validating xml: " + e);
      }
    } catch (Exception e) {
      throw new Exception("Error validating xml: " + e);
    }
  }
  
   /**
   * Validiert die übergebene XML-Datei. Ist die übergebene XML-Datei nicht valide,
   * dann wird eine Exception ausgelöst.
   * @param  fileName
   * @throws Exception
   */
  public static void validate(InputSource i, File schemaFile) throws Exception {
  	validate(i, schemaFile.getAbsolutePath() );
  }  
  

  /**
   * Validiert die übergebene XML-Datei. Ist die übergebene XML-Datei nicht valide,
   * dann wird eine Exception ausgelöst.
   * @param  fileName
   * @throws Exception
   */
  public static void validate(String fileName, String schemaFile) throws Exception {

    try {

      // Create a new handler for the parser
      SOSXMLValidator valXML = new SOSXMLValidator();

      // Get an instance of the parser
      SAXParser parser = new SAXParser();

      // set validation mode
      parser.setFeature("http://xml.org/sax/features/validation", true);
      parser.setFeature("http://apache.org/xml/features/validation/schema", true);
      if( schemaFile != null && schemaFile.length() > 0 ){
    	  if (schemaFile.trim().indexOf(" ")!=-1){
    		  parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", schemaFile);
    	  } else {
    		  parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", schemaFile);
    	  }
      }    	  

      parser.setContentHandler(valXML);
      parser.setEntityResolver(valXML);
      parser.setDTDHandler(valXML);
      parser.setErrorHandler(valXML);

      // Convert file to URL and parse
      try {
          parser.parse(createURL(fileName).toString());
      }
      catch (SAXParseException e) {
          throw new Exception(e.getMessage());
      }
      catch (SAXException e) {
          throw new Exception(e.getMessage());
      }
    } catch (Exception e) {
        throw new Exception("error occurred in SOSXMLValidator.validate(): " + e.getMessage());
    }
  }
  
  /**
   * Validiert die übergebene InputSource. Ist sie nicht valide,
   * dann wird eine Exception ausgelöst.
   * @param  is InputSource
   * @throws Exception
   */
  public static void validate(InputSource is, String schemaFile ) throws Exception {

    try {
      // Create a new handler for the parser
      SOSXMLValidator valXML = new SOSXMLValidator();

      // Get an instance of the parser
      SAXParser parser = new SAXParser();

      // set validation mode
      parser.setFeature("http://xml.org/sax/features/validation", true);
      parser.setFeature("http://apache.org/xml/features/validation/schema", true);
      parser.setContentHandler(valXML);
      parser.setEntityResolver(valXML);
      parser.setDTDHandler(valXML);
      parser.setErrorHandler(valXML);
      if( schemaFile != null && schemaFile.length() > 0 ){
    	  if (schemaFile.trim().indexOf(" ")!=-1){
    		  parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", schemaFile);
    	  } else {
    		  parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", schemaFile);
    	  }
      }    	  

     
      // Convert file to URL and parse
      try {
        parser.parse(is);
      }
      catch (SAXParseException e) {
          throw new Exception("Error validating xml: " + e);
      }
      catch (SAXException e) {
          throw new Exception("Error validating xml: " + e);
      }
    } catch (Exception e) {
      throw new Exception("Error validating xml: " + e);
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
  }

  /**
   * SaxParser API Implementierung.
   */
  public void endDocument() throws SAXException {
  }

  /**
   * SaxParser API Implementierung.
   */
  public void startElement(String string1, String name, String string2,
                           Attributes atts) throws SAXException {
  }

  /**
   * SaxParser API Implementierung.
   */

  public void endElement(String string1, String name, String string2) throws
      SAXException {
  }

  /**
   * SaxParser API Implementierung.
   */

  public void characters(char[] cbuf, int start, int len) {
  }

  /**
   * SaxParser API Implementierung.
   */

  public void ignorableWhitespace(char[] cbuf, int start, int len) {
  }

  /**
   * SaxParser API Implementierung.
   */

  public void processingInstruction(String target, String data) throws
      SAXException {
  }

  //////////////////////////////////////////////////////////////////////
  // Sample implementation of the EntityResolver interface.
  //////////////////////////////////////////////////////////////////////
  /**
   * SaxParser API Implementierung.
   */
  public InputSource resolveEntity(String publicId, String systemId) throws
      SAXException {
      //System.out.println("ResolveEntity:"+publicId+" "+systemId);
      //System.out.println("Locator:"+locator.getPublicId()+" "+
      //            locator.getSystemId()+
      //           " "+locator.getLineNumber()+" "+locator.getColumnNumber());
    return null;
  }

  //////////////////////////////////////////////////////////////////////
  // Sample implementation of the DTDHandler interface.
  //////////////////////////////////////////////////////////////////////
  /**
   * SaxParser API Implementierung.
   */
  public void notationDecl(String name, String publicId, String systemId) {
  }

  /**
   * SaxParser API Implementierung.
   */
  public void unparsedEntityDecl(String name, String publicId,
                                 String systemId, String notationName) {
  }

  //////////////////////////////////////////////////////////////////////
  // Sample implementation of the ErrorHandler interface.
  //////////////////////////////////////////////////////////////////////
  /**
   * SaxParser API Implementierung.
   */
  public void warning(SAXParseException e) throws SAXException {
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
    throw new SAXException(e.getMessage());
  }

  /**
   * SaxParser API Implementierung.
   */
  public void skippedEntity(String string) throws SAXException {
  }

  /**
   * SaxParser API Implementierung.
   */
  public void endPrefixMapping(String string) throws SAXException {
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
        throw (new SAXException("error in createURL(): " + e));
      }
    }
    return url;
  }

  
  /**
   * Parst die Programmargumente
   * @throws java.lang.Exception
   */
  public void parseArguments() throws Exception {
      
    int c;
    try {
      if (args.length == 0) {
        throw new Exception("no arguments specified.");
      }

      GetOpt getopt = new GetOpt(args, "i:s:h");
      while ( (c = getopt.getNextOption()) != -1) {
        switch (c) {
          case 'i':
            this.inputFile = getopt.getOptionArg();
            File f = new File(this.inputFile);
            if (!f.exists())
              usage("input file missing: " + this.inputFile);
            break;
          case 's':
            this.schemaFile = getopt.getOptionArg();
            File s = new File(this.schemaFile);
            break;
          case 'h':
            usage(null);
            break;
          default:
            usage(null);
        }

      }
      if (inputFile == null)
        usage("error occurred: more arguments should be specified.");
    }
    catch (Exception e) {
      throw e;
    }
  }

  
  	/**
   *
  	* @param message kann eine Fehlermeldung oder Info an den Benutzer sein.
  	*
  	*/
  	public void usage(String message) {
  	    
  	    if (message != null)
  	        System.err.println(message);
  	    System.err.println(USAGE);
  	    System.exit(1);
  	} 
  
  
  	public static void main(String[] args) throws Exception {
      
  	    SOSXMLValidator validator = null;
      
  	    /*try {
	    	String xmlString = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?> <familie>  <vater/>  <mutter/>  <kinder/></familie>";
	    	System.out.println(xmlString);
	    	java.io.StringReader reader = new java.io.StringReader(xmlString);
	    	InputSource i = new InputSource(reader);
	    	validator = new SOSXMLValidator();
	    	//validator.validate(i, "http://www.deutschebkk.de/schema/2005/InsuredPersonDocument J:/E/java/mo/axis/schema.insured_person/InsuredPersonDocument.xsd");
	    	validator.validate(i, "C:/temp/a.xsd");
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }*/
	    
  	    try {
  	        validator = new SOSXMLValidator(args);
  	        validator.parseArguments();

  	        File dir = new File(validator.inputFile);
  	        if (dir.isFile()) {
  	  	        System.out.print("\nfile [" + validator.inputFile + "]: validation in progress ... ");
  	            if (validator.schemaFile != null && validator.schemaFile.length() > 0) {
  	                SOSXMLValidator.validate(validator.inputFile, validator.schemaFile);
  	            } else {
  	                SOSXMLValidator.validate(validator.inputFile);
  	            }
 	            System.out.println("ok");
  	        } else if (dir.isDirectory()) {
        		Vector filelist = SOSFile.getFilelist(dir.getAbsolutePath(), "\\.xml$", 0);
        		Iterator iterator = filelist.iterator();
        		while(iterator.hasNext()) {
        			File currentFile = (File)iterator.next();
  	    	        System.out.print("\nfile [" + currentFile.getAbsolutePath() + "]: validation in progress ... ");
  	  	            if (validator.schemaFile != null && validator.schemaFile.length() > 0) {
  	  	                SOSXMLValidator.validate(currentFile.getAbsolutePath(), validator.schemaFile);
  	  	            } else {
  	  	                SOSXMLValidator.validate(currentFile.getAbsolutePath());
  	  	            }
  	  	            System.out.println("ok");
        		}
  	        }
  	        else { // file type unbekannt
  	            throw new Exception("file not found: " + validator.inputFile);
  	        }
  	    }
  	    catch (Exception e) {
  	        System.out.println("");
  	        validator.usage(e.getMessage());
  	    }
    }
  
}
