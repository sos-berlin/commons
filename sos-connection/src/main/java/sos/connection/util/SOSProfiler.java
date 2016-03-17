package sos.connection.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import sos.connection.SOSConnection;

/** @author Mueruevet Oeksuez */
public class SOSProfiler {

    private static String table_settings = "PROFILER_SETTINGS";
    private static String table_history = "PROFILER_HISTORY";
    private static String entry_history_id = "profiler_history.id";
    private static String entry_history_step = "profiler_history.step";
    private int history_id;
    private long startTimeStamp;
    private static String application;
    private static int step;
    private static int session;
    public int scope = 100;
    private static String scriptname;
    private static String context;
    private String classname = new String();
    private String methodname = new String();
    public SOSConnection conn;
    public static boolean profilingAllowed = true;
    public static String profilingDepth = "profiling_depth";
    public static int allowedDepth = 100;

    public SOSProfiler(SOSConnection conn_) {
        try {
            conn = conn_;
            step = getSequence(entry_history_step);
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
            history_id = this.getSequence(entry_history_id);
            insStr = " INSERT INTO " + table_history + " ( " + "  \"ID\" " + ", \"APPLICATION\" " + ", \"SESSION\" " + ", \"STEP\" " 
                    + ", \"CONTEXT\" " + ", \"SCOPE\" " + ", \"CLASS\" " + ", \"FUNCTION\" " + ", \"SCRIPT_NAME\" " + ", \"START_TIME\" " 
                    + ", \"START_TIMESTAMP\" " + ", \"STATEMENT\" " + " ) VALUES ( " + history_id + ", '" + application + "'" + ", " 
                    + session + ", " + step + ", '" + context + "'" + ", " + scope + ", '" + classname + "'" + ", '" + methodname + "'" 
                    + ", '" + scriptname + "'" + ", %now " + ", " + getStartTimeStamp() + ", '" + sqlStatement + "'" + " )";
            profilingAllowed = false;
            conn.execute(insStr);
            conn.commit();
            profilingAllowed = true;
        } catch (Exception e) {
            // 
        }
    }

    public void stop(String error_code, String error_text) {
        try {
            if (!(profilingAllowed)) {
                return;
            }
            long endTimeStamp = System.currentTimeMillis();
            int error = 0;
            if ((error_code != null && !error_code.isEmpty()) || (error_text != null && !error_text.isEmpty())) {
                error = 1;
            }
            String updStr = "UPDATE " + table_history + " SET \"END_TIME\" = %now, " + " \"END_TIMESTAMP\" = " + endTimeStamp + "," 
                    + " \"ELAPSED\" = " + endTimeStamp + " - \"START_TIMESTAMP\"," + " \"ERROR\" = " + error;
            if (error_code != null) {
                if (error_code.length() > 50) {
                    updStr = updStr.concat(", \"ERROR_CODE\" = '" + error_code.substring(0, 50) + "'");
                } else {
                    updStr = updStr.concat(", \"ERROR_CODE\" = '" + error_code + "'");
                }
            }
            if (error_text != null) {
                if (error_text.length() > 250) {
                    updStr = updStr.concat(", \"ERROR_TEXT\" = '" + error_text.substring(0, 250) + "'");
                } else {
                    updStr = updStr.concat(", \"ERROR_TEXT\" = '" + error_text + "'");
                }
            }
            updStr = updStr.concat(" WHERE \"ID\" = " + history_id);
            profilingAllowed = false;
            conn.execute(updStr);
            conn.commit();
            profilingAllowed = true;
        } catch (Exception e) {
            //
        }
    }

    public static void setProfilingAllowed(boolean profilingAllowed_) throws Exception {
        profilingAllowed = profilingAllowed_;
    }

    public static boolean isProfilingAllowed() {
        return profilingAllowed;
    }

    public static void setDepthAllowed(int allowedDepth_) {
        allowedDepth = allowedDepth_;
    }

    private int getSequence(String entry) {
        int retVal = 0;
        try {
            String updStr = " update " + table_settings + " set \"VALUE\"=\"VALUE\"+1 " + " where \"NAME\"='" + entry + "'";
            profilingAllowed = false;
            conn.execute(updStr);
            conn.commit();
            profilingAllowed = true;
        } catch (Exception e) {
            //
        }
        try {
            String selStr = " select \"VALUE\" from " + table_settings + " where \"NAME\"='" + entry + "'";
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

    private void setStartTimeStamp(long startTimeStamp_) {
        startTimeStamp = startTimeStamp_;
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

    public static void setApplication(String application_) {
        application = application_;
    }

    public static void setSession(int session_) {
        session = session_;
    }

    public static void setScriptname(String scriptname_) {
        scriptname = scriptname_;
    }

    public static void setContext(String context_) {
        context = context_;
    }

    public void setName4TableSettings(String tableSettings_) {
        table_settings = tableSettings_;
    }

    public String getName4TableSettings() {
        return table_settings;
    }

    public void setName4TableHistory(String tableHistory) {
        table_history = tableHistory;
    }

    public String getName4TableHistory() {
        return table_history;
    }

    public void setEntryHistoryId(String historyId) {
        entry_history_id = historyId;
    }

    public String getEntryHistoryId() {
        return entry_history_id;
    }

    public void setEntryHistoryStep(String entryHistoryStep) {
        entry_history_step = entryHistoryStep;
    }

    public String getEntryHistoryStep() {
        return entry_history_step;
    }

}