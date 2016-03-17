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

/** @author Mueruevet Oeksuez */
public class SOSSAXParserXML implements ContentHandler, ErrorHandler, DTDHandler, EntityResolver {

    private Locator locator;
    private static SOSStandardLogger sosLogger;
    private static String tableName = new String();
    private HashMap insertTags = new HashMap();
    private String lastTagname = "";
    private String lastTagvalue = "";
    private ArrayList listOfTags = null;
    private boolean startTag = true;
    private String errorText = "";
    private static HashMap defaultFields = new HashMap();
    private SOSString sosString = null;
    private static boolean counter = false;
    private static int count = 0;
    private String outputScripFilename = "";
    private static BufferedWriter output = null;
    private static HashMap mappingTagNames = new HashMap();
    private ArrayList parent = new ArrayList();
    private static SOSConnection connection = null;
    private static boolean checkValidate = false;

    public SOSSAXParserXML(SOSStandardLogger spooler_log_) {
        sosLogger = spooler_log_;
    }

    public SOSSAXParserXML() {
    }

    public void parseXMLFile(String xmlFileName) throws Exception {
        try {
            if (xmlFileName.length() == 1) {
                throw new Exception("xml Filename not exist!!");
            }
            SAXParser parser;
            SOSSAXParserXML sample;
            try {
                sample = new SOSSAXParserXML();
                parser = new SAXParser();
            } catch (Exception e) {
                sosLogger.warn("SAXParser Not found " + e);
                throw new Exception("SAXParser Not found " + e);
            }
            if (isCheckValidate()) {
                parser.setFeature("http://xml.org/sax/features/validation", true);
            }
            parser.setContentHandler(sample);
            parser.setEntityResolver(sample);
            parser.setDTDHandler(sample);
            parser.setErrorHandler(sample);
            try {
                parser.parse(createURL(xmlFileName).toString());
            } catch (SAXParseException e) {
                sosLogger.debug5("Exception by Parsing/Validation - SAXParseXML.parseXMLFile() " + e);
                throw new SAXException("Error in SAXParseXML.parseXMLFile() 1" + e);
            }
        } catch (Exception e) {
            sosLogger.debug5("Exception by Parsing/Validation - SAXParseXML.parseXMLFile() " + e);
            throw new Exception("Fehler im SAXParseXML.parseXMLFile()" + e);
        }
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startPrefixMapping(String string, String string1) throws SAXException {

    }

    public void endPrefixMapping(String string) throws SAXException {

    }

    public void skippedEntity(String string) throws SAXException {

    }

    public void startDocument() {
        sosString = new SOSString();
        listOfTags = new ArrayList();
        count = 0;
    }

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

    public void startElement(String string1, String name, String string2, Attributes atts) throws SAXException {
        try {
            if (listOfTags.contains(name)) {
                writeInsertStatement();
                updateListOfTags(name);
            }
            if (startTag) {
                parent.add(lastTagname);
                sosLogger.debug5("~~~~~~~~~~~~~~~~> mein Vaterknoten ist ein Komplextyp: " + lastTagname);
            } else {
                listOfTags.add(lastTagname);
                insertTags.put(lastTagname, lastTagvalue);
                sosLogger.debug5(lastTagname + "=" + lastTagvalue);
                sosLogger.debug5("startelement: " + name);
                for (int i = 0; i < atts.getLength(); i++) {
                    System.out.println(atts.getQName(i) + " = " + atts.getValue(i));
                }
            }
            startTag = true;
            lastTagname = name;
            lastTagvalue = "";
        } catch (Exception e) {
            throw new SAXException("\n -> error in SAXParseXML.startelement " + e);
        }
    }

    public void endElement(String string1, String name, String string2) throws SAXException {
        try {
            if (!errorText.isEmpty()) {
                throw new SAXException(errorText);
            }
            if (name.equals(parent.get(parent.size() - 1))) {
                sosLogger.debug5("~~~~~~~~~~~~~~~~> Vaterknoten wird jetzt gelöscht: " + name);
                parent.remove(parent.size() - 1);
            }
            sosLogger.debug5("endelement: " + name);
            startTag = false;
        } catch (Exception e) {
            throw new SAXException("\n ->..error in SAXParseXML.endElement " + e.toString());
        }
    }

    public void characters(char[] cbuf, int start, int len) {
        try {
            String ch = new String(cbuf, start, len);
            ch = ch.replaceAll("'", "''");
            ch = ch.replaceAll("&", "&' ||'");
            ch = ch.replaceAll("\n", "");
            ch = ch.replaceAll("\t", "");
            sosLogger.debug5("Char: " + ch);
            if (startTag) {
                lastTagvalue = ch;
            }
        } catch (Exception e) {
            errorText = "\n -> ..error in SAXParseXML.characters " + e;
        }
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
        throw new SAXException(e.getMessage());
    }

    public void fatalError(SAXParseException e) throws SAXException {
        throw new SAXException(e.getMessage());
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
                throw new SAXException("Fehler in SAXParseXML.createURL()" + e);
            }
        }
        return url;
    }

    private void updateListOfTags(String tagname) throws Exception {
        try {
            for (int i = listOfTags.size() - 1; i != 0; i--) {
                if (listOfTags.get(i).equals(tagname)) {
                    insertTags.remove(listOfTags.get(i));
                    listOfTags.remove(i);
                    break;
                } else {
                    insertTags.remove(listOfTags.get(i));
                    listOfTags.remove(i);
                }
            }
        } catch (Exception e) {
            throw new Exception("\n ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private void writeInsertStatement() throws Exception {
        try {
            HashMap currHash = new HashMap();
            Iterator keys = this.insertTags.keySet().iterator();
            Iterator vals = insertTags.values().iterator();
            String key = "";
            String val = "";
            String insStr = " insert into " + tableName + " ( ";
            String insStr2 = " values ( ";
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
            if (this.isCounter()) {
                if (!insStr.isEmpty()) {
                    insStr = insStr + ", ";
                    insStr2 = insStr2 + ", ";
                }
                if (mappingTagNames.containsKey("counter")) {
                    insStr = insStr + mappingTagNames.get("counter");
                } else {
                    insStr = insStr + " counter ";
                }
                insStr2 = insStr2 + count++;
                currHash.put(key, val);
            }
            if (insStr.length() > 16) {
                insStr = insStr + ", ";
                insStr2 = insStr2 + ", ";
            }
            while (keys.hasNext()) {
                key = sosString.parseToString(keys.next());
                val = sosString.parseToString(vals.next());
                if (mappingTagNames.containsKey(sosString.parseToString(key).toLowerCase())) {
                    key = sosString.parseToString(mappingTagNames, sosString.parseToString(key).toLowerCase());
                }
                insStr = insStr + key;
                insStr2 = insStr2 + "'" + val + "'";
                if (keys.hasNext()) {
                    insStr = insStr + ", ";
                    insStr2 = insStr2 + ", ";
                }
            }
            insStr = insStr + " ) " + insStr2 + " ) ";
            sosLogger.debug4(" \n~~~~~~~~~~~~~~~~~~InsertStament: " + insStr + "\n");
            if (output != null) {
                output.write(insStr + ";\n");
            }
            if (connection != null) {
                connection.executeQuery(insStr);
            }
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public HashMap getDefaultFields() {
        return defaultFields;
    }

    public void setDefaultFields(HashMap defaultFields_) {
        defaultFields = defaultFields_;
    }

    public boolean isCounter() {
        return counter;
    }

    public void setCounter(boolean counter_) {
        counter = counter_;
    }

    public String getOutputScripFilename() {
        return outputScripFilename;
    }

    public void setOutputScripFilename(String outputScripFilename) throws Exception {
        try {
            this.outputScripFilename = outputScripFilename;
            if (outputScripFilename != null && !outputScripFilename.isEmpty()) {
                output = new BufferedWriter((new FileWriter(outputScripFilename)));
            }
        } catch (Exception e) {
            throw new Exception("\n ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public HashMap getMappingTagNames() {
        return mappingTagNames;
    }

    public void setMappingTagNames(HashMap mappingTagNames_) throws Exception {
        try {
            Iterator keys = mappingTagNames_.keySet().iterator();
            Iterator vals = mappingTagNames_.values().iterator();
            while (keys.hasNext()) {
                mappingTagNames.put(keys.next().toString().toLowerCase(), vals.next());
            }
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName_) {
        tableName = tableName_;
    }

    public SOSConnection getConnection() {
        return connection;
    }

    public void setConnection(SOSConnection connection_) {
        connection = connection_;
    }

    public boolean isCheckValidate() {
        return checkValidate;
    }

    public void setCheckValidate(boolean checkValidate) {
        SOSSAXParserXML.checkValidate = checkValidate;
    }

    public int getCountOfInsertStatement() {
        return count;
    }

    public static void main(String[] args) {
        try {
            sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG9);
            SOSSAXParserXML saxXML = new SOSSAXParserXML(sosLogger);
            saxXML.setOutputScripFilename("C:/temp/saxscript.sql");
            saxXML.parseXMLFile("C:/temp/b.xml");
            sosLogger.debug("es wurden insgesamt " + saxXML.getCountOfInsertStatement() + " insert Statement erstellt");
        } catch (Exception e) {
            System.err.print(e);
        } finally {
            try {
                connection.commit();
                connection.disconnect();
            } catch (Exception ex) {
                // no exception handling
            }
        }
    }

}
