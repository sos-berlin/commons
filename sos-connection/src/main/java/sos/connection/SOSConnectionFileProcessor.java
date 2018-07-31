package sos.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import sos.util.SOSClassUtil;
import sos.util.SOSFile;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

public class SOSConnectionFileProcessor {

    SOSConnection connection = null;
    SOSLogger logger = null;
    String settingsFilename = null;
    String fileSpec = "^(.*)";
    boolean hasDirectory = false;
    boolean commitAtEnd = false;
    private ArrayList<String> successFiles;
    private LinkedHashMap<String, String> errorFiles;

    public SOSConnectionFileProcessor(SOSConnection sosConnection) throws Exception {
        this.setConnection(sosConnection);
        this.init();
    }

    public SOSConnectionFileProcessor(SOSConnection sosConnection, SOSLogger sosLogger) throws Exception {
        this.setConnection(sosConnection);
        this.setLogger(sosLogger);
        this.init();
    }

    public SOSConnectionFileProcessor(String settingsFilename) throws Exception {
        this.setSettingsFilename(settingsFilename);
        this.init();
    }

    public SOSConnectionFileProcessor(String settingsFilename, SOSLogger sosLogger) throws Exception {
        this.setSettingsFilename(settingsFilename);
        this.setLogger(sosLogger);
        this.init();
    }

    public void init() throws Exception {
        try {
            if (this.getConnection() == null) {
                if (this.getSettingsFilename() == null || this.getSettingsFilename().trim().isEmpty()) {
                    throw new Exception("no connection and no settings filename were given for connection");
                }
                if (this.getLogger() != null) {
                    this.getLogger().debug3("DB Connecting.. .");
                }
                this.setConnection(SOSConnection.createInstance(this.getSettingsFilename()));
                this.getConnection().connect();
                if (this.getLogger() != null) {
                    this.getLogger().debug3("DB Connected");
                }
            }
        } catch (Exception e) {
            throw new Exception("connect to database failed: " + e.getMessage(), e);
        }
    }

    public void process(File inputFile, String fileSpec) throws Exception {
        this.setFileSpec(fileSpec);
        this.process(inputFile);
    }

    private void initCounters() {
        this.errorFiles = new LinkedHashMap<String, String>();
        this.successFiles = new ArrayList<String>();
    }

    public void process(File inputFile) throws Exception {
        final String methodName = SOSClassUtil.getMethodName();
        boolean isEnd = false;
        try {
            if (inputFile.isDirectory()) {
                if (this.getLogger() != null) {
                    this.getLogger().info(String.format("%s: process directory %s, fileSpec = %s", methodName, inputFile.getAbsolutePath(), this
                            .getFileSpec()));
                }
                this.hasDirectory = true;
                this.initCounters();
                Vector<File> filelist = SOSFile.getFilelist(inputFile.getAbsolutePath(), this.getFileSpec(), 0);
                Iterator<File> iterator = filelist.iterator();
                while (iterator.hasNext()) {
                    this.process(iterator.next());
                }
                isEnd = true;
                if (this.getLogger() != null) {
                    this.getLogger().info(String.format("%s: directory proccessed (total = %s, success = %s, error = %s) %s", methodName, filelist
                            .size(), this.successFiles.size(), this.errorFiles.size(), inputFile.getAbsolutePath()));
                    if (!this.successFiles.isEmpty()) {
                        this.getLogger().info(String.format("%s:   success:", methodName));
                        for (int i = 0; i < this.successFiles.size(); i++) {
                            this.getLogger().info(String.format("%s:     %s) %s", methodName, i + 1, this.successFiles.get(i)));
                        }
                    }
                    if (!this.errorFiles.isEmpty()) {
                        this.getLogger().info(String.format("%s:   error:", methodName));
                        int i = 1;
                        for (Entry<String, String> entry : this.errorFiles.entrySet()) {
                            this.getLogger().info(String.format("%s:     %s) %s: %s", methodName, i, entry.getKey(), entry.getValue()));
                            i++;
                        }
                    }
                }
            } else {
                FileReader fr = null;
                BufferedReader br = null;
                StringBuilder sb = new StringBuilder();
                if (this.getLogger() != null) {
                    this.getLogger().info(String.format("%s: process file %s", methodName, inputFile.getAbsolutePath()));
                }
                try {
                    fr = new FileReader(inputFile.getAbsolutePath());
                    br = new BufferedReader(fr);
                    String nextLine = "";
                    while ((nextLine = br.readLine()) != null) {
                        sb.append(nextLine);
                        sb.append("\n");
                    }
                } catch (Exception ex) {
                    throw ex;
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (Exception ex) {
                            //
                        }
                    }
                    if (fr != null) {
                        try {
                            fr.close();
                        } catch (Exception ex) {
                            //
                        }
                    }
                }
                this.getConnection().executeStatements(sb.toString());
                if (!this.hasDirectory) {
                    isEnd = true;
                }
                this.successFiles.add(inputFile.getAbsolutePath());
                if (this.getLogger() != null) {
                    this.getLogger().info(String.format("%s: file successfully processed %s", methodName, inputFile.getAbsolutePath()));
                }
            }
        } catch (Exception e) {
            this.errorFiles.put(inputFile.getAbsolutePath(), e.getMessage());
            if (this.getLogger() != null) {
                this.getLogger().warn(String.format("%s: an error occurred processing file [%s]: %s", methodName, inputFile.getAbsolutePath(), e
                        .getMessage()));
            }
        } finally {
            try {
                if (this.getConnection() != null && isEnd) {
                    if (this.isCommitAtEnd()) {
                        this.getConnection().commit();
                    } else {
                        this.getConnection().executeUpdate("ROLLBACK");
                    }
                }
            } catch (Exception ex) {
                //
            }
        }
    }

    private String getParamValue(String param) {
        String[] arr = param.split("=");
        if (arr.length > 1) {
            return arr[1].trim();
        } else {
            return param;
        }
    }

    public SOSConnection getConnection() {
        return connection;
    }

    public void setConnection(SOSConnection connection) {
        this.connection = connection;
    }

    public SOSLogger getLogger() {
        return logger;
    }

    public void setLogger(SOSLogger logger) {
        this.logger = logger;
    }

    public String getSettingsFilename() {
        return settingsFilename;
    }

    public void setSettingsFilename(String settingsFilename) {
        this.settingsFilename = settingsFilename;
    }

    public String getFileSpec() {
        return fileSpec;
    }

    public void setFileSpec(String fileSpec) {
        this.fileSpec = fileSpec;
    }

    public boolean isCommitAtEnd() {
        return commitAtEnd;
    }

    public void setCommitAtEnd(boolean commitAtEnd) {
        this.commitAtEnd = commitAtEnd;
    }

    public static void main(String args[]) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: SOSConnectionFileProcessor configuration-file  path  [file-specification]  [log-level] "
                    + "[-commit-at-end|-auto-commit|-execute-batch|batch-size=xxx]");
            return;
        }

        SOSConnectionFileProcessor processor = null;
        int exitCode = 0;
        boolean logToStdErr = false;
        try {
            String settingsFile = args[0];
            int logLevel = 0;
            if (args.length > 3) {
                logLevel = Integer.parseInt(args[3]);
            }
            logToStdErr =  Arrays.asList(args).contains("-execute-from-setup");
            processor = new SOSConnectionFileProcessor(settingsFile, (SOSLogger) new SOSStandardLogger(logLevel));
            File inputFile = null;
            for (int i = 0; i < args.length; i++) {
                String param = args[i].trim();
                System.out.println(String.format("  %s) %s", i + 1, param));
                if (i == 1) {
                    inputFile = new File(param);
                } else if (i == 2) {
                    processor.setFileSpec(param);
                } else if (i > 3) {
                    if ("-commit-at-end".equalsIgnoreCase(param)) {
                        processor.setCommitAtEnd(true);
                    } else if ("-auto-commit".equalsIgnoreCase(param)) {
                        processor.getConnection().setAutoCommit(true);
                    } else if ("-execute-batch".equalsIgnoreCase(param)) {
                        processor.getConnection().setUseExecuteBatch(true);
                    } else if (param.startsWith("batch-size")) {
                        int batchSize = processor.getConnection().getBatchSize();
                        try {
                            batchSize = Integer.parseInt(processor.getParamValue(param));
                        } catch (Exception ex) {
                            System.out.println(String.format("   error: invalid value of the param %s", param));
                            System.out.println(String.format("          batch-size setted to default value = %s", batchSize));
                        }
                        processor.getConnection().setBatchSize(batchSize);
                    } else if ("-execute-from-setup".equalsIgnoreCase(param)) {
                        processor.getConnection().setUseExecuteBatch(true);
                        logToStdErr = true;
                    }
                }
            }
            processor.process(inputFile);
            if (processor.errorFiles != null) {
                exitCode = processor.errorFiles.size();
                if (logToStdErr && !processor.errorFiles.isEmpty()) {
                    Entry<String, String> entry = processor.errorFiles.entrySet().iterator().next();
                    System.err.println(String.format("%s: %s", entry.getKey(), entry.getValue()));
                }
            }
            if (logToStdErr && processor.successFiles != null && !processor.successFiles.isEmpty()) {
                System.err.println(String.format("%s successful processed", processor.successFiles.get(0)));
            }
        } catch (Exception e) {
            exitCode = 1;
            if (logToStdErr) {
                e.printStackTrace(System.err);
            } else {
                e.printStackTrace(System.out);
            }
            // throw e;
        } finally {
            if (processor != null && processor.getConnection() != null) {
                try {
                    processor.getConnection().disconnect();
                } catch (Exception ex) {
                }
            }
        }
        System.exit(exitCode);
    }

}