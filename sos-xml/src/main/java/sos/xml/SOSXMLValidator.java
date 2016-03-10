package sos.xml;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

import org.apache.xalan.xsltc.cmdline.getopt.GetOpt;
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

import sos.util.SOSFile;

/** @author Mürüvet Öksüz */
public class SOSXMLValidator implements ContentHandler, ErrorHandler, DTDHandler, EntityResolver {

    private final static String USAGE = "\nUSAGE: java SOSXMLValidator" + " -i <xml-file> | <dir>" + " [-s schema]" + " [-h]"
            + "\nvalidate the specified xml file or the xml files in the specified directory.";
    private Locator locator;
    private String[] args;
    private String inputFile;
    private String schemaFile;

    public SOSXMLValidator() {
    }

    public SOSXMLValidator(String[] args) {
        this.args = args;
    }

    public void validate() throws Exception {
        if (this.inputFile == null || this.inputFile.isEmpty()) {
            throw new Exception("no input file was given.");
        }
        validate(this.inputFile);
    }

    public static void validate(File file) throws Exception {
        validate(file.getAbsolutePath());
    }

    public static void validate(String fileName) throws Exception {
        try {
            SOSXMLValidator valXML = new SOSXMLValidator();
            SAXParser parser = new SAXParser();
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setContentHandler(valXML);
            parser.setEntityResolver(valXML);
            parser.setDTDHandler(valXML);
            parser.setErrorHandler(valXML);
            try {
                parser.parse(createURL(fileName).toString());
            } catch (SAXParseException e) {
                throw new Exception(e.getMessage(), e);
            } catch (SAXException e) {
                throw new Exception(e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new Exception("error occurred in SOSXMLValidator.validate(): " + e.getMessage(), e);
        }
    }

    public static void validate(InputSource is) throws Exception {
        try {
            SOSXMLValidator valXML = new SOSXMLValidator();
            SAXParser parser = new SAXParser();
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setContentHandler(valXML);
            parser.setEntityResolver(valXML);
            parser.setDTDHandler(valXML);
            parser.setErrorHandler(valXML);
            try {
                parser.parse(is);
            } catch (SAXParseException e) {
                throw new Exception("Error validating xml: " + e.getMessage(), e);
            } catch (SAXException e) {
                throw new Exception("Error validating xml: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new Exception("Error validating xml: " + e.getMessage(), e);
        }
    }

    public static void validate(InputSource i, File schemaFile) throws Exception {
        validate(i, schemaFile.getAbsolutePath());
    }

    public static void validate(String fileName, String schemaFile) throws Exception {
        try {
            SOSXMLValidator valXML = new SOSXMLValidator();
            SAXParser parser = new SAXParser();
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            if (schemaFile != null && !schemaFile.isEmpty()) {
                if (schemaFile.trim().indexOf(" ") != -1) {
                    parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", schemaFile);
                } else {
                    parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", schemaFile);
                }
            }
            parser.setContentHandler(valXML);
            parser.setEntityResolver(valXML);
            parser.setDTDHandler(valXML);
            parser.setErrorHandler(valXML);
            try {
                parser.parse(createURL(fileName).toString());
            } catch (SAXParseException e) {
                throw new Exception(e.getMessage(), e);
            } catch (SAXException e) {
                throw new Exception(e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new Exception("error occurred in SOSXMLValidator.validate(): " + e.getMessage());
        }
    }

    public static void validate(InputSource is, String schemaFile) throws Exception {
        try {
            SOSXMLValidator valXML = new SOSXMLValidator();
            SAXParser parser = new SAXParser();
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setContentHandler(valXML);
            parser.setEntityResolver(valXML);
            parser.setDTDHandler(valXML);
            parser.setErrorHandler(valXML);
            if (schemaFile != null && !schemaFile.isEmpty()) {
                if (schemaFile.trim().indexOf(" ") != -1) {
                    parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation", schemaFile);
                } else {
                    parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", schemaFile);
                }
            }
            try {
                parser.parse(is);
            } catch (SAXParseException e) {
                throw new Exception("Error validating xml: " + e.getMessage(), e);
            } catch (SAXException e) {
                throw new Exception("Error validating xml: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            throw new Exception("Error validating xml: " + e.getMessage(), e);
        }
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startDocument() {
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(String string1, String name, String string2, Attributes atts) throws SAXException {
    }

    public void endElement(String string1, String name, String string2) throws SAXException {
    }

    public void characters(char[] cbuf, int start, int len) {
    }

    public void ignorableWhitespace(char[] cbuf, int start, int len) {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        return null;
    }

    public void notationDecl(String name, String publicId, String systemId) {
    }

    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) {
    }

    public void warning(SAXParseException e) throws SAXException {
    }

    public void error(SAXParseException e) throws SAXException {
        throw new SAXException(e.getMessage(), e);
    }

    public void fatalError(SAXParseException e) throws SAXException {
        throw new SAXException(e.getMessage(), e);
    }

    public void skippedEntity(String string) throws SAXException {
    }

    public void endPrefixMapping(String string) throws SAXException {
    }

    public void startPrefixMapping(String string, String string1) throws SAXException {
    }

    private static URL createURL(String fileName) throws SAXException {

        URL url = null;
        try {
            url = new URL(fileName);
        } catch (MalformedURLException ex) {
            File f = new File(fileName);
            try {
                String path = f.getAbsolutePath();
                String fs = System.getProperty("file.separator");
                if (fs.length() == 1) {
                    char sep = fs.charAt(0);
                    if (sep != '/') {
                        path = path.replace(sep, '/');
                    }
                    if (path.charAt(0) != '/') {
                        path = '/' + path;
                    }
                }
                path = "file://" + path;
                url = new URL(path);
            } catch (MalformedURLException e) {
                throw new SAXException("error in createURL(): " + e.getMessage(), e);
            }
        }
        return url;
    }

    public void parseArguments() throws Exception {
        int c;
        try {
            if (args.length == 0) {
                throw new Exception("no arguments specified.");
            }
            GetOpt getopt = new GetOpt(args, "i:s:h");
            while ((c = getopt.getNextOption()) != -1) {
                switch (c) {
                case 'i':
                    this.inputFile = getopt.getOptionArg();
                    File f = new File(this.inputFile);
                    if (!f.exists()) {
                        usage("input file missing: " + this.inputFile);
                    }
                    break;
                case 's':
                    this.schemaFile = getopt.getOptionArg();
                    break;
                case 'h':
                    usage(null);
                    break;
                default:
                    usage(null);
                }
            }
            if (inputFile == null) {
                usage("error occurred: more arguments should be specified.");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void usage(String message) {
        if (message != null) {
            System.err.println(message);
        }
        System.err.println(USAGE);
        System.exit(1);
    }

    public static void main(String[] args) throws Exception {
        SOSXMLValidator validator = null;
        try {
            validator = new SOSXMLValidator(args);
            validator.parseArguments();
            File dir = new File(validator.inputFile);
            if (dir.isFile()) {
                System.out.print("\nfile [" + validator.inputFile + "]: validation in progress ... ");
                if (validator.schemaFile != null && !validator.schemaFile.isEmpty()) {
                    SOSXMLValidator.validate(validator.inputFile, validator.schemaFile);
                } else {
                    SOSXMLValidator.validate(validator.inputFile);
                }
                System.out.println("ok");
            } else if (dir.isDirectory()) {
                Vector filelist = SOSFile.getFilelist(dir.getAbsolutePath(), "\\.xml$", 0);
                Iterator iterator = filelist.iterator();
                while (iterator.hasNext()) {
                    File currentFile = (File) iterator.next();
                    System.out.print("\nfile [" + currentFile.getAbsolutePath() + "]: validation in progress ... ");
                    if (validator.schemaFile != null && !validator.schemaFile.isEmpty()) {
                        SOSXMLValidator.validate(currentFile.getAbsolutePath(), validator.schemaFile);
                    } else {
                        SOSXMLValidator.validate(currentFile.getAbsolutePath());
                    }
                    System.out.println("ok");
                }
            } else {
                throw new Exception("file not found: " + validator.inputFile);
            }
        } catch (Exception e) {
            System.out.println("");
            validator.usage(e.getMessage());
        }
    }

}
