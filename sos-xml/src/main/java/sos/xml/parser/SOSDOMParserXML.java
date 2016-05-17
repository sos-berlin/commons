package sos.xml.parser;

import java.net.URL;
import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.CDATASection;
import org.xml.sax.InputSource;
import sos.util.*;
import java.net.MalformedURLException;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SOSDOMParserXML {

    private HashMap hashTag = new HashMap();
    private ArrayList insertStatement = new ArrayList();
    private ArrayList listOfTags = new ArrayList();
    private ArrayList listOfXMLPath = new ArrayList();
    private HashMap listOfAttribut = new HashMap();
    private SOSString sosString = null;
    private String tableName = "tabellenname";
    private HashMap mappingTagNames = new HashMap();
    private boolean removeParents = true;
    private HashMap defaultFields = new HashMap();
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
            sosString = new SOSString();
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
        HashMap attr = new HashMap();
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
        ArrayList parentName = new ArrayList();
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
                tags = sosString.parseToString(listOfTags.get(i));
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
                split = sosString.parseToString(listOfTags.get(i)).split("=");
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
            HashMap currHash = new HashMap();
            Iterator keys = hashTag.keySet().iterator();
            Iterator vals = hashTag.values().iterator();
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
                key = sosString.parseToString(keys.next());
                val = sosString.parseToString(vals.next());
                if (mappingTagNames.containsKey(key)) {
                    key = sosString.parseToString(mappingTagNames.get(key));
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
            listOfXMLPath.add(currHash.clone());
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

    public ArrayList getInsertStatement() {
        return insertStatement;
    }

    public HashMap getMappingTagNames() {
        return mappingTagNames;
    }

    public void setMappingTagNames(HashMap mappingTagNames) {
        this.mappingTagNames = mappingTagNames;
    }

    public boolean isRemoveParents() {
        return removeParents;
    }

    public void setRemoveParents(boolean removeParents) {
        this.removeParents = removeParents;
    }

    public ArrayList getListOfXMLPath() {
        return listOfXMLPath;
    }

    private URL createURL(String fileName) throws Exception {
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
                throw new Exception("\n ..error in " + SOSClassUtil.getMethodName() + " Cannot create url for: " + fileName);

            }
        }
        return url;
    }

    public HashMap getDefaultFields() {
        return defaultFields;
    }

    public void setDefaultFields(HashMap defaultFields) {
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

    public HashMap getListOfAttribut() {
        return listOfAttribut;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getEncoding(String fileName, byte[] bytesRead) throws Exception {
        String line = "";
        String[] split = null;
        BufferedReader f = null;
        InputSource ipSource = null;
        try {

            if (fileName != null) {
                f = new BufferedReader(new FileReader(new File(fileName)));
            } else if (ipSource != null) {
                sosLogger.warn("..could not read the encoding for InputSource.");
                return "";
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
            HashMap res = null;
            ArrayList tags = parser.getListOfXMLPath();
            for (int i = 0; i < tags.size(); i++) {
                res = (HashMap) tags.get(i);
                Iterator keys = res.keySet().iterator();
                Iterator vals = res.values().iterator();
                while (keys.hasNext()) {
                    System.out.println(keys.next() + "=" + vals.next());
                }
            }
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    private static HashMap getTestMappingTagNames() throws Exception {
        try {
            HashMap mappingTagNames = new HashMap();
            mappingTagNames.put("Quantity", "myQuantity");
            mappingTagNames.put("StockLevelTimeStamp", "myStockLevelTimeStamp");
            mappingTagNames.put("VendorBatchNumber", "myVendorBatchNumber");
            mappingTagNames.put("Decimal", "myDecimal");
            mappingTagNames.put("OwningClient", "myOwningClient");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch_BatchStatus__"
                            + "@_StockUsage", "myStockUsage");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2__@_ReportingClient",
                    "#document_LogisticsStocks_" + "LogisticsStock_ReportingClients_ReportingClients2__@_ReportingClient");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_DemandID", "myDemandID");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item__@_UnitOfMeasurement",
                    "myUnitOfMeasurement");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_SystemID", "mySystemId");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport__@_StockLevelTimeStamp",
                    "myStockLevelTimeStamp");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item__@_ItemType",
                    "ItemType");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_Decimal", "myDecimal");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item__@_LocalItemNumber",
                    "myLocalItemNumber");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__@_"
                            + "VendorBatchNumber", "myVendorBatchNumber");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_TimeZone", "myTimeZone");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch_BatchStatus__"
                            + "@_Quantity", "myQuantity");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch_BatchStatus__"
                            + "@_QualityControlStatus", "myQualityControlStatus");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_DeliveryNumber", "myDeliveryNumber");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__"
                    + "@_InventoryBatchNumber", "myInventoryBatchNumber");
            mappingTagNames.put("#document_LogisticsStocks_TechnicalInformation__@_CreationTimeStamp", "myCreationTimeStamp");
            mappingTagNames.put(
                    "#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__@_OwningClient",
                    "myOwningClient");
            mappingTagNames.put("#document_LogisticsStocks_LogisticsStock_ReportingClients_ReportingClients2_StockLevelReport_Warehouse_Item_Batch__"
                    + "@_PackagingBatchNumber", "myPackagingBatchNumber");
            return mappingTagNames;
        } catch (Exception e) {
            throw new Exception("\n ->..error in" + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

    private static HashMap getTestDefaults() throws Exception {
        try {
            HashMap defaults = new HashMap();
            defaults.put("created", "24.06.2004");
            defaults.put("createdBy", "mo");
            return defaults;
        } catch (Exception e) {
            throw new Exception("\n ->..error in" + SOSClassUtil.getMethodName() + " " + e.getMessage(), e);
        }
    }

}