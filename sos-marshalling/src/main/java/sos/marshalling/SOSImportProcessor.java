package sos.marshalling;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import sos.connection.SOSConnection;
import sos.util.SOSFile;
import sos.util.SOSStandardLogger;

/** @author Robert Ehrlich */
public class SOSImportProcessor {

    private SOSConnection sosConnection = null;
    private SOSStandardLogger sosLogger = null;
    private boolean update = false;
    private String fileSpec = "^(.*)";
    private File configFile = null;
    private File inputFile = null;
    private File logFile = null;
    private int logLevel = 0;

    public SOSImportProcessor(String settingsFilename, SOSStandardLogger sosLogger) throws Exception {
        this.configFile = new File(settingsFilename);
        this.sosLogger = sosLogger;
    }

    public SOSImportProcessor(File configFile, File logFile, int logLevel, File inputFile) throws Exception {
        if (configFile == null) {
            throw new NullPointerException("Import: Parameter config == null!");
        }
        if (inputFile == null) {
            throw new NullPointerException("Import: Parameter input == null!");
        }
        try {
            this.configFile = configFile;
            this.logFile = logFile;
            this.logLevel = logLevel;
            this.inputFile = inputFile;
            if (configFile != null && !configFile.getName().isEmpty() && !configFile.exists()) {
                throw new Exception("configuration file not found: " + configFile);
            }
            if (inputFile != null && !inputFile.getName().isEmpty() && !inputFile.exists()) {
                throw new Exception("input file not found: " + inputFile);
            }
            if (logLevel != 0 && "".equals(logFile.toString())) {
                throw new Exception("log file is not defined");
            }
        } catch (Exception e) {
            throw new Exception("error in SOSImportProcessor: " + e.getMessage(), e);
        }
    }

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

    public void doImport() throws Exception {
        try {
            if (logLevel == 0) {
                sosLogger = new SOSStandardLogger(SOSStandardLogger.DEBUG);
            } else {
                sosLogger = new SOSStandardLogger(logFile.toString(), logLevel);
            }
            sosConnection = SOSConnection.createInstance(configFile.toString());
            sosConnection.connect();
            SOSImport imp = new SOSImport(sosConnection, inputFile.toString(), null, null, null, sosLogger);
            imp.doImport();
            sosConnection.commit();
            System.out.println("");
            System.out.println("Import erfolgreich beendet.");
        } catch (Exception e) {
            sosConnection.rollback();
            throw new Exception("error in SOSImportProcessor.doImport: " + e.getMessage());
        } finally {
            try {
                if (sosConnection != null) {
                    sosConnection.disconnect();
                }
            } catch (Exception e) {
                //
            }
        }
    }

    public void process(File inputFile) throws Exception {
        try {
            sosConnection = SOSConnection.createInstance(configFile.toString());
            sosConnection.connect();
            if (inputFile.isDirectory()) {
                int counter = 0;
                Vector filelist = SOSFile.getFilelist(inputFile.getAbsolutePath(), this.getFileSpec(), 0);
                Iterator iterator = filelist.iterator();
                while (iterator.hasNext()) {
                    this.process((File) iterator.next());
                }
            } else {
                SOSImport imp = new SOSImport(sosConnection, inputFile.toString(), null, null, null, sosLogger);
                imp.setUpdate(getUpdate());
                imp.doImport();
                sosConnection.commit();
            }
        } catch (Exception e) {
            sosLogger.warn("an error occurred processing file [" + inputFile.getAbsolutePath() + "]: " + e);
        } finally {
            try {
                if (sosConnection != null) {
                    sosConnection.rollback();
                }
            } catch (Exception ex) {
                //
            }
        }
    }

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

    public void setFileSpec(String fileSpec) {
        this.fileSpec = fileSpec;
    }

    public String getFileSpec() {
        return fileSpec;
    }

}