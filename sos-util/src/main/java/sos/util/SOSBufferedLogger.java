package sos.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** @author Andreas Liebert */
public class SOSBufferedLogger extends SOSLogger {

    protected StringBuffer logBuffer = new StringBuffer();
    private static final DateFormat TIME = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final DateFormat TIMESTAMP = new SimpleDateFormat("EEEE MMMM dd HH:mm:ss yyyy");
    private static final String INDENT = "          ";
    private static final String nl = "\n";
    private Date currentDate = new Date();
    private int logLevel = 10;

    public SOSBufferedLogger(int logLevel) throws Exception {
        this.setLogLevel(logLevel);
        writeHeader();
    }

    private void writeHeader() throws Exception {
        logBuffer.append(TIME.format(currentDate) + INDENT + "---------- " + TIMESTAMP.format(currentDate) + nl);
    }

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

    public void warn(String warningMessage) throws Exception {
        log(WARN, warningMessage);
        setWarning(warningMessage);
    }

    public void error(String errorMessage) throws Exception {
        log(ERROR, errorMessage);
        setError(errorMessage);
    }

    public void info(String str) throws Exception {
        log(INFO, str);
    }

    public void debug(String str) throws Exception {
        log(DEBUG, str);
    }

    public void debug1(String str) throws Exception {
        log(DEBUG1, str);
    }

    public void debug2(String str) throws Exception {
        log(DEBUG2, str);
    }

    public void debug3(String str) throws Exception {
        log(DEBUG3, str);
    }

    public void debug4(String str) throws Exception {
        log(DEBUG4, str);
    }

    public void debug5(String str) throws Exception {
        log(DEBUG5, str);
    }

    public void debug6(String str) throws Exception {
        log(DEBUG6, str);
    }

    public void debug7(String str) throws Exception {
        log(DEBUG7, str);
    }

    public void debug8(String str) throws Exception {
        log(DEBUG8, str);
    }

    public void debug9(String str) throws Exception {
        log(DEBUG9, str);
    }

    public void close() throws Exception {
        this.reset();
        this.logBuffer = null;
    }

    public void reset() {
        super.reset();
        this.logBuffer = new StringBuffer();
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public StringBuffer getLogBuffer() {
        return logBuffer;
    }

}
