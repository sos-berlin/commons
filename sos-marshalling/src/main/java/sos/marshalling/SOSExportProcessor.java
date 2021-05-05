package sos.marshalling;

import java.io.File;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sos.connection.SOSConnection;
import sos.util.SOSArguments;

/** @author Robert Ehrlich */
public class SOSExportProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSExportProcessor.class);
    private SOSConnection sosConnection = null;
    private File configFile = null;
    private File outputFile = null;
    private String tableNames = null;
    private String executeQuery = null;
    private String keys = null;
    private boolean enableTableParametr = true;
    private boolean enableExecuteParametr = true;

    public SOSExportProcessor(File configFile, File logFile, int logLevel, File outputFile, String tableNames, String executeQuery, String keys)
            throws Exception {
        if (configFile == null) {
            throw new NullPointerException("Export: Parameter config == null!");
        }
        if (outputFile == null) {
            throw new NullPointerException("Export: Parameter output == null!");
        }
        try {
            this.configFile = configFile;
            this.outputFile = outputFile;
            this.tableNames = tableNames;
            this.executeQuery = executeQuery;
            this.keys = keys;
            if (configFile != null && !configFile.getName().isEmpty() && !configFile.exists()) {
                throw new Exception("configuration file not found: " + configFile);
            }
            if ((tableNames != null && !"".equals(tableNames)) && (executeQuery != null && !"".equals(executeQuery))) {
                throw new Exception("-tables and -execute may not be indicated together");
            }
            if (logLevel != 0 && "".equals(logFile.toString())) {
                throw new Exception("log file is not defined");
            }
        } catch (Exception e) {
            throw new Exception("error in SOSExportProcessor: " + e.getMessage());
        }
    }

    public SOSExportProcessor() {
        System.out.println("Syntax");
        System.out.println("Optionen :");
        System.out.println("        -config     Namen der Konfigurationsdatei f�r die DB Verbindung angeben.");
        System.out.println("                    Default : hibernate.cfg.xml");
        System.out.println("        -output     Namen der Export XML-Datei angeben.");
        System.out.println("                    Default : sos_export.xml ");
        System.out.println("        -tables     Tabellennamen f�r den Export.");
        System.out.println("                    Es werden alle Daten der jeweiligen Tabelle exportiert.");
        System.out.println("                    Mehrere Tabellen durch + Zeichen getrennt");
        System.out.println("        -keys    	Schl�sselfelder f�r eine bzw mehreren Tabellen angeben.");
        System.out.println("           			Wird im Zusammenhang mit der Option -tables ber�cksichtigt.");
        System.out.println("                    Schl�sselfelder sind wichtig, wenn eine Tabelle CLOB bzw BLOB enth�lt.");
        System.out.println("                    mehrere Schl�ssel f�r eine Tabelle - durch Komma getrennt.");
        System.out.println("                    f�r mehreren Tabellen durch + Zeichen getrennt.");
        System.out.println("                    f�r mehreren Tabellen : die Reihenfolge wie bei -tables.");
        System.out.println("        -execute    eigene SQL-Statement f�r eine Tabelle angeben.");
        System.out.println("                    SQL-Statement in doppelten Hochkommas.");
        System.out.println("");
        System.out.println("");
        System.out.println("Notiz : -execute und -tables d�rfen nicht zusammen angegeben werden.");
        System.out.println("        eine von beiden Optionen muss angegeben sein");
        System.out.println("");
        System.out.println("");
        System.out.println("Beispiel 1 : alle Daten der Tabelle t1 exportieren und in die default Log-Datei loggen");
        System.out.println("         -config=config/hibernate.cfg.xml -tables=t1 -log-level=9");
        System.out.println("");
        System.out.println("Beispiel 2 : wie Beispiel 1 + Schl�sselfeld ID f�r die Tabelle t1 definieren");
        System.out.println("         -config=config/hibernate.cfg.xml -tables=t1 -keys=ID -log-level=9");
        System.out.println("");
        System.out.println("Beispiel 3 : alle Daten der Tabellen t1 und t2 ohne zu loggen exportieren");
        System.out.println("         -config=config/hibernate.cfg.xml -tables=t1+t2");
        System.out.println("");
        System.out.println(
                "Beispiel 4 : wie Beispiel 3 + Schl�sselfelder T1_ID und T1_NAME f�r die Tabelle t1 und Schl�sselfeld T2_ID f�r die Tabelle t2 definieren");
        System.out.println("         -config=config/hibernate.cfg.xml -tables=t1+t2 -keys=T1_ID,T1_NAME+T2_ID");
        System.out.println("");
        System.out.println("Beispiel 5 : eigene SQL-Statement f�r die Tabelle t1 definieren");
        System.out.println("         -config=config/hibernate.cfg.xml -execute=\"select * from t1 where ID=1\"");
    }

    public void doExport() throws Exception {
        try {
            if (this.isEnableTableParametr() && this.isEnableExecuteParametr() && (tableNames == null || "".equals(tableNames))
                    && (executeQuery == null || "".equals(executeQuery))) {
                throw new Exception("undefined operation for export. Check please input for your -tables or -execute arguments");
            }
            sosConnection = SOSConnection.createInstance(configFile.toString());
            sosConnection.connect();
            SOSExport export = new SOSExport(sosConnection, outputFile.toString(), "EXPORT");
            prepareExport(export);
            export.doExport();
            System.out.println("");
            System.out.println("Export erfolgreich beendet.");
        } catch (Exception e) {
            throw new Exception("error in SOSExportProcessor: " + e.getMessage());
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

    public void prepareExport(SOSExport export) throws Exception {
        String keys = "";
        String[] tablesKeys = {};
        if (keys != null && !"".equals(keys.trim())) {
            keys = keys.toUpperCase();
            tablesKeys = keys.split("\\+");
        }
        if (!"".equals(tableNames)) {
            StringTokenizer tables = new StringTokenizer(tableNames, "+");
            int i = 0;
            while (tables.hasMoreTokens()) {
                String table = tables.nextToken().toUpperCase();
                String key = "";
                if (!"".equals(table)) {
                    if (tablesKeys != null && tablesKeys.length != 0) {
                        try {
                            key = tablesKeys[i];
                        } catch (Exception e) {
                            //
                        }
                    }
                    export.add(table, key, "select * from " + table, null, i);
                    i++;
                }
            }
        } else if (!"".equals(executeQuery)) {
            StringTokenizer st = new StringTokenizer(executeQuery, " ");
            String table = "";
            while (st.hasMoreTokens()) {
                String token = st.nextToken().toUpperCase();
                if ("FROM".equals(token)) {
                    table = st.nextToken().toUpperCase();
                    break;
                }
            }
            export.add(table, keys, executeQuery, null, 0);
        }
    }

    public static void main(String[] args) throws Exception {
        boolean isExport = true;
        if (args.length == 1) {
            String argument = args[0].toLowerCase().trim();
            if ("?".equals(argument) || "help".equals(argument)) {
                isExport = false;
            }
        }
        if (isExport) {
            SOSArguments arguments = new SOSArguments(args);
            SOSExportProcessor processor = new SOSExportProcessor(new File(arguments.asString("-config=", "hibernate.cfg.xml")), new File(arguments
                    .asString("-log=", "sos_export.log")), arguments.asInt("-log-level=", 0), new File(arguments.asString("-output=",
                            "sos_export.xml")), new String(arguments.asString("-tables=", "")), new String(arguments.asString("-execute=", "")),
                    new String(arguments.asString("-keys=", "")));
            arguments.checkAllUsed();
            processor.doExport();
        }
    }

    public boolean isEnableExecuteParametr() {
        return enableExecuteParametr;
    }

    public void setEnableExecuteParametr(boolean enableExecuteParametr) {
        this.enableExecuteParametr = enableExecuteParametr;
    }

    public boolean isEnableTableParametr() {
        return enableTableParametr;
    }

    public void setEnableTableParametr(boolean enableTableParametr) {
        this.enableTableParametr = enableTableParametr;
    }

}