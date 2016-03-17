package sos.marshalling;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Types;

import sos.connection.SOSConnection;
import sos.connection.SOSMySQLConnection;
import sos.marshalling.SOSImExportTableFieldTypes;
import sos.util.SOSStandardLogger;

/** <p>
 * Title: SOSExport
 * </p>
 * <p>
 * Description: Exportieren von Daten einer Applikation im XML-Format.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: SOS-Berlin GmbH
 * </p>
 * 
 * @author Titus Meyer
 * @version 1.5.5 */

public class SOSExport {

    /** <p>
     * Title: Query
     * </p>
     * <p>
     * Description: Nimmt einen SQL-Statement mit Name, Schl&uuml;sselfeldern,
     * Parametern und Abh&auml;ngigkeiten auf.
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

    /** Export Connection Objekt der Klasse SOSConnection */
    private SOSConnection _conn = null;
    /** StandardLogger Objekt der Klasse SOSStandardLogger */
    private SOSStandardLogger _log = null;
    /** Name der exportierenden Applikation */
    private String _application = null;
    /** Name der Export-Datei */
    private String _fileName = null;
    /** Name des Tags, das den Export umklammert - default: sos_export */
    private String _xmlTagname = "sos_export";
    /** Zeichensatz - default: iso-8859-1 */
    private String _xmlEncoding = "iso-8859-1";
    /** Keynormalisierung: strtolower / strtoupper (default) */
    private String _normalizeFieldName = "strtoupper";
    /** Tagnormalesierung: strtolower (default) / strtoupper */
    private String _normalizeTagName = "strtolower";
    /** verwendete Einr&uuml;ckungsstring - default: zwei Leerzeichen */
    private String _xmlIndentation = "  ";
    /** Aktuelle Einr&uuml;ckungsstufe */
    private int _xmlIndent = 0;
    /** Eine Liste von Query-Objekten, die die SQL-Statements enthalten */
    private Queries _queries = new Queries();
    /** Index der aktuellen Abfrage */
    private int _queryCnt = 0;
    /** Rekursionsz&auml;hler */
    private int _rekursionCnt = 0;
    /** Zeilenumbruch nach dem Zeichen x fr blobs/clobs */
    private int _lineWrap = 254;
    /** Tabelle zur Konvertierung von Teilbytes in den dazugeh&ouml;rigen
     * Hex-Wert */
    private static char[] _hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private class Query {

        /** Tagname bzw. Tabellenname zu dem SQL-Statement */
        private String _tag = null;
        /** ArrayListe der zugeh&ouml;rigen Schl&uuml;sselfelder */
        private ArrayList _key = new ArrayList();
        /** Der Querystring */
        private String _query = null;
        /** ArrayListe der Parameter - Das ? Zeichen wird durch Spalteninhalte,
         * die als Parameter angegeben wurden ersetzt */
        private ArrayList _parameters = new ArrayList();
        /** ArrayListe der abh&auml;ngigen SQL-Statement IDs */
        private ArrayList _dependRefs = new ArrayList();
        /** ArrayListe der unabh&auml;ngigen SQL-Statement IDs */
        private ArrayList _indepdRefs = new ArrayList();
        /** Bearbeitungsstatus des SQL-Statements */
        private boolean _done = false;
        private boolean dependent = false;
        /** Operation die durchgeführt wurde: Mögliche Operation ist insert,
         * update oder delete */
        private String _operation = null;
        HashMap fieldsKeys = null;

        // Hilfsvariable: Beim operation delete werden hier Schlüsselfelder
        // eingefügt

        /** Konstruktor
         * 
         * @param tag Name des klammernden Tags fr Elemente der Abfrage
         *            (Tabellenname)
         * @param key Schl&uuml;sselfeld - mehrere durch Komma getrennt
         * @param query SQL-Statement der Abfrage
         * @param parameters Abfrageparameter, d.i. Felder einer vorhergehenden
         *            Abfrage, mehrere durch Komma getrennt
         * @param dependRef Nr. der vorhergehenden Abfrage, die Abfrageparameter
         *            liefert */
        public Query(String tag, String key, String query, String parameters, int dependRef) {
            _tag = tag;
            _key.addAll(Arrays.asList(key.split(",")));
            _query = query;
            _parameters.addAll(Arrays.asList(parameters.split(",")));
            _dependRefs.add(new Integer(dependRef));
        }

        /** Konstruktor
         * 
         * @param tag Name des klammernden Tags f&uuml;r Elemente der Abfrage
         *            (Tabellenname)
         * @param key Schl&uuml;sselfeld - mehrere durch Komma getrennt
         * @param query SQL-Statement der Abfrage
         * @param parameters Abfrageparameter, d.i. Felder einer vorhergehenden
         *            Abfrage mehrere durch Komma getrennt */
        public Query(String tag, String key, String query, String parameters) throws Exception {
            _tag = tag;
            if (key != null && !"".equals(key)) {
                _key.addAll(Arrays.asList(key.split(",")));
            }
            _query = query;
            if (parameters != null) {
                _parameters.addAll(Arrays.asList(parameters.split(",")));
            }
        }

        /** Konstruktor
         * 
         * @param tag Name des klammernden Tags f&uuml;r Elemente der Abfrage
         *            (Tabellenname)
         * @param key Schl&uuml;sselfeld - mehrere durch Komma getrennt
         * @param query SQL-Statement der Abfrage
         * 
         * 
         * @param parameters Abfrageparameter, d.i. Felder einer vorhergehenden
         *            Abfrage mehrere durch Komma getrennt */
        public Query(String tag, String key, String query, String parameters, String operation, HashMap keys4Delete) throws Exception {
            _tag = tag;
            if (key != null && !"".equals(key)) {
                _key.addAll(Arrays.asList(key.split(",")));
            }
            _query = query;
            fieldsKeys = keys4Delete;
            _operation = operation;
            if (parameters != null) {
                _parameters.addAll(Arrays.asList(parameters.split(",")));
            }
        }

        /** Liefert die unabh&auml;ngige Referenz des SQL-Statements an der
         * Stelle index zur&uuml;ck - sonst null.
         * 
         * @param index Index der Referenzliste
         * @return unabh&auml;ngige Referenz-ID */
        public Integer getDependRef(int index) {
            return (Integer) _dependRefs.get(index);
        }

        /** Liefert die Anzahl der unabh&auml;ngigen Referenzen des
         * SQL-Statements zur&uuml;ck.
         * 
         * @return Anzahl der unabh&auml;ngigen Referenzen. */
        public int getDependRefCnt() {
            return _dependRefs.size();
        }

        /** F&uuml;gt eine unabh&auml;ngige Referenz-ID der Liste hinzu.
         * 
         * @param dependRef ID der Referenz. */
        public void addDependRef(Integer dependRef) {
            _dependRefs.add(dependRef);
        }

        /** Liefert einen String mit der Auflistung der unabh&auml;ngigen
         * Referenzen des SQL-Statements zur&uuml;ck.
         * 
         * @return String der Referenzliste. */
        public String dependRefToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _dependRefs.size(); i++) {
                sb.append(_dependRefs.get(i).toString());
                if (i < _dependRefs.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        /** Gibt an, ob der SQL-Statement schon verarbeitet wurde.
         * 
         * @return Status */
        public boolean isDone() {
            return _done;
        }

        /** Setzt den Verarbeitungsstatus des SQL-Statements.
         * 
         * @param done Status als boolean. */
        public void setDone(boolean done) {
            _done = done;
        }

        /** Liefert die unabh&auml;ngige Referenz-ID des SQL-Statements an der
         * Stelle index - sonst null.
         * 
         * @param index Index der Referenz-ID in der Liste.
         * @return unabh&auml;ngige Referenz-ID */
        public Integer getIndepdRef(int index) {
            return (Integer) _indepdRefs.get(index);
        }

        /** Liefert die Anzahl der unabh&auml;ngigen Referenzen in der Liste.
         * 
         * @return Anzahl der Referenzen. */
        public int getIndepdRefCnt() {
            return _indepdRefs.size();
        }

        /** F&uuml;gt eine unabh&auml;ngige Referenz-ID in die Liste ein.
         * 
         * @param indepdRef Referenz-ID */
        public void addIndepdRef(Integer indepdRef) {
            _indepdRefs.add(indepdRef);
        }

        /** Liefert einen String mit der Auflistung der unabh&auml;ngigen
         * Referenzen zur&uuml;ck.
         * 
         * @return String der Referenzliste */
        public String indepdRefToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _indepdRefs.size(); i++) {
                sb.append(_indepdRefs.get(i).toString());
                if (i < _indepdRefs.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        /** Liefert das Schl&uuml;sselfeld des SQL-Statements an der Stelle index
         * zur&uuml;ck - sonst null.
         * 
         * @param index Index des Schl&uuml;sselfeldes in der Liste
         * @return Key als String */
        public String getKey(int index) {
            return (String) _key.get(index);
        }

        /** Liefert die Anzahl der Schl&uuml;sselfelder in der Liste.
         * 
         * @return Anzahl der Keys */
        public int getKeyCnt() {
            return _key.size();
        }

        /** F&uuml;gt ein Schl&uuml;sselfeld an die Liste an.
         * 
         * @param key Schl&uuml;sselfeld */
        public void addKey(String key) {
            _key.add(key);
        }

        /** Liefert einen String mit der Auflistung der Schl&uuml;sselfelder
         * zur&uuml;ck.
         * 
         * @return String der Schl&uuml;sselfelder */
        public String keysToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _key.size(); i++) {
                sb.append((String) _key.get(i));
                if (i < _key.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        /** Liefert einen Parameter an der Stelle index in der Liste zur&uuml;ck
         * - sonst null.
         * 
         * @param index Index des Parameters
         * @return String des Parameters */
        public String getParameter(int index) {
            return (String) _parameters.get(index);
        }

        /** Liefert die Anzahl der Parameter zur&uuml;ck.
         * 
         * @return Anzahl der Parameter */
        public int getParameterCnt() {
            return _parameters.size();
        }

        /** F&uuml;gt einen Parameter an die Parameterliste an.
         * 
         * @param parameter String des Parameters */
        public void addParameter(String parameter) {
            _parameters.add(parameter);
        }

        /** Liefert einen String mit der Auflistung der Parameter zur&uuml;ck.
         * 
         * @return String der Parameterliste */
        public String parametersToStr() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < _parameters.size(); i++) {
                sb.append((String) _parameters.get(i));
                if (i < _parameters.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }

        /** Liefert das SQL-Statement zur&uuml;ck.
         * 
         * @return String des SQL-Statements */
        public String getQuery() {
            return _query;
        }

        /** Speichert das SQL-Statement.
         * 
         * @param query SQL-Statement */
        public void setQuery(String query) {
            _query = query;
        }

        /** Liefert den Name des klammernden Tags f&uuml;r Elemente der Abfrage
         * zur&uuml;ck.
         * 
         * @return String des Tags */
        public String getTag() {
            return _tag;
        }

        /** Speichert den Name des klammernden Tags f&uuml;r Elemente der
         * Abfrage.
         * 
         * @param tag String des Tags */
        public void setTag(String tag) {
            _tag = tag;
        }

        /** @return Returns the dependent. */
        public boolean isDependent() {
            return dependent;
        }

        /** @param dependent The dependent to set. */
        public void setDependent(boolean dependent) {
            this.dependent = dependent;
        }

        public String getOperation() {
            return _operation;
        }

        public void setOperation(String _operation) {
            this._operation = _operation;
        }

        public HashMap getFieldsKeys() {
            return fieldsKeys;
        }

        public void setFieldsKeys(HashMap fieldsKeys) {
            this.fieldsKeys = fieldsKeys;
        }
    }

    /** <p>
     * Title: Queries
     * </p>
     * <p>
     * Description: Eine Liste f&uuml;r Queries.
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
    private class Queries {

        /** ArrayListe der SQL-Statement Objekte */
        private ArrayList _list = new ArrayList();

        /** F&uuml;gt eine SQL-Statement Objekt an die Liste an.
         * 
         * @param query Query Objekt
         * @throws UnsupportedOperationException
         * @throws ClassCastException
         * @throws NullPointerException
         * @throws Exception */
        public void add(Query query) throws Exception {
            _list.add(query);
        }

        /** Liefert ein Query Objekt an der Position index zur&uuml;ck - sonst
         * null.
         * 
         * @param index Index des Objektes in der Liste
         * @return Query Objekt
         * @throws IndexOutOfBoundsException */
        public Query get(int index) throws IndexOutOfBoundsException {
            return (Query) _list.get(index);
        }

        /** L&ouml;scht die gesamte Query-Liste.
         * 
         * @throws UnsupportedOperationException */
        public void clear() throws UnsupportedOperationException {
            _list.clear();
        }

        /** Liefert die Anzahl der gespeicherten Query-Objekte zur&uuml;ck.
         * 
         * @return Anzahl der Queries */
        public int cnt() {
            return _list.size();
        }
    }

    /** Konstruktor
     * 
     * @param conn Datenbankverbindung der SOSConnection Klasse
     * @param fileName Datei, in die Exportiert wird. Ist file_name = null, so
     *            liefert export() das Ergebnis zur&uuml;ck.
     * @param application Name der Anwendung, die die Klasser verwendet
     * @param log Logger der SOSStandardLogger Klasse */
    public SOSExport(SOSConnection conn, String fileName, String application, SOSStandardLogger log) {
        if (conn != null) {
            _conn = conn;
        }
        if (fileName != null) {
            _fileName = fileName;
        }
        if (application != null) {
            _application = application;
        }
        if (log != null) {
            _log = log;
        }

        // Wirkt sich nur auf den Oracle Treiber aus - verhindert eine Exception
        // bei dem Versuch die Groesse der 4GB grossen blobs/clobs in einen
        // Integer
        // zu wandeln
        System.setProperty("oracledatabasemetadata.get_lob_precision", "false");
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

    /** Setzt den Namen der Applikation - wird im Export-Tag gespeichert.
     * 
     * @param application Name */
    public void setApplication(String application) {
        _application = application;
    }

    /** Setzt den Pfad der Datei, in die exportiert werden soll. Ist der Pfad
     * leer bzw. null, dann wird das Ergebnis des Exports als String zur&uuml;ck
     * geliefert.
     * 
     * @param fileName Pfad der Datei */
    public void setFileName(String fileName) {
        _fileName = fileName;
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
     * @param normalizeFieldName Name der Normalisierung */
    public void setNormalizeFieldName(String normalizeFieldName) {
        if (!normalizeFieldName.equalsIgnoreCase("strtolower") || !normalizeFieldName.equalsIgnoreCase("strtoupper")) {
            throw new IllegalArgumentException("SOSExport.setNormalizeFieldName: normalizeFielName must be \"strtolower\" or \"strtoupper\"");
        }
        _normalizeFieldName = normalizeFieldName;
    }

    /** Setzt die Normalisierung der XML-Tagnamen: {strtolower, strtoupper}.
     * Default: strtolower
     * 
     * @param normalizeTagName Name der Normalisierung */
    public void setNormalizeTagName(String normalizeTagName) {
        if (!normalizeTagName.equalsIgnoreCase("strtolower") || !normalizeTagName.equalsIgnoreCase("strtoupper")) {
            throw new IllegalArgumentException("SOSExport.setNormalizeTagName: normalizeTagName must be \"strtolower\" or \"strtoupper\"");
        }
        _normalizeTagName = normalizeTagName;
    }

    /** Setzt den String, der f&uuml;r eine Einr&uuml;ckung verwendet werden soll
     * (muss eine gerade L&auml;nge besitzen). Default: zwei Leerzeichen
     * 
     * @param indentation Einr&uuml;ckungsstring */
    public void setXMLIndentation(String indentation) {
        if ((indentation.length() & 0x1) != 0) {
            throw new IllegalArgumentException("SOSExport.setXMLIndentation: the indentation string must have an even length");
        }
        _xmlIndentation = indentation;
    }

    /** Setzt die Zeilenl&auml;nge, aber der eine neue Begonnen werden soll
     * (Z&auml;hlt nur f&uuml;r die Hex-Bl&ouml;cke und muss eine gerade
     * L&auml;nge haben). Default: 254
     * 
     * @param lineWrap Zeilenl&auml;nge */
    public void setLineWrap(int lineWrap) {
        if ((lineWrap & 0x1) != 0) {
            throw new IllegalArgumentException("SOSExport.setLineWrap: the line must wrap at an even length");
        }
        _lineWrap = lineWrap;
    }

    /** Abh&auml;ngige Abfrage f&uuml;r den Export registrieren.
     * 
     * @param tag Name des klammernden Tags f&uuml;r Elemente der Abfrage -
     *            Tabellenname
     * @param key Schl&uuml;sselfeld(er) der Tabelle - mehrere durch Komma
     *            getrennt
     * @param query SQL-Statement der Abfrage. Bei abh&auml;ngigen Abfragen wird
     *            das ? Zeichen durch den Inhalt der in parameter angegebenen
     *            Felder substituiert.
     * @param parameter Abfrageparameter - Feldnamen aus vorhergehender Abfrage,
     *            deren Inhalt in die abh&auml;ngige Abfrage substituiert wird.
     * @param queryId Nr. der vorhergehenden Abfrage, die die Abfrageparameter
     *            liefert
     * @return Lfd. Nr. der Abfrage
     * @throws java.lang.Exception */
    public int query(String tag, String key, String query, String parameter, int queryId) throws Exception {
        try {
            if (query != null && !query.equals("") && tag != null && !tag.equals("")) {
                Query obj = new Query(tag, key, query, parameter);
                if (queryId > -1) {
                    obj.setDependent(true);
                }
                // bei zu wenigen parametern -> fehler
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }
                if (_log != null) {
                    _log.debug3("query: tag=" + tag + " key=" + key + " query_id=" + queryId + " query_cnt=" + _queryCnt);
                }
                // Abhaengigkeit erstellen
                if (queryId >= 0 && queryId < _queries.cnt()) {
                    _queries.get(queryId).addDependRef(new Integer(_queryCnt));
                } else if (_queryCnt > 0 && (_queryCnt - 1) < _queries.cnt()) {
                    _queries.get(_queryCnt - 1).addDependRef(new Integer(_queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.query: query_id index out of range: " + queryId);
                }
                // Abfrage einfuegen
                _queries.add(obj);
                return _queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: empty query statement!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.query: " + e.getMessage(), e);
        }
    }

    /** Abh&auml;ngige Abfrage f&uuml;r den Export registrieren.
     * 
     * @param tag Name des klammernden Tags f&uuml;r Elemente der Abfrage -
     *            Tabellenname
     * @param key Schl&uuml;sselfeld(er) der Tabelle - mehrere durch Komma
     *            getrennt
     * @param query SQL-Statement der Abfrage. Bei abh&auml;ngigen Abfragen wird
     *            das ? Zeichen durch den Inhalt der in parameter angegebenen
     *            Felder substituiert.
     * @param parameter Abfrageparameter - Feldnamen aus vorhergehender Abfrage,
     *            deren Inhalt in die abh&auml;ngige Abfrage substituiert wird.
     * @param queryId Nr. der vorhergehenden Abfrage, die die Abfrageparameter
     *            liefert
     * 
     * @param operation welche operation wurde durchgeführt? insert, update oder
     *            delete
     * 
     * @return Lfd. Nr. der Abfrage
     * @throws java.lang.Exception */
    public int query(String tag, String key, String query, String parameter, String operation, HashMap keys4Delete, int queryId) throws Exception {
        try {
            if (query != null && !query.equals("") && tag != null && !tag.equals("")) {
                Query obj = new Query(tag, key, query, parameter, operation, keys4Delete);
                // Query obj = new Query(tag, key, query, parameter);
                if (queryId > -1) {
                    obj.setDependent(true);
                }
                // bei zu wenigen parametern -> fehler
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }
                if (_log != null) {
                    _log.debug3("query: tag=" + tag + " key=" + key + " query_id=" + queryId + " query_cnt=" + _queryCnt);
                }
                // Abhaengigkeit erstellen
                if (queryId >= 0 && queryId < _queries.cnt()) {
                    _queries.get(queryId).addDependRef(new Integer(_queryCnt));
                } else if (_queryCnt > 0 && (_queryCnt - 1) < _queries.cnt()) {
                    _queries.get(_queryCnt - 1).addDependRef(new Integer(_queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.query: query_id index out of range: " + queryId);
                }
                // Abfrage einfuegen
                _queries.add(obj);
                return _queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: empty query statement!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.query: " + e.getMessage(), e);
        }
    }

    /** Abh&auml;ngige Abfrage f&uuml;r den Export registrieren.
     * 
     * @param tag Name des klammernden Tags f&uuml;r Elemente der Abfrage -
     *            Tabellenname
     * @param key Schl&uuml;sselfeld(er) der Tabelle - mehrere durch Komma
     *            getrennt
     * @param query SQL-Statement der Abfrage. Bei abh&auml;ngigen Abfragen wird
     *            das ? Zeichen durch den Inhalt der in parameter angegebenen
     *            Felder substituiert.
     * @return Lfd. Nr. der Abfrage
     * @throws java.lang.Exception */
    public int query(String tag, String key, String query) throws Exception {
        return this.query(tag, key, query, null, -1);
    }

    /** Unabh&auml;ngige Unterabfrage f&uuml;r Export hinzuf&uuml;gen.
     * 
     * @param tag Name des klammernden Tags f&uuml;r Elemente der Abfrage -
     *            Tabellenname
     * @param key Schl&uuml;sselfeld(er) der Tabelle - mehrere durch Komma
     *            getrennt
     * @param query SQL-Statement der Abfrage. Bei unabh&auml;ngigen Abfragen
     *            wird das ? Zeichen durch den Inhalt der in parameter
     *            angegebenen Felder substituiert.
     * @param parameter Abfrageparameter - Feldnamen aus vorhergehender Abfrage,
     *            deren Inhalt in die unabh&auml;ngige Abfrage substituiert
     *            wird.
     * @param queryId Nr. der vorhergehenden Abfrage, die die Abfrageparameter
     *            liefert
     * @return Lfd. Nr. der Abfrage
     * @throws java.lang.Exception */
    public int add(String tag, String key, String query, String parameter, int queryId) throws Exception {
        try {
            if (query != null && !query.equals("") && tag != null && !tag.equals("")) {
                Query obj = new Query(tag, key, query, parameter);
                // bei zu wenigen parametern -> fehler
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }

                if (_log != null) {
                    _log.debug3("add: tag=" + tag + " key=" + key + " query_id=" + queryId + "query_cnt=" + _queryCnt);
                }
                if (queryId >= 0 && queryId < _queries.cnt()) {
                    _queries.get(queryId).addIndepdRef(new Integer(_queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.add: query_id index out of range: " + queryId);
                }
                _queries.add(obj);
                return _queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: tag and query must be defined!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.add: " + e.getMessage(), e);
        }
    }

    /** Unabh&auml;ngige Unterabfrage f&uuml;r Export hinzuf&uuml;gen.
     * 
     * @param tag Name des klammernden Tags f&uuml;r Elemente der Abfrage -
     *            Tabellenname
     * @param key Schl&uuml;sselfeld(er) der Tabelle - mehrere durch Komma
     *            getrennt
     * @param query SQL-Statement der Abfrage. Bei unabh&auml;ngigen Abfragen
     *            wird das ? Zeichen durch den Inhalt der in parameter
     *            angegebenen Felder substituiert.
     * @param parameter Abfrageparameter - Feldnamen aus vorhergehender Abfrage,
     *            deren Inhalt in die unabh&auml;ngige Abfrage substituiert
     *            wird.
     * 
     * @param operation welche operation wurde durchgeführt? insert, update oder
     *            delete
     * 
     * @param queryId Nr. der vorhergehenden Abfrage, die die Abfrageparameter
     *            liefert
     * @return Lfd. Nr. der Abfrage
     * @throws java.lang.Exception */
    public int add(String tag, String key, String query, String parameter, String operation, HashMap keys4Delete, int queryId) throws Exception {
        try {
            if (query != null && !query.equals("") && tag != null && !tag.equals("")) {
                Query obj = new Query(tag, key, query, parameter, operation, keys4Delete);
                // Query obj = new Query(tag, key, query, parameter);
                // bei zu wenigen parametern -> fehler
                if (obj.getParameterCnt() < countStr(query, "?")) {
                    throw new IllegalArgumentException("SOSExport.query: too few fields in parameter for substitution in the query");
                }
                if (_log != null) {
                    _log.debug3("add: tag=" + tag + " key=" + key + " query_id=" + queryId + "query_cnt=" + _queryCnt);
                }
                if (queryId >= 0 && queryId < _queries.cnt()) {
                    _queries.get(queryId).addIndepdRef(new Integer(_queryCnt));
                } else if (parameter != null) {
                    throw new IllegalArgumentException("SOSExport.add: query_id index out of range: " + queryId);
                }
                _queries.add(obj);
                return _queryCnt++;
            } else {
                throw new IllegalArgumentException("SOSExport.query: tag and query must be defined!");
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.add: " + e.getMessage(), e);
        }
    }

    /** Export anhand der registrierten Abfragen ausf&uuml;hren.
     * 
     * @return Leerer String bei file_name != null, sonst Inhalt des Exports
     * @throws java.lang.Exception, java.io.FileNotFoundException */
    public String doExport() throws Exception, FileNotFoundException {
        try {
            if (_normalizeFieldName.equalsIgnoreCase("strtoupper")) {
                _conn.setKeysToUpperCase();
                _conn.setFieldNameToUpperCase(true);
            } else {
                _conn.setKeysToLowerCase();
                _conn.setFieldNameToUpperCase(false);
            }
            // Datei pruefen
            if (_fileName != null && !_fileName.equals("")) {
                File file = new File(_fileName);
                file.createNewFile();
                if (!file.canWrite()) {
                    throw new FileNotFoundException("File not writeable: " + _fileName);
                }
                if (_log != null) {
                    _log.debug2("Starte Export in die Datei...");
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // FileWriter fw = new FileWriter(_fileName);
                FileOutputStream fos = new FileOutputStream(_fileName);
                OutputStreamWriter fw = new OutputStreamWriter(fos, _xmlEncoding);
                // XML Header
                fw.write("<?xml version=\"1.0\" encoding=\"" + _xmlEncoding + "\"?>\n");
                fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname) + " application=\"" + _application + "\" created=\"" + dateFormat.format(new Date())
                        + "\">\n");
                dateFormat = null;
                // Abfragen ausfuehren
                for (int i = 0; i < _queries.cnt(); i++) {
                    if (_queries.get(i).isDone() == false && !_queries.get(i).isDependent()) {
                        if (_queries.get(i).getOperation() != null && _queries.get(i).getOperation().equalsIgnoreCase("delete")) {
                            exportQueriesForDelete(i, null, fw);
                        } else {
                            exportQueries(i, fw);
                        }
                    }
                }
                fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname) + ">\n");
                fw.close();
                fos.close();
                if (_log != null) {
                    _log.debug2("...Export in die Datei beendet");
                }
                return "";
            } else {
                if (_log != null) {
                    _log.debug2("Starte Export...");
                }
                StringBuilder output = new StringBuilder();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                // XML Header
                output.append("<?xml version=\"1.0\" encoding=\"" + _xmlEncoding + "\"?>\n");
                output.append(indent(1) + "<" + normalizeTagName(_xmlTagname) + " application=\"" + _application + "\" created=\""
                        + dateFormat.format(new Date()) + "\">\n");
                dateFormat = null;
                // Abfragen ausfuehren
                for (int i = 0; i < _queries.cnt(); i++) {
                    if (_queries.get(i).isDone() == false) {
                        output.append(exportQueries(i));
                    }
                }
                output.append(indent(-1) + "</" + normalizeTagName(_xmlTagname) + ">\n");
                // Ergebnis zurueck liefern
                if (_log != null) {
                    _log.debug2("...Export beendet");
                }
                return output.toString();
            }
        } catch (Exception e) {
            throw new Exception("SOSExport.export: " + e.getMessage(), e);
        }
    }

    /** Export anhand der registrierten Abfragen ausf&uuml;hren.
     * 
     * @param conn Datenbankverbindung der SOSConnection Klasse
     * @param fileName Datei, in die Exportiert wird. Wenn file_name == null,
     *            dann wird das Ergebnis zur&uuml;ck gegeben.
     * @return leerer String bei einer Datei - sonst Inhalt des Exports
     * @throws java.lang.Exception, java.io.FileNotFoundException */
    public String doExport(SOSConnection conn, String fileName) throws Exception, FileNotFoundException {
        _conn = conn;
        _fileName = fileName;
        return doExport();
    }

    /** Rekursiver Export einer Abfrage mit deren Unterabfragen als String
     * bilden.
     * 
     * @param queryId Nr. der aktuellen Abfrage
     * @return XML Export als String
     * @throws java.lang.Exception */
    private String exportQueries(int queryId) throws Exception {
        return exportQueries(queryId, new ArrayList());
    }

    /** Rekursiver Export einer Abfrage mit deren Unterabfragen gleich in eine
     * Datei schreiben.
     * 
     * @param queryId Nr. der aktuellen Abfrage
     * @param fw FileWriter Objekt zum Schreiben in eine Datei
     * @return XML Export als String
     * @throws java.lang.Exception */
    private String exportQueries(int queryId, Writer fw) throws Exception {
        return exportQueries(queryId, new ArrayList(), fw);
    }

    /** Rekursiver Export einer Abfrage mit deren Unterabfragen gleich in eine
     * Datei schreiben.
     * 
     * @param queryId Nr. der aktuellen Abfrage
     * @param parameterValues Substitutionswerte f&uuml;r den SQL-Statement
     * @param fw FileWriter Objekt zum Schreiben in eine Datei
     * @return Leerstring
     * @throws java.lang.Exception */
    private String exportQueries(int queryId, ArrayList parameterValues, Writer fw) throws Exception {
        try {
            if (_log != null) {
                _log.debug3("export_queries: " + " name=\"" + _queries.get(queryId).getTag() + "\" query_id=\"" + queryId + "\" key=\""
                        + _queries.get(queryId).keysToStr() + "\" dependend=\"" + _queries.get(queryId).dependRefToStr() + "\" operation=\""
                        + _queries.get(queryId).getOperation() + "\" independend=\"" + _queries.get(queryId).indepdRefToStr() + "\"");
            }
            _rekursionCnt++;
            // Parameterwerte in das SQL-Statement einfuegen
            String queryStm = substituteQuery(_queries.get(queryId).getQuery(), parameterValues);
            HashMap allFieldNames = prepareGetFieldName(queryStm);
            // Feldinformationen abrufen
            SOSImExportTableFieldTypes fieldTypes = getFieldTypes(queryStm.toString());
            // Daten abrufen
            ArrayList result = new ArrayList();
            result = getArray(queryStm.toString());
            fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_package id=\"") + _queries.get(queryId).getTag() + "\">\n");
            if (!result.isEmpty()) {
                // // META START ////
                fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_meta") + ">\n");
                fw.write(indent(0) + "<" + normalizeTagName("table name=\"") + _queries.get(queryId).getTag() + "\" />\n");
                // Key-Felder
                fw.write(indent(1) + "<" + normalizeTagName("key_fields") + ">\n");
                for (int i = 0; i < _queries.get(queryId).getKeyCnt(); i++) {
                    if (_log != null) {
                        _log.debug6("key_field[" + i + "]=\"" + _queries.get(queryId).getKey(i) + "\"");
                    }
                    fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName(_queries.get(queryId).getKey(i)) + "\"");
                    fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    fw.write(" />\n");
                }
                fw.write(indent(-1) + normalizeTagName("</key_fields>") + "\n");
                // Felder
                fw.write(indent(1) + normalizeTagName("<fields>") + "\n");
                Object[] fields = ((HashMap) result.get(0)).keySet().toArray();
                for (int i = 0; i < fields.length; i++) {
                    fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName((String) fields[i]) + "\"");
                    fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName((String) fields[i])) + "\"");
                    fw.write(" />\n");
                }
                fields = null;
                fw.write(indent(-1) + normalizeTagName("</fields>") + "\n");
                fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_meta") + ">\n");
                // // META END ////
                // Records schreiben
                fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_data") + ">\n");
                // Zeilen
                for (int i = 0; i < result.size(); i++) {
                    HashMap record = (HashMap) result.get(i);
                    if (_log != null) {
                        _log.debug9("get: " + _queries.get(queryId).getTag() + " query_id=" + queryId);
                    }
                    fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_record name=\"") + _queries.get(queryId).getTag() + "\">\n");
                    fw.write(indent(1)
                            + "<"
                            + normalizeTagName(_xmlTagname + "_fields")
                            + (_queries.get(queryId).getOperation() != null && _queries.get(queryId).getOperation().length() > 0 ? " operation=\""
                                    + _queries.get(queryId).getOperation() + "\" " : "") + ">\n");
                    // Spalten alphabetische Sortierung
                    // Vector record_vector = new Vector(record.keySet());
                    // Collections.sort(record_vector);
                    for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                        String key = it.next().toString();
                        String lobType = null;
                        switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                        case Types.CLOB:
                        case Types.LONGVARCHAR:
                            lobType = "clob";
                            break;
                        case Types.BLOB:
                        case Types.BINARY:
                        case Types.VARBINARY:
                        case Types.LONGVARBINARY:
                            lobType = "blob";
                            break;
                        default:
                            // ...sonst als xml string ausgeben
                            if (record.get(key) != null) {
                                switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                                case Types.DATE:
                                case Types.TIMESTAMP:
                                    String val = record.get(key).toString();
                                    if (val.endsWith(".0")) {
                                        record.put(key, val.substring(0, val.length() - 2));
                                    }
                                }
                            }
                            fw.write(indent() + "<" + normalizeTagName(key) + " null=");
                            if (record.get(key) != null) {
                                fw.write("\"false\"><![CDATA[" + asXml(record.get(key).toString()) + "]]>");
                            } else {
                                fw.write("\"true\"><![CDATA[]]>");
                            }
                            fw.write("</" + normalizeTagName(key) + ">\n");
                            break;
                        }
                        if (lobType != null) {
                            // // als binaer behandeln und in hex umwandeln ////
                            // create blob-stm
                            int posBegin = new String(queryStm).toLowerCase().indexOf("from");
                            int posEnd = new String(queryStm).toLowerCase().indexOf(" ", posBegin + 5);
                            StringBuilder queryBlobStm = new StringBuilder();
                            String blobFieldName = getBlobFieldName(allFieldNames, key.toString());
                            queryBlobStm.append("SELECT " + normalizeFieldName(blobFieldName) + " ");
                            if (posEnd < posBegin) {
                                posEnd = queryStm.length();
                            }
                            queryBlobStm.append(queryStm.substring(posBegin, posEnd));
                            String and = " WHERE ";
                            for (int j = 0; j < _queries.get(queryId).getKeyCnt(); j++) {
                                String keyFieldName = getKeyFieldName(allFieldNames, _queries.get(queryId).getKey(j));
                                queryBlobStm.append(and + normalizeFieldName(keyFieldName) + " =");
                                queryBlobStm.append(quote(fieldTypes.getTypeId(normalizeFieldName(_queries.get(queryId).getKey(j))),
                                        (String) record.get(normalizeFieldName(_queries.get(queryId).getKey(j)))));
                                and = " AND ";
                            }
                            // blob mit Umbruch ausgeben
                            byte[] blob = null;
                            if (lobType.equals("blob")) {
                                blob = _conn.getBlob(queryBlobStm.toString());
                            } else {
                                blob = str2bin(_conn.getClob(queryBlobStm.toString()));
                            }
                            fw.write(indent() + "<" + normalizeTagName(key) + " null=");
                            if (blob != null && blob.length > 0) {
                                indent(1);
                                fw.write("\"false\">\n");
                                toHexString(blob, indent(), _lineWrap, fw);
                                fw.write("\n" + indent(-1));
                            } else {
                                fw.write("\"true\">");
                            }
                            fw.write("</" + normalizeTagName(key) + ">\n");
                        }
                    }
                    fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_fields") + ">\n");
                    // Rekursion independend refs
                    if (_queries.get(queryId).getIndepdRefCnt() > 0) {
                        for (int j = 0; j < _queries.get(queryId).getIndepdRefCnt(); j++) {
                            int recQuery = _queries.get(queryId).getIndepdRef(j).intValue();
                            if (_log != null) {
                                _log.debug6("recursive independend query: " + _queries.get(recQuery).getQuery());
                            }
                            exportQueries(recQuery, queryParams(recQuery, record), fw);
                        }
                    }
                    // Rekursion dependend refs
                    if (_queries.get(queryId).getDependRefCnt() > 0) {
                        for (int j = 0; j < _queries.get(queryId).getDependRefCnt(); j++) {
                            int recQuery = _queries.get(queryId).getDependRef(j).intValue();
                            if (_log != null) {
                                _log.debug6("recursive dependend query: " + _queries.get(recQuery).getQuery());
                            }
                            exportQueries(recQuery, queryParams(recQuery, record), fw);
                        }
                    }
                    fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_record") + ">\n");
                }
                fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_data") + ">\n");
            }
            fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_package") + ">\n");
            _queries.get(queryId).setDone(true);
            return "";
        } catch (Exception e) {
            throw new Exception("SOSExport.exportQueries: " + e.getMessage(), e);
        }
    }

    /** Rekursiver Export einer Abfrage mit deren Unterabfragen gleich in eine
     * Datei schreiben.
     * 
     * @param queryId Nr. der aktuellen Abfrage
     * @param parameterValues Substitutionswerte f&uuml;r den SQL-Statement
     * @param fw FileWriter Objekt zum Schreiben in eine Datei
     * @return Leerstring
     * @throws java.lang.Exception */
    public String exportQueriesForDelete(int queryId, ArrayList parameterValues, Writer fw) throws Exception {
        try {
            if (_log != null) {
                _log.debug3("export_queries: " + " name=\"" + _queries.get(queryId).getTag() + "\" query_id=\"" + queryId + "\" key=\""
                        + _queries.get(queryId).keysToStr() + "\" dependend=\"" + _queries.get(queryId).dependRefToStr() + "\" operation=\""
                        + _queries.get(queryId).getOperation() + "\" field_keys=\"" + _queries.get(queryId).getFieldsKeys() + "\" independend=\""
                        + _queries.get(queryId).indepdRefToStr() + "\"");
            }
            _rekursionCnt++;
            // Parameterwerte in das SQL-Statement einfuegen
            String queryStm = substituteQuery(_queries.get(queryId).getQuery(), parameterValues);
            HashMap allFieldNames = prepareGetFieldName(queryStm);
            // Feldinformationen abrufen
            SOSImExportTableFieldTypes fieldTypes = getFieldTypes(queryStm.toString());
            fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_package id=\"") + _queries.get(queryId).getTag() + "\">\n");
            // // META START ////
            fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_meta") + ">\n");
            fw.write(indent(0) + "<" + normalizeTagName("table name=\"") + _queries.get(queryId).getTag() + "\" />\n");
            // Key-Felder
            fw.write(indent(1) + "<" + normalizeTagName("key_fields") + ">\n");
            for (int i = 0; i < _queries.get(queryId).getKeyCnt(); i++) {
                if (_log != null) {
                    _log.debug6("key_field[" + i + "]=\"" + _queries.get(queryId).getKey(i) + "\"");
                }
                fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName(_queries.get(queryId).getKey(i)) + "\"");
                fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                fw.write(" />\n");
            }
            fw.write(indent(-1) + normalizeTagName("</key_fields>") + "\n");
            // Felder
            fw.write(indent(1) + normalizeTagName("<fields>") + "\n");
            Object[] fields = _queries.get(queryId).getFieldsKeys().keySet().toArray();
            for (int i = 0; i < fields.length; i++) {
                fw.write(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName((String) fields[i]) + "\"");
                fw.write(" type=\"" + fieldTypes.getTypeName(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" len=\"" + fieldTypes.getLength(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" scale=\"" + fieldTypes.getScale(normalizeFieldName((String) fields[i])) + "\"");
                fw.write(" />\n");
            }
            fields = null;
            fw.write(indent(-1) + normalizeTagName("</fields>") + "\n");
            fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_meta") + ">\n");
            // // META END ////
            // Records schreiben
            fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_data") + ">\n");
            // Zeilen
            HashMap record = _queries.get(queryId).getFieldsKeys();
            if (_log != null) {
                _log.debug9("get: " + _queries.get(queryId).getTag() + " query_id=" + queryId);
            }
            fw.write(indent(1) + "<" + normalizeTagName(_xmlTagname + "_record name=\"") + _queries.get(queryId).getTag() + "\">\n");
            fw.write(indent(1)
                    + "<"
                    + normalizeTagName(_xmlTagname + "_fields")
                    + (_queries.get(queryId).getOperation() != null && _queries.get(queryId).getOperation().length() > 0 ? " operation=\""
                            + _queries.get(queryId).getOperation() + "\" " : "") + ">\n");
            for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                String key = it.next().toString();
                if (record.get(key) != null) {
                    switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                    case Types.DATE:
                    case Types.TIMESTAMP:
                        String val = record.get(key).toString();
                        if (val.endsWith(".0")) {
                            record.put(key, val.substring(0, val.length() - 2));
                        }
                    }
                }
                if (record.get(key) != null && record.get(key).toString().length() > 0) {
                    fw.write(indent() + "<" + normalizeTagName(key) + " null=");
                    if (record.get(key) != null) {
                        fw.write("\"false\"><![CDATA[" + asXml(record.get(key).toString()) + "]]>");
                    } else {
                        fw.write("\"true\"><![CDATA[]]>");
                    }
                    fw.write("</" + normalizeTagName(key) + ">\n");
                }
            } // iteration Fields
            fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_fields") + ">\n");
            // Rekursion independend refs
            if (_queries.get(queryId).getIndepdRefCnt() > 0) {
                for (int j = 0; j < _queries.get(queryId).getIndepdRefCnt(); j++) {
                    int recQuery = _queries.get(queryId).getIndepdRef(j).intValue();
                    if (_log != null) {
                        _log.debug6("recursive independend query: " + _queries.get(recQuery).getQuery());
                    }
                    exportQueries(recQuery, queryParams(recQuery, record), fw);
                }
            }
            // Rekursion dependend refs
            if (_queries.get(queryId).getDependRefCnt() > 0) {
                for (int j = 0; j < _queries.get(queryId).getDependRefCnt(); j++) {
                    int recQuery = _queries.get(queryId).getDependRef(j).intValue();
                    if (_log != null) {
                        _log.debug6("recursive dependend query: " + _queries.get(recQuery).getQuery());
                    }
                    exportQueriesForDelete(recQuery, queryParams(recQuery, record), fw);
                }
            }

            fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_record") + ">\n");
            fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_data") + ">\n");
            fw.write(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_package") + ">\n");
            _queries.get(queryId).setDone(true);
            return "";
        } catch (Exception e) {
            throw new Exception("SOSExport.exportQueries: " + e.getMessage(), e);
        }
    }

    /** Rekursiver Export einer Abfrage mit deren Unterabfragen.
     * 
     * @param queryId Nr. der aktuellen Abfrage
     * @param parameterValues Substitutionswerte f&uuml;r den SQL-Statement
     * @return XML Export als String
     * @throws java.lang.Exception */
    private String exportQueries(int queryId, ArrayList parameterValues) throws Exception {
        StringBuilder output = new StringBuilder();
        try {
            if (_log != null) {
                _log.debug3("export_queries: name=\"" + _queries.get(queryId).getTag() + "\" query_id=\"" + queryId + "\" key=\""
                        + _queries.get(queryId).keysToStr() + "\" dependend=\"" + _queries.get(queryId).dependRefToStr() + "\" independend=\""
                        + _queries.get(queryId).indepdRefToStr() + "\"");
            }
            _rekursionCnt++;
            // Parameterwerte in das SQL-Statement einfuegen
            String queryStm = substituteQuery(_queries.get(queryId).getQuery(), parameterValues);
            HashMap allFieldNames = prepareGetFieldName(queryStm);
            // Feldinformationen abrufen
            SOSImExportTableFieldTypes fieldTypes = getFieldTypes(queryStm.toString());
            // Daten abrufen
            ArrayList result = new ArrayList();
            result = getArray(queryStm.toString());
            output.append(indent(1) + "<" + normalizeTagName(_xmlTagname + "_package id=\"") + _queries.get(queryId).getTag() + "\">\n");
            if (!result.isEmpty()) {
                // // META START ////
                output.append(indent(1) + "<" + normalizeTagName(_xmlTagname + "_meta") + ">\n");
                output.append(indent(0) + "<" + normalizeTagName("table name=\"") + _queries.get(queryId).getTag() + "\" />\n");
                // Key-Felder
                output.append(indent(1) + "<" + normalizeTagName("key_fields") + ">\n");
                for (int i = 0; i < _queries.get(queryId).getKeyCnt(); i++) {
                    if (_log != null) {
                        _log.debug6("key_field[" + i + "]=\"" + _queries.get(queryId).getKey(i) + "\"");
                    }
                    output.append(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName(_queries.get(queryId).getKey(i)) + "\"");
                    output.append(" type=\"" + fieldTypes.getTypeName(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    output.append(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    output.append(" len=\"" + fieldTypes.getLength(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    output.append(" scale=\"" + fieldTypes.getScale(normalizeFieldName(_queries.get(queryId).getKey(i))) + "\"");
                    output.append(" />\n");
                }
                output.append(indent(-1) + normalizeTagName("</key_fields>") + "\n");
                // Felder
                output.append(indent(1) + normalizeTagName("<fields>") + "\n");
                Object[] fields = ((HashMap) result.get(0)).keySet().toArray();
                for (int i = 0; i < fields.length; i++) {
                    output.append(indent() + "<" + normalizeTagName("field name=\"") + normalizeFieldName((String) fields[i]) + "\"");
                    output.append(" type=\"" + fieldTypes.getTypeName(normalizeFieldName((String) fields[i])) + "\"");
                    output.append(" typeID=\"" + fieldTypes.getTypeId(normalizeFieldName((String) fields[i])) + "\"");
                    output.append(" len=\"" + fieldTypes.getLength(normalizeFieldName((String) fields[i])) + "\"");
                    output.append(" scale=\"" + fieldTypes.getScale(normalizeFieldName((String) fields[i])) + "\"");
                    output.append(" />\n");
                }
                fields = null;
                output.append(indent(-1) + normalizeTagName("</fields>") + "\n");
                output.append(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_meta") + ">\n");
                // // META END ////
                // Records schreiben
                output.append(indent(1) + "<" + normalizeTagName(_xmlTagname + "_data") + ">\n");
                // Zeilen
                for (int i = 0; i < result.size(); i++) {
                    HashMap record = (HashMap) result.get(i);
                    if (_log != null) {
                        _log.debug9("get: " + _queries.get(queryId).getTag() + " query_id=" + queryId);
                    }
                    output.append(indent(1) + "<" + normalizeTagName(_xmlTagname + "_record name=\"") + _queries.get(queryId).getTag() + "\">\n");
                    output.append(indent(1) + "<" + normalizeTagName(_xmlTagname + "_fields") + ">\n");
                    // Spalten alphabetische Sortierung
                    for (Iterator it = record.keySet().iterator(); it.hasNext();) {
                        String key = it.next().toString();
                        String lobType = null;
                        switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                        case Types.CLOB:
                        case Types.LONGVARCHAR:
                            lobType = "clob";
                            break;
                        case Types.BLOB:
                        case Types.BINARY:
                        case Types.VARBINARY:
                        case Types.LONGVARBINARY:
                            lobType = "blob";
                            break;
                        default:
                            // ...sonst als xml string ausgeben
                            if (record.get(key) != null) {
                                switch (fieldTypes.getTypeId(normalizeFieldName(key))) {
                                case Types.DATE:
                                case Types.TIMESTAMP:
                                    String val = record.get(key).toString();
                                    if (val.endsWith(".0")) {
                                        record.put(key, val.substring(0, val.length() - 2));
                                    }
                                }
                            }
                            output.append(indent() + "<" + normalizeTagName(key) + " null=");
                            if (record.get(key) != null) {
                                output.append("\"false\"><![CDATA[" + asXml(record.get(key).toString()) + "]]>");
                            } else {
                                output.append("\"true\"><![CDATA[]]>");
                            }
                            output.append("</" + normalizeTagName(key) + ">\n");
                            break;
                        }
                        if (lobType != null) {
                            // // als binaer behandeln und in hex umwandeln ////
                            // create blob-stm
                            int posBegin = new String(queryStm).toLowerCase().indexOf("from");
                            int posEnd = new String(queryStm).toLowerCase().indexOf(" ", posBegin + 5);
                            StringBuilder queryBlobStm = new StringBuilder();
                            String blobFieldName = getBlobFieldName(allFieldNames, key.toString());
                            queryBlobStm.append("SELECT " + normalizeFieldName(blobFieldName) + " ");
                            if (posEnd < posBegin) {
                                posEnd = queryStm.length();
                            }
                            queryBlobStm.append(queryStm.substring(posBegin, posEnd));
                            String and = " WHERE ";
                            for (int j = 0; j < _queries.get(queryId).getKeyCnt(); j++) {
                                String keyFieldName = getKeyFieldName(allFieldNames, _queries.get(queryId).getKey(j));
                                queryBlobStm.append(and + normalizeFieldName(keyFieldName) + " =");
                                queryBlobStm.append(quote(fieldTypes.getTypeId(normalizeFieldName(_queries.get(queryId).getKey(j))),
                                        (String) record.get(normalizeFieldName(_queries.get(queryId).getKey(j)))));
                                and = " AND ";
                            }
                            // blob mit Umbruch ausgeben
                            byte[] blob = null;
                            if (lobType.equals("blob")) {
                                blob = _conn.getBlob(queryBlobStm.toString());
                            } else {
                                blob = str2bin(_conn.getClob(queryBlobStm.toString()));
                            }
                            output.append(indent() + "<" + normalizeTagName(key) + " null=");
                            if (blob != null && blob.length > 0) {
                                indent(1);
                                output.append("\"false\">\n" + toHexString(blob, indent(), _lineWrap) + "\n" + indent(-1));
                            } else {
                                output.append("\"true\">");
                            }
                            output.append("</" + normalizeTagName(key) + ">\n");
                        }
                    }
                    output.append(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_fields") + ">\n");
                    // Rekursion independend refs
                    if (_queries.get(queryId).getIndepdRefCnt() > 0) {
                        for (int j = 0; j < _queries.get(queryId).getIndepdRefCnt(); j++) {
                            int recQuery = _queries.get(queryId).getIndepdRef(j).intValue();
                            if (_log != null) {
                                _log.debug6("recursive independend query: " + _queries.get(recQuery).getQuery());
                            }
                            output.append(exportQueries(recQuery, queryParams(recQuery, record)));
                        }
                    }
                    // Rekursion dependend refs
                    if (_queries.get(queryId).getDependRefCnt() > 0) {
                        for (int j = 0; j < _queries.get(queryId).getDependRefCnt(); j++) {
                            int recQuery = _queries.get(queryId).getDependRef(j).intValue();
                            if (_log != null) {
                                _log.debug6("recursive dependend query: " + _queries.get(recQuery).getQuery());
                            }
                            output.append(exportQueries(recQuery, queryParams(recQuery, record)));
                        }
                    }
                    output.append(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_record") + ">\n");
                }
                output.append(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_data") + ">\n");
            }
            output.append(indent(-1) + "</" + normalizeTagName(_xmlTagname + "_package") + ">\n");
            _queries.get(queryId).setDone(true);
            return output.toString();
        } catch (Exception e) {
            throw new Exception("SOSExport.exportQueries: " + e.getMessage(), e);
        }
    }

    /*************************************************************************** Da es im SQL Statement Felder mit "AS" selektiert werden können, ordnet
     * diese Methode die originale Feldnamen zu dem neuen Feldnamen zu z.B :
     * select vorname as name,nachname from table [name] = vorname [nachname] =
     * nachname
     * 
     * @param stmt SQL Statement
     * @return wenn select * leere HashMap, sonst HashMap mit dem Schlüssel
     *         -neuer Feldname wert - alter Feldname ***************************************************************************/
    private HashMap prepareGetFieldName(String stmt) throws Exception {
        int posBegin = new String(stmt).toUpperCase().indexOf("SELECT");
        int posEnd = new String(stmt).toUpperCase().indexOf("FROM");
        if (posBegin == -1 || posEnd == -1) {
            throw new Exception("sql statement is not valid : " + stmt);
        }
        String selectFields = stmt.substring(posBegin + 6, posEnd).trim().toUpperCase();
        HashMap hm = new HashMap();
        if (!selectFields.equals("*")) {
            String[] splitFields = selectFields.split(",");
            for (int i = 0; i < splitFields.length; i++) {
                String field = splitFields[i].trim();
                String[] split = field.split(" ");
                int len = split.length;
                if (len > 1) {
                    String lastName = split[len - 1].replace('"', ' ').trim();
                    if (field.indexOf(" AS ") != -1) {
                        String firstName = split[0].replace('"', ' ').trim();
                        hm.put(lastName, firstName);
                    }
                } else {
                    String firstName = field.replace('"', ' ').trim();
                    hm.put(firstName, firstName);
                }
            }
        }
        return hm;
    }

    /** da es vorkommen kann, dass mann einen Blob|Clob unter einem anderen Namen
     * selektiert, (select BLOB_FELD as BLOB ...) ermittelt diese Methode
     * Blob|Clob Feldname für getBlob
     * 
     * @param stmt gesamte SQL Statement
     * @param sampleFieldName Feldname aus DB Meta-Daten */
    private String getBlobFieldName(HashMap hm, String sampleFieldName) throws Exception {
        String sample = sampleFieldName.toUpperCase();
        if (hm != null && hm.size() > 0) {
            if (hm.containsKey(sample)) {
                return "\"" + hm.get(sample) + "\" as \"" + sample + "\"";
            }
        }
        return "\"" + sample + "\"";
    }

    /** @param stmt gesamte SQL Statement
     * @param sampleFieldName Feldname aus DB Meta-Daten */
    private String getKeyFieldName(HashMap hm, String sampleFieldName) throws Exception {
        String sample = sampleFieldName.toUpperCase();
        if (hm != null && hm.size() > 0) {
            if (hm.containsKey(sample)) {
                return "\"" + hm.get(sample) + "\"";
            }
        }
        return "\"" + sample + "\"";
    }

    /** Liefert die Abfrageparameter als kommaseparierte Liste.
     * 
     * @param queryId Nr. der Abfrage
     * @param record HashMap mit Record-Daten
     * @return kommaseparierte Liste der Parameter
     * @throws java.lang.Exception */
    public ArrayList queryParams(int queryId, HashMap record) throws Exception {
        ArrayList values = new ArrayList();
        try {
            if (_log != null) {
                _log.debug3("query_params: query_id=" + queryId);
            }
            for (int i = 0; i < _queries.get(queryId).getParameterCnt(); i++) {
                if (_log != null) {
                    _log.debug6("param_value[" + i + "] = " + _queries.get(queryId).getParameter(i));
                }
                if (record.containsKey(normalizeFieldName(_queries.get(queryId).getParameter(i)))) {
                    values.add(record.get(normalizeFieldName(_queries.get(queryId).getParameter(i))));
                }
            }
            return values;
        } catch (Exception e) {
            throw new Exception("SOSExport.queryParams: " + e.getMessage(), e);
        }
    }

    /** F&uuml;gt die Parameterwerte der Reihe nach in das SQL-Statement ein.
     * 
     * @param query SQL-Statement, in dem jedes ?-Zeichen substituiert wird
     * @param values Werte, die mit den ?-Zeichen substituiert werden sollen
     * @return substituiertes SQL-Statement */
    private String substituteQuery(String query, ArrayList values) {
        int matches = countStr(query, "?");
        String[] queryParts = query.split("\\?");
        if (matches == 0) {
            return query;
        }
        // zu wenige werte
        if (matches > values.size()) {
            throw new IllegalArgumentException("SOSExport.substituteQuery: too few values for substitution");
        }
        StringBuilder queryStm = new StringBuilder();
        for (int i = 0; i < queryParts.length; i++) {
            queryStm.append(queryParts[i]);
            if (i < values.size()) {
                if (values.get(i) == null || values.get(i).equals("")) {
                    queryStm.append("NULL");
                } else {
                    queryStm.append(values.get(i));
                }
            }
        }
        return queryStm.toString();
    }

    /** Liefert die Anzahl der Vorkommen von part in string.
     * 
     * @param string String, in dem gesucht werden soll
     * @param part Teilstring, der gesucht werden soll
     * @return gefundene Anzahl von part in string */
    private static int countStr(String string, String part) {
        int index = 0;
        int matches = 0;
        index = string.indexOf(part, index);
        while (index >= 0) {
            matches++;
            index = string.indexOf(part, ++index);
        }
        return matches;
    }

    /** Liefert die Treffer als ArrayList aus HashMaps zur&uuml;ck. Die Feldnamen
     * sind in lower-case abgespeichert. SQL NULL Werte und Blobs/Clobs werden
     * mit null abgelegt.
     * 
     * @param query SQL-Statement
     * @return ArrayList aus HashMaps
     * @throws Exception */
    private ArrayList getArray(String query) throws Exception {
        try {
            _conn.executeQuery(query);
            ResultSet rs = _conn.getResultSet();
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            int i, n = rsmd.getColumnCount();
            ArrayList result = new ArrayList();
            while (rs.next()) {
                HashMap row = new HashMap(n);
                for (i = 1; i <= n; i++) {
                    switch (rsmd.getColumnType(i)) {
                    case Types.BIGINT:
                    case Types.DECIMAL:
                    case Types.DOUBLE:
                    case Types.FLOAT:
                    case Types.INTEGER:
                    case Types.REAL:
                    case Types.SMALLINT:
                    case Types.NUMERIC:
                    case Types.TINYINT:
                    case Types.DATE:
                    case Types.TIMESTAMP:
                        row.put(rsmd.getColumnName(i), rs.getString(i));
                        break;
                    case Types.LONGVARCHAR:
                    case Types.BINARY:
                    case Types.BLOB:
                    case Types.CLOB:
                    case Types.LONGVARBINARY:
                    case Types.VARBINARY:
                        row.put(rsmd.getColumnName(i), null);
                        break;
                    default:
                        row.put(rsmd.getColumnName(i), rs.getString(i));
                        break;
                    }
                }
                result.add(row);
            }
            rs.close();
            return result;
        } catch (Exception e) {
            throw new Exception("SOSExport.getArray: " + e.getMessage());
        }
    }

    /** Liefert die Feldbeschreibungen zu einem SQL-Statement. Es wird in das
     * SQL-Statement entsprechend ein 1=0 eingef&uuml;gt.
     * 
     * @param query SQL-Statement, aus dem die Feldinformationene gewonnen
     *            werden sollen
     * @return Eine Liste der gefundenen Felbeschreibungen
     * @throws Exception */
    private SOSImExportTableFieldTypes getFieldTypes(String query) throws Exception {
        try {
            // 1=0 unter beruecksichtigung von einem order by am ende
            // hinzufuegen
            StringBuilder stm = new StringBuilder();
            int index = query.toLowerCase().indexOf("order by");
            if (index >= 0) {
                stm.append(query.substring(0, index - 1));
            } else {
                stm.append(query);
            }
            if (query.toLowerCase().indexOf("where") >= 0) {
                stm.append(" AND 1=0");
            } else {
                stm.append(" WHERE 1=0");
            }
            if (index >= 0) {
                stm.append(query.substring(index - 1));
            }
            // Feldinformationen abrufen
            _conn.executeQuery(stm.toString());
            ResultSet resultSet = _conn.getResultSet();
            SOSImExportTableFieldTypes fieldTypes = new SOSImExportTableFieldTypes();
            HashMap fieldDesc = new HashMap();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                fieldDesc = _conn.fieldDesc(i);
                Integer type = new Integer(fieldDesc.get("columnType").toString());
                BigInteger size = new BigInteger(fieldDesc.get("columnDisplaySize").toString());
                Integer scale = new Integer(fieldDesc.get("scale").toString());
                if (_log != null) {
                    _log.debug9("field_type: name=" + fieldDesc.get("columnName") + " type=" + fieldDesc.get("columnTypeName") + " type_id=" + type + " size="
                            + size + " scale=" + scale);
                }
                fieldTypes.addField(normalizeFieldName(normalizeFieldName((String) fieldDesc.get("columnName"))), (String) fieldDesc.get("columnTypeName"),
                        type, size, scale);
            }
            resultSet.close();
            return fieldTypes;
        } catch (Exception e) {
            throw new Exception("SOSExport.getFieldTypes: " + e.getMessage(), e);
        }
    }

    /** Quotiert einen Wert anhand seines SQL-Typs.
     * 
     * @param type jdbc Typ-ID des Feldes
     * @param val zu quotierender Wert
     * @return Quotierter Wert */
    private String quote(int type, String val) {
        if (val == null) {
            return "NULL";
        } else if (val.equals("")) {
            return "NULL";
        }
        switch (type) {
        case Types.DOUBLE:
            if (val.equalsIgnoreCase("null")) {
                val = "NULL";
            }
            return val;
        case Types.BIGINT:
        case Types.DECIMAL:
        case Types.FLOAT:
        case Types.INTEGER:
        case Types.REAL:
        case Types.SMALLINT:
        case Types.NUMERIC:
        case Types.TINYINT:
            if (val.equalsIgnoreCase("null")) {
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

    /** Gibt die kleinere zweier Zahlen zur&uuml;ck.
     * 
     * @param eins Erste Zahl
     * @param zwei Zweite Zahl
     * @return die kleinere Zahl */
    private int min(int eins, int zwei) {
        if (eins < zwei) {
            return eins;
        } else {
            return zwei;
        }
    }

    /** Liefert einen String f&uuml;r die Einr&uuml;ckung zur&uuml;ck. Ein pos.
     * Wert erh&ouml;ht die Einr&uuml;ckungsstufe f&uuml;r den n&auml;chste
     * Aufruf und ein neg. veringert diese um den Wert.
     * 
     * @param indent relative Einr&uuml;ckungsstufe
     * @return fertiger String f&uuml;r die Einr&uuml;ckung */
    private String indent(int indent) {
        int curIndent = _xmlIndent;
        _xmlIndent += indent;
        StringBuilder output = new StringBuilder(curIndent);
        if (indent > 0) {
            for (int i = 0; i < curIndent; i++) {
                output.append(_xmlIndentation);
            }
        } else {
            for (int i = 0; i < _xmlIndent; i++) {
                output.append(_xmlIndentation);
            }
        }
        return output.toString();
    }

    /** Liefert einen String f&uuml;r die Einr&uuml;ckung zur&uuml;ck -
     * gleichbleibend.
     * 
     * @return fertiger String f&uuml;r die Einr&uuml;ckung */
    private String indent() {
        return indent(0);
    }

    /** Normalisiert anhand von _normalizeTagName einen Tag.
     * 
     * @param tag String des Tags
     * @return normalisierter String des Tags */
    private String normalizeTagName(String tag) {
        if (_normalizeTagName.equalsIgnoreCase("strtoupper")) {
            return tag.toUpperCase();
        } else {
            return tag.toLowerCase();
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

    /** Wandelnt einen String in einen XML konformen String um.
     * 
     * @param str Eingabestring
     * @return XML konformer String */
    private static String asXml(String str) {
        return str;
    }

    /** Wandelt einen String in ein Byte-Array um.
     * 
     * @param str String
     * @return Byte-Array des Strings */
    private static byte[] str2bin(String str) {
        return str.getBytes();
    }

    /** Schreibt ein Byte-Array in eine Datei.
     * 
     * @param bin Byte-Array
     * @param indent Indent-String
     * @param wrap Zeilenumbruch (0 = keiner)
     * @return Hex-String des Byte-Arrays */
    private static String toHexString(byte[] b, String indent, int wrap, Writer fw) throws Exception {
        int length = b.length * 2;
        int indentLength = indent.length();
        if (wrap > 0) {
            // Die Laenge mit Zeilenumbruch und Einrueckungen ermitteln
            int indents = length / (wrap - indentLength);
            length = length + indents * (indentLength + "\n".length());
            length += indentLength;
        }
        int line = indentLength;
        if (wrap > 0) {
            fw.write(indent);
        }
        for (int i = 0; i < b.length; i++) {
            // oberer Byteanteil
            fw.write(_hexChar[(b[i] & 0xf0) >>> 4]);
            // unterer Byteanteil
            fw.write(_hexChar[b[i] & 0x0f]);
            line += 2;
            // Zeilenumbruch mit Einrueckung
            if (wrap > 0 && line >= wrap && i < (b.length - 1)) {
                fw.write("\n" + indent);
                line = indentLength;
            }
        }
        return "";
    }

    /** Wandelt ein Byte-Array in ein Hex-String um.
     * 
     * @param bin Byte-Array
     * @param indent Indent-String
     * @param wrap Zeilenumbruch (0 = keiner)
     * @return Hex-String des Byte-Arrays */
    private static String toHexString(byte[] b, String indent, int wrap) {
        int length = b.length * 2;
        int indentLength = indent.length();
        if (wrap > 0) {
            // Die Laenge mit Zeilenumbruch und Einrueckungen ermitteln
            int indents = length / (wrap - indentLength);
            length = length + indents * (indentLength + "\n".length());
            length += indentLength;
        }
        int line = indentLength;
        StringBuilder sb = new StringBuilder(length);
        if (wrap > 0) {
            sb.append(indent);
        }
        for (int i = 0; i < b.length; i++) {
            // oberer Byteanteil
            sb.append(_hexChar[(b[i] & 0xf0) >>> 4]);
            // unterer Byteanteil
            sb.append(_hexChar[b[i] & 0x0f]);
            line += 2;
            // Zeilenumbruch mit Einrueckung
            if (wrap > 0 && line >= wrap && i < (b.length - 1)) {
                sb.append("\n" + indent);
                line = indentLength;
            }
        }
        return sb.toString();
    }
}