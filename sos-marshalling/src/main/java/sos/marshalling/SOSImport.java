package sos.marshalling;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.sql.Types;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.apache.xerces.parsers.SAXParser;

import sos.connection.SOSConnection;
import sos.connection.SOSMySQLConnection;
import sos.marshalling.SOSImExportTableFieldTypes;
import sos.util.SOSStandardLogger;

/** @author Titus Meyer */
public class SOSImport extends DefaultHandler {

    private SOSConnection _conn = null;
    private SOSStandardLogger _log = null;
    private boolean _autoCommit = false;
    private boolean _enableInsert = true;
    private boolean _enableUpdate = true;
    private String _fileName = null;
    private boolean _restrictMode = true;
    private String _packageId = null;
    private String _packageElement = null;
    private String _packageValue = null;
    private String _xmlTagname = "sos_export";
    private String _xmlEncoding = "iso-8859-1";
    private String _normalizeFieldName = "strtoupper";
    private boolean _autoNormalizeField = true;
    private boolean _autoChecked = false;
    private HashMap _restrictObject = new HashMap();
    private HashMap _keyHandler = new HashMap();
    private HashMap _recordHandler = new HashMap();
    private Tables _tables = new Tables();
    private MetaRecords _metaKeyRecords = new MetaRecords();
    private MetaRecords _metaFieldRecords = new MetaRecords();
    private Records _records = new Records();
    private int _recordIndex = -1;
    private HashMap _recordIdentifier = new HashMap();
    private boolean _curExportOpen = false;
    private boolean _curPackageOpened = false;
    private String _curPackageId = null;
    private boolean _curMetaOpened = false;
    private String _curMetaTableName = null;
    private boolean _curMetaKeysOpened = false;
    private boolean _curMetaFieldsOpened = false;
    private boolean _curDataOpened = false;
    private boolean _curDataRecordOpened = false;
    private String _curDataRecordName = null;
    private boolean _curDataFieldsOpened = false;
    private boolean _curDataFieldOpened = false;
    private String _curDataFieldName = null;
    private StringBuilder _curDataFieldData = null;
    private boolean _curDataFieldNull = false;
    private String _curElement = null;
    private int _curImportPackage = 1;
    private int _curImportPackageDepth = 0;
    private int _curBlockedPackageDepth = 0;
    private int _curPackageDepth = 0;
    private String _operation = null;
    private HashMap mappingTablenames = null;

    /** @author Titus Meyer */
    private class MetaRecords {

        private HashMap _metaRecords = new HashMap();

        public SOSImExportTableFieldTypes get(String packageId) {
            return (SOSImExportTableFieldTypes) _metaRecords.get(packageId);
        }

        public void add(String packageId) {
            _metaRecords.put(packageId, new SOSImExportTableFieldTypes());
        }
    }

    /** @author Titus Meyer */
    private class Records {

        private ArrayList _records = new ArrayList();

        public int addRecord() {
            _records.add(new HashMap());
            return _records.size() - 1;
        }

        public void addValue(int index, String key, String val) {
            HashMap hash = (HashMap) _records.get(index);
            if (hash != null) {
                hash.put(normalizeFieldName(key), val);
            }
        }

        public String getValue(int index, String key) {
            try {
                HashMap hash = (HashMap) _records.get(index);
                if (hash != null) {
                    return (String) hash.get(normalizeFieldName(key));
                }
            } catch (Exception e) {
            }
            return null;
        }

        public int size() {
            return _records.size();
        }

        public Iterator getIterator(int index) {
            HashMap hash = (HashMap) _records.get(index);
            if (hash != null) {
                return hash.keySet().iterator();
            } else {
                return null;
            }
        }
    }

    /** @author Titus Meyer */
    private class Tables {

        private HashMap _tables = new HashMap();

        public void addTable(String table, boolean replace, HashMap restrict) {
            ArrayList array = new ArrayList();
            array.add(new Boolean(replace));
            array.add(restrict);
            _tables.put(table.toLowerCase(), array);
        }

        public boolean getReplace(String table) {
            try {
                ArrayList list = (ArrayList) _tables.get(table.toLowerCase());
                if (list != null) {
                    return ((Boolean) list.get(0)).booleanValue();
                }
            } catch (Exception e) {
            }
            return false;
        }

        public HashMap getRestrict(String table) {
            try {
                ArrayList list = (ArrayList) _tables.get(table.toLowerCase());
                if (list != null) {
                    return (HashMap) list.get(1);
                }
            } catch (Exception e) {
            }
            return null;
        }

        public int size() {
            return _tables.size();
        }

        public Iterator getIterator() {
            return _tables.keySet().iterator();
        }
        
        public boolean isEmpty() {
            return _tables.isEmpty();
        }
        
    }

    public SOSImport(SOSConnection conn, String fileName, String packageId, String packageElement, String packageValue, SOSStandardLogger log) {
        super();
        if (conn != null) {
            _conn = conn;
        }
        if (fileName != null) {
            _fileName = fileName;
        }
        if (packageId != null) {
            _packageId = packageId;
        }
        if (packageElement != null) {
            _packageElement = packageElement;
        }
        if (packageValue != null) {
            _packageValue = packageValue;
        }
        if (log != null) {
            _log = log;
        }
    }

    public SOSImport(SOSConnection conn, String fileName) {
        this(conn, fileName, null, null, null, null);
    }

    public SOSImport(SOSConnection conn, String fileName, String packageId, String packageElement, String packageValue) {
        this(conn, fileName, packageId, packageElement, packageValue, null);
    }

    public SOSImport() {
        this(null, null, null, null, null, null);
    }

    public void setConnection(SOSConnection conn) {
        _conn = conn;
    }

    public void setLogger(SOSStandardLogger log) {
        _log = log;
    }

    public void setAutoCommit(boolean autoCommit) {
        _autoCommit = autoCommit;
    }

    public void setUpdate(boolean update) {
        _enableUpdate = update;
    }

    public void setInsert(boolean insert) {
        _enableInsert = insert;
    }

    public void setFileName(String fileName) {
        _fileName = fileName;
    }

    public void setRestrictMode(boolean restrictMode) {
        _restrictMode = restrictMode;
    }

    public void setPackageId(String packageId) {
        _packageId = packageId;
    }

    public void setPackageValue(String packageValue) {
        _packageValue = packageValue;
    }

    public void setPackageElement(String packageElement) {
        _packageElement = packageElement;
    }

    public void setXMLTagname(String xmlTagname) {
        _xmlTagname = xmlTagname;
    }

    public void setXMLEncoding(String xmlEncoding) {
        _xmlEncoding = xmlEncoding;
    }

    public void setNormalizeFieldName(String normalizeFieldName) throws Exception {
        if (!"strtolower".equalsIgnoreCase(normalizeFieldName) && !"strtoupper".equalsIgnoreCase(normalizeFieldName)) {
            throw new IllegalArgumentException("SOSExport.setNormalizeFieldName: normalizeFielName must be \"strtolower\" or \"strtoupper\"");
        }
        try {
            _normalizeFieldName = normalizeFieldName;
            _conn.setFieldNameToUpperCase("strtoupper".equalsIgnoreCase(normalizeFieldName));
            if ("strtoupper".equalsIgnoreCase(normalizeFieldName)) {
                _conn.setKeysToUpperCase();
            } else {
                _conn.setKeysToLowerCase();
            }
        } catch (Exception e) {
            throw new Exception("SOSImport.setNormalizeFieldName: " + e.getMessage());
        }
    }

    public void setAutoNormalizeField(boolean auto) {
        _autoNormalizeField = auto;
    }

    public void startDocument() throws SAXException {
        _autoChecked = false;
        try {
            if (_log != null) {
                _log.debug2("Starte Import...");
            }
        } catch (Exception e) {
            throw new SAXException("SOSImport.startDocument: " + e.getMessage(), e);
        }
    }

    public void endDocument() throws SAXException {
        _autoChecked = false;
        try {
            _log.debug2("...beende Import");
        } catch (Exception e) {
            throw new SAXException("SOSImport.endDocument: " + e.getMessage(), e);
        }
    }

    public void startElement(String uri, String name, String qName, org.xml.sax.Attributes atts) throws SAXException {
        _curElement = name;
        try {
            if (_log != null) {
                _log.debug9("import startElement: " + name);
            }
            if (name.equalsIgnoreCase(_xmlTagname)) {
                _curExportOpen = true;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_package") && _curExportOpen) {
                _curPackageOpened = true;
                _curPackageId = atts.getValue("id");
            } else if (name.equalsIgnoreCase(_xmlTagname + "_meta") && _curPackageOpened) {
                _metaKeyRecords.add(_curPackageId);
                _metaFieldRecords.add(_curPackageId);
                _curMetaOpened = true;
            } else if ("table".equalsIgnoreCase(name) && _curMetaOpened) {
                _curMetaTableName = atts.getValue("name");
                if (mappingTablenames != null && mappingTablenames.get(_curMetaTableName) != null
                        && !mappingTablenames.get(_curMetaTableName).toString().isEmpty()) {
                    _log.debug("import tablename " + _curMetaTableName + " is mapping in " + mappingTablenames.get(_curMetaTableName));
                    _curMetaTableName = mappingTablenames.get(_curMetaTableName).toString();
                }
                _metaKeyRecords.get(_curPackageId).setTable(_curMetaTableName);
                _metaFieldRecords.get(_curPackageId).setTable(_curMetaTableName);
            } else if ("key_fields".equalsIgnoreCase(name) && _curMetaOpened) {
                _curMetaKeysOpened = true;
            } else if ("field".equalsIgnoreCase(name) && _curMetaKeysOpened) {
                if (_log != null) {
                    _log.debug9("add keyfield: name = " + normalizeFieldName(atts.getValue("name")) + " type = " + atts.getValue("type") + " typeID = "
                            + atts.getValue("typeID") + " len = " + atts.getValue("len") + " scale = " + atts.getValue("scale"));
                }
                String field = atts.getValue("name");
                if (_autoNormalizeField && !_autoChecked) {
                    autoNormalize(field);
                    _autoChecked = true;
                }
                _metaKeyRecords.get(_curPackageId).addField(normalizeFieldName(field), atts.getValue("type"), new Integer(atts.getValue("typeID")),
                        new BigInteger(atts.getValue("len")), new Integer(atts.getValue("scale")));
            } else if ("fields".equalsIgnoreCase(name) && _curMetaOpened) {
                _curMetaFieldsOpened = true;
            } else if ("field".equalsIgnoreCase(name) && _curMetaFieldsOpened) {
                if (_log != null) {
                    _log.debug9("add field: name = " + normalizeFieldName(atts.getValue("name")) + " type = " + atts.getValue("type") + " typeID = "
                            + atts.getValue("typeID") + " len = " + atts.getValue("len") + " scale = " + atts.getValue("scale"));
                }
                String field = atts.getValue("name");
                if (_autoNormalizeField && !_autoChecked) {
                    autoNormalize(field);
                    _autoChecked = true;
                }
                _metaFieldRecords.get(_curPackageId).addField(normalizeFieldName(field), atts.getValue("type"), new Integer(atts.getValue("typeID")),
                        new BigInteger(atts.getValue("len")), new Integer(atts.getValue("scale")));
            } else if (name.equalsIgnoreCase(_xmlTagname + "_data") && _curPackageOpened && !_curMetaOpened) {
                _curDataOpened = true;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_record")) {
                _curDataRecordOpened = true;
                _curDataRecordName = atts.getValue("name");
                _curPackageDepth++;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_fields") && _curDataRecordOpened) {
                _curDataFieldsOpened = true;
                // neuen Record beginnen
                if (_curBlockedPackageDepth <= 0 || _curBlockedPackageDepth > _curPackageDepth) {
                    _recordIndex = _records.addRecord();
                }
                if (atts.getValue("operation") != null && !atts.getValue("operation").isEmpty()) {
                    _operation = atts.getValue("operation");
                }
            } else if (_curDataFieldsOpened) {
                _curDataFieldOpened = true;
                _curDataFieldName = normalizeFieldName(name);
                _curDataFieldData = new StringBuilder();
                _curDataFieldNull = atts.getValue("null") != null && "true".equalsIgnoreCase(atts.getValue("null"));
                _records.addValue(_recordIndex, _curDataFieldName, _curDataFieldData.toString());
            }
        } catch (Exception e) {
            throw new SAXException("SOSImport.startElement: " + e.getMessage(), e);
        }
    }

    public void endElement(String uri, String name, String qName) throws SAXException {
        try {
            if (_log != null) {
                _log.debug9("import endElement: " + name);
            }
            if (name.equalsIgnoreCase(_xmlTagname)) {
                _curExportOpen = false;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_package")) {
                _curPackageOpened = false;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_meta")) {
                _curMetaOpened = false;
            } else if ("key_fields".equalsIgnoreCase(name)) {
                _curMetaKeysOpened = false;
            } else if ("fields".equalsIgnoreCase(name)) {
                _curMetaFieldsOpened = false;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_data")) {
                _curDataOpened = false;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_record")) {
                _curDataRecordOpened = false;
                if (_packageId != null && _curPackageDepth == _curImportPackageDepth) {
                    _curImportPackage = 0;
                }
                if (_curBlockedPackageDepth == _curPackageDepth) {
                    _curBlockedPackageDepth = 0;
                }
                _curPackageDepth--;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_fields")) {
                _curDataFieldsOpened = false;
                if (_curImportPackage == 1 && (_curBlockedPackageDepth == 0 || _curPackageDepth == 0)) {
                    // /// IMPORT des Records ausfuehren /////
                    if (_operation != null && "insert".equals(_operation)) {
                        importRecord(_curDataRecordName, _recordIndex, _operation);
                    } else if (_operation != null && "update".equals(_operation)) {
                        importRecord(_curDataRecordName, _recordIndex, _operation);
                    } else if (_operation != null && "delete".equals(_operation)) {
                        deleteRecord(_curDataRecordName, _recordIndex);
                    } else {
                        importRecord(_curDataRecordName, _recordIndex);
                    }
                    _operation = null;
                }
            } else if (_curDataFieldOpened) {
                if (_curDataFieldNull) {
                    _records.addValue(_recordIndex, _curDataFieldName, null);
                } else {
                    _records.addValue(_recordIndex, _curDataFieldName, _curDataFieldData.toString());
                }
                if (_curImportPackage <= 0 && _curDataRecordName.equalsIgnoreCase(_packageId) && _curElement.equalsIgnoreCase(_packageElement)
                        && _curDataFieldData.toString().equalsIgnoreCase(_packageValue)) {
                    _curImportPackage = 1;
                    _curImportPackageDepth = _curPackageDepth;
                }
                _curDataFieldOpened = false;
                _curDataFieldName = null;
                _curDataFieldData = null;
            }
        } catch (Exception e) {
            throw new SAXException("SOSImport.endElement: " + e.getMessage(), e);
        }
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if (_curDataFieldOpened) {
            String str = new String(ch, start, length);
            if (!"".equals(str)) {
                _curDataFieldData.append(str);
            }
        }
    }

    public void setTable(String table, boolean replace, HashMap restrict) throws Exception {
        try {
            if (table == null || "".equals(table)) {
                throw new IllegalArgumentException("SOSImport.setTable: you have to give a table name");
            }
            if (_log != null) {
                _log.debug3("setTable: table=" + table + " replace=" + replace);
            }
            _tables.addTable(table, replace, restrict);
        } catch (Exception e) {
            throw new Exception("SOSImport.setTable: " + e.getMessage(), e);
        }
    }

    public void setHandler(String table, String keyHandler, String recordHandler, String recordIdentifier) {
        if (table == null || "".equals(table)) {
            throw new IllegalArgumentException("SOSImport.setHandler: you have to give a table name");
        }
        if (keyHandler != null && !"".equals(keyHandler)) {
            _keyHandler.put(table.toLowerCase(), keyHandler);
        }
        if (recordHandler != null && !"".equals(recordHandler)) {
            _recordHandler.put(table.toLowerCase(), recordHandler);
        }
        if (recordIdentifier != null && !"".equals(recordIdentifier)) {
            _recordIdentifier.put(table.toLowerCase(), recordIdentifier);
        }
    }

    public void doImport() throws Exception, SAXException, FileNotFoundException {
        try {
            if ("strtoupper".equalsIgnoreCase(_normalizeFieldName)) {
                _conn.setKeysToUpperCase();
                _conn.setFieldNameToUpperCase(true);
            } else {
                _conn.setKeysToLowerCase();
                _conn.setFieldNameToUpperCase(false);
            }
            SAXParser parser = new SAXParser();
            parser.setContentHandler(this);
            File file = new File(_fileName);
            if (!file.canRead()) {
                throw new FileNotFoundException("File not found: " + _fileName);
            }
            if (_log != null) {
                _log.debug3("Using file: " + _fileName);
            }
            parser.parse(_fileName);
        } catch (SAXException e) {
            throw new SAXException("SOSImport.doImport: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("SOSImport.doImport: " + e.getMessage(), e);
        }
    }

    public void doImport(SOSConnection conn, String fileName) throws Exception, SAXException, FileNotFoundException {
        try {
            if (conn != null) {
                _conn = conn;
            }
            if (fileName != null) {
                _fileName = fileName;
            }
            if (_packageId != null) {
                _curImportPackage = 0;
            }
            doImport();
        } catch (SAXException e) {
            throw new SAXException("SOSImport.doImport: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception("SOSImport.doImport: " + e.getMessage(), e);
        }
    }

    public void doImport(SOSConnection conn, String fileName, String packageId, String packageElement, String packageValue) throws Exception, SAXException,
            FileNotFoundException {
        try {
            if (conn != null) {
                _conn = conn;
            }
            if (fileName != null) {
                _fileName = fileName;
            }
            if (packageId != null) {
                _packageId = packageId;
            }
            if (packageElement != null) {
                _packageElement = packageElement;
            }
            if (packageValue != null) {
                _packageValue = packageValue;
            }
            if (_packageId != null) {
                _curImportPackage = 0;
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
            if (_log != null) {
                _log.debug3("import_record(" + name + ", " + index + "): key_fields="
                        + normalizeFieldName(_metaKeyRecords.get(name).getKeyString() + " table=" + _metaKeyRecords.get(name).getTable()));
            }
            if (_conn == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = _restrictMode;
            HashMap restrictObject = _restrictObject;
            if (!_tables.isEmpty()) {
                boolean found = false;
                for (Iterator it = _tables.getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    if (name.equalsIgnoreCase(key)) {
                        found = true;
                        restrictMode = _tables.getReplace(key);
                        restrictObject = _tables.getRestrict(key);
                        break;
                    }
                }
                if (!found) {
                    if (_log != null) {
                        _log.debug3("Import denied for table by restriction: " + name);
                    }
                    return;
                }
            }
            if (restrictObject != null && !restrictObject.isEmpty()) {
                for (Iterator it = restrictObject.keySet().iterator(); it.hasNext();) {
                    String restrictName = it.next().toString();
                    String restrictValue = (String) restrictObject.get(restrictName);
                    if (_log != null) {
                        _log.debug3("checking for element: " + restrictName + "=" + restrictValue);
                    }
                    if (_records.getValue(index, restrictName) == null) {
                        _curBlockedPackageDepth = _curPackageDepth;
                        if (_log != null) {
                            _log.debug3("import denied for element by restriction at depth " + _curBlockedPackageDepth + ": " + restrictName + " not in record");
                        }
                        return;
                    }
                    if (_records.getValue(index, restrictName) != null && restrictValue.equalsIgnoreCase(_records.getValue(index, restrictName))) {
                        _curBlockedPackageDepth = _curPackageDepth;
                        if (_log != null) {
                            _log.debug3("import denied for element by restriction at depth " + _curBlockedPackageDepth + ": " + restrictName + ": "
                                    + restrictValue + " != " + _records.getValue(index, restrictName));
                        }
                        return;
                    }
                }
                restrictObject = null;
            }
            HashMap keys = new HashMap();
            for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                if (_log != null) {
                    _log.debug6("key_fields key [" + key + "] = " + _records.getValue(index, key));
                }
                keys.put(normalizeFieldName(key), _records.getValue(index, key));
            }
            String keyHandler = _metaKeyRecords.get(name).getTable().toLowerCase();
            if (_keyHandler.containsKey(keyHandler)) {
                if (_log != null) {
                    _log.debug6("key_handler: " + (String) _keyHandler.get(keyHandler));
                }
                Class[] params = new Class[] { HashMap.class };
                Method method = getClass().getMethod((String) _keyHandler.get(keyHandler), params);
                HashMap[] args = { keys };
                keys = (HashMap) method.invoke(this, args);
            }
            HashMap record = null;
            boolean isNew = false;
            if (operation != null && "insert".equalsIgnoreCase(operation)) {
                isNew = true;
            } else if (operation != null && "update".equalsIgnoreCase(operation)) {
                isNew = false;
            } else {
                if (keys != null && !keys.isEmpty() && restrictMode) {
                    StringBuilder stm = new StringBuilder();
                    stm.append("SELECT * FROM " + _metaFieldRecords.get(name).getTable());
                    String and = " WHERE ";
                    for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
                        String key = it.next().toString();
                        stm.append(and + "\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) keys.get(key)));
                        and = " AND ";
                    }
                    record = _conn.getSingle(stm.toString());
                }
                isNew = (record == null || record.isEmpty());
            }
            if (_log != null) {
                _log.debug6("is_new: " + isNew);
            }
            record = new HashMap();
            for (Iterator it = _records.getIterator(index); it.hasNext();) {
                String key = it.next().toString();
                String val = _records.getValue(index, key);
                if (_log != null) {
                    if (val != null) {
                        _log.debug9("record[" + key + "] = " + (val.length() > 255 ? "(" + val.length() + " Chars)" : val));
                    } else {
                        _log.debug9("record[" + key + "] = NULL");
                    }
                }
                record.put(key, fromXML(val));
            }
            String record_handler = _metaFieldRecords.get(name).getTable().toLowerCase();
            if (_recordHandler.containsKey(record_handler)) {
                String recordIdentifier = _recordIdentifier.containsKey(record_handler) ? (String) _recordIdentifier.get(record_handler) : null;
                if (_log != null) {
                    _log.debug6("record_handler: " + (String) _recordHandler.get(record_handler));
                }
                Class[] params = new Class[] { HashMap.class, HashMap.class, String.class };
                Method method = getClass().getMethod((String) _recordHandler.get(record_handler), params);
                Object[] args = { keys, record, recordIdentifier };
                record = (HashMap) method.invoke(this, args);
                HashMap recordKeys = new HashMap();
                for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    recordKeys.put(normalizeFieldName(key), _records.getValue(index, key));
                }
                if (record != null && !record.isEmpty()) {
                    for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
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
                if (_keyHandler.containsKey(keyHandler)) {
                    if (isNewKey) {
                        for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            if (_log != null) {
                                _log.debug6("updating key_fields key[" + key + "] = " + record.get(key));
                            }
                            keys.put(key, (String) record.get(key));
                        }
                    } else {
                        for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            if (_log != null) {
                                _log.debug6("updating key_fields record[" + key + "] = " + record.get(key));
                            }
                            record.put(key, (String) keys.get(key));
                        }
                    }
                }
                HashMap blobs = getBlobs(name, record);
                if (isNew) {
                    if (!_enableInsert) {
                        if (_log != null) {
                            _log.debug1("record skipped: no insert enabled");
                        }
                    } else {
                        insertRecord(name, record);
                    }
                } else {
                    if (!_enableUpdate) {
                        if (_log != null) {
                            _log.debug1("record skipped: no update enabled");
                        }
                    } else {
                        updateRecord(name, keys, record);
                    }
                }
                updateBlob(name, blobs, keys, record);
            } else {
                if (_log != null) {
                    _log.debug6("record skipped");
                }
            }
            if (_autoCommit) {
                _conn.commit();
            }
        } catch (Exception e) {
            if (_autoCommit) {
                _conn.rollback();
            }
            _log.error("" + e.getMessage());
            throw new Exception("SOSImport.import_record: " + e.getMessage(), e);
        }
    }

    private void deleteRecord(String name, int index) throws Exception {
        try {
            if (_log != null) {
                _log.debug3("delete_record(" + name + ", " + index + "): key_fields="
                        + normalizeFieldName(_metaKeyRecords.get(name).getKeyString() + " table=" + _metaKeyRecords.get(name).getTable()));
            }
            if (_conn == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = _restrictMode;
            HashMap restrictObject = _restrictObject;
            if (!_tables.isEmpty()) {
                boolean found = false;
                for (Iterator it = _tables.getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    if (name.equalsIgnoreCase(key)) {
                        found = true;
                        restrictMode = _tables.getReplace(key);
                        restrictObject = _tables.getRestrict(key);
                        break;
                    }
                }
                if (!found) {
                    if (_log != null) {
                        _log.debug3("Import denied for table by restriction: " + name);
                    }
                    return;
                }
            }
            if (restrictObject != null && !restrictObject.isEmpty()) {
                for (Iterator it = restrictObject.keySet().iterator(); it.hasNext();) {
                    String restrictName = it.next().toString();
                    String restrictValue = (String) restrictObject.get(restrictName);
                    if (_log != null) {
                        _log.debug3("checking for element: " + restrictName + "=" + restrictValue);
                    }
                    if (_records.getValue(index, restrictName) == null) {
                        _curBlockedPackageDepth = _curPackageDepth;
                        if (_log != null) {
                            _log.debug3("import denied for element by restriction at depth " + _curBlockedPackageDepth + ": " + restrictName + " not in record");
                        }
                        return;
                    }
                    if (_records.getValue(index, restrictName) != null && restrictValue.equalsIgnoreCase(_records.getValue(index, restrictName))) {
                        _curBlockedPackageDepth = _curPackageDepth;
                        if (_log != null) {
                            _log.debug3("import denied for element by restriction at depth " + _curBlockedPackageDepth + ": " + restrictName + ": "
                                    + restrictValue + " != " + _records.getValue(index, restrictName));
                        }
                        return;
                    }
                }
                restrictObject = null;
            }
            HashMap keys = new HashMap();
            for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                if (_log != null) {
                    _log.debug6("key_fields key [" + key + "] = " + _records.getValue(index, key));
                }
                keys.put(normalizeFieldName(key), _records.getValue(index, key));
            }
            String keyHandler = _metaKeyRecords.get(name).getTable().toLowerCase();
            if (_keyHandler.containsKey(keyHandler)) {
                if (_log != null) {
                    _log.debug6("key_handler: " + (String) _keyHandler.get(keyHandler));
                }
                Class[] params = new Class[] { HashMap.class };
                Method method = getClass().getMethod((String) _keyHandler.get(keyHandler), params);
                HashMap[] args = { keys };
                keys = (HashMap) method.invoke(this, args);
            }
            deleteRecord(name, keys);
            if (_autoCommit) {
                _conn.commit();
            }
        } catch (Exception e) {
            if (_autoCommit) {
                _conn.rollback();
            }
            _log.error("" + e.getMessage());
            throw new Exception("SOSImport.import_record: " + e.getMessage(), e);
        }
    }

    private void importRecord(String name, int index) throws Exception {
        boolean isNewKey = false;
        try {
            if (_log != null) {
                _log.debug3("import_record(" + name + ", " + index + "): key_fields="
                        + normalizeFieldName(_metaKeyRecords.get(name).getKeyString() + " table=" + _metaKeyRecords.get(name).getTable()));
            }
            if (_conn == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = _restrictMode;
            HashMap restrictObject = _restrictObject;
            if (!_tables.isEmpty()) {
                boolean found = false;
                for (Iterator it = _tables.getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    if (name.equalsIgnoreCase(key)) {
                        found = true;
                        restrictMode = _tables.getReplace(key);
                        restrictObject = _tables.getRestrict(key);
                        break;
                    }
                }
                if (!found) {
                    if (_log != null) {
                        _log.debug3("Import denied for table by restriction: " + name);
                    }
                    return;
                }
            }
            if (restrictObject != null && !restrictObject.isEmpty()) {
                for (Iterator it = restrictObject.keySet().iterator(); it.hasNext();) {
                    String restrictName = it.next().toString();
                    String restrictValue = (String) restrictObject.get(restrictName);
                    if (_log != null) {
                        _log.debug3("checking for element: " + restrictName + "=" + restrictValue);
                    }
                    if (_records.getValue(index, restrictName) == null) {
                        _curBlockedPackageDepth = _curPackageDepth;
                        if (_log != null) {
                            _log.debug3("import denied for element by restriction at depth " + _curBlockedPackageDepth + ": " + restrictName + " not in record");
                        }
                        return;
                    }
                    if (_records.getValue(index, restrictName) != null && restrictValue.equalsIgnoreCase(_records.getValue(index, restrictName))) {
                        _curBlockedPackageDepth = _curPackageDepth;
                        if (_log != null) {
                            _log.debug3("import denied for element by restriction at depth " + _curBlockedPackageDepth + ": " + restrictName + ": "
                                    + restrictValue + " != " + _records.getValue(index, restrictName));
                        }
                        return;
                    }
                }
                restrictObject = null;
            }
            HashMap keys = new HashMap();
            for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                if (_log != null) {
                    _log.debug6("key_fields key [" + key + "] = " + _records.getValue(index, key));
                }
                keys.put(normalizeFieldName(key), _records.getValue(index, key));
            }
            String keyHandler = _metaKeyRecords.get(name).getTable().toLowerCase();
            if (_keyHandler.containsKey(keyHandler)) {
                if (_log != null) {
                    _log.debug6("key_handler: " + (String) _keyHandler.get(keyHandler));
                }
                Class[] params = new Class[] { HashMap.class };
                Method method = getClass().getMethod((String) _keyHandler.get(keyHandler), params);
                HashMap[] args = { keys };
                keys = (HashMap) method.invoke(this, args);
            }
            HashMap record = null;
            if (keys != null && !keys.isEmpty() && restrictMode) {
                StringBuilder stm = new StringBuilder();
                stm.append("SELECT * FROM " + _metaFieldRecords.get(name).getTable());
                String and = " WHERE ";
                for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
                    String key = it.next().toString();
                    stm.append(and + "\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) keys.get(key)));
                    and = " AND ";
                }
                record = _conn.getSingle(stm.toString());
            }
            boolean isNew = record == null || record.isEmpty();
            if (_log != null) {
                _log.debug6("is_new: " + isNew);
            }
            record = new HashMap();
            for (Iterator it = _records.getIterator(index); it.hasNext();) {
                String key = it.next().toString();
                String val = _records.getValue(index, key);
                if (_log != null) {
                    if (val != null) {
                        _log.debug9("record[" + key + "] = " + (val.length() > 255 ? "(" + val.length() + " Chars)" : val));
                    } else {
                        _log.debug9("record[" + key + "] = NULL");
                    }
                }
                record.put(key, fromXML(val));
            }
            String record_handler = _metaFieldRecords.get(name).getTable().toLowerCase();
            if (_recordHandler.containsKey(record_handler)) {
                String recordIdentifier = _recordIdentifier.containsKey(record_handler) ? (String) _recordIdentifier.get(record_handler) : null;
                if (_log != null) {
                    _log.debug6("record_handler: " + (String) _recordHandler.get(record_handler));
                }
                Class[] params = new Class[] { HashMap.class, HashMap.class, String.class };
                Method method = getClass().getMethod((String) _recordHandler.get(record_handler), params);
                Object[] args = { keys, record, recordIdentifier };
                record = (HashMap) method.invoke(this, args);
                HashMap recordKeys = new HashMap();
                for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                    String key = it.next().toString();
                    recordKeys.put(normalizeFieldName(key), _records.getValue(index, key));
                }
                if (record != null && !record.isEmpty()) {
                    for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
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
                if (_keyHandler.containsKey(keyHandler)) {
                    if (isNewKey) {
                        for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            if (_log != null) {
                                _log.debug6("updating key_fields key[" + key + "] = " + record.get(key));
                            }
                            keys.put(key, (String) record.get(key));
                        }
                    } else {
                        for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                            String key = normalizeFieldName(it.next().toString());
                            if (_log != null) {
                                _log.debug6("updating key_fields record[" + key + "] = " + record.get(key));
                            }
                            record.put(key, (String) keys.get(key));
                        }
                    }
                }
                HashMap blobs = getBlobs(name, record);
                if (isNew) {
                    if (!_enableInsert) {
                        if (_log != null) {
                            _log.debug1("record skipped: no insert enabled");
                        }
                    } else {
                        insertRecord(name, record);
                    }
                } else {
                    if (!_enableUpdate) {
                        if (_log != null) {
                            _log.debug1("record skipped: no update enabled");
                        }
                    } else {
                        updateRecord(name, keys, record);
                    }
                }
                updateBlob(name, blobs, keys, record);
            } else {
                if (_log != null) {
                    _log.debug6("record skipped");
                }
            }
            if (_autoCommit) {
                _conn.commit();
            }
        } catch (Exception e) {
            if (_autoCommit) {
                _conn.rollback();
            }
            _log.error("" + e.getMessage());
            throw new Exception("SOSImport.import_record: " + e.getMessage(), e);
        }
    }

    private HashMap getBlobs(String name, HashMap record) throws Exception {
        try {
            HashMap blobs = new HashMap();
            for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                String xmlVal = (String) record.get(key);
                byte[] val = fromHexString(xmlVal);
                String blobType = null;
                ArrayList obj = new ArrayList(2);
                switch (_metaFieldRecords.get(name).getTypeId(key)) {
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
                if (_log != null) {
                    _log.debug6("found " + blobType + ": name = " + key + " type = " + _metaFieldRecords.get(name).getTypeName(key) + " typeID = "
                            + _metaFieldRecords.get(name).getTypeId(key));
                }
                obj.add(blobType);
                blobs.put(key, obj);
                record.put(key, null);
            }
            return blobs;
        } catch (Exception e) {
            throw new Exception("SOSImport.getBlobs: " + e.getMessage(), e);
        }
    }

    private void updateBlob(String name, HashMap blobs, HashMap keys, HashMap record) throws Exception {
        try {
            for (Iterator it = blobs.keySet().iterator(); it.hasNext();) {
                String blobName = it.next().toString();
                ArrayList blobValue = (ArrayList) blobs.get(blobName);
                if (_log != null) {
                    _log.debug6("update " + (String) blobValue.get(0) + ": " + blobName);
                }
                StringBuilder stm = new StringBuilder();
                String and = "";
                for (Iterator it2 = keys.keySet().iterator(); it2.hasNext();) {
                    String key = it2.next().toString();
                    if (!record.containsKey(key)) {
                        throw new Exception("SOSImport.import_record: missing key in record: " + key);
                    }
                    stm.append(and + " \"" + normalizeFieldName(key) + "\"=" + quote(name, normalizeFieldName(key), (String) record.get(key)));
                    and = " AND ";
                }
                if (blobValue.get(1) != null) {
                    if ("blob".equals((String) blobValue.get(0))) {
                        _conn.updateBlob(_metaFieldRecords.get(name).getTable(), blobName, (byte[]) blobValue.get(1), stm.toString());
                    } else {
                        _conn.updateClob(_metaFieldRecords.get(name).getTable(), blobName, (String) blobValue.get(1), stm.toString());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("SOSImport.updateBlob: " + e.getMessage(), e);
        }
    }

    private void insertRecord(String name, HashMap record) throws Exception {
        try {
            if (_log != null) {
                _log.debug6("inserting record");
            }
            StringBuilder fields = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                fields.append("\"" + key + "\"");
                values.append(quote(name, key, (String) record.get(key)));
                if (it.hasNext()) {
                    fields.append(", ");
                    values.append(", ");
                }
            }
            _conn.execute("INSERT INTO " + _metaFieldRecords.get(name).getTable() + " (" + fields + ") VALUES(" + values + ")");
        } catch (Exception e) {
            throw new Exception("SOSImport.insertRecord: " + e.getMessage(), e);
        }
    }

    private void updateRecord(String name, HashMap keys, HashMap record) throws Exception {
        try {
            if (_log != null) {
                _log.debug6("updating record");
            }
            StringBuilder stm = new StringBuilder("UPDATE " + _metaFieldRecords.get(name).getTable() + " SET ");
            for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                stm.append("\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) record.get(key)));
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
            _conn.execute(stm.toString());
        } catch (Exception e) {
            throw new Exception("SOSImport.updateRecord: " + e.getMessage(), e);
        }
    }

    private void deleteRecord(String name, HashMap keys) throws Exception {
        try {
            if (_log != null) {
                _log.debug6("delete record");
            }
            StringBuilder stm = new StringBuilder("DELETE FROM " + _metaFieldRecords.get(name).getTable());
            String and = " WHERE ";
            for (Iterator it = keys.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                if (keys.get(key) != null) {
                    stm.append(and + "\"" + normalizeFieldName(key) + "\"=" + quote(name, key, (String) keys.get(key)));
                    and = " AND ";
                }
            }
            if (_log != null) {
                _log.debug9("deleted record:" + stm.toString());
            }
            _conn.execute(stm.toString());
        } catch (Exception e) {
            throw new Exception("SOSImport.updateRecord: " + e.getMessage(), e);
        }
    }

    private String quote(String package_id, String field, String val) {
        if (val == null) {
            return "NULL";
        }

        switch (_metaFieldRecords.get(package_id).getTypeId(field)) {
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
            if (_conn instanceof SOSMySQLConnection) {
                val = val.replaceAll("\\\\", "\\\\\\\\");
            }
            return "'" + val + "'";
        }
    }

    private void autoNormalize(String field) throws Exception {
        String copy = new String(field);
        try {
            if (field.equals(copy.toLowerCase())) {
                if (_log != null) {
                    _log.debug6("Auto Normalisation: lower case");
                }
                setNormalizeFieldName("strtolower");
            } else if (field.equals(copy.toUpperCase())) {
                _log.debug6("Auto Normalisation: upper case");
                setNormalizeFieldName("strtoupper");
            } else {
                _log.debug6("Auto Normalisation: upper case");
                setNormalizeFieldName("strtoupper");
            }
        } catch (Exception e) {
            throw new Exception("SOSImport.autoNormalize: " + e.getMessage());
        }
    }

    private String normalizeFieldName(String field) {
        if ("strtoupper".equalsIgnoreCase(_normalizeFieldName)) {
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
        char c_low;
        char c_high;
        int i = 0, j = 0;
        while (i < strLength) {
            c_low = s.charAt(i);
            if (c_low == ' ' || c_low == '\n' || c_low == '\t') {
                i++;
                continue;
            }
            c_high = s.charAt(i + 1);
            int high = charToNibble(c_low);
            int low = charToNibble(c_high);
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