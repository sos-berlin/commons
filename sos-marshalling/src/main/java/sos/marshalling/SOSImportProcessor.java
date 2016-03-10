package sos.marshalling;

import java.io.File;
import java.util.Vector;
import java.io.BufferedReader;

import sos.connection.SOSConnection;
import sos.util.SOSArguments;
import sos.util.SOSFile;
import sos.marshalling.SOSImport;
import java.util.Iterator;
import sos.util.SOSStandardLogger;
import sos.util.SOSLogger;

/** Title: SOSImportProcessor<br>
 * Description: Kommandozeilentool zum Importieren von Daten im XML-Format<br>
 * 
 * Copyright: Copyright (c) 2005<br>
 * Company: SOS Berlin GmbH<br>
 * 
 * @author <a href="mailto:robert.ehrlich@sos-berlin.com">Robert Ehrlich</a>
 *         Resource sos.connection.jar sos.util.jar xalan.jar xercesImpl.jar
 *         xml-apis.jar
 * @version $Id$ */
public class SOSImportProcessor {

    private SOSConnection _sosConnection = null;
    private SOSStandardLogger _sosLogger = null;
    private boolean update = false;
    private String fileSpec = "^(.*)";
    private File _configFile = null;
    private File _inputFile = null;
    private File _logFile = null;
    private int _logLevel = 0;

    public SOSImportProcessor(String settingsFilename, SOSStandardLogger sosLogger) throws Exception {

        _configFile = new File(settingsFilename);
        _sosLogger = sosLogger;
    }

    /** Konstruktor
     * 
     * 
     * @param configFile Datei, in der die Zugangsdaten zur Datenbank enthalten
     *            sind
     * @param logFile Name der Protokolldatei
     * @param logLevel Log Level für die Protokolldatei
     * @param inputFile Dateiname für Import
     * @throws Exception */
    public SOSImportProcessor(File configFile, File logFile, int logLevel, File inputFile) throws Exception {
        if (configFile == null) {
            throw new NullPointerException("Import: Parameter config == null!");
        }
        if (inputFile == null) {
            throw new NullPointerException("Import: Parameter input == null!");
        }
        try {
            _configFile = configFile;
            _logFile = logFile;
            _logLevel = logLevel;
            _inputFile = inputFile;
            if (_configFile != null && !_configFile.getName().isEmpty() && !_configFile.exists()) {
                throw new Exception("configuration file not found: " + _configFile);
            }
            if (_inputFile != null && !_inputFile.getName().isEmpty() && !_inputFile.exists()) {
                throw new Exception("input file not found: " + _inputFile);
            }
            if (_logLevel != 0 && "".equals(_logFile.toString())) {
                throw new Exception("log file is not defined");
            }
        } catch (Exception e) {
            throw new Exception("error in SOSImportProcessor: " + e.getMessage());
        }
    }

    /** Konstruktor
     * 
     * wird aufgerufen, um die Programm USAGE anzuzeigen */
    public SOSImportProcessor() {
        System.out.println("Syntax");
        System.out.println("Optionen :");
        System.out.println("        -config     Namen der Konfigurationsdatei für die DB Verbindung angeben.");
        System.out.println("                    Default : sos_settings.ini");
        System.out.println("        -input      Namen der Import XML-Datei angeben.");
        System.out.println("                    Default : sos_export.xml ");
        System.out.println("        -log        Namen der Log-Datei angeben.");
        System.out.println("                    Default : sos_import.log");
        System.out.println("        -log-level  Loglevel angeben.");
        System.out.println("                    Default : 0  keine Log-Datei schreiben");
        System.out.println("");
        System.out.println("");
        System.out.println("Beispiel 1 : Datei sos_import.xml in die Tabelle t1 importieren");
        System.out.println("         -config=config/sos_settings.ini -input=sos_import.xml -table=t1");
    }

    /** Import ausf&uuml;hren
     * 
     * @throws Exception */
    public void doImport() throws Exception {
        try {
            if (_logLevel == 0) {
                _sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG);
            } else {
                _sosLogger = new SOSStandardLogger(_logFile.toString(), _logLevel);
            }
            _sosConnection = SOSConnection.createInstance(_configFile.toString(), _sosLogger);
            _sosConnection.connect();
            SOSImport imp = new SOSImport(_sosConnection, _inputFile.toString(), null, null, null, _sosLogger);
            imp.doImport();
            _sosConnection.commit();
            System.out.println("");
            System.out.println("Import erfolgreich beendet.");
        } catch (Exception e) {
            _sosConnection.rollback();
            throw new Exception("error in SOSImportProcessor.doImport: " + e.getMessage());
        } finally {
            try {
                if (_sosConnection != null) {
                    _sosConnection.disconnect();
                }
            } catch (Exception e) {
            }
        }
    }

    public void process(File inputFile) throws Exception {
        try {
            _sosConnection = SOSConnection.createInstance(_configFile.toString(), _sosLogger);
            _sosConnection.connect();
            if (inputFile.isDirectory()) {
                int counter = 0;
                Vector filelist = SOSFile.getFilelist(inputFile.getAbsolutePath(), this.getFileSpec(), 0);
                Iterator iterator = filelist.iterator();
                while (iterator.hasNext()) {
                    this.process((File) iterator.next());
                }
            } else {
                SOSImport imp = new SOSImport(_sosConnection, inputFile.toString(), null, null, null, _sosLogger);
                imp.setUpdate(getUpdate());
                imp.doImport();
                _sosConnection.commit();
            }
        } catch (Exception e) {
            _sosLogger.warn("an error occurred processing file [" + inputFile.getAbsolutePath() + "]: " + e);
        } finally {
            try {
                if (_sosConnection != null) {
                    _sosConnection.rollback();
                }
            } catch (Exception ex) {
            }
        }
    }

    /** Programm ausführen<br>
     * 
     * @param args Programmargumente<br>
     * <br>
     * 
     * @throws Exception */
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: SOSImportProcessor configuration-file  path  [file-specification] [update] [log-level]");
        }
        int logLevel = 0;
        if (args.length > 4) {
            logLevel = Integer.parseInt(args[4]);
        }
        SOSImportProcessor processor = new SOSImportProcessor(args[0], new SOSStandardLogger(logLevel));
        File inputFile = new File(args[1]);
        if (args.length > 2) {
            processor.setFileSpec(args[2]);
        }
        if (args.length > 3) {
            processor.setUpdate("1".equals(args[3]));
        }
        processor.process(inputFile);
    }

    public void setUpdate(boolean ovr) {
        this.update = ovr;
    }

    public boolean getUpdate() {
        return this.update;
    }

    /** @param fileSpec The fileSpec to set. */
    public void setFileSpec(String fileSpec) {
        this.fileSpec = fileSpec;
    }

    /** @return Returns the fileSpec. */
    public String getFileSpec() {
        return fileSpec;
    }
    
}