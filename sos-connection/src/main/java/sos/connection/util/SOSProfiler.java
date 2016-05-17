package sos.connection.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import sos.connection.SOSConnection;

/** @author Mueruevet Oeksuez */
public class SOSProfiler {

    private String tableSettings = "PROFILER_SETTINGS";
    private String tableHistory = "PROFILER_HISTORY";
    private String entryHistoryId = "profiler_history.id";
    private String entryHistoryStep = "profiler_history.step";
    private String application;
    private int step;
    private int session;
    private String scriptname;
    private String context;
    private int historyId;
    private long startTimeStamp;
    private String classname = new String();
    private String methodname = new String();
    public static boolean profilingAllowed = true;
    public static String profilingDepth = "profiling_depth";
    public static int allowedDepth = 100;
    public SOSConnection conn;
    public int scope = 100;

    public SOSProfiler(SOSConnection conn) {
        try {
            this.conn = conn;
            step = getSequence(entryHistoryStep);
        } catch (Exception e) {
            // ignore all errors
        }
    }

    public void start(String sqlStatement) {
        try {
            if (!(profilingAllowed)) {
                return;
            }
            String insStr = new String();
            getMethodName();
            if (scope > allowedDepth) {
                return;
            }
            sqlStatement = sqlStatement.replaceAll("'", "''");
            setStartTimeStamp(System.currentTimeMillis());
            historyId = this.getSequence(entryHistoryId);
            insStr =
                    " INSERT INTO " + tableHistory + " ( " + "  \"ID\" " + ", \"APPLICATION\" " + ", \"SESSION\" " + ", \"STEP\" " + ", \"CONTEXT\" "
                            + ", \"SCOPE\" " + ", \"CLASS\" " + ", \"FUNCTION\" " + ", \"SCRIPT_NAME\" " + ", \"START_TIME\" "
                            + ", \"START_TIMESTAMP\" " + ", \"STATEMENT\" " + " ) VALUES ( " + historyId + ", '" + application + "'" + ", " + session
                            + ", " + step + ", '" + context + "'" + ", " + scope + ", '" + classname + "'" + ", '" + methodname + "'" + ", '"
                            + scriptname + "'" + ", %now " + ", " + getStartTimeStamp() + ", '" + sqlStatement + "'" + " )";
            profilingAllowed = false;
            conn.execute(insStr);
            conn.commit();
            profilingAllowed = true;
        } catch (Exception e) {
            //
        }
    }

    public void stop(String errorCode, String errorText) {
        try {
            if (!(profilingAllowed)) {
                return;
            }
            long endTimeStamp = System.currentTimeMillis();
            int error = 0;
            if ((errorCode != null && !errorCode.isEmpty()) || (errorText != null && !errorText.isEmpty())) {
                error = 1;
            }
            String updStr =
                    "UPDATE " + tableHistory + " SET \"END_TIME\" = %now, " + " \"END_TIMESTAMP\" = " + endTimeStamp + "," + " \"ELAPSED\" = "
                            + endTimeStamp + " - \"START_TIMESTAMP\"," + " \"ERROR\" = " + error;
            if (errorCode != null) {
                if (errorCode.length() > 50) {
                    updStr = updStr.concat(", \"ERROR_CODE\" = '" + errorCode.substring(0, 50) + "'");
                } else {
                    updStr = updStr.concat(", \"ERROR_CODE\" = '" + errorCode + "'");
                }
            }
            if (errorText != null) {
                if (errorText.length() > 250) {
                    updStr = updStr.concat(", \"ERROR_TEXT\" = '" + errorText.substring(0, 250) + "'");
                } else {
                    updStr = updStr.concat(", \"ERROR_TEXT\" = '" + errorText + "'");
                }
            }
            updStr = updStr.concat(" WHERE \"ID\" = " + historyId);
            profilingAllowed = false;
            conn.execute(updStr);
            conn.commit();
            profilingAllowed = true;
        } catch (Exception e) {
            //
        }
    }

    public static void setProfilingAllowed(boolean profilingAllowed) throws Exception {
        SOSProfiler.profilingAllowed = profilingAllowed;
    }

    public static boolean isProfilingAllowed() {
        return profilingAllowed;
    }

    public static void setDepthAllowed(int allowedDepth) {
        SOSProfiler.allowedDepth = allowedDepth;
    }

    private int getSequence(String entry) {
        int retVal = 0;
        try {
            String updStr = " update " + tableSettings + " set \"VALUE\"=\"VALUE\"+1 " + " where \"NAME\"='" + entry + "'";
            profilingAllowed = false;
            conn.execute(updStr);
            conn.commit();
            profilingAllowed = true;
        } catch (Exception e) {
            //
        }
        try {
            String selStr = " select \"VALUE\" from " + tableSettings + " where \"NAME\"='" + entry + "'";
            profilingAllowed = false;
            String singleValue = conn.getSingleValue(selStr);
            profilingAllowed = true;
            retVal = Integer.parseInt(singleValue);
        } catch (Exception e) {
            //
        }
        return retVal;
    }

    private long getStartTimeStamp() {
        return startTimeStamp;
    }

    private void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    private void getMethodName() {
        try {
            StringWriter sw = new StringWriter();
            new Throwable().printStackTrace(new PrintWriter(sw));
            String callStack = sw.toString();
            scope = getCountOfString(callStack);
            int atPos = callStack.indexOf("Profiler.start");
            atPos = callStack.indexOf("at", atPos);
            int dotPos = callStack.indexOf("(", atPos);
            callStack = callStack.substring(atPos + 2, dotPos);
            int metPos = callStack.lastIndexOf(".");
            classname = callStack.substring(1, metPos);
            methodname = callStack.substring(metPos + 1, callStack.length());
        } catch (Exception e) {
            // ignore all errors
        }
    }

    private static int getCountOfString(String str) {
        try {
            int iPos = 0;
            int count = 0;
            for (int i = 0; i < str.length(); i++) {
                iPos = str.indexOf("at ");
                if (iPos > -1) {
                    str = str.substring(iPos + 1, str.length());
                    count = count + 1;
                }
            }
            return count - 2;
        } catch (Exception e) {
            return 0;
            // ignore all errors
        }
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setName4TableSettings(String tableSettings) {
        this.tableSettings = tableSettings;
    }

    public String getName4TableSettings() {
        return tableSettings;
    }

    public void setName4TableHistory(String tableHistory) {
        this.tableHistory = tableHistory;
    }

    public String getName4TableHistory() {
        return tableHistory;
    }

    public void setEntryHistoryId(String historyId) {
        this.entryHistoryId = historyId;
    }

    public String getEntryHistoryId() {
        return entryHistoryId;
    }

    public void setEntryHistoryStep(String entryHistoryStep) {
        this.entryHistoryStep = entryHistoryStep;
    }

    public String getEntryHistoryStep() {
        return entryHistoryStep;
    }

}