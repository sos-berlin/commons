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

/** <p>
 * Title: SOSExport
 * </p>
 * <p>
 * Description: Importieren von Daten im XML-Format.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: SOS-Berlin GmbH
 * </p>
 *
 * @author Titus Meyer
 * @version 1.5.6 */
public class SOSImport extends DefaultHandler {

    /** Export DB-Connection der Klasse SOSConnection */
    private SOSConnection _conn = null;
    /** StandardLogger der Klasse SOSStandardLogger */
    private SOSStandardLogger _log = null;
    /** Jede Transaktion automatisch abschlie&szlig;en */
    private boolean _autoCommit = false;
    /** Einf&uuml;gen neuer S&auml;tze zulassen */
    private boolean _enableInsert = true;
    /** Aktualisieren vorhandener S&auml;tze erlauben */
    private boolean _enableUpdate = true;
    /** Dateipfad der Import-Datei */
    private String _fileName = null;
    /** Import-Mode: false = neue Records erstellen / true = existierende Records &uuml;berschreiben */
    private boolean _restrictMode = true;
    /** Lfd. Nr. des Export-Pakets in Export-Datei */
    private String _packageId = null;
    /** Name des Elements mit dem Paket-Tag */
    private String _packageElement = null;
    /** Wert des Elements mit dem Paket-Tag */
    private String _packageValue = null;
    /** Name des Tags, das den Export umklammert */
    private String _xmlTagname = "sos_export";
    /** Zeichensatz */
    private String _xmlEncoding = "iso-8859-1";
    /** Feldnormalisierung strtolower / strtoupper (default) */
    private String _normalizeFieldName = "strtoupper";
    /** Feldnormalisierung wird aus dem Import &uuml;bernommen */
    private boolean _autoNormalizeField = true;
    private boolean _autoChecked = false;
    /** Name/Wert-Paare, die im Import &uuml;bersprungen werden */
    private HashMap _restrictObject = new HashMap();
    /** Funktionsnamen zu einzelnen Tabellen zur Schl&uuml;sselmanipulation */
    private HashMap _keyHandler = new HashMap();
    /** Funktionsnamen zu einzelnen Tabellen zur Recordmanipulation */
    private HashMap _recordHandler = new HashMap();
    /** Importierbare Tabellen */
    private Tables _tables = new Tables();
    /** Schl&uuml;sselfeldbeschreibungen der einzelnen Tabellen */
    private MetaRecords _metaKeyRecords = new MetaRecords();
    /** Tabellenfeldbeschreibungen der einzelnen Tabellen */
    private MetaRecords _metaFieldRecords = new MetaRecords();
    /** Liste der Records */
    private Records _records = new Records();
    /** Index des aktuellen Records */
    private int _recordIndex = -1;
    /** Hash mit Tabellen- und Feldnamen f&uuml;r Dublettenabgleich */
    private HashMap _recordIdentifier = new HashMap();
    // Parserflags
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
    // operation = update, insert oder delete
    private String _operation = null;
    // beinhaltet Tabellennamen, die umbenannt werden sollen: key=alte Tabellenname, value=neue Tabellenname
    private HashMap mappingTablenames = null;

    /** <p>
     * Title: MetaRecords
     * </p>
     * <p>
     * Description: Liste zur Speicherung mehrerer Tabellenfeldbeschreibungen
     * </p>
     * <p>
     * Copyright: Copyright (c) 2004
     * </p>
     * <p>
     * Company: SOS-Berlin GmbH
     * </p>
     *
     * @author Titus Meyer
     * @version 1.0 */
    private class MetaRecords {

        /** Eine HashMap von Tabellenbeschreibungen */
        private HashMap _metaRecords = new HashMap();

        /** Liefert zu einem Tabellennamen die Feldbeschreibungen - sonst null.
         *
         * @param packageId Tabellenname
         * @return */
        public SOSImExportTableFieldTypes get(String packageId) {
            return (SOSImExportTableFieldTypes) _metaRecords.get(packageId);
        }

        /** F&uuml;gt ein neues SOSTableFieldTypes Objekt hinzu, welches darauf
         * gef&uuml;llt werden kann.
         *
         * @param packageId Tabellenname, unter dem die Felddaten gespeichert
         *            werden sollen */
        public void add(String packageId) {
            _metaRecords.put(packageId, new SOSImExportTableFieldTypes());
        }
    }

    /** <p>
     * Title: SOSExport
     * </p>
     * <p>
     * Description: Liste zur Speicherung mehrerer Records.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2004
     * </p>
     * <p>
     * Company: SOS-Berlin GmbH
     * </p>
     *
     * @author Titus Meyer
     * @version 1.0 */
    private class Records {

        /** Arrayliste zur Aufnahme mehrerer Records */
        private ArrayList _records = new ArrayList();

        /** F&uuml;gt einen leeren Record hinzu und gibt deren Index zur&uuml;ck.
         *
         * @return Index des letzten neuen Records. */
        public int addRecord() {
            _records.add(new HashMap());
            return _records.size() - 1;
        }

        /** F&uuml;gt ein Schl&uuml;ssel/Wert-Paar in einen Record ein.
         *
         * @param index Index des Records in der Liste
         * @param key Schl&uuml;ssel
         * @param val Wert */
        public void addValue(int index, String key, String val) {
            HashMap hash = (HashMap) _records.get(index);
            if (hash != null) {
                hash.put(normalizeFieldName(key), val);
            }
        }

        /** Liefert den Wert zu einem Schl&uuml;ssel in einem Record.
         *
         * @param index Index des Records in der Liste
         * @param key Schl&uuml;ssel
         * @return Wert/null */
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

        /** Liefert die Anzahl der Records in der Liste.
         *
         * @return Recordanzahl */
        public int size() {
            return _records.size();
        }

        /** Liefert einen Iterator &uuml;ber einen Record aus der Liste.
         *
         * @param index Index des Records in der Liste
         * @return Key-Iterator */
        public Iterator getIterator(int index) {
            HashMap hash = (HashMap) _records.get(index);
            if (hash != null) {
                return hash.keySet().iterator();
            } else {
                return null;
            }
        }
    }

    /** <p>
     * Title: SOSExport
     * </p>
     * <p>
     * Description: Liste zur Speicherung von Tabellennamen und deren Attribute
     * Replace und Restrict
     * </p>
     * <p>
     * Copyright: Copyright (c) 2004
     * </p>
     * <p>
     * Company: SOS-Berlin GmbH
     * </p>
     *
     * @author Titus Meyer
     * @version 1.0 */
    private class Tables {

        private HashMap _tables = new HashMap();

        /** F&uuml;gt eine neue Tabelle hinzu bzw. &auml;ndert eine vorhandene.
         *
         * @param table Tabellenname
         * @param replace Gibt an, ob Updates durchgef&uuml;hrt werden
         *            d&uuml;rfen
         * @param restrict Name/Wert-Paare zur Einschr&auml;nkung des Imports */
        public void addTable(String table, boolean replace, HashMap restrict) {
            ArrayList array = new ArrayList();
            array.add(new Boolean(replace));
            array.add(restrict);
            _tables.put(table.toLowerCase(), array);
        }

        /** Liefert den Replace-Status einer Tabelle.
         *
         * @param table Name der Tabelle
         * @return Replace-Status der Tabelle oder null */
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

        /** Liefert die Restriktionen einer Tabelle.
         *
         * @param table Name der Tabelle
         * @return Restriktionen der Tabelle oder null */
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

        /** Liefert die Anzahl der Tabellen.
         *
         * @return Tabellenanzahl */
        public int size() {
            return _tables.size();
        }

        /** Liefert einen Iterator &uuml;ber die Tabellennamen.
         *
         * @return Iterator */
        public Iterator getIterator() {
            return _tables.keySet().iterator();
        }
    }

    /** Konstruktor
     *
     * @param conn Verbindungsobjekt der Klasse SOSConnection
     * @param fileName Pfad der Export-Datei
     * @param packageId Lfd. Nr. des Export-Pakets in der Export-Datei
     * @param packageElement Name des Elements mit dem Paket-Tag
     * @param packageValue Wert des Elements mit dem Paket-Tag
     * @param log Loggerobjekt der Klasse SOSStandardLogger */
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

    /** Konstruktor
     *
     * @param conn Verbindungsobjekt der Klasse SOSConnection
     * @param fileName Pfad der Export-Datei */
    public SOSImport(SOSConnection conn, String fileName) {
        this(conn, fileName, null, null, null, null);
    }

    /** Konstruktor
     *
     * @param conn Verbindungsobjekt der Klasse SOSConnection
     * @param fileName Pfad der Export-Datei
     * @param packageId Lfd. Nr. des Export-Pakets in Export-Datei
     * @param packageElement Name des Elements mit dem Paket-Tag
     * @param packageValue Wert des Elements mit dem Paket-Tag */
    public SOSImport(SOSConnection conn, String fileName, String packageId, String packageElement, String packageValue) {
        this(conn, fileName, packageId, packageElement, packageValue, null);
    }

    /** Konstruktor */
    public SOSImport() {
        this(null, null, null, null, null, null);
    }

    /** Setzt das Verbindungsobjekt.
     *
     * @param conn Datenbankobjekt */
    public void setConnection(SOSConnection conn) {
        _conn = conn;
    }

    /** Setzt das Logger/Debugger-Objekt.
     *
     * @param log Loggerobjekt */
    public void setLogger(SOSStandardLogger log) {
        _log = log;
    }

    /** AutoCommit-Modus setzten f&uuml;r automatischen Transaktionsabschluss.
     * Default: false
     *
     * @param autoCommit true f&uuml;r AutoCommit */
    public void setAutoCommit(boolean autoCommit) {
        _autoCommit = autoCommit;
    }

    /** Setzt den Update-Modus, der das Updaten von vorhandenen Datens&auml;tzen
     * erlaubt. Default: true
     *
     * @param update true f&uuml;r Updates */
    public void setUpdate(boolean update) {
        _enableUpdate = update;
    }

    /** Setzt den Insert-Modus, der das Einf&uuml;gen von neuen Datens&auml;tzen
     * erlaubt. Default: true
     *
     * @param insert true f&uuml;r Inserts */
    public void setInsert(boolean insert) {
        _enableInsert = insert;
    }

    /** Setzt den Pfad der Datei, aus der importiert werden soll
     *
     * @param fileName Pfad der Datei */
    public void setFileName(String fileName) {
        _fileName = fileName;
    }

    /** Setzt den Import-Modus. Bei false werden neue Records erstellt und bei
     * true werden vorhandene Records &uuml;berschrieben.
     *
     * @param restrictMode Import-Modus */
    public void setRestrictMode(boolean restrictMode) {
        _restrictMode = restrictMode;
    }

    /** Setzt die Package-ID des Imports.
     *
     * @param packageId Package-ID */
    public void setPackageId(String packageId) {
        _packageId = packageId;
    }

    /** Setzt den Wert des Import-Elementnamen.
     *
     * @param packageValue Wert des Elementes */
    public void setPackageValue(String packageValue) {
        _packageValue = packageValue;
    }

    /** Setzt den Elementnamen der Import-Package-ID.
     *
     * @param packageValue Elementname */
    public void setPackageElement(String packageElement) {
        _packageElement = packageElement;
    }

    /** Setzt den Prefix der XML-Tags. Default: sos_export
     *
     * @param xmlTagname */
    public void setXMLTagname(String xmlTagname) {
        _xmlTagname = xmlTagname;
    }

    /** Setzt die Encoding-Angabe im XML-Header. Default: iso-8859-1
     *
     * @param xmlEncoding Enconding */
    public void setXMLEncoding(String xmlEncoding) {
        _xmlEncoding = xmlEncoding;
    }

    /** Setzt die Normalisierung der Tabellenfeldnamen: {strtolower, strtoupper}.
     * Default: strtoupper
     *
     * @param normalizeFieldName Name der Normalisierung
     * @throws Exception */
    public void setNormalizeFieldName(String normalizeFieldName) throws Exception {
        if (!normalizeFieldName.equalsIgnoreCase("strtolower") && !normalizeFieldName.equalsIgnoreCase("strtoupper")) {
            throw new IllegalArgumentException("SOSExport.setNormalizeFieldName: normalizeFielName must be \"strtolower\" or \"strtoupper\"");
        }
        try {
            _normalizeFieldName = normalizeFieldName;
            _conn.setFieldNameToUpperCase(normalizeFieldName.equalsIgnoreCase("strtoupper"));
            // _conn.setTableNameToUpperCase(normalizeFieldName.equalsIgnoreCase("strtoupper"));
            if (normalizeFieldName.equalsIgnoreCase("strtoupper")) {
                _conn.setKeysToUpperCase();
            } else {
                _conn.setKeysToLowerCase();
            }
        } catch (Exception e) {
            throw new Exception("SOSImport.setNormalizeFieldName: " + e.getMessage());
        }
    }

    /** Setzt den Status der automatischen Feldnormalisierung. Hierbei wird diese
     * aus der XML-Datei erkannt.
     *
     * @param auto true = automatisch / false = nach Einstellung */
    public void setAutoNormalizeField(boolean auto) {
        _autoNormalizeField = auto;
    }

    /** startDocument Handler
     *
     * @throws org.xml.sax.SAXException */
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

    /** endDocument Handler
     *
     * @throws org.xml.sax.SAXException */
    public void endDocument() throws SAXException {
        _autoChecked = false;
        try {
            _log.debug2("...beende Import");
        } catch (Exception e) {
            throw new SAXException("SOSImport.endDocument: " + e.getMessage(), e);
        }
    }

    /** startElement Handler
     *
     * @throws org.xml.sax.SAXException */
    public void startElement(String uri, String name, String qName,
            org.xml.sax.Attributes atts) throws SAXException {
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
            } else if (name.equalsIgnoreCase("table") && _curMetaOpened) {
                _curMetaTableName = atts.getValue("name");
                if (mappingTablenames != null && mappingTablenames.get(_curMetaTableName) != null &&
                        mappingTablenames.get(_curMetaTableName).toString().length() > 0) {
                    _log.debug("import tablename " + _curMetaTableName + " is mapping in " + mappingTablenames.get(_curMetaTableName));
                    _curMetaTableName = mappingTablenames.get(_curMetaTableName).toString();
                }
                // Tabellennamen speichern
                _metaKeyRecords.get(_curPackageId).setTable(_curMetaTableName);
                _metaFieldRecords.get(_curPackageId).setTable(_curMetaTableName);
            } else if (name.equalsIgnoreCase("key_fields") && _curMetaOpened) {
                _curMetaKeysOpened = true;
            } else if (name.equalsIgnoreCase("field") && _curMetaKeysOpened) {
                if (_log != null) {
                    _log.debug9("add keyfield: name = " + normalizeFieldName(atts.getValue("name")) + " type = "
                            + atts.getValue("type") + " typeID = " + atts.getValue("typeID") + " len = " + atts.getValue("len")
                            + " scale = " + atts.getValue("scale"));
                }
                String field = atts.getValue("name");
                // auto modus
                if (_autoNormalizeField && !_autoChecked) {
                    autoNormalize(field);
                    _autoChecked = true;
                }
                // Schluesselfeld hinzufuegen
                _metaKeyRecords.get(_curPackageId).addField(normalizeFieldName(field), atts.getValue("type"), new Integer(atts.getValue("typeID")),
                        new BigInteger(atts.getValue("len")), new Integer(atts.getValue("scale")));
            } else if (name.equalsIgnoreCase("fields") && _curMetaOpened) {
                _curMetaFieldsOpened = true;
            } else if (name.equalsIgnoreCase("field") && _curMetaFieldsOpened) {
                if (_log != null) {
                    _log.debug9("add field: name = " + normalizeFieldName(atts.getValue("name")) + " type = " + atts.getValue("type") + " typeID = "
                            + atts.getValue("typeID") + " len = " + atts.getValue("len") + " scale = " + atts.getValue("scale"));
                }
                String field = atts.getValue("name");
                // auto modus
                if (_autoNormalizeField && !_autoChecked) {
                    autoNormalize(field);
                    _autoChecked = true;
                }
                // Tabellenfeld hinzufuegen
                _metaFieldRecords.get(_curPackageId).addField( normalizeFieldName(field), atts.getValue("type"), new Integer(atts.getValue("typeID")),
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
                if (atts.getValue("operation") != null && atts.getValue("operation").length() > 0) {
                    _operation = atts.getValue("operation");
                }
            } else if (_curDataFieldsOpened) {
                _curDataFieldOpened = true;
                _curDataFieldName = normalizeFieldName(name);
                _curDataFieldData = new StringBuilder();
                _curDataFieldNull = atts.getValue("null") != null && atts.getValue("null").equalsIgnoreCase("true");
                // Feld mit leerem Inhalt in den Record einfuegen
                _records.addValue(_recordIndex, _curDataFieldName, _curDataFieldData.toString());
            }
        } catch (Exception e) {
            throw new SAXException("SOSImport.startElement: " + e.getMessage(), e);
        }
    }

    /** endElement Handler
     *
     * @throws org.xml.sax.SAXException */
    public void endElement(String uri, String name, String qName) throws SAXException {
        try {
            if (_log != null) {
                _log.debug9("import endElement: " + name);
            }
            // SOS_EXPORT
            if (name.equalsIgnoreCase(_xmlTagname)) {
                _curExportOpen = false;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_package")) {
                _curPackageOpened = false;
            } else if (name.equalsIgnoreCase(_xmlTagname + "_meta")) {
                _curMetaOpened = false;
            } else if (name.equalsIgnoreCase("key_fields")) {
                _curMetaKeysOpened = false;
            } else if (name.equalsIgnoreCase("fields")) {
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
                    if (_operation != null && _operation.equals("insert")) {
                        importRecord(_curDataRecordName, _recordIndex, _operation);
                    } else if (_operation != null && _operation.equals("update")) {
                        importRecord(_curDataRecordName, _recordIndex, _operation);
                    } else if (_operation != null && _operation.equals("delete")) {
                        deleteRecord(_curDataRecordName, _recordIndex);
                    } else {
                        importRecord(_curDataRecordName, _recordIndex);
                    }
                    _operation = null;
                }
            } else if (_curDataFieldOpened) {
                // Record jetzt mit Zwischenspeicher fuellen
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

    /** characters Handler
     *
     * @throws org.xml.sax.SAXException */
    public void characters(char ch[], int start, int length) throws SAXException {
        if (_curDataFieldOpened) {
            // Daten des Feldes zwischenspeichern
            String str = new String(ch, start, length);
            if (!str.equals("")) {
                _curDataFieldData.append(str);
            }
        }
    }

    /** Eine Tabelle f&uuml;r den Import Markieren. Wird kein setTable()
     * ausgef&uuml;hrt, so werden alle Tabellen importiert. Sobald min. eine
     * Tabelle f&uuml;r den Import markiert ist, werden nur diese Importiert.
     * Mit den Restriktionen k&ouml;nnen einzelne Tabellenfelder (Name/Wert) und
     * deren evtl. Abh&auml;ngigkeiten ausgegrenzt werden.
     *
     * @param table Objekt Name der Tabelle
     * @param replace Vorhandenes, gleichnamiges Objekt ersetzen
     * @param restrict HashMap von Name/Wert-Paaren f&uuml;r Felder der
     *            Import-Datei zur Beschr&auml;nkung des Umfangs
     * @throws java.lang.Exception */
    public void setTable(String table, boolean replace, HashMap restrict) throws Exception {
        try {
            if (table == null || table.equals("")) {
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

    /** Callback-Methode dieser Klasse f&uuml;r die Manipulation von
     * Schl&uuml;sseln und Datens&auml;tzen registrieren.
     *
     * Die hiermit registrierte Methode, die durch Vererbung in dieser Klasse
     * steht, wird vor dem Einf&uuml;gen des Datensatzes aufgerufen. Die
     * individuell implementierte Methode kann Felder ver&auml;ndern,
     * hinzuf&uuml;gen etc.
     *
     * @param table Name der Tabelle
     * @param keyHandler Name der Methode zur Behandlung von Schl&uuml;sseln
     * @param recordHandler Name der Methode zur Behandlung von Datens&auml;tzen
     * @param recordIdentifier kommaseparierte Liste der identifizierenden
     *            Feldnamen zum Dublettenabgleich */
    public void setHandler(String table, String keyHandler, String recordHandler, String recordIdentifier) {
        if (table == null || table.equals("")) {
            throw new IllegalArgumentException("SOSImport.setHandler: you have to give a table name");
        }
        if (keyHandler != null && !keyHandler.equals("")) {
            _keyHandler.put(table.toLowerCase(), keyHandler);
        }
        if (recordHandler != null && !recordHandler.equals("")) {
            _recordHandler.put(table.toLowerCase(), recordHandler);
        }
        if (recordIdentifier != null && !recordIdentifier.equals("")) {
            _recordIdentifier.put(table.toLowerCase(), recordIdentifier);
        }
    }

    /** Import ausf&uuml;hren.
     *
     * @throws java.lang.Exception, org.xml.sax.SAXException,
     *             java.io.FileNotFoundException */
    public void doImport() throws Exception, SAXException, FileNotFoundException {
        try {
            if (_normalizeFieldName.equalsIgnoreCase("strtoupper")) {
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

    /** Import ausf&uuml;hren.
     *
     * @param conn Verbindungsobjekt der Klasse SOSConnection
     * @param fileName Pfad der Export-Datei
     * @throws java.lang.Exception, org.xml.sax.SAXException,
     *             java.io.FileNotFoundException */
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

    /** Import ausf&uuml;hren.
     *
     * @param conn Verbindungsobjekt der Klasse SOSConnection
     * @param fileName Pfad der Export-Datei
     * @param packageId Lfd. Nr. des Export-Pakets in Export-Datei
     * @param packageElement Name des Elements mit dem Paket-Tag
     * @param packageValue Wert des Elements mit dem Paket-Tag
     * @throws java.lang.Exception, org.xml.sax.SAXException,
     *             java.io.FileNotFoundException */
    public void doImport(SOSConnection conn, String fileName, String packageId, String packageElement, String packageValue) 
            throws Exception, SAXException, FileNotFoundException {
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

    /** Datensatz in die Datenbank aufnehmen.
     *
     * @param name Name des XML-Elements (Tabelle)
     * @param index Index des Records in der Liste
     * @param operation
     * @throws java.lang.Exception */
    private void importRecord(String name, int index, String operation) throws Exception {
        boolean isNewKey = false;
        try {
            if (_log != null) {
                _log.debug3("import_record(" + name + ", " + index + "): key_fields=" + normalizeFieldName(_metaKeyRecords.get(name).getKeyString() 
                        + " table=" + _metaKeyRecords.get(name).getTable()));
            }
            if (_conn == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = _restrictMode;
            HashMap restrictObject = _restrictObject;
            // Import auf gegebenen Tabelle beschraenken
            if (_tables.size() > 0) {
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

            // Schluesselfelder-Hashmap erstellen
            HashMap keys = new HashMap();
            for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                if (_log != null) {
                    _log.debug6("key_fields key [" + key + "] = " + _records.getValue(index, key));
                }
                keys.put(normalizeFieldName(key), _records.getValue(index, key));
            }
            // Schluesselfelder anpassen lassen
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
            if (operation != null && operation.equalsIgnoreCase("insert")) {
                isNew = true;
            } else if (operation != null && operation.equalsIgnoreCase("update")) {
                isNew = false;
            } else {
                // auf vorhandenen record pruefen
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
            // record mit key/value pairs fuellen
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
            // Record anpassen lassen
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
                // pruefen, ob der Client die Schluesselfelder im Record veraendert hat
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
                        isNew = new Boolean(((String) record.get("SOS_EXPORT_IS_NEW"))).booleanValue();
                        isNewKey = true;
                        record.remove("SOS_EXPORT_IS_NEW");
                    }
                }
            }
            // record verarbeiten...
            if (record != null && !record.isEmpty()) {
                // wenn Schluesselfelder neu, dann anpassen
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
                // blobs und clobs ermitteln
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
                // update blobs and clobs
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
            // Import auf gegebenen Tabelle beschraenken
            if (_tables.size() > 0) {
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

            // Schluesselfelder-Hashmap erstellen
            HashMap keys = new HashMap();
            for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                if (_log != null) {
                    _log.debug6("key_fields key [" + key + "] = " + _records.getValue(index, key));
                }
                keys.put(normalizeFieldName(key), _records.getValue(index, key));
            }
            // Schluesselfelder anpassen lassen
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

    /** Datensatz in die Datenbank aufnehmen.
     *
     * @param name Name des XML-Elements (Tabelle)
     * @param index Index des Records in der Liste
     * @throws java.lang.Exception */
    private void importRecord(String name, int index) throws Exception {
        boolean isNewKey = false;
        try {
            if (_log != null) {
                _log.debug3("import_record(" + name + ", " + index + "): key_fields=" + normalizeFieldName(_metaKeyRecords.get(name).getKeyString() 
                        + " table=" + _metaKeyRecords.get(name).getTable()));
            }
            if (_conn == null) {
                throw new Exception("No aktive database connection!");
            }
            boolean restrictMode = _restrictMode;
            HashMap restrictObject = _restrictObject;
            // Import auf gegebenen Tabelle beschraenken
            if (_tables.size() > 0) {
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
            // Schluesselfelder-Hashmap erstellen
            HashMap keys = new HashMap();
            for (Iterator it = _metaKeyRecords.get(name).getIterator(); it.hasNext();) {
                String key = it.next().toString();
                if (_log != null) {
                    _log.debug6("key_fields key [" + key + "] = " + _records.getValue(index, key));
                }
                keys.put(normalizeFieldName(key), _records.getValue(index, key));
            }
            // Schluesselfelder anpassen lassen
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
            // auf vorhandenen record pruefen
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

            boolean isNew = (record == null || record.isEmpty());
            if (_log != null) {
                _log.debug6("is_new: " + isNew);
            }
            record = new HashMap();
            // record mit key/value pairs fuellen
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
            // Record anpassen lassen
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
                // pruefen, ob der Client die Schluesselfelder im Record veraendert hat
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
                        isNew = new Boolean(((String) record.get("SOS_EXPORT_IS_NEW"))).booleanValue();
                        isNewKey = true;
                        record.remove("SOS_EXPORT_IS_NEW");
                    }
                }
            }
            // record verarbeiten...
            if (record != null && !record.isEmpty()) {
                // wenn Schluesselfelder neu, dann anpassen
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
                // blobs und clobs ermitteln
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

                // update blobs and clobs
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

    /** Liefert eine Liste der Blobs und Clobs aus dem Record mit den Namen als
     * Schl?ssel und einem Array als Wert. An erster Stelle im Array steht der
     * Typ (blob/clob) und an zweiter der Inhalt als Byte-Array oder String. Die
     * Blob/Clob-Werte werden im Record mit NULL gef&uuml;llt.
     *
     * @param name Name des Imports - Tabellenname
     * @param record Record, aus dem die Blobs/Clobs gewonnen werden sollen
     * @return Liste der Blobs/Clobs mit Typ und Wert
     * @throws Exception */
    private HashMap getBlobs(String name, HashMap record) throws Exception {
        try {
            HashMap blobs = new HashMap();
            for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                String xmlVal = (String) record.get(key);
                String blobType = null;
                switch (_metaFieldRecords.get(name).getTypeId(key)) {
                case Types.LONGVARCHAR:
                    blobType = "clob";
                case Types.BINARY:
                    if (blobType == null) {
                        blobType = "blob";
                    }
                case Types.BLOB:
                    if (blobType == null) {
                        blobType = "blob";
                    }
                case Types.CLOB:
                    if (blobType == null) {
                        blobType = "clob";
                    }
                case Types.LONGVARBINARY:
                    if (blobType == null) {
                        blobType = "blob";
                    }
                case Types.VARBINARY:
                    if (blobType == null) {
                        blobType = "blob";
                    }
                    if (_log != null) {
                        _log.debug6("found " + blobType + ": name = " + key + " type = " + _metaFieldRecords.get(name).getTypeName(key) + " typeID = "
                                + _metaFieldRecords.get(name).getTypeId(key));
                    }
                    ArrayList obj = new ArrayList(2);
                    obj.add(blobType);
                    byte[] val = fromHexString(xmlVal);
                    if (blobType.equals("blob") && val != null && val.length > 0) {
                        obj.add(val);
                    } else if (blobType.equals("clob") && val != null && val.length > 0) {
                        // nur, wenn es einen Wert gibt (>0), da SOSConnection.updateClob keinen Leerstring akzeptiert
                        obj.add(new String(val));
                    } else {
                        obj.add(null);
                    }
                    blobs.put(key, obj);
                    // blobs und clobs muessen SQL NULL faehig sein
                    record.put(key, null);
                    break;
                }
            }
            return blobs;
        } catch (Exception e) {
            throw new Exception("SOSImport.getBlobs: " + e.getMessage(), e);
        }
    }

    /** F&uuml;hrt ein Update auf eine Blob/Clob-Liste aus.
     *
     * @param name Name des Imports - Tabellenname
     * @param blobs Durch getBlobs() ermittelte Blob/Clob-Liste
     * @param keys Zu verwendende Schl&uuml;sselfelder mit Werten
     * @param record Der dazugeh&ouml;rige Record
     * @throws Exception */
    private void updateBlob(String name, HashMap blobs, HashMap keys, HashMap record) throws Exception {
        try {
            for (Iterator it = blobs.keySet().iterator(); it.hasNext();) {
                String blobName = it.next().toString();
                ArrayList blobValue = (ArrayList) blobs.get(blobName);
                if (_log != null) {
                    _log.debug6("update " + (String) blobValue.get(0) + ": " + blobName);
                }
                // Bedingung
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
                // update blob/clob wenn es einen wert gibt, dann wird dieser hier geschrieben.
                // ansonsten ist in updateRecord schon ein SQL NULL geschrieben worden
                if (blobValue.get(1) != null) {
                    if (((String) blobValue.get(0)).equals("blob")) {
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

    /** F&uuml;hrt ein SQL-Insert mit dem angegebenen Record aus.
     *
     * @param name Name des Imports - Tabellenname
     * @param record Einzuf&uuml;gender Record
     * @throws Exception */
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

    /** F&uuml;hrt ein SQL-Update mit dem angegebenen Record und dessen
     * Schl&uuml;sselfeldern aus.
     *
     * @param name Name des Imports - Tabellenname
     * @param keys Lister der Schl&uuml;sselfelder mit Werten
     * @param record Record
     * @throws Exception */
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

    /** Quotiert einen Wert nach seinem SQL-Typ.
     *
     * @param package_id Name des XML-Elements (Tabelle)
     * @param field Name des Tabellenfeldes
     * @param val Wert des Tabellenfeldes
     * @return Quotiertert Wert */
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
            if (val.equals("")) {
                val = "NULL";
            }
            return val;
        case Types.DATE:
        case Types.TIMESTAMP:
            return "%timestamp_iso('" + val + "')";
        default:
            val = val.replaceAll("'", "''");
            if (_conn instanceof SOSMySQLConnection)
                val = val.replaceAll("\\\\", "\\\\\\\\");
            return "'" + val + "'";
        }
    }

    /** Setzt automatisch die richtige Feldnormalisierung anhand eines Feldnamen.
     * Wenn ein Feldname gemischt aus Gross- und Kleinbuchstaben besteht, wird
     * die Normalisierung auf Grossbuchstaben gestellt.
     *
     * @param field Feldname
     * @throws Exception */
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

    /** Normalisiert anhand von _normalizeFieldName einen Feldnamen.
     *
     * @param field String des Feldnamen
     * @return normalisierter String des Feldnamen */
    private String normalizeFieldName(String field) {
        if (_normalizeFieldName.equalsIgnoreCase("strtoupper")) {
            return field.toUpperCase();
        } else {
            return field.toLowerCase();
        }
    }

    /** Wandelt Entities wieder zur&uuml;ck.
     *
     * @param xml XML-Wert
     * @return Normalisierter Wert */
    private String fromXML(String xml) {
        return xml;
    }

    /** Wandelt einen Hex-String in ein Byte-Array um.
     *
     * @param hex Hex-String
     * @return Byte-Array des Hex-Strings
     * @throws java.lang.Exception */
    private static byte[] fromHexString(String s) {
        // wenn leer bzw NULL - immer null->SQL NULL
        if (s == null) {
            return null;
        } else if (s.equals("")) {
            return null;
        }
        int length = 0;
        int strLength = s.length();
        // Anzahl der ueberfluessigen Zeichen zaehlen
        for (int i = 0; i < strLength; i++) {
            if (s.charAt(i) == ' ' || s.charAt(i) == '\n' || s.charAt(i) == '\t') { 
                length++;
            }
        }
        // Laenge des Ergebnisses
        length = s.length() - length;
        // die Laenge des reinen Hex-Wertes muss gerade sein
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
            // ueberfluessige Zeichen ueberspringen
            if (c_low == ' ' || c_low == '\n' || c_low == '\t') {
                i++;
                continue;
            }
            c_high = s.charAt(i + 1);
            // Hex nach Byte
            int high = charToNibble(c_low);
            int low = charToNibble(c_high);
            b[j] = (byte) ((high << 4) | low);
            j++;
            i += 2;
        }
        return b;
    }

    /** Konvertiert ein Char in ein Halb-Byte um.
     *
     * @param c Zu konvertierendes Zeichen - muss 0-9 a-f oder A-F sein.
     * @return der entsprechende Integerwert */
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