package sos.settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.fileupload.DefaultFileItem;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileUploadException;

import sos.connection.SOSConnection;

import sos.util.SOSClassUtil;
import sos.util.SOSDate;
import sos.marshalling.SOSExport;
import sos.marshalling.SOSImport;
import sos.util.SOSLogger;
import sos.util.SOSResourceBundle;
import sos.util.SOSStandardLogger;

/**
 * Title: SOSSettingsDialog<br>
 * Description: Dialogklasse um Einstellungen für eine Applikation zu verarbeiten<br>
 * im ACL-Betrieb werden zusätzlich sos.user.jar und sos.acl.jar benötigt<br>
 * Copyright: Copyright (c) 2004<br>
 * Company: SOS Berlin GmbH<br>
 * @author <a href="mailto:robert.ehrlich@sos-berlin.com">Robert Ehrlich</a>
 * resource sos.connection.jar sos.util.jar servlet.jar commons-fileupload-1.0.jar
 * @version $Id$
 */
public class SOSSettingsDialog {

    /** Debuglevel */
    private int debugLevel = 0;

    /** ConnectionSettings Objekt */
    private SOSConnectionSettings settings;

    /** SOSConnection Objekt */
    private SOSConnection connection;

    /** SOSUser Objekt */
    private sos.user.SOSUser user;

    /** Session Objekt */
    private HttpSession session;

    /** Request Objekt */
    private HttpServletRequest request;

    /** Response Objekt */
    private HttpServletResponse response;

    /** Ausgabe Objekt */
    public JspWriter out;

    /** Settingstabelle */
    private String source = "";

    /** HTML Titel */
    private String title = "Settings Dialog";

    /** Name der rufenden Applikation */
    private String applicationName = "";

    /** Titel der Liste aller Applikationen */
    private String dialogApplicationsTitle = "(Alle Einstellungen)";

    /** Titel für neue Applikation */
    private String dialogApplicationsNewTitle = "(Neuer Bereich)";

    /** Titel für Export einer Applikation */
    private String dialogApplicationsExportTitle = "(Alle Einträge dieses Bereichs)";

    /** Titel der Liste aller Sektionen */
    private String dialogSectionsTitle = "Einstellungen des Bereichs";

    /** Titel für neue Sektion */
    private String dialogSectionsNewTitle = "(Neue Sektion)";

    /** Titel für Export einer Sektion */
    private String dialogSectionsExportTitle = "(Alle Einträge dieser Sektion)";

    /** Titel für neue Zähler-Sektion */
    private String dialogSectionsCounterTitle = "Nr. des letzten Eintrags für Tabelle";

    /** Titel für neue Schema-Sektion */
    private String dialogSectionsSchemaTitle = "(Schema des Bereichs)";

    /** Titel der Liste aller Einträge */
    private String dialogEntriesTitle = "Einstellungen der Sektion";

    /** Titel für neuen Eintrag */
    private String dialogEntriesNewTitle = "(Neuer Eintrag)";

    /** Verzeichnis der Graphiken */
    private String imgDir = "images/settings/";

    /** Graphik für Navigation */
    private String imgNavigation = "arr_rightlr.gif";

    /** Graphik für Aktionen */
    private String imgAction = "arr_rightb.gif";

    /** Graphik für Seitenanfang */
    private String imgTop = "arr_upb.gif";

    /** Graphik für Seitenende */
    private String imgBottom = "arr_downb.gif";

    /** Graphik für Hilfe-Symbol */
    private String imgHelp = "icon_help.gif";

    /** Navigieren in Applikationen zulassen */
    private boolean enableApplicationNavigation = true;

    /** Applikationsverwaltung zulassen */
    private boolean enableApplicationManager = true;

    /** Sektionsverwaltung zulassen */
    private boolean enableSectionManager = true;

    /** Sektionsverwaltung zulassen */
    private boolean enableEntryManager = true;

    /** Listenverwaltung zulassen */
    private boolean enableListManager = true;

    /** Anzeige der Namen von Einträgen in Listen zulassen */
    private boolean enableEntryNames = true;

    /** Ändern der Werte von Einträgen in Listen zulassen */
    private boolean enableEntryValues = true;

    /** Zwischenablage zulassen */
    private boolean enableClipboard = false;

    /** Anzeige des Buttons "Hilfe" zulassen */
    private boolean enableHelps = true;

    /** Anzeige des Buttons "Dokumentieren" zulassen */
    private boolean enableDocumentation = true;

    /** Export-Funktion zulassen */
    private boolean enableExport = true;

    /** Import-Funktion zulassen */
    private boolean enableImport = true;

    /** Import-Funktion: Dateigröße beschränken, -1 = beliebig */
    private long importMaxSize = -1;

    /** Name des Formular-Elements */
    private String form = "sos_settings";

    /** Name des Formular-Elements für Zusatzaktionen */
    private String formActions = "sos_settings_actions";

    /** Name der Web-Seite */
    private String site = "";

    /** Query-Parameter für Session-ID */
    private String sessionVAR = "JSESSIONID";

    /** Query-Parameterwert für Session-ID */
    private String sessionID = "";

    /** Session-ID wird von PHP automatisch übergeben: aus php.ini */
    private boolean sessionUseTransSID = true;

    /** CSS-Klasse für Aktionen via Link */
    private String styleLinkAction = "settingsLinkAction";

    /** CSS-Klasse für Aktionen via Link */
    private String styleLinkNavigation = "settingsLinkNavigation";

    /** CSS-Klasse für Aktionen via Link */
    private String styleLinkInactiveNavigation = "settingsLinkGrey";

    /** CSS-Klasse für Aktionen via Font */
    private String styleFontAction = "settingsFontAction";

    /** CSS-Klasse für Aktionen via Font */
    private String styleFontNavigation = "settingsFontNavigation";

    /** CSS-Style TABLE.settings = { .... } */
    private String styleTable = "settingsTable";

    /** CSS-Style: Tabellenränder 1/0 */
    private String styleBorder = "1";

    /** CSS-Style TR.settings = { .... } */
    private String styleTr = "settingsTable";

    /** CSS-Style TH.settings = { .... } */
    private String styleTh = "settingsTable";

    /** CSS-Style TD.settings = { .... } */
    private String styleTd = "settingsTable";

    /** CSS-Style TD.settings = { .... } */
    private String styleTdLabel = "settingsTableLabel";

    /** CSS-Style TD.settings = { .... } */
    private String styleTdBackground = "settingsTableBackground";

    /** CSS-Style INPUT.settings = { .... } */
    private String styleInput = "settingsInput";

    /** Inhalt des Headers */
    private String headerContent = "<p>";

    /** Default: Anzahl Zeichen für Eingabe eines Eintrags */
    private String defaultInputSize = "250";

    /** Default: Anzahl Pixel für Anzeige eines Eintrags */
    private String defaultDisplaySize = "50";

    /** Benutzername */
    private String author = "";

    /** Anzahl Zeilen bei Darstellung von Textareas */
    private int displayTextareaRows = 4;

    /** Anzahl Zeilen bei Darstellung von Document (long_value) Textareas */
    private int displayDocumentTextareaRows = 10;

    /** Datei mit Hilfetexten */
    private String helpFile = "settings_show_help.jsp";

    /** Breite des JS-Hilfefensters */
    private int helpWinWidth = 600;

    /** Höhe des JS-Hilfefensters */
    private int helpWinHeight = 500;

    /** Breite des JS-Hilfefensters */
    private int helpsWinWidth = 600;

    /** Höhe des JS-Hilfefensters */
    private int helpsWinHeight = 500;

    /** Template für die Generierung der Dokumentation */
    private String documentationFile = "settings_show_docu.jsp";

    /** ORDER BY für die Doku-Anzeige */
    private String documentationSort = "\"APPLICATION\" asc, \"SECTION\" asc, \"NAME\" asc";

    /** Breite des JS-Hilfefensters */
    private int docuWinWidth = 1000;

    /** Höhe des JS-Hilfefensters */
    private int docuWinHeight = 500;

    /** Liste der verfügbaren Applikationen */
    private Vector dialogApplications = new Vector();

    /** aktuell selektierte Applikation */
    private Integer dialogApplicationIndex = new Integer(-1);

    /** Liste der verfügbaren Sektionen */
    private Vector dialogSections = new Vector();

    /** aktuell selektierte Sektion */
    private Integer dialogSectionIndex = new Integer(-1);

    /** Liste der verfügbaren Einträge */
    private Vector dialogEntries = new Vector();

    /** aktuell selektierter Eintrag */
    private Integer dialogEntryIndex = new Integer(-1);

    private SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /** Anzeige des Buttons "Hilfe" zulassen */
    private boolean hasHelps = false;

    /** Nur Dokumentation exportieren */
    private int exportDocumentation = 0;

    /** Nur Dokumentation importieren */
    private int importDocumentation = 0;

    /**
     * Funktionsnamen für die Dokugestaltung in der Template Datei
     * "$this->documentation_template_file"
     */
    private String docuFuncNameApplication = "docu_application";

    private String docuFuncNameSection = "docu_section";

    private String docuFuncNameEntry = "docu_entry";

    /** Flag für neue Datensätze */
    private boolean isNew = false;

    /** Aktion der Transaktion: show, new, insert, store, delete */
    private String action = "";

    /** Bereich der Transaktion: application, section, entry */
    private String range = "";

    /** Element der Transaktion */
    private String item = "";

    /** Eingabeformat aus Schemasektion */
    private int applicationType = 0;

    /** Eingabeformat für Listen */
    private int sectionType = 0;

    /** Clipboard-Objekt (SOS_Clipboard) */
    // var $clipboard;
    /** Aktion der Zwischenablage: copy, paste */
    private String clipboardAction = "";

    private HashMap record = new HashMap();

    private boolean err = false;

    private String errMsg = "";

    private String msg = "";

    private String errLocation = "";

    private boolean enableDocuTemplateCss = false;

    private String inputQuery = "";
    
    private String replaceQuery	= "";

    private String inputExport = "";

    private String inputImport = "";

    private Vector entriesLongValues = new Vector();

    /** Leserecht */
    private boolean hasReadRight = true;

    /** Schreibrecht */
    private boolean hasWriteRight = true;

    /** Recht zum Löschen */
    private boolean hasDeleteRight = true;

    /** Create Recht */
    private boolean hasCreateRight = true;

    /**
     * Attribute von Eingabefeldern (kein Schreibrecht -> this.disabled = "
     * disabled ")
     */
    private String disabled = "";

    /** Tabelle ACL */
    private String tableAcls = "ACL";

    /** Settings benutzt ACL's */
    private boolean hasAcls = false;

    /** ACL-Betrieb mit Datenbank */
    private boolean hasAclDb = true;

    /** Alle ACL's mit Settingszuordnung */
    private HashMap allAcls = new HashMap();

    /** Bereich der ACL */
    private String aclRange = "application";

    /** Zeichen für alle Applikationen|Sektionen|Entries für eine ACL */
    private String allAclNote = "*";

    /**
     * ACL zum Schutzt der Anzeige von allen Applikationen bzw "neue
     * Applikation"
     */
    private String topLevelAcl = "";

    /** Schreibrecht der this.topLevelAcl */
    private boolean hasTopLevelWriteRight = true;

    /** Create Recht der this.topLevelAcl */
    private boolean hasTopLevelCreateRight = true;

    /** Leserecht der this.topLevelAcl */
    private boolean hasTopLevelReadRight = true;

    /** FCK Web Editor benutzen */
    private boolean enableEditor = false;

    /** Names des Editors - Request variable */
    private String editorName = "sos_fckeditor";

    /** FCK Web Editor Höhe */
    private String editorHeight = "300px";

    /** FCK Web Editor Breite - wird an td Element angewandt, daswegen in % */
    //private String editorWidth = "800px";
    private String editorWidth = "100%";

    private Locale locale = Locale.UK;

    private SOSResourceBundle rb = null;

    /** Name der Properties Datei */
    //private String bundleName =
    // this.getClass().getPackage().getName()+".sos_settings";
    private String bundleName = "sos_settings";

    /** Sprache der Anwendung */
    protected String sosLang;

    /**
     * Automatisches Setzen in der DB von gerade aktiven LONG_VALUE bzw VALUE
     * zulassen (Funktion - set_dialog_entries_long_values)
     */
    private boolean enableAutoSetLongValues = false;

    /**
     * Upload Element für Document binary anzeigen, benutzt wird als
     * style="display:none"
     */
    private String displayBinaryUpload = "none";

    /** long_value bei binary Dokumenten */
    private Hashtable hasBinaryValue = new Hashtable();

    /** Fleck für show_dialog_value */
    private boolean isShowEntries = false;
    
    /** aktuelle Request, Session Daten anzeigen */
    private boolean enableShowDevelopmentData = false;
    
    private String divDevelopmentDataLeft    = "850px";
    
    private String divDevelopmentDataTop	  = "10px";
    
    
    private String importOriginalFileName		= "";

    private boolean isSourceDownloaded					= false;
    
    /**
     * Konstruktor
     * 
     * @param sosConnection
     *            ein SOSConnection-Objekt
     * @param source
     *            Name der Tabelle für Einstellungen
     * @param servletRequest
     *            Request Objekt
     * @param servletResponse
     *            response Objekt
     * @param out
     *            JspWriter Objekt
     * @throws Exception
     * @see #SOSSettingsDialog( SOSConnection, String)
     */
    public SOSSettingsDialog(SOSConnection sosConnection, String source,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse, JspWriter out)
            throws Exception {

        this.connection = sosConnection;
        SOSLogger logger = new SOSStandardLogger(SOSStandardLogger.DEBUG9);
        this.settings = new SOSConnectionSettings(sosConnection, source, logger);

        this.request = servletRequest;
        this.response = servletResponse;
        this.out = out;
        this.site = this.request.getRequestURI();
        this.session = this.request.getSession(false);

        this.setDefaultLanguage();

    }

    /**
     * Konstruktor
     * 
     * @param sosConnection
     *            ein SOSConnection-Objekt
     * @param source
     *            Name der Tabelle für Einstellungen
     * @param logger
     *            Das Logger-Objekt
     * @param servletRequest
     *            Request Objekt
     * @param servletResponse
     *            response Objekt
     * @param out
     *            JspWriter Objekt
     * @throws Exception
     * @see #SOSSettingsDialog( SOSConnection, String,SOSLogger )
     */
    public SOSSettingsDialog(SOSConnection sosConnection, String source,
            SOSLogger logger, HttpServletRequest servletRequest,
            HttpServletResponse servletResponse, JspWriter out)
            throws Exception {

        this.connection = sosConnection;
        this.settings = new SOSConnectionSettings(sosConnection, source, logger);

        this.request = servletRequest;
        this.response = servletResponse;
        this.out = out;
        this.site = this.request.getRequestURI();
        this.session = this.request.getSession(false);

        this.setDefaultLanguage();

    }

    /**
     * Konstruktor
     * 
     * @param sosConnection
     *            DBConnection
     * @param source
     *            Name der Tabelle
     * @param application
     *            Name der Anwendung
     * @param logger
     *            Das Logger-Objekt
     * @param servletRequest
     *            Request Objekt
     * @param servletResponse
     *            response Objekt
     * @param out
     *            JspWriter Objekt
     * @throws Exception
     * @see #SOSSettingsDialog( SOSConnection, String,String, SOSLogger )
     */
    public SOSSettingsDialog(SOSConnection sosConnection, String source,
            String application, SOSLogger logger,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse, JspWriter out)
            throws Exception {

        this.connection = sosConnection;
        this.settings = new SOSConnectionSettings(sosConnection, source,
                application, logger);

        this.request = servletRequest;
        this.response = servletResponse;
        this.out = out;
        this.site = this.request.getRequestURI();
        this.session = this.request.getSession(false);

        this.setDefaultLanguage();

    }

    /**
     * @param sosConnection
     *            DBConnection
     * @param source
     *            Name der Tabelle
     * @param application
     *            Name der Anwendung
     * @param section
     *            Name der Sektion
     * @param logger
     *            Das Logger-Objekt
     * @param servletRequest
     *            Request Objekt
     * @param servletResponse
     *            response Objekt
     * @param out
     *            JspWriter Objekt
     * @throws Exception
     * @see #SOSSettingsDialog( SOSConnection, String, String, String, SOSLogger )
     */
    public SOSSettingsDialog(SOSConnection sosConnection, String source,
            String application, String section, SOSLogger logger,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse, JspWriter out)
            throws Exception {

        this.connection = sosConnection;
        this.settings = new SOSConnectionSettings(sosConnection, source,
                application, section, logger);

        this.request = servletRequest;
        this.response = servletResponse;
        this.out = out;
        this.site = this.request.getRequestURI();
        this.session = this.request.getSession(false);

        this.setDefaultLanguage();
    }

    /**
     * @throws Exception
     */
    private void setFeaturesLabels() throws Exception {

        /* Titel der Liste aller Applikationen */
        this.dialogApplicationsTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_applications_title");

        /* Titel für neue Applikation */
        this.dialogApplicationsNewTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_applications_new_title");

        /* Titel für Export einer Applikation */
        this.dialogApplicationsExportTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_applications_export_title");

        /* Titel der Liste aller Sektionen */
        this.dialogSectionsTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_sections_title");

        /* Titel für neue Sektion */
        this.dialogSectionsNewTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_sections_new_title");

        /* Titel für Export einer Sektion */
        this.dialogSectionsExportTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_sections_export_title");

        /* Titel für neue Zähler-Sektion */
        this.dialogSectionsCounterTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_sections_counter_title");

        /* Titel für neue Schema-Sektion */
        this.dialogSectionsSchemaTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_sections_schema_title");

        /* Titel der Liste aller Einträge */
        this.dialogEntriesTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_entries_title");

        /* Titel für neuen Eintrag */
        this.dialogEntriesNewTitle = this.rb
                .getMessage("sos.settings.dialog.dialog_entries_new_title");

    }

    /**
     * @throws Exception
     */
    private void setDefaultLanguage() throws Exception {

        this.rb = new SOSResourceBundle();

        this.rb.setBundle(this.bundleName, this.locale);

        this.sosLang = this.locale.getLanguage().toLowerCase();
        SOSDate.locale = this.locale;

    }

    /**
     * @param locale
     *            The locale to set.
     */
    public void setLocale(Locale locale) throws Exception {

        if (locale != null) {
            this.locale = locale;
        }

        if (this.rb == null) {
            this.rb = new SOSResourceBundle();
        }

        this.rb.setBundle(this.bundleName, this.locale);
        SOSDate.locale = this.locale;
        this.sosLang = this.locale.getLanguage().toLowerCase();

    }

    /**
     * @param locale
     *            The locale to set.
     */
    public void setLanguage(String language) throws Exception {

        if (language != null && language.length() != 0) {
            if (language.equalsIgnoreCase("en")) {
                this.locale = Locale.UK;
            } else if (language.equalsIgnoreCase("de")) {
                this.locale = Locale.GERMANY;
            }
        }

        if (this.locale == null) {
            this.locale = Locale.UK;
        }

        if (this.rb == null) {
            this.rb = new SOSResourceBundle();
        }

        this.rb.setBundle(this.bundleName, this.locale);
        SOSDate.locale = this.locale;
        this.sosLang = this.locale.getLanguage().toLowerCase();

    }

    /**
     * Aktionssteuerung
     * 
     * @throws Exception
     * @see #switchAction(String, String, String)
     */
    public void switchAction() throws Exception {

        this.switchAction(null, null, null);
    }

    /**
     * Aktionssteuerung
     * 
     * @throws Exception
     * @see #switchAction(String, String, String)
     */
    public void switchAction(String action, String range) throws Exception {

        this.switchAction(action, range, null);
    }

    /**
     * Aktionssteuerung
     * 
     * @param action
     *            Aktion
     * @param range
     *            Bereich der Aktion
     * @param item
     *            Transaktion
     * @throws Exception
     * @see #switchAction(String, String, String)
     */
    public void switchAction(String action, String range, String item)
            throws Exception {

        this.sessionUseTransSID = this.request.isRequestedSessionIdFromCookie();
        if (this.session != null) {
            this.sessionID = this.session.getId();
        }

        this.setFeaturesLabels();

        if (action != null) {
            this.action = action;
        }
        if (range != null) {
            this.range = range;
        }
        if (item != null) {
            this.item = item;
        }

        this.checkRequest();

				
        String switchAction = this.action.toLowerCase() + "_"
                + this.range.toLowerCase();
				
				
				if(switchAction.equals("download_source")) {
        	this.downloadSource(this.settings.application,this.settings.section,this.getRequestValue("download_entry"));
        }
				
				System.out.println("action = "+this.action+" range = "+this.range);
								
        this.debug(3, "switchAction : action = " + this.action + " range = "
                + this.range + " item = " + item);
        this.debug(3, "switchAction : application = "
                + this.settings.application + " section = "
                + this.settings.section + " entry = " + this.settings.entry);
				
				
        // Begin Aktionssteuerung
        if (switchAction.equals("show_applications")) {
            this.setDialogApplications(false);
            this.showDialogHeader(null);
            this.showDialogApplications(null, null);
        } else if (switchAction.equals("show_application")) {
            this.showDialogHeader(null);
            this.settings.section = this.settings.application;
            this.settings.entry = this.settings.application;
            this.setDialogApplications(false);
            if (this.recordGetKey(true)) {
                this.showDialogApplication();
            } 
            else {
                this.showDialogApplications(null, null);
            }
        } else if (switchAction.equals("new_application")) {
            this.showDialogHeader(null);
            this.isNew = true;
            this.dialogApplicationIndex = new Integer(-1);
            this.setDialogApplications(false);
            if (this.recordCreate()) {
                this.showDialogApplication();
            } else {
                this.showDialogApplications(null, null);
            }

        } else if (switchAction.equals("insert_application")) {
            this.showDialogHeader(null);
            this.isNew = true;
            if (this.recordInsert()) {
                this.settings.application = this.record.get("application").toString();

                if (this.setDialogApplications(true)) {
                    this.setDialogSections();
                    this.showDialogSections(null, null);
                }
            } 
            else {
                this.showDialogApplication();
                //this.setDialogApplications(false);
                //this.showDialogApplications(null, null);
            }
        } else if (switchAction.equals("store_application")) {
            this.showDialogHeader(null);
            if (this.recordStore()) {
                this.settings.application = this.record.get("application")
                        .toString();
                this.settings.section = this.record.get("section").toString();

                this.setDialogApplications(true);
                this.setDialogSections();
                this.showDialogSections(null, null);
            } else {
                this.setDialogApplications(false);
                this.showDialogApplications(null, null);
            }
        } else if (switchAction.equals("delete_application")) {
            this.showDialogHeader(null);
            this.recordDeleteKey();

            this.setDialogApplications(true);

            if (this.enableApplicationNavigation) {
                this.settings.application = "";
                this.settings.section = "";
                this.settings.entry = "";

                this.showDialogApplications(null, null);
            } else {
                this.isNew = true;
                this.dialogApplicationIndex = new Integer(-1);
                if (this.recordCreate()) {
                    this.showDialogApplication();
                } else {
                    this.showDialogApplications(null, null);
                }
            }
        } else if (switchAction.equals("show_sections")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.showDialogSections(null, null);
        } else if (switchAction.equals("show_section")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.settings.entry = this.settings.section;
            if (this.recordGetKey(true)) {
                this.showDialogApplication();
            } else {
                //this.setDialogSections();
                this.showDialogSections(null, null);
            }
        } else if (switchAction.equals("new_section")) {
            this.showDialogHeader(null);
            this.isNew = true;
            this.setDialogApplications(false);
            this.setDialogSections();
            if (this.recordCreate()) {
                this.showDialogApplication();
            } else {
                this.showDialogSections(null, null);
            }
        } else if (switchAction.equals("insert_section")) {
            this.showDialogHeader(null);
            this.isNew = true;
            this.setDialogApplications(false);
            this.recordInsert();
            this.setDialogSections();
            this.showDialogSections(null, null);

        } else if (switchAction.equals("store_section")) {
            this.showDialogHeader(null);
            this.recordStore();
            this.setDialogApplications(false);
            this.setDialogSections();
            this.showDialogSections(null, null);
        } else if (switchAction.equals("delete_section")) {
            this.showDialogHeader(null);
            this.recordDeleteKey();
            this.setDialogApplications(false);
            this.setDialogSections();
            this.showDialogSections(null, null);
        } else if (switchAction.equals("schema_section")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();

            if (this.recordGetKey(true)) {
                this.recordStore();
            } else {
                this.resetError();
                this.isNew = true;
                if (this.recordInsert()) {
                    this.isNew = false;
                }
            }
            this.showDialogEntry(1, 1);
        } else if (switchAction.equals("store_list")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            
            //this.setDialogEntries();
            this.recordStoreList();
            this.setDialogEntries();
           
            this.showDialogEntries(null, null);
            
            /*
            if (this.error()) {
                this.showDialogEntries(null, null);
            } 
            */
            
        } else if (switchAction.equals("show_entries")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            this.showDialogEntries(null, null);
        } else if (switchAction.equals("show_entry")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            if (this.recordGetKey(true)) {
                this.showDialogEntry(0, 0);
            } else {
                this.showDialogEntries(null, null);
            }
        } else if (switchAction.equals("schema_entry")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            if (this.recordGetKey(true)) {
                this.showDialogEntry(1, 0);
            } else {
                this.showDialogEntries(null, null);
            }
        } else if (switchAction.equals("new_entry")) {
            this.showDialogHeader(null);
            this.isNew = true;
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            if (this.recordCreate()) {
                this.showDialogEntry(1, 0);
            } else {
                this.showDialogEntries(null, null);
            }
        } else if (switchAction.equals("insert_entry")) {
            this.showDialogHeader(null);
            this.isNew = true;
            this.recordInsert();
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            this.showDialogEntries(null, null);
        } else if (switchAction.equals("store_entry")) {
            this.showDialogHeader(null);
            this.recordStore();
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            this.showDialogEntries(null, null);
        } else if (switchAction.equals("delete_entry")) {
            this.showDialogHeader(null);
            this.recordDeleteKey();
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            this.showDialogEntries(null, null);
        } else if (switchAction.equals("query_entries")) {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            if (this.queryEntries(this.inputQuery, 0,this.replaceQuery) != 0) {
                this.showDialogEntries(null, null);
            } else {
                if (!this.settings.section.equals("")) {
                    this.showDialogEntries(null, null);
                } else if (!this.settings.application.equals("")) {
                    this.showDialogSections(null, null);
                } else {
                    this.showDialogApplications(null, null);
                }

                this
                        .setError(
                                "&nbsp;&nbsp;&nbsp;&nbsp;"
                                        + this.rb
                                                .getMessage("sos.settings.dialog.err_not_found_entries"),
                                null);

            }
        } else if (switchAction.equals("duplicate_entries")) {
            this.showDialogHeader(null);
            inputQuery = "";
            if (this.getRequestValue("name") != null) {
                inputQuery = this.getRequestValue("name").trim();
            }
            //this.setDialogApplications(true);
            this.setDialogApplications(false);
            this.setDialogSections();
            this.setDialogEntries();
            if (this.queryEntries(inputQuery, 1, null) != 0) {
                this.showDialogEntries(null, null);
            } else {
                if (!this.settings.section.equals("")) {
                    this.showDialogEntries(null, null);
                } else if (!this.settings.application.equals("")) {
                    this.showDialogSections(null, null);
                } else {
                    this.showDialogApplications(null, null);
                }
                this
                        .setError(
                                "&nbsp;&nbsp;&nbsp;&nbsp;"
                                        + this.rb
                                                .getMessage("sos.settings.dialog.err_not_found_entries"),
                                null);
            }
        } else if (switchAction.equals("export_entries")) {
            this.exportEntries(this.inputExport, null);
        } else if (switchAction.equals("import_entries")) {

            String error = "";

            this.importEntries(this.inputImport, null);

            if (this.error()) {
                error = this.getError();
            }

            this.setDialogApplications(true);
            this.showDialogHeader(null);
            this.showDialogApplications(null, null);

            if (error.equals("")) {
                this.showMsg("&nbsp;&nbsp;&nbsp;&nbsp;"
                        + this.rb.getMessage("sos.settings.dialog.msg_import"));
            }

        } 
        else if (switchAction.equals("show_help")) {
            this.showHelp();
        } else if (switchAction.equals("show_helps")) {
            this.showHelps();
        } else if (switchAction.equals("show_documentation")) {
            this.showDocumentation();
        } else {
            this.showDialogHeader(null);
            this.setDialogApplications(false);
            this.showDialogApplications(null, null);

        }

        // Ende Aktionssteuerung

        this.destroy();

        if (this.msg != null && this.msg.length() > 0) {
            this.showMsg(this.msg);
            this.msg = "";
        }

        if (this.error()) {
            this.showError(this.getError());
            this.resetError();
        }

    }

    /**
     * Einträge im Profil suchen
     * 
     * @param query
     *            Suchstring
     * @param range
     * @param replaceQuery 		Replacement
     * @return int Grösse der dialogEntries
     * @throws Exception
     * @see #queryEntries(String, int)
     */

    private int queryEntries(String query, int range,String replaceQuery) throws Exception {


				boolean isReplaceQuery = this.item.equals("replace") ? true : false;
				
				if(replaceQuery == null){
					replaceQuery = "";
				} 
			
        this.dialogEntryIndex = new Integer(-1);
        this.dialogEntries = new Vector();

        StringBuffer sqlStmt = new StringBuffer("select * from "
                + this.settings.source + " ").append(
                " where (\"" + this.settings.entryApplication + "\" <> \""
                        + this.settings.entrySection + "\" and ").append(
                "       \"" + this.settings.entrySection + "\" <> \""
                        + this.settings.entryName + "\" and ").append(
                "       \"" + this.settings.entrySection + "\" <> '"
                        + this.settings.entrySchemaSection + "')");

        if (range == 0) {
            if (!this.settings.application.equals("")) {
                sqlStmt.append(" and \"" + this.settings.entryApplication
                        + "\" ='" + this.settings.application + "'");
            }
            if (!this.settings.section.equals("")) {
                sqlStmt.append(" and \"" + this.settings.entrySection + "\" ='"
                        + this.settings.section + "'");
            }

            if (!query.equals("")) {
            		if(isReplaceQuery){
            			
            		}
            		else{
                	sqlStmt.append(
                	        " and ( %lcase(\"" + this.settings.entryName
                	                + "\") like '%" + query.toLowerCase() + "%'")
                	        .append(
                	                "    or %lcase(\"" + this.settings.entrySection
                	                        + "\") like '%" + query.toLowerCase()
                	                        + "%'").append(
                	                "    or %lcase(\"" + this.settings.entryValue
                	                        + "\") like '%" + query.toLowerCase()
                	                        + "%'").append(
                	                "    or %lcase(\"" + this.settings.entryTitle
                	                        + "\") like '%" + query.toLowerCase()
                	                        + "%' )");
              	}
            }
        } else if (!query.equals("")) {
        		if(isReplaceQuery){
        			
        		}
        		else{
            	sqlStmt.append(" and %lcase(\"" + this.settings.entryName
              	      + "\") like '" + query.toLowerCase() + "%'");
            }
        }
        sqlStmt.append(" order by \"" + this.settings.entryApplication
                + "\",\"" + this.settings.entrySection + "\",\""
                + this.settings.entryName + "\"");

        this.debug(3, "queryEntries: " + sqlStmt.toString());

        try {
        	    if(isReplaceQuery && query.length() > 0){
        	    		this.dialogEntries = new Vector();
    					  Vector dialog_entries = this.connection.getArrayAsVector(sqlStmt.toString());
    					  
    					  int j = 0;
    					  for(int i=0;i< dialog_entries.size();i++){
    						HashMap hm = new HashMap();
  			                hm = (HashMap)dialog_entries.get(i);
  			                String value = hm.get("value").toString();
  			                
  			                Pattern p = Pattern.compile(query);
  			                Matcher matcher = p.matcher(value);
  			                if (matcher.find()){
  			                	if(replaceQuery.length()>0){
  			                		hm.put("value",value.replaceAll(query,replaceQuery));
  			                	}
  			                	this.dialogEntries.add(j,hm);
  			                	j++;
  			                }
    					  } 
    					}
    					else{
            		this.dialogEntries = this.connection.getArrayAsVector(sqlStmt.toString());
							}            
            
            
            
            return this.dialogEntries.size();
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            return 0;
        }
    }

    /**
     * Datensatz entfernen
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */
    private boolean recordDeleteKey() throws Exception {

        this.debug(3, "record_delete_key");

        try {
            this.connection.execute("delete from " + this.settings.source
                    + " where \"" + this.settings.entryApplication + "\" = "
                    + this.dbQuoted(this.settings.application) + " and \""
                    + this.settings.entrySection + "\" = "
                    + this.dbQuoted(this.settings.section) + " and \""
                    + this.settings.entryName + "\" = "
                    + this.dbQuoted(this.settings.entry));

            if (this.range.equals("application")) {
                try {
                    this.connection.execute("delete from "
                            + this.settings.source + " where \""
                            + this.settings.entryApplication + "\"="
                            + this.dbQuoted(this.settings.application));
                } catch (Exception e) {
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                }
            } else if (this.range.equals("section")) {
                try {
                    this.connection.execute("delete from "
                            + this.settings.source + " where \""
                            + this.settings.entrySection + "\" = "
                            + this.dbQuoted(this.settings.section) + " and \""
                            + this.settings.entryApplication + "\" = "
                            + this.dbQuoted(this.settings.application));
                } catch (Exception e) {
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                }
            } else {
                if (this.applicationType > 0) {
                    try {
                        Vector results = this.connection
                                .getArrayAsVector("select \"SECTION\" from "
                                        + this.settings.source
                                        + " where \""
                                        + this.settings.entryApplication
                                        + "\"= "
                                        + this
                                                .dbQuoted(this.settings.application)
                                        + " and \""
                                        + this.settings.entrySection
                                        + "\" <> '"
                                        + this.settings.entrySchemaSection
                                        + "' and \"" + this.settings.entryName
                                        + "\" = \""
                                        + this.settings.entrySection
                                        + "\" and \"" + this.settings.entryName
                                        + "\" <> \""
                                        + this.settings.entryApplication + "\"");
                        for (Enumeration el = results.elements(); el
                                .hasMoreElements();) {
                            HashMap result = (HashMap) el.nextElement();
                            try {
                                this.connection
                                        .execute("delete from "
                                                + this.settings.source
                                                + " where \""
                                                + this.settings.entryApplication
                                                + "\" = "
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and \""
                                                + this.settings.entrySection
                                                + "\" = "
                                                + this.dbQuoted(result.get(
                                                        "section").toString())
                                                + " and \""
                                                + this.settings.entryName
                                                + "\" = "
                                                + this
                                                        .dbQuoted(this.settings.entry));
                            } catch (Exception e) {
                                //this.setError(e.getMessage(),SOSClassUtil.getMethodName());
                                break;
                            }
                        }
                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }
                }
            }

        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

        if (this.error()) {
            this.connection.rollback();
            return false;
        } else {
            this.connection.commit();
        }

        return true;
    }

    /**
     * Record vorbereiten
     *  
     */
    private void createRecord() throws Exception {
        this.debug(3, "createRecord");

        this.record = new HashMap();

        Date date = new Date();

        this.record.put(this.settings.entryApplication.toLowerCase(),
                this.settings.application);
        this.record.put(this.settings.entrySection.toLowerCase(),
                this.settings.section);
        this.record.put(this.settings.entryName.toLowerCase(),
                this.settings.entry);

        this.record.put("value", "");
        this.record.put("default_value", "");
        this.record.put("documentation", "");
        this.record.put("long_value", "");
        this.record.put("title", "");
        this.record.put("input_type", "");
        this.record.put("input_size", "");
        this.record.put("display_type", "");
        this.record.put("display_size", "");
        this.record.put("forced", "");
        this.record.put("created", this.dateFormat.format(date));
        this.record.put("created_by", this.settings.author);
        this.record.put("modified", this.dateFormat.format(date));
        this.record.put("modified_by", this.settings.author);
        this.record.put("entry_type", "");
    }

    
    /**
     * Datensatz einfügen
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */

    private boolean recordInsert() throws Exception {

        this.debug(3, "record_insert");

				// document binary
   	 		boolean enableUploadFile = false;
   
				byte[] backUpLongValue   = null;
				
				
        this.createRecord();

        Iterator it = this.record.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String param = entry.getKey().toString();
            String value = entry.getValue().toString();

            if (this.getRequestValue(param) != null) {
                if (param.equals("application") || param.equals("section")
                        || param.equals("name")) {
                    if (this.getIgnoreCase()) {
                        this.record.put(param, this.getRequestValue(param)
                                .trim());
                    } else {
                        this.record.put(param, this.getRequestValue(param)
                                .trim().toLowerCase());
                    }
                } else {
                    this.record.put(param, this.getRequestValue(param)
                            .trim());

                }
            }
        }

				boolean isSaveAs = false; 
    		this.record.put("original_name","");
    		String origName = this.getRequestValue("original_name");
    		if(origName != null && origName.trim().length() > 0){
      		this.record.put("original_name",origName.trim()); 
      		isSaveAs = true; 
    		}


        HashMap backupRecord = (HashMap)this.record.clone();

        int entryType = 0;
        try {
            entryType = Integer.parseInt(this.record.get("entry_type")
                    .toString());
        } catch (Exception e) {
            this.record.put("entry_type", "0");
        }

        try {
            Integer.parseInt(this.record.get("input_type").toString());
        } catch (Exception e) {
            this.record.put("input_type", "0");
        }

        try {
            if (Integer.parseInt(this.record.get("input_size").toString()) <= 0) {
                this.record.put("input_size", this.defaultInputSize);
            }
        } catch (Exception e) {
            this.record.put("input_size", this.defaultInputSize);
        }

        try {
            Integer.parseInt(this.record.get("display_type").toString());
        } catch (Exception e) {
            this.record.put("display_type", "0");
        }

        try {
            if (Integer.parseInt(this.record.get("display_size").toString()) <= 0) {
                this.record.put("display_size", this.defaultDisplaySize);
            }
        } catch (Exception e) {
            this.record.put("display_size", this.defaultDisplaySize);
        }

        StringBuffer sqlStmt = null;
        if (this.range.equalsIgnoreCase("application")) {
            this.record.put(this.settings.entryApplication.toLowerCase(),
                    this.record.get("name").toString());
            this.record.put(this.settings.entrySection.toLowerCase(),
                    this.record.get("name").toString());

            try {
                int forced = Integer.parseInt(this.record.get("forced")
                        .toString());
                if (forced != 1 && forced != -1) {
                    this.record.put("forced", "0");
                }
            } catch (Exception e) {
                this.record.put("forced", "0");
            }

            boolean isInserted = false;
           
           /* 
           !!!!!!!!!!!schema ist nicht berücksichtigt 
            try{
            	String count = this.connection.getSingleValue("select count(\"APPLICATION\") from "+this.settings.source+" where \"APPLICATION\" = "+this.dbQuoted(this.record.get("name").toString())+" and \"SECTION\" = \"APPLICATION\" and \"NAME\" = \"APPLICATION\"");
            	if(!count.equals("0")){
            		this.setError(this.rb.getMessage("sos.settings.dialog.err_available_application"), SOSClassUtil.getMethodName());
            		return false;
            	}
            }
            catch(Exception e){
            	this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            	return false;
            }
            */

            if (entryType == 1) {
                sqlStmt = new StringBuffer(
                        " insert into "
                                + this.settings.source
                                + "(\"APPLICATION\", \"SECTION\", \"NAME\", \"VALUE\", \"TITLE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\", \"CREATED\", \"CREATED_BY\", \"MODIFIED\", \"MODIFIED_BY\") ")
                        .append(" values ("
                                + this.dbQuoted(this.record.get("name")
                                        .toString()) + ",'"
                                + this.settings.entrySchemaSection + "','"
                                + this.settings.entrySchemaSection + "',0,'"
                                + this.dialogSectionsSchemaTitle
                                + "', 1, 0, 0, 10, 0, 0, %now, '"
                                + this.settings.author + "', %now, '"
                                + this.settings.author + "')");

                try {
                    this.connection.execute(sqlStmt.toString());
                    isInserted = true;
                } catch (Exception e) {
                    //break;
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                }

            }

            if (!this.error()) {

                if (!this.settings.application.equals("")) {
                    sqlStmt = new StringBuffer(
                            "select \"SECTION\",\"NAME\", \"VALUE\", \"TITLE\", \"DEFAULT_VALUE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\" from "
                                    + this.settings.source + " ")
                            .append(" where \""
                                    + this.settings.entryApplication + "\" = "
                                    + this.dbQuoted(this.settings.application));

                    try {
                        Vector results = this.connection
                                .getArrayAsVector(sqlStmt.toString());
                        for (Enumeration el = results.elements(); el
                                .hasMoreElements();) {
                            HashMap result = (HashMap) el.nextElement();

                            if (isInserted
                                    && result.get("section").toString().equals(
                                            this.settings.entrySchemaSection)
                                    && result.get("name").toString().equals(
                                            this.settings.entrySchemaSection)) {
                                continue;
                            }

                            StringBuffer sql = new StringBuffer(
                                    "insert into "
                                            + this.settings.source
                                            + " (\"APPLICATION\", \"SECTION\", \"NAME\", \"VALUE\", \"TITLE\", \"DEFAULT_VALUE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\", \"CREATED\", \"CREATED_BY\", \"MODIFIED\", \"MODIFIED_BY\") ");

                            String i_s = "";
                            String e_v = "";
                            String d_v = "";

                            if (result.get("input_size").toString().equals("")) {
                                i_s = "NULL";
                            } else {
                                i_s = result.get("input_size").toString();
                            }

                            if (result.get("value").toString().equals("")) {
                                e_v = "NULL";
                            } else {
                                e_v = this.dbQuoted(result.get("value")
                                        .toString());
                            }

                            if (result.get("default_value").toString().equals(
                                    "")) {
                                d_v = "NULL";
                            } else {
                                d_v = this.dbQuoted(result.get("default_value")
                                        .toString());
                            }

                            sql.append(
                                    "values ("
                                            + this.dbQuoted(this.record.get(
                                                    "name").toString())
                                            + ","
                                            + this.dbQuoted(result.get(
                                                    "section").toString())
                                            + ", "
                                            + this.dbQuoted(result.get("name")
                                                    .toString())
                                            + ", "
                                            + e_v
                                            + ", "
                                            + this.dbQuoted(result.get("title")
                                                    .toString())
                                            + ", "
                                            + d_v
                                            + ", "
                                            + result.get("input_type")
                                                    .toString() + ", " + i_s
                                            + ", ").append(
                                    result.get("display_type").toString()
                                            + ", "
                                            + result.get("display_size")
                                                    .toString()
                                            + ", "
                                            + result.get("forced").toString()
                                            + ", "
                                            + result.get("entry_type")
                                                    .toString() + ", %now, '"
                                            + this.settings.author
                                            + "', %now, '"
                                            + this.settings.author + "')");

                            try {
                                this.connection.execute(sql.toString());

                                byte[] longValue = this.connection
                                        .getBlob("select \"LONG_VALUE\" from "
                                                + this.settings.source
                                                + " where \""
                                                + this.settings.entryApplication
                                                + "\" = "
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and \""
                                                + this.settings.entrySection
                                                + "\" = "
                                                + this.dbQuoted(result.get(
                                                        "section").toString())
                                                + " and \""
                                                + this.settings.entryName
                                                + "\" = "
                                                + this.dbQuoted(result.get(
                                                        "name").toString())
                                                + " ");
                                if (longValue != null && longValue.length != 0) {
                                    this.connection
                                            .updateBlob(
                                                    this.settings.source,
                                                    "LONG_VALUE",
                                                    longValue,
                                                    " \""
                                                            + this.settings.entryApplication
                                                            + "\" = "
                                                            + this
                                                                    .dbQuoted(this.record
                                                                            .get(
                                                                                    "name")
                                                                            .toString())
                                                            + " and \""
                                                            + this.settings.entrySection
                                                            + "\" = "
                                                            + this
                                                                    .dbQuoted(result
                                                                            .get(
                                                                                    "section")
                                                                            .toString())
                                                            + " and \""
                                                            + this.settings.entryName
                                                            + "\" = "
                                                            + this
                                                                    .dbQuoted(result
                                                                            .get(
                                                                                    "name")
                                                                            .toString())
                                                            + " ");

                                }

                                byte[] documentation = this.connection
                                        .getBlob("select \"DOCUMENTATION\" from "
                                                + this.settings.source
                                                + " where \""
                                                + this.settings.entryApplication
                                                + "\" = "
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and \""
                                                + this.settings.entrySection
                                                + "\" = "
                                                + this.dbQuoted(result.get(
                                                        "section").toString())
                                                + " and \""
                                                + this.settings.entryName
                                                + "\" = "
                                                + this.dbQuoted(result.get(
                                                        "name").toString())
                                                + " ");
                                if (documentation != null
                                        && documentation.length != 0) {
                                    this.connection
                                            .updateBlob(
                                                    this.settings.source,
                                                    "DOCUMENTATION",
                                                    documentation,
                                                    " \""
                                                            + this.settings.entryApplication
                                                            + "\" = "
                                                            + this
                                                                    .dbQuoted(this.record
                                                                            .get(
                                                                                    "name")
                                                                            .toString())
                                                            + " and \""
                                                            + this.settings.entrySection
                                                            + "\" = "
                                                            + this
                                                                    .dbQuoted(result
                                                                            .get(
                                                                                    "section")
                                                                            .toString())
                                                            + " and \""
                                                            + this.settings.entryName
                                                            + "\" = "
                                                            + this
                                                                    .dbQuoted(result
                                                                            .get(
                                                                                    "name")
                                                                            .toString())
                                                            + " ");
                                }

                            } catch (Exception e) {
                                this.setError(e.getMessage(), SOSClassUtil
                                        .getMethodName());
                            }

                        }// Enumeration

                        String sqlDel = " delete from "
                                + this.settings.source
                                + " where \""
                                + this.settings.entryApplication
                                + "\" = "
                                + this.dbQuoted(this.record.get("name")
                                        .toString()) + " and \""
                                + this.settings.entrySection + "\" = "
                                + this.dbQuoted(this.settings.application)
                                + " and \"" + this.settings.entryName + "\" = "
                                + this.dbQuoted(this.settings.application);
                        try {
                            this.connection.execute(sqlDel);
                        } catch (Exception e) {
                            this.setError(e.getMessage(), SOSClassUtil
                                    .getMethodName());

                        }

                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }

                }
            }
        } else if (this.range.equalsIgnoreCase("section")) {
            this.record.put(this.settings.entryApplication.toLowerCase(),
                    this.settings.application);
            this.record.put(this.settings.entrySection.toLowerCase(),
                    this.record.get("name").toString());

            try {
                int forced = Integer.parseInt(this.record.get("forced")
                        .toString());
                if (forced != 1 && forced != -1) {
                    this.record.put("forced", "0");
                }
            } catch (Exception e) {
                this.record.put("forced", "0");
            }

            if (entryType == 1) {
                sqlStmt = new StringBuffer(
                        " insert into "
                                + this.settings.source
                                + " (\"APPLICATION\", \"SECTION\", \"NAME\", \"VALUE\", \"TITLE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\", \"CREATED\", \"CREATED_BY\", \"MODIFIED\", \"MODIFIED_BY\") ")
                        .append(" values ('"
                                + this.settings.entryCounterApplication
                                + "','"
                                + this.settings.entryCounterSection
                                + "','"
                                + (this.settings.source + "." + this.record
                                        .get("name").toString()).toLowerCase()
                                + "',0,'" + this.dialogSectionsCounterTitle
                                + " " + this.record.get("name").toString()
                                + "', 1, 0, 0, 10, 0, 0, %now, '"
                                + this.settings.author + "', %now, '"
                                + this.settings.author + "')");

                try {
                    this.connection.execute(sqlStmt.toString());
                } catch (Exception e) {
                    //break;
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                }

            }

            if (!this.error()) {

                String entryValue = "value";
                boolean ok = (!this.settings.section.equals("")); // ..
                // Speichern
                // unter

                if (!ok) {
                    ok = (this.applicationType > 0); // Übernahme aus einer
                    // Schema-Sektion
                    if (ok) {
                        this.settings.section = this.settings.entrySchemaSection;
                        entryValue = "default_value";
                    }
                }
                if (ok) {
                    sqlStmt = new StringBuffer(
                            " select \"NAME\", \"VALUE\", \"TITLE\", \"DEFAULT_VALUE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\" from "
                                    + this.settings.source + " ")
                            .append(" where \""
                                    + this.settings.entryApplication + "\"="
                                    + this.dbQuoted(this.settings.application)
                                    + " and \"" + this.settings.entrySection
                                    + "\"="
                                    + this.dbQuoted(this.settings.section)
                                    + " and \"" + this.settings.entrySection
                                    + "\"<>\"" + this.settings.entryName + "\"");

                    try {
                        Vector results = this.connection
                                .getArrayAsVector(sqlStmt.toString());

                        if (results.size() > 0) {
                            for (Enumeration el = results.elements(); el
                                    .hasMoreElements();) {
                                HashMap result = (HashMap) el.nextElement();

                                sqlStmt = new StringBuffer(
                                        " insert into "
                                                + this.settings.source
                                                + " (\"APPLICATION\", \"SECTION\", \"NAME\", \"VALUE\", \"TITLE\", \"DEFAULT_VALUE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\", \"CREATED\", \"CREATED_BY\", \"MODIFIED\", \"MODIFIED_BY\") ");

                                String i_s = "";
                                String e_v = "";
                                String d_v = "";

                                if (result.get("input_size").toString().equals(
                                        "")) {
                                    i_s = "NULL";
                                } else {
                                    i_s = result.get("input_size").toString();
                                }

                                if (result.get(entryValue).toString()
                                        .equals("")) {
                                    e_v = "NULL";
                                } else {
                                    e_v = this.dbQuoted(result.get(entryValue)
                                            .toString());
                                }

                                if (result.get("default_value").toString()
                                        .equals("")) {
                                    d_v = "NULL";
                                } else {
                                    d_v = this.dbQuoted(result.get(
                                            "default_value").toString());
                                }

                                String db_application = (this.getIgnoreCase()) ? this.settings.application
                                        : this.settings.application
                                                .toLowerCase();
                                String db_name = (this.getIgnoreCase()) ? result
                                        .get("name").toString()
                                        : result.get("name").toString()
                                                .toLowerCase();

                                sqlStmt.append(
                                        " values ("
                                                + this.dbQuoted(db_application)
                                                + ","
                                                + this
                                                        .dbQuoted(this.record
                                                                .get("name")
                                                                .toString())
                                                + ","
                                                + this.dbQuoted(db_name)
                                                + ", "
                                                + e_v
                                                + ", "
                                                + this.dbQuoted(result.get(
                                                        "title").toString())
                                                + ","
                                                + d_v
                                                + ","
                                                + result.get("input_type")
                                                        .toString() + "," + i_s
                                                + ", ").append(
                                        result.get("display_type").toString()
                                                + ", "
                                                + result.get("display_size")
                                                        .toString()
                                                + ", "
                                                + result.get("forced")
                                                        .toString()
                                                + ", "
                                                + result.get("entry_type")
                                                        .toString()
                                                + ", %now, '"
                                                + this.settings.author
                                                + "', %now, '"
                                                + this.settings.author + "')");
                                try {
                                    this.connection.execute(sqlStmt.toString());

                                    byte[] longValue = this.connection
                                            .getBlob("select \"LONG_VALUE\" from "
                                                    + this.settings.source
                                                    + " where \""
                                                    + this.settings.entryApplication
                                                    + "\" = "
                                                    + this
                                                            .dbQuoted(this.settings.application)
                                                    + " and \""
                                                    + this.settings.entrySection
                                                    + "\" = "
                                                    + this
                                                            .dbQuoted(this.settings.section)
                                                    + " and \""
                                                    + this.settings.entryName
                                                    + "\" = "
                                                    + this.dbQuoted(result.get(
                                                            "name").toString()));
                                    if (longValue != null
                                            && longValue.length != 0) {
                                        this.connection
                                                .updateBlob(
                                                        this.settings.source,
                                                        "LONG_VALUE",
                                                        longValue,
                                                        " \""
                                                                + this.settings.entryApplication
                                                                + "\" = "
                                                                + this
                                                                        .dbQuoted(this.settings.application)
                                                                + " and \""
                                                                + this.settings.entrySection
                                                                + "\" = "
                                                                + this
                                                                        .dbQuoted(this.record
                                                                                .get(
                                                                                        "name")
                                                                                .toString())
                                                                + " and \""
                                                                + this.settings.entryName
                                                                + "\" = "
                                                                + this
                                                                        .dbQuoted(result
                                                                                .get(
                                                                                        "name")
                                                                                .toString()));
                                    }

                                    byte[] documentation = this.connection
                                            .getBlob("select \"DOCUMENTATION\" from "
                                                    + this.settings.source
                                                    + " where \""
                                                    + this.settings.entryApplication
                                                    + "\" = "
                                                    + this
                                                            .dbQuoted(this.settings.application)
                                                    + " and \""
                                                    + this.settings.entrySection
                                                    + "\" = "
                                                    + this
                                                            .dbQuoted(this.settings.section)
                                                    + " and \""
                                                    + this.settings.entryName
                                                    + "\" = "
                                                    + this.dbQuoted(result.get(
                                                            "name").toString()));
                                    if (documentation != null
                                            && documentation.length != 0) {
                                        this.connection
                                                .updateBlob(
                                                        this.settings.source,
                                                        "DOCUMENTATION",
                                                        documentation,
                                                        " \""
                                                                + this.settings.entryApplication
                                                                + "\" = "
                                                                + this
                                                                        .dbQuoted(this.settings.application)
                                                                + " and \""
                                                                + this.settings.entrySection
                                                                + "\" = "
                                                                + this
                                                                        .dbQuoted(this.record
                                                                                .get(
                                                                                        "name")
                                                                                .toString())
                                                                + " and \""
                                                                + this.settings.entryName
                                                                + "\" = "
                                                                + this
                                                                        .dbQuoted(result
                                                                                .get(
                                                                                        "name")
                                                                                .toString()));
                                    }

                                } catch (Exception e) {
                                    //break;
                                    this.setError(e.getMessage(), SOSClassUtil
                                            .getMethodName());
                                }

                            }
                        }

                    } catch (Exception e) {
                        //break;
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }

                }
            }

        } else {
            this.record.put(this.settings.entryApplication.toLowerCase(),
                    this.settings.application);
            this.record.put(this.settings.entrySection.toLowerCase(),
                    this.settings.section);

						String inputType 		= this.record.get("input_type").toString();
						String displayType 	= this.record.get("display_type").toString();
						 
						if(inputType.equals("5")){// Dokument
							if(displayType.equals("4")){// Vestreckt . Behandlung wie bei case 6
						  	this.record.put("input_size","");
        			 	this.record.put("display_type","4"); // Hidden
        			 	this.record.put("value","");
        			 
        			 	if(this.item.equals("upload")){
        			  	if(this.inputImport != null && this.inputImport.length() > 0 && this.importOriginalFileName != null && this.importOriginalFileName.length()>0){
        			    	backupRecord.put("long_value",this.inputImport);
        			     	//this.record.put("default_value",this.importOriginalFileName);
        			     	enableUploadFile = true;
        			   	}
        			   	else{
        			    	return false;
        			   	}
        			 	}
        			}                  
        			else{// Textarea             
        				this.record.put("input_size","");
        			  this.record.put("display_type","3"); // Textarea
        			  if(this.record.get("value").toString().trim().length() > 0){
        			  	if(this.record.get("name").toString().trim().length() > 0){
        			    	backupRecord.put("long_value",this.record.get("value").toString());
        			    }
        			   }
        				this.record.put("value","");
        			}
        			
        		}
        		else if(inputType.equals("6")){// Dokument binär
        			this.record.put("input_size","");
        			this.record.put("display_type","4"); // Hidden
        			this.record.put("value","");
        			
        			if(this.item.equals("upload")){
        				if(this.inputImport != null && this.inputImport.length() > 0 && this.importOriginalFileName != null && this.importOriginalFileName.length()>0){
        			    backupRecord.put("long_value",this.inputImport);
        				 	//this.record.put("default_value",this.importOriginalFileName);
        				 	enableUploadFile = true;
        				}
        				else{
        					return false;
        				}
        			}
        		}
        		        		
        		if(isSaveAs && !enableUploadFile){
        		  if(inputType.equals("5") || inputType.equals("6")){
        		  	 backUpLongValue = this.connection.getBlob("select \"LONG_VALUE\" from "+this.settings.source+" where \""+this.settings.entryApplication+"\" = "+this.dbQuoted(this.record.get("application").toString())+" and \""+this.settings.entrySection+"\" = "+this.dbQuoted(this.record.get("section").toString())+" and \""+this.settings.entryName+"\" = "+this.dbQuoted(this.record.get("original_name").toString()));
        		     if(backUpLongValue != null && backUpLongValue.length > 0){
        		     		//backupRecord.put("long_value",backUpLongValue);
        				 }
        				 else{
        				 	backupRecord.put("long_value","");
        				 }
        		  }
        		}
	
						//////////////////////////
            if (this.applicationType > 0) {
                try {
                    Vector results = this.connection
                            .getArrayAsVector(" select \"SECTION\" from "
                                    + this.settings.source
                                    + " where \""
                                    + this.settings.entryApplication
                                    + "\" = "
                                    + this.dbQuoted(this.record.get(
                                            "application").toString())
                                    + " and \"" + this.settings.entrySection
                                    + "\" <> '"
                                    + this.settings.entrySchemaSection
                                    + "' and \"" + this.settings.entryName
                                    + "\" = \"" + this.settings.entrySection
                                    + "\" and \"" + this.settings.entryName
                                    + "\" <> \""
                                    + this.settings.entryApplication + "\"");

                    if (results.size() > 0) {
                        for (Enumeration el = results.elements(); el
                                .hasMoreElements();) {
                            HashMap result = (HashMap) el.nextElement();
                            String i_s = "";
                            String d_v = "";
                            String e_v = "";

                            if (this.record.get("input_size").toString()
                                    .equals("")) {
                                i_s = "NULL";
                            } else {
                                i_s = this.record.get("input_size").toString();
                            }

                            if (this.record.get("default_value").toString()
                                    .equals("")) {
                                d_v = "NULL";
                            } else {
                                d_v = this.dbQuoted(this.record.get(
                                        "default_value").toString());
                            }

														String input_type = this.record.get("input_type").toString();
                            if (input_type.equals("5") || input_type.equals("6")) { // dokument oder dokument binär
                                e_v = "NULL";
                            } 
                            else {
                                e_v = d_v;
                            }

                            String db_application = (this.getIgnoreCase()) ? this.record
                                    .get("application").toString()
                                    : this.record.get("application").toString()
                                            .toLowerCase();
                            String db_section = (this.getIgnoreCase()) ? result
                                    .get("section").toString() : result.get(
                                    "section").toString().toLowerCase();
                            String db_name = (this.getIgnoreCase()) ? this.record
                                    .get("name").toString()
                                    : this.record.get("name").toString()
                                            .toLowerCase();

                            sqlStmt = new StringBuffer(
                                    " insert into "
                                            + this.settings.source
                                            + " (\"APPLICATION\", \"SECTION\", \"NAME\", \"VALUE\", \"TITLE\", \"DEFAULT_VALUE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\", \"CREATED\", \"CREATED_BY\", \"MODIFIED\", \"MODIFIED_BY\") ")
                                    .append(" values ( "
                                            + this.dbQuoted(db_application)
                                            + ","
                                            + this.dbQuoted(db_section)
                                            + ", "
                                            + this.dbQuoted(db_name)
                                            + ","
                                            + e_v
                                            + ", "
                                            + this.dbQuoted(this.record.get(
                                                    "title").toString())
                                            + ", "
                                            + d_v
                                            + ","
                                            + this.record.get("input_type")
                                                    .toString()
                                            + ", "
                                            + i_s
                                            + ", "
                                            + this.record.get("display_type")
                                                    .toString()
                                            + ", "
                                            + this.record.get("display_size")
                                                    .toString()
                                            + ","
                                            + this.record.get("forced")
                                                    .toString()
                                            + ", "
                                            + this.record.get("entry_type")
                                                    .toString() + ", %now, '"
                                            + this.settings.author
                                            + "', %now, '"
                                            + this.settings.author + "')");

                            try {
                                this.connection.execute(sqlStmt.toString());
                            } catch (Exception e) {
                                //break;
                                this.setError(e.getMessage(), SOSClassUtil
                                        .getMethodName());
                            }

                        }
                    }
                } catch (Exception e) {
                    //break;
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                }
            }

            if (!this.error()) {

                if (this.sectionType > 0) {
                    try {
                        int sequence = this.settings.getSequence(
                        				this.settings.entryCounterApplication,
                        				this.settings.entryCounterSection,
                        				(this.settings.source + "." + this.settings.section).toLowerCase()
                                        );
                        this.record.put(this.settings.entryName.toLowerCase(),
                                new Integer(sequence));
                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }

                    this.record.put("forced", "0");
                    if (this.error()) { return false; }
                }
            }

        }

        Date date = new Date();
        this.record.put("created", this.dateFormat.format(date));
        this.record.put("created_by", this.settings.author);
        this.record.put("modified", this.dateFormat.format(date));
        this.record.put("modified_by", this.settings.author);

        //backupRecord.put("documentation",this.record.get("documentation").toString());
        //this.record.put("documentation",""); // insert();

        // Kommt aus dem FKC Editor
        if (this.getRequestValue(this.editorName) != null) {
            this.record.put("documentation", "");
            backupRecord.put("documentation", "");

            String editorValue = this.getRequestValue(this.editorName)
                    .trim();
            if (!editorValue.equals("")) {
                // \z - endet damit
                String copy = editorValue.toLowerCase();
                //String pattern = "^(<p>(&nbsp;)+[\\s]*</p>[\\s]*)+\\z";
                String pattern = "^(<p>(&nbsp;)*((<[a-zA-Z]*>)*(</[a-zA-Z]*>)*)*[\\s]*</p>[\\s]*)+\\z";
                Pattern p = Pattern.compile(pattern);
                Matcher matcher = p.matcher(copy);
                if (!matcher.find()) {
                    this.record.put("documentation", editorValue);
                    backupRecord.put("documentation", editorValue);
                }
            }
        }

        if (!this.error()) {

            String value = "";
            String defaultValue = "";
            String inputSize = "";

            if (this.record.get("value").toString().equals("")) {
                value = "NULL";
            } else {
                value = this.dbQuoted(this.record.get("value").toString());
            }

            if (this.record.get("default_value").toString().equals("")) {
                defaultValue = "NULL";
            } else {
                defaultValue = this.dbQuoted(this.record.get("default_value")
                        .toString());
            }

            if (this.record.get("input_size").toString().equals("")) {
                inputSize = "NULL";
            } else {
                inputSize = this.record.get("input_size").toString();
            }

            String db_application = (this.getIgnoreCase()) ? this.record.get(
                    "application").toString() : this.record.get("application")
                    .toString().toLowerCase();
            String db_section = (this.getIgnoreCase()) ? this.record.get(
                    "section").toString() : this.record.get("section")
                    .toString().toLowerCase();
            String db_name = (this.getIgnoreCase()) ? this.record.get("name")
                    .toString() : this.record.get("name").toString()
                    .toLowerCase();

            String created = this.record.get("created").toString();
            if (created.length() > 19) {
                created = created.substring(0, 19);
            }
            String modified = this.record.get("modified").toString();
            if (modified.length() > 19) {
                modified = modified.substring(0, 19);
            }
            sqlStmt = new StringBuffer("insert into " + this.settings.source
                    + " ")
                    .append(
                            "(\"APPLICATION\",\"SECTION\",\"NAME\",\"VALUE\",\"DEFAULT_VALUE\",\"TITLE\",")
                    .append(
                            "\"INPUT_TYPE\",\"INPUT_SIZE\",\"DISPLAY_TYPE\",\"DISPLAY_SIZE\",\"FORCED\",")
                    .append(
                            "\"ENTRY_TYPE\",\"CREATED\",\"CREATED_BY\",\"MODIFIED\",\"MODIFIED_BY\")")
                    .append(
                            " values("
                                    + this.dbQuoted(db_application)
                                    + ","
                                    + this.dbQuoted(db_section)
                                    + ","
                                    + this.dbQuoted(db_name)
                                    + ","
                                    + value
                                    + ","
                                    + defaultValue
                                    + ","
                                    + this.dbQuoted(this.record.get("title")
                                            .toString()) + ",").append(
                            this.record.get("input_type").toString()
                                    + ","
                                    + inputSize
                                    + ","
                                    + this.record.get("display_type")
                                            .toString()
                                    + ","
                                    + this.record.get("display_size")
                                            .toString() + ","
                                    + this.record.get("forced").toString()
                                    + ",").append(
                            this.record.get("entry_type").toString()
                                    + ",%timestamp_iso('" + created + "'),'"
                                    + this.record.get("created_by").toString()
                                    + "',%timestamp_iso('" + modified + "'),'"
                                    + this.record.get("modified_by").toString()
                                    + "')");

            try {
                this.connection.execute(sqlStmt.toString());
                String documentation = backupRecord.get("documentation").toString();
                
                byte[] longValue = null;
                if(backUpLongValue != null && backUpLongValue.length > 0 ){
                	longValue = backUpLongValue;
								}
								else{
									longValue = backupRecord.get("long_value").toString().getBytes();
								}
								
                if (!documentation.trim().equals("")) {
                    try {
                        this.connection.updateBlob(this.settings.source,
                                "DOCUMENTATION", documentation.getBytes(),
                                "\"APPLICATION\"="
                                        + this.dbQuoted(this.record.get(
                                                "application").toString())
                                        + " and \"SECTION\" = "
                                        + this.dbQuoted(this.record.get(
                                                "section").toString())
                                        + " and \"NAME\" = "
                                        + this.dbQuoted(this.record.get("name")
                                                .toString()));
                        //this.record.put("documentation",documentation);
                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }
                }

                if (longValue != null && longValue.length>0) {
                    try {
                    	if(enableUploadFile){ // document binary
            						//this.connection.update_blob_from_file($this->source,'"LONG_VALUE"',$backup_record['long_value'],'where "'.$this->entry_application.'"=\''.$this->db->str_quoted($this->record['application']).'\' and "'.$this->entry_section.'" = \''.$this->db->str_quoted($this->record['section']).'\' and "'.$this->entry_name.'" = \''.$this->db->str_quoted($this->record['name']).'\'');
          					//update from file		
                    	    this.connection.updateBlob(this.settings.source,"LONG_VALUE",backupRecord.get("long_value").toString(),"\"APPLICATION\"="+this.dbQuoted(this.record.get("application").toString())+" and \"SECTION\" = "+this.dbQuoted(this.record.get("section").toString())+ " and \"NAME\" = " + this.dbQuoted(this.record.get("name").toString()));
          						}
          						else{ // document
                        
                        
                        this.connection.updateBlob(this.settings.source,
                                "LONG_VALUE", longValue,
                                "\"APPLICATION\"="
                                        + this.dbQuoted(this.record.get(
                                                "application").toString())
                                        + " and \"SECTION\" = "
                                        + this.dbQuoted(this.record.get(
                                                "section").toString())
                                        + " and \"NAME\" = "
                                        + this.dbQuoted(this.record.get("name")
                                                .toString()));
                                                
                       }                      
                                                
                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }
                }

            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }

            if (this.error()) {
                this.connection.rollback();
                this.record = backupRecord;
                return false;
            } else {
                this.connection.commit();
            }
        } else {
            return false;
        }
				
				this.msg = this.rb.getMessage("sos.settings.dialog.msg_store");
				
        return true;
    }

    /**
     * Datensatz speichern
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */

    private boolean recordStore() throws Exception {
        this.debug(3, "recordStore");

        StringBuffer sqlStmt = null;

        sqlStmt = new StringBuffer("select ")
                .append(
                        "\"APPLICATION\",\"SECTION\",\"NAME\",\"VALUE\",\"DEFAULT_VALUE\",\"TITLE\",")
                .append(
                        "\"INPUT_TYPE\",\"INPUT_SIZE\",\"DISPLAY_TYPE\",\"DISPLAY_SIZE\",\"FORCED\",")
                .append(
                        "\"ENTRY_TYPE\",\"CREATED\",\"CREATED_BY\",\"MODIFIED\",\"MODIFIED_BY\" ")
                .append(" from " + this.settings.source + " ").append(
                        " where \"" + this.settings.entryApplication + "\"  = "
                                + this.dbQuoted(this.settings.application)
                                + " and ").append(
                        "       \"" + this.settings.entrySection + "\"      = "
                                + this.dbQuoted(this.settings.section)
                                + " and ").append(
                        "       \"" + this.settings.entryName + "\"         = "
                                + this.dbQuoted(this.settings.entry));

        try {
            this.record = this.connection.getSingle(sqlStmt.toString());

            sqlStmt = new StringBuffer("select \"DOCUMENTATION\"").append(
                    " from " + this.settings.source + " ").append(
                    " where \"" + this.settings.entryApplication + "\"  = "
                            + this.dbQuoted(this.settings.application)
                            + " and ").append(
                    "       \"" + this.settings.entrySection + "\"      = "
                            + this.dbQuoted(this.settings.section) + " and ")
                    .append(
                            "       \"" + this.settings.entryName
                                    + "\"         = "
                                    + this.dbQuoted(this.settings.entry));

            try {
                byte[] documentation = this.connection.getBlob(sqlStmt
                        .toString());
                if (documentation != null) {
                    this.record.put("documentation", new String(documentation));
                } else {
                    this.record.put("documentation", "");
                }
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }

        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

        if (this.error()) { return false; }

        Iterator it = this.record.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String param = entry.getKey().toString();
            String value = entry.getValue().toString();
            if (this.getRequestValue(param) != null) {
                if (param.equals("application") || param.equals("section")
                        || param.equals("name")) {

                    if (this.getIgnoreCase()) {
                        this.record.put(param, this.getRequestValue(param)
                                .trim());

                    } else {
                        this.record.put(param, this.getRequestValue(param)
                                .trim().toLowerCase());
                    }
                } else {
                    this.record.put(param, this.getRequestValue(param)
                            .trim());

                }
            }
        }

        // Kommt aus dem FKC Editor
        if (this.getRequestValue(this.editorName) != null) {
            this.record.put("documentation", "");
            String editorValue = this.getRequestValue(this.editorName)
                    .trim();
            if (!editorValue.equals("")) {
                // \z - endet damit
                String copy = editorValue.toLowerCase();
                //String pattern = "^(<p>(&nbsp;)+[\\s]*</p>[\\s]*)+\\z";
                String pattern = "^(<p>(&nbsp;)*((<[a-zA-Z]*>)*(</[a-zA-Z]*>)*)*[\\s]*</p>[\\s]*)+\\z";
                Pattern p = Pattern.compile(pattern);
                Matcher matcher = p.matcher(copy);
                if (!matcher.find()) {
                    this.record.put("documentation", editorValue);
                }
            }
        }

        HashMap backupRecord = (HashMap)this.record.clone();

        int entryType = 0;
        try {
            entryType = Integer.parseInt(this.record.get("entry_type")
                    .toString());
        } catch (Exception e) {
            this.record.put("entry_type", "0");
        }

        try {
            Integer.parseInt(this.record.get("input_type").toString());
        } catch (Exception e) {
            this.record.put("input_type", "0");
        }

        try {
            if (Integer.parseInt(this.record.get("input_size").toString()) <= 0) {
                this.record.put("input_size", this.defaultInputSize);
            }
        } catch (Exception e) {
            this.record.put("input_size", this.defaultInputSize);
        }

        try {
            Integer.parseInt(this.record.get("display_type").toString());
        } catch (Exception e) {
            this.record.put("display_type", "0");
        }

        try {
            if (Integer.parseInt(this.record.get("display_size").toString()) <= 0) {
                this.record.put("display_size", this.defaultDisplaySize);
            }
        } catch (Exception e) {
            this.record.put("display_size", this.defaultDisplaySize);
        }

        boolean isDocumentationUpdated 	= false;
        boolean emptyLongValue 					= false;

        if (this.range.equalsIgnoreCase("application")) {

            this.record.put(this.settings.entryApplication.toLowerCase(),
                    this.record.get("name").toString());
            this.record.put(this.settings.entrySection.toLowerCase(),
                    this.record.get("name").toString());

            try {
                int forced = Integer.parseInt(this.record.get("forced")
                        .toString());
                if (forced != 1 && forced != -1) {
                    this.record.put("forced", "0");
                }
            } catch (Exception e) {
                this.record.put("forced", "0");
            }

            String recordDocumentation = this.record.get("documentation")
                    .toString();

            backupRecord.put("documentation", "");

            if (!recordDocumentation.trim().equals("")) {
                if (!this.record.get("name").toString().equals("")) {
                    try {
                        this.connection
                                .updateBlob(
                                        this.settings.source,
                                        "DOCUMENTATION",
                                        recordDocumentation.getBytes(),
                                        "\"APPLICATION\"="
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and \"SECTION\" = "
                                                + this
                                                        .dbQuoted(this.settings.section)
                                                + " and \"NAME\" = "
                                                + this
                                                        .dbQuoted(this.record
                                                                .get("name")
                                                                .toString()));
                        backupRecord.put("documentation", recordDocumentation);
                        //this.record.remove("documentation"); // wegen BLOB
                        // beim this->db->update($this->record)
                        isDocumentationUpdated = true;
                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }

                }
            }

            if (!this.error()) {

                if (this.record.get("entry_type").toString().equals("1")) {
                    try {
                        String hasSchema = this.connection
                                .getSingleValue("select count(*) from "
                                        + this.settings.source
                                        + " where \""
                                        + this.settings.entryApplication
                                        + "\" = "
                                        + this
                                                .dbQuoted(this.settings.application)
                                        + " and \""
                                        + this.settings.entrySection + "\" = '"
                                        + this.settings.entrySchemaSection
                                        + "' and \"" + this.settings.entryName
                                        + "\" = '"
                                        + this.settings.entrySchemaSection
                                        + "'");

                        if (hasSchema.equals("") || hasSchema.equals("0")) {
                            sqlStmt = new StringBuffer(
                                    "insert into "
                                            + this.settings.source
                                            + " (\"APPLICATION\", \"SECTION\", \"NAME\", \"VALUE\", \"TITLE\", \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\", \"CREATED\", \"CREATED_BY\", \"MODIFIED\", \"MODIFIED_BY\") ")
                                    .append(" values ("
                                            + this.dbQuoted(this.record.get(
                                                    "name").toString()) + ",'"
                                            + this.settings.entrySchemaSection
                                            + "','"
                                            + this.settings.entrySchemaSection
                                            + "',0,'"
                                            + this.dialogSectionsSchemaTitle
                                            + "', 1, 0, 0, 10, 0, 0, %now, '"
                                            + this.settings.author
                                            + "', %now, '"
                                            + this.settings.author + "')");

                            try {
                                this.connection.execute(sqlStmt.toString());
                            } catch (Exception e) {
                                // break;
                                this.setError(e.getMessage(), SOSClassUtil
                                        .getMethodName());
                            }
                        }
                    } catch (Exception e) {
                        //break;
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }
                }

                if (!this.error()) {
                    sqlStmt = new StringBuffer("update " + this.settings.source
                            + " set \"" + this.settings.entryApplication
                            + "\" = "
                            + this.dbQuoted(this.record.get("name").toString())
                            + " where \"" + this.settings.entryApplication
                            + "\" = "
                            + this.dbQuoted(this.settings.application));
                }
            }
        } else if (this.range.equalsIgnoreCase("section")) {

            this.record.put(this.settings.entryApplication.toLowerCase(),
                    this.settings.application);
            this.record.put(this.settings.entrySection.toLowerCase(),
                    this.record.get("name").toString());

            try {
                int forced = Integer.parseInt(this.record.get("forced")
                        .toString());
                if (forced != 1 && forced != -1) {
                    this.record.put("forced", "0");
                }
            } catch (Exception e) {
                this.record.put("forced", "0");
            }

            String recordDocumentation = this.record.get("documentation")
                    .toString();

            backupRecord.put("documentation", "");

            if (!recordDocumentation.trim().equals("")) {
                if (!this.record.get("name").toString().equals("")) {
                    try {
                        this.connection
                                .updateBlob(
                                        this.settings.source,
                                        "DOCUMENTATION",
                                        recordDocumentation.getBytes(),
                                        "\"APPLICATION\"="
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and \"SECTION\" = "
                                                + this
                                                        .dbQuoted(this.settings.section)
                                                + " and \"NAME\" = "
                                                + this
                                                        .dbQuoted(this.settings.entry));
                        backupRecord.put("documentation", recordDocumentation);
                        //this.record.remove("documentation"); // wegen BLOB
                        // beim this->db->update($this->record)
                        isDocumentationUpdated = true;
                    } catch (Exception e) {
                        //break();
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }

                }
            }

            if (!this.error()) {

                if (this.record.get("entry_type").toString().equals("1")) {
                    sqlStmt = new StringBuffer("update "
                            + this.settings.source
                            + " set \"INPUT_TYPE\" = "
                            + this.record.get("input_type").toString()
                            + ", \"INPUT_SIZE\" = "
                            + this.record.get("input_size").toString()
                            + ", \"DISPLAY_TYPE\" = "
                            + this.record.get("display_type").toString()
                            + ", \"DISPLAY_SIZE\" = "
                            + this.record.get("display_size").toString()
                            + ", \"FORCED\" = "
                            + this.record.get("forced").toString()
                            + ", \"ENTRY_TYPE\" = "
                            + this.record.get("entry_type").toString()
                            + ", \"DEFAULT_VALUE\" = "
                            + this.dbQuoted(this.record.get("default_value")
                                    .toString())).append(" where \""
                            + this.settings.entryApplication + "\" = "
                            + this.dbQuoted(this.settings.application)
                            + " and \"" + this.settings.entrySection + "\" = "
                            + this.dbQuoted(this.settings.section));

                    try {
                        this.connection.execute(sqlStmt.toString());
                    } catch (Exception e) {
                        // break;
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }
                }

                if (!this.error()) {
                    sqlStmt = new StringBuffer("update " + this.settings.source
                            + " set \"" + this.settings.entrySection + "\" = "
                            + this.dbQuoted(this.record.get("name").toString())
                            + " where \"" + this.settings.entryApplication
                            + "\" = "
                            + this.dbQuoted(this.settings.application)
                            + " and \"" + this.settings.entrySection + "\" = "
                            + this.dbQuoted(this.settings.section));
                }
            }

        }// end section 
        else {
        		
        		if(this.getRequestValue("binary_value") != null){ // checkbox binäre inhalt leeren
          		this.record.put("value","");
        			emptyLongValue 				= true;
        		}

						//////////////////////////////
						String input_type = this.record.get("input_type").toString();
						String display_type = this.record.get("display_type").toString();
						
						if(input_type.equals("5")){ // Dokument
							if(display_type.equals("4")){// Versteckt . Behandlung wie bei input_type = 6
								
								this.record.put("value","");   
             		this.record.put("input_size","");
             		this.record.put("display_type","4"); // Hidden
             		                      
             		if(this.item.equals("upload")){
             		  if(this.inputImport != null && this.inputImport.length() > 0 && this.importOriginalFileName != null && this.importOriginalFileName.length()>0){
        			      //this.record.put("default_value",this.importOriginalFileName);
             		    try{
             		    	this.connection.updateBlob(this.settings.source,"LONG_VALUE",this.inputImport,"\""+this.settings.entryApplication+"\" = "+this.dbQuoted(this.settings.application)+" and \""+this.settings.entrySection+"\" = "+this.dbQuoted(this.settings.section)+" and \""+this.settings.entryName+"\" = "+this.dbQuoted(this.record.get("name").toString()));
             		    }
             		    catch(Exception e){
             		    	this.setError(e.getMessage(), SOSClassUtil.getMethodName());
             		    	return false;	
             		    }
             		  }
             		  else{
             		    return false;
             		  }
             		}
            	}// display_type = 4
							else{ // textarea            
              	this.record.put("input_size","");
              	this.record.put("display_type","3"); // Textarea
              	
              	if(this.record.get("value").toString().trim().length() > 0 ){
              	  if(this.record.get("name").toString().length() > 0){
              	    try{
              	    	this.connection.updateBlob(this.settings.source,"LONG_VALUE",this.record.get("value").toString().getBytes(),"\""+this.settings.entryApplication+"\" = "+this.dbQuoted(this.settings.application)+" and \""+this.settings.entrySection+"\" = "+this.dbQuoted(this.settings.section)+" and \""+this.settings.entryName+"\" = "+this.dbQuoted(this.record.get("name").toString()));
             		    }
              	    catch(Exception e){
              	    	this.setError(e.getMessage(), SOSClassUtil.getMethodName());
              	    	return false;
              	    }
              	    
              	  }
              	}
              	else{
              	  this.record.put("long_value","");
              	  emptyLongValue = true;
              	}
              	this.record.put("value","");
							}
						}// input_type = 5 
						else if(input_type.equals("6")){ // Dokument binär

								this.record.put("value","");   
             		this.record.put("input_size","");
             		this.record.put("display_type","4"); // Hidden
             		                      
             		if(this.item.equals("upload")){
             		  
             		  if(this.inputImport != null && this.inputImport.length() > 0 && this.importOriginalFileName != null && this.importOriginalFileName.length()>0){
        			      //this.record.put("default_value",this.importOriginalFileName);
             		    try{
             		    	this.connection.updateBlob(this.settings.source,"LONG_VALUE",this.inputImport,"\""+this.settings.entryApplication+"\" = "+this.dbQuoted(this.settings.application)+" and \""+this.settings.entrySection+"\" = "+this.dbQuoted(this.settings.section)+" and \""+this.settings.entryName+"\" = "+this.dbQuoted(this.record.get("name").toString()));
             		    }
             		    catch(Exception e){
             		    	this.setError(e.getMessage(), SOSClassUtil.getMethodName());
             		    	return false;	
             		    }
             		  }
             		  else{
             		    return false;
             		  }
             		}
						}// input_type = 6
						else{
							emptyLongValue = true;
						}
						
						/////////////////////////////////////
            sqlStmt = new StringBuffer("");

            this.record.put(this.settings.entryApplication.toLowerCase(),
                    this.settings.application);
            this.record.put(this.settings.entrySection.toLowerCase(),
                    this.settings.section);

            String recordDocumentation = this.record.get("documentation")
                    .toString();
            if (!recordDocumentation.trim().equals("")) {
                if (!this.record.get("name").toString().equals("")) {
                    try {
                        this.connection
                                .updateBlob(
                                        this.settings.source,
                                        "DOCUMENTATION",
                                        recordDocumentation.getBytes(),
                                        "\"APPLICATION\"="
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and \"SECTION\" = "
                                                + this
                                                        .dbQuoted(this.settings.section)
                                                + " and \"NAME\" = "
                                                + this
                                                        .dbQuoted(this.settings.entry));
                        //this.record.remove("documentation"); // wegen BLOB
                        // beim this->db->update($this->record)
                        isDocumentationUpdated = true;
                    } catch (Exception e) {
                        //break();
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }

                }
            }

            if (!this.error()) {

                if (this.applicationType > 0) {
                    try {
                    	
                    		// wir sind nicht direkt in Schema, sondern in Sektions die Schema benutzen
          							if(!this.settings.section.equals(this.settings.entrySchemaSection)){
          								// file_name aus schema
          								if(display_type.equals("4") && (input_type.equals("5") || input_type.equals("6"))){
          									String sql_def_value = "select \"DEFAULT_VALUE\"  from "+this.settings.source+" where \""+this.settings.entryApplication+"\" = "+this.dbQuoted(this.settings.application)+" and \""+this.settings.entrySection+"\" = '"+this.settings.entrySchemaSection+"' and \""+this.settings.entryName+"\" = "+this.dbQuoted(this.record.get("name").toString());
          									String def_value = this.connection.getSingleValue(sql_def_value);
          									this.record.put("default_value",def_value);
          								}
          							}
                    	
                    	
                        Vector results = this.connection
                                .getArrayAsVector(" select \"SECTION\" from "
                                        + this.settings.source
                                        + " where \""
                                        + this.settings.entryApplication
                                        + "\" = "
                                        + this
                                                .dbQuoted(this.settings.application)
                                        + " and \""
                                        + this.settings.entrySection
                                        + "\" <> '"
                                        + this.settings.entrySchemaSection
                                        + "' and \"" + this.settings.entryName
                                        + "\" = \""
                                        + this.settings.entrySection
                                        + "\" and \"" + this.settings.entryName
                                        + "\" <> \""
                                        + this.settings.entryApplication + "\"");

                        if (results.size() > 0) {
                            for (Enumeration el = results.elements(); el
                                    .hasMoreElements();) {
                                HashMap result = (HashMap) el.nextElement();

                                String sqlAdd = "";
                                String i_s = "NULL";
                                String input_size = this.record.get("input_size").toString();
																
            										if(input_type.equals("5") || input_type.equals("6")){ //document & document binary 
            											sqlAdd = ", \"DEFAULT_VALUE\" = "+this.dbQuoted(this.record.get("default_value").toString()); 
            										}
            										else{
            										  sqlAdd = ", \"DEFAULT_VALUE\"= "+this.dbQuoted(this.record.get("default_value").toString())+" "; 
            										  sqlAdd+= ", \"LONG_VALUE\"  = NULL ";                           
            										}
            										
																if (input_size.length() > 0) {
                                	i_s = input_size;
                                }

                                sqlStmt = new StringBuffer(" update "
                                        + this.settings.source
                                        + " set \"NAME\" = "
                                        + this.dbQuoted(this.record.get("name")
                                                .toString())
                                        + ", \"TITLE\" = "
                                        + this.dbQuoted(this.record
                                                .get("title").toString())
                                        + ", \"INPUT_TYPE\" = "
                                        + this.record.get("input_type")
                                                .toString()
                                        + ", \"INPUT_SIZE\" = "
                                        + i_s
                                        + ", \"DISPLAY_TYPE\" = "
                                        + this.record.get("display_type")
                                                .toString()
                                        + ", \"DISPLAY_SIZE\" = "
                                        + this.record.get("display_size")
                                                .toString()
                                        + ", \"FORCED\" = "
                                        + this.record.get("forced").toString()
                                        + ", \"ENTRY_TYPE\" = "
                                        + this.record.get("entry_type").toString()
                                        + " " + sqlAdd + " ")
                                        .append(" where \""
                                                + this.settings.entryApplication
                                                + "\" = "
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and \""
                                                + this.settings.entrySection
                                                + "\" ="
                                                + this.dbQuoted(result.get(
                                                        "section").toString())
                                                + " and \""
                                                + this.settings.entryName
                                                + "\" ="
                                                + this
                                                        .dbQuoted(this.settings.entry));

                                try {
                                    this.connection.execute(sqlStmt.toString());
                                } catch (Exception e) {
                                    //break;
                                    this.setError(e.getMessage(), SOSClassUtil
                                            .getMethodName());
                                }

                            }
                        }
                    } catch (Exception e) {
                        //break;
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }
                }

            }

        }

        Date date = new Date();
        this.record.put("modified", this.dateFormat.format(date));
        this.record.put("modified_by", this.settings.author);

        if (!this.error()) {
            String sqlDocumentation = "";
            String sqlLongValue = "";

            if (isDocumentationUpdated == false) {
                sqlDocumentation = ", \"DOCUMENTATION\" = NULL ";
            }

            if (emptyLongValue == true) {
                sqlLongValue = ", \"LONG_VALUE\" = NULL ";
            }

            String created = this.record.get("created").toString();
            if (created.length() > 19) {
                created = created.substring(0, 19);
            }
            String modified = this.record.get("modified").toString();
            if (modified.length() > 19) {
                modified = modified.substring(0, 19);
            }

            String value = "";
            String defaultValue = "";
            String inputSize = "";

            if (this.record.get("value").toString().equals("")) {
                value = "NULL";
            } else {
                value = this.dbQuoted(this.record.get("value").toString());
            }

            if (this.record.get("default_value").toString().equals("")) {
                defaultValue = "NULL";
            } else {
                defaultValue = this.dbQuoted(this.record.get("default_value")
                        .toString());
            }

            if (this.record.get("input_size").toString().equals("")) {
                inputSize = "NULL";
            } else {
                inputSize = this.record.get("input_size").toString();
            }

            StringBuffer sql = new StringBuffer("update "
                    + this.settings.source + " ").append(
                    " set \"APPLICATION\"   = "
                            + this.dbQuoted(this.record.get("application")
                                    .toString()) + ",").append(
                    "     \"SECTION\"       = "
                            + this.dbQuoted(this.record.get("section")
                                    .toString()) + ",").append(
                    "     \"NAME\"          = "
                            + this.dbQuoted(this.record.get("name").toString())
                            + ",").append(
                    "     \"VALUE\"         = " + value + ",").append(
                    "     \"DEFAULT_VALUE\" = " + defaultValue + ",").append(
                    "     \"TITLE\"         = "
                            + this
                                    .dbQuoted(this.record.get("title")
                                            .toString()) + ",").append(
                    "     \"INPUT_TYPE\"    = "
                            + this.record.get("input_type").toString() + ",")
                    .append("     \"INPUT_SIZE\"    = " + inputSize + ",")
                    .append(
                            "     \"DISPLAY_TYPE\"  = "
                                    + this.record.get("display_type")
                                            .toString() + ",").append(
                            "     \"DISPLAY_SIZE\"  = "
                                    + this.record.get("display_size")
                                            .toString() + ",").append(
                            "     \"FORCED\"        = "
                                    + this.record.get("forced").toString()
                                    + ",").append(
                            "     \"ENTRY_TYPE\"    = "
                                    + this.record.get("entry_type").toString()
                                    + ",").append(
                            "     \"CREATED\"       = %timestamp_iso('"
                                    + created + "'),").append(
                            "     \"CREATED_BY\"    = '"
                                    + this.record.get("created_by").toString()
                                    + "',").append(
                            "     \"MODIFIED\"      = %timestamp_iso('"
                                    + modified + "'),").append(
                            "     \"MODIFIED_BY\"   = '"
                                    + this.record.get("modified_by").toString()
                                    + "' ").append(sqlDocumentation).append(
                            sqlLongValue).append(
                            " where \"" + this.settings.entryApplication
                                    + "\"  = "
                                    + this.dbQuoted(this.settings.application)
                                    + " and ").append(
                            "       \"" + this.settings.entrySection
                                    + "\"      = "
                                    + this.dbQuoted(this.settings.section)
                                    + " and ").append(
                            "       \"" + this.settings.entryName
                                    + "\"         = "
                                    + this.dbQuoted(this.settings.entry));

            try {

                this.connection.execute(sql.toString());

                if (!sqlStmt.toString().equals("")) {
                    try {
                        this.connection.execute(sqlStmt.toString());
                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }
                }
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }

        }

        if (!this.error()) {
            this.connection.commit();
            this.msg = this.rb.getMessage("sos.settings.dialog.msg_store");
        		
            
        }
  
        if (this.error()) {
            this.connection.rollback();
            this.record = backupRecord;
            return false;
        }

        return true;
    }

    /**
     * Liste von Datensätzen speichern
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */

    private boolean recordStoreList() throws Exception {
        this.debug(3, "recordStoreList");

        HashMap applications = new HashMap();
        HashMap sections = new HashMap();
        HashMap names = new HashMap();
        HashMap values = new HashMap();
        HashMap binary_values = new HashMap();
       
        int cnt = 0;
        int numOfEntries = 100;

        if (this.getRequestValue("num_of_entries") != null) {
            try {
                numOfEntries = Integer.parseInt(this.getRequestValue(
                        "num_of_entries").toString());
            } catch (Exception e) {
                numOfEntries = 100;
            }
        }

        for (int i = 0; i < numOfEntries; i++) {
            if (this.getRequestValue("application_" + i) == null) {
                continue;
            }
            if (this.getRequestValue("section_" + i) == null) {
                continue;
            }
            if (this.getRequestValue("name_" + i) == null) {
                continue;
            }
            
						boolean has_value  = false;  
						boolean has_binary_value = false;
						
						// !!! Binary zuerst
						if(this.getRequestValue("binary_value_"+i) != null ){ 
      				has_binary_value = true; 
      			}
      			else if(this.getRequestValue("value_"+i) != null		){ has_value        = true; }
      			
      			if(!has_value && !has_binary_value)         { continue; }
      			
            applications.put(new Integer(cnt), this.getRequestValue("application_" + i));
            sections.put(new Integer(cnt), this.getRequestValue("section_" + i));
            names.put(new Integer(cnt), this.getRequestValue("name_" + i));
            
            // binäre dokumente leeren
      			if(has_binary_value) { binary_values.put(new Integer(cnt++), "1");  }
      			else                 { values.put(new Integer(cnt++), this.getRequestValue("value_"+ i));  }
    				
        }
				 
        if(names.size() == 0 || (values.size() == 0 && binary_values.size() == 0)) { 
            this.setError(this.rb
                    .getMessage("sos.settings.dialog.err_not_found_list"),
                    SOSClassUtil.getMethodName());
            return false;
        }

        StringBuffer error = new StringBuffer();

        for (int i = 0; i < names.size(); i++) {
            if (names.get(new Integer(i)).toString().equals("")) {
                continue;
            }

            try {
                StringBuffer sqlStmt = new StringBuffer("update "+this.settings.source+" ");
      					if(binary_values.containsKey(new Integer(i))){
      					  sqlStmt.append(" set  \"LONG_VALUE\"  = NULL, ");
      					}
      					else{
      					  sqlStmt.append(" set  \""+this.settings.entryValue+"\" = "+this.dbQuoted(values.get(new Integer(i)).toString()) + ", ");
      					}
      					sqlStmt.append("\"MODIFIED\" = %now, ")
                                .append("\"MODIFIED_BY\" = '" + this.settings.author + "' ")
                        				.append(
                                " where \""
                                        + this.settings.entryApplication
                                        + "\" = "
                                        + this.dbQuoted(applications.get(
                                                new Integer(i)).toString())
                                        + " and ").append(
                                "\""
                                        + this.settings.entrySection
                                        + "\" = "
                                        + this.dbQuoted(sections.get(
                                                new Integer(i)).toString())
                                        + " and ").append(
                                "\""
                                        + this.settings.entryName
                                        + "\" = "
                                        + this.dbQuoted(names.get(
                                                new Integer(i)).toString()));

                String title = "";
                if (this.getRequestValue("title_" + i) != null) {
                    title = this.getRequestValue("title_" + i);
                } else {
                    title = names.get(new Integer(i)).toString();
                }
                try {
                    this.connection.execute(sqlStmt.toString());
                } catch (Exception e) {
                    error.append(title + " - " + e.getMessage() + "<br>");
                    break;
                }

            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                return false;
            }

        }

        if (error.length() == 0) {
            this.connection.commit();
        		this.msg = this.rb.getMessage("sos.settings.dialog.msg_store");
        } else {
            this.setError(error.toString(), SOSClassUtil.getMethodName());
            this.connection.rollback();
            return false;
        }

        return true;
    }

    /**
     * Debugging setzen
     * 
     * @param level
     *            Debug Level
     * @param msg
     *            Message
     * @see #debug(int, String)
     */
    private void debug(int level, String msg) throws Exception {

        if (level <= this.debugLevel) {
            this.out.println("<font style=\"color: #009933;\">[debug" + level
                    + "] [" + this.getClass().getName() + "] " + msg
                    + "</font><br>");
        }
    }

    /**
     * Dokumentation anzeigen
     * 
     * @throws Exception
     */

    private void showHelps() throws Exception {
        this.debug(3, "showHelps");

        String orderBy = "";
        if (this.documentationSort != null
                && !this.documentationSort.equals("")) {
            orderBy = " order by " + this.documentationSort;
        }

        String applicationsTitle = this.rb
                .getMessage("sos.settings.dialog.label_all_apps_title");
        String sectionsTitle = "&nbsp;";

        StringBuffer sql = new StringBuffer();
        sql
                .append(" select s.\"APPLICATION\", s.\"SECTION\", s.\"NAME\", s.\"VALUE\",s.\"DEFAULT_VALUE\",s.\"TITLE\", s.\"INPUT_TYPE\",s.\"INPUT_SIZE\",s.\"DISPLAY_TYPE\",s.\"DISPLAY_SIZE\", s.\"ENTRY_TYPE\", s.\"FORCED\" ");
        sql.append(" from " + this.settings.source + " s ");

        StringBuffer appSql = new StringBuffer();
        appSql.append(" select s.\"TITLE\" ");
        appSql.append(" from " + this.settings.source + " s ");

        String section = "";
        String function = "";

        if (this.item.equalsIgnoreCase("application")) {
            sql.append("  where  s.\"" + this.settings.entryApplication
                    + "\"  = s.\"" + this.settings.entrySection + "\" and ");
            sql.append("         s.\"" + this.settings.entryApplication
                    + "\"  = s.\"" + this.settings.entryName + "\" ");
            sql.append(orderBy);

            function = "application";
        } else if (this.item.equalsIgnoreCase("section")) {
            sql.append("  where  s.\"" + this.settings.entryApplication
                    + "\"  =  " + this.dbQuoted(this.settings.application)
                    + " and ");
            sql.append("         s.\"" + this.settings.entryApplication
                    + "\"  <> s.\"" + this.settings.entrySection + "\" and ");
            sql.append("         s.\"" + this.settings.entrySection
                    + "\"      =  s.\"" + this.settings.entryName + "\" ");
            sql.append(orderBy);

            appSql.append(" where  s.\"" + this.settings.entryApplication
                    + "\"  = " + this.dbQuoted(this.settings.application)
                    + " and ");
            appSql.append("        s.\"" + this.settings.entryApplication
                    + "\"  = s.\"" + this.settings.entrySection + "\" and ");
            appSql.append("        s.\"" + this.settings.entryApplication
                    + "\"  = s.\"" + this.settings.entryName + "\" ");

            try {
                applicationsTitle = this.connection.getSingleValue(appSql
                        .toString());
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }

            function = "section";
        } else if (this.item.equalsIgnoreCase("entry")) {

            section = this.settings.section;
            if (this.settings.section.equals(this.settings.entrySchemaSection)) {
                section = this.settings.entrySchemaSection;
            } else {
                this.setDialogSections();
                if (this.dialogSections != null
                        && this.dialogSections.size() != 0) {
                    HashMap hs = (HashMap) this.dialogSections.firstElement();
                    if (hs.containsKey("section")) {
                        if (hs.get("section").toString().equals(
                                this.settings.entrySchemaSection)) {
                            section = this.settings.entrySchemaSection;
                        }
                    }
                }
            }

            sql.append("  where  s.\"" + this.settings.entryApplication
                    + "\"  =  " + this.dbQuoted(this.settings.application)
                    + " and ");
            sql.append("         s.\"" + this.settings.entrySection
                    + "\"      =  " + this.dbQuoted(section) + " and ");
            sql.append("         s.\"" + this.settings.entrySection
                    + "\"      <>  s.\"" + this.settings.entryName + "\" ");
            sql.append(orderBy);

            appSql.append(" where  s.\"" + this.settings.entryApplication
                    + "\"  = " + this.dbQuoted(this.settings.application)
                    + " and ");
            appSql.append("        s.\"" + this.settings.entryApplication
                    + "\"  = s.\"" + this.settings.entrySection + "\" and ");
            appSql.append("        s.\"" + this.settings.entryApplication
                    + "\"  = s.\"" + this.settings.entryName + "\" ");

            try {
                applicationsTitle = this.connection.getSingleValue(appSql
                        .toString());
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }

            StringBuffer secSql = new StringBuffer();

            secSql.append(" select s.\"TITLE\" ");
            secSql.append(" from " + this.settings.source + " s ");
            secSql.append(" where  s.\"" + this.settings.entryApplication
                    + "\"  =  " + this.dbQuoted(this.settings.application)
                    + " and ");
            secSql.append("        s.\"" + this.settings.entrySection
                    + "\"      = " + this.dbQuoted(section) + " and ");
            secSql.append("        s.\"" + this.settings.entrySection
                    + "\"      =  s.\"" + this.settings.entryName + "\" ");

            try {
                sectionsTitle = this.connection.getSingleValue(secSql
                        .toString());
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }

            function = "entry";

        }

        Vector items = null;

        try {
            items = this.connection.getArrayAsVector(sql.toString());
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            return;
        }

        if (items != null) {
            for (Enumeration el = items.elements(); el.hasMoreElements();) {
                HashMap item = (HashMap) el.nextElement();

                byte[] documentation = null;
                try {
                    documentation = this.connection
                            .getBlob("select \"DOCUMENTATION\" from "
                                    + this.settings.source
                                    + " where \""
                                    + this.settings.entryApplication
                                    + "\" = "
                                    + this.dbQuoted(item.get("application")
                                            .toString())
                                    + " and \""
                                    + this.settings.entrySection
                                    + "\" = "
                                    + this.dbQuoted(item.get("section")
                                            .toString())
                                    + " and \""
                                    + this.settings.entryName
                                    + "\" = "
                                    + this
                                            .dbQuoted(item.get("name")
                                                    .toString()) + " ");
                } catch (Exception e) {
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                    return;
                }

                if (documentation != null) {
                    item.put("documentation", new String(documentation));
                } else {
                    item.put("documentation", "");
                }
                if (item.get("documentation").equals("")) {
                    item.put("documentation", "&nbsp;");
                }
                if (item.get("value").equals("")) {
                    item.put("value", "&nbsp;");
                }
                if (item.get("default_value").equals("")) {
                    item.put("default_value", "&nbsp;");
                }
                if (item.get("input_type").equals("")) {
                    item.put("input_type", "&nbsp;");
                }
                if (item.get("input_size").equals("")) {
                    item.put("input_size", "&nbsp;");
                }
                if (item.get("display_type").equals("")) {
                    item.put("display_type", "&nbsp;");
                }
                if (item.get("display_size").equals("")) {
                    item.put("display_size", "&nbsp;");
                }
                if (item.get("entry_type").equals("")) {
                    item.put("entry_type", "&nbsp;");
                }
                if (item.get("forced").equals("")) {
                    item.put("forced", "&nbsp;");
                }

                item.put("application_title", applicationsTitle);
                item.put("section_title", sectionsTitle);

                //kein schema
                /*
                 * if (!item.get("section").toString().equals(
                 * this.settings.entrySchemaSection) &&
                 * !item.get("name").toString().equals(
                 * this.settings.entrySchemaSection)) {
                 */
                if (function.equals("application")) {
                    this.docuApplication(item);
                } else if (function.equals("section")) {
                    this.docuSection(item);
                } else if (function.equals("entry")) {
                    this.docuEntry(item);
                }
                //}
            }
        }
    }

    /**
     * Dokumentation anzeigen
     * 
     * @throws Exception
     */

    private void showDocumentation() throws Exception {
        this.debug(3, "showDocumentation");

        StringBuffer sqlStmt = new StringBuffer(
                "select \"TITLE\",\"APPLICATION\",\"SECTION\",\"NAME\",\"VALUE\",\"DISPLAY_TYPE\",\"INPUT_TYPE\" from "
                        + this.settings.source);

        String sqlAnd = " where ";

				String[] display_type_entries = this.rb.getMessage("sos.settings.dialog.listbox_display_type_entries").split(";");
				String[] input_type_entries = this.rb.getMessage("sos.settings.dialog.listbox_input_type_entries").split(";");
				
        if (this.settings.section.equals(this.settings.entrySchemaSection)) {
            this.settings.section = this.settings.entrySchemaSection;
        }

        if (!this.settings.application.equals("")) {
            sqlStmt.append(sqlAnd + " \"" + this.settings.entryApplication
                    + "\" = " + this.dbQuoted(this.settings.application));
            sqlAnd = " and ";
        }
        if (!this.settings.section.equals("")) {
            sqlStmt.append(sqlAnd + " \"" + this.settings.entrySection
                    + "\" = " + this.dbQuoted(this.settings.section));
            sqlAnd = " and ";
        }

        sqlStmt.append(" order by \"" + this.settings.entryApplication
                + "\",\"" + this.settings.entrySection + "\",\""
                + this.settings.entryName + "\"");

        try {
            Vector results = this.connection.getArrayAsVector(sqlStmt
                    .toString());

            LinkedHashMap applications = new LinkedHashMap();
            LinkedHashMap sections = new LinkedHashMap();
            LinkedHashMap entries = new LinkedHashMap();

            for (Enumeration el = results.elements(); el.hasMoreElements();) {
                HashMap result = (HashMap) el.nextElement();
                if (result.get("application").toString().equals(
                        result.get("section").toString())
                        && result.get("application").toString().equals(
                                result.get("name").toString())) {

                    applications.put(result.get("application").toString(),
                            result);

                } else if (result.get("section").toString().equals(
                        result.get("name").toString())
                        && !result.get("section").toString().equals(
                                result.get("application").toString())) {

                    String key = result.get("application").toString();
                    LinkedHashMap section = new LinkedHashMap();

                    if (sections.containsKey(key)) {
                        section = (LinkedHashMap) sections.get(key);
                    }
                    section.put(result.get("section").toString(), result);
                    sections.put(key, section);

                } else {
                    String appKey = result.get("application").toString();
                    String secKey = result.get("section").toString();

                    LinkedHashMap section = new LinkedHashMap();
                    LinkedHashMap entry = new LinkedHashMap();

                    if (entries.containsKey(appKey)) {
                        section = (LinkedHashMap) entries.get(appKey);
                        if (section.containsKey(secKey)) {
                            entry = (LinkedHashMap) section.get(secKey);
                        }
                    }
                    entry.put(result.get("name").toString(), result);
                    section.put(secKey, entry);
                    entries.put(appKey, section);

                }

            }
            //out.print(entries);

            results = null;
            this.styleBorder = "1\" bgcolor=\"#ffffff\" bordercolorlight=\"#D2D2D2\" bordercolordark=\"#ffffff";

            this.showTableBegin();
            if (applications.size() != 0) {
                Iterator appIt = applications.entrySet().iterator();
                while (appIt.hasNext()) {
                    Map.Entry application = (Map.Entry) appIt.next();
                    String app_key = application.getKey().toString();
                    HashMap app_value = (HashMap) application.getValue();
                    this.out.println("<tr>");
                    this.out
                            .println("	<td colspan=\"3\" nowrap style=\"color:#808080;font-weight:bold;\">"
                                    + app_value.get("title") + "</td>");
                    this.out.println("	<td nowrap>" + app_value.get("name")
                            + "</td>");
                    this.out.println("	<td nowrap>&nbsp;</td>");
                    this.out.println("</tr>");
                    if (sections.containsKey(app_key)) {
                        HashMap appSections = (HashMap) sections.get(app_key);
                        Iterator secIt = appSections.entrySet().iterator();
                        while (secIt.hasNext()) {
                            Map.Entry section = (Map.Entry) secIt.next();
                            String sec_key = section.getKey().toString();
                            HashMap sec_value = (HashMap) section.getValue();
                            if (!sec_value.get("name").toString().equals(
                                    this.settings.entrySchemaSection)) {
                                this.out.println("<tr>");
                                this.out.println("	<td nowrap>&nbsp;</td>");
                                this.out
                                        .println("	<td colspan=\"2\" nowrap style=\"color:#808080;font-weight:bold;\">"
                                                + sec_value.get("title")
                                                + "</td>");
                                this.out.println("	<td nowrap>"
                                        + sec_value.get("name") + "</td>");
                                this.out.println("	<td nowrap>&nbsp;</td>");
                                this.out.println("</tr>");
                                if (entries.containsKey(app_key)) {
                                    HashMap secEntrie = (HashMap) entries
                                            .get(app_key);
                                    if (secEntrie.containsKey(sec_key)) {
                                        HashMap secEntries = (HashMap) secEntrie
                                                .get(sec_key);

                                        Iterator entIt = secEntries.entrySet()
                                                .iterator();
                                        while (entIt.hasNext()) {
                                            Map.Entry entry = (Map.Entry) entIt
                                                    .next();
                                            String ent_key = entry.getKey()
                                                    .toString();
                                            HashMap ent_value = (HashMap) entry
                                                    .getValue();

                                            this.out.println("<tr>");
                                            this.out
                                                    .println("	<td width=\"20\" nowrap>&nbsp;</td>");
                                            this.out
                                                    .println("	<td width=\"20\" nowrap>&nbsp;</td>");
                                            this.out
                                                    .println("	<td width=\"35%\" nowrap valign=\"top\">"
                                                            + ent_value
                                                                    .get("title")
                                                            + "</td>");
                                            this.out
                                                    .println("	<td width=\"10%\" nowrap valign=\"top\">"
                                                            + ent_value
                                                                    .get("name")
                                                            + "</td>");
                                            this.out
                                                    .println("	<td nowrap valign=\"top\">");
                                            if (ent_value.get("input_type").toString().equals("5")) { // long_value
                                                this.out.println("[ "+this.rb.getMessage("sos.settings.dialog.dialog_long_value_title")+ " ]");
                                                if(ent_value.get("display_type").toString().equals("4")){
                      														this.out.println("&nbsp;"+display_type_entries[4]);  
                    														}
                                            } 
                                            else if(ent_value.get("input_type").toString().equals("6")){ // binary versteckt
                    													this.out.println("[ "+input_type_entries[6]+" ]");
                  													}
                                            else {
                                                if (ent_value.get("display_type").toString().endsWith("3")) {
                                                    this.out
                                                            .println("<pre>"
                                                                    + this
                                                                            .htmlSpecialChars(ent_value
                                                                                    .get(
                                                                                            "value")
                                                                                    .toString())
                                                                    + "</pre>");
                                                } else {
                                                    this.out
                                                            .println(this
                                                                    .htmlSpecialChars(ent_value
                                                                            .get(
                                                                                    "value")
                                                                            .toString())
                                                                    + "&nbsp;");
                                                }
                                            }
                                            this.out.println("	</td>");
                                            this.out.println("</tr>");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }// if applications
            else if (sections.size() != 0) {
                try {
                    HashMap application = this.connection
                            .getSingle("select \"TITLE\",\"NAME\" from "
                                    + this.settings.source + " where \""
                                    + this.settings.entryApplication + "\" = "
                                    + this.dbQuoted(this.settings.application)
                                    + " and \"" + this.settings.entrySection
                                    + "\" = "
                                    + this.dbQuoted(this.settings.application)
                                    + " and \"" + this.settings.entryName
                                    + "\" = "
                                    + this.dbQuoted(this.settings.application));

                    Iterator secIt = sections.entrySet().iterator();
                    if (sections.containsKey(this.settings.application)) {
                        HashMap appSections = (HashMap) sections
                                .get(this.settings.application);
                        Iterator secI = appSections.entrySet().iterator();
                        while (secI.hasNext()) {
                            Map.Entry section = (Map.Entry) secI.next();
                            String sec_key = section.getKey().toString();
                            HashMap sec_value = (HashMap) section.getValue();
                            this.out.println("<tr>");
                            this.out
                                    .println("	<td colspan=\"3\" nowrap style=\"color:#808080;font-weight:bold;\">"
                                            + application.get("title")
                                            + "</td>");
                            this.out.println("	<td nowrap>"
                                    + application.get("name") + "</td>");
                            this.out.println("	<td nowrap>&nbsp;</td>");
                            this.out.println("</tr>");
                            this.out.println("<tr>");
                            this.out.println("	<td nowrap>&nbsp;</td>");
                            this.out
                                    .println("	<td colspan=\"2\" nowrap style=\"color:#808080;font-weight:bold;\">"
                                            + sec_value.get("title") + "</td>");
                            this.out.println("	<td nowrap>"
                                    + sec_value.get("name") + "</td>");
                            this.out.println("	<td nowrap>&nbsp;</td>");
                            this.out.println("</tr>");
                            if (entries.containsKey(this.settings.application)) {
                                HashMap secEntrie = (HashMap) entries
                                        .get(this.settings.application);
                                if (secEntrie.containsKey(sec_key)) {
                                    HashMap secEntries = (HashMap) secEntrie
                                            .get(sec_key);
                                    Iterator entI = secEntries.entrySet()
                                            .iterator();
                                    while (entI.hasNext()) {
                                        Map.Entry entry = (Map.Entry) entI
                                                .next();
                                        String ent_key = entry.getKey()
                                                .toString();
                                        HashMap ent_value = (HashMap) entry
                                                .getValue();

                                        this.out.println("<tr>");
                                        this.out
                                                .println("	<td width=\"3%\" nowrap>&nbsp;</td>");
                                        this.out
                                                .println("	<td width=\"3%\" nowrap>&nbsp;</td>");
                                        this.out
                                                .println("	<td width=\"35%\" nowrap valign=\"top\">"
                                                        + ent_value
                                                                .get("title")
                                                        + "</td>");
                                        this.out
                                                .println("	<td width=\"10%\" nowrap valign=\"top\">"
                                                        + ent_value.get("name")
                                                        + "</td>");
                                        this.out
                                                .println("	<td nowrap valign=\"top\">");
                                        if (ent_value.get("input_type").toString().equals("5")) { // long_value
                                            this.out.println("[ "+ this.rb.getMessage("sos.settings.dialog.dialog_long_value_title")+ " ]");
                                        		if(ent_value.get("display_type").toString().equals("4")){
                      												this.out.println("&nbsp;"+display_type_entries[4]);  
                    												}
                                        } 
                                        else if(ent_value.get("input_type").toString().equals("6")){ // binary versteckt
                    											this.out.println("[ "+input_type_entries[6]+" ]");
                  											} 
                                        else {
                                            if (ent_value.get("display_type")
                                                    .toString().endsWith("3")) {
                                                this.out
                                                        .println("<pre>"
                                                                + this
                                                                        .htmlSpecialChars(ent_value
                                                                                .get(
                                                                                        "value")
                                                                                .toString())
                                                                + "</pre>");
                                            } else {
                                                this.out
                                                        .println(this
                                                                .htmlSpecialChars(ent_value
                                                                        .get(
                                                                                "value")
                                                                        .toString()));
                                            }
                                        }
                                        this.out.println("	</td>");
                                        this.out.println("</tr>");
                                    }
                                }
                            }
                        }
                        //out.print(section);

                    }

                } catch (Exception e) {
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                    return;
                }
            }

            this.showTableEnd();

        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            return;
        }

    }
    
    
  /**
  * Sourcecode bei binären Dokumenten
  * 
  * @return   boolean  Fehlerzustand
  * @access   public
  * @author   Robert Ehrlich <re@sos-berlin.com>
  * @version  1.0-2004/03/17
  */

  private void downloadSource(String application,String section,String entry) throws Exception{
    this.debug(3,"download_source : application = "+application+" section = "+section+" entry = "+entry);
    
    if(application == null || application.length() == 0){
    	this.setError("application for download is empty",SOSClassUtil.getMethodName());	
    	return;
    }
    
    if(section == null || section.length() == 0){
    	this.setError("section for download is empty",SOSClassUtil.getMethodName());	
    	return;
    }
    
    if(entry == null || entry.length() == 0){
    	this.setError("entry for download is empty",SOSClassUtil.getMethodName());	
    	return;
    }
    
    byte[] data;
    try{
    	String sql = "select \"LONG_VALUE\" from "+this.settings.source+" where \""+this.settings.entryApplication+"\" = "+this.dbQuoted(application)+" and \""+this.settings.entrySection+"\" = "+this.dbQuoted(section)+" and \""+this.settings.entryName+"\" = "+this.dbQuoted(entry);
    	data = this.connection.getBlob(sql);
    	if(data == null || data.length == 0){
    		this.setError("no data found for application <b>"+application+"</b> section <br>"+section+"</b> entry <b>"+entry+"</b>", SOSClassUtil.getMethodName());
    		return;
    	}
    }
    catch(Exception e){
    	this.setError(e.getMessage(), SOSClassUtil.getMethodName());
    	return;
    }
    
    String r_file_name = this.getRequestValue("file_name");
    String file_name 	= (r_file_name != null && r_file_name.length()>0) ? r_file_name : this.settings.defaultDocumentFileName; 
    
    
    String header_ext = "application/octet-stream";
    
    // da beim Download getOutputStream benutz wird(damit schreibt man binäre Sachen)
	// kommt es ohne aufzuräumen zu einer Exception (nur in Tomcate LOGs),
	// weil out bereits aktiv ist(es kann entweder OutputStream oder Writer geben)
    // muss auch in der jsp Datei gesetzt werden
    this.out.clear();
    
    // keine Header mehr !!!
    this.response.setContentType(header_ext);
    this.response.setIntHeader("Content-length",data.length);
    this.response.setHeader("Content-Disposition","attachment; filename="+file_name);
    
    
    //this.response.flushBuffer();
    //this.response.getWriter().write(new String(data));
    //this.response.getWriter().close();
    
    this.isSourceDownloaded = false;
    
    try{
    	       
        javax.servlet.ServletOutputStream os = response.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
        
        this.isSourceDownloaded = true;
        System.out.println("--------------- Downloaded -----------------");

    }
    catch(Exception e){
        System.out.println("EXCEPTION : "+e);
    }
  }

    
    
    

    /**
     *  
     */
    private String htmlSpecialChars(String htmltext) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < htmltext.length(); i++) {
            char elem = htmltext.charAt(i);
            if (elem == '<')
                sb.append("&lt;");
            else if (elem == '>')
                sb.append("&gt;");
            else if (elem == '&')
                sb.append("&amp;");
            else if (elem == '"')
                sb.append("&quot;");
            else
                sb.append(elem);
        }
        return sb.toString();
    }

    /**
     * Template für die Anzeige der Applikationsdoku
     * 
     * @param settings
     *            Werte für die Doku
     * @see #docuApplication(HashMap)
     */
    private void docuApplication(HashMap settings) throws Exception {
        this.debug(3, "docuApplication");

        if (!settings.get("documentation").toString().equals("&nbsp;")) {
            this.out.println("<p class=\"sos-doc-content\">");
            this.out
                    .println("  <table class=\"sos-application-table\" cellpadding=\"1\" cellspacing=\"1\">");
            this.out.println("  <tr>");
            this.out
                    .println("    <th align=\"left\" class=\"sos-application-th\">"
                            + settings.get("title").toString() + "</th>");
            this.out.println("  </tr>");
            this.out.println("  <tr>");
            this.out.println("    <td class=\"sos-application-td\">"
                    + settings.get("documentation").toString() + "</td>");
            this.out.println("  </tr>");
            this.out.println("  </table>");
            this.out.println("</p>");
        }
    }

    /**
     * Template für die Anzeige der Sektionsdoku
     * 
     * @param settings
     *            Werte für die Doku
     * @see #docuSection(HashMap)
     */

    private void docuSection(HashMap settings) throws Exception {
        this.debug(3, "docuSection");

        if (!settings.get("documentation").toString().equals("&nbsp;")) {
            this.out
                    .println("<table class=\"sos-doc-table\" cellpadding=\"1\" cellspacing=\"1\">");
            this.out.println("<tr>");
            this.out.println("  <th align=\"left\" class=\"sos-section-th\">");
            this.out.println("    <a class=\"sos-keyword\" name=\""
                    + settings.get("title").toString() + "\">");
            //this.out.println(" <a
            // href=\""+settings.get("title").toString()+"_doc.htm\">"+settings.get("application_title").toString()+"/"+settings.get("title").toString()+"</a>");
            this.out.println("      "
                    + settings.get("application_title").toString() + "/"
                    + settings.get("title").toString());
            this.out.println("    </a>");
            this.out.println("  </th>");
            this.out.println("</tr>");
            this.out.println("<tr>");
            this.out.println("  <td class=\"sos-section-td\">"
                    + settings.get("documentation").toString() + "</td>");
            this.out.println("</tr>");
            this.out.println("</table>");
        }
    }

    /**
     * Template für die Anzeige der Entriesdoku
     * 
     * @param settings
     *            Werte für die Doku
     * @see #docuEntry(HashMap)
     */

    private void docuEntry(HashMap settings) throws Exception {
        this.debug(3, "docuEntry");

        if (!settings.get("documentation").toString().equals("&nbsp;")) {
            String sectionTitle = "";

            if (!settings.get("section").toString().equals(
                    this.settings.entrySchemaSection)) {
                sectionTitle = settings.get("section_title") + "/";
            }

            this.out
                    .println("<table class=\"sos-entry-table\" cellpadding=\"1\" cellspacing=\"1\">");
            this.out.println("<tr>");
            this.out.println("  <th align=\"left\" class=\"sos-entry-th\">");
            this.out.println(settings.get("application_title").toString() + "/"
                    + sectionTitle + settings.get("title").toString());
            if (settings.get("forced").toString().equals("1")) {
                this.out
                        .println("&nbsp;:&nbsp;"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_mandatory_field"));
            }
            this.out.println("  </th>");
            this.out.println("</tr>");
            this.out.println("<tr>");
            this.out.println("  <td class=\"sos-section-td\">"
                    + settings.get("documentation").toString() + "</td>");
            this.out.println("</tr>");
            this.out.println("</table>");
        }
    }

    /**
     * Hilfetexte anzeigen
     * 
     * @throws Exception
     */

    private void showHelp() throws Exception {

        this.debug(3, "showHelp");

        if (this.settings.application.equals("")) {
            this.setError(this.rb
                    .getMessage("sos.settings.dialog.err_missing_apps"),
                    SOSClassUtil.getMethodName());
            return;
        }

        if (this.settings.section.equals("")) {
            this.setError(this.rb
                    .getMessage("sos.settings.dialog.err_missing_section"),
                    SOSClassUtil.getMethodName());
            return;
        } else {
            if (this.settings.section.equals(this.settings.entrySchemaSection)) {
                this.settings.section = this.settings.entrySchemaSection;
            }
        }

        if (this.settings.entry.equals("")) {
            this.setError(this.rb
                    .getMessage("sos.settings.dialog.err_missing_entry"),
                    SOSClassUtil.getMethodName());
            return;
        }

        String title = "";
        byte[] help = null;
        String helpText = "";

        try {
            title = this.connection.getSingleValue("select \"TITLE\" from "
                    + this.settings.source + " where \"APPLICATION\" = "
                    + this.dbQuoted(this.settings.application)
                    + " and \"SECTION\" = "
                    + this.dbQuoted(this.settings.section) + " and \"NAME\" = "
                    + this.dbQuoted(this.settings.entry));

            try {
                help = this.connection.getBlob("select \"DOCUMENTATION\" from "
                        + this.settings.source + " where \"APPLICATION\" = "
                        + this.dbQuoted(this.settings.application)
                        + " and \"SECTION\" = "
                        + this.dbQuoted(this.settings.section)
                        + " and \"NAME\" = "
                        + this.dbQuoted(this.settings.entry));
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                return;
            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            return;
        }

        if (help == null || help.length == 0) {
            helpText = this.rb.getMessage("sos.settings.dialog.msg_helps");
        } else {
            helpText = new String(help);
        }

        this.showTableBegin();

        this.out.println("<tr class=\"" + this.styleTr + "\">");
        this.out.println("  <th align=\"left\" valign=\"top\" class=\""
                + this.styleTh + "\">");
        this.out.println("    " + title);
        this.out.println("  </th>");
        this.out.println("</tr>");
        this.out.println("<tr class=\"" + this.styleTr + "\">");
        this.out.println("  <td valign=\"top\" class=\"" + this.styleTdLabel
                + "\">");
        this.out.println("    " + helpText + " ");
        this.out.println("  </td>");
        this.out.println("</tr>");

        this.showTableEnd();

        this.out.println("<table width=\"97%\" border=\"0\" align=\"center\">");
        this.out.println(" <tr>");
        this.out
                .println("  <td align=\"right\"><a href=\"javascript:window.close();\">"
                        + this.rb
                                .getMessage("sos.settings.dialog.label_close_window")
                        + "</br></td>");
        this.out.println("  </tr>");
        this.out.println("</table>");

    }

    /**
     * Alle Einträge einer Sektion anzeigen
     * 
     * @param entriesTitle
     *            Titel
     * @param entries
     *            Werte(siehe getDialogEntries)
     * @return boolean Fehlerzustand
     * @throws Exception
     * @see #showDialogEntries(String, Vector)
     */

    private boolean showDialogEntries(String entriesTitle, Vector entries)
            throws Exception {

        this.debug(3, "showDialogEntries : entriesTitle = " + entriesTitle
                + " entries = " + entries);

        // Fleck für showDialogValues()
        this.isShowEntries = true;

        if (entriesTitle != null) {
            this.dialogEntriesTitle = entriesTitle;
        }
        if (entries != null) {
            this.dialogEntries = entries;
        }

        String imgLink = "<img src=\"" + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"4\" vspace=\"1\">";

        String thisCon = (this.site.indexOf('?') != -1) ? "&" : "?";
        String querySession = "?";
        if (!this.sessionUseTransSID && !this.sessionVAR.equals("")) {
            querySession = thisCon + this.sessionVAR + "=" + this.sessionID
                    + "&";
        } else {
            querySession = thisCon;
        }

        this.showTableBegin();

        int columnCount = this.enableEntryNames ? 3 : 2;

        try {
            this.showNavigation(new Integer(columnCount),
                    this.dialogApplicationIndex, this.dialogSectionIndex, null);
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());

        }

        this.aclRange = "section";
        try {
            this.getRights(this.settings.application, this.settings.section,
                    null);
        } catch (Exception e) {
            this.setError("ACL : " + e.getMessage(), SOSClassUtil
                    .getMethodName());
        }

        if (this.applicationType == 1) {
            if (this.hasCreateRight) {
                this.hasWriteRight = true;
                this.disabled = "";
            }
        }

        // JS Code schreiben
        this.openHelpWin();

        if (this.enableEntryValues == true) {
            this.out.println("<form name=\"" + this.form + "\" action=\""
                    + this.site + "\" method=\"post\" onSubmit=\"return "
                    + this.form + "_check_onSubmit()\">");
        }

        if (this.dialogSectionIndex.intValue() >= 0
                && this.enableEntryManager == true) {
            boolean showNew = false;
            if (this.applicationType == 1) {
                if (this.settings.section
                        .equals(this.settings.entrySchemaSection)) {
                    if (this.hasCreateRight == true) {
                        showNew = true;
                    }
                }
            } else {
                if (this.hasCreateRight == true) {
                    showNew = true;
                }
            }
            if (showNew == true) {
                this.out.println("  <tr class=\"" + this.styleTr + "\">");
                String link = "<a class=\"" + this.styleLinkNavigation
                        + "\" href=\"" + this.site + querySession
                        + "action=new&range=entry&application="
                        + this.response.encodeURL(this.settings.application)
                        + "&section="
                        + this.response.encodeURL(this.settings.section)
                        + "&application_type=" + this.applicationType;
                if (this.sectionType > 0) {
                    link += "&section_type=" + this.sectionType;
                }
                link += "\">";
                this.out.println("    <td class=\"" + this.styleTd + "\">"
                        + link + imgLink + this.dialogEntriesNewTitle
                        + "</a>&nbsp;</td>");
                if (this.enableEntryNames == true) {
                    this.out.println("    <td class=\"" + this.styleTd
                            + "\">&nbsp;</td>");
                }
                this.out.println("    <td class=\"" + this.styleTd
                        + "\">&nbsp;</td>");
                this.out.println("  </tr>");
            }
        }

        String linkEntry = "<a href=\"" + this.site + querySession
                + "action=show";
        String linkSchema = (this.enableEntryManager == true) ? "<a href=\""
                + this.site + querySession + "action=schema" : linkEntry;
        int cnt = 0;
        String lastApplication = "";
        String lastSection = "";

        boolean hasTitle = false;
        if (this.action.equals("query") || this.action.equals("duplicate")) {
            hasTitle = true;
        }

        HashMap application = new HashMap();
        HashMap section = new HashMap();

        application.put("entry_type", new Integer(this.applicationType));
        section.put("entry_type", new Integer(this.sectionType));
        String aclApplication = "";

        StringBuffer sql = new StringBuffer();
        StringBuffer sqlHelp = new StringBuffer();
        StringBuffer sqlBinary = new StringBuffer();

        if (this.sectionType > 0 || this.applicationType > 0) { // aus der
                                                                // Schema
            sql.append(" select s.\"" + this.settings.entryApplication
                    + "\",s.\"" + this.settings.entrySection + "\", s.\""
                    + this.settings.entryName + "\" ");
            sql.append(" from " + this.settings.source + " s ");
            sql.append(" where s.\"" + this.settings.entryApplication
                    + "\"  = " + this.dbQuoted(this.settings.application)
                    + " and ");
            sql.append("       s.\"" + this.settings.entrySection
                    + "\"      = '" + this.settings.entrySchemaSection
                    + "' and ");
            sql.append("       s.\"" + this.settings.entrySection
                    + "\"     <> s.\"" + this.settings.entryName + "\" and ");

            sqlBinary.append(" select s.\"" + this.settings.entryApplication
                    + "\",s.\"" + this.settings.entrySection + "\", s.\""
                    + this.settings.entryName + "\" ");
            sqlBinary.append(" from " + this.settings.source + " s ");
            sqlBinary.append(" where s.\"" + this.settings.entryApplication
                    + "\"  = " + this.dbQuoted(this.settings.application)
                    + " and ");
            sqlBinary.append("       s.\"" + this.settings.entrySection
                    + "\"      = " + this.dbQuoted(this.settings.section)
                    + " and ");
            sqlBinary.append("       s.\"" + this.settings.entrySection
                    + "\"     <> s.\"" + this.settings.entryName + "\" and ");
        } else {
            sql.append(" select s.\"" + this.settings.entryApplication
                    + "\",s.\"" + this.settings.entrySection + "\", s.\""
                    + this.settings.entryName + "\" ");
            sql.append(" from " + this.settings.source + " s ");
            sql.append(" where s.\"" + this.settings.entryApplication
                    + "\"  = " + this.dbQuoted(this.settings.application)
                    + " and ");
            sql.append("       s.\"" + this.settings.entrySection
                    + "\"      = " + this.dbQuoted(this.settings.section)
                    + " and ");
            sql.append("       s.\"" + this.settings.entrySection
                    + "\"     <> s.\"" + this.settings.entryName + "\" and ");

            sqlBinary = new StringBuffer(sql.toString());
        }

        sqlBinary.append("  s.\"LONG_VALUE\" is not null");
        
        sqlHelp = sql.append("  s.\"DOCUMENTATION\" is not null");

        
        Hashtable helpTexts = new Hashtable();
    
        try {
            Vector helpText = this.connection.getArrayAsVector(sqlHelp
                    .toString());
            if (helpText.size() > 0) {
                for (Enumeration el = helpText.elements(); el.hasMoreElements();) {
                    HashMap hm = (HashMap) el.nextElement();
                    helpTexts.put(hm.get("application").toString()
                            + hm.get("section").toString()
                            + hm.get("name").toString(), "Help");
                }
                this.hasHelps = true;
            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

        this.hasBinaryValue = new Hashtable();
        try {
            Vector binaryValues = this.connection.getArrayAsVector(sqlBinary
                    .toString());
            if (binaryValues.size() > 0) {
                for (Enumeration el = binaryValues.elements(); el
                        .hasMoreElements();) {
                    HashMap hm = (HashMap) el.nextElement();
                    this.hasBinaryValue.put(hm.get("application").toString()
                            + hm.get("section").toString()
                            + hm.get("name").toString(), "1");
                }
            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

        Vector checkEntries = new Vector();
        int ce = 0;
        int index = 0;
        boolean isNotAllDisabled = false;

        String[] display_type_entries = this.rb.getMessage(
                "sos.settings.dialog.listbox_display_type_entries").split(";");
        if (display_type_entries.length != 5) {
            this
                    .setError(
                            "\"sos.settings.dialog.listbox_display_type_entries\" expected 5 values, given : "
                                    + display_type_entries.length, SOSClassUtil
                                    .getMethodName());
            return false;
        }

        String[] input_type_entries = this.rb.getMessage(
                "sos.settings.dialog.listbox_input_type_entries").split(";");
        if (input_type_entries.length != 7) {
            this
                    .setError(
                            "\"sos.settings.dialog.listbox_input_type_entries\" expected 7 values, given : "
                                    + input_type_entries.length, SOSClassUtil
                                    .getMethodName());
            return false;
        }

        for (Enumeration el = this.dialogEntries.elements(); el
                .hasMoreElements();) {
            HashMap entry = (HashMap) el.nextElement();

            this.aclRange = "entry";
            try {
                this.getRights(entry.get("application").toString(), entry.get(
                        "section").toString(), entry.get("name").toString());
                if (this.hasReadRight == false && this.hasCreateRight == false) {
                    continue;
                }
            } catch (Exception e) {
                this.out.println("  <tr class=\"" + this.styleTr + "\">");
                this.out.println("   <td valign=\"middle\" class=\""
                        + this.styleTd + "\" colspan=\"3\">");
                this.out
                        .println("   <table width=\"100%\" border =\"0\" cellspacing=\"0\" cellpadding=\"0\">");
                this.out.println("   <tr>");
                this.out.println("     <td>");
                this.out.println("        "
                        + entry.get(this.settings.entryTitle.toLowerCase())
                                .toString() + "&nbsp;");
                this.out.println("     </td>");
                this.out.println("     <td align=\"right\" nowrap>");
                this.showError("ACL : " + e.getMessage());
                this.out.println("     </td>");
                this.out.println("   </tr>");
                this.out.println("   </table>");
                this.out.println("   </td>");
                this.out.println("  </tr>");
                continue;
            }
            // Acls end

            cnt++;
            String link = "";
            if ((hasTitle == true)
                    && (!entry
                            .get(this.settings.entryApplication.toLowerCase())
                            .toString().equals(lastApplication))) {
                try {
                    application = this.connection
                            .getSingle("select \"NAME\",\"TITLE\",\"ENTRY_TYPE\" from "
                                    + this.settings.source
                                    + " where \""
                                    + this.settings.entryApplication
                                    + "\" = "
                                    + this.dbQuoted(entry.get(
                                            this.settings.entryApplication
                                                    .toLowerCase()).toString())
                                    + " and \""
                                    + this.settings.entryApplication
                                    + "\"= \""
                                    + this.settings.entrySection
                                    + "\" and \""
                                    + this.settings.entrySection
                                    + "\"= \""
                                    + this.settings.entryName + "\"");
                    if (application.size() == 0) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                try {
                    section = this.connection
                            .getSingle("select \"NAME\",\"TITLE\",\"ENTRY_TYPE\" from "
                                    + this.settings.source
                                    + " where \""
                                    + this.settings.entryApplication
                                    + "\" = "
                                    + this.dbQuoted(entry.get(
                                            this.settings.entryApplication
                                                    .toLowerCase()).toString())
                                    + " and \""
                                    + this.settings.entrySection
                                    + "\"= "
                                    + this.dbQuoted(entry.get(
                                            this.settings.entrySection
                                                    .toLowerCase()).toString())
                                    + " and \""
                                    + this.settings.entrySection
                                    + "\"= \"" + this.settings.entryName + "\"");
                    if (section.size() == 0) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
                lastApplication = entry.get(
                        this.settings.entryApplication.toLowerCase())
                        .toString();
                lastSection = entry.get(
                        this.settings.entrySection.toLowerCase()).toString();
                this.out.println("  <tr class=\"" + this.styleTr + "\">");
                link = linkEntry + "&range=sections&application="
                        + this.response.encodeURL(lastApplication)
                        + "\" class=\"" + this.styleLinkNavigation + "\">";
                this.out.println("    <th class=\"" + this.styleTh
                        + "\" align=\"left\">" + link + imgLink
                        + application.get("title").toString() + "</a></th>");
                link = linkEntry + "&range=entries&application="
                        + this.response.encodeURL(lastApplication)
                        + "&section=" + this.response.encodeURL(lastSection)
                        + "&application_type="
                        + application.get("entry_type").toString()
                        + "&section_type="
                        + section.get("entry_type").toString() + "\" class=\""
                        + this.styleLinkNavigation + "\">";
                this.out.println("    <th class=\"" + this.styleTh
                        + "\" align=\"left\" colspan=\"" + (columnCount - 1)
                        + "\">" + link + imgLink
                        + section.get("title").toString() + "</th>");
                this.out.println("  </tr>");

            }

            if ((hasTitle == true)
                    && (!entry.get(this.settings.entrySection.toLowerCase())
                            .toString().equals(lastSection))) {
                section = this.connection
                        .getSingle("select \"NAME\",\"TITLE\",\"ENTRY_TYPE\" from "
                                + this.settings.source
                                + " where \""
                                + this.settings.entryApplication
                                + "\" = "
                                + this.dbQuoted(entry.get(
                                        this.settings.entryApplication
                                                .toLowerCase()).toString())
                                + " and \""
                                + this.settings.entrySection
                                + "\"= "
                                + this.dbQuoted(entry.get(
                                        this.settings.entrySection
                                                .toLowerCase()).toString())
                                + " and \""
                                + this.settings.entrySection
                                + "\"= \"" + this.settings.entryName + "\"");
                lastSection = entry.get(
                        this.settings.entrySection.toLowerCase()).toString();
                this.out.println("  <tr class=\"" + this.styleTr + "\">");
                this.out.println("    <th class=\"" + this.styleTh
                        + "\" align=\"left\">&nbsp;</th>");
                link = linkEntry + "&range=entries&application="
                        + this.response.encodeURL(lastApplication)
                        + "&section=" + this.response.encodeURL(lastSection)
                        + "&application_type="
                        + application.get("entry_type").toString()
                        + "&section_type="
                        + section.get("entry_type").toString() + "\" class=\""
                        + this.styleLinkNavigation + "\">";
                this.out.println("    <th class=\"" + this.styleTh
                        + "\" align=\"left\" colspan=\"" + (columnCount - 1)
                        + "\">" + link + imgLink
                        + section.get("title").toString() + "</th>");
                this.out.println("  </tr>");
            }

            String linkHelp = "";

            /////////////

            this.out.println("  <tr class=\"" + this.styleTr + "\">");

            if (entry.get(this.settings.entrySection.toLowerCase()).toString()
                    .equals(this.settings.entrySchemaSection)) {
                linkHelp = "&nbsp;";

                String k = entry.get(
                        this.settings.entryApplication.toLowerCase())
                        .toString()
                        + this.settings.entrySchemaSection
                        + entry.get(this.settings.entryName.toLowerCase())
                                .toString();

                if (helpTexts.containsKey(k)) {
                    String session = "''";
                    if (!this.sessionID.equals("")) {
                        session = "'" + this.sessionID + "'";
                    }
                    String helpHref = "source="
                            + this.settings.source
                            + "&application="
                            + this.response.encodeURL(entry.get(
                                    this.settings.entryApplication
                                            .toLowerCase()).toString())
                            + "&section="
                            + this.response
                                    .encodeURL(this.settings.entrySchemaSection)
                            + "&entry="
                            + this.response.encodeURL(entry.get(
                                    this.settings.entryName.toLowerCase())
                                    .toString());
                    linkHelp = "<a href=\"javascript:openHelpWin('"
                            + helpHref
                            + "',"
                            + session
                            + ");\"><img src=\""
                            + this.imgDir
                            + this.imgHelp
                            + "\" border=\"0\" title=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_help")
                            + "\"></a>";
                }

                String linkQuery = "&range=entry&application="
                        + this.response.encodeURL(entry.get(
                                this.settings.entryApplication.toLowerCase())
                                .toString())
                        + "&section="
                        + this.response.encodeURL(entry.get(
                                this.settings.entrySection.toLowerCase())
                                .toString())
                        + "&entry="
                        + this.response.encodeURL(entry.get(
                                this.settings.entryName.toLowerCase())
                                .toString()) + "&application_type="
                        + application.get("entry_type").toString() + "\">";

                this.out.println("   <td valign=\"middle\" class=\""
                        + this.styleTd + "\">");
                this.out
                        .println("   <table width=\"100%\" border =\"0\" cellspacing=\"0\" cellpadding=\"0\">");
                this.out.println("   <tr>");
                this.out.println("     <td>");
                this.out.println("       " + linkSchema + linkQuery + imgLink
                        + "</a>");
                link = (entry.get(this.settings.entrySection.toLowerCase())
                        .toString().equals(this.settings.entrySchemaSection)) ? linkSchema
                        : linkEntry;
                this.out.println("        "
                        + link
                        + linkQuery
                        + entry.get(this.settings.entryTitle.toLowerCase())
                                .toString() + "</a>&nbsp;");
                this.out.println("     </td>");
                this.out.println("     <td align=\"right\" nowrap>");
                this.out.println("       " + linkHelp);
                this.out.println("     </td>");
                this.out.println("   </tr>");
                this.out.println("   </table>");
                this.out.println("   </td>");
            } else if (this.sectionType > 0 || this.applicationType > 0) { // schema

                linkHelp = "&nbsp;";
                if (this.hasCreateRight) { // bei Schema reicht CREATE Recht
                    this.hasWriteRight = true;
                    this.disabled = "";
                }

                String k = entry.get(
                        this.settings.entryApplication.toLowerCase())
                        .toString()
                        + this.settings.entrySchemaSection
                        + entry.get(this.settings.entryName.toLowerCase())
                                .toString();
                if (helpTexts.containsKey(k)) {
                    String session = "''";
                    if (!this.sessionID.equals("")) {
                        session = "'" + this.sessionID + "'";
                    }
                    String helpHref = "source="
                            + this.settings.source
                            + "&application="
                            + this.response.encodeURL(entry.get(
                                    this.settings.entryApplication
                                            .toLowerCase()).toString())
                            + "&section="
                            + this.response
                                    .encodeURL(this.settings.entrySchemaSection)
                            + "&entry="
                            + this.response.encodeURL(entry.get(
                                    this.settings.entryName.toLowerCase())
                                    .toString());

                    linkHelp = "<a href=\"javascript:openHelpWin('"
                            + helpHref
                            + "',"
                            + session
                            + ");\"><img src=\""
                            + this.imgDir
                            + this.imgHelp
                            + "\" border=\"0\" title=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_help")
                            + "\"></a>";
                }

                String linkQuery = "&range=entry&application="
                        + this.response.encodeURL(entry.get(
                                this.settings.entryApplication.toLowerCase())
                                .toString())
                        + "&section="
                        + this.response.encodeURL(entry.get(
                                this.settings.entrySection.toLowerCase())
                                .toString())
                        + "&entry="
                        + this.response.encodeURL(entry.get(
                                this.settings.entryName.toLowerCase())
                                .toString()) + "&application_type="
                        + application.get("entry_type").toString()
                        + "&section_type="
                        + section.get("entry_type").toString() + "\">";

                this.out.println("   <td valign=\"middle\" class=\""
                        + this.styleTd + "\">");
                this.out
                        .println("   <table width=\"100%\" border =\"0\" cellspacing=\"0\" cellpadding=\"0\">");
                this.out.println("   <tr>");
                this.out.println("     <td nowrap>");

                if (this.enableEntryValues == true) {
                    this.out.println("       "
                            + linkEntry
                            + linkQuery
                            + imgLink
                            + "</a>"
                            + linkEntry
                            + linkQuery
                            + entry.get(this.settings.entryTitle.toLowerCase())
                                    .toString() + "</a>&nbsp;");
                } else {
                    this.out.println("       "
                            + entry.get(this.settings.entryTitle.toLowerCase())
                                    .toString() + "&nbsp;");
                }
                this.out.println("     </td>");
                this.out.println("     <td align=\"right\" nowrap>");
                this.out.println("       " + linkHelp);
                this.out.println("     </td>");
                this.out.println("   </tr>");
                this.out.println("   </table>");
                this.out.println("   </td>");
            } else {
                linkHelp = "&nbsp;";
                String k = entry.get(
                        this.settings.entryApplication.toLowerCase())
                        .toString()
                        + entry.get(this.settings.entrySection.toLowerCase())
                                .toString()
                        + entry.get(this.settings.entryName.toLowerCase())
                                .toString();
                if (helpTexts.containsKey(k)) {
                    String session = "''";
                    if (!this.sessionID.equals("")) {
                        session = "'" + this.sessionID + "'";
                    }
                    String helpHref = "source="
                            + this.settings.source
                            + "&application="
                            + this.response.encodeURL(entry.get(
                                    this.settings.entryApplication
                                            .toLowerCase()).toString())
                            + "&section="
                            + this.response.encodeURL(entry.get(
                                    this.settings.entrySection.toLowerCase())
                                    .toString())
                            + "&entry="
                            + this.response.encodeURL(entry.get(
                                    this.settings.entryName.toLowerCase())
                                    .toString());
                    linkHelp = "<a href=\"javascript:openHelpWin('"
                            + helpHref
                            + "',"
                            + session
                            + ");\"><img src=\""
                            + this.imgDir
                            + this.imgHelp
                            + "\" border=\"0\" title=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_help")
                            + "\"></a>";
                }

                String linkQuery = "&range=entry&application="
                        + this.response.encodeURL(entry.get(
                                this.settings.entryApplication.toLowerCase())
                                .toString())
                        + "&section="
                        + this.response.encodeURL(entry.get(
                                this.settings.entrySection.toLowerCase())
                                .toString())
                        + "&entry="
                        + this.response.encodeURL(entry.get(
                                this.settings.entryName.toLowerCase())
                                .toString()) + "&application_type="
                        + application.get("entry_type").toString() + "\">";

                this.out.println("    <td valign=\"middle\" class=\""
                        + this.styleTd + "\" nowrap>");
                this.out
                        .println("   <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
                this.out.println("   <tr>");
                this.out.println("     <td>");
                if (this.enableEntryValues == true) {
                    this.out.println("       " + linkSchema + linkQuery
                            + imgLink + "</a>");
                    link = (entry.get(this.settings.entrySection.toLowerCase())
                            .toString()
                            .equals(this.settings.entrySchemaSection)) ? linkSchema
                            : linkEntry;
                    this.out.println("        " + link + linkQuery
                            + entry.get(this.settings.entryTitle.toLowerCase())
                            + "</a>&nbsp;");
                } else {
                    this.out.println("        "
                            + entry.get(this.settings.entryTitle.toLowerCase())
                            + "&nbsp;");
                }
                this.out.println("     </td>");
                this.out.println("     <td align=\"right\" nowrap>");
                this.out.println("       " + linkHelp);
                this.out.println("     </td>");
                this.out.println("   </tr>");
                this.out.println("   </table>");
                this.out.println("   </td>");
            }

            if (this.enableEntryNames == true) {
                this.out.println("    <td valign=\"top\" class=\""
                        + this.styleTd + "\">"
                        + entry.get(this.settings.entryName.toLowerCase())
                        + "&nbsp;</td>");
            }

            if ((this.enableEntryValues == true)
                    && (!entry.get(this.settings.entrySection.toLowerCase())
                            .toString()
                            .equals(this.settings.entrySchemaSection))) {

                if (this.disabled.equals("")) {
                    isNotAllDisabled = true;
                }

                this.out.println("    <td valign=\"top\" class=\""
                        + this.styleTd + "\">");
                /*
                 * if (entry.get("input_type").toString().equals("5")) {
                 * this.out.println("[ " + this.rb
                 * .getMessage("sos.settings.dialog.dialog_long_value_title") + "
                 * ]"); } else { this.showDialogValue(entry, 1, index); if
                 * (!entry.get("forced").toString().equals("0") &&
                 * !entry.get("forced").toString().equals("") &&
                 * entry.get("display_type").toString().equals("0")) {
                 * checkEntries.add(ce, "value_" + index); ce++; } }
                 * this.out.println("&nbsp; </td> ");
                 */
                this.out
                        .println("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                this.out.println("	<tr>");

                String hbv = "";
                if (this.hasBinaryValue.containsKey(entry.get("application")
                        .toString()
                        + entry.get("section").toString()
                        + entry.get("name").toString())) {
                    hbv = this.hasBinaryValue.get(
                            entry.get("application").toString()
                                    + entry.get("section").toString()
                                    + entry.get("name").toString()).toString();
                }

                if ((hbv != null && hbv.length() > 0 && !hbv.equals("0")) 
                	|| (entry.get("input_type").toString().equals("5") || entry.get("input_type").toString().equals("6") )
                	) {
                    if (entry.get("input_type").toString().equals("5")) { // Dokument

                        this.out.println("     <td>");
                        this.out
                                .println("[ "
                                        + this.rb
                                                .getMessage("sos.settings.dialog.dialog_long_value_title")
                                        + " ]&nbsp;");

                        if (entry.get("display_type").toString().equals("4")) { // versteckt
                            this.out.println(display_type_entries[4] + "</td>");
                            this.out.println("   <td align=\"right\">");
                            this.showDialogValue(entry, 1, index);
                            this.out.println("&nbsp;</td>");
                        } else {
                            this.out.println(" </td>");
                            this.out
                                    .println("   <td align=\"right\">&nbsp;</td>");
                        }
                    } else { // Dokument binär oder wir machen zum Dokument
                             // binär
                        this.out.println(" <td>");
                        this.out.println("[ " + input_type_entries[6] + " ]");
                        this.out.println(" </td>");
                        entry.put("input_type", "6");
                        entry.put("display_type", "4");
                        this.out.println(" <td align=\"right\">");
                        this.showDialogValue(entry, 1, index);
                        this.out.println("&nbsp;</td>");
                    }
                } else {
                    this.out.println(" <td colspan=\"2\">");
                    this.showDialogValue(entry, 1, index);
                    this.out.println(" </td>");
                    if (!entry.get("forced").toString().equals("0")
                            && !entry.get("forced").toString().equals("")
                            && entry.get("display_type").toString().equals("0")) {
                        checkEntries.add(ce, "value_" + index);
                        ce++;
                    }
                }

                this.out.println("</tr>");
                this.out.println("</table>");

                this.out.println("</td>");
            } else {
                this.out.println("    <td valign=\"middle\" class=\""
                        + this.styleTd
                        + "\">"
                        + entry.get(this.settings.entryValue.toLowerCase())
                                .toString() + "&nbsp;</td>");
            }
            this.out.println("  </tr>");

            index++;
        }// foreach

        this.out
                .println("<script language=\"JavaScript\" type=\"text/javascript\">");
        this.out.println("  function " + this.form + "_check_onSubmit() {");
        this.out.println(" ");
        this.out.print("    var entries = Array(");
        for (int i = 0; i < checkEntries.size(); i++) {
            if (i > 0) {
                this.out.print(",");
            }
            this.out.print("\"" + checkEntries.get(i) + "\"");
        }
        this.out.println(" );");
        this.out.println("    for(var i=0; i<entries.length; i++) {");
        this.out.println("      if ( document." + this.form
                + ".elements[entries[i]].value == null || document."
                + this.form + ".elements[entries[i]].value == \"\" ) {");
        this.out.println("        alert( \""
                + this.rb.getMessage("sos.settings.dialog.alert_entry_empty")
                + "\");");
        this.out.println("        document." + this.form
                + ".elements[entries[i]].focus();");
        this.out.println("        return false;");
        this.out.println("      }");
        this.out.println("    }");
        this.out.println("    return true;");
        this.out.println("  }");
        this.out.println("</script>");

        if (this.enableEntryValues == true) {
            this.out.println("  <tr class=\"" + this.styleTr + "\">");
            this.out.println("    <td valign=\"middle\" colspan=\""
                    + columnCount + "\" class=\"" + this.styleTd + "\">&nbsp;");

            if (isNotAllDisabled) {
                this.out
                        .println("      <input type=\"image\" name=\"btn_store\"  src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_store.gif\"  alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_store_alt")
                                + "\" onClick=\"document.sos_settings.button.value=\'store\';\">&nbsp;");
                this.out
                        .println("      <input type=\"image\" name=\"btn_cancel\" src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_cancel.gif\" alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_cancel_alt")
                                + "\" onClick=\"document.sos_settings.button.value=\'cancel\';\">&nbsp;");
                this.out
                        .println("      <input type=\"image\" name=\"btn_reset\"  src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_reset.gif\"  alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_reset_alt")
                                + "\" onClick=\"document.sos_settings.reset(); return false;\">&nbsp;");
            } else {
                this.out
                        .println("      <input type=\"image\" name=\"btn_cancel\" src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_cancel.gif\" alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_cancel_alt")
                                + "\" onClick=\"document.sos_settings.button.value=\'cancel\';\">&nbsp;");
            }
            this.out.println("<input type=\"hidden\" name=\"button\">");
            this.out
                    .println("<input type=\"hidden\" name=\"application\"  value=\""
                            + this.settings.application + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"section\"      value=\""
                            + this.settings.section + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"entry\"        value=\"\">");
            this.out
                    .println("<input type=\"hidden\" name=\"range\"        value=\"list\">");
            this.out
                    .println("<input type=\"hidden\" name=\"section_type\" value=\""
                            + this.sectionType + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"application_type\" value=\""
                            + this.applicationType + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"num_of_entries\" value=\""
                            + cnt + "\">");

            this.out.println("    </td>");
            this.out.println("  </tr>");
            this.out.println("  </form>");
        }

        this.showActions(new Integer(columnCount), this.dialogApplicationIndex,
                this.dialogSectionIndex, null);
        this.showTableEnd();

        return true;
    }

		/**
     */
		private void showUpload() throws Exception{

    	this.out.println("<script language=\"javascript\">");
      this.out.println(" ");
      this.out.println("	function importOnChange(){");
      this.out.println(" "); 
      this.out.println("  	var file_value		= document.getElementById('input_import');");
      this.out.println("  	var default_value = document."+this.form+".default_value;");
      this.out.println(" "); 
      this.out.println("		if(typeof default_value != 'undefined'){");
			this.out.println("			var arr = file_value.value.split('\\\\');");
			this.out.println("			var el	= arr.length-1;");
			this.out.println("			default_value.value = arr[el];");
			this.out.println("		}");  
      this.out.println("	}");
      this.out.println(" ");
    	this.out.println("</script>");
			this.out.println(" ");    
    	this.out.println("<span id=\"sos_settings_import\" style=\"display:"+this.displayBinaryUpload+"\">");
     	this.out.println("	<input onchange=\"importOnChange()\" id=\"input_import\" valign=\"middle\" type=\"file\" style=\"height:20px;width:400px;alpha(opacity=0);\" name=\"input_import\" value=\"\">");
    	this.out.println("</span>");

		}

    /**
     * Eintrag anzeigen
     * 
     * @param displayMode
     *            Flag zur Darstellung mit/ohne Schema
     * @param displayType
     *            0=Eintrag, 1=Sektion, 2=Applikation
     * @return boolean Fehlerzustand
     * @throws Exception
     * @see #showDialogEntry(int, int)
     */

    private boolean showDialogEntry(int displayMode, int displayType)
            throws Exception {

        this.debug(3, "showDialogEntry : displayMode = " + displayMode
                + " displayType = " + displayType);

        StringBuffer defaultJavascript = new StringBuffer();

        String labelDocument = "";

        String displayRange = (this.record.get(
                this.settings.entrySection.toLowerCase()).toString()
                .equals(this.record.get(this.settings.entryName.toLowerCase())
                        .toString())) ? "section" : "entry";

        boolean readRight = true;
        this.aclRange = "entry";
        try {
            if (this.getRights(this.record.get("application").toString(),
                    this.record.get("section").toString(), this.record.get(
                            "name").toString()) == false) {
                readRight = false;
            }

        } catch (Exception e) {
            this.setError("ACL : " + e.getMessage(), SOSClassUtil
                    .getMethodName());
            readRight = false;
        }

        if (this.settings.entrySchemaSection.equals(this.settings.section)) {
            if (this.hasCreateRight) {
                readRight = true;
                if (this.isNew) {
                    this.hasReadRight = true;
                    this.hasWriteRight = true;
                    this.disabled = "";
                }
            }
        }

        if (this.applicationType == 1 || this.isNew == true) {
            if (this.hasCreateRight) {
                this.hasWriteRight = true;
                this.disabled = "";
            }
        }

        String formOnSubmit = " onSubmit=\"return sos_settings_onSubmit()\"";
        if (this.hasWriteRight == false) {
            formOnSubmit = "";
        }

        this.out.println("<form enctype=\"multipart/form-data\" name=\"" + this.form + "\" action=\""
                + this.site + "\" method=\"post\"" + formOnSubmit + ">");

        this.out.println("<input type=\"hidden\" name=\"button\">");
        this.out
                .println("<input type=\"hidden\" name=\"application\"      value=\""
                        + this.record.get(
                                this.settings.entryApplication.toLowerCase())
                                .toString() + "\">");
        this.out
                .println("<input type=\"hidden\" name=\"section\"          value=\""
                        + this.record.get(
                                this.settings.entrySection.toLowerCase())
                                .toString() + "\">");
        this.out
                .println("<input type=\"hidden\" name=\"entry\"            value=\""
                        + this.record
                                .get(this.settings.entryName.toLowerCase())
                                .toString() + "\">");
        this.out
                .println("<input type=\"hidden\" name=\"range\"            value=\""
                        + displayRange + "\">");
        this.out
                .println("<input type=\"hidden\" name=\"section_type\"     value=\""
                        + this.sectionType + "\">");
        this.out
                .println("<input type=\"hidden\" name=\"application_type\" value=\""
                        + this.applicationType + "\">");

        String lastAction = (this.getRequestValue("last_action") != null && !this.action.equals("schema")) ? this.getRequestValue("last_action").trim() : this.action;
        
        this.out
                .println("<input type=\"hidden\" name=\"last_action\"      value=\""
                        + lastAction + "\">");
				
				if(!this.isNew && this.record.containsKey("original_name")){
      		this.out.println("<input type=\"hidden\" name=\"original_name\"  value=\""+this.record.get("original_name").toString()+"\" />");
    		}


        if (displayMode != 1 || this.sectionType > 0) {
            if (this.sectionType == 0) {
                this.out
                        .println("<input type=\"hidden\" name=\"title\"        value=\""
                                + this.record.get("title").toString() + "\">");
            }
            this.out
                    .println("<input type=\"hidden\" name=\"default_value\"  value=\""
                            + this.record.get("default_value").toString()
                            + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"entry_type\"     value=\""
                            + this.record.get("entry_type").toString() + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"input_type\"     value=\""
                            + this.record.get("input_type").toString() + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"input_size\"     value=\""
                            + this.record.get("input_size").toString() + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"display_type\"   value=\""
                            + this.record.get("display_type").toString()
                            + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"display_size\"   value=\""
                            + this.record.get("display_size").toString()
                            + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"forced\"         value=\""
                            + this.record.get("forced").toString() + "\">");
        }

        this.showTableBegin();

        this.showNavigation(new Integer(2), this.dialogApplicationIndex,
                this.dialogSectionIndex, null);

        if (readRight == false) {
            this.showTableEnd();
            return false;
        }

        this.out.println("<tr class=\"" + this.styleTr + "\">");
        this.out.println("  <th align=\"left\" valign=\"top\" class=\""
                + this.styleTh + "\" colspan=\"2\">"
                + this.record.get("title").toString() + "&nbsp;</th>");
        this.out.println("</tr>");

        if (displayType == 0
                && (!this.settings.section
                        .equals(this.settings.entrySchemaSection))) {
            this.out.println("<tr class=\"" + this.styleTr + "\">");
            this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                    + this.styleTdLabel + "\">"
                    + this.rb.getMessage("sos.settings.dialog.label_value")
                    + "&nbsp;</td>");
            this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                    + this.styleTd + "\">");

            this.showDialogValue(this.record, 0, 0);
            
            this.showUpload();

            this.out.println("  &nbsp;</td>");
            this.out.println("</tr>");
            if (this.record.get("display_type").toString().equals("0")) {
                defaultJavascript.append("document." + this.form
                        + ".value.focus();");
            }
        }

        this.out.println("<tr class=\"" + this.styleTr + "\">");

        if (this.applicationType == 0
                || this.settings.section
                        .equals(this.settings.entrySchemaSection)) {

            if (this.sectionType == 0) {

                this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                        + this.styleTdLabel + "\">Name&nbsp;</td>");
                this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                        + this.styleTd + "\">");
                if (this.enableEntryManager == true) {
                    this.out
                            .println("   <input "
                                    + this.disabled
                                    + " name=\"name\" type=\"text\" size=\"80\" maxlength=\"100\" class=\""
                                    + this.styleInput + "\" value=\""
                                    + this.record.get("name").toString()
                                    + "\">");
                    if (defaultJavascript.length() == 0) {
                        defaultJavascript.append("document." + this.form
                                + ".name.focus();");
                    }
                } else {
                    this.out
                            .println("    <input name=\"name\" type=\"hidden\" value=\""
                                    + this.record.get("name")
                                    + "\">"
                                    + this.record.get("name").toString());
                }
                this.out.println("  &nbsp;</td>");
            } else {
                this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                        + this.styleTdLabel + "\">"
                        + this.rb.getMessage("sos.settings.dialog.label_title")
                        + "&nbsp;</td>");
                this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                        + this.styleTd + "\">");
                this.out
                        .println("    <input "
                                + this.disabled
                                + " name=\"title\" type=\"text\" size=\"80\" maxlength=\"100\" class=\""
                                + this.styleInput + "\" value=\""
                                + this.record.get("title").toString() + "\">");
                this.out
                        .println("    <input type=\"hidden\" name=\"name\"      value=\""
                                + this.record.get("name").toString() + "\">");
                this.out.println("  &nbsp;</td>");
                if (defaultJavascript.length() == 0) {
                    defaultJavascript.append("document." + this.form
                            + ".title.focus();");
                }
            }
        }

        this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
        this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                + this.styleTdLabel + "\">"
                + this.rb.getMessage("sos.settings.dialog.label_modified")
                + "&nbsp;</td>");
        this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                + this.styleTd
                + "\">"
                + SOSDate.getLocaleDateTimeAsString(this.record.get("modified")
                        .toString()) + "&nbsp;"
                + this.record.get("modified_by").toString() + "&nbsp;</td>");
        this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
        this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                + this.styleTdLabel + "\">"
                + this.rb.getMessage("sos.settings.dialog.label_created")
                + "&nbsp;</td>");
        this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                + this.styleTd
                + "\">"
                //+this.record.get("created").toString()
                + SOSDate.getLocaleDateTimeAsString(this.record.get("created")
                        .toString()) + "&nbsp;" + this.record.get("created_by")
                + "&nbsp;</td>");
        this.out.println("</tr><tr class=\"" + this.styleTr + "\">");

        if (displayMode == 1
                && (this.sectionType == 0 || displayType == 1 || (this.settings.section
                        .equals(this.settings.entrySchemaSection) && this.sectionType == 1))) {
            this.out.println("  <th align=\"left\" valign=\"top\" class=\""
                    + this.styleTh + "\" colspan=\"2\">");
            if (displayType == 2) {
                this.out.println(this.rb
                        .getMessage("sos.settings.dialog.label_schema_range")
                        + "&nbsp;</th>");
            } else if (displayType == 1) {
                this.out.println(this.rb
                        .getMessage("sos.settings.dialog.label_schema_section")
                        + "&nbsp;</th>");
            } else {
                this.out.println(this.rb
                        .getMessage("sos.settings.dialog.label_schema_entry")
                        + "&nbsp;</th>");
            }

            this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
            this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                    + this.styleTdLabel + "\">"
                    + this.rb.getMessage("sos.settings.dialog.label_title")
                    + "&nbsp;</td>");
            this.out
                    .println("  <td width=\"80%\" valign=\"top\" class=\""
                            + this.styleTd
                            + "\"><input "
                            + this.disabled
                            + " type=\"text\" name=\"title\" size=\"80\" maxlength=\"100\" class=\""
                            + this.styleInput + "\" value=\""
                            + this.record.get("title").toString() + "\">"
                            + "&nbsp;</td>");
            this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
            this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                    + this.styleTdLabel + "\">"
                    + this.rb.getMessage("sos.settings.dialog.label_default")
                    + "&nbsp;</td>");
            this.out
                    .println("  <td width=\"80%\" valign=\"top\" class=\""
                            + this.styleTd
                            + "\"><input "
                            + this.disabled
                            + " type=\"text\" name=\"default_value\" size=\"80\" maxlength=\"250\" class=\""
                            + this.styleInput + "\" value=\""
                            + this.record.get("default_value").toString()
                            + "\">" + "&nbsp;</td>");
            this.out.println("</tr><tr class=\"" + this.styleTr + "\">");

            if (displayType > 0) {

                String[] input_type_apps = this.rb.getMessage(
                        "sos.settings.dialog.listbox_input_type_apps").split(
                        ";");
                if (input_type_apps.length != 2) { throw new Exception(
                        "\"sos.settings.dialog.listbox_input_type_apps\" expected two values, given : "
                                + input_type_apps.length); }

                this.out
                        .println("  <td width=\"20%\" valign=\"top\" class=\""
                                + this.styleTdLabel
                                + "\">"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_input_entries")
                                + "&nbsp;</td>");
                this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                        + this.styleTd
                        + "\">"
                        + this.rb
                                .getMessage("sos.settings.dialog.label_format")
                        + "&nbsp;<select " + this.disabled
                        + " name=\"entry_type\" size=\"1\" class=\""
                        + this.styleInput + "\">");
                this.out.println("     <option value=\"0\"");
                if (!this.record.get("entry_type").toString().equals("1")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_apps[0].trim() + "&nbsp;");
                this.out.println("     <option value=\"1\"");
                if (this.record.get("entry_type").toString().equals("1")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_apps[1].trim()
                        + "&nbsp;</select></td>");
                this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
            }

            if (displayType < 2) {

                String[] input_type_entries = this.rb.getMessage(
                        "sos.settings.dialog.listbox_input_type_entries")
                        .split(";");
                if (input_type_entries.length != 7) { throw new Exception(
                        "\"sos.settings.dialog.listbox_input_type_entries\" expected 7 values, given : "
                                + input_type_entries.length); }

                labelDocument = input_type_entries[5];

                String[] display_type_entries = this.rb.getMessage(
                        "sos.settings.dialog.listbox_display_type_entries")
                        .split(";");
                if (display_type_entries.length != 5) { throw new Exception(
                        "\"sos.settings.dialog.listbox_display_type_entries\" expected 5 values, given : "
                                + display_type_entries.length); }

                this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                        + this.styleTdLabel + "\">"
                        + this.rb.getMessage("sos.settings.dialog.label_input")
                        + "&nbsp;</td>");
                this.out
                        .println("  <td width=\"80%\" valign=\"top\" class=\""
                                + this.styleTd
                                + "\">"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_format")
                                + "&nbsp;<select "
                                + this.disabled
                                + " name=\"input_type\" class=\""
                                + this.styleInput
                                + "\" onChange=\"sos_settings_input_type_onChange()\">");
                this.out.println("     <option value=\"0\"");
                if (this.record.get("input_type").toString().equals("0")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_entries[0].trim() + "&nbsp;");

                this.out.println("     <option value=\"1\"");
                if (this.record.get("input_type").toString().equals("1")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_entries[1].trim() + "&nbsp;");

                this.out.println("     <option value=\"2\"");
                if (this.record.get("input_type").toString().equals("2")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_entries[2].trim() + "&nbsp;");

                this.out.println("     <option value=\"3\"");
                if (this.record.get("input_type").toString().equals("3")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_entries[3].trim() + "&nbsp;");

                this.out.println("     <option value=\"4\"");
                if (this.record.get("input_type").toString().equals("4")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_entries[4].trim() + "&nbsp;");

                this.out.println("     <option value=\"5\"");
                if (this.record.get("input_type").toString().equals("5")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_entries[5].trim() + "&nbsp;");

                this.out.println("     <option value=\"6\"");
                if (this.record.get("input_type").toString().equals("6")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_entries[6].trim() + "&nbsp;");
                
                this.out.println("</select>");
                this.out
                        .println("&nbsp;&nbsp;"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_number_of_tokens")
                                + "&nbsp;<input "
                                + this.disabled
                                + " type=\"text\" name=\"input_size\" size=\"10\" maxlength=\"4\" class=\""
                                + this.styleInput + "\" value=\""
                                + this.record.get("input_size").toString()
                                + "\">");
                String checked = (!this.record.get("forced").toString().equals(
                        "0")) ? " checked" : "";
                String unchecked = (!checked.equals("")) ? "" : " checked";
                this.out
                        .println("&nbsp;&nbsp;"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_mandatory")
                                + "&nbsp;");

                this.out.println("<input " + this.disabled
                        + " type=\"radio\" name=\"forced\" class=\""
                        + this.styleInput + "\" value=\"1\" " + checked + ">"
                        + this.rb.getMessage("sos.settings.dialog.label_yes")
                        + "&nbsp;");
                this.out.println("<input " + this.disabled
                        + " type=\"radio\" name=\"forced\" class=\""
                        + this.styleInput + "\" value=\"0\" " + unchecked + ">"
                        + this.rb.getMessage("sos.settings.dialog.label_no")
                        + "&nbsp;");
                this.out.println("    &nbsp;</td>");

                this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
                this.out
                        .println("  <td width=\"20%\" valign=\"top\" class=\""
                                + this.styleTdLabel
                                + "\">"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_display")
                                + "&nbsp;</td>");
                this.out
                        .println("  <td width=\"80%\" valign=\"top\" class=\""
                                + this.styleTd
                                + "\">Format&nbsp;<select "
                                + this.disabled
                                + " name=\"display_type\" class=\""
                                + this.styleInput
                                + "\" onChange=\"sos_settings_display_type_onChange()\">");
                this.out.println("     <option value=\"0\"");
                if (this.record.get("display_type").toString().equals("0")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + display_type_entries[0].trim()
                        + "&nbsp;");
                this.out.println("     <option value=\"1\"");
                if (this.record.get("display_type").toString().equals("1")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + display_type_entries[1].trim()
                        + "&nbsp;");
                this.out.println("     <option value=\"2\"");
                if (this.record.get("display_type").toString().equals("2")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + display_type_entries[2].trim()
                        + "&nbsp;");
                
                this.out.println("     <option value=\"3\"");
                if (this.record.get("display_type").toString().equals("3")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + display_type_entries[3].trim()+"&nbsp;");
                
                this.out.println("     <option value=\"4\"");
                if (this.record.get("display_type").toString().equals("4")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + display_type_entries[4].trim()+"&nbsp;");
                
                this.out.println("</select>");
                this.out
                        .println("&nbsp;&nbsp;"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_number_of_pixel")
                                + "&nbsp;<input "
                                + this.disabled
                                + " type=\"text\" name=\"display_size\" size=\"10\" maxlength=\"4\" class=\""
                                + this.styleInput + "\" value=\""
                                + this.record.get("display_size").toString()
                                + "\">");
                this.out.println("    &nbsp;</td>");
                this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
            }

            if (displayType == 0 || displayType == 1) {
                if (this.hasWriteRight) {
                    String height = (this.enableEditor) ? "height=\""
                            + this.editorHeight + "\"" : "";

                    this.out
                            .println("  <td width=\"20%\" "
                                    + height
                                    + " valign=\"top\" class=\""
                                    + this.styleTdLabel
                                    + "\">"
                                    + this.rb
                                            .getMessage("sos.settings.dialog.label_description")
                                    + "&nbsp;</td>");
                    this.out
                            .println("  <td width=\"80%\" " + height
                                    + " valign=\"top\" class=\"" + this.styleTd
                                    + "\">");

                    if (this.enableEditor) {
                        this.showEditor(this.record.get("documentation")
                                .toString());
                    } else {

                        this.out
                                .println("    <textarea rows=\"5\" cols=\"70\" wrap=\"off\" name=\"documentation\" class=\""
                                        + this.styleInput
                                        + "\">"
                                        + this.htmlSpecialChars(this.record
                                                .get("documentation")
                                                .toString())
                                        + "</textarea>&nbsp;");
                    }
                    this.out.println("</td>");
                    this.out.println("</tr>");
                    this.out.println("<tr class=\"" + this.styleTr + "\">");
                } else {
                    this.out
                            .println("  <td width=\"20%\" valign=\"top\" class=\""
                                    + this.styleTdLabel
                                    + "\">"
                                    + this.rb
                                            .getMessage("sos.settings.dialog.label_description")
                                    + "&nbsp;</td>");
                    this.out
                            .println("  <td width=\"80%\" valign=\"top\" class=\""
                                    + this.styleTd + "\">");
                    this.out.println(this.record.get("documentation")
                            .toString());
                    this.out.println("  &nbsp;</td>");
                    this.out.println("</tr>");
                    this.out.println("<tr class=\"" + this.styleTr + "\">");
                }
            }
        }

        this.out.println("  <td width=valign=\"top\" class=\"" + this.styleTd
                + "\">&nbsp;</td>");
        this.out
                .println("  <td valign=\"top\" class=\"" + this.styleTd + "\">");

        boolean isBreak = false;

        if (this.hasWriteRight == true) {
        	
        	this.out.println("<script language=\"javascript\">");
					this.out.println(" ");   		
    			this.out.println("	function checkImportFile(id){");
    			this.out.println(" ");	
    			this.out.println("		var btn_store 		= document.getElementById(id);");
    			this.out.println(" ");		
    			this.out.println("		var file_span			= document.getElementById('sos_settings_import');");
    			this.out.println("		var file_value 		= document.getElementById('input_import');");
    			this.out.println("		var default_value	= document."+this.form+".default_value;");
    			this.out.println(" ");		
					this.out.println("		if(typeof file_span == 'undefined' || typeof file_value == 'undefined' || file_span == null){");
					this.out.println("			// kein file upload");
					this.out.println("		}");
					this.out.println("		else{");
					this.out.println("			var display_style = file_span.style.display;");
					this.out.println("			if(typeof display_style != 'undefined' && display_style != 'none'){");
					this.out.println("				if(file_value.value != ''){");
					this.out.println("					btn_store.name = 'btn_import_file';");
					this.out.println("					if(id == 'btn_store_as'){");
					this.out.println("						document."+this.form+".last_action.value = 'new';");
					this.out.println("					}");
					this.out.println("				}");
					this.out.println("			}");	
					this.out.println("		}");
    			this.out.println("	}");
    			this.out.println(" ");
    			this.out.println("</script>");
        	
          if (this.isNew == true) {
                this.out
                        .println("    <input id=\"btn_store\" type=\"image\" name=\"btn_insert\" src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_insert.gif\"  alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_insert_alt")
                                + "\" onClick=\"document.sos_settings.button.value=\'insert\';checkImportFile('btn_store');\">&nbsp; ");
            } else {
                this.out
                        .println("    <input id=\"btn_store\" type=\"image\" name=\"btn_store\"  src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_store.gif\"  alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_store_alt")
                                + "\" onClick=\"document.sos_settings.button.value=\'store\';checkImportFile('btn_store');\">&nbsp; ");
                if ((this.enableEntryManager == true)
                        && (this.applicationType == 0 || this.settings.section
                                .equals(this.settings.entrySchemaSection))) {
                    this.out
                            .println("    <input id=\"btn_store_as\" type=\"image\" name=\"btn_insert\" src=\""
                                    + this.imgDir
                                    + this.sosLang
                                    + "/btn_insert.gif\" alt=\""
                                    + this.rb
                                            .getMessage("sos.settings.dialog.btn_store_as_alt")
                                    + "\" onClick=\"document.sos_settings.button.value=\'insert\';checkImportFile('btn_store_as');\">&nbsp; ");
                    if (this.hasDeleteRight == true) {
                        this.out
                                .println("    <input type=\"image\" name=\"btn_delete\" src=\""
                                        + this.imgDir
                                        + this.sosLang
                                        + "/btn_delete.gif\" alt=\""
                                        + this.rb
                                                .getMessage("sos.settings.dialog.btn_delete_alt")
                                        + "\" onClick=\"document.sos_settings.button.value=\'delete\'; return confirm(\'"
                                        + this.rb
                                                .getMessage("sos.settings.dialog.confirm_delete_entry")
                                        + "\');\">&nbsp; ");
                    }
                    if (this.sectionType == 0) {
                        this.out
                                .println("    <input type=\"image\" name=\"btn_schema\" src=\""
                                        + this.imgDir
                                        + this.sosLang
                                        + "/btn_schema.gif\" alt=\""
                                        + this.rb
                                                .getMessage("sos.settings.dialog.btn_schema_alt")
                                        + "\" onClick=\"document.sos_settings.button.value=\'schema\';\">&nbsp; ");
                    }
                    this.out
                            .println("    <input type=\"image\" name=\"btn_duplicate\" src=\""
                                    + this.imgDir
                                    + this.sosLang
                                    + "/btn_duplicate.gif\" alt=\""
                                    + this.rb
                                            .getMessage("sos.settings.dialog.btn_duplicate_alt")
                                    + "\" onClick=\"document.sos_settings.button.value=\'duplicate\';\">&nbsp; ");
                    isBreak = true;
                }
            }

            this.out
                    .println("    <input type=\"image\" name=\"btn_cancel\" src=\""
                            + this.imgDir
                            + this.sosLang
                            + "/btn_cancel.gif\" alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_cancel_alt")
                            + "\" onClick=\"document.sos_settings.button.value=\'cancel\';\">&nbsp; ");
            this.out
                    .println("    <input type=\"image\" name=\"btn_reset\"  src=\""
                            + this.imgDir
                            + this.sosLang
                            + "/btn_reset.gif\"  alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_reset_alt")
                            + "\" onClick=\"document.sos_settings.reset(); return false;\">&nbsp; ");

        } else {
            this.out
                    .println("    <input type=\"image\" name=\"btn_cancel\" src=\""
                            + this.imgDir
                            + this.sosLang
                            + "/btn_cancel.gif\" alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_cancel_alt")
                            + "\">&nbsp; ");
        }

        this.out.println("  </td>");
        this.out.println("</tr>");
        this.showTableEnd();
        this.out.println("</form>");

        if (this.hasWriteRight == true) {

            StringBuffer confirm = new StringBuffer();
            StringBuffer confirm_vals = new StringBuffer();
            boolean hasEntriesLongValues = false;

            if (this.entriesLongValues != null
                    && this.entriesLongValues.size() > 0) {
                hasEntriesLongValues = true;

                for (int v = 0; v < this.entriesLongValues.size(); v++) {
                    HashMap hm = (HashMap) this.entriesLongValues.get(v);
                    confirm_vals
                            .append(this.rb
                                    .getMessage(
                                            "sos.settings.dialog.confirm_change_schema_long_value_sub",
                                            hm.get("section").toString(), hm
                                                    .get("name").toString()));
                }
                confirm.append(this.rb.getMessage(
                        "sos.settings.dialog.confirm_change_schema_long_value",
                        labelDocument.trim(), confirm_vals.toString()));
            }

            this.out
                    .println("<script language=\"JavaScript\" type=\"text/javascript\">");
            if (defaultJavascript.length() != 0) {
                this.out.println(defaultJavascript.toString());
            }
            /////////////////////////////////////////////////
            this.out.println("function sos_settings_input_type_onChange()  {");
            this.out.println(" ");
            this.out.println("	if(typeof document." + this.form
                    + ".value   != \"undefined\"){");
            this.out.println("	  document." + this.form
                    + ".value.style.display   										= '';");
            this.out
                    .println("	  document.getElementById(\"sos_settings_import\").style.display  = 'none';");
            this.out.println("	}");
            this.out.println(" ");
            this.out.println("	var it = document.sos_settings.input_type;");
            this.out.println("	var is = document.sos_settings.input_size;");
            this.out.println("	var iv = it.options[it.selectedIndex].value;");
            this.out.println("	var dt = document.sos_settings.display_type;");
            this.out.println("	var ds = document.sos_settings.display_size;");
            this.out.println("	var dv = dt.options[dt.selectedIndex].value;");
            this.out.println(" ");
            this.out.println("	//alert(iv+\"=\"+dv);");
            this.out.println(" ");
            this.out.println("	switch (iv) {");
            this.out
                    .println("	  case '0'  :   if (dv == '2' || dv == '4'  ) { ");
            this.out.println("	                  dt.value = '0';");
            this.out.println("	                }");
            this.out.println("	                else if (dv == '3') {");
            this.out.println("	                  ds.value = '"
                    + this.defaultDisplaySize + "';");
            this.out.println("	                  is.value = '"
                    + this.defaultInputSize + "';");
            this.out.println("	                }");
            if (hasEntriesLongValues) {
                this.out.println("	                  if(confirm('"
                        + confirm.toString() + "') == false){");
                if (this.record.get("input_type").toString().equals("5")) {
                    this.out.println("	                      it.value  = '5';");
                    this.out.println("	                      dt.value  = '3';");
                    this.out.println("	                      is.value  = '';");
                    this.out
                            .println("	                      ds.value  = '100';");
                } else {
                    this.out.println("	                      it.value  = '6';");
                    this.out.println("	                      dt.value  = '4';");
                    this.out.println("	                      is.value  = '';");
                    this.out.println("	                      ds.value  = '';");
                }
                this.out.println("	                  }");
                this.out.println(" ");
            }
            this.out.println(" ");
            this.out.println("	                break;");
            this.out.println(" ");
            this.out.println("	  case '1'  :");
            this.out.println("	  case '2'  :");
            this.out
                    .println("	  case '4'  :   if (dv == '2' || dv == '4') { dt.value = '0'; }");
            if (hasEntriesLongValues) {
                this.out.println("	                  if(confirm('"
                        + confirm.toString() + "') == false){");
                if (this.record.get("input_type").toString().equals("5")) {
                    this.out.println("	                      it.value  = '5';");
                    this.out.println("	                      dt.value  = '3';");
                    this.out.println("	                      is.value  = '';");
                    this.out
                            .println("	                      ds.value  = '100';");
                } else {
                    this.out.println("	                      it.value  = '6';");
                    this.out.println("	                      dt.value  = '4';");
                    this.out.println("	                      is.value  = '';");
                    this.out.println("	                      ds.value  = '';");
                }
                this.out.println("	                  }");
            }
            this.out.println(" ");
            this.out.println("	                break;");
            this.out.println(" ");
            this.out.println("	  case '3'  :   dt.value = '2';");
            if (hasEntriesLongValues) {
                this.out.println("	                  if(confirm('"
                        + confirm.toString() + "') == false){");
                if (this.record.get("input_type").toString().equals("5")) {
                    this.out.println("	                      it.value  = '5';");
                    this.out.println("	                      dt.value  = '3';");
                    this.out.println("	                      is.value  = '';");
                    this.out
                            .println("	                      ds.value  = '100';");
                } else {
                    this.out.println("	                      it.value  = '6';");
                    this.out.println("	                      dt.value  = '4';");
                    this.out.println("	                      is.value  = '';");
                    this.out.println("	                      ds.value  = '';");
                }
                this.out.println("	                  }");
            }
            this.out.println(" ");
            this.out.println("	                break;");
            this.out.println(" ");
            this.out.println("	  case '5'  :");
            this.out.println("	                if(dt.value == '4'){");
            this.out.println("	                  if(typeof document."
                    + this.form + ".value != \"undefined\"){");
            this.out
                    .println("	                    document.getElementById(\"sos_settings_import\").style.display  = '';");
            this.out.println("	                    document." + this.form
                    + ".value.style.display                			= 'none';");
            this.out.println("	                  }");
            this.out.println("	                  is.value  = '';");
            this.out.println("	                  ds.value  = '';");
            this.out.println("	                }");
            this.out.println("	                else{");
            this.out.println("	                  dt.value  = '3';");
            this.out.println("	                  is.value  = '';");
            this.out.println("	                  ds.value  = '100';");
            this.out.println(" ");
            this.out.println("	                  //!!!!!!!!!!!!!");
            this.out.println("	                  if(typeof document."
                    + this.form + ".value != \"undefined\"){");
            this.out.println("	                    document." + this.form
                    + ".value.style.display                = '';");
            this.out
                    .println("	                    document.getElementById(\"sos_settings_import\").style.display  = 'none';");
            this.out.println("	                  }");
            this.out.println("	                }");
            this.out.println(" ");
            this.out.println("	                break;");
            this.out.println(" ");
            this.out.println("	  case '6'    :");
            this.out.println("	                dt.value = '4';");
            this.out.println(" ");
            this.out.println("	                if(typeof document." + this.form
                    + ".value != \"undefined\"){");
            this.out
                    .println("	                  document.getElementById(\"sos_settings_import\").style.display  = '';");
            this.out.println("	                  document." + this.form
                    + ".value.style.display                			= 'none';");
            this.out.println("	                }");
            this.out.println(" ");
            this.out.println("	                is.value  = '';");
            this.out.println("	                ds.value  = '';");
            this.out.println(" ");
            this.out.println("	                break;");
            this.out.println("	}// switch");
            this.out.println("}//function");
            this.out.println(" ");
            this.out.println(" ");
            this.out.println(" ");
            this.out.println("function sos_settings_display_type_onChange() {");
            this.out.println(" ");
            this.out.println("  var it = document.sos_settings.input_type;");
            this.out.println("  var iv = it.options[it.selectedIndex].value;");
            this.out.println("  var dt = document.sos_settings.display_type;");
            this.out.println("  var dv = dt.options[dt.selectedIndex].value;");
            this.out.println("  var ds = document.sos_settings.display_size;");
            this.out.println("  var is = document.sos_settings.input_size;");
            this.out.println(" ");
            this.out.println("  //alert(dv+'='+it.value);");
            this.out.println(" ");
            this.out.println("  switch (dv) {");
            this.out.println("    case '0' :");
            this.out.println("    case '1' :");
            this.out.println("    case '3' :");
            this.out.println("                if(it.value == '6'){");
            this.out.println("                  dt.selectedIndex = 4;");
            this.out.println("                }");
            this.out.println("                else if(it.value == '5'){");
            this.out.println("                  //!!!!!!!!!!!");
            this.out.println("                  if(typeof document."
                    + this.form + ".value != \"undefined\"){");
            this.out.println("                    document." + this.form
                    + ".value.style.display                = '';");
            this.out
                    .println("                    document.getElementById(\"sos_settings_import\").style.display  = 'none';");
            this.out.println("                  }");
            this.out.println("                  dt.selectedIndex  = 3;");
            this.out.println("                  ds.value          = '100';");
            this.out.println("                  is.value          = '';");
            this.out.println("                }");
            this.out.println(" ");
            this.out.println("                break;");
            this.out.println(" ");
            this.out.println("    case '2'  :");
            this.out.println("                if(it.value == '6'){");
            this.out.println("                  dt.selectedIndex = 4;");
            this.out.println("                }");
            this.out.println("                else if(it.value == '5'){");
            this.out.println("                  dt.selectedIndex = 3;");
            this.out.println("                }");
            this.out.println("                else{");
            this.out.println("                  it.value = '3';");
            this.out.println("                }");
            this.out.println(" ");
            this.out.println("                break;");
            this.out.println(" ");
            this.out.println("    case '4'  :");
            this.out.println(" ");
            this.out
                    .println("                if(it.value == '6' || it.value == '5'){");
            this.out.println("                  dt.selectedIndex = 4;");
            this.out.println("                  ds.value          = '';");
            this.out.println("                  is.value          = '';");
            this.out.println("                }");
            this.out.println("                else{");
            this.out.println("                  it.value = '6';");
            this.out.println("                }");
            this.out.println("                if(typeof document." + this.form
                    + ".value != \"undefined\"){");
            this.out
                    .println("                  document.getElementById(\"sos_settings_import\").style.display  = '';");
            this.out.println("                  document." + this.form
                    + ".value.style.display                			= 'none';");
            this.out.println("                }");
            this.out.println(" ");
            this.out.println("                break;");
            this.out.println("  } // switch");
            this.out.println("}// function");
            this.out.println(" ");
            this.out.println(" ");
            this.out.println("function sos_settings_onSubmit() {");
            this.out.println("  var submitFlag = true;");
            this.out.println("  var checkFlag  = false;");
            this.out.println(" ");
            this.out.println("  var dt = document.sos_settings.display_type;");
            this.out.println("  if(typeof dt.options != 'undefined'){");
            this.out.println(" 		var dv = dt.options[dt.selectedIndex];");
            this.out.println(" 	}");
            this.out.println(" ");
            this.out.println("  switch(document.sos_settings.button.value) {");
            this.out.println("    case 'import'           :");
            this.out.println("    case 'schema'           :");
            this.out.println("    case 'clipboard_copy'   :");
            this.out.println("    case 'clipboard_paste'  :");
            this.out.println("    case 'cancel'           :");
            this.out
                    .println("    case 'reset'            : return submitFlag;");
            this.out.println("  }");
            this.out.println(" ");
            if (this.applicationType == 0
                    || this.settings.section
                            .equals(this.settings.entrySchemaSection)) {
                if (this.sectionType == 0) {
                    this.out
                            .println("      if(submitFlag && ( document.sos_settings.name.value == null || document.sos_settings.name.value == '' )){");
                    this.out
                            .println("        alert(\""
                                    + this.rb
                                            .getMessage("sos.settings.dialog.alert_name_empty")
                                    + "\");");
                    this.out
                            .println("        document.sos_settings.name.focus();");
                    this.out.println("        submitFlag = false;");
                    this.out.println("      }");
                    this.out
                            .println("      if(submitFlag && ( document.sos_settings.title.value == null || document.sos_settings.title.value == '')){");
                    this.out
                            .println("        alert(\""
                                    + this.rb
                                            .getMessage("sos.settings.dialog.alert_title_empty")
                                    + "\");");
                    this.out
                            .println("        document.sos_settings.title.focus();");
                    this.out.println("        submitFlag = false;");
                    this.out.println("      }");
                    this.out.println(" ");
                    this.out.println(" 			if(typeof dv != 'undefined'){");
                    this.out.println("      	if(submitFlag && ( dv.value == '4' && document.sos_settings.default_value.value == '')){");
                    this.out.println("          alert(\""+ this.rb.getMessage("sos.settings.dialog.alert_binary_default_value_empty")+ "\");");
                    this.out.println("          document.sos_settings.default_value.focus();");
                    this.out.println("          submitFlag = false;");
                    this.out.println("      	}");
                    this.out.println(" 			}");
                    this.out.println(" ");
                }
            }

            this.out
                    .println("  if(document.sos_settings.forced.type == 'hidden' ){");
            this.out
                    .println("    checkFlag = (document.sos_settings.forced.value == '1' );");
            this.out.println("  }");
            this.out.println("  else{");
            this.out
                    .println("    checkFlag = document.sos_settings.forced[0].checked;");
            this.out.println("  }");

            if (displayType == 0
                    && !this.settings.section
                            .equals(this.settings.entrySchemaSection)) {

                this.out
                        .println("    if(submitFlag && checkFlag && ( document.sos_settings.value.value == null || document.sos_settings.value.value == '')){");
                this.out
                        .println("      alert('"+this.rb.getMessage("sos.settings.dialog.alert_value_empty")+"');");
                this.out.println("      document.sos_settings.value.focus();");
                this.out.println("      submitFlag = false;");
                this.out.println("    }");

            }

            this.out.println("return submitFlag;");
            this.out.println("}");
            this.out.println("</script>");
            ////////////////////////////////777
        }

        return true;
    }

    /**
     * Datensatz anlegen
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */

    private boolean recordCreate() throws Exception {
        this.debug(3, "recordCreate");

        this.record = new HashMap();

        Date date = new Date();

        this.record.put(this.settings.entryApplication.toLowerCase(),
                this.settings.application);
        this.record.put(this.settings.entrySection.toLowerCase(),
                this.settings.section);
        this.record.put("name", "");
        this.record.put("value", "");
        this.record.put("default_value", "");
        this.record.put("documentation", "");
        this.record.put("long_value", "");
        this.record.put("title", "");
        this.record.put("forced", "0");
        this.record.put("entry_type", "0");
        this.record.put("created", this.dateFormat.format(date));
        this.record.put("created_by", this.settings.author);
        this.record.put("modified", this.dateFormat.format(date));
        this.record.put("modified_by", this.settings.author);

        int ok = 0;
        if (!this.range.equals("application")) {
            ok = 1;
        }

        if (ok == 1) {
            if (!this.settings.section.equals(this.settings.entrySchemaSection)) {
                ok = 1;
            } else {
                ok = 0;
            }
        }

        if (ok == 1) {
            StringBuffer sql = new StringBuffer();
            sql
                    .append(" select \"INPUT_TYPE\", \"INPUT_SIZE\", \"DISPLAY_TYPE\", \"DISPLAY_SIZE\", \"FORCED\", \"ENTRY_TYPE\", \"DEFAULT_VALUE\" ");
            sql.append(" from " + this.settings.source + " ");
            sql.append(" where  \"" + this.settings.entryApplication + "\"  = "
                    + this.dbQuoted(this.settings.application) + " and ");
            sql.append("        \"" + this.settings.entrySection + "\"      = "
                    + this.dbQuoted(this.settings.section) + " and ");
            sql.append("        \"" + this.settings.entryName + "\"         = "
                    + this.dbQuoted(this.settings.section));

            try {
                HashMap defaults = this.connection.getSingle(sql.toString());
                if (defaults.size() > 0) {
                    this.record.put("input_type", defaults.get("input_type")
                            .toString());
                    this.record.put("input_size", defaults.get("input_size")
                            .toString());
                    this.record.put("display_type", defaults
                            .get("display_type").toString());
                    this.record.put("display_size", defaults
                            .get("display_size").toString());
                    this.record
                            .put("forced", defaults.get("forced").toString());
                    this.record.put("entry_type", defaults.get("entry_type")
                            .toString());
                } else {
                    ok = 0;
                }
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                return false;
            }
        }

        if (ok == 0) {
            this.record.put("input_type", "0");
            this.record.put("input_size", this.defaultInputSize);
            this.record.put("display_type", "0");
            this.record.put("display_size", this.defaultDisplaySize);
            this.record.put("forced", "0");
        }

        return true;
    }

    /**
     * Alle Sektionen einer Applikation anzeigen
     * 
     * @param sectionsTitle
     *            Titel
     * @param sections
     *            Werte (siehe getDialogSections)
     * @return boolean Fehlerzustand
     * @throws Exception
     * @see #showDialogSections(String, Vector)
     */

    private boolean showDialogSections(String sectionsTitle, Vector sections)
            throws Exception {

        this.debug(3, "showDialogSections : sectionsTitle = " + sectionsTitle
                + " sections = " + sections);

        if (sectionsTitle != null) {
            this.dialogSectionsTitle = sectionsTitle;
        }
        if (sections != null) {
            this.dialogSections = sections;
        }

        if (this.dialogSections == null) {
            this.dialogSections = new Vector();
        }

        if (this.dialogSections.size() == 0) {
            if (this.error() == false) {
                this
                        .setError(
                                this.rb
                                        .getMessage("sos.settings.dialog.err_not_found_sections"),
                                SOSClassUtil.getMethodName());
            }

        }

        String imgLink = "<img src=\"" + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"4\" vspace=\"1\">";

        String thisCon = (this.site.indexOf('?') != -1) ? "&" : "?";
        String querySession = "?";
        if (!this.sessionUseTransSID && !this.sessionVAR.equals("")) {
            querySession = thisCon + this.sessionVAR + "=" + this.sessionID
                    + "&";
        } else {
            querySession = thisCon;
        }

        this.showTableBegin();
        this.showNavigation(new Integer(1), this.dialogApplicationIndex, null,
                null);

        this.aclRange = "application";
        try {
            this.getRights(this.settings.application, null, null);
        } catch (Exception e) {
            this.setError("ACL : " + e.getMessage(), SOSClassUtil
                    .getMethodName());
        }

        // JS Code fir's Help schreiben
        this.openHelpWin();
        String link = "";
        if (this.dialogApplicationIndex.intValue() >= 0
                && this.enableSectionManager == true) {
            if (this.applicationType == 1) { // Schema
                if (this.hasReadRight == true || this.hasWriteRight == true
                        || this.hasCreateRight == true) {
                    this.out.println("<tr class=\"" + this.styleTr + "\">");
                    link = "<a class=\""
                            + this.styleLinkNavigation
                            + "\" href=\""
                            + this.site
                            + querySession
                            + "action=new&range=section&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "&application_type=" + this.applicationType
                            + "\">";
                    this.out.println("    <td class=\"" + this.styleTd + "\">"
                            + link + imgLink + this.dialogSectionsNewTitle
                            + "</a></td>");
                    this.out.println("  </tr>");
                }
            } else {
                if (this.hasCreateRight == true) {

                    this.out.println("  <tr class=\"" + this.styleTr + "\">");
                    link = "<a class=\""
                            + this.styleLinkNavigation
                            + "\" href=\""
                            + this.site
                            + querySession
                            + "action=new&range=section&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "&application_type=" + this.applicationType
                            + "\">";
                    this.out.println("    <td class=\"" + this.styleTd + "\">"
                            + link + imgLink + this.dialogSectionsNewTitle
                            + "</a></td>");
                    this.out.println("  </tr>");
                }
            }
        }

        String sqlHelp = " select \"APPLICATION\",\"SECTION\", \"NAME\" ";
        sqlHelp += " from " + this.settings.source + " s ";
        sqlHelp += " where s.\"" + this.settings.entryApplication + "\"  = "
                + this.dbQuoted(this.settings.application) + "  and ";
        sqlHelp += "       s.\"" + this.settings.entryApplication
                + "\"  <> s.\"" + this.settings.entrySection + "\" and ";
        sqlHelp += "       s.\"" + this.settings.entrySection + "\"    =  s.\""
                + this.settings.entryName + "\" and ";
        sqlHelp += "       s.\"DOCUMENTATION\" is not null ";

        Hashtable helpTexts = new Hashtable();

        try {
            Vector helpText = this.connection.getArrayAsVector(sqlHelp);
            if (helpText.size() > 0) {
                for (Enumeration el = helpText.elements(); el.hasMoreElements();) {
                    HashMap hm = (HashMap) el.nextElement();
                    helpTexts.put(hm.get("application").toString()
                            + hm.get("section").toString()
                            + hm.get("name").toString(), "Help");
                }
                this.hasHelps = true;
            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

        for (Enumeration el = this.dialogSections.elements(); el
                .hasMoreElements();) {
            HashMap section = (HashMap) el.nextElement();

            String linkHelp = "&nbsp;";
            String linkEntries = "";
            String linkSection = "";
            boolean hasRights = true;
            boolean isSchema = false;

            if (section.get("name").toString().equals(
                    this.settings.entrySchemaSection)) {

                isSchema = true;

                if (this.hasCreateRight == true) { // Schema bearbeiten
                    linkEntries = "<a class=\""
                            + this.styleLinkNavigation
                            + "\" href=\""
                            + this.site
                            + querySession
                            + "action=show&range=entries&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "&section="
                            + this.response.encodeURL(section.get(
                                    this.settings.entrySection.toLowerCase())
                                    .toString()) + "&application_type="
                            + this.applicationType + "&section_type="
                            + section.get("entry_type").toString() + "\">";

                    linkSection = linkEntries;
                } else {
                    hasRights = false;
                }
            } else {

                String k = this.settings.application
                        + section.get(this.settings.entrySection.toLowerCase())
                                .toString()
                        + section.get(this.settings.entrySection.toLowerCase())
                                .toString();

                if (helpTexts.containsKey(k)) {
                    String session = "''";
                    if (!this.sessionID.equals("")) {
                        session = "'" + this.sessionID + "'";
                    }
                    String helpHref = "source="
                            + this.settings.source
                            + "&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "&section="
                            + this.response.encodeURL(section.get(
                                    this.settings.entrySection.toLowerCase())
                                    .toString())
                            + "&entry="
                            + this.response.encodeURL(section.get(
                                    this.settings.entrySection.toLowerCase())
                                    .toString());
                    linkHelp = "<a href=\"javascript:openHelpWin('"
                            + helpHref
                            + "',"
                            + session
                            + ");\"><img src=\""
                            + this.imgDir
                            + this.imgHelp
                            + "\" border=\"0\" title=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_help")
                            + "\"></a>";
                }

                linkEntries = "<a href=\""
                        + this.site
                        + querySession
                        + "action=show&range=entries&application="
                        + this.response.encodeURL(this.settings.application)
                        + "&section="
                        + this.response.encodeURL(section.get(
                                this.settings.entrySection.toLowerCase())
                                .toString()) + "&application_type="
                        + this.applicationType + "&section_type="
                        + section.get("entry_type").toString() + "\">";
                linkSection = (this.enableSectionManager == true) ? "<a href=\""
                        + this.site
                        + querySession
                        + "action=show&range=section&application="
                        + this.response.encodeURL(this.settings.application)
                        + "&section="
                        + this.response.encodeURL(section.get(
                                this.settings.entrySection.toLowerCase())
                                .toString()) + "\">"
                        : linkEntries;
            }

            this.aclRange = "section";
            try {
                this.getRights(section.get("application").toString(), section
                        .get("section").toString(), null);
                if (this.hasReadRight == false && this.hasCreateRight == false) {
                    continue;
                }
            } catch (Exception e) {
                this.out.println("  <tr class=\"" + this.styleTr + "\">");
                this.out.println("    <td class=\"" + this.styleTd + "\">");
                this.out
                        .println("    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                this.out.println("    <tr>");
                this.out.println("     <td class=\"" + this.styleTd
                        + "\" width=\"50%\">");
                this.out.println("       "
                        + section.get(
                                this.settings.entrySettingTitle.toLowerCase())
                                .toString());
                this.out.println("     </td>");
                this.out.println("     <td>");
                this.showError("ACL : " + e.getMessage());
                this.out.println("			</td>");
                this.out.println("    </tr>");
                this.out.println("    </table>");
                this.out.println("    </td>");
                this.out.println("  </tr>");

                continue;
            }

            if (hasRights) {

                String sectionTitle = (isSchema) ? this.rb
                        .getMessage("sos.settings.dialog.dialog_sections_schema_title")
                        : section.get(
                                this.settings.entrySettingTitle.toLowerCase())
                                .toString();

                this.out.println("  <tr class=\"" + this.styleTr + "\">");
                this.out.println("    <td class=\"" + this.styleTd + "\">");
                this.out
                        .println("    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                this.out.println("    <tr>");
                this.out.println("     <td class=\"" + this.styleTd
                        + "\" width=\"50%\">");
                this.out.println("       " + linkSection + imgLink + "</a>"
                        + linkEntries + sectionTitle + "</a>");
                this.out.println("     </td>");
                this.out.println("     <td>" + linkHelp + "</td>");
                this.out.println("    </tr>");
                this.out.println("    </table>");
                this.out.println("    </td>");
                this.out.println("  </tr>");
            }

        }// enumeration

        this.showActions(new Integer(1), this.dialogApplicationIndex, null,
                null);

        this.showTableEnd();

        return true;
    }

    /**
     * Datensatz lesen
     * 
     * @param readDocumentation
     *            true-Blob lesen
     * @return boolean Fehlerzustand
     * @throws Exception
     * @see #recordGetKey(boolean)
     */

    private boolean recordGetKey(boolean readDocumentation) throws Exception {

        this
                .debug(3, "recordGetKey : readDocumentation = "
                        + readDocumentation);

        boolean autoSetLongValues = false;
        if (this.enableAutoSetLongValues && this.range.equals("entry")) {
            autoSetLongValues = true;
        }

        StringBuffer sql = new StringBuffer();

        sql
                .append(" select \"APPLICATION\",\"SECTION\",\"NAME\",\"VALUE\",\"DEFAULT_VALUE\",\"TITLE\",\"INPUT_TYPE\",\"INPUT_SIZE\",\"DISPLAY_TYPE\",\"DISPLAY_SIZE\",\"FORCED\",\"ENTRY_TYPE\",\"CREATED\",\"CREATED_BY\",\"MODIFIED\",\"MODIFIED_BY\" ");
        sql.append(" from " + this.settings.source + " ");
        sql.append(" where \"" + this.settings.entryApplication + "\" = "
                + this.dbQuoted(this.settings.application) + " and ");
        sql.append("  \"" + this.settings.entrySection + "\" = "
                + this.dbQuoted(this.settings.section) + " and ");
        sql.append("  \"" + this.settings.entryName + "\" = "
                + this.dbQuoted(this.settings.entry));

        try {
            this.record = this.connection.getSingle(sql.toString());

            this.record
                    .put("original_name", this.record.get("name").toString());
            this.record.put("long_value", "");
            this.record.put("documentation", "");

            if (this.record.size() == 0) {
                return false;
            } else {
                if (readDocumentation == true) {
                    try {
                        byte[] documentation = this.connection
                                .getBlob("select \"DOCUMENTATION\" from "
                                        + this.settings.source
                                        + " where \"APPLICATION\"="
                                        + this
                                                .dbQuoted(this.settings.application)
                                        + " and \"SECTION\"="
                                        + this.dbQuoted(this.settings.section)
                                        + " and \"NAME\" ="
                                        + this.dbQuoted(this.settings.entry));
                        if (documentation != null) {
                            this.record.put("documentation", new String(
                                    documentation));
                        } else {
                            this.record.put("documentation", "");
                        }

                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                        return false;
                    }
                }

                if (autoSetLongValues) {
                    this.setDialogEntriesLongValues();
                } else {
                    if (this.record.get("input_type").toString().equals("5")) { // Dokument
                        try {
                            byte[] longValue = this.connection
                                    .getBlob("select \"LONG_VALUE\" from "
                                            + this.settings.source
                                            + " where \"APPLICATION\"="
                                            + this
                                                    .dbQuoted(this.settings.application)
                                            + " and \"SECTION\"="
                                            + this
                                                    .dbQuoted(this.settings.section)
                                            + " and \"NAME\" ="
                                            + this
                                                    .dbQuoted(this.settings.entry));
                            if (longValue != null && longValue.length > 0) {
                                this.record.put("value", new String(longValue));

                                if (this.record.get("display_type").toString().equals("4")) { // hidden
                                    this.hasBinaryValue.put(this.record.get(
                                            "application").toString()
                                            + this.record.get("section")
                                                    .toString()
                                            + this.record.get("name")
                                                    .toString(), "1");
                                }

                            } 
                            else {
                                this.record.put("value", "");
                            }
                            this.record.put("long_value", "");

                        } catch (Exception e) {
                            this.setError(e.getMessage(), SOSClassUtil
                                    .getMethodName());
                            return false;
                        }
                    } else if (this.record.get("input_type").toString().equals(
                            "6")) { // Dokument binär
                        String bv = this.connection
                                .getSingleValue("select count(\"APPLICATION\") from "
                                        + this.settings.source
                                        + " where \""
                                        + this.settings.entryApplication
                                        + "\" = "
                                        + this
                                                .dbQuoted(this.settings.application)
                                        + " and \""
                                        + this.settings.entrySection
                                        + "\" = "
                                        + this.dbQuoted(this.settings.section)
                                        + " and \""
                                        + this.settings.entryName
                                        + "\" = "
                                        + this.dbQuoted(this.settings.entry)
                                        + " and \"LONG_VALUE\" is not null");
                        this.hasBinaryValue.put(this.record.get("application")
                                .toString()
                                + this.record.get("section").toString()
                                + this.record.get("name").toString(), bv);
                    }
                }

                this.entriesLongValues = new Vector();

                if (this.applicationType > 0 && this.action.equals("schema")) {
                    this.entriesLongValues = this.connection
                            .getArrayAsVector("select \"SECTION\",\"NAME\" from "
                                    + this.settings.source
                                    + " where \""
                                    + this.settings.entryApplication
                                    + "\" = "
                                    + this.dbQuoted(this.settings.application)
                                    + " and \""
                                    + this.settings.entrySection
                                    + "\" <> '"
                                    + this.settings.entrySchemaSection
                                    + "' and \""
                                    + this.settings.entryName
                                    + "\" =  "
                                    + this.dbQuoted(this.settings.entry)
                                    + " and \""
                                    + this.settings.entryName
                                    + "\" <> \""
                                    + this.settings.entryApplication
                                    + "\" and \"LONG_VALUE\" is not null ");
                }

            }

        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            return false;
        }

        return true;
    }

    /**
     * Anzeige des Seitenkopfes
     * 
     * @param content
     *            Inhalt des Headers
     * @see #showDialogHeader(String)
     */

    private void showDialogHeader(String content) throws Exception {
        this.debug(3, "showDialogHeader : content = " + content);

        if (content != null) {
            this.headerContent = content;
        }

        this.out.println(this.headerContent);
    }

    /**
     * Alle Applikationen anzeigen
     * 
     * @param applicationsTitle
     *            Titel
     * @param applications
     *            Werte (siehe getDialogApplications)
     * @return boolean Fehlerzustand
     * @throws Exception
     * @see #showDialogApplications(String, Vector)
     */

    private boolean showDialogApplications(String applicationsTitle,
            Vector applications) throws Exception {

        this.debug(3, "showDialogApplications : applicationsTitle = "
                + applicationsTitle + " applications = " + applications);

        if (applicationsTitle != null) {
            this.dialogApplicationsTitle = applicationsTitle;
        }
        if (applications != null) {
            this.dialogApplications = applications;
        }

        if (this.dialogApplications == null) {
            this.dialogApplications = new Vector();
        }

        if (this.dialogApplications.size() == 0) {
            if (this.error() == false) {
                this.setError(this.rb
                        .getMessage("sos.settings.dialog.err_not_found_apps"),
                        SOSClassUtil.getMethodName());

            }
        }

        this.showTableBegin();
        this.showNavigation(new Integer(1), null, null, null);

        try {
            this.getTopLevelRights();
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            this.showTableEnd();
            return false;

        }

        if (!this.hasTopLevelReadRight && !this.hasTopLevelCreateRight) {
            this.showTableEnd();
            return false;
        }

        // JS Code fir's Help schreiben
        this.openHelpWin();

        //$normalize_field_name = $this->normalize_field_name;
        String imgLink = "<img src=\"" + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"4\" vspace=\"1\">";

        String thisCon = (this.site.indexOf('?') != -1) ? "&" : "?";
        String querySession = "?";
        if (!this.sessionUseTransSID && !this.sessionVAR.equals("")) {
            querySession = thisCon + this.sessionVAR + "=" + this.sessionID
                    + "&";
        } else {
            querySession = thisCon;
        }

        if (this.enableApplicationManager == true
                && this.hasTopLevelCreateRight) {
            this.out.println("  <tr class=\"" + this.styleTr + "\">");
            String link = "<a class=\"" + this.styleLinkNavigation
                    + "\" href=\"" + this.site + querySession
                    + "action=new&range=application\">";
            this.out.println("     <td class=\"" + this.styleTd + "\">");
            if (this.hasCreateRight == true) {
                this.out.println(link + imgLink
                        + this.dialogApplicationsNewTitle + "</a>");
            }
            this.out.println("     </td>");
            this.out.println("  </tr>");
        }

        String sqlHelp = " select \"APPLICATION\",\"SECTION\", \"NAME\" ";
        sqlHelp += " from " + this.settings.source + " s ";
        sqlHelp += " where s.\"" + this.settings.entryApplication
                + "\"  = s.\"" + this.settings.entrySection + "\"  and ";
        sqlHelp += "       s.\"" + this.settings.entryApplication
                + "\"  = s.\"" + this.settings.entryName + "\" and ";
        sqlHelp += "       s.\"DOCUMENTATION\" is not null ";

        Hashtable helpTexts = new Hashtable();

        try {
            Vector helpText = this.connection.getArrayAsVector(sqlHelp);
            if (helpText.size() > 0) {
                for (Enumeration el = helpText.elements(); el.hasMoreElements();) {
                    HashMap hm = (HashMap) el.nextElement();
                    helpTexts.put(hm.get("application").toString()
                            + hm.get("section").toString()
                            + hm.get("name").toString(), "Help");
                }
                this.hasHelps = true;
            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

        try {
            for (Enumeration el = this.dialogApplications.elements(); el
                    .hasMoreElements();) {
                HashMap application = (HashMap) el.nextElement();
                String linkSections = "<a href=\""
                        + this.site
                        + querySession
                        + "action=show&range=sections&application="
                        + this.response.encodeURL(application.get(
                                this.settings.entryApplication.toLowerCase())
                                .toString()) + "&application_type="
                        + application.get("entry_type") + "\">";
                String linkApplication = "";

                if (this.enableApplicationManager == true) {
                    linkApplication = "<a href=\""
                            + this.site
                            + querySession
                            + "action=show&range=application&application="
                            + this.response.encodeURL(application.get(
                                    this.settings.entryApplication
                                            .toLowerCase()).toString()) + "\">";
                           
                } else {
                    linkApplication = linkSections;
                }

                String k = application.get(
                        this.settings.entryApplication.toLowerCase())
                        .toString()
                        + application.get(
                                this.settings.entrySection.toLowerCase())
                                .toString()
                        + application
                                .get(this.settings.entryName.toLowerCase())
                                .toString();

                String linkHelp = "&nbsp;";
                if (helpTexts.containsKey(k)) {
                    String session = "''";
                    if (!this.sessionID.equals("")) {
                        session = "'" + this.sessionID + "'";
                    }
                    String helpHref = "source="
                            + this.settings.source
                            + "&application="
                            + this.response.encodeURL(application.get(
                                    this.settings.entryApplication
                                            .toLowerCase()).toString())
                            + "&section="
                            + this.response.encodeURL(application.get(
                                    this.settings.entrySection.toLowerCase())
                                    .toString())
                            + "&entry="
                            + this.response.encodeURL(application.get(
                                    this.settings.entryName.toLowerCase())
                                    .toString());
                    linkHelp = "<a href=\"javascript:openHelpWin('"
                            + helpHref
                            + "',"
                            + session
                            + ");\"><img src=\""
                            + this.imgDir
                            + this.imgHelp
                            + "\" border=\"0\" title=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_help")
                            + "\"></a>";
                }

                try {
                    this.getRights(application.get("application").toString(),
                            null, null);
                    if (this.hasReadRight == false
                            && this.hasCreateRight == false) {
                        continue;
                    }
                } catch (Exception e) {
                    this.out.println("  <tr class=\"" + this.styleTr + "\">");
                    this.out.println("    <td class=\"" + this.styleTd + "\">");
                    this.out
                            .println("    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                    this.out.println("    <tr>");
                    this.out.println("     <td class=\"" + this.styleTd
                            + "\" width=\"50%\">");
                    this.out.println("       "
                            + application.get(this.settings.entrySettingTitle
                                    .toLowerCase()));
                    this.out.println("     </td>");
                    this.out.print("     <td>");
                    this.showError("ACL : " + e.getMessage());
                    this.out.println("</td>");
                    this.out.println("    </tr>");
                    this.out.println("    </table>");
                    this.out.println("    </td>");
                    this.out.println("  </tr>");

                    continue;
                }

                this.out.println("  <tr class=\"" + this.styleTr + "\">");
                this.out.println("    <td class=\"" + this.styleTd + "\">");
                this.out
                        .println("    <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
                this.out.println("    <tr>");
                this.out.println("     <td class=\"" + this.styleTd
                        + "\" width=\"50%\">");
                this.out.println(linkApplication
                        + imgLink
                        + "</a>"
                        + linkSections
                        + application.get(this.settings.entrySettingTitle
                                .toLowerCase()) + "</a>");
                this.out.println("     </td>");
                this.out.println("     <td>" + linkHelp + "</td>");
                this.out.println("    </tr>");
                this.out.println("    </table>");
                this.out.println("    </td>");
                this.out.println("  </tr>");

            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

        this.showActions(new Integer(1), null, null, null);
        this.showTableEnd();

        return true;
    }

    /**
     * JS Code zum öffnen von Hifsfenster
     *  
     */

    private void openHelpWin() throws Exception {
        this.debug(3, "openHelpWin");

        this.out.println("<script language=\"JavaScript\">");
        this.out.println("  function openHelpWin(href,jsessionid){");
        this.out.println("    var session_str = \"\";");
        this.out.println("    if(jsessionid){");
        this.out.println("      session_str = \"&" +this.sessionVAR
                + "=\"+jsessionid;");
        this.out.println("    }");

        //this.out.println(" href = href.replace(\"\\\",\"\\\\\"); ");

        this.out
                .println("    helpWin = window.open(\""
                        + this.helpFile
                        + "?action=show&range=help&locale="
                        + this.locale
                        + "&\"+href+session_str,\"_blank\",\"width="
                        + this.helpWinWidth
                        + ",height="
                        + this.helpWinHeight
                        + ",status=no,toolbar=no,menubar=no,resizable=yes,directories=no,location=no,scrollbars\");");
        this.out.println("  }");
        this.out.println("</script>");

    }

    /**
     * JS Code zum öffnen von Hilfetexten Fenster
     *  
     */

    private void openHelpsWin() throws Exception {
        this.debug(3, "openHelpsWin");

        this.out.println("<script language=\"JavaScript\">");
        this.out.println("  function openHelpsWin(href,jsessionid){");
        this.out.println("    var session_str = \"\";");
        this.out.println("    if(jsessionid){");
        this.out.println("      session_str = \"&" + this.sessionVAR
                + "=\"+jsessionid;");
        this.out.println("    }");
        this.out
                .println("    helpsWin = window.open(\""
                        + this.documentationFile
                        + "?locale="+this.locale+"&action=show&range=helps&\"+href+session_str,\"_blank\",\"width="
                        + this.helpsWinWidth
                        + ",height="
                        + this.helpsWinHeight
                        + ",status=no,toolbar=no,menubar=no,resizable=yes,directories=no,location=no,scrollbars\");");
        this.out.println("  }");
        this.out.println("</script>");

    }

    /**
     * JS Code zum öffnen von Hilfetexten Fenster
     *  
     */

    private void openDocumentationWin() throws Exception {
        this.debug(3, "openDocumentationWin");

        this.out.println("<script language=\"JavaScript\">");
        this.out.println("  function openDocumentationWin(href,jsessionid){");
        this.out.println("    var session_str = \"\";");
        this.out.println("    if(jsessionid){");
        this.out.println("      session_str = \"&" + this.sessionVAR
                + "=\"+jsessionid;");
        this.out.println("    }");
        this.out
                .println("    documentationWin = window.open(\""
                        + this.documentationFile
                        + "?locale="+this.locale+"&action=show&range=documentation&\"+href+session_str,\"_blank\",\"width="
                        + this.docuWinWidth
                        + ",height="
                        + this.docuWinHeight
                        + ",status=no,toolbar=no,menubar=no,resizable=yes,directories=no,location=no,scrollbars\");");
        this.out.println("  }");
        this.out.println("</script>");

    }

    /**
     * Werte aller Applickationen lesen
     * 
     * @param refresh
     *            true Werte aus der Batenbank lesen
     * @return boolean Fehlerzustand
     * @throws Exception
     * @see #setDialogApplications(boolean)
     *  
     */
    private boolean setDialogApplications(boolean refresh) throws Exception {
        this.debug(3, "setDialogApplications : refresh = " + refresh);

        if (this.dialogApplications == null
                || this.dialogApplications.size() == 0 || refresh == true) {

            try {
                this.getDialogApplications(null);
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                return false;
            }
        }

        if (!this.settings.application.equals("")
                && this.dialogApplications != null) {
            for (int i = 0; i < this.dialogApplications.size(); i++) {

                HashMap hm = new HashMap();
                hm = (HashMap) this.dialogApplications.get(i);

                if (this.settings.application.equals(hm.get(
                        this.settings.entryApplication.toLowerCase())
                        .toString())) {
                    try {
                        this.applicationType = Integer.parseInt((String) hm
                                .get("entry_type"));
                    } catch (Exception e) {
                        this.applicationType = 0;
                    }
                    this.dialogApplicationIndex = new Integer(i);
                    break;
                }
            }
        } else {
            this.dialogApplicationIndex = new Integer(-1);
        }

        return true;
    }

    /**
     * Werte aller Sectionen einer Applikation lesen und die Indexes setzen
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */
    private boolean setDialogSections() throws Exception {
        this.debug(3, "setDialogSections");

        if (!this.settings.application.equals("")) {
            try {
                this.getDialogSections(this.settings.application, null);
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                return false;
            }

        }
        return true;
    }

    /**
     * Werte aller Entries einer Sektion lesen und die Indexes setzen
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */

    private boolean setDialogEntries() throws Exception {
        this.debug(3, "setDialogEntries");

        if (!this.settings.section.equals("") && this.dialogSections != null) {
            for (int i = 0; i < this.dialogSections.size(); i++) {

                HashMap hm = new HashMap();
                hm = (HashMap) this.dialogSections.get(i);

                if (this.settings.section.equals(hm.get(
                        this.settings.entrySection.toLowerCase()).toString())) {
                    try {
                        this.sectionType = Integer.parseInt((String) hm
                                .get("entry_type"));
                    } catch (Exception e) {
                        this.sectionType = 0;
                    }
                    this.dialogSectionIndex = new Integer(i);
                    break;
                }
            }
        } else {
            this.dialogSectionIndex = new Integer(-1);
        }

        if (!this.settings.application.equals("")
                && !this.settings.section.equals("")) {

            try {
                /*
                 * if (this.enableAutoSetLongValues) {
                 * this.setDialogEntriesLongValues(); }
                 */

                this.getDialogEntries(this.settings.application,
                        this.settings.section, null);
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                this.connection.rollback();
                return false;
            }

        }

        if (!this.settings.entry.equals("") && this.dialogEntries != null) {
            for (int i = 0; i < this.dialogEntries.size(); i++) {

                HashMap hm = new HashMap();
                hm = (HashMap) this.dialogEntries.get(i);

                if (this.settings.entry.equals(hm.get(
                        this.settings.entryName.toLowerCase()).toString())) {
                    this.dialogEntryIndex = new Integer(i);
                    break;
                }
            }
        } else {
            this.dialogEntryIndex = new Integer(-1);
        }

        return true;
    }

    /**
     * Diese Funktion vermeidet Inhalt von inkonsistenten Daten.
     * 
     * Hintergrund : da es als Wert entweder "VALUE" oder "LONG_VALUE" benutzt
     * werden können, dient "INPUT_TYPE" Feld als Flag , um zu unterscheiden -
     * welches von den beiden Felder gerade aktiv ist. bei "LONG_VALUE" müssen
     * "INPUT_TYPE" = 5(Dokument) oder 6(Dokument binär) und "VALUE" = null
     * gesetzt werden. und ausserdem "DISPLAY_TYPE" 3(Textarea) bzw 4(Versteckt)
     * sein
     * 
     * bei Settingsaktualisierung durch die fremden Schnittstellen, kann
     * vorkommen , dass die "LONG_VALUE"s aktualisiert werden, ohne das
     * "INPUT_TYPE" auf 5,6 und "DISPLAY_TYPE" auf 3(Textarea) bzw 4(Versteckt)
     * zu setzen : das wird dazu führen, dass die BLOBs dann (ohne diese
     * Funktion) als eine Zeichenkette betrachtet werden und verloren gehen.
     *  
     */
    private void setDialogEntriesLongValues() throws Exception {

        StringBuffer sqlLongValue = new StringBuffer(
                "select \"LONG_VALUE\" from " + this.settings.source).append(
                " where \"" + this.settings.entryApplication + "\" = "
                        + this.dbQuoted(this.settings.application) + " and ")
                .append(
                        "	\"" + this.settings.entrySection + "\" = "
                                + this.dbQuoted(this.settings.section)
                                + " and ").append(
                        " \"" + this.settings.entryName + "\" = "
                                + this.dbQuoted(this.settings.entry));

        StringBuffer sql;

        try {

            byte[] longValue = this.connection.getBlob(sqlLongValue.toString());

            if (longValue != null && longValue.length > 0) {

                String defaultValue = (this.record.get("default_value")
                        .toString().indexOf('.') != -1) ? this.record.get(
                        "default_value").toString()
                        : this.settings.defaultDocumentFileName;
                String inputType = this.record.get("input_type").toString();
                String displayType = this.record.get("display_type").toString();

                if (inputType.equals("5")) { // Dokument
                    if (displayType.equals("3") || displayType.equals("4")) {
                        //Textarea oder Versteckt - alles in Ordnung
                    } else {
                        sql = new StringBuffer(" update "
                                + this.settings.source + " ")
                                .append(
                                        " set 	\"" + this.settings.entryValue
                                                + "\" 	= null 	, ")
                                .append("			\"INPUT_TYPE\"							= 5	, ")
                                .append("			\"DISPLAY_TYPE\"						= 4 , ")
                                .append(
                                        "			\"DEFAULT_VALUE\"						= '"
                                                + defaultValue + "'	")
                                .append(
                                        " where	\""
                                                + this.settings.entryApplication
                                                + "\"	= "
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and ")
                                .append(
                                        "				\""
                                                + this.settings.entrySection
                                                + "\"			= "
                                                + this
                                                        .dbQuoted(this.settings.section)
                                                + " and ")
                                .append(
                                        "				\""
                                                + this.settings.entryName
                                                + "\"					= "
                                                + this
                                                        .dbQuoted(this.settings.entry));

                        this.connection.execute(sql.toString());
                        this.connection.commit();

                        this.record.put("input_type", "5");
                        this.record.put("display_type", "4");
                        this.record.put("default_value", defaultValue);
                    }

                    this.record.put("value", new String(longValue));
                    this.record.put("long_value", "");

                    if (displayType.equals("4")) {
                        this.hasBinaryValue.put(this.record.get("application")
                                .toString()
                                + this.record.get("section").toString()
                                + this.record.get("name").toString(), "1");
                    }
                }//inputType = 5
                else if (inputType.equals("6")) { // Dokument binär
                    if (displayType.equals("4")) {
                        //Versteckt - alles in Ordnung
                    } else {
                        sql = new StringBuffer(" update "
                                + this.settings.source + " ")
                                .append(
                                        " set 	\"" + this.settings.entryValue
                                                + "\" 	= null 	, ")
                                .append("			\"INPUT_TYPE\"							= 6	, ")
                                .append("			\"DISPLAY_TYPE\"						= 4 , ")
                                .append(
                                        "			\"DEFAULT_VALUE\"						= '"
                                                + defaultValue + "'	")
                                .append(
                                        " where	\""
                                                + this.settings.entryApplication
                                                + "\"	= "
                                                + this
                                                        .dbQuoted(this.settings.application)
                                                + " and ")
                                .append(
                                        "				\""
                                                + this.settings.entrySection
                                                + "\"			= "
                                                + this
                                                        .dbQuoted(this.settings.section)
                                                + " and ")
                                .append(
                                        "				\""
                                                + this.settings.entryName
                                                + "\"					= "
                                                + this
                                                        .dbQuoted(this.settings.entry));

                        this.connection.execute(sql.toString());
                        this.connection.commit();

                        this.record.put("input_type", "6");
                        this.record.put("display_type", "4");
                        this.record.put("default_value", defaultValue);
                    }
                    this.record.put("value", "");
                    this.hasBinaryValue.put(this.record.get("application")
                            .toString()
                            + this.record.get("section").toString()
                            + this.record.get("name").toString(), "1");

                } else {// wir machen Dokument Binär Versteckt
                    sql = new StringBuffer(" update " + this.settings.source
                            + " ").append(
                            " set 	\"" + this.settings.entryValue
                                    + "\" 	= null 	, ").append(
                            "			\"INPUT_TYPE\"							= 6	, ").append(
                            "			\"DISPLAY_TYPE\"						= 4 , ").append(
                            "			\"DEFAULT_VALUE\"						= '" + defaultValue
                                    + "'	").append(
                            " where	\"" + this.settings.entryApplication
                                    + "\"	= "
                                    + this.dbQuoted(this.settings.application)
                                    + " and ").append(
                            "				\"" + this.settings.entrySection + "\"			= "
                                    + this.dbQuoted(this.settings.section)
                                    + " and ").append(
                            "				\"" + this.settings.entryName + "\"					= "
                                    + this.dbQuoted(this.settings.entry));

                    this.connection.execute(sql.toString());
                    this.connection.commit();

                    this.record.put("value", "");
                    this.record.put("input_type", "6");
                    this.record.put("display_type", "4");
                    this.record.put("default_value", defaultValue);

                    this.hasBinaryValue.put(this.record.get("application")
                            .toString()
                            + this.record.get("section").toString()
                            + this.record.get("name").toString(), "1");
                }

            }// if long value
            else {
            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

    }

    /**
     * Aktionen anzeigen: Suchen, Export, Import
     * 
     * @param columnIndex
     *            Anzahl Spalten der Darstellung
     * @param applicationIndex
     *            Index der aktuellen Applikation in dialog_applications()
     * @param sectionIndex
     *            Index der aktuellen Sektion in dialog_sections()
     * @param entryIndex
     *            Index des aktuellen Eintrags in dialog_entries()
     * @return boolean Fehlerzustand
     * @see #showActions(Integer, Integer, Integer, Integer)
     */

    private boolean showActions(Integer columnIndex, Integer applicationIndex,
            Integer sectionIndex, Integer entryIndex) throws Exception {
        this.debug(3, "showActions : columnIndex = " + columnIndex
                + " applicationIndex = " + applicationIndex
                + " sectionIndex = " + sectionIndex + " entryIndex = "
                + entryIndex);

        if (this.settings.section.equals(this.settings.entrySchemaSection)) {
            if (this.hasCreateRight == false) {
                this.enableExport = false;
                this.enableImport = false;
                this.enableDocumentation = false;
            }
        } else {
            if (this.aclRange.equals("application")) { // Alle Applicationen
                if (this.hasTopLevelWriteRight == false) {
                    this.enableExport = false;
                    this.enableImport = false;
                    this.enableDocumentation = false;
                    //this.enableHelps = false;
                }

            } else {
                if (this.hasWriteRight == false) {
                    this.enableExport = false;
                    this.enableImport = false;
                    this.enableDocumentation = false;
                    //this.enableHelps = false;
                }
            }
        }

        if (this.action.equals("query") && this.settings.application.equals("")) { // Suche
            // bei
            // allen
            // Applikationen
            try {
                this.getTopLevelRights();
            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }

            if (this.hasTopLevelWriteRight == false) {
                this.enableExport = false;
                this.enableImport = false;
                this.enableDocumentation = false;
                //this.enableHelps = false;
            }
        }

        String inputQuery 				= "";
        String inputQueryReplace 	= "";
        String inputExport 				= "";
        String inputImport 				= "";
        
        
        String	selected_meta        = " selected ";
    		String selected_value       = "";
    		String span_replace_style   = " style=\"display:none\" ";
   			int query_select_range   		= 1;

        if (this.request != null) {
            if (this.getRequestValue("input_query") != null) {
                inputQuery = this.getRequestValue("input_query");
            }
            // kein trim wegen regul.ausdr usw
            if (this.getRequestValue("input_query_replace") != null) {
                inputQueryReplace = this.getRequestValue("input_query_replace");
            }
            if (this.getRequestValue("input_export") != null) {
                inputExport = this.getRequestValue("input_export").trim();
            }
            if (this.getRequestValue("input_import") != null) {
                inputImport = this.getRequestValue("input_import").trim();
            }
            
            try{
            	if(this.getRequestValue("query_select_range") != null){
            		query_select_range = Integer.parseInt(this.getRequestValue("query_select_range").trim());
            	}
            }
            catch(Exception e){}
        }
        
       	switch(query_select_range){
      		case 2  :
          	        selected_value     = " selected ";
            	      selected_meta      = "";
              	    span_replace_style = "";
                  
                	  break;
   			}
        
        String[] listbox_translation = {"all settings","values"};
        try{
        	String[] listbox_translation_msg = this.rb.getMessage("sos.settings.dialog.listbox_query_select_range").split(";");
        	listbox_translation[0] = listbox_translation_msg[0];
        	listbox_translation[1] = listbox_translation_msg[1]; 
        }
        catch(Exception e){}
        

        this.out.println("<tr class=\"" + this.styleTr + "\">");
        this.out.println("  <td class=\"" + this.styleTd + "\" colspan=\""
                + columnIndex + "\">");
        this.out.println("    <table width=\"100%\" border=\"0\"><tr>");

        this.out.println("      <form name=\"" + this.form
                + "_query\" action=\"" + this.site + "\" method=\"post\">");
        this.out.println("      <td class=\"" + this.styleTd
                + "\" valign=\"top\">");
        this.out
                .println("        <input type=\"image\" name=\"btn_query\" src=\""
                        + this.imgDir
                        + this.sosLang
                        + "/btn_query.gif\" alt=\""
                        + this.rb
                                .getMessage("sos.settings.dialog.btn_query_alt")
                        + "\">");
        this.out.println("     </td>");
        
        this.out.println("       <td>");
    		this.out.println("       <select name=\"query_select_range\" onchange=\"document.getElementById('span_query_replace').style.display = (this.selectedIndex == 1) ? '' : 'none';\">" );
    		this.out.println("         <option value=\"1\" "+selected_meta+">"+listbox_translation[0]+"</option>");
    		this.out.println("         <option value=\"2\" "+selected_value+">"+listbox_translation[1]+"</option>");
    		this.out.println("       </select>");
    		this.out.println("       </td>");

                        
                        
        this.out.println("      <td class=\"" + this.styleTd
                + "\" valign=\"top\" colspan=\"2\" nowrap>");
        this.out
                .println("        <input type=\"text\"  name=\"input_query\" class=\""
                        + this.styleInput + "\" value=\"" + inputQuery + "\">");
				
				this.out.println("        <span id=\"span_query_replace\" "+span_replace_style+">");
    		this.out.println(this.rb.getMessage("sos.settings.dialog.label_query_replace"));
    		this.out.println("        <input type=\"text\"  name=\"input_query_replace\" class=\""+this.styleInput+"\" value=\""+inputQueryReplace+"\">");
    		this.out.println("         &nbsp;"+this.rb.getMessage("sos.settings.dialog.label_query_replace_store"));
    		this.out.println("        </span>");
				
        boolean hasEntries = false;
        if (applicationIndex != null) {
            if (applicationIndex.intValue() == 0
                    || applicationIndex.intValue() > 0) {
                this.out
                        .println("&nbsp;"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_search_range"));
                hasEntries = true;
            }
        }
        if (sectionIndex != null && hasEntries == false) {
            if (sectionIndex.intValue() == 0 || sectionIndex.intValue() > 0) {
                this.out
                        .println("&nbsp;"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_search_range"));
            }
        }

        this.out
                .println("        <input type=\"hidden\" name=\"application\" value=\""
                        + this.settings.application + "\">");
        this.out
                .println("        <input type=\"hidden\" name=\"section\" value=\""
                        + this.settings.section + "\">");
        this.out.println("      &nbsp;</td></form></tr>");

        if (this.enableExport == true) {
            this.out.println("    <tr class=\"" + this.styleTr + "\">");
            this.out.println("      <form name=\"" + this.form
                    + "_export\" action=\"" + this.site
                    + "\" method=\"post\" onSubmit=\"if (document." + this.form
                    + "_export.input_export.value != '' ) { document."
                    + this.form
                    + "_export.sos_headers_off.value=0; } return true;\">");
            this.out.println("      <td class=\"" + this.styleTd+ "\" valign=\"top\">");
            this.out
                    .println("        <input type=\"image\" name=\"btn_export\" src=\""
                            + this.imgDir
                            + this.sosLang
                            + "/btn_export.gif\" alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_export_alt")
                            + "\">&nbsp;");
            
            this.out.println("      </td>");                
            this.out.println("      <td class=\"" + this.styleTd+ "\" valign=\"top\">");
            this.out
                    .println("         <input type=\"checkbox\" name=\"export_documentation\" value=\"1\"");
            if (this.exportDocumentation == 1) {
                this.out.println(" checked");
            }
            this.out
                    .println(">&nbsp;"
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_only_docs"));
            this.out.println("       </td>");
            this.out.println("      <td class=\"" + this.styleTd
                    + "\" valign=\"top\" width=\"40%\"><nobr>");
            this.out
                    .println("        <input type=\"text\"  name=\"input_export\" class=\""
                            + this.styleInput
                            + "\" value=\""
                            + inputExport
                            + "\">&nbsp;");

            boolean hasExportTitle = false;
            if (sectionIndex != null) {
                if (sectionIndex.intValue() == 0 || sectionIndex.intValue() > 0) {
                    this.out.println(this.dialogSectionsExportTitle);
                    hasExportTitle = true;
                }
            }

            if (applicationIndex != null && hasExportTitle == false) {
                if (applicationIndex.intValue() == 0
                        || applicationIndex.intValue() > 0) {
                    this.out.println(this.dialogApplicationsExportTitle);
                }
            }

            this.out.println("      &nbsp;</nobr></td>");
            this.out.println("      <td class=\"" + this.styleTd
                    + "\" width=\"40%\">");
            this.out
                    .println("        <input type=\"hidden\" name=\"sos_headers_off\" value=\"1\">");
            this.out
                    .println("        <input type=\"hidden\" name=\"application\" value=\""
                            + this.settings.application + "\">");
            this.out
                    .println("        <input type=\"hidden\" name=\"section\" value=\""
                            + this.settings.section + "\">");
            this.out.println("      &nbsp;</td></form></tr>");
        }

        if (this.enableImport == true) {
            this.out.println("    <tr class=\"" + this.styleTr + "\">");
            this.out.println("    <form name=\"" + this.form
                    + "_import\" enctype=\"multipart/form-data\" action=\""
                    + this.site + "\" method=\"post\">");
            this.out.println("      <td class=\"" + this.styleTd+ "\" >");
            this.out
                    .println("        <input type=\"image\" name=\"btn_import\" src=\""
                            + this.imgDir
                            + this.sosLang
                            + "/btn_import.gif\" alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_import_alt")
                            + "\">&nbsp;");
                            
            this.out.println("      </td>");
            this.out.println("      <td class=\"" + this.styleTd+ "\" valign=\"top\">");
            this.out
                    .println("         <input type=\"checkbox\" name=\"import_documentation\" value=\"1\"");
            if (this.importDocumentation == 1) {
                this.out.println(" checked");
            }
            this.out
                    .println(">&nbsp;"
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_only_docs"));
            this.out.println("       </td>");
            this.out.println("      <td class=\"" + this.styleTd
                    + "\" width=\"40%\">");

            //$input_import = isset($_FILES['input_import']) ?
            // $_FILES['input_import']['name'] : $_REQUEST['input_import'];
            inputImport = "";

            this.out
                    .println("        <input type=\"file\"  name=\"input_import\" class=\""
                            + this.styleInput
                            + "\" value=\""
                            + inputImport
                            + "\">&nbsp;</td>");
            this.out.println("      <td class=\"" + this.styleTd
                    + "\" width=\"40%\">");
            this.out
                    .println("        <input type=\"hidden\" name=\"application\" value=\""
                            + this.settings.application + "\">");
            this.out
                    .println("        <input type=\"hidden\" name=\"section\" value=\""
                            + this.settings.section + "\">");
            this.out.println("      &nbsp;</td></tr></form>");
        }

        if (this.enableHelps == true) {
            this.openHelpsWin();
            String helpsHref = "source=" + this.settings.source;

            if (this.settings.application.equals("")
                    && this.settings.section.equals("")) {
                helpsHref += "&item=application";
            } else if (!this.settings.application.equals("")
                    && this.settings.section.equals("")) {
                helpsHref += "&item=section&application="
                        + this.response.encodeURL(this.settings.application);
            } else if (!this.settings.application.equals("")
                    && !this.settings.section.equals("")) {
                String section = this.settings.section;

                if (this.settings.section
                        .equals(this.settings.entrySchemaSection)) {
                    section = this.settings.entrySchemaSection;
                }
                helpsHref += "&item=entry&application="
                        + this.response.encodeURL(this.settings.application)
                        + "&section=" + this.response.encodeURL(section);
            }

            String session = "''";
            if (!this.sessionID.equals("")) {
                session = "'" + this.sessionID + "'";
            }

            this.out.println("    <tr class=\"" + this.styleTr + "\">");
            this.out.println("      <td class=\"" + this.styleTd
                    + "\" colspan=\"4\">");
            if (this.hasHelps) {
                this.out.println("        <a href=\"javascript:openHelpsWin('"
                        + helpsHref
                        + "',"
                        + session
                        + ");\"><img src=\""
                        + this.imgDir
                        + this.sosLang
                        + "/btn_help.gif\" alt=\""
                        + this.rb
                                .getMessage("sos.settings.dialog.btn_help_alt")
                        + "\" border=\"0\"></a>");
            } else {
                this.out
                        .println("<img src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_help.gif\" alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_help_not_found_alt")
                                + "\" border=\"0\">");
            }
            this.out.println("      </td>");
            this.out.println("     </tr>");
        }

        if (this.enableDocumentation == true) {
            this.openDocumentationWin();
            String docuHref = "source=" + this.settings.source;

            if (this.settings.application.equals("")
                    && this.settings.section.equals("")) {
                docuHref += "&item=application";
            } else if (!this.settings.application.equals("")
                    && this.settings.section.equals("")) {
                docuHref += "&item=section&application="
                        + this.response.encodeURL(this.settings.application);
            } else if (!this.settings.application.equals("")
                    && !this.settings.section.equals("")) {
                String section = this.settings.section;

                if (this.settings.section
                        .equals(this.settings.entrySchemaSection)) {
                    section = this.settings.entrySchemaSection;
                }
                docuHref += "&item=entry&application="
                        + this.response.encodeURL(this.settings.application)
                        + "&section=" + this.response.encodeURL(section);
            }

            String session = "''";
            if (!this.sessionID.equals("")) {
                session = "'" + this.sessionID + "'";
            }

            this.out.println("    <tr class=\"" + this.styleTr + "\">");
            this.out.println("      <td class=\"" + this.styleTd
                    + "\" colspan=\"4\">");
            this.out
                    .println("        <a href=\"javascript:openDocumentationWin('"
                            + docuHref
                            + "',"
                            + session
                            + ");\"><img src=\""
                            + this.imgDir
                            + this.sosLang
                            + "/btn_docu.gif\" alt=\""
                            + this.rb.getMessage("sos.settings.dialog.btn_docu_alt")
                            + "\" border=\"0\"></a>");
            this.out.println("      </td>");
            this.out.println("     </tr>");
        }

        this.out.println("     </table>");
        this.out.println("   </td>");
        this.out.println("</tr>");

        return true;
    }

    /**
     * Eintrag anzeigen
     * 
     * @return boolean Fehlerzustand
     * @throws Exception
     */

    private boolean showDialogApplication() throws Exception {
        this.debug(3, "showDialogApplication");

        this.out
                .println("<form name=\""
                        + this.form
                        + "\" action=\""
                        + this.site
                        + "\" method=\"post\" onSubmit=\"return sos_settings_onSubmit()\">");

        this.showTableBegin();

        this.showNavigation(new Integer(2), this.dialogApplicationIndex, null,
                null);

        this.out.println("<input type=\"hidden\" name=\"button\">");
        this.out.println("<input type=\"hidden\" name=\"application\" value=\""
                + this.record.get(this.settings.entryApplication.toLowerCase())
                + "\">");

        boolean checkAcl = true;
        if (this.range.equalsIgnoreCase("application")) {
            this.out
                    .println("<input type=\"hidden\" name=\"range\"       value=\"application\">");
            this.out
                    .println("<input type=\"hidden\" name=\"section\"     value=\""
                            + this.record.get(this.settings.entryApplication
                                    .toLowerCase()) + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"entry\"       value=\""
                            + this.record.get(this.settings.entryApplication
                                    .toLowerCase()) + "\">");

            if (this.isNew) {
                try {
                    this.getTopLevelRights();
                } catch (Exception e) {
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                    return false;
                }
                this.hasCreateRight = this.hasTopLevelCreateRight;

                if (this.hasCreateRight) {
                    this.hasWriteRight = true;
                    this.hasReadRight = true;
                    this.disabled = "";
                } else {
                    if (this.user != null) {
                        this.setError(this.rb.getMessage(
                                "sos.settings.dialog.err_acl_create_new_apps",
                                this.user.getFullName()), SOSClassUtil
                                .getMethodName());
                        return false;
                    }
                }

                checkAcl = false;

            }

        } else if (this.range.equalsIgnoreCase("section")) {
            this.out
                    .println("<input type=\"hidden\" name=\"range\"       value=\"section\">");
            this.out
                    .println("<input type=\"hidden\" name=\"section\"     value=\""
                            + this.record.get(this.settings.entrySection
                                    .toLowerCase()) + "\">");
            this.out
                    .println("<input type=\"hidden\" name=\"entry\"       value=\""
                            + this.record.get(this.settings.entrySection
                                    .toLowerCase()) + "\">");
        }

        if (checkAcl) {
            this.aclRange = this.range;

            try {
                this.getRights(this.record.get("application").toString(),
                        this.record.get("section").toString(), null);
            } catch (Exception e) {
                this.setError("ACL : " + e.getMessage(), SOSClassUtil
                        .getMethodName());
            }

            if (this.hasCreateRight) {
                if (this.isNew || this.applicationType == 1) {
                    this.hasWriteRight = true;
                    this.disabled = "";
                }
            }

            if (this.applicationType == 1) { // schema
                if (this.isNew) {
                    this.hasWriteRight = true;
                    this.disabled = "";
                }
            }
        }

        this.out.println("<tr class=\"" + this.styleTr + "\">");
        this.out.println("  <th align=\"left\" valign=\"top\" class=\""
                + this.styleTh + "\" colspan=\"2\">" + this.record.get("title")
                + "&nbsp;</th>");
        this.out.println("</tr><tr class=\"" + this.styleTr + "\">");

        this.out.println("<td width=\"20%\" valign=\"top\" class=\""
                + this.styleTdLabel + "\">Name&nbsp;</td>");
        this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                + this.styleTd + "\">");
        this.out
                .println("   <input "
                        + this.disabled
                        + " name=\"name\" type=\"text\" size=\"80\" maxlength=\"100\" class=\""
                        + this.styleInput + "\" value=\""+this.htmlSpecialChars(this.record.get("name").toString()) + "\">");
        this.out.println("  &nbsp;</td>");

        this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
        this.out.println("  <td width=\"20%\" valign=\"top\" class=\""
                + this.styleTdLabel + "\">"
                + this.rb.getMessage("sos.settings.dialog.label_title")
                + "&nbsp;</td>");
        this.out
                .println("  <td width=\"80%\" valign=\"top\" class=\""
                        + this.styleTd
                        + "\"><input "
                        + this.disabled
                        + " type=\"text\" name=\"title\" size=\"80\" maxlength=\"100\" class=\""
                        + this.styleInput + "\" value=\""+this.htmlSpecialChars(this.record.get("title").toString()) + "\">&nbsp;</td>");
        this.out.println("</tr><tr class=\"" + this.styleTr + "\">");

        if (this.enableListManager == true) {
            if (this.range.equalsIgnoreCase("application")
                    || (this.applicationType == 0 && this.range
                            .equalsIgnoreCase("section"))) {

                String[] input_type_apps = this.rb.getMessage(
                        "sos.settings.dialog.listbox_input_type_apps").split(
                        ";");
                if (input_type_apps.length != 2) { throw new Exception(
                        "\"sos.settings.dialog.listbox_input_type_apps\" expected two values, given : "
                                + input_type_apps.length); }

                this.out
                        .println("  <td width=\"20%\" valign=\"top\" class=\""
                                + this.styleTdLabel
                                + "\">"
                                + this.rb
                                        .getMessage("sos.settings.dialog.label_input_format")
                                + "&nbsp;</td>");
                this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                        + this.styleTd + "\"><select " + this.disabled
                        + " name=\"entry_type\" size=\"1\" class=\""
                        + this.styleInput + "\">");
                this.out.println("     <option value=\"0\"");
                if (!this.record.get("entry_type").equals("1")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_apps[0].trim() + "&nbsp;");
                this.out.println("     <option value=\"1\"");
                if (this.record.get("entry_type").equals("1")) {
                    this.out.println(" selected");
                }
                this.out.println(">" + input_type_apps[1].trim()
                        + "&nbsp;</select></td>");
                this.out.println("</tr><tr class=\"" + this.styleTr + "\">");
            }
        }

        if (this.hasWriteRight) {

            String height = (this.enableEditor) ? "height=\""
                    + this.editorHeight + "\"" : "";

            this.out
                    .println("  <td width=\"20%\" "
                            + height
                            + " valign=\"top\" class=\""
                            + this.styleTdLabel
                            + "\">"
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_description")
                            + "&nbsp;</td>");

            this.out.println("  <td width=\"80%\" " + height
                    + " valign=\"top\" class=\"" + this.styleTd + "\">");

            if (this.enableEditor) {
                this.showEditor(this.record.get("documentation").toString());
            } else {
                this.out
                        .println("    <textarea rows=\"5\" cols=\"70\" wrap=\"off\" name=\"documentation\" class=\""
                                + this.styleInput
                                + "\">"
                                + this.htmlSpecialChars(this.record.get(
                                        "documentation").toString())
                                + "</textarea>&nbsp;");
            }
            this.out.println("</td>");
            this.out.println("</tr>");
            this.out.println("<tr class=\"" + this.styleTr + "\">");
        } else {
            this.out
                    .println("  <td width=\"20%\" valign=\"top\" class=\""
                            + this.styleTdLabel
                            + "\">"
                            + this.rb
                                    .getMessage("sos.settings.dialog.label_description")
                            + "&nbsp;</td>");
            this.out.println("  <td width=\"80%\" valign=\"top\" class=\""
                    + this.styleTd + "\">");
            this.out.println(this.record.get("documentation"));
            this.out.println("  &nbsp;</td>");
            this.out.println("</tr>");
            this.out.println("<tr class=\"" + this.styleTr + "\">");
        }

        this.out.println("<td width=valign=\"top\" class=\"" + this.styleTd
                + "\">&nbsp;</td>");
        this.out.println("<td valign=\"top\" class=\"" + this.styleTd + "\">");

        if (this.hasWriteRight == true) {
            if (this.isNew == true) {
                this.out
                        .println("    <input type=\"image\" name=\"btn_insert\" value=\"insert\" src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_insert.gif\"  alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_insert_alt")
                                + "\" onClick=\"document.sos_settings.button.value='insert';\">&nbsp;");
            } else {
                this.out
                        .println("   <input type=\"image\" name=\"btn_store\"  value=\"store\"  src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_store.gif\"  alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_store_alt")
                                + "\" onClick=\"document.sos_settings.button.value='store';\">&nbsp;");

                this.out
                        .println("    <input type=\"image\" name=\"btn_insert\" value=\"insert\" src=\""
                                + this.imgDir
                                + this.sosLang
                                + "/btn_insert.gif\" alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_store_as_alt")
                                + "\" onClick=\"document.sos_settings.button.value='insert';\">&nbsp;");

            }
        }// hasWriteRight

        if (this.hasDeleteRight == true) {
            this.out
                    .println("    <input type=\"image\" name=\"btn_delete\" value=\"delete\" src=\""
                            + this.imgDir
                            + this.sosLang
                            + "/btn_delete.gif\" alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_delete_alt")
                            + "\" onClick=\"document.sos_settings.button.value=\'delete\'; if ( confirm(\'"
                            + this.rb
                                    .getMessage("sos.settings.dialog.confirm_delete_apps_1")
                            + "\') ) { return confirm(\'"
                            + this.rb
                                    .getMessage("sos.settings.dialog.confirm_delete_apps_2")
                            + "\') } else { return false; }\">&nbsp;");
        }

        if (this.hasWriteRight) {
            if (this.range.equals("section") && this.applicationType == 0) {
                this.out
                        .println("    <input type=\"image\" name=\"btn_schema\" src=\""
                                + this.imgDir
                                + sosLang
                                + "/btn_schema.gif\" alt=\""
                                + this.rb
                                        .getMessage("sos.settings.dialog.btn_schema_alt")
                                + "\" onClick=\"document.sos_settings.button.value='schema';\">&nbsp;");
            }
            this.out
                    .println("    <input type=\"image\" name=\"btn_cancel\" value=\"cancel\" src=\""
                            + this.imgDir
                            + sosLang
                            + "/btn_cancel.gif\" alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_cancel_alt")
                            + "\" onClick=\"document.sos_settings.button.value='cancel';\">&nbsp;");
            this.out
                    .println("    <input type=\"image\" name=\"btn_reset\"  value=\"reset\"  src=\""
                            + this.imgDir
                            + sosLang
                            + "/btn_reset.gif\"  alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_reset_alt")
                            + "\" onClick=\"document.sos_settings.reset(); return false;\">&nbsp;");
        } else {

            if (this.hasCreateRight) {
                if (this.range.equals("section") && this.applicationType == 0) {
                    this.out
                            .println("    <input type=\"image\" name=\"btn_schema\" src=\""
                                    + this.imgDir
                                    + sosLang
                                    + "/btn_schema.gif\" alt=\""
                                    + this.rb
                                            .getMessage("sos.settings.dialog.btn_schema_alt")
                                    + "\" onClick=\"document.sos_settings.button.value='schema';\">&nbsp;");
                }
            }
            this.out
                    .println("    <input type=\"image\" name=\"btn_cancel\" value=\"cancel\" src=\""
                            + this.imgDir
                            + sosLang
                            + "/btn_cancel.gif\" alt=\""
                            + this.rb
                                    .getMessage("sos.settings.dialog.btn_cancel_alt")
                            + "\" onClick=\"document.sos_settings.button.value='cancel';\">&nbsp;");
        }

        this.out.println("  </td>");
        this.out.println("</tr>");

        this.showTableEnd();

        if (this.hasWriteRight == true) {
            this.out
                    .println("<script language=\"JavaScript\" type=\"text/javascript\">");
            this.out
                    .println("  var on_load = 1; if ( on_load == 1 ) { document."
                            + this.form
                            + ".name.focus(); document."
                            + this.form + ".name.focus();}");
            this.out.println("  function sos_settings_onSubmit() {");
            this.out.println(" ");
            this.out.println("    var submitFlag = true; on_load = 0;");
            this.out.println(" ");
            this.out.println("    switch( document." + this.form
                    + ".button.value ) {");
            this.out.println("     case 'cancel' :");
            this.out.println("     case 'reset'  : return submitFlag;");
            this.out.println("   }");
            this.out.println("    if ( submitFlag && ( document." + this.form
                    + ".name.value == null || document." + this.form
                    + ".name.value == \"\" ) ) {");
            this.out.println("      alert( \""
                    + this.rb
                            .getMessage("sos.settings.dialog.alert_name_empty")
                    + "\");");
            this.out.println("      document." + this.form + ".name.focus();");
            this.out.println("      submitFlag = false;");
            this.out.println("    }");
            this.out.println("    if ( submitFlag && ( document." + this.form
                    + ".title.value == null || document." + this.form
                    + ".title.value == \"\" ) ) {");
            this.out
                    .println("      alert( \""
                            + this.rb
                                    .getMessage("sos.settings.dialog.alert_title_empty")
                            + "\");");
            this.out.println("      document." + this.form + ".title.focus();");
            this.out.println("      submitFlag = false;");
            this.out.println("    }");
            this.out.println("    return submitFlag;");
            this.out.println("  }");
            this.out.println("</script>");
        }

        this.out.println("</form>");

        return true;
    }

    /**
     * Navigationsleiste anzeigen
     * 
     * @param columnIndex
     *            Anzahl Spalten der Darstellung
     * @param applicationIndex
     *            Index der aktuellen Applikation in dialog_applications()
     * @param sectionIndex
     *            Index der aktuellen Sektion in dialog_sections()
     * @param entryIndex
     *            Index des aktuellen Eintrags in dialog_entries()
     * @return boolean Fehlerzustand
     * @throws Exception
     * @see #showNavigation(Integer, Integer, Integer, Integer)
     */

    private boolean showNavigation(Integer columnIndex,
            Integer applicationIndex, Integer sectionIndex, Integer entryIndex)
            throws Exception {
        this.debug(3, "showNavigation : columnIndex = " + columnIndex
                + " applicationIndex = " + applicationIndex
                + " sectionIndex = " + sectionIndex + " entryIndex = "
                + entryIndex);

        String imgLink = "<img src=\"" + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"4\" vspace=\"1\">";
        String imgFont = "<img src=\"" + this.imgDir + this.imgNavigation
                + "\" border=\"0\" hspace=\"4\" vspace=\"1\">";
        String imgLink2 = "&nbsp;<img src=\"" + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\"><img src=\""
                + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\">&nbsp;";
        String imgFont2 = "<img src=\"" + this.imgDir + this.imgNavigation
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\"><img src=\""
                + this.imgDir + this.imgNavigation
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\">&nbsp;";
        String imgLink3 = "&nbsp;<img src=\"" + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\"><img src=\""
                + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\"><img src=\""
                + this.imgDir + this.imgAction
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\">&nbsp;";
        String imgFont3 = "<img src=\"" + this.imgDir + this.imgNavigation
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\"><img src=\""
                + this.imgDir + this.imgNavigation
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\"><img src=\""
                + this.imgDir + this.imgNavigation
                + "\" border=\"0\" hspace=\"1\" vspace=\"1\">&nbsp;";

        String title = "";

        if (applicationIndex == null || applicationIndex.intValue() < 0) {

            if (this.enableApplicationNavigation == true) {
                title = "";
                for (Enumeration e = this.settings.sources.keys(); e
                        .hasMoreElements();) {
                    String settingsSource = (String) e.nextElement();
                    String settingsTitle = (String) this.settings.sources
                            .get(settingsSource);

                    String styleLink = (settingsSource
                            .equalsIgnoreCase(this.settings.source)) ? this.styleLinkNavigation
                            : this.styleLinkInactiveNavigation;
                    title += "<a class=\""
                            + styleLink
                            + "\" href=\""
                            + this.site
                            + "?action=show&range=applications&settings_source="
                            + settingsSource + "\">" + imgLink + settingsTitle
                            + "</a>&nbsp;&nbsp;&nbsp;&nbsp;";

                }
            } else {
                title = "";
            }
        } else {
            if (this.enableApplicationNavigation) {
                title = "<a class=\"" + this.styleLinkNavigation + "\" href=\""
                        + this.site + "?action=show&range=applications\">"
                        + imgLink + this.dialogApplicationsTitle
                        + "</a>&nbsp;&nbsp;&nbsp;&nbsp;";
            } else {
                title = "";
            }

            try {
                HashMap hm1 = (HashMap) this.dialogApplications
                        .get(applicationIndex.intValue());
                String entrySettingsTitle = hm1.get(
                        this.settings.entrySettingTitle.toLowerCase())
                        .toString();
                if (sectionIndex == null || sectionIndex.intValue() < 0) {
                    title += "<p style=\"margin: 2px 0px;\">&nbsp;&nbsp;&nbsp;<a class=\""
                            + this.styleLinkNavigation
                            + "\" href=\""
                            + this.site
                            + "?action=show&range=application&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "\">"
                            + imgLink2
                            + "</a><a class=\""
                            + this.styleLinkNavigation
                            + "\" href=\""
                            + this.site
                            + "?action=show&range=sections&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "\">";
                    title += entrySettingsTitle;
                    title += "</a>&nbsp;&nbsp;&nbsp;&nbsp;</p>";
                } else {
                    title += "<p style=\"margin: 2px 0px;\">&nbsp;&nbsp;&nbsp;<a class=\""
                            + this.styleLinkNavigation
                            + "\" href=\""
                            + this.site
                            + "?action=show&range=application&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "\">"
                            + imgLink2
                            + "</a><a class=\""
                            + this.styleLinkNavigation
                            + "\" href=\""
                            + this.site
                            + "?action=show&range=sections&application="
                            + this.response
                                    .encodeURL(this.settings.application)
                            + "\">";
                    title += entrySettingsTitle;
                    title += "</a>&nbsp;&nbsp;&nbsp;&nbsp;</p>";
                    String entryType = hm1.get("entry_type").toString();
                    String section = hm1.get("section").toString();
                    String range = (entryType.equals("0") || (!section
                            .equals(this.settings.entrySchemaSection))) ? "section"
                            : "entries";

                    try {
                        HashMap hm2 = (HashMap) this.dialogSections
                                .get(sectionIndex.intValue());

                        if (hm2.get("section").toString().equals(
                                this.settings.entrySchemaSection)) {
                            entrySettingsTitle = this.rb
                                    .getMessage("sos.settings.dialog.dialog_sections_schema_title");
                        } else {
                            entrySettingsTitle = hm2.get(
                                    this.settings.entrySettingTitle
                                            .toLowerCase()).toString();
                        }
                    } catch (Exception e) {
                        this.setError(e.getMessage(), SOSClassUtil
                                .getMethodName());
                    }

                    if (entryIndex == null || entryIndex.intValue() < 0) {
                        title += "<p style=\"margin: 2px 0px;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class=\""
                                + this.styleLinkNavigation
                                + "\" href=\""
                                + this.site
                                + "?action=show&range="
                                + range
                                + "&application="
                                + this.response
                                        .encodeURL(this.settings.application)
                                + "&section="
                                + this.response
                                        .encodeURL(this.settings.section)
                                + "\">"
                                + imgLink3
                                + "</a><a class=\""
                                + this.styleLinkNavigation
                                + "\" href=\""
                                + this.site
                                + "?action=show&range=entries&application="
                                + this.response
                                        .encodeURL(this.settings.application)
                                + "&section="
                                + this.response
                                        .encodeURL(this.settings.section)
                                + "\">";
                        title += entrySettingsTitle;
                        title += "</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                    } else {
                        title += "<p style=\"margin: 2px 0px;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a class=\""
                                + this.styleLinkNavigation
                                + "\" href=\""
                                + this.site
                                + "?action=show&range="
                                + range
                                + "&application="
                                + this.response
                                        .encodeURL(this.settings.application)
                                + "\">"
                                + imgLink3
                                + "</a><a class=\""
                                + this.styleLinkNavigation
                                + "\" href=\""
                                + this.site
                                + "?action=show&range=sections&application="
                                + this.response
                                        .encodeURL(this.settings.application)
                                + "\">";
                        title += entrySettingsTitle;
                        title += "</a>&nbsp;&nbsp;&nbsp;&nbsp;";
                    }
                }

            } catch (Exception e) {
                this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            }
        }
        this.out.println("<tr class=\"" + this.styleTr
                + "\"><th align=\"left\" valign=\"top\" colspan=\""
                + columnIndex + "\" class=\"" + this.styleTh + "\">" + title
                + "</th></tr>");
        return true;
    }

    /*
     * Requestvariablen lesen
     * 
     *  
     */
    private void checkRequest() throws Exception {
        this.debug(3, "checkRequest");

        this.settings.sources.put(this.settings.source,
                this.dialogApplicationsTitle);

        this.settings.application = "";
        this.settings.section = "";
        this.settings.entry = "";

        this.inputQuery = "";
        this.inputExport = "";
        this.inputImport = "";
        this.importOriginalFileName = "";
        
        // Daten aus fileUpload
        LinkedHashMap requestMultipart = new LinkedHashMap();
      
        if (this.request != null) {
      			/////////////////////////////////////////////////////////
      			String contentType = this.request.getHeader("Content-type");
            if (contentType != null && contentType.startsWith("multipart/")) { // ob Import
                try {
                    DiskFileUpload upload = new DiskFileUpload();

                    upload.setSizeMax(this.importMaxSize);
                    upload.setSizeThreshold(0); // nicht im Memory sondern als
                    // Datei speichern

                    List items = upload.parseRequest(this.request);
                    Iterator iter = items.iterator();
                    
                    while (iter.hasNext()) {
                        //FileItem item = (FileItem) iter.next();
                        DefaultFileItem item = (DefaultFileItem) iter.next();
                        if (item.isFormField()) {
                            requestMultipart.put(item.getFieldName(), item.getString());
                            this.request.setAttribute(item.getFieldName(),item.getString());
                        } 
                        else { // aus upload
                            if (item.getName() != null && !item.getName().equals("")) {
                               	//requestMultipart.put(item.getFieldName(),item.getStoreLocation());
                               	requestMultipart.put(item.getFieldName(), item.getStoreLocation().getAbsolutePath());
                            		this.request.setAttribute(item.getFieldName(),item.getStoreLocation().getAbsolutePath());
                            		this.importOriginalFileName = item.getName();
                            } 
                            else {
                                requestMultipart.put(item.getFieldName(), "");
                                this.request.setAttribute(item.getFieldName(),"");
                            }
                            
                        }

                    }
						     } catch (FileUploadException e) {
                    this.setError(e.getMessage(), SOSClassUtil.getMethodName());
                }
            }// MULTIPART Form

 						/////////////////////////////////////////////////////////     			
      		if (this.getRequestValue("application") != null) {
                this.settings.application = this.getRequestValue("application");
            }
            if (this.getRequestValue("section") != null) {
                this.settings.section = this.getRequestValue("section");
            }
            if (this.getRequestValue("entry") != null) {
                this.settings.entry = this.getRequestValue("entry");
            }
            if (this.getRequestValue("application_type") != null) {
                try {
                    this.applicationType = Integer.parseInt(this.getRequestValue("application_type"));
                } catch (Exception e) {
                    this.applicationType = 0;
                }
            }
            if (this.getRequestValue("section_type") != null) {
                try {
                    this.sectionType = Integer.parseInt(this.getRequestValue("section_type"));
                } catch (Exception e) {
                    this.sectionType = 0;
                }
            }

            if (this.getRequestValue("action") != null) {
                this.action = this.getRequestValue("action");

            }
            if (this.getRequestValue("range") != null) {
                this.range = this.getRequestValue("range");
            }
            if (this.getRequestValue("item") != null) {
                this.item = this.getRequestValue("item");
            }
            if ((this.getRequestValue("btn_store.x") != null)
                    && (this.getRequestValue("btn_store.y") != null)) {
                this.action = "store";
            } else if ((this.getRequestValue("btn_insert.x") != null)
                    && (this.getRequestValue("btn_insert.y") != null)) {
                this.action = "insert";
            } else if ((this.getRequestValue("btn_delete.x") != null)
                    && (this.getRequestValue("btn_delete.y") != null)) {
                this.action = "delete";
            } else if ((this.getRequestValue("btn_schema.x") != null)
                    && (this.getRequestValue("btn_schema.y") != null)) {
                this.action = "schema";
            } else if ((this.getRequestValue("btn_duplicate.x") != null)
                    && (this.getRequestValue("btn_duplicate.y") != null)) {
                this.action = "duplicate";
                this.range = "entries";
            } else if ((this.getRequestValue("btn_cancel.x") != null)
                    && (this.getRequestValue("btn_cancel.y") != null)) {
                this.action = "show";
                if (this.range.equals("application")) {
                    this.range = "applications";
                } else if (this.range.equals("section")) {
                    this.range = "sections";
                } else {
                    this.range = this.range.equals("list") ? "sections"
                            : "entries";
                }
            } else if ((this.getRequestValue("btn_query.x") != null)
                    && (this.getRequestValue("btn_query.y") != null)) {
                this.action = "query";
                this.range = "entries";
                
                if(this.getRequestValue("query_select_range") != null && 
                	this.getRequestValue("query_select_range").equals("2")){
      						this.item = "replace";	
      					}
                
                
                
            } 
            else if ((this.getRequestValue("btn_export.x") != null)
                    && (this.getRequestValue("btn_export.y") != null)) {
                this.action = "export";
                this.range = "entries";
            } 
            else if ((this.getRequestValue("btn_import.x") != null)
                    && (this.getRequestValue("btn_import.y") != null)) {
                this.action = "import";
                this.range = "entries";
            }
            else if ((this.getRequestValue("btn_clipboard_copy.x") != null)
                    && (this.getRequestValue("btn_clipboard_copy.y") != null)) {
                if (this.getRequestValue("last_action") != null) {
                    this.action = this.getRequestValue("last_action");
                } else {
                    this.action = "show";
                }
                this.clipboardAction = "copy";
            } else if ((this.getRequestValue("btn_clipboard_paste.x") != null)
                    && (this.getRequestValue("btn_clipboard_paste.y") != null)) {
                if (this.getRequestValue("last_action") != null) {
                    this.action = this.getRequestValue("last_action");
                } else {
                    this.action = "show";
                }

                this.clipboardAction = "paste";
            } else if ((this.getRequestValue("btn_import_file.x") != null)
                    && (this.getRequestValue("btn_import_file.y") != null)) {

                this.action = ((this.getRequestValue("last_action") != null) && this.getRequestValue("last_action").equals("new")) ? "insert"
                        : "store";
                this.range = "entry";
                this.item = "upload";
            }

            if (this.getRequestValue("input_query") != null) {
                this.inputQuery = this.getRequestValue("input_query");
            }
            
            if (this.getRequestValue("input_query_replace") != null) {
                this.replaceQuery = this.getRequestValue("input_query_replace");
            }
            
            if (this.getRequestValue("input_export") != null) {
                this.inputExport = this.getRequestValue("input_export");
            }

            if (this.getRequestValue("export_documentation") != null) {
                this.exportDocumentation = 1;
            }
            
            if(this.getRequestValue("input_import") != null) {
            	this.inputImport = this.getRequestValue("input_import");
          	}
            
        }
        if (this.applicationName.equals("")) {
            this.applicationName = this.settings.application;
        }

				if(this.enableShowDevelopmentData){
					this.showDevelopmentData(requestMultipart);	
				}

    }

    /**
     * 
     * 
     * @param name
     * @return
     */
    public String getRequestValue(String name){
        String value = null;
        if(this.request.getParameter(name) != null){
				     return this.request.getParameter(name);
        }
        else if(this.request.getAttribute(name) != null){
            return (String)this.request.getAttribute(name);
        }
        
    return value;
    }
    
    
    /**
     * Anzeige des Werts eines Eintrags
     * 
     * @param entry
     *            HashMap mit Feldern des Eintrags
     * @param type
     *            Typ 0=Eintrag, 2=Listeneintrag
     * @param index
     *            Reihenfolgeindex
     * @see #showDialogValue(HashMap, int, int)
     */

    private void showDialogValue(HashMap entry, int type, int index)
            throws Exception {
        this.debug(3, "showDialogValue : entry = " + entry + " type = " + type
                + " index = " + index);

        String inputName = (type > 0) ? "value_" + index : "value";

        try {
            int inputSize = Integer
                    .parseInt(entry.get("input_size").toString());
            if (inputSize < 0 || inputSize == 0) {
                entry.put("input_size", this.defaultInputSize);
            }
        } catch (Exception e) {
            entry.put("input_size", this.defaultInputSize);
        }

        try {
            int displaySize = Integer.parseInt(entry.get("display_size")
                    .toString());
            if (displaySize < 0 || displaySize == 0) {
                entry.put("display_size", this.defaultDisplaySize);
            }
        } catch (Exception e) {
            entry.put("display_size", this.defaultDisplaySize);
        }

        String checked = "";
        String unchecked = "";

        int textareaRows = this.displayTextareaRows;

        if (entry.get("input_type").toString().equals("5")) { // long_value
            entry.put("input_size", "");
            textareaRows = this.displayDocumentTextareaRows;
        }

        switch (entry.get("display_type").toString().charAt(0)) {

      case '4': // hidden bei Document binär
              
              String has_binary_value = "";
              try{
              	has_binary_value = this.hasBinaryValue.get(entry.get("application").toString()+entry.get("section").toString()+entry.get("name").toString()).toString();
              }
              catch(Exception e){}
              
              if(has_binary_value != null && has_binary_value.length() > 0 && !has_binary_value.equals("0")){
                
                String session 		= (this.sessionID.length() > 0 ) 	? "&"+this.sessionVAR+"="+this.sessionID 	: "";
            		String entry_href = (this.settings.entry.length()>0) 				? "&entry="+this.settings.entry 										: ""; 
            	  
            	  String default_value	= entry.get("default_value").toString();
            	  String file_name = (default_value.length()>0) ? default_value : this.settings.defaultDocumentFileName;
                
            	  
                String source_href = "file_name="+file_name+"&application="+this.response.encodeURL(this.settings.application)+"&section="+this.response.encodeURL(this.settings.section)+"&download_entry="+this.response.encodeURL(entry.get("name").toString());
                source_href +="&application_type="+this.applicationType+"&old_action="+this.action+session+entry_href;
                
                this.out.println("<a href=\""+this.site+"?action=download&range=source&"+source_href+"\"><img src=\""+this.imgDir+"icon_download.gif\" border=\"0\" alt=\"Download "+file_name+"\" title=\"Download "+file_name+"\"></a>");
                this.out.println("&nbsp;&nbsp;");
                this.out.println(this.rb.getMessage("sos.settings.dialog.label_empty"));
                
                String js = "if(typeof document."+this.form+"."+inputName+"  != 'undefined'){";
                js+= " if(this.checked == true){";
                js+= "   document."+this.form+"."+inputName+".disabled = true;";
                js+= " }";
                js+= " else{";
                js+= "   document."+this.form+"."+inputName+".disabled = false;";
                js+= " }";
                js+= "}";
                
                this.out.println("<input onclick=\""+js+"\" type=\"checkbox\" name=\"binary_"+inputName+"\"  value=\"1\" />");
              }
              else{
               this.out.println("");
              }
              
              if(!this.isShowEntries){
                this.out.println("<textarea "+this.disabled+" style=\"display:none;width:600px;height:300px\" name=\""+inputName+"\" class=\""+this.styleInput+"\">"+this.htmlSpecialChars(entry.get("value").toString())+"</textarea>&nbsp;");
              }
              
              this.displayBinaryUpload = "";
              break;

        case '3':
            // textarea

            this.out.println("<textarea " + this.disabled + " name=\""
                    + inputName + "\" class=\"" + this.styleInput
                    + "\" rows=\"" + textareaRows + "\" cols=\""
                    + entry.get("display_size").toString() + "\" maxlength=\""
                    + entry.get("input_size").toString() + "\">"
                    + this.htmlSpecialChars(entry.get("value").toString())
                    + "</textarea>&nbsp;");
            break;

        case '2':
            // radiobutton
            //checked = ( !entry.get("value").toString().equals("") ||
            // !entry.get("value").toString().equals("0")) ? " checked" : "";
            checked = (entry.get("value").toString().equals("1")) ? " checked"
                    : "";
            unchecked = (!checked.equals("")) ? "" : " checked";

            this.out.println("<input " + this.disabled + " name=\"" + inputName
                    + "\" type=\"radio\" class=\"" + this.styleInput
                    + "\" value=\"1\" " + checked + ">Ja&nbsp;");
            this.out.println("<input " + this.disabled + " name=\"" + inputName
                    + "\" type=\"radio\" class=\"" + this.styleInput
                    + "\" value=\"0\" " + unchecked + ">Nein&nbsp;");

            break;

        case '1':
            // listbox

            StringTokenizer options = new StringTokenizer(entry.get(
                    "default_value").toString(), ";");

            this.out.println("<select " + this.disabled + " name=\""
                    + inputName + "\" class=\"" + this.styleInput + "\">");
            while (options.hasMoreTokens()) {
                String next = options.nextToken();
                String selected = (entry.get("value").toString().equals(next)) ? " selected"
                        : "";
                this.out.println("<option value=\"" + next + "\" " + selected
                        + ">" + next);
            }
            this.out.println("</select>");

            break;

        default:
            // input
            this.out.println("<input " + this.disabled + " name=\"" + inputName
                    + "\" type=\"text\" class=\"" + this.styleInput
                    + "\" size=\"" + entry.get("display_size").toString()
                    + "\" maxlength=\"" + entry.get("input_size").toString()
                    + "\" value=\"" + entry.get("value").toString() + "\">");
        }

        if (type > 0) {
            this.out.println("<input name=\"application_" + index
                    + "\" type=\"hidden\" value=\""
                    + entry.get("application").toString() + "\">");
            this.out.println("<input name=\"section_" + index
                    + "\" type=\"hidden\" value=\""
                    + entry.get("section").toString() + "\">");
            this.out.println("<input name=\"name_" + index
                    + "\" type=\"hidden\" value=\""
                    + entry.get("name").toString() + "\">");
            this.out.println("<input name=\"title_" + index
                    + "\" type=\"hidden\" value=\""
                    + entry.get("title").toString() + "\">");
        }

    }

    /**
     * Alle Applikationen lesen
     * 
     * @param source
     *            Name der Tabelle
     * @return Vector Zweidimensionale Liste der Applikationen
     * @throws Exception
     * @see #getDialogApplications(String)
     */

    private Vector getDialogApplications(String source) throws Exception {
        this.debug(3, "getDialogApplications : source = " + source);

        this.connection.setKeysToLowerCase();

        if (source != null) {
            this.settings.source = source;
        }

        this.dialogApplications = new Vector();

        StringBuffer sqlStmt = new StringBuffer();

        sqlStmt
                .append(" select s.\"APPLICATION\", s.\"SECTION\", s.\"NAME\", s.\"TITLE\", s.\"ENTRY_TYPE\" ");
        sqlStmt.append(" from " + this.settings.source + " s ");
        sqlStmt.append(" where s.\"" + this.settings.entryApplication
                + "\"  = s.\"" + this.settings.entrySection + "\"  and ");
        sqlStmt.append("       s.\"" + this.settings.entryApplication
                + "\"  = s.\"" + this.settings.entryName + "\" ");
        sqlStmt
                .append(" order by s.\"" + this.settings.entryApplication
                        + "\"");

        return this.dialogApplications = this.connection
                .getArrayAsVector(sqlStmt.toString());
    }

    /**
     * Alle Sektionen einer Applikation lesen
     * 
     * @param application
     *            Name der Applikation
     * @param source
     *            Name der Tabelle
     * @return Vector Zweidimensionale Liste der Sektionen
     * @throws Exception
     * @see #getDialogSections(String, String)
     */

    private int getDialogSections(String application, String source)
            throws Exception {
        this.debug(3, "getDialogSections : application = " + application
                + " source = " + source);

        if (application != null) {
            this.settings.application = application;
        }
        if (source != null) {
            this.settings.source = source;
        }

        this.dialogSections = new Vector();

        String sqlStmt = "select s.\"APPLICATION\", s.\"SECTION\", s.\"NAME\", s.\"TITLE\", s.\"ENTRY_TYPE\" from "
                + this.settings.source
                + " s where s.\""
                + this.settings.entryApplication
                + "\"="
                + this.dbQuoted(this.settings.application)
                + " and s.\""
                + this.settings.entryApplication
                + "\"<>s.\""
                + this.settings.entrySection
                + "\" and s.\""
                + this.settings.entrySection
                + "\"=s.\""
                + this.settings.entryName
                + "\" order by s.\""
                + this.settings.entrySection + "\" asc";

        this.dialogSections = this.connection.getArrayAsVector(sqlStmt);

        return this.dialogSections.size();
    }

    /**
     * Alle Einträge einer Sektion lesen
     * 
     * @param application
     *            Name der Applikation
     * @param section
     *            Name der Applikation
     * @param source
     *            Name der Tabelle
     * @return Vector Zweidimensionale Liste der Entries
     * @throws Exception
     * @see #getDialogEntries(String, String, String)
     */

    private Vector getDialogEntries(String application, String section,
            String source) throws Exception {
        this.debug(3, "getDialogEntries : application = " + application
                + " section = " + section + " source = " + source);

        if (application != null) {
            this.settings.application = application;
        }
        if (section != null) {
            this.settings.section = section;
        }
        if (source != null) {
            this.settings.source = source;
        }

        this.dialogEntries = new Vector();

        StringBuffer sqlStmt = new StringBuffer();

        sqlStmt
                .append(" select \"APPLICATION\",\"SECTION\",\"NAME\",\"VALUE\",\"DEFAULT_VALUE\",\"TITLE\",\"INPUT_TYPE\",\"INPUT_SIZE\",\"DISPLAY_TYPE\",\"DISPLAY_SIZE\",\"FORCED\",\"ENTRY_TYPE\",\"CREATED\",\"CREATED_BY\",\"MODIFIED\",\"MODIFIED_BY\" ");
        sqlStmt.append(" from " + this.settings.source + " ");
        sqlStmt.append(" where \"" + this.settings.entryApplication + "\"  = "
                + this.dbQuoted(this.settings.application) + " and ");
        sqlStmt.append("       \"" + this.settings.entrySection + "\"      = "
                + this.dbQuoted(this.settings.section) + " and ");
        sqlStmt.append("       \"" + this.settings.entrySection
                + "\"     <> \"" + this.settings.entryName + "\" ");
        sqlStmt.append(" order by " + this.getEntryOrder() + "");

        return this.dialogEntries = this.connection.getArrayAsVector(sqlStmt
                .toString());
    }

    /**
     * Tabellenkopf ausgeben
     * 
     * Um gleichmässige Einrückungen auf einer Seite zu erhalten, wird ein
     * Tabellenkopf geschrieben und nicht beendet
     */

    private void showTableBegin() throws Exception {
        this.debug(3, "showTableBegin");

        this.out.println("<table width=\"100%\" border=\"0\" class=\""
                + this.styleTable + "\">");
        this.out.println("  <tr class=\"" + this.styleTr + "\">");
        this.out.println("    <td width=\"1%\">&nbsp;</td>");
        this.out.println("    <td width=\"98%\" class=\""
                + this.styleTdBackground + "\">");
        this.out.println("      <table width=\"100%\" border=\""
                + this.styleBorder
                + "\" cellPadding=\"5\" cellSpacing=\"1\" class=\""
                + this.styleTable + "\">");
    }

    /**
     * Tabellenfuß ausgeben
     * 
     * Um gleichmässige Einrückungen auf einer Seite zu erhalten, wird ein
     * Tabellenkopf beendet, der zuvor begonnen wurde
     */

    private void showTableEnd() throws Exception {
        this.debug(3, "showTableEnd");

        this.out.println("      </table>");
        this.out.println("    </td>");
        this.out.println("    <td width=\"1%\">&nbsp;</td>");
        this.out.println("  </tr>");
        this.out.println("</table>");
    }

    /**
     * Einträge exportieren
     * 
     * @param file
     *            Ausgabedatei
     * @param source
     *            Name der Tabelle
     * @return boolean Fehlerzustand
     */

    private void exportEntries(String file, String source) throws Exception {
        if (source != null) {
            this.settings.source = source;
        }

        StringBuffer sql = new StringBuffer();
        file = file.trim();

        if (this.exportDocumentation == 1) {
            sql
                    .append("select \"APPLICATION\",\"SECTION\",\"NAME\",\"DOCUMENTATION\" from "
                            + this.settings.source);
        } else {
            sql
                    .append("select \"APPLICATION\",\"SECTION\",\"NAME\",\"VALUE\",\"DEFAULT_VALUE\",\"LONG_VALUE\",\"DOCUMENTATION\",\"TITLE\",\"INPUT_TYPE\",\"INPUT_SIZE\",\"DISPLAY_TYPE\",\"DISPLAY_SIZE\",\"FORCED\",\"ENTRY_TYPE\",\"CREATED\",\"CREATED_BY\",\"MODIFIED\",\"MODIFIED_BY\" from "
                            + this.settings.source);
        }

        String sqlAnd = " where ";

        if (!this.settings.application.equals("")) {
            sql.append(" " + sqlAnd + " \"" + this.settings.entryApplication
                    + "\" = " + this.dbQuoted(this.settings.application));
            sqlAnd = " and ";
        }

        if (!this.settings.section.equals("")) {
            sql.append(" " + sqlAnd + " \"" + this.settings.entrySection
                    + "\" = " + this.dbQuoted(this.settings.section));
            sqlAnd = " and ";
        }

        sql.append(" order by \"" + this.settings.entryApplication
                + "\" asc,\"" + this.settings.entrySection + "\" asc,\""
                + this.settings.entryName + "\" asc");

        this.debug(3, "exportEntries: file = " + file + " source = " + source
                + " stmt = " + sql.toString());

        
        //SOSStandardLogger logger = new
        // SOSStandardLogger("c:/settings_import_log.txt",SOSStandardLogger.DEBUG9);
        SOSStandardLogger logger = new SOSStandardLogger(
                SOSStandardLogger.DEBUG9);
        try {

            SOSExport export = new SOSExport(this.connection, file,
                    this.applicationName, logger);
            export.query(this.settings.source, this.settings.entryApplication
                    + "," + this.settings.entrySection + ","
                    + this.settings.entryName, sql.toString());

            String rc = export.doExport();

            if (!rc.equals("") && file.equals("")) {
                this.response.setContentType("application/xml");
                this.response.setHeader("Content-Disposition",
                        "attachment; filename=settings.xml");

                this.response.getWriter().print(rc);
                this.response.getWriter().close();
            }
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
        }

    }

    /**
     * Einträge importieren
     * 
     * @param string
     *            $file Eingabedatei
     * @param string
     *            $source Name der Tabelle
     * @return boolean Fehlerzustand
     * @version 1.0-2002/10/20
     */

    private void importEntries(String file, String source) throws Exception {

        if (file == null || file.trim().equals("")) {
            this
                    .setError(
                            "&nbsp;&nbsp;&nbsp;&nbsp;"
                                    + this.rb
                                            .getMessage("sos.settings.dialog.err_import_file_missing"),
                            SOSClassUtil.getMethodName());
            return;
        }

        if (source != null) {
            this.settings.source = source;
        }

        try {
            //file = "c:/settings.xml";
            //SOSStandardLogger logger = new
            // SOSStandardLogger("c:/settings_import_log.txt",SOSStandardLogger.DEBUG9);
            SOSStandardLogger logger = new SOSStandardLogger(
                    SOSStandardLogger.DEBUG9);

            SOSImport imp = new SOSImport(this.connection, file, null, null,
                    null, logger);
            //SOSImport imp = new SOSImport(
            // this.connection,"c:/1.xml",null,null,null,logger);

            if (this.importDocumentation == 1) {
                imp.setInsert(false);
            }
            imp.doImport();
            this.connection.commit();
        } catch (Exception e) {
            this.setError(e.getMessage(), SOSClassUtil.getMethodName());
            this.connection.rollback();
        }
    }

    /**
     * Klasseneigenschaften zurücksetzen
     *  
     */
    private void destroy() throws Exception {
        this.debug(3, "destroy");

        this.dialogSections = new Vector();
        this.dialogEntries = new Vector();
        this.isNew = false;
        this.record = new HashMap();
        this.dialogApplicationIndex = new Integer(-1);
        this.dialogSectionIndex = new Integer(-1);
        this.dialogEntryIndex = new Integer(-1);
        this.hasHelps = false;
        this.entriesLongValues = new Vector();
        this.hasAcls = false;
        this.hasCreateRight = true;
        this.hasDeleteRight = true;
        this.hasReadRight = true;
        this.hasWriteRight = true;
        this.topLevelAcl = "";
        this.hasTopLevelCreateRight = true;
        this.hasTopLevelReadRight = true;
        this.hasTopLevelWriteRight = true;
        this.aclRange = "application";
        this.isShowEntries = false;
        this.hasBinaryValue = new Hashtable();

    }

    /**
     * HTML Header setzen
     * 
     * @param title
     *            Html Element Titel
     * @throws Exception
     * @see #showHeader(String)
     */
    public void showHeader(String title) throws Exception {
        this.debug(3, "showHeader : title = " + title);

        if (title != null) {
            this.title = title;
        }

        this.response.setContentType("text/html");

        this.out.println("<html>");
        this.out.println("  <head>");
        this.out
                .println("  	<meta http-equiv=\"content-type\" content=\"text/html; charset=iso-8859-1\">");
        this.out
                .println("    <meta name=\"generator\"   content=\"FrameWork Factory 1.0\">");
        this.out
                .println("    <meta name=\"author\"      content=\"SOS GmbH\">");
        this.out
                .println("    <meta name=\"publisher\"   content=\"Software- und Organisations- Service GmbH (SOS), Berlin\">");
        this.out
                .println("    <meta name=\"copyright\"   content=\"Copyright 2004 SOFTWARE UND ORGANISATIONS-SERVICE GmbH (SOS), Berlin. All rights reserved.\">");
        this.out
                .println("    <meta name=\"description\" content=\"Document Factory\">");
        this.out.println("    <meta name=\"keywords\"    content=\"Java\">");
        this.out.println("    <title>" + this.title + "</title>");
        this.out
                .println("    <link rel=\"stylesheet\" type=\"text/css\" href=\"site.css\">");
        if (this.enableDocuTemplateCss) {
            this.out
                    .println("    <link rel=\"stylesheet\" type=\"text/css\" href=\"settings_docu_template.css\">");
        }
        this.out.println("    <style type=\"text/css\"><!--");
        this.out.println("      @import url(settings.css);");
        this.out.println("     //--></style>");
        this.out
                .println("    <script language=\"JavaScript\" type=\"text/javascript\" src=\"site.js\"></script>");
        this.out.println("</head>");
        this.out.println("<body>");
    }

    /**
     * HTML schlissen
     *  
     */
    public void showFooter() throws Exception {
        this.debug(3, "showFooter");

        this.out.println("</body>\n</html>");
        this.out.flush();
    }

    /**
     * Fehler auslösen
     * 
     * @param msg
     *            Text der Fehlernachricht
     * @see #setError(String, String)
     */

    private void setError(String msg) {
        this.setError(msg, null);
    }

    /**
     * Fehler auslösen
     * 
     * @param msg
     *            Text der Fehlernachricht
     * @param location
     *            Klassenname und die Zeile
     * @see #setError(String, String)
     */

    private void setError(String msg, String location) {

        this.err = true;
        this.errLocation = "";

        if (msg != null && !msg.equals("")) {
            this.errMsg = msg;
        }

        if (location != null && !location.equals("")) {
            this.errLocation = location;
        }

    }

    /**
     * Fehlerzustand feststellen
     * 
     * @return boolean Fehlerzustand
     */

    private boolean error() {

        return this.err;
    }

    /**
     * Fehlermeldung liefern
     * 
     * Liest einen Fehlerzustand und liefert einen Meldungsstring bestehend aus
     * den Eigenschaften der Basisklasse SOS_Class mit Fehlercode, -meldung,
     * exception.
     * 
     * @return string Fehlermeldung oder null, falls kein Fehler
     */

    private String getError() {

        if (this.err == true) {
            String location = "";
            if (this.debugLevel > 0) {
                if (this.errLocation != null && !errLocation.equals("")) {
                    location = " [" + errLocation + "]";
                }
            }
            return this.errMsg + location;
        } else {
            return null;
        }
    }

    /**
     * Fehler zurücksetzen
     *  
     */

    private void resetError() {

        this.err = false;
        this.errMsg = "";
        this.errLocation = "";
    }

    /**
     * Fehlermeldung anzeigen
     * 
     * @param msg
     *            Text der Nachricht über fehlende Berechtigung
     * @see #showError(String)
     */

    private void showError(String msg) throws Exception {

        this.out
                .println("<table width=\"100%\"><tr><td width=\"5\">&nbsp;</td><td><font style=\"color: #FF6347; font-size: 14px;\">"
                        + msg + "</font></td></tr></table>");
    }

    /**
     * Nachricht anzeigen
     * 
     * @param msg
     *            Text der Nachricht über fehlende Berechtigung
     * @see #showMsg(String)
     */

    private void showMsg(String msg) throws Exception {

        this.out
                .println("<table width=\"100%\"><tr><td width=\"5\">&nbsp;</td><td><font style=\"color: #009933;\">"
                        + msg + "</font></td></tr></table>");
    }

    /**
     * ACL pro bestimmte Applikation|Sektion|Entrie definieren
     * 
     * @param aclName
     *            ACL Identifier
     * @param settings
     *            HashMap der ACL Geltungsbereichen <br>
     *            gültige HashMap Keys : "application","section","entry" <br>
     *            Beispiele :<br>
     *            <br>
     *            1)für alle Applikationen <br>
     *            settings.put("application","*"); <br>
     *            setAcl("acl_name",settings); <br>
     *            um die Anzeige von allen Applikationen zu schützen <br>
     * @see #setTopLevelAcl(String) <br>
     *      <br>
     *      2) eine Bestimmte Applikation schützen <br>
     *      settings.put("application","applikation_name"); <br>
     *      setAcl("acl_name",settings); <br>
     *      <br>
     *      3)eine bestimmte Sektion einer bestimmten Applikation schützen <br>
     *      für alle Sektionen einer Applikation kann statt "section_name" ->
     *      "*" angegeben werden <br>
     *      settings.put("application","applikation_name"); <br>
     *      settings.put("section","section_name"); <br>
     *      setAcl("acl_name",settings); <br>
     *      <br>
     *      4)einen bestimmten Wert einer bestimmten Sektion einer bestimmten
     *      Applikation schützen <br>
     *      für alle Entries einer Sektion einer Applikation kann statt
     *      "entrie_name" -> "*" angegeben werden <br>
     *      settings.put("application","applikation_name"); <br>
     *      settings.put("section","section_name"); <br>
     *      settings.put("entrie","entrie_name"); <br>
     *      setAcl("acl_name",settings); <br>
     *      <br>
     * @throws Exception
     * @see #setAcl(String, HashMap)
     */
    public void setAcl(String aclName, HashMap settings) throws Exception {
        this.debug(3, "setAcl : aclName = " + aclName + " settings = "
                + settings);

        if (aclName == null || aclName.equals("")) { throw new Exception(
                this.rb.getMessage("sos.settings.dialog.err_acl_missing_name")); }

        if (settings == null || settings.size() == 0) { throw new Exception(
                this.rb.getMessage(
                        "sos.settings.dialog.err_acl_undefined_scope", aclName)); }

        if (settings.containsKey("application")) {
            int count = settings.size();
            if (count == 1) { // Applikations
                this.allAcls.put("[" + settings.get("application").toString()
                        + "][application]", aclName);
            } else if (count == 2) { // Sektions
                if (settings.containsKey("section")) {
                    this.allAcls
                            .put("[" + settings.get("application").toString()
                                    + "][section]["
                                    + settings.get("section").toString() + "]",
                                    aclName);
                } else {
                    throw new Exception(this.rb.getMessage(
                            "sos.settings.dialog.err_acl_undefined_section",
                            aclName));
                }
            } else if (count == 3) { // Entries
                if (settings.containsKey("section")
                        && settings.containsKey("entry")) {
                    this.allAcls.put("["
                            + settings.get("application").toString()
                            + "][entry][" + settings.get("section").toString()
                            + "][" + settings.get("entry").toString() + "]",
                            aclName);
                } else {
                    throw new Exception(
                            this.rb
                                    .getMessage(
                                            "sos.settings.dialog.err_acl_undefined_section_or_entry",
                                            aclName));
                }
            } else {
                throw new Exception(this.rb.getMessage(
                        "sos.settings.dialog.err_acl_invalid_scope", aclName));
            }

        } else {
            throw new Exception(this.rb.getMessage(
                    "sos.settings.dialog.err_acl_undefined_application",
                    aclName));
        }

        if (this.allAcls != null && this.allAcls.size() != 0) {
            this.hasAcls = true;
        }

    }

    /**
     * Rechte für die erste Ebene (Alle Applikationen) lesen
     * 
     * @return boolean Fehlerzustand
     */

    private boolean getTopLevelRights() throws Exception {
        this.debug(3, "getTopLevelRights");

        this.hasTopLevelWriteRight = true;
        this.hasTopLevelCreateRight = true;
        this.hasTopLevelReadRight = true;

        if (!this.topLevelAcl.equals("")) {
            if (this.user == null) { throw new Exception(this.rb
                    .getMessage("sos.settings.dialog.err_acl_missing_user")); }

            try {
                sos.acl.SOSAcl acl = new sos.acl.SOSAcl(this.topLevelAcl);

                acl.setLocale(this.locale);

                if (this.debugLevel > 0) {
                    acl.setOut(this.out);
                    acl.setDebugLevel(this.debugLevel);
                }
                acl.setTableAcls(this.tableAcls);
                acl.setConnection(this.connection);

                acl.get(this.topLevelAcl);

                acl.access(this.user.getUserId(), this.user
                        .getUserGroupIdList(), "rwdc");
                String op = acl.getResultOperations();

                if (op.indexOf('w') == -1) {
                    this.hasTopLevelWriteRight = false;
                    this.disabled = " disabled ";
                }

                if (op.indexOf('c') == -1) {
                    this.hasTopLevelCreateRight = false;
                }

                if (op.indexOf('r') == -1) {
                    this.hasTopLevelReadRight = false;
                }
            } catch (NoClassDefFoundError e) {
                throw new Exception("Class not found (sos.acl.jar) : "
                        + e.getMessage());
            } catch (Exception e) {
                throw new Exception(SOSClassUtil.getMethodName() + " : "
                        + e.getMessage());
            }
        }

        return true;
    }

    /**
     * Rechte auf Default-Werte setzen
     *  
     */
    private void resetRights() throws Exception {
        this.debug(3, "resetRights");

        this.hasWriteRight = true;
        this.hasReadRight = true;
        this.hasDeleteRight = true;
        this.hasCreateRight = true;
        this.disabled = "";

    }

    /**
     * Rechte setzen
     *  
     */

    private void setRights(sos.acl.SOSAcl acl) throws Exception {

        if (acl == null) { throw new Exception(this.rb.getMessage(
                "sos.settings.dialog.err_acl_missing_acl", SOSClassUtil
                        .getMethodName())); }
        if (this.user == null) { throw new Exception(this.rb
                .getMessage("sos.settings.dialog.err_acl_missing_user")); }

        acl.access(this.user.getUserId(), this.user.getUserGroupIdList(),
                "rwdc");

        String aclName = acl.getIdentifier();

        String op = acl.getResultOperations();

        if (op.indexOf('w') == -1) {
            this.hasWriteRight = false;
            this.disabled = " disabled ";
        }

        if (op.indexOf('d') == -1) {
            this.hasDeleteRight = false;
        }

        if (op.indexOf('c') == -1) {
            this.hasCreateRight = false;
        }

        if (op.indexOf('r') == -1) {
            this.hasReadRight = false;
        }

        this.debug(3, "setRights : acl(" + aclName + ") resultOperations = "
                + op);
        this.debug(3, "setRights : write = " + this.hasWriteRight
                + " delete = " + this.hasDeleteRight + " create = "
                + this.hasCreateRight + " read = " + this.hasReadRight);
    }

    /**
     * Rechte lesen
     * 
     * @param application
     *            Name der Applikation
     * @param section
     *            Name der Sektion
     * @param entry
     *            Name des Entries
     * @return boolean Fehlerzustand
     */
    private boolean getRights(String application, String section, String entry)
            throws Exception {
        this.debug(3, "getRights : application = " + application
                + " section = " + section + " entry = " + entry
                + " this.aclRange = " + this.aclRange);

        this.resetRights();

        if (this.hasAcls) {
            if (this.aclRange.equals("application")) {
                //  Rechte einer bestimmten Applikation
                if (this.allAcls.containsKey("[" + application
                        + "][application]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][application]").toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }

                } // Rechte für alle Applikationen
                else if (this.allAcls.containsKey("[" + this.allAclNote
                        + "][application]")) {
                    String identifier = this.allAcls.get(
                            "[" + this.allAclNote + "][application]")
                            .toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }

            }// range application
            else if (this.aclRange.equals("section")) {
                // Rechte für eine bestimmte Sektion einer bestimmten
                // Applikation
                if (this.allAcls.containsKey("[" + application + "][section]["
                        + section + "]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][section][" + section + "]")
                            .toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }//Rechte für alle Sektionen einer bestimmten Applikation
                else if (this.allAcls.containsKey("[" + application
                        + "][section][" + this.allAclNote + "]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][section][" + this.allAclNote
                                    + "]").toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }//  Rechte einer bestimmten Applikation
                else if (this.allAcls.containsKey("[" + application
                        + "][application]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][application]").toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                } // Rechte für alle Applikationen
                else if (this.allAcls.containsKey("[" + this.allAclNote
                        + "][application]")) {
                    String identifier = this.allAcls.get(
                            "[" + this.allAclNote + "][application]")
                            .toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }
            }// range section
            else if (this.aclRange.equals("entry")) {
                //Rechte für einen bestimmten Entry in einer bestimmten Sektion
                // einer bestimmten Applikation
                if (this.allAcls.containsKey("[" + application + "][entry]["
                        + section + "][" + entry + "]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][entry][" + section + "]["
                                    + entry + "]").toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }//  Rechte für alle Entry in einer bestimmten Sektion einer
                // bestimmten Applikation
                else if (this.allAcls.containsKey("[" + application
                        + "][entry][" + section + "][" + this.allAclNote + "]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][entry][" + section + "]["
                                    + this.allAclNote + "]").toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }// Rechte für eine bestimmte Sektion einer bestimmten
                // Applikation
                else if (this.allAcls.containsKey("[" + application
                        + "][section][" + section + "]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][section][" + section + "]")
                            .toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }//Rechte für alle Sektionen einer bestimmten Applikation
                else if (this.allAcls.containsKey("[" + application
                        + "][section][" + this.allAclNote + "]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][section][" + this.allAclNote
                                    + "]").toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }//  Rechte einer bestimmten Applikation
                else if (this.allAcls.containsKey("[" + application
                        + "][application]")) {
                    String identifier = this.allAcls.get(
                            "[" + application + "][application]").toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                } // Rechte für alle Applikationen
                else if (this.allAcls.containsKey("[" + this.allAclNote
                        + "][application]")) {
                    String identifier = this.allAcls.get(
                            "[" + this.allAclNote + "][application]")
                            .toString();
                    try {
                        sos.acl.SOSAcl acl = new sos.acl.SOSAcl(identifier);

                        acl.setLocale(this.locale);

                        if (this.debugLevel > 0) {
                            acl.setOut(this.out);
                            acl.setDebugLevel(this.debugLevel);
                        }
                        acl.setTableAcls(this.tableAcls);
                        acl.setConnection(this.connection);

                        acl.get(identifier);
                        this.setRights(acl);

                    } catch (NoClassDefFoundError e) {
                        throw new Exception("Class not found (sos.acl.jar) : "
                                + e.getMessage());
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    }
                }

            }// range entry
            else {
                throw new Exception(
                        this.rb
                                .getMessage("sos.settings.dialog.err_acls_invalid_scope"));
            }

        }// if hasAcl

        return true;
    }

    /**
     * @param value
     * @throws Exception
     */
    private void showEditor(String value) throws Exception {

        if (value == null) {
            value = "";
        }

        try {
            sos.fckeditor.SOSFCKEditor editor = new sos.fckeditor.SOSFCKEditor(
                    this.editorName);

            editor.setLanguage(this.sosLang);
            editor.setWidth(this.editorWidth);
            editor.setValue(value);
            editor.show(this.request, this.out);

        } catch (NoClassDefFoundError e) {
            throw new Exception("class not found : " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("editor : " + e.getMessage());
        }

    }


    /**
     * !!! unter Produktion muß nich verwendet werden <br/>Anzeige von aktuellen
     * Request,Session usw Variablen
     * 
     * @param request
     * @param out
     * @throws IOException
     */
    public void showDevelopmentData(LinkedHashMap requestMultipart)
            throws Exception {

        this.out.println("<div id='div_temp_request' style=\"background-color:#ffffff;width:400px; overflow: auto; POSITION: absolute; Z-INDEX: 20; VISIBILITY: visible; TOP: "+this.divDevelopmentDataTop+"; LEFT: "+this.divDevelopmentDataLeft+"; border:solid 2px #dddddd;\">");
        
        this.out.println("<table width=\"100%\" cellPadding=\"0\" cellSpacing=\"0\">");
        
        this.out.println("<tr>");
        this.out.println("	<td colspan=\"2\"><b>action</b> = "+this.action+" <b>range</b> = "+this.range+" <b>item</b> = "+this.item+"</td>");
        this.out.println("</tr>");
        
        this.out.println("<tr>");
        this.out.println("	<td colspan=\"2\"><b>application</b> = "+this.settings.application+" <b>section</b> = "+this.settings.section+" <b>entry</b> = "+this.settings.entry+"</td>");
        this.out.println("</tr>");
        
        this.out.println("<tr><td colspan=\"2\">&nbsp;</td></tr>");
        
        this.out.println("<tr>");
        this.out.println("	<td colspan=\"2\"><b>REQUEST PARAMETER: </b></td>");
        this.out.println("</tr>");
        this.out.println("<tr>");
     		this.out.println("	<td>");
     		this.out.println("	<table width=\"100%\" cellPadding=\"0\" cellSpacing=\"0\">");
        Enumeration en = this.request.getParameterNames();
        while (en.hasMoreElements()) {
            String a = (String) en.nextElement();
        		this.out.println("<tr>");
     				this.out.println("	<td><b>"+a+"</b></td>");
     	  		this.out.println(" 	<td>"+this.request.getParameter(a)+"</td>");
       			this.out.println("</tr>");
        }
        this.out.println("	</table>");
        this.out.println("	</td>");
        this.out.println("	<td>&nbsp;</td>");
       	this.out.println("</tr>");
        this.out.println("<tr><td colspan=\"2\">&nbsp;</td></tr>");
        
        
        this.out.println("<tr>");
        this.out.println("	<td width=\"50%\"><b>REQUEST ATTRIBUTE</b></td>");
        this.out.println("	<td><b>MULTIPART-REQUEST</b></td>");
        this.out.println("</tr>");
        this.out.println("<tr>");
     		this.out.println("	<td>");
     		this.out.println("	<table width=\"100%\" cellPadding=\"0\" cellSpacing=\"0\">");
        Enumeration ena = this.request.getAttributeNames();
        int r = 0;
        while (ena.hasMoreElements()) {
            String a = (String) ena.nextElement();
        		this.out.println("<tr>");
     				this.out.println("	<td><b>"+a+"</b></td>");
     	  		this.out.println(" 	<td>"+(String)this.request.getAttribute(a)+"</td>");
       			this.out.println("</tr>");
        		r++;
        }
        this.out.println("	</table>");
        this.out.println("	</td>");
        this.out.println("	<td>");
        int m = 0;
        if(requestMultipart != null && requestMultipart.size() > 0){
       		 this.out.println("	<table width=\"100%\" cellPadding=\"0\" cellSpacing=\"0\">");
           Iterator it = requestMultipart.entrySet().iterator(); 
            while (it.hasNext()) {
              Map.Entry entry = (Map.Entry)it.next();
		      		this.out.println("<tr>");
     					this.out.println("	<td><b>"+(String)entry.getKey()+"</b></td>");
     	  			this.out.println(" 	<td>"+(String)entry.getValue()+"</td>");
       				this.out.println("</tr>");
		    			m++;
		    		}
		    	this.out.println("	</table>");
        }
		    this.out.println("	</td>");
     		this.out.println("</tr>");
     		this.out.println("<tr><td>"+r+"</td><td>"+m+"</td></tr>");
        this.out.println("<tr><td colspan=\"2\">&nbsp;</td></tr>");
        
        this.out.println("<tr>");
        this.out.println("	<td colspan=\"2\"><b>Session: </b></td>");
        this.out.println("</tr>");
        this.out.println("<tr>");
        this.out.println("	<td colspan=\"2\"><b>ID = </b>"+this.session.getId()+"</td>");
        this.out.println("</tr>");
        if(this.request.isRequestedSessionIdFromCookie()){
        	this.out.println("<tr>");
        	this.out.println("	<td colspan=\"2\">Session aus Cookie</td>");
        	this.out.println("</tr>");
        }
        if(this.request.isRequestedSessionIdFromURL()){
        	this.out.println("<tr>");
        	this.out.println("	<td colspan=\"2\">Session aus URL</td>");
        	this.out.println("</tr>");
        }
        
        this.out.println("<tr>");
        this.out.println("	<td colspan=\"2\">");
        if(this.session.isNew()){ 
        	this.out.print("neue Session"); 
        }
        else{ 
        	this.out.print("alte Session"); 
        } 
        this.out.println("	</td>");
        this.out.println("</tr>");
        
        
        Enumeration sessionAttributeNames = this.session.getAttributeNames();
        
        
        //this.session.setAttribute("SOS 1","SOS value");
        //String[] a = {"aa","bb","cc"};
        //this.session.setAttribute("SOS 2",a);
        
        this.out.println("<tr>");
     		this.out.println("	<td colspan=\"2\">");
        while ( sessionAttributeNames.hasMoreElements() ) { 
        	String  sessionAttribute = (String) sessionAttributeNames.nextElement();
        	Object obj = this.session.getAttribute(sessionAttribute);
        	this.out.print( "<b>" + sessionAttribute + "</b>  = "+obj+"<br />"); 
       	}
       	this.out.println("	</td>");
     		this.out.println("</tr>");
        this.out.println("</table>");
        
        this.out.println("</div>");

    }


    /**
     * Liefert Debuglevel
     * 
     * @return debugLevel Debuglevel.
     */
    public int getDebugLevel() {
        return debugLevel;
    }

    /**
     * Debuglevel setzen
     * 
     * @param debugLevel
     *            Debuglevel.
     */
    public void setDebugLevel(int debugLevel) {
        this.debugLevel = debugLevel;
    }

    /**
     * Liefert Sprache der Anwendung (de,en ...)
     * 
     * @return sosLang default : de
     */
    public String getLanguage() {
        return sosLang;
    }

    /**
     * Liefert Voreinstellung für created_name, modified_name bei Änderung von
     * Einstellungen
     * 
     * @return Name des Autors.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Voreinstellung für created_name, modified_name bei Änderung von
     * Einstellungen
     * 
     * @param author
     *            Name des Autors.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Liefert HTML Titel
     * 
     * @return title Titel
     */
    public String getTitle() {
        return title;
    }

    /**
     * HTML Titel setzen
     * 
     * @param title
     *            Titel
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Liefert Titel der Liste aller Applicationen
     * 
     * @return dialogApplicationsTitle Titel z.B:(Alle Einstellungen).
     */
    public String getDialogApplicationsTitle() {
        return dialogApplicationsTitle;
    }

    /**
     * Titel der Liste aller Applicationen setzen
     * 
     * @param dialogApplicationsTitle
     *            Titel z.B:(Alle Einstellungen).
     */
    public void setDialogApplicationsTitle(String dialogApplicationsTitle) {

        this.dialogApplicationsTitle = dialogApplicationsTitle;
    }

    /**
     * Liefert Titel für neue Applikation
     * 
     * @return dialogApplicationsNewTitle Titel z.B: (Neuer Bereich)
     */
    public String getDialogApplicationsNewTitle() {
        return dialogApplicationsNewTitle;
    }

    /**
     * Titel für neue Applikation setzen
     * 
     * @param dialogApplicationsNewTitle
     *            Titel z.B: (Neuer Bereich)
     */
    public void setDialogApplicationsNewTitle(String dialogApplicationsNewTitle) {
        this.dialogApplicationsNewTitle = dialogApplicationsNewTitle;
    }

    /**
     * Liefert Titel der Liste aller Sektionen
     * 
     * @return dialogSectionsTitle Titel z.B: Einstellungen des Bereichs
     */
    public String getDialogSectionsTitle() {
        return dialogSectionsTitle;
    }

    /**
     * Titel der Liste aller Sektionen setzen
     * 
     * @param dialogSectionsTitle
     *            Titel z.B: Einstellungen des Bereichs
     */
    public void setDialogSectionsTitle(String dialogSectionsTitle) {
        this.dialogSectionsTitle = dialogSectionsTitle;
    }

    /**
     * Liefert Titel für neue Sektion
     * 
     * @return dialogSectionsNewTitle Titel z.B: (Neue Sektion)
     */
    public String getDialogSectionsNewTitle() {
        return dialogSectionsNewTitle;
    }

    /**
     * Titel für neue Sektion setzen
     * 
     * @param dialogSectionsNewTitle
     *            Titel z.B: (Neue Sektion)
     */
    public void setDialogSectionsNewTitle(String dialogSectionsNewTitle) {
        this.dialogSectionsNewTitle = dialogSectionsNewTitle;
    }

    /**
     * Setzt Titel für neue Schema-Sektion
     * 
     * @return dialogSectionsSchemaTitle Titel z.B: (Schema des Bereichs)
     */
    public String getDialogSectionsSchemaTitle() {
        return dialogSectionsSchemaTitle;
    }

    /**
     * Titel für neue Schema-Sektion setzen
     * 
     * @param dialogSectionsSchemaTitle
     *            Titel z.B: (Schema des Bereichs)
     */
    public void setDialogSectionsSchemaTitle(String dialogSectionsSchemaTitle) {
        this.dialogSectionsSchemaTitle = dialogSectionsSchemaTitle;
    }

    /**
     * Liefert Titel für neue Zähler-Sektion
     * 
     * @return dialogSectionsCounterTitle Titel z.B: Nr. des letzten Eintrags
     *         für Tabelle
     */
    public String getDialogSectionsCounterTitle() {
        return dialogSectionsCounterTitle;
    }

    /**
     * Titel für neue Zähler-Sektion setzen
     * 
     * @param dialogSectionsCounterTitle
     *            Titel z.B: Nr. des letzten Eintrags für Tabelle
     */
    public void setDialogSectionsCounterTitle(String dialogSectionsCounterTitle) {
        this.dialogSectionsCounterTitle = dialogSectionsCounterTitle;
    }

    /**
     * Liefert Titel der Liste aller Einträge
     * 
     * @return dialogEntriesTitle Titel z.B: Einstellungen der Sektion
     */
    public String getDialogEntriesTitle() {
        return dialogEntriesTitle;
    }

    /**
     * Titel der Liste aller Einträge setzen
     * 
     * @param dialogEntriesTitle
     *            Titel z.B: Einstellungen der Sektion
     */
    public void setDialogEntriesTitle(String dialogEntriesTitle) {
        this.dialogEntriesTitle = dialogEntriesTitle;
    }

    /**
     * Liefert Titel für neuen Eintrag
     * 
     * @return dialogEntriesNewTitle Titel z.B: (Neuer Eintrag)
     */
    public String getDialogEntriesNewTitle() {
        return dialogEntriesNewTitle;
    }

    /**
     * Titel für neuen Eintrag setzen
     * 
     * @param dialogEntriesNewTitle
     *            Titel z.B: (Neuer Eintrag)
     */
    public void setDialogEntriesNewTitle(String dialogEntriesNewTitle) {
        this.dialogEntriesNewTitle = dialogEntriesNewTitle;
    }

    /**
     * Liefert Titel für Export einer Applikation
     * 
     * @return dialogApplicationsExportTitle Titel z.B: (Alle Einträge dieses
     *         Bereichs)
     */
    public String getDialogApplicationsExportTitle() {
        return dialogApplicationsExportTitle;
    }

    /**
     * Titel für Export einer Applikation setzen
     * 
     * @param dialogApplicationsExportTitle
     *            Titel z.B: (Alle Einträge dieses Bereichs)
     */
    public void setDialogApplicationsExportTitle(
            String dialogApplicationsExportTitle) {
        this.dialogApplicationsExportTitle = dialogApplicationsExportTitle;
    }

    /**
     * Liefert Titel für Export einer Sektion
     * 
     * @return dialogSectionsExportTitle Titel z.B: (Alle Einträge dieser
     *         Sektion)
     */
    public String getDialogSectionsExportTitle() {
        return dialogSectionsExportTitle;
    }

    /**
     * Titel für Export einer Sektion setzen
     * 
     * @param dialogSectionsExportTitle
     *            Titel z.B: (Alle Einträge dieser Sektion)
     */
    public void setDialogSectionsExportTitle(String dialogSectionsExportTitle) {
        this.dialogSectionsExportTitle = dialogSectionsExportTitle;
    }

    /**
     * Liefert Name der Web-Seite
     * 
     * @return site Name der Web-Seite
     */
    public String getSite() {
        return site;
    }

    /**
     * Name der Web-Seite setzen
     * 
     * @param site
     *            Name der Web-Seite
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * Anzeige von "Dokumentieren" Button zulassen
     * 
     * @return enableDocumentation false - nicht zulassen
     */
    public boolean isEnableDocumentation() {
        return enableDocumentation;
    }

    /**
     * Anzeige von "Dokumentieren" Button zulassen
     * 
     * @param enableDocumentation
     *            false - nicht zulassen
     */
    public void setEnableDocumentation(boolean enableDocumentation) {
        this.enableDocumentation = enableDocumentation;
    }

    /**
     * Liefert Name der JSP Datei für die Anzeige der Dokumentation
     * 
     * @return documentationFile default settings_show_docu.jsp
     */
    public String getDocumentationFile() {
        return documentationFile;
    }

    /**
     * JSP Datei für die Anzeige der Dokumentation
     * 
     * @param documentationFile
     *            default settings_show_docu.jsp
     */
    public void setDocumentationFile(String documentationFile) {
        this.documentationFile = documentationFile;
    }

    /**
     * Liefert Order by in dem Dokufenster
     * 
     * @return documentationSort default : "APPLICATION" asc, "SECTION" asc,
     *         "NAME" asc
     */
    public String getDocumentationSort() {
        return documentationSort;
    }

    /**
     * Order by in dem Dokufenster
     * 
     * @param documentationSort
     *            default : "APPLICATION" asc, "SECTION" asc, "NAME" asc
     */
    public void setDocumentationSort(String documentationSort) {
        this.documentationSort = documentationSort;
    }

    /**
     * Liefert Höhe des Dokufensters
     * 
     * @return docuWinHeight Höhe in px
     */
    public int getDocuWinHeight() {
        return docuWinHeight;
    }

    /**
     * Höhe des Dokufensters
     * 
     * @param docuWinHeight
     *            Höhe in px
     */
    public void setDocuWinHeight(int docuWinHeight) {
        this.docuWinHeight = docuWinHeight;
    }

    /**
     * Liefert Breite des Dokufensters
     * 
     * @return docuWinWidth Breite in px
     */
    public int getDocuWinWidth() {
        return docuWinWidth;
    }

    /**
     * Breite des Dokufensters
     * 
     * @param docuWinWidth
     *            Breite in px
     */
    public void setDocuWinWidth(int docuWinWidth) {
        this.docuWinWidth = docuWinWidth;
    }

    /**
     * Template CSS Datei für die Dokuanzeige importieren
     * 
     * @return enableTemplateCss default false : nicht importieren
     */
    public boolean isEnableDocuTemplateCss() {
        return enableDocuTemplateCss;
    }

    /**
     * Template CSS Datei für die Dokuanzeige importieren
     * 
     * @param enableTemplateCss
     *            default false : nicht importieren
     */
    public void setEnableDocuTemplateCss(boolean enableDocuTemplateCss) {
        this.enableDocuTemplateCss = enableDocuTemplateCss;
    }

    /**
     * JSP Datei für die Anzeige von Hilfetexten
     * 
     * @return helpFile Name der Datei default : settings_show_help.jsp
     */
    public String getHelpFile() {
        return helpFile;
    }

    /**
     * JSP Datei für die Anzeige von Hilfetexten
     * 
     * @param helpFile
     *            Name der Datei default : settings_show_help.jsp
     */
    public void setHelpFile(String helpFile) {
        this.helpFile = helpFile;
    }

    /**
     * Höhe des Hilfefensters
     * 
     * @return helpWinHeight Höhe in px
     */
    public int getHelpWinHeight() {
        return helpWinHeight;
    }

    /**
     * Höhe des Hilfefensters
     * 
     * @param helpWinHeight
     *            Höhe in px
     */
    public void setHelpWinHeight(int helpWinHeight) {
        this.helpWinHeight = helpWinHeight;
    }

    /**
     * Breite des Hilfefensters
     * 
     * @return helpWinWidth Breite in px
     */
    public int getHelpWinWidth() {
        return helpWinWidth;
    }

    /**
     * Breite des Hilfefensters
     * 
     * @param helpWinWidth
     *            Breite in px
     */
    public void setHelpWinWidth(int helpWinWidth) {
        this.helpWinWidth = helpWinWidth;
    }

    /**
     * Applikationsverwaltung zulassen
     * 
     * @return enableApplicationManager default true : zulassen
     */
    public boolean isEnableApplicationManager() {
        return enableApplicationManager;
    }

    /**
     * Applikationsverwaltung zulassen
     * 
     * @param enableApplicationManager
     *            default true : zulassen
     */
    public void setEnableApplicationManager(boolean enableApplicationManager) {
        this.enableApplicationManager = enableApplicationManager;
    }

    /**
     * Navigieren in Applikationen zulassen
     * 
     * @return enableApplicationNavigation default true : zulassen
     */
    public boolean isEnableApplicationNavigation() {
        return enableApplicationNavigation;
    }

    /**
     * Navigieren in Applikationen zulassen
     * 
     * @param enableApplicationNavigation
     *            default true : zulassen
     */
    public void setEnableApplicationNavigation(
            boolean enableApplicationNavigation) {
        this.enableApplicationNavigation = enableApplicationNavigation;
    }

    /**
     * Sektionsverwaltung zulassen
     * 
     * @return enableSectionManager default true : zulassen
     */
    public boolean isEnableSectionManager() {
        return enableSectionManager;
    }

    /**
     * Sektionsverwaltung zulassen
     * 
     * @param enableSectionManager
     *            default true : zulassen
     */
    public void setEnableSectionManager(boolean enableSectionManager) {
        this.enableSectionManager = enableSectionManager;
    }

    /**
     * Eintragsverwaltung zulassen
     * 
     * @return enableEntryManager default true : zulassen
     */
    public boolean isEnableEntryManager() {
        return enableEntryManager;
    }

    /**
     * Eintragsverwaltung zulassen
     * 
     * @param enableEntryManager
     *            default true : zulassen
     */
    public void setEnableEntryManager(boolean enableEntryManager) {
        this.enableEntryManager = enableEntryManager;
    }

    /**
     * Anzeige der Namen von Einträgen in Listen zulassen
     * 
     * @return enableEntryNames default true : zulassen
     */
    public boolean isEnableEntryNames() {
        return enableEntryNames;
    }

    /**
     * Anzeige der Namen von Einträgen in Listen zulassen
     * 
     * @param enableEntryNames
     *            default true : zulassen
     */
    public void setEnableEntryNames(boolean enableEntryNames) {
        this.enableEntryNames = enableEntryNames;
    }

    /**
     * Ändern der Werte von Einträgen in Listen zulassen
     * 
     * @return enableEntryValues default true : zulassen
     */
    public boolean isEnableEntryValues() {
        return enableEntryValues;
    }

    /**
     * Ändern der Werte von Einträgen in Listen zulassen
     * 
     * @param enableEntryValues
     *            default true : zulassen
     */
    public void setEnableEntryValues(boolean enableEntryValues) {
        this.enableEntryValues = enableEntryValues;
    }

    /**
     * Export zulassen
     * 
     * @return enableExport default true : zulassen
     */
    public boolean isEnableExport() {
        return enableExport;
    }

    /**
     * Export zulassen
     * 
     * @param enableExport
     *            default true : zulassen
     */
    public void setEnableExport(boolean enableExport) {
        this.enableExport = enableExport;
    }

    /**
     * Import zulassen
     * 
     * @return enableImport default true : zulassen
     */
    public boolean isEnableImport() {
        return enableImport;
    }

    /**
     * Import zulassen
     * 
     * @param enableImport
     *            default true : zulassen
     */
    public void setEnableImport(boolean enableImport) {
        this.enableImport = enableImport;
    }

    /**
     * Inhalt des Headers setzen
     * 
     * @param headerContent
     *            Inhalt
     */
    public void setHeaderContent(String headerContent) {
        this.headerContent = headerContent;
    }

    /**
     * Liefert Anzahl Pixel für Anzeige eines Eintrags (default 50)
     * 
     * @return Default: Anzahl Pixel für Anzeige eines Eintrags
     */
    public String getDefaultDisplaySize() {
        return defaultDisplaySize;
    }

    /**
     * Anzahl Pixel für Anzeige eines Eintrags setzen (default 50)
     * 
     * @param defaultDisplaySize
     *            Anzahl Pixel.
     */
    public void setDefaultDisplaySize(String defaultDisplaySize) {
        this.defaultDisplaySize = defaultDisplaySize;
    }

    /**
     * Liefert Anzahl Zeichen für Eingabe eines Eintrags (default 250)
     * 
     * @return defaultInputSize Anzahl Zeichen.
     */
    public String getDefaultInputSize() {
        return defaultInputSize;
    }

    /**
     * Anzahl Zeichen für Eingabe eines Eintrags setzen (default 250)
     * 
     * @param defaultInputSize
     *            Anzahl Zeichen.
     */
    public void setDefaultInputSize(String defaultInputSize) {
        this.defaultInputSize = defaultInputSize;
    }

    /**
     * Liefert Anzahl Zeilen bei Darstellung von Textareas
     * 
     * @return displayTextareaRows Anzahl Zeilen
     */
    public int getDisplayTextareaRows() {
        return displayTextareaRows;
    }

    /**
     * Anzahl Zeilen bei Darstellung von Textareas setzen
     * 
     * @param displayTextareaRows
     *            Anzahl Zeilen
     */
    public void setDisplayTextareaRows(int displayTextareaRows) {
        this.displayTextareaRows = displayTextareaRows;
    }

    /**
     * Liefert Anzahl Zeilen bei Darstellung von Document (long_value) Textareas
     * 
     * @return displayDocumentTextareaRows Anzahl Zeilen
     */
    public int getDisplayDocumentTextareaRows() {
        return displayDocumentTextareaRows;
    }

    /**
     * Anzahl Zeilen bei Darstellung von Document (long_value) Textareas setzen
     * 
     * @param displayDocumentTextareaRows
     *            Anzahl Zeilen
     */
    public void setDisplayDocumentTextareaRows(int displayDocumentTextareaRows) {
        this.displayDocumentTextareaRows = displayDocumentTextareaRows;
    }

    /**
     * Liefert Imageverzeichnis der Anwendung
     * 
     * @return imgDir Name des Verzeichnisses
     */
    public String getImgDir() {
        return imgDir;
    }

    /**
     * Imageverzeichnis der Anwendung setzen
     * 
     * @param imgDir
     *            Name des Verzeichnisses
     */
    public void setImgDir(String imgDir) {
        this.imgDir = imgDir;
    }

    /**
     * Graphik für Aktionen
     * 
     * @return imgAction Dateiname
     */
    public String getImgAction() {
        return imgAction;
    }

    /**
     * Graphik für Aktionen
     * 
     * @param imgAction
     *            Dateiname
     */
    public void setImgAction(String imgAction) {
        this.imgAction = imgAction;
    }

    /**
     * Graphik für Seitenende
     * 
     * @return imgBottom Dateiname
     */
    public String getImgBottom() {
        return imgBottom;
    }

    /**
     * Graphik für Seitenende
     * 
     * @param imgBottom
     *            Dateiname
     */
    public void setImgBottom(String imgBottom) {
        this.imgBottom = imgBottom;
    }

    /**
     * Graphik für Hilfe
     * 
     * @return imgHelp Dateiname
     */
    public String getImgHelp() {
        return imgHelp;
    }

    /**
     * Graphik für Hilfe
     * 
     * @param imgHelp
     *            Dateiname
     */
    public void setImgHelp(String imgHelp) {
        this.imgHelp = imgHelp;
    }

    /**
     * Graphik für Navigation
     * 
     * @return imgNavigation The imgNavigation to set.
     */
    public String getImgNavigation() {
        return imgNavigation;
    }

    /**
     * Graphik für Navigation
     * 
     * @param imgNavigation
     *            The imgNavigation to set.
     */
    public void setImgNavigation(String imgNavigation) {
        this.imgNavigation = imgNavigation;
    }

    /**
     * Graphik für Seitenanfang
     * 
     * @return imgTop Dateiname
     */
    public String getImgTop() {
        return imgTop;
    }

    /**
     * Graphik für Seitenanfang
     * 
     * @param imgTop
     *            Dateiname
     */
    public void setImgTop(String imgTop) {
        this.imgTop = imgTop;
    }

    /**
     * CSS-Style: Tabellenränder 1/0
     * 
     * @return styleBorder default : 1
     */
    public String getStyleBorder() {
        return styleBorder;
    }

    /**
     * CSS-Style: Tabellenränder 1/0
     * 
     * @param styleBorder
     *            default : 1
     */
    public void setStyleBorder(String styleBorder) {
        this.styleBorder = styleBorder;
    }

    /**
     * CSS-Klasse für Aktionen via Font
     * 
     * @return styleFontAction CSS-Klasse
     */
    public String getStyleFontAction() {
        return styleFontAction;
    }

    /**
     * CSS-Klasse für Aktionen via Font
     * 
     * @param styleFontAction
     *            CSS-Klasse
     */
    public void setStyleFontAction(String styleFontAction) {
        this.styleFontAction = styleFontAction;
    }

    /**
     * CSS-Klasse für Aktionen via Font
     * 
     * @return styleFontNavigation CSS-Klasse
     */
    public String getStyleFontNavigation() {
        return styleFontNavigation;
    }

    /**
     * CSS-Klasse für Aktionen via Font
     * 
     * @param styleFontNavigation
     *            CSS-Klasse
     */
    public void setStyleFontNavigation(String styleFontNavigation) {
        this.styleFontNavigation = styleFontNavigation;
    }

    /**
     * CSS-Style INPUT.settings
     * 
     * @return styleInput CSS-Klasse
     */
    public String getStyleInput() {
        return styleInput;
    }

    /**
     * CSS-Style INPUT.settings
     * 
     * @param styleInput
     *            CSS-Klasse
     */
    public void setStyleInput(String styleInput) {
        this.styleInput = styleInput;
    }

    /**
     * CSS-Klasse für Aktionen via Link
     * 
     * @return styleLinkAction CSS-Klasse
     */
    public String getStyleLinkAction() {
        return styleLinkAction;
    }

    /**
     * CSS-Klasse für Aktionen via Link
     * 
     * @param styleLinkAction
     *            CSS-Klasse
     */
    public void setStyleLinkAction(String styleLinkAction) {
        this.styleLinkAction = styleLinkAction;
    }

    /**
     * CSS-Klasse für Aktionen via Link
     * 
     * @return styleLinkInactiveNavigation CSS-Klasse
     */
    public String getStyleLinkInactiveNavigation() {
        return styleLinkInactiveNavigation;
    }

    /**
     * CSS-Klasse für Aktionen via Link
     * 
     * @param styleLinkInactiveNavigation
     *            CSS-Klasse
     */
    public void setStyleLinkInactiveNavigation(
            String styleLinkInactiveNavigation) {
        this.styleLinkInactiveNavigation = styleLinkInactiveNavigation;
    }

    /**
     * CSS-Klasse für Aktionen via Link
     * 
     * @return styleLinkNavigation CSS-Klasse
     */
    public String getStyleLinkNavigation() {
        return styleLinkNavigation;
    }

    /**
     * CSS-Klasse für Aktionen via Link
     * 
     * @param styleLinkNavigation
     *            CSS-Klasse
     */
    public void setStyleLinkNavigation(String styleLinkNavigation) {
        this.styleLinkNavigation = styleLinkNavigation;
    }

    /**
     * CSS-Style TABLE.settings
     * 
     * @return styleTable CSS-Klasse
     */
    public String getStyleTable() {
        return styleTable;
    }

    /**
     * CSS-Style TABLE.settings
     * 
     * @param styleTable
     *            CSS-Klasse
     */
    public void setStyleTable(String styleTable) {
        this.styleTable = styleTable;
    }

    /**
     * CSS-Style TD.settings
     * 
     * @return styleTd CSS-Klasse
     */
    public String getStyleTd() {
        return styleTd;
    }

    /**
     * CSS-Style TD.settings
     * 
     * @param styleTd
     *            CSS-Klasse
     */
    public void setStyleTd(String styleTd) {
        this.styleTd = styleTd;
    }

    /**
     * CSS-Style TD.settings
     * 
     * @return styleTdBackground CSS-Klasse
     */
    public String getStyleTdBackground() {
        return styleTdBackground;
    }

    /**
     * CSS-Style TD.settings
     * 
     * @param styleTdBackground
     *            CSS-Klasse
     */
    public void setStyleTdBackground(String styleTdBackground) {
        this.styleTdBackground = styleTdBackground;
    }

    /**
     * CSS-Style TD.settings
     * 
     * @return styleTdLabel CSS-Klasse
     */
    public String getStyleTdLabel() {
        return styleTdLabel;
    }

    /**
     * CSS-Style TD.settings
     * 
     * @param styleTdLabel
     *            CSS-Klasse
     */
    public void setStyleTdLabel(String styleTdLabel) {
        this.styleTdLabel = styleTdLabel;
    }

    /**
     * CSS-Style TH.settings
     * 
     * @return styleTh CSS-Klasse
     */
    public String getStyleTh() {
        return styleTh;
    }

    /**
     * CSS-Style TH.settings
     * 
     * @param styleTh
     *            CSS-Klasse
     */
    public void setStyleTh(String styleTh) {
        this.styleTh = styleTh;
    }

    /**
     * CSS-Style TR.settings
     * 
     * @return styleTr CSS-Klasse
     */
    public String getStyleTr() {
        return styleTr;
    }

    /**
     * CSS-Style TR.settings
     * 
     * @param styleTr
     *            CSS-Klasse
     */
    public void setStyleTr(String styleTr) {
        this.styleTr = styleTr;
    }

    /**
     * Import-Funktion: Dateigröße beschränken, -1 = beliebig
     * 
     * @return importMaxSize Dateigrösse
     */
    public long getImportMaxSize() {
        return importMaxSize;
    }

    /**
     * Import-Funktion: Dateigröße beschränken, -1 = beliebig
     * 
     * @param importMaxSize
     *            Dateigrösse
     */
    public void setImportMaxSize(long importMaxSize) {
        this.importMaxSize = importMaxSize;
    }

    /**
     * Liefert ACL Identifier
     * 
     * @return Returns the topLevelAcl.
     */
    public String getTopLevelAcl() {
        return topLevelAcl;
    }

    /**
     * Setzt ACL für die Anzeige von allen Applikationen bzw "neue Applikation"
     * 
     * @param topLevelAcl
     *            ACL identifier.
     */
    public void setTopLevelAcl(String topLevelAcl) {
        this.topLevelAcl = topLevelAcl;
    }

    /**
     * Liefert User Objekt
     * 
     * @return User Objekt.
     */
    public sos.user.SOSUser getUser() {
        return user;
    }

    /**
     * Setzt ein SOSUser Objekt
     * 
     * @param user
     *            SOSUser Objekt.
     * @throws Exception
     */
    public void setUser(Object user) throws Exception {

        try {
            this.user = (sos.user.SOSUser) user;
        } catch (ClassCastException e) {
            throw new Exception("user is not valid SOSUser Object."
                    + e.getMessage());
        } catch (NoClassDefFoundError e) {
            throw new Exception("Class not found (sos.user.jar) : "
                    + e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 
     *  
     */
    private String dbQuoted(String value) {
        if (value == null) return "NULL";

        value = value.replaceAll("'", "''");

        if (this.connection instanceof sos.connection.SOSMySQLConnection) { // nur
            // bei
            // MySQL
            value = value.replaceAll("\\\\", "\\\\\\\\"); // einfache durch
            // doppelte
        }

        return "'" + value + "'";
    }

    /**
     * Liefert Tabelle der ACLs
     * 
     * @return Tabelle der ACLs.
     */
    public String getTableAcls() {
        return tableAcls;
    }

    /**
     * Setzt Tabelle der ACLs
     * 
     * @param tableAcls
     *            Tabelle der ACLs.
     */
    public void setTableAcls(String tableAcls) {
        this.tableAcls = tableAcls;
    }

    /**
     * Liefert Zeichen für alle Applikationen|Sektionen|Entries (default "*")
     * bei der Verwendung von ACLs
     * 
     * @return Returns the Zeichen für alle Applikationen|Sektionen|Entries.
     */
    public String getAllAclNote() {
        return allAclNote;
    }

    /**
     * Setzt Zeichen für alle Applikationen|Sektionen|Entries (default "*") bei
     * der Verwendung von ACLs
     * 
     * @param allAclNote
     *            Zeichen für alle Applikationen|Sektionen|Entries.
     */
    public void setAllAclNote(String allAclNote) {
        this.allAclNote = allAclNote;
    }

    /**
     * Liefert Zustand Anzeige von Hilfetexten zulassen
     * 
     * @return Returns the enableHelps.
     */
    public boolean isEnableHelps() {
        return enableHelps;
    }

    /**
     * Setzt Zustand Anzeige von Hilfetexten zulassen
     * 
     * @param enableHelps
     *            The enableHelps to set.
     */
    public void setEnableHelps(boolean enableHelps) {
        this.enableHelps = enableHelps;
    }

    /**
     * Listenverwaltung zulassen
     * 
     * @return Listenverwaltung zulassen.
     */
    public boolean isEnableListManager() {
        return enableListManager;
    }

    /**
     * Listenverwaltung zulassen
     * 
     * @param enableListManager
     *            Listenverwaltung zulassen.
     */
    public void setEnableListManager(boolean enableListManager) {
        this.enableListManager = enableListManager;
    }

    /**
     * Liefert Name des Formular-Elements
     * 
     * @return Name des Formular-Elements.
     */
    public String getForm() {
        return form;
    }

    /**
     * Setzt Name des Formular-Elements
     * 
     * @param form
     *            Name des Formular-Elements.
     */
    public void setForm(String form) {
        this.form = form;
    }

    /**
     * Liefert Höhe des JS-Hilfefensters
     * 
     * @return Höhe des JS-Hilfefensters.
     */
    public int getHelpsWinHeight() {
        return helpsWinHeight;
    }

    /**
     * Setzt Höhe des JS-Hilfefensters
     * 
     * @param helpsWinHeight
     *            Höhe des JS-Hilfefensters.
     */
    public void setHelpsWinHeight(int helpsWinHeight) {
        this.helpsWinHeight = helpsWinHeight;
    }

    /**
     * Liefert Breite des JS-Hilfefensters
     * 
     * @return Breite des JS-Hilfefensters.
     */
    public int getHelpsWinWidth() {
        return helpsWinWidth;
    }

    /**
     * Setzt Breite des JS-Hilfefensters
     * 
     * @param helpsWinWidth
     *            Breite des JS-Hilfefensters.
     */
    public void setHelpsWinWidth(int helpsWinWidth) {
        this.helpsWinWidth = helpsWinWidth;
    }

    /**
     * Liefert aktuelle Session ID
     * 
     * @return Returns the sessionID.
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * Setzt Session ID
     * 
     * @param sessionID
     *            The sessionID to set.
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * Liefert Query-Parameter für Session-ID (default JSESSIONID)
     * 
     * @return Query-Parameter für Session-ID.
     */
    public String getSessionVAR() {
        return sessionVAR;
    }

    /**
     * Setzt Query-Parameter für Session-ID (default JSESSIONID)
     * 
     * @param sessionVAR
     *            Query-Parameter für Session-ID.
     */
    public void setSessionVAR(String sessionVAR) {
        this.sessionVAR = sessionVAR;
    }

    /**
     * Liefert Sortierfeld(er) für Anzeige der Einträge
     * 
     * @return Sortierfeld(er) für Anzeige der Einträge.
     */
    public String getEntryOrder() {
        return this.settings.getEntryOrder();
    }

    /**
     * Setzt Sortierfeld(er) für Anzeige der Einträge
     * 
     * @param entryOrder
     *            Sortierfeld(er) für Anzeige der Einträge. Default : NAME
     */
    public void setEntryOrder(String entryOrder) {

        this.settings.setEntryOrder(entryOrder);
    }

    /**
     * Liefert den Name der Anwendung
     * 
     * @return Name der Anwendung.
     */
    public String getApplication() {
        return this.settings.getApplication();
    }

    /**
     * Setzt den Name der Anwendung
     * 
     * @param application
     *            Name der Anwendung. Default : Leerstring
     */
    public void setApplication(String application) {
        this.settings.setApplication(application);
    }

    /**
     * Liefert den Name der Applikation für Zählervariablen
     * 
     * @return Name der Applikation für Zählervariablen.
     */
    public String getEntryCounterApplication() {
        return this.settings.getEntryCounterApplication();
    }

    /**
     * Setzt den Name der Applikation für Zählervariablen
     * 
     * @param entryCounterApplication
     *            Name der Applikation für Zählervariablen. Default : counter
     */
    public void setEntryCounterApplication(String entryCounterApplication) {
        this.settings.setEntryCounterApplication(entryCounterApplication);
    }

    /**
     * Lifert den Name der Sektion für Zählervariablen
     * 
     * @return Name der Sektion für Zählervariablen.
     */
    public String getEntryCounterSection() {
        return this.settings.getEntryCounterSection();
    }

    /**
     * Setzt den Name der Sektion für Zählervariablen
     * 
     * @param entryCounterSection
     *            Name der Sektion für Zählervariablen. Default : counter
     */
    public void setEntryCounterSection(String entryCounterSection) {
        this.settings.setEntryCounterSection(entryCounterSection);
    }

    /**
     * Liefert den Name der Sektion für Master Schemata
     * 
     * @return Name der Sektion für Master Schemata
     */
    public String getEntrySchemaSection() {
        return this.settings.getEntrySchemaSection();
    }

    /**
     * Setzt den Name der Sektion für Master Schemata
     * 
     * @param entrySchemaSection
     *            Name der Sektion für Master Schemata. Default : **schema**
     */
    public void setEntrySchemaSection(String entrySchemaSection) {
        this.settings.setEntrySchemaSection(entrySchemaSection);
    }

    /**
     * Liefert das Feld für Titels der Einträge
     * 
     * @return Returns the entrySettingTitle.
     */
    public String getEntrySettingTitle() {
        return this.getEntrySettingTitle();
    }

    /**
     * Setzt das Feld für Titels der Einträge
     * 
     * @param entrySettingTitle
     *            Feld für Titels der Einträge.
     */
    public void setEntrySettingTitle(String entrySettingTitle) {
        this.settings.setEntrySettingTitle(entrySettingTitle);
    }

    /**
     * @return Returns the dateFormat.
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * @param dateFormat
     *            The dateFormat to set.
     */
    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * @return Returns the enableEditor.
     */
    public boolean isEnableEditor() {
        return enableEditor;
    }

    /**
     * @param enableEditor
     *            The enableEditor to set.
     */
    public void setEnableEditor(boolean enableEditor) {
        this.enableEditor = enableEditor;
    }

    /**
     * @return Returns the source.
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     *            The source to set.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return Returns the editorHeight.
     */
    public String getEditorHeight() {
        return editorHeight;
    }

    /**
     * @param editorHeight
     *            The editorHeight to set.
     */
    public void setEditorHeight(String editorHeight) {
        this.editorHeight = editorHeight;
    }

    /**
     * @return Returns the editorName.
     */
    public String getEditorName() {
        return editorName;
    }

    /**
     * @param editorName
     *            The editorName to set.
     */
    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    /**
     * @return Returns the locale.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Setzt den Schalter für die Berücksichtigung von Groß/Kleinschreibung
     * 
     * @param ignoreCase
     */
    public void setIgnoreCase(boolean ignoreCase) {

        this.settings.ignoreCase = ignoreCase;
    }

    /**
     * liefert den Schalter für die Berücksichtigung von Groß/Kleinschreibung
     * 
     * @param ignoreCase
     */
    public boolean getIgnoreCase() {

        return this.settings.ignoreCase;
    }

    /**
     * Setzt den Messagetext
     * 
     * @param ignoreCase
     */
    public void setMsg(String msg) {

        this.msg = msg;
    }

    /**
     * liefert den Messagetext
     * 
     * @param ignoreCase
     */
    public String getMsg() {

        return this.msg;
    }

    /**
     * Automatisches Setzen in der DB von gerade aktiven LONG_VALUE bzw VALUE
     * zulassen (Funktion - set_dialog_entries_long_values)
     * 
     * @return Returns the enableAutoSetLongValues.
     */
    public boolean isEnableAutoSetLongValues() {
        return enableAutoSetLongValues;
    }

    /**
     * Automatisches Setzen in der DB von gerade aktiven LONG_VALUE bzw VALUE
     * zulassen (Funktion - set_dialog_entries_long_values)
     * 
     * @param enableAutoSetLongValues
     *            The enableAutoSetLongValues to set.
     */
    public void setEnableAutoSetLongValues(boolean enableAutoSetLongValues) {
        this.enableAutoSetLongValues = enableAutoSetLongValues;
    }
    
    /**
     * FCKEditor Breite
     * 
     * @return Returns the editorWidth.
     */
    public String getEditorWidth() {
        return editorWidth;
    }
    
    /**
     * FCKEditor Breite
     * 
     * @param editorWidth The editorWidth to set.
     */
    public void setEditorWidth(String editorWidth) {
        this.editorWidth = editorWidth;
    }
    
    /**
     * Aktuelle Request, Session Daten anzeigen
     * 
     * @return Returns the enableShowDevelopmentData.
     */
    public boolean isEnableShowDevelopmentData() {
        return enableShowDevelopmentData;
    }
    
    /**
     * Aktuelle Request, Session Daten anzeigen
     * 
     * @param enableShowDevelopmentData The enableShowDevelopmentData to set.
     */
    public void setEnableShowDevelopmentData(boolean enableShowDevelopmentData) {
        this.enableShowDevelopmentData = enableShowDevelopmentData;
    }
    
    /**
     * DIV Positionierung Left
     * 
     * @param divDevelopmentDataLeft The divDevelopmentDataLeft to set.
     */
    public void setDivDevelopmentDataLeft(String divDevelopmentDataLeft) {
        this.divDevelopmentDataLeft = divDevelopmentDataLeft;
    }
    
    /**
     * DIV Positionierung Top
     * 
     * @param divDevelopmentDataTop The divDevelopmentDataTop to set.
     */
    public void setDivDevelopmentDataTop(String divDevelopmentDataTop) {
        this.divDevelopmentDataTop = divDevelopmentDataTop;
    }
    /**
     * @return Returns the isSourceDownloaded.
     */
    public boolean isSourceDownloaded() {
        return isSourceDownloaded;
    }
}