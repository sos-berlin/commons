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

/** @author Mürüvet Öksüz */
public class SOSSchemaInterpreter implements ContentHandler, ErrorHandler, DTDHandler, EntityResolver {

    private Locator locator;
    private static String content_id = new String();
    private static String dbIniFile = new String();
    private static String scriptName = new String();
    private HashMap hashContentsElements = new HashMap();
    private static HashMap hashTypes = new HashMap();
    private int content_element_id = 0;
    private ArrayList parent = new ArrayList();
    private String parentID4TechnicalInformation = new String("");;
    private boolean bNextParent = false;
    private boolean bParent = false;
    private String error = new String();
    private static BufferedWriter output = null;
    private static String tablePrefix = "INBOUND";
    private String tableContentModel = "CONTENT_MODELS";
    private static String contentTableName = new String("");
    private int content_model_id = 1;
    private int contentElementOrder = 0;
    private boolean bdoc = false;
    private String xsdVersionNumber = new String();
    private SOSOracleConnection conn = null;
    private static SOSStandardLogger sosLogger = null;
    private static HashMap allColumnNamefromTagname = new HashMap();
    private static HashMap allTagnamefromColumnName = new HashMap();
    private static HashMap allTagTypeForTagName = new HashMap();
    private static HashMap allMinOccurForTagName = new HashMap();
    private static HashMap startTag = new HashMap();
    private static boolean collect = true;
    private int lastParentTag = 0;
    private static HashMap allChildrenFromParent = new HashMap();
    private static HashMap tagname2columnname = null;
    private static ArrayList childList = new ArrayList();
    private static String fileName = "";
    private static SOSSchemaInterpreter sample = new SOSSchemaInterpreter();
    private int count = 0;
    private static ArrayList allTags = new ArrayList();

    public SOSSchemaInterpreter(String fileName_, String content_id_, String scriptName_, String dbIniFile_, String contentTableName_,
            String tablePrefix_) throws Exception {
        try {
            fileName = fileName_;
            content_id = content_id_;
            scriptName = scriptName_;
            dbIniFile = dbIniFile_;
            contentTableName = contentTableName_;
            tablePrefix = tablePrefix_;
            sample = new SOSSchemaInterpreter();
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }

    }

    public SOSSchemaInterpreter(String fileName_, String content_id_, String scriptName_, String contentTableName_, String tablePrefix_)
            throws Exception {
        try {
            fileName = fileName_;
            content_id = content_id_;
            scriptName = scriptName_;
            contentTableName = contentTableName_;
            tablePrefix = tablePrefix_;
            sample = new SOSSchemaInterpreter();
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

    public SOSSchemaInterpreter() {

    }

    static public void main(String[] argv) throws Exception {
        try {
            if (argv.length < 5) {
                System.err.println("Usage: <Schemafilename.xsd> <Content_id> <scriptname.sql> <db Ini Filename> <Tablename> ");
                System.exit(1);
            }
            if (argv[0] != null) {
                if (argv[0].indexOf(".xsd") == -1) {
                    System.out.println("There is no XSD-File: " + argv[0]);
                }
            }
            fileName = argv[0].toString();
            if (argv[1] != null) {
                content_id = argv[1].toString();
            }
            if (argv[2] != null) {
                scriptName = argv[2].toString();
            }
            if (argv[3] != null) {
                dbIniFile = argv[3].toString();
            }
            if (argv[4] != null) {
                contentTableName = argv[4].toString();
            }
            if (argv.length == 6) {
                tablePrefix = argv[5].toString();
            }
            sample = new SOSSchemaInterpreter();
            sample.parse();
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

    public void parse() {
        try {
            try {
                output = new BufferedWriter((new FileWriter(scriptName)));
            } catch (IOException e) {
                System.out.println("Error by creating insert script " + e);
            }

            SAXParser parser = new SAXParser();
            parser.setContentHandler(sample);
            parser.setEntityResolver(sample);
            parser.setDTDHandler(sample);
            parser.setErrorHandler(sample);
            try {
                parser.parse(createURL(fileName).toString());
            } catch (SAXParseException e) {
                if (!"Element 'xsd:schema' used but not declared.".equals(e.getMessage())) {
                    System.out.println(e.getMessage());
                }
            } catch (SAXException e) {
                if (!(e.getMessage().indexOf("Element 'xsd:schema' used but not declared.") > 0)) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            if (!(e.getMessage().indexOf("Element 'xsd:schema' used but not declared.") > 0)) {
                System.out.println(e.toString());
            }
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                System.out.println("Error while Closing schema.sql");
            }
        }

    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    public void startDocument() {
        try {
            sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG9);
            parent.add("0");
            getConnection(dbIniFile);
            getType();
            getContentElementId();
            getContentModelID();
        } catch (SQLException e) {
            System.out.println("Error in Schema.startDocument()" + e);
            error = "Error in Schema.startDocument()" + e;
        } catch (Exception e) {
            System.out.println("Error in Schema.startDocument()" + e);
            error = "Error in Schema.startDocument()" + e;
        }
    }

    public void endDocument() throws SAXException {
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

    public void startElement(String string1, String name, String string2, Attributes atts) throws SAXException {
        try {
            count++;
            if (count == 46) {
                System.out.println("test");
            }
            if (!error.isEmpty()) {
                throw new SAXException(error);
            }
            if ("element".equalsIgnoreCase(name)) {
                if (hashContentsElements.size() > 1) {
                    writeInDB();
                }
                initContentsElements();
            }
            if ("documentation".equalsIgnoreCase(name)) {
                bdoc = true;
            }
            if ("simpleType".equalsIgnoreCase(name)) {
                hashContentsElements.put("LEAF", "1");
                bParent = false;
            }
            if ("complexType".equalsIgnoreCase(name)) {
                bParent = true;
                hashContentsElements.put("LEAF", "0");
                parent.add(String.valueOf(content_element_id));
                if (bNextParent) {
                    parentID4TechnicalInformation = parent.get(parent.size() - 1).toString();
                    bNextParent = false;
                }
            }
            for (int i = 0; i < atts.getLength(); i++) {
                String aname = atts.getLocalName(i);
                String value = atts.getValue(i);
                if ("TechnicalInformation".equalsIgnoreCase(value)) {
                    bNextParent = true;
                }
                if ("totalDigits".equalsIgnoreCase(name)) {
                    hashContentsElements.put("TOTAL_DIGITS", value);
                }
                if ("fractionDigits".equalsIgnoreCase(name)) {
                    hashContentsElements.put("FRACTION_DIGITS", value);
                }
                if ("version".equalsIgnoreCase(aname)) {
                    xsdVersionNumber = value;
                    try {
                        output.write(getInsertsIOContentModells() + "\n");
                    } catch (Exception e) {
                        System.out.println("Error while write in insert script: " + e);
                    }
                }
                if ("name".equalsIgnoreCase(aname)) {
                    hashContentsElements.put("TAG_NAME", value);
                    if (tagname2columnname != null && tagname2columnname.get(value) != null) {
                        hashContentsElements.put("COLUMN_NAME", tagname2columnname.get(value));
                    } else {
                        hashContentsElements.put("COLUMN_NAME", value.toUpperCase());
                    }
                } else if ("restriction".equalsIgnoreCase(name) && "base".equalsIgnoreCase(aname)) {
                    String stype = value.substring(4, value.length());
                    int itype = 1;
                    if (stype != null && hashTypes.containsKey(stype.toLowerCase())) {
                        itype = getColumnType((Integer.parseInt(hashTypes.get(stype.toLowerCase()).toString())));
                    }
                    hashContentsElements.put("COLUMN_TYPE", String.valueOf(itype));
                    hashContentsElements.put("TAG_TYPE", hashTypes.get(stype.toLowerCase()));
                } else if ("maxLength".equalsIgnoreCase(name) && "value".equalsIgnoreCase(aname)) {
                    hashContentsElements.put("COLUMN_LENGTH", value);
                    hashContentsElements.put("TAG_MAXLENGTH", value);
                } else if ("length".equalsIgnoreCase(name) && "value".equalsIgnoreCase(aname)) {
                    hashContentsElements.put("COLUMN_LENGTH", value);
                    hashContentsElements.put("TAG_MAXLENGTH", value);
                } else if ("element".equalsIgnoreCase(name) && "minOccurs".equalsIgnoreCase(aname)) {
                    hashContentsElements.put("MIN_OCCURS", value);
                } else if ("element".equalsIgnoreCase(name) && "maxOccurs".equalsIgnoreCase(aname)) {
                    if ("unbounded".equals(value)) {
                        hashContentsElements.put("MAX_OCCURS", "-1");
                    } else {
                        hashContentsElements.put("MAX_OCCURS", value);
                    }
                }
            }
        } catch (Exception e) {
            throw new SAXException("\n -> ..error in SchemaInterpreter.starteElement() " + e.getMessage(), e);
        }
    }

    public void endElement(String string1, String name, String string2) throws SAXException {
        try {
            if ("complexType".equalsIgnoreCase(name)) {
                writeInDB();
                parent.remove(parent.size() - 1);
                hashContentsElements.put("PARENT", parent.get(parent.size() - 1));
            }
        } catch (Exception e) {
            throw new SAXException("\n -> ..error in SchemaInterpreter.endElement() " + e.getMessage(), e);
        }
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
                throw new SAXException("Fehler in SAXParseXML.createURL()" + e.getMessage(), e);
            }
        }
        return url;
    }

    private void writeInDB() throws Exception {
        String insStr = "";
        try {
            if (hashContentsElements.get("TAG_NAME") == null) {
                return;
            }
            insStr = getInsertString();
            output.write(insStr + "\n");
            hashContentsElements.clear();
        } catch (Exception e) {
            throw new Exception("\n ->..error in SchemaInterpreter.writeInDB() " + e.getMessage(), e);
        }
    }

    private String getInsertString() throws Exception {
        String insStr = new String("");
        String sContentID = "";
        HashMap childHash = new HashMap();
        Object jparent = "0";
        try {
            insStr =
                    "insert into CONTENT_TAGS" + " ( " + "\"CONTENT_ELEMENT_ID\", " + "\"LEAF\", " + "\"CONTENT_MODEL_ID\", " + "\"CONTENT_ID\", "
                            + "\"CONTENT_ELEMENT_ORDER\", " + "\"CONTENT_IS_NODE\", " + "\"PARENT\", " + "\"TAG_NAME\", " + "\"TAG_TYPE\", "
                            + "\"TAG_MAXLENGTH\", " + "\"TOTAL_DIGITS\", " + "\"FRACTION_DIGITS\", " + "\"MIN_OCCURS\", " + "\"MAX_OCCURS\", "
                            + "\"DESCRIPTION\" " + ") values ( " + content_element_id++ + "," + "" + hashContentsElements.get("LEAF") + ", " + ""
                            + content_model_id + ",";
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
                if (allChildrenFromParent.size() == 1 && startTag.isEmpty()) {
                    startTag.put("content_element_id", String.valueOf(content_element_id - 1));
                    startTag.put("tag_name", hashContentsElements.get("TAG_NAME"));
                }
            }
            insStr =
                    insStr + " '" + hashContentsElements.get("TAG_NAME") + "', " + "" + hashContentsElements.get("TAG_TYPE") + ", " + ""
                            + hashContentsElements.get("TAG_MAXLENGTH") + ", " + "" + hashContentsElements.get("TOTAL_DIGITS") + ", " + ""
                            + hashContentsElements.get("FRACTION_DIGITS") + ", " + "" + hashContentsElements.get("MIN_OCCURS") + ", " + ""
                            + hashContentsElements.get("MAX_OCCURS") + ", " + "'" + hashContentsElements.get("DESCRIPTION") + "');";
            if (collect && sContentID.equals(content_id)) {
                childHash = new HashMap();
                childHash.put("tag_name", hashContentsElements.get("TAG_NAME"));
                childHash.put("tag_type", hashContentsElements.get("TAG_TYPE"));
                if ("0".equals(hashContentsElements.get("TAG_TYPE"))) {
                    childHash.put("groupable", "0");
                } else {
                    childHash.put("groupable", "1");
                }
                childHash.put("parent", jparent);
                childHash.put("content_element_id", String.valueOf(content_element_id - 1));
                childHash.put("tag_maxlength", hashContentsElements.get("TAG_MAXLENGTH"));
                childHash.put("tag_type", hashContentsElements.get("TAG_TYPE"));
                childHash.put("total_digits", hashContentsElements.get("TOTAL_DIGITS"));
                childHash.put("min_occurs", hashContentsElements.get("MIN_OCCURS"));
                childHash.put("max_occurs", hashContentsElements.get("MAX_OCCURS"));
                childHash.put("column_name", hashContentsElements.get("COLUMN_NAME"));
                childHash.put("content_id", sContentID);
                allTags.add(childHash.clone());
            }
        } catch (Exception e) {
            throw new Exception("\n ->..error in  " + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
        return insStr;
    }

    private String getInsertsIOContentModells() throws Exception {
        String insStr = new String();
        try {
            insStr =
                    "insert into CONTENT_TAGS" + " ( " + "\"CONTENT_MODEL_ID\", " + "\"CONTENT_ID\", " + "\"CONTENT_ELEMENT_ORDER\", "
                            + "\"CONTENT_IS_NODE\" , " + "\"SCHEMA_NAME\", " + "\"DESCRIPTION\"" + ") values ( " + content_model_id + "," + "'"
                            + content_id + "'," + "'" + tablePrefix.toLowerCase() + "'," + "'" + xsdVersionNumber + "'," + "'');";
        } catch (Exception e) {
            throw e;
        }
        return insStr;
    }

    private void getType() throws Exception {
        String selStr = "";
        try {
            if (conn != null) {
                selStr =
                        " SELECT LOWER(\"TITLE\"), \"VALUE\" " + " FROM SETTINGS " + " WHERE \"APPLICATION\" = 'standard_tables' "
                                + " AND  \"SECTION\" = 'tag_types' " + " AND \"VALUE\" <> ' ' ORDER BY \"VALUE\" ";
                hashTypes.putAll(conn.getArrayAsProperties(selStr));
            }
        } catch (Exception e) {
            System.out.println("ERROR in Schema.getTypes()" + e + " Statement called: " + selStr);
            throw (e);
        }
    }

    private void getContentElementId() throws Exception {
        try {
            if (conn != null) {
                String result = conn.getSingleValue(" SELECT MAX(\"CONTENT_ELEMENT_ID\") FROM \"CONTENT_TAGS\"");
                if (result != null && !result.isEmpty()) {
                    content_element_id = Integer.parseInt(result);
                }
            } else {
                content_element_id = 1;
            }

        } catch (Exception e) {
            System.out.println("ERROR in Schema.getContentElementId()" + e);
            throw (e);
        }
    }

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

    private void getConnection(String iniFile) {
        try {
            if (iniFile == null || (iniFile != null && iniFile.isEmpty())) {
                return;
            }
            conn = new SOSOracleConnection(iniFile, sosLogger);
            conn.connect();
        } catch (Exception e) {
            System.err.println("Error in TestHistory.getcoonetion: " + e);
        }
    }

    private int getColumnType(int tagType) {
        int retVal = -1;
        switch (tagType) {
        case 1:
            retVal = 1;
            break;
        case 2:
            retVal = 2;
            hashContentsElements.put("TOTAL_DIGITS", "16");
            hashContentsElements.put("FRACTION_DIGITS", "0");
            break;
        case 3:
            retVal = 3;
            break;
        case 4:
            retVal = 2;
            hashContentsElements.put("TOTAL_DIGITS", "16");
            hashContentsElements.put("FRACTION_DIGITS", "4");
            break;
        case 5:
            retVal = 3;
            break;
        }
        return retVal;
    }

    private void getContentModelID() throws Exception {
        try {
            if (conn != null) {
                String selStr = conn.getSingleValue("SELECT MAX(\"CONTENT_MODEL_ID\") FROM " + tableContentModel);
                if (selStr != null && !selStr.isEmpty()) {
                    content_model_id = Integer.parseInt(selStr);
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR in Schema.getContentModelID()" + e);
            throw (e);
        }
    }

    public HashMap getAllColumnNamefromTagname() {
        return allColumnNamefromTagname;
    }

    public HashMap getAllMinOccurForTagName() {
        return allMinOccurForTagName;
    }

    public HashMap getAllTagnamefromColumnName() {
        return allTagnamefromColumnName;
    }

    public HashMap getAllTagTypeForTagName() {
        return allTagTypeForTagName;
    }

    public HashMap getAllChildrenFromParent() {
        return allChildrenFromParent;
    }

    public void mapTagname2columnname(HashMap tagname2columnname) {
        SOSSchemaInterpreter.tagname2columnname = tagname2columnname;
    }

    public void setCollect(boolean collect) {
        SOSSchemaInterpreter.collect = collect;
    }

    public HashMap getStartTag() {
        return startTag;
    }

    public ArrayList getAllTags() {
        return allTags;
    }

    private void analyzeListOfAllTags() throws Exception {
        HashMap childHash = new HashMap();
        HashMap childHash2 = new HashMap();
        ArrayList childList = new ArrayList();
        ArrayList list = null;
        try {
            list = getAllTags();
            for (int i = 0; i < list.size(); i++) {
                childHash = (HashMap) list.get(i);
                allMinOccurForTagName.put(childHash.get("tag_name"), childHash.get("min_occurs"));
                allColumnNamefromTagname.put(childHash.get("tag_name"), childHash.get("column_name"));
                allTagnamefromColumnName.put(childHash.get("column_name"), childHash.get("tag_name"));
                allTagTypeForTagName.put(childHash.get("tag_name"), childHash.get("tag_type"));
            }
            ArrayList parentList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                childHash = (HashMap) list.get(i);
                if ("0".equals(childHash.get("tag_type"))) {
                    parentList.add(childHash.get("content_element_id"));
                }
            }
            Object parentId = "0";
            for (int i = 0; i < parentList.size(); i++) {
                parentId = parentList.get(i);
                for (int j = 0; j < list.size(); j++) {
                    childHash = (HashMap) list.get(j);
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
                if (allChildrenFromParent.isEmpty()) {
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
            throw new Exception("\n ->..error in getTestDaten() " + e.getMessage(), e);
        }
    }

    public HashMap getTypes() {
        return hashTypes;
    }

    public void setTypes(HashMap hashTypes_) {
        hashTypes = hashTypes_;
    }

}