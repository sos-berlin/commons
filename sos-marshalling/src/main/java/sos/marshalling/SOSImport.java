package sos.marshalling;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xerces.parsers.SAXParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sos.connection.SOSConnection;
import sos.connection.SOSMySQLConnection;

/** @author Titus Meyer */
public class SOSImport extends DefaultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSImport.class);
    private SOSConnection connection = null;
    private boolean autoCommit = false;
    private boolean enableInsert = true;
    private boolean enableUpdate = true;
    private String fileName = null;
    private boolean restrictMode = true;
    private String packageId = null;
    private String packageElement = null;
    private String packageValue = null;
    private String xmlTagname = "sos_export";
    private String xmlEncoding = "iso-8859-1";
    private String normalizeFieldName = "strtoupper";
    private boolean autoNormalizeField = true;
    private boolean autoChecked = false;
    private HashMap restrictObject = new HashMap();
    private HashMap keyHandler = new HashMap();
    private HashMap recordHandler = new HashMap();
    private Tables tables = new Tables();
    private MetaRecords metaKeyRecords = new MetaRecords();
    private MetaRecords metaFieldRecords = new MetaRecords();
    private Records records = new Records();
    private int recordIndex = -1;
    private HashMap recordIdentifier = new HashMap();
    private boolean curExportOpen = false;
    private boolean curPackageOpened = false;
    private String curPackageId = null;
    private boolean curMetaOpened = false;
    private String curMetaTableName = null;
    private boolean curMetaKeysOpened = false;
    private boolean curMetaFieldsOpened = false;
    private boolean curDataOpened = false;
    private boolean curDataRecordOpened = false;
    private String curDataRecordName = null;
    private boolean curDataFieldsOpened = false;
    private boolean curDataFieldOpened = false;
    private String curDataFieldName = null;
    private StringBuilder curDataFieldData = null;
    private boolean curDataFieldNull = false;
    private String curElement = null;
    private int curImportPackage = 1;
    private int curImportPackageDepth = 0;
    private int curBlockedPackageDepth = 0;
    private int curPackageDepth = 0;
    private String operation = null;
    private HashMap mappingTablenames = null;

    /** @author Titus Meyer */
    private class MetaRecords {

        private HashMap metaRecords = new HashMap();

        public SOSImExportTableFieldTypes get(String packageId) {
            return (SOSImExportTableFieldTypes) metaRecords.get(packageId);
        }

        public void add(String packageId) {
            metaRecords.put(packageId, new SOSImExportTableFieldTypes());
        }
    }

    /** @author Titus Meyer */
    private class Records {

        private ArrayList records = new ArrayList();

        public int addRecord() {
            records.add(new HashMap());
            return records.size() - 1;
        }

        public void addValue(int index, String key, String val) {
            HashMap hash = (HashMap) records.get(index);
            if (hash != null) {
                hash.put(normalizeFieldName(key), val);
            }
        }

        public String getValue(int index, String key) {
            try {
                HashMap hash = (HashMap) records.get(index);
                if (hash != null) {
                    return (String) hash.get(normalizeFieldName(key));
                }
            } catch (Exception e) {
                //
            }
            return null;
        }

        public int size() {
            return records.size();
        }

        public Iterator getIterator(int index) {
            HashMap hash = (HashMap) records.get(index);
            if (hash != null) {
                return hash.keySet().iterator();
            } else {
                return null;
            }
        }
    }

    /** @author Titus Meyer */
    private class Tables {

        private HashMap tables = new HashMap();

        public void addTable(String table, boolean replace, HashMap restrict) {
            ArrayList array = new ArrayList();
            array.add(new Boolean(replace));
            array.add(restrict);
            tables.put(table.toLowerCase(), array);
        }

        public boolean getReplace(String table) {
            try {
                ArrayList list = (ArrayList) tables.get(table.toLowerCase());
                if (list != null) {
                    return ((Boolean) list.get(0)).booleanValue();
                }
            } catch (Exception e) {
                //
            }
            return false;
        }

        public HashMap getRestrict(String table) {
            try {
                ArrayList list = (ArrayList) tables.get(table.toLowerCase());
                if (list != null) {
                    return (HashMap) list.get(1);
                }
            } catch (Exception e) {
                //
            }
            return null;
        }

        public int size() {
            return tables.size();
        }

        public Iterator getIterator() {
            return tables.keySet().iterator();
        }

        public boolean isEmpty() {
            return tables.isEmpty();
        }

    }

    public SOSImport(SOSConnection conn, String fileName, String packageId, String packageElement, String packageValue) {
        super();
        if (conn != null) {
            this.connection = conn;
        }
        if (fileName != null) {
            this.fileName = fileName;
        }
        if (packageId != null) {
            this.packageId = packageId;
        }
        if (packageElement != null) {
            this.packageElement = packageElement;
        }
        if (packageValue != null) {
            this.packageValue = packageValue;
        }
    }

    public SOSImport(SOSConnection conn, String fileName) {
        this(conn, fileName, null, null, null);
    }

    public SOSImport() {
        this(null, null, null, null, null);
    }

    public void setConnection(SOSConnection conn) {
        this.connection = conn;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setUpdate(boolean update) {
        this.enableUpdate = update;
    }

    public void setInsert(boolean insert) {
        this.enableInsert = insert;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setRestrictMode(boolean restrictMode) {
        this.restrictMode = restrictMode;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public void setPackageElement(String packageElement) {
        this.packageElement = packageElement;
    }

    public void setXMLTagname(String xmlTagname) {
        this.xmlTagname = xmlTagname;
    }

    public void setXMLEncoding(String xmlEncoding) {
        this.xmlEncoding = xmlEncoding;
    }

    public void setNormalizeFieldName(String normalizeFieldName) throws Exception {
        if (!"strtolower".equalsIgnoreCase(normalizeFieldName) && !"strtoupper".equalsIgnoreCase(normalizeFieldName)) {
            throw new IllegalArgumentException("SOSExport.setNormalizeFieldName: normalizeFielName must be \"strtolower\" or \"strtoupper\"");
        }
        try {
            this.normalizeFieldName = normalizeFieldName;
            this.connection.setFieldNameToUpperCase("strtoupper".equalsIgnoreCase(normalizeFieldName));
            if ("strtoupper".equalsIgnoreCase(normalizeFieldName)) {
                this.connection.setKeysToUpperCase();
            } else {
                this.connection.setKeysToLowerCase();
            }
        } catch (Exception e) {
            throw new Exception("SOSImport.setNormalizeFieldName: " + e.getMessage());
        }
    }

    public void setAutoNormalizeField(boolean auto) {
        this.autoNormalizeField = auto;
    }

    public void startDocument() throws SAXException {
        autoChecked = false;
        try {
            LOGGER.debug("Starte Import...");
        } catch (Exception e) {
            throw new SAXException("SOSImport.startDocument: " + e.getMessage(), e);
        }
    }

    public void endDocument() throws SAXException {
        autoChecked = false;
        try {
            LOGGER.debug("...beende Import");
        } catch (Exception e) {
            throw new SAXException("SOSImport.endDocument: " + e.getMessage(), e);
        }
    }

    public void startElement(String uri, String name, String qName, org.xml.sax.Attributes atts) throws SAXException {
        curElement = name;
        try {
            LOGGER.trace("import startElement: " + name);
            if (name.equalsIgnoreCase(xmlTagname)) {
                curExportOpen = true;
            } else if (name.equalsIgnoreCase(xmlTagname + "_package") && curExportOpen) {
                curPackageOpened = true;
                curPackageId = atts.getValue("id");
            } else if (name.equalsIgnoreCase(xmlTagname + "_meta") && curPackageOpened) {
                metaKeyRecords.add(curPackageId);
                metaFieldRecords.add(curPackageId);
                curMetaOpened = true;
            } else if ("table".equalsIgnoreCase(name) && curMetaOpened) {
                curMetaTableName = atts.getValue("name");
                if (mappingTablenames != null && mappingTablenames.get(curMetaTableName) != null && !mappingTablenames.get(curMetaTableName)
                        .toString().isEmpty()) {
                    LOGGER.debug("import tablename " + curMetaTableName + " is mapping in " + mappingTablenames.get(curMetaTableName));
                    curMetaTableName = mappingTablenames.get(curMetaTableName).toString();
                }
                metaKeyRecords.get(curPackageId).setTable(curMetaTableName);
                metaFieldRecords.get(curPackageId).setTable(curMetaTableName);
            } else if ("key_fields".equalsIgnoreCase(name) && curMetaOpened) {
                curMetaKeysOpened = true;
            } else if ("field".equalsIgnoreCase(name) && curMetaKeysOpened) {
                LOGGER.trace("add keyfield: name = " + normalizeFieldName(atts.getValue("name")) + " type = " + atts.getValue("type") + " typeID = "
                        + atts.getValue("typeID") + " len = " + atts.getValue("len") + " scale = " + atts.getValue("scale"));
                String field = atts.getValue("name");
                if (autoNormalizeField && !autoChecked) {
                    autoNormalize(field);
                    autoChecked = true;
                }
                metaKeyRecords.get(curPackageId).addField(normalizeFieldName(field), atts.getValue("type"), new Integer(atts.getValue("typeID")),
                        new BigInteger(atts.getValue("len")), new Integer(atts.getValue("scale")));
            } else if ("fields".equalsIgnoreCase(name) && curMetaOpened) {
                curMetaFieldsOpened = true;
            } else if ("field".equalsIgnoreCase(name) && curMetaFieldsOpened) {
                LOGGER.trace("add field: name = " + normalizeFieldName(atts.getValue("name")) + " type = " + atts.getValue("type") + " typeID = "
                        + atts.getValue("typeID") + " len = " + atts.getValue("len") + " scale = " + atts.getValue("scale"));
                String field = atts.getValue("name");
                if (autoNormalizeField && !autoChecked) {
                    autoNormalize(field);
                    autoChecked = true;
                }
                metaFieldRecords.get(curPackageId).addField(normalizeFieldName(field), atts.getValue("type"), new Integer(atts.getValue("typeID")),
                        new BigInteger(atts.getValue("len")), new Integer(atts.getValue("scale")));
            } else if (name.equalsIgnoreCase(xmlTagname + "_data") && curPackageOpened && !curMetaOpened) {
                curDataOpened = true;
            } else if (name.equalsIgnoreCase(xmlTagname + "_record")) {
                curDataRecordOpened = true;
                curDataRecordName = atts.getValue("name");
                curPackageDepth++;
            } else if (name.equalsIgnoreCase(xmlTagname + "_fields") && curDataRecordOpened) {
                curDataFieldsOpened = true;
                if (curBlockedPackageDepth <= 0 || curBlockedPackageDepth > curPackageDepth) {
                    recordIndex = records.addRecord();
                }
                if (atts.getValue("operation") != null && !atts.getValue("operation").isEmpty()) {
                    operation = atts.getValue("operation");
                }
            } else if (curDataFieldsOpened) {
                curDataFieldOpened = true;
                curDataFieldName = normalizeFieldName(name);
                curDataFieldData = new StringBuilder();
                curDataFieldNull = atts.getValue("null") != null && "true".equalsIgnoreCase(atts.getValue("null"));
                records.addValue(recordIndex, curDataFieldName, curDataFieldData.toString());
            }
        } catch (Exception e) {
            throw new SAXException("SOSImport.startElement: " + e.getMessage(), e);
        }
    }

    public void endElement(String uri, String name, String qName) throws SAXException {
        try {
            LOGGER.trace("import endElement: " + name);
            if (name.equalsIgnoreCase(xmlTagname)) {
                curExportOpen = false;
            } else if (name.equalsIgnoreCase(xmlTagname + "_package")) {
                curPackageOpened = false;
            } else if (name.equalsIgnoreCase(xmlTagname + "_meta")) {
                curMetaOpened = false;
            } else if ("key_fields".equalsIgnoreCase(name)) {
                curMetaKeysOpened = false;
            } else if ("fields".equalsIgnoreCase(name)) {
                curMetaFieldsOpened = false;
            } else if (name.equalsIgnoreCase(xmlTagname + "_data")) {
                curDataOpened = false;
            } else if (name.equalsIgnoreCase(xmlTagname + "_record")) {
                curDataRecordOpened = false;
                if (packageId != null && curPackageDepth == curImportPackageDepth) {
                    curImportPackage = 0;
                }
                if (curBlockedPackageDepth == curPackageDepth) {
                    curBlockedPackageDepth = 0;
                }
                curPackageDepth--;
            } else if (name.equalsIgnoreCase(xmlTagname + "_fields")) {
                curDataFieldsOpened = false;
                if (curImportPackage == 1 && (curBlockedPackageDepth == 0 || curPackageDepth == 0)) {
                    if (operation != null && "insert".equals(operation)) {
                        importRecord(curDataRecordName, recordIndex, operation);
                    } else if (operation != null && "update".equals(operation)) {
                        importRecord(curDataRecordName, recordIndex, operation);
                    } else if (operation != null && "delete".equals(operation)) {
                        deleteRecord(curDataRecordName, recordIndex);
                    } else {
                        importRecord(curDataRecordName, recordIndex);
                    }
                    operation = null;
                }
            } else if (curDataFieldOpened) {
                if (curDataFieldNull) {
                    records.addValue(recordIndex, curDataFieldName, null);
                } else {
                    records.addValue(recordIndex, curDataFieldName, curDataFieldData.toString());
                }
                if (curImportPackage <= 0 && curDataRecordName.equalsIgnoreCase(packageId) && curElement.equalsIgnoreCase(packageElement)
                        && curDataFieldData.toString().equalsIgnoreCase(packageValue)) {
                    curImportPackage = 1;
                    curImportPackageDepth = curPackageDepth;
                }
                curDataFieldOpened = false;
                curDataFieldName = null;
                curDataFieldData = null;
            }
        } catch (Exception e) {
            throw new SAXException("SOSImport.endElement: " + e.getMessage(), e);
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (curDataFieldOpened) {
            String str = new String(ch, start, length);
            if (!"".equals(str)) {
                curDataFieldData.append(str);
            }
        }
    }

    public void setTable(String table, boolean replace, HashMap restrict) throws Exception {
        try {
            if (table == null || "".equals(table)) {
                throw new IllegalArgumentException("SOSImport.setTable: you have to give a table name");
            }
            LOGGER.debug("setTable: table=" + table + " replace=" + replace);
            tables.addTable(table, replace, restrict);
        } catch (Exception e) {
            throw new Exception("SOSImport.setTable: " + e.getMessage(), e);
        }
    }

    public void setHandler(String table, String keyHandler, String recordHandler, String recordIdentifier) {
        if (table == null || "".equals(table)) {
            throw new IllegalArgumentException("SOSImport.setHandler: you have to give a table name");
        }
        if (keyHandler != null && !"".equals(keyHandler)) {
            this.keyHandler.put(table.toLowerCase(), keyHandler);
        }
        if (recordHandler != null && !"".equals(recordHandler)) {
            this.recordHandler.put(table.toLowerCase(), recordHandler);
        }
        if (recordIdentifier != null && !"".equals(recordIdentifier)) {
            this.recordIdentifier.put(table.toLowerCase(), recordIdentifier);
        }
    }

    public void doImport() throws Exception, SAXException, FileNotFoundException {
        try {
            if ("strtoupper".equalsIgnoreCase(normalizeFieldName)) {
                connection.setKeysToUpperCase();
                connection.setFieldNameToUpperCase(true);
            } else {
                connection.setKeysToLowerCase();
                connection.setFieldNameToUpperCase(false);
            }
            SAXParser parser = new SAXParser();
            parser.setContentHandler(this);
            File file = new File(fileName);
            if (!file.canRead()) {
                throw new FileNotFoundException("File not found: " + fileName);
            }
            LOGGER.debug("Using file: " + fileName);
            parser.parse(fileName);
        } catch (SAXException e) {
            throw new SAXException("SOSImport.doImport: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("SOSImport.doImport: " + e.getMessage(), e);
        }
    }

    public void doImport(SOSConnection conn, String fileName) throws Exception, SAXException, FileNotFoundException {
        try {
            if (conn != null) {
                this.connection = conn;
            }
            if (fileName != null) {
                this.fileName = fileName;
            }
            if (packageId != null) {
                this.curImportPackage = 0;
            }
            doImport();
        } catch (SAXException e) {
            throw new SAXException("SOSImport.doImport: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("SOSImport.doImport: " + e.getMessage(), e);
        }
    }

    public void doImport(SOSConnection conn, String fileName, String packageId, String packageElement, String packageValue) throws Exception,
            SAXException, FileNotFoundException {
        try {
            if (conn != null) {
                this.connection = conn;
            }
            if (fileName != null) {
                this.fileName = fileName;
            }
            if (packageId != null) {
                this.packageId = packageId;
            }
            if (packageElement != null) {
                this.packageElement = packageElement;
            }
            if (packageValue != null) {
                this.packageValue = packageValue;
            }
            if (packageId != null) {
                this.curImportPackage = 0;
            }
            doImport();
        } catch (SAXException e) {
            throw new SAXException("SOSImport.doImport: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("SOSImport.doImport: " + e.getMessage(), e);
        }
    }

    private void importRecord(String name, int index, String operation) throws Exception {
        boolean isNewKey = false;
        try {
            LOGGER.debug("import_record(" + name + ", " + index + "): key_fields=" + normalizeFieldName(metaKeyRecords.get(name).getKeyString()
                    + " table=" + metaKeyRecords.get(name).getTable()));
            if (connection == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = this.restrictMode;
            HashMap restrictObject = this.restrictObject;
            if (!tables.isEmpty()) {
                boolean found = false;
                for (Iterator it = tables.getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    if (name.equalsIgnoreCase(key)) {
                        found = true;
                        restrictMode = tables.getReplace(key);
                        restrictObject = tables.getRestrict(key);
                        break;
                    }
                }
                if (!found) {
                    LOGGER.debug("Import denied for table by restriction: " + name);
                    return;
                }
            }
            if (restrictObject != null && !restrictObject.isEmpty()) {
                for (Iterator it = restrictObject.keySet().iterator(); it.hasNext();) {
                    String restrictName = it.next().toString();
                    String restrictValue = (String) restrictObject.get(restrictName);
                    LOGGER.debug("checking for element: " + restrictName + "=" + restrictValue);
                    if (records.getValue(index, restrictName) == null) {
                        curBlockedPackageDepth = curPackageDepth;
                        LOGGER.debug("import denied for element by restriction at depth " + curBlockedPackageDepth + ": " + restrictName
                                + " not in record");
                        return;
                    }
                    if (records.getValue(index, restrictName) != null && restrictValue.equalsIgnoreCase(records.getValue(index, restrictName))) {
                        curBlockedPackageDepth = curPackageDepth;
                        LOGGER.debug("import denied for element by restriction at depth " + curBlockedPackageDepth + ": " + restrictName + ": "
                                + restrictValue + " != " + records.getValue(index, restrictName));
                        return;
                    }
                }
                restrictObject = null;
            }
            HashMap keys = new HashMap();
            for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                LOGGER.debug("key_fields key [" + key + "] = " + records.getValue(index, key));
                keys.put(normalizeFieldName(key), records.getValue(index, key));
            }
            String keyHandler = metaKeyRecords.get(name).getTable().toLowerCase();
            if (this.keyHandler.containsKey(keyHandler)) {
                LOGGER.debug("key_handler: " + (String) this.keyHandler.get(keyHandler));
                Class[] params = new Class[] { HashMap.class };
                Method method = getClass().getMethod((String) this.keyHandler.get(keyHandler), params);
                HashMap[] args = { keys };
                keys = (HashMap) method.invoke(this, args);
            }
            Map<String, String> record = null;
            boolean isNew = false;
            if (operation != null && "insert".equalsIgnoreCase(operation)) {
                isNew = true;
            } else if (operation != null && "update".equalsIgnoreCase(operation)) {
                isNew = false;
            } else {
                if (keys != null && !keys.isEmpty() && restrictMode) {
                    StringBuilder stm = new StringBuilder();
                    stm.append("SELECT * FROM " + metaFieldRecords.get(name).getTable());
                    String and = " WHERE ";
                    for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
                        String key = it.next().toString();
                        stm.append(and + "\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) keys.get(key)));
                        and = " AND ";
                    }
                    record = connection.getSingle(stm.toString());
                }
                isNew = (record == null || record.isEmpty());
            }
            LOGGER.debug("is_new: " + isNew);
            record = new HashMap();
            for (Iterator it = records.getIterator(index); it.hasNext();) {
                String key = it.next().toString();
                String val = records.getValue(index, key);
                if (val != null) {
                    LOGGER.trace("record[" + key + "] = " + (val.length() > 255 ? "(" + val.length() + " Chars)" : val));
                } else {
                    LOGGER.trace("record[" + key + "] = NULL");
                }
                record.put(key, fromXML(val));
            }
            String recordHandler = metaFieldRecords.get(name).getTable().toLowerCase();
            if (this.recordHandler.containsKey(recordHandler)) {
                String recordIdentifier = this.recordIdentifier.containsKey(recordHandler) ? (String) this.recordIdentifier.get(recordHandler) : null;
                LOGGER.debug("recordHandler: " + (String) this.recordHandler.get(recordHandler));
                Class[] params = new Class[] { HashMap.class, HashMap.class, String.class };
                Method method = getClass().getMethod((String) this.recordHandler.get(recordHandler), params);
                Object[] args = { keys, record, recordIdentifier };
                record = (HashMap) method.invoke(this, args);
                HashMap recordKeys = new HashMap();
                for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    recordKeys.put(normalizeFieldName(key), records.getValue(index, key));
                }
                if (record != null && !record.isEmpty()) {
                    for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                        String key = normalizeFieldName(it.next().toString());
                        if (recordKeys.get(key) != record.get(key)) {
                            isNew = true;
                            isNewKey = true;
                            break;
                        }
                    }
                    if (record.containsKey("SOS_EXPORT_IS_NEW")) {
                        isNew = new Boolean((String) record.get("SOS_EXPORT_IS_NEW")).booleanValue();
                        isNewKey = true;
                        record.remove("SOS_EXPORT_IS_NEW");
                    }
                }
            }
            if (record != null && !record.isEmpty()) {
                if (this.keyHandler.containsKey(keyHandler)) {
                    if (isNewKey) {
                        for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            LOGGER.debug("updating key_fields key[" + key + "] = " + record.get(key));
                            keys.put(key, (String) record.get(key));
                        }
                    } else {
                        for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            LOGGER.debug("updating key_fields record[" + key + "] = " + record.get(key));
                            record.put(key, (String) keys.get(key));
                        }
                    }
                }
                Map<String, List<Object>> blobs = getBlobs(name, record);
                if (isNew) {
                    if (!enableInsert) {
                        LOGGER.debug("record skipped: no insert enabled");
                    } else {
                        insertRecord(name, record);
                    }
                } else {
                    if (!enableUpdate) {
                        LOGGER.debug("record skipped: no update enabled");
                    } else {
                        updateRecord(name, keys, record);
                    }
                }
                updateBlob(name, blobs, keys, record);
            } else {
                LOGGER.debug("record skipped");
            }
            if (autoCommit) {
                connection.commit();
            }
        } catch (Exception e) {
            if (autoCommit) {
                connection.rollback();
            }
            LOGGER.error(e.toString(), e);
            throw new Exception("SOSImport.import_record: " + e.toString(), e);
        }
    }

    private void deleteRecord(String name, int index) throws Exception {
        try {
            LOGGER.debug("delete_record(" + name + ", " + index + "): key_fields=" + normalizeFieldName(metaKeyRecords.get(name).getKeyString()
                    + " table=" + metaKeyRecords.get(name).getTable()));
            if (connection == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = this.restrictMode;
            HashMap restrictObject = this.restrictObject;
            if (!tables.isEmpty()) {
                boolean found = false;
                for (Iterator it = tables.getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    if (name.equalsIgnoreCase(key)) {
                        found = true;
                        restrictMode = tables.getReplace(key);
                        restrictObject = tables.getRestrict(key);
                        break;
                    }
                }
                if (!found) {
                    LOGGER.debug("Import denied for table by restriction: " + name);
                    return;
                }
            }
            if (restrictObject != null && !restrictObject.isEmpty()) {
                for (Iterator it = restrictObject.keySet().iterator(); it.hasNext();) {
                    String restrictName = it.next().toString();
                    String restrictValue = (String) restrictObject.get(restrictName);
                    LOGGER.debug("checking for element: " + restrictName + "=" + restrictValue);
                    if (records.getValue(index, restrictName) == null) {
                        curBlockedPackageDepth = curPackageDepth;
                        LOGGER.debug("import denied for element by restriction at depth " + curBlockedPackageDepth + ": " + restrictName
                                + " not in record");
                        return;
                    }
                    if (records.getValue(index, restrictName) != null && restrictValue.equalsIgnoreCase(records.getValue(index, restrictName))) {
                        curBlockedPackageDepth = curPackageDepth;
                        LOGGER.debug("import denied for element by restriction at depth " + curBlockedPackageDepth + ": " + restrictName + ": "
                                + restrictValue + " != " + records.getValue(index, restrictName));
                        return;
                    }
                }
                restrictObject = null;
            }
            HashMap keys = new HashMap();
            for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                LOGGER.debug("key_fields key [" + key + "] = " + records.getValue(index, key));
                keys.put(normalizeFieldName(key), records.getValue(index, key));
            }
            String keyHandler = metaKeyRecords.get(name).getTable().toLowerCase();
            if (this.keyHandler.containsKey(keyHandler)) {
                LOGGER.debug("key_handler: " + (String) this.keyHandler.get(keyHandler));
                Class[] params = new Class[] { HashMap.class };
                Method method = getClass().getMethod((String) this.keyHandler.get(keyHandler), params);
                HashMap[] args = { keys };
                keys = (HashMap) method.invoke(this, args);
            }
            deleteRecord(name, keys);
            if (autoCommit) {
                connection.commit();
            }
        } catch (Exception e) {
            if (autoCommit) {
                connection.rollback();
            }
            LOGGER.error(e.toString(), e);
            throw new Exception("SOSImport.import_record: " + e.toString(), e);
        }
    }

    private void importRecord(String name, int index) throws Exception {
        boolean isNewKey = false;
        try {
            LOGGER.debug("import_record(" + name + ", " + index + "): key_fields=" + normalizeFieldName(metaKeyRecords.get(name).getKeyString()
                    + " table=" + metaKeyRecords.get(name).getTable()));
            if (connection == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = this.restrictMode;
            HashMap restrictObject = this.restrictObject;
            if (!tables.isEmpty()) {
                boolean found = false;
                for (Iterator it = tables.getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    if (name.equalsIgnoreCase(key)) {
                        found = true;
                        restrictMode = tables.getReplace(key);
                        restrictObject = tables.getRestrict(key);
                        break;
                    }
                }
                if (!found) {
                    LOGGER.debug("Import denied for table by restriction: " + name);
                    return;
                }
            }
            if (restrictObject != null && !restrictObject.isEmpty()) {
                for (Iterator it = restrictObject.keySet().iterator(); it.hasNext();) {
                    String restrictName = it.next().toString();
                    String restrictValue = (String) restrictObject.get(restrictName);
                    LOGGER.debug("checking for element: " + restrictName + "=" + restrictValue);
                    if (records.getValue(index, restrictName) == null) {
                        curBlockedPackageDepth = curPackageDepth;
                        LOGGER.debug("import denied for element by restriction at depth " + curBlockedPackageDepth + ": " + restrictName
                                + " not in record");
                        return;
                    }
                    if (records.getValue(index, restrictName) != null && restrictValue.equalsIgnoreCase(records.getValue(index, restrictName))) {
                        curBlockedPackageDepth = curPackageDepth;
                        LOGGER.debug("import denied for element by restriction at depth " + curBlockedPackageDepth + ": " + restrictName + ": "
                                + restrictValue + " != " + records.getValue(index, restrictName));
                        return;
                    }
                }
                restrictObject = null;
            }
            HashMap keys = new HashMap();
            for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                LOGGER.debug("key_fields key [" + key + "] = " + records.getValue(index, key));
                keys.put(normalizeFieldName(key), records.getValue(index, key));
            }
            String keyHandler = metaKeyRecords.get(name).getTable().toLowerCase();
            if (this.keyHandler.containsKey(keyHandler)) {
                LOGGER.debug("key_handler: " + (String) this.keyHandler.get(keyHandler));
                Class[] params = new Class[] { HashMap.class };
                Method method = getClass().getMethod((String) this.keyHandler.get(keyHandler), params);
                HashMap[] args = { keys };
                keys = (HashMap) method.invoke(this, args);
            }
            Map<String, String> record = null;
            if (keys != null && !keys.isEmpty() && restrictMode) {
                StringBuilder stm = new StringBuilder();
                stm.append("SELECT * FROM " + metaFieldRecords.get(name).getTable());
                String and = " WHERE ";
                for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
                    String key = it.next().toString();
                    stm.append(and + "\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) keys.get(key)));
                    and = " AND ";
                }
                record = connection.getSingle(stm.toString());
            }
            boolean isNew = record == null || record.isEmpty();
            LOGGER.debug("is_new: " + isNew);
            record = new HashMap<String, String>();
            for (Iterator it = records.getIterator(index); it.hasNext();) {
                String key = it.next().toString();
                String val = records.getValue(index, key);
                if (val != null) {
                    LOGGER.trace("record[" + key + "] = " + (val.length() > 255 ? "(" + val.length() + " Chars)" : val));
                } else {
                    LOGGER.trace("record[" + key + "] = NULL");
                }
                record.put(key, fromXML(val));
            }
            String recordHandler = metaFieldRecords.get(name).getTable().toLowerCase();
            if (this.recordHandler.containsKey(recordHandler)) {
                String recordIdentifier = this.recordIdentifier.containsKey(recordHandler) ? (String) this.recordIdentifier.get(recordHandler) : null;
                LOGGER.debug("recordHandler: " + (String) this.recordHandler.get(recordHandler));
                Class[] params = new Class[] { HashMap.class, HashMap.class, String.class };
                Method method = getClass().getMethod((String) this.recordHandler.get(recordHandler), params);
                Object[] args = { keys, record, recordIdentifier };
                record = (HashMap<String, String>) method.invoke(this, args);
                HashMap recordKeys = new HashMap();
                for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    recordKeys.put(normalizeFieldName(key), records.getValue(index, key));
                }
                if (record != null && !record.isEmpty()) {
                    for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                        String key = normalizeFieldName(it.next().toString());
                        if (recordKeys.get(key) != record.get(key)) {
                            isNew = true;
                            isNewKey = true;
                            break;
                        }
                    }
                    if (record.containsKey("SOS_EXPORT_IS_NEW")) {
                        isNew = new Boolean((String) record.get("SOS_EXPORT_IS_NEW")).booleanValue();
                        isNewKey = true;
                        record.remove("SOS_EXPORT_IS_NEW");
                    }
                }
            }
            if (record != null && !record.isEmpty()) {
                if (this.keyHandler.containsKey(keyHandler)) {
                    if (isNewKey) {
                        for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            LOGGER.debug("updating key_fields key[" + key + "] = " + record.get(key));
                            keys.put(key, (String) record.get(key));
                        }
                    } else {
                        for (Iterator it = metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            LOGGER.debug("updating key_fields record[" + key + "] = " + record.get(key));
                            record.put(key, (String) keys.get(key));
                        }
                    }
                }
                Map<String, List<Object>> blobs = getBlobs(name, record);
                if (isNew) {
                    if (!enableInsert) {
                        LOGGER.debug("record skipped: no insert enabled");
                    } else {
                        insertRecord(name, record);
                    }
                } else {
                    if (!enableUpdate) {
                        LOGGER.debug("record skipped: no update enabled");
                    } else {
                        updateRecord(name, keys, record);
                    }
                }
                updateBlob(name, blobs, keys, record);
            } else {
                LOGGER.debug("record skipped");
            }
            if (autoCommit) {
                connection.commit();
            }
        } catch (Exception e) {
            if (autoCommit) {
                connection.rollback();
            }
            LOGGER.error(e.toString(), e);
            throw new Exception("SOSImport.import_record: " + e.toString(), e);
        }
    }

    private Map<String, List<Object>> getBlobs(String name, Map<String, String> record) throws Exception {
        try {
            Map<String, List<Object>> blobs = new HashMap<String, List<Object>>();
            for (Iterator<String> it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                String xmlVal = (String) record.get(key);
                byte[] val = fromHexString(xmlVal);
                String blobType = null;
                List<Object> obj = new ArrayList<Object>(2);
                switch (metaFieldRecords.get(name).getTypeId(key)) {
                case Types.LONGVARCHAR:
                case Types.CLOB:
                    blobType = "clob";
                    if (val != null && val.length > 0) {
                        obj.add(new String(val));
                    } else {
                        obj.add(null);
                    }
                    break;
                case Types.BLOB:
                case Types.BINARY:
                case Types.VARBINARY:
                case Types.LONGVARBINARY:
                    blobType = "blob";
                    if (val != null && val.length > 0) {
                        obj.add(val);
                    } else {
                        obj.add(null);
                    }
                    break;
                }
                LOGGER.debug("found " + blobType + ": name = " + key + " type = " + metaFieldRecords.get(name).getTypeName(key) + " typeID = "
                        + metaFieldRecords.get(name).getTypeId(key));
                obj.add(blobType);
                blobs.put(key, obj);
                record.put(key, null);
            }
            return blobs;
        } catch (Exception e) {
            throw new Exception("SOSImport.getBlobs: " + e.getMessage(), e);
        }
    }

    private void updateBlob(String name, Map<String, List<Object>> blobs, HashMap keys, Map<String, String> record) throws Exception {
        try {
            for (Iterator<String> it = blobs.keySet().iterator(); it.hasNext();) {
                String blobName = it.next();
                List<Object> blobValue = blobs.get(blobName);
                LOGGER.debug("update " + (String) blobValue.get(0) + ": " + blobName);
                StringBuilder stm = new StringBuilder();
                String and = "";
                for (Iterator it2 = keys.keySet().iterator(); it2.hasNext();) {
                    String key = it2.next().toString();
                    if (!record.containsKey(key)) {
                        throw new Exception("SOSImport.import_record: missing key in record: " + key);
                    }
                    stm.append(and + " \"" + normalizeFieldName(key) + "\"=" + quote(name, normalizeFieldName(key), record.get(key)));
                    and = " AND ";
                }
                if (blobValue.get(1) != null) {
                    if ("blob".equals((String) blobValue.get(0))) {
                        connection.updateBlob(metaFieldRecords.get(name).getTable(), blobName, (byte[]) blobValue.get(1), stm.toString());
                    } else {
                        connection.updateClob(metaFieldRecords.get(name).getTable(), blobName, (String) blobValue.get(1), stm.toString());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("SOSImport.updateBlob: " + e.getMessage(), e);
        }
    }

    private void insertRecord(String name, Map<String, String> record) throws Exception {
        try {
            LOGGER.debug("inserting record");
            StringBuilder fields = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (Iterator<String> it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                fields.append("\"" + key + "\"");
                values.append(quote(name, key, record.get(key)));
                if (it.hasNext()) {
                    fields.append(", ");
                    values.append(", ");
                }
            }
            connection.execute("INSERT INTO " + metaFieldRecords.get(name).getTable() + " (" + fields + ") VALUES(" + values + ")");
        } catch (Exception e) {
            throw new Exception("SOSImport.insertRecord: " + e.getMessage(), e);
        }
    }

    private void updateRecord(String name, HashMap keys, Map<String, String> record) throws Exception {
        try {
            LOGGER.debug("updating record");
            StringBuilder stm = new StringBuilder("UPDATE " + metaFieldRecords.get(name).getTable() + " SET ");
            for (Iterator<String> it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                stm.append("\"" + normalizeFieldName(key) + "\"=" + quote(name, key, record.get(key)));
                if (it.hasNext()) {
                    stm.append(", ");
                }
            }
            String and = " WHERE ";
            for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                stm.append(and + "\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) keys.get(key)));
                and = " AND ";
            }
            connection.execute(stm.toString());
        } catch (Exception e) {
            throw new Exception("SOSImport.updateRecord: " + e.getMessage(), e);
        }
    }

    private void deleteRecord(String name, HashMap keys) throws Exception {
        try {
            LOGGER.debug("delete record");
            StringBuilder stm = new StringBuilder("DELETE FROM " + metaFieldRecords.get(name).getTable());
            String and = " WHERE ";
            for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                if (keys.get(key) != null) {
                    stm.append(and + "\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) keys.get(key)));
                    and = " AND ";
                }
            }
            LOGGER.trace("deleted record:" + stm.toString());
            connection.execute(stm.toString());
        } catch (Exception e) {
            throw new Exception("SOSImport.updateRecord: " + e.getMessage(), e);
        }
    }

    private String quote(String packageId, String field, String val) {
        if (val == null) {
            return "NULL";
        }

        switch (metaFieldRecords.get(packageId).getTypeId(field)) {
        case Types.BIGINT:
        case Types.DECIMAL:
        case Types.DOUBLE:
        case Types.FLOAT:
        case Types.INTEGER:
        case Types.REAL:
        case Types.SMALLINT:
        case Types.NUMERIC:
        case Types.TINYINT:
            if ("".equals(val)) {
                val = "NULL";
            }
            return val;
        case Types.DATE:
        case Types.TIMESTAMP:
            return "%timestamp_iso('" + val + "')";
        default:
            val = val.replaceAll("'", "''");
            if (connection instanceof SOSMySQLConnection) {
                val = val.replaceAll("\\\\", "\\\\\\\\");
            }
            return "'" + val + "'";
        }
    }

    private void autoNormalize(String field) throws Exception {
        String copy = new String(field);
        try {
            if (field.equals(copy.toLowerCase())) {
                LOGGER.debug("Auto Normalisation: lower case");
                setNormalizeFieldName("strtolower");
            } else if (field.equals(copy.toUpperCase())) {
                LOGGER.debug("Auto Normalisation: upper case");
                setNormalizeFieldName("strtoupper");
            } else {
                LOGGER.debug("Auto Normalisation: upper case");
                setNormalizeFieldName("strtoupper");
            }
        } catch (Exception e) {
            throw new Exception("SOSImport.autoNormalize: " + e.getMessage());
        }
    }

    private String normalizeFieldName(String field) {
        if ("strtoupper".equalsIgnoreCase(normalizeFieldName)) {
            return field.toUpperCase();
        } else {
            return field.toLowerCase();
        }
    }

    private String fromXML(String xml) {
        return xml;
    }

    private static byte[] fromHexString(String s) {
        if (s == null) {
            return null;
        } else if ("".equals(s)) {
            return null;
        }
        int length = 0;
        int strLength = s.length();
        for (int i = 0; i < strLength; i++) {
            if (s.charAt(i) == ' ' || s.charAt(i) == '\n' || s.charAt(i) == '\t') {
                length++;
            }
        }
        length = s.length() - length;
        if ((length & 0x1) != 0) {
            throw new IllegalArgumentException("fromHexString requires an even number of hex characters");
        }
        length = length / 2;
        byte[] b = new byte[length];
        char cLow;
        char cHigh;
        int i = 0, j = 0;
        while (i < strLength) {
            cLow = s.charAt(i);
            if (cLow == ' ' || cLow == '\n' || cLow == '\t') {
                i++;
                continue;
            }
            cHigh = s.charAt(i + 1);
            int high = charToNibble(cLow);
            int low = charToNibble(cHigh);
            b[j] = (byte) ((high << 4) | low);
            j++;
            i += 2;
        }
        return b;
    }

    private static int charToNibble(char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 0xa;
        } else if ('A' <= c && c <= 'F') {
            return c - 'A' + 0xa;
        } else {
            throw new IllegalArgumentException("Invalid hex character: " + c);
        }
    }

    public HashMap getMappingTablenames() {
        return mappingTablenames;
    }

    public void setMappingTablenames(HashMap mappingTablenames) {
        this.mappingTablenames = mappingTablenames;
    }

}