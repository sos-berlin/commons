package sos.xml.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import sos.util.SOSClassUtil;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

public class SOSDOMParserXML {

    private Map<String,String> hashTag = new HashMap<String,String>();
    private List<String> insertStatement = new ArrayList<String>();
    private List<String> listOfTags = new ArrayList<String>();
    private List<Map<String,String>> listOfXMLPath = new ArrayList<Map<String,String>>();
    private Map<String,Map<String,String>> listOfAttribut = new HashMap<String,Map<String,String>>();
    private String tableName = "tabellenname";
    private Map<String,String> mappingTagNames = new HashMap<String,String>();
    private boolean removeParents = true;
    private Map<String,String> defaultFields = new HashMap<String,String>();
    private String outputScripFilename = "";
    private BufferedWriter output = null;
    private boolean counter = false;
    private int count = 0;
    private SOSLogger sosLogger = null;
    private String encoding = "";
    private int depth = 0;

    public SOSDOMParserXML(SOSLogger sosLogger) throws Exception {
        try {
            this.sosLogger = sosLogger;
        } catch (Exception e) {
            throw new Exception("\n ..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public boolean parse(byte[] bytesRead) throws Exception {
        return parse(null, null, bytesRead);
    }

    public boolean parse(InputSource ipSource) throws Exception {
        return parse(null, ipSource, null);
    }

    public boolean parse(String fileName) throws Exception {
        return parse(fileName, null, null);
    }

    public boolean parse(String fileName, InputSource ipSource, byte[] bytesRead) throws Exception {
        try {
            DocumentBuilderFactory docBFac;
            DocumentBuilder docBuild;
            Document doc = null;
            docBFac = DocumentBuilderFactory.newInstance();
            docBuild = docBFac.newDocumentBuilder();
            if (fileName != null) {
                doc = docBuild.parse(fileName);
            } else if (ipSource != null) {
                doc = docBuild.parse(ipSource);
            } else if (bytesRead != null) {
                Reader reader = (Reader) new CharArrayReader(new String(bytesRead).toCharArray());
                ipSource = new org.xml.sax.InputSource(reader);
                doc = docBuild.parse(ipSource);
            }
            parseDocument(doc);
            writeInsertStatement();
            sosLogger.debug4("encoding wurde aus der XML-Datei bestimmt: " + getEncoding(fileName, bytesRead));
            return true;
        } catch (Exception e) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + e.toString());
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    public void parseDocument(Document doc) throws Exception {
        try {
            NodeList nl = doc.getElementsByTagName("*");
            Node n;
            String allParentname = "";
            String lastTagname = "";
            String lastTagnameAndValue = "";
            int countOfRepeat = 1;
            String curTagnameAndValue = "";
            for (int i = 0; i < nl.getLength(); i++) {
                n = nl.item(i);
                if (lastTagname.equals(n.getParentNode().getNodeName()) && removeParents) {
                    listOfTags.remove(listOfTags.size() - 1);
                }
                if (depth == 0) {
                    allParentname = n.getNodeName();
                } else if (depth == 99) {
                    allParentname = getParentName(n) + "_@_" + n.getNodeName();
                } else {
                    allParentname = n.getParentNode().getNodeName() + "_@_" + n.getNodeName();
                }
                getAttributs(n);
                curTagnameAndValue = allParentname + "=" + getChildText(n);
                if (curTagnameAndValue.equals(lastTagnameAndValue)) {
                    countOfRepeat++;
                } else {
                    if (countOfRepeat > 1) {
                        sosLogger.debug5("..count of repeat last Tagname and Tagvalue: " + countOfRepeat);
                    }
                    sosLogger.debug5(curTagnameAndValue);
                    countOfRepeat = 1;
                }
                listOfTags.add(curTagnameAndValue);
                lastTagnameAndValue = curTagnameAndValue;
                lastTagname = n.getNodeName();
            }
            if (countOfRepeat > 1) {
                sosLogger.debug5("..count of repeat last Tagname and Tagvalue: " + countOfRepeat);
            }
        } catch (Exception ec) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + ec);
        }
    }

    public void getAttributs(Node n) throws Exception {
        Map<String,String> attr = new HashMap<String,String>();
        try {
            NamedNodeMap nm = n.getAttributes();
            attr.put("tagname", n.getNodeName());
            for (int i = 0; i < nm.getLength(); i++) {
                attr.put(nm.item(i).getNodeName(), nm.item(i).getNodeValue());
            }
            listOfAttribut.put(n.getNodeName(), attr);
        } catch (Exception ec) {
            throw new Exception("\n -> ..error in " + SOSClassUtil.getMethodName() + " " + ec);
        }
    }

    private String getParentName(Node node) throws Exception {
        String retVal = "";
        List<String> parentName = new ArrayList<String>();
        try {
            while (node.getParentNode() != null) {
                parentName.add(node.getParentNode().getNodeName());
                node = node.getParentNode();
            }
            for (int i = parentName.size() - 1; i != -1; i--) {
                retVal = retVal + parentName.get(i) + "_";
            }
            return retVal;
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    protected void writeInsertStatement() throws Exception {
        String[] splitTag = null;
        String tags = "";
        try {
            for (int i = 0; i < listOfTags.size(); i++) {
                tags = listOfTags.get(i);
                splitTag = tags.split("=");
                if (hashTag.containsKey(splitTag[0])) {
                    writeStatement();
                    updatelistOfTags(splitTag[0], i);
                    i = -1;
                } else {
                    if (splitTag.length == 2) {
                        hashTag.put(splitTag[0], tags.substring(tags.indexOf(splitTag[1])));
                    } else {
                        hashTag.put(splitTag[0], "NULL");
                    }
                }
            }
            writeStatement();
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

    private void updatelistOfTags(String tagname, int position) throws Exception {
        String[] split = null;
        boolean breakOK = true;
        try {
            for (int i = 0; i < listOfTags.size() && breakOK; i++) {
                split = listOfTags.get(i).split("=");
                if (split[0].equals(tagname)) {
                    for (int j = i; j < position; j++) {
                        listOfTags.remove(i);
                        breakOK = false;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

    private void writeStatement() throws Exception {
        try {
            Map<String,String> currHash = new HashMap<String,String>();
            Iterator<String> keys = hashTag.keySet().iterator();
            Iterator<String> vals = hashTag.values().iterator();
            String key = "";
            String val = "";
            String insStr = " insert into " + tableName + " ( ";
            String insStr2 = " values ( ";
            Iterator<String> dkeys = defaultFields.keySet().iterator();
            Iterator<String> dvals = defaultFields.values().iterator();
            String dkey = "";
            String dval = "";
            while (dkeys.hasNext()) {
                dkey = dkeys.next();
                dval = dvals.next();
                insStr = insStr + dkey;
                insStr2 = insStr2 + "'" + dval + "'";
                if (dkeys.hasNext()) {
                    insStr = insStr + ", ";
                    insStr2 = insStr2 + ", ";
                }
                if (key != null && !key.isEmpty()) {
                    currHash.put(key, val);
                }
            }
            if (!defaultFields.isEmpty()) {
                insStr = insStr + ", ";
                insStr2 = insStr2 + ", ";
            }
            if (this.isCounter()) {
                insStr = insStr + " counter";
                insStr2 = insStr2 + " " + this.count++;
                if (key != null && !key.isEmpty()) {
                    currHash.put(key, val);
                }
                insStr = insStr + ", ";
                insStr2 = insStr2 + ", ";
            }
            while (keys.hasNext()) {
                key = keys.next();
                val = vals.next();
                if (mappingTagNames.containsKey(key)) {
                    key = mappingTagNames.get(key);
                }
                insStr = insStr + key;
                if ("NULL".equals(val)) {
                    insStr2 = insStr2 + val;
                } else {
                    insStr2 = insStr2 + "'" + val + "'";
                }
                if (keys.hasNext()) {
                    insStr = insStr + ", ";
                    insStr2 = insStr2 + ", ";
                }
                if (key != null && !key.isEmpty()) {
                    currHash.put(key, val);
                }
            }
            insStr = insStr + " ) " + insStr2 + " ) ";
            sosLogger.debug9(" InsertStament: " + insStr);
            if (output != null) {
                output.write(insStr + ";\n");
            }
            insertStatement.add(insStr);
            listOfXMLPath.add(currHash);
            hashTag.clear();
        } catch (Exception e) {
            throw new Exception("\n ->..error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    private String getChildText(Node node) throws Exception {
        NodeList list = node.getChildNodes();
        StringBuffer ret = new StringBuffer();
        try {
            if (list != null) {
                for (int i = 0; i < list.getLength(); i++) {
                    Node son = list.item(i);
                    if (son.getNodeType() == Node.TEXT_NODE) {
                        ret.append(son.getNodeValue().trim());
                    }
                    if (son.getNodeType() == Node.CDATA_SECTION_NODE) {
                        CDATASection cs = (CDATASection) son;
                        ret.append(cs.getData());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error in getChildText : " + e.getMessage(), e);
        }
        return ret.toString();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<String> getInsertStatement() {
        return insertStatement;
    }

    public Map<String,String> getMappingTagNames() {
        return mappingTagNames;
    }

    public void setMappingTagNames(Map<String,String> mappingTagNames) {
        this.mappingTagNames = mappingTagNames;
    }

    public boolean isRemoveParents() {
        return removeParents;
    }

    public void setRemoveParents(boolean removeParents) {
        this.removeParents = removeParents;
    }

    public List<Map<String,String>> getListOfXMLPath() {
        return listOfXMLPath;
    }

    public Map<String,String> getDefaultFields() {
        return defaultFields;
    }

    public void setDefaultFields(Map<String,String> defaultFields) {
        this.defaultFields = defaultFields;
    }

    public boolean isCounter() {
        return counter;
    }

    public void setCounter(boolean counter) {
        this.counter = counter;
    }

    public String getOutputScripFilename() {
        return outputScripFilename;
    }

    public void setOutputScripFilename(String outputScripFilename) throws Exception {
        try {
            this.outputScripFilename = outputScripFilename;
            if (outputScripFilename != null && !outputScripFilename.isEmpty()) {
                output = new BufferedWriter(new FileWriter(outputScripFilename));
            }
        } catch (Exception e) {
            throw new Exception("\n ..error in " + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

    public Map<String,Map<String,String>> getListOfAttribut() {
        return listOfAttribut;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getEncoding(String fileName, byte[] bytesRead) throws Exception {
        String line = "";
        String[] split = null;
        BufferedReader f = null;
//        InputSource ipSource = null;
        try {

            if (fileName != null) {
                f = new BufferedReader(new FileReader(new File(fileName)));
//            } else if (ipSource != null) {
//                sosLogger.warn("..could not read the encoding for InputSource.");
//                return "";
            } else if (bytesRead != null) {
                Reader readers = (Reader) new CharArrayReader(new String(bytesRead).toCharArray());
                f = new BufferedReader(readers);
            }
            line = f.readLine();
            split = line.split(" ");
            for (int i = 0; i < split.length; i++) {
                if (split[i].indexOf("encoding") > -1) {
                    encoding = split[i].substring(split[i].indexOf("\"") + 1, split[i].lastIndexOf("\""));
                    return encoding;
                }
            }
            return encoding;
        } catch (Exception e) {
            throw new Exception("\n ->..error in" + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        try {
            SOSStandardLogger sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG9);
            SOSDOMParserXML parser = new SOSDOMParserXML(sosLogger);
            parser.setDepth(0);
            parser.setRemoveParents(true);
            parser.parse("J:/E/java/mo/sos.stacks/servingxml/samples/sos/resources/mapping.xml");
            System.out.println("-------Ergebnisse-------------------------------------------------");
            System.out.println("-------Alle Tag auslesen:----------------------");
            Map<String,String> res = null;
            List<Map<String,String>> tags = parser.getListOfXMLPath();
            for (int i = 0; i < tags.size(); i++) {
                res = (Map<String,String>) tags.get(i);
                Iterator<String> keys = res.keySet().iterator();
                Iterator<String> vals = res.values().iterator();
                while (keys.hasNext()) {
                    System.out.println(keys.next() + "=" + vals.next());
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }
}