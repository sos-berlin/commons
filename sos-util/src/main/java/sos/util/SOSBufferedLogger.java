package sos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Logger which logs into a Buffer
 *
 * @author Andreas Liebert */
public class SOSBufferedLogger extends SOSLogger {

    protected StringBuffer logBuffer = new StringBuffer();
    private static final DateFormat TIME = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final DateFormat TIMESTAMP = new SimpleDateFormat("EEEE MMMM dd HH:mm:ss yyyy");
    private static final String INDENT = "          ";
    private static final String nl = "\n";
    private Date currentDate = new Date();
    private int logLevel = 10;

    /** Konstruktor logging nach buffer
     * 
     * @param logLevel ein Wert von 1 bis 9
     * @throws Exception */
    public SOSBufferedLogger(int logLevel) throws Exception {
        this.setLogLevel(logLevel);
        writeHeader();
    }

    /** Schreibt den header
     * 
     * @throws java.lang.Exception */
    private void writeHeader() throws Exception {
        logBuffer.append(TIME.format(currentDate) + INDENT + "---------- " + TIMESTAMP.format(currentDate) + nl);
    }

    /** Loggt die angegebene message
     * 
     * @param level Protokollierungstiefe
     * @param str stellt die log message dar */
    private void log(int level, String str) {
        if (level > logLevel && level < INFO) {
            return;
        }
        currentDate = new Date();
        StringBuffer curBuffer = new StringBuffer();
        if (level == INFO) {
            curBuffer.append("[info]");
        } else if (level == WARN) {
            curBuffer.append("[warn]");
            setWarning(str);
        } else if (level == ERROR) {
            curBuffer.append("[error]");
            setError(str);
        } else if (level == DEBUG) {
            curBuffer.append("[debug]");
        } else {
            curBuffer.append("[debug");
            if (level == 0) {
                curBuffer.append("]");
            } else {
                curBuffer.append(Integer.toString(level));
                curBuffer.append("] ");
            }
        }
        curBuffer.append(INDENT.substring(0, INDENT.length() - curBuffer.length()));
        curBuffer.insert(0, INDENT);
        curBuffer.insert(0, TIME.format(currentDate));
        curBuffer.append(str);
        curBuffer.append("\n");
        logBuffer.append(curBuffer);
    }

    /** Schreibt eine Warnung ins Protokoll
     * 
     * @param warningMessage Inhalt der Warnung
     * @throws java.lang.Exception */
    public void warn(String warningMessage) throws Exception {
        log(WARN, warningMessage);
        setWarning(warningMessage);
    }

    /** Schreibt eine Fehlermeldung ins Protokoll
     * 
     * @param errorMessage Inhalt der Fehlermeldung
     * @throws java.lang.Exception */
    public void error(String errorMessage) throws Exception {
        log(ERROR, errorMessage);
        setError(errorMessage);
    }

    /** Schreibt info-message
     * 
     * @param str stellt die info message dar
     * @throws java.lang.Exception */
    public void info(String str) throws Exception {
        log(INFO, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug(String str) throws Exception {
        log(DEBUG, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug1(String str) throws Exception {
        log(DEBUG1, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug2(String str) throws Exception {
        log(DEBUG2, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug3(String str) throws Exception {
        log(DEBUG3, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug4(String str) throws Exception {
        log(DEBUG4, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug5(String str) throws Exception {
        log(DEBUG5, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug6(String str) throws Exception {
        log(DEBUG6, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug7(String str) throws Exception {
        log(DEBUG7, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug8(String str) throws Exception {
        log(DEBUG8, str);
    }

    /** Schreibt debug-message
     * 
     * @param str stellt die debug message dar
     * @throws java.lang.Exception */
    public void debug9(String str) throws Exception {
        log(DEBUG9, str);
    }

    /** Schliesst das stream-objekt
     * 
     * @throws java.lang.Exception */
    public void close() throws Exception {
        this.reset();
        this.logBuffer = null;
    }

    /** Destruktor
     * 
     * @throws Exception */
    protected void finalize() throws Exception {
        try {
            if (this.logBuffer != null){
                this.close();
            }
        } catch (Exception e) {
        }
    }

    /** setzt den Zustand des Loggers zurück */
    public void reset() {
        super.reset();
        this.logBuffer = new StringBuffer();
    }

    /** @return Returns the logLevel. */
    public int getLogLevel() {
        return logLevel;
    }

    /** @param logLevel The logLevel to set. */
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    /** @return Returns the logBuffer. */
    public StringBuffer getLogBuffer() {
        return logBuffer;
    }
}
