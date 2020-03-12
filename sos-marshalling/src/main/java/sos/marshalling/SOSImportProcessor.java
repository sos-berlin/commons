package sos.marshalling;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.connection.SOSConnection;
import sos.util.SOSFile;

/** @author Robert Ehrlich */
public class SOSImportProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSImportProcessor.class);
    private SOSConnection sosConnection = null;
    private boolean update = false;
    private String fileSpec = "^(.*)";
    private File configFile = null;
    private File inputFile = null;

    public SOSImportProcessor(String settingsFilename) throws Exception {
        this.configFile = new File(settingsFilename);
    }

    public SOSImportProcessor(File configFile, File inputFile) throws Exception {
        if (configFile == null) {
            throw new NullPointerException("Import: Parameter config == null!");
        }
        if (inputFile == null) {
            throw new NullPointerException("Import: Parameter input == null!");
        }
        try {
            this.configFile = configFile;
            this.inputFile = inputFile;
            if (configFile != null && !configFile.getName().isEmpty() && !configFile.exists()) {
                throw new Exception("configuration file not found: " + configFile);
            }
            if (inputFile != null && !inputFile.getName().isEmpty() && !inputFile.exists()) {
                throw new Exception("input file not found: " + inputFile);
            }

        } catch (Exception e) {
            throw new Exception("error in SOSImportProcessor: " + e.getMessage(), e);
        }
    }

    public SOSImportProcessor() {
        System.out.println("Syntax");
        System.out.println("Optionen :");
        System.out.println("        -config     Namen der Konfigurationsdatei für die DB Verbindung angeben.");
        System.out.println("                    Default : hibernate.cfg.xml");
        System.out.println("        -input      Namen der Import XML-Datei angeben.");
        System.out.println("                    Default : sos_export.xml ");
        System.out.println("");
        System.out.println("");
        System.out.println("Beispiel 1 : Datei sos_import.xml in die Tabelle t1 importieren");
        System.out.println("         -config=config/hibernate.cfg.xml -input=sos_import.xml -table=t1");
    }

    public void doImport() throws Exception {
        try {
            sosConnection = SOSConnection.createInstance(configFile.toString());
            sosConnection.connect();
            SOSImport imp = new SOSImport(sosConnection, inputFile.toString(), null, null, null);
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
                SOSImport imp = new SOSImport(sosConnection, inputFile.toString(), null, null, null);
                imp.setUpdate(getUpdate());
                imp.doImport();
                sosConnection.commit();
            }
        } catch (Exception e) {
            LOGGER.warn("an error occurred processing file [" + inputFile.getAbsolutePath() + "]: " + e, e);
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
            System.out.println("Usage: SOSImportProcessor configuration-file  path  [file-specification] [update]");
        }
        SOSImportProcessor processor = new SOSImportProcessor(args[0]);
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