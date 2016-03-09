package sos.util;

/** <p>
 * Title:
 * </p>
 * <p>
 * Description: Logger-Klasse on using the SOS-Scheduler
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: SOS GmbH
 * </p>
 * 
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$ */

public class SOSSchedulerLogger extends SOSLogger {

    private sos.spooler.Log spoolerLog;

    /** construktor
     *
     * @param spoolerLog the log-object of the scheduler
     * @throws Exception */
    public SOSSchedulerLogger(sos.spooler.Log spoolerLog) throws Exception {

        if (spoolerLog == null)
            throw new Exception("spooler_log is null.");
        this.spoolerLog = spoolerLog;
    }

    /** write the warning message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void warn(String str) throws Exception {
        log(WARN, str);
        setWarning(str);
    }

    /** write the error message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void error(String str) throws Exception {
        log(ERROR, str);
        setError(str);
    }

    /** write the info message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void info(String str) throws Exception {
        log(INFO, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug(String str) throws Exception {
        log(DEBUG, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug1(String str) throws Exception {
        log(DEBUG1, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug2(String str) throws Exception {
        log(DEBUG2, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug3(String str) throws Exception {
        log(DEBUG3, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug4(String str) throws Exception {
        log(DEBUG4, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug5(String str) throws Exception {
        log(DEBUG5, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug6(String str) throws Exception {
        log(DEBUG6, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug7(String str) throws Exception {
        log(DEBUG7, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug8(String str) throws Exception {
        log(DEBUG8, str);
    }

    /** write the debug message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug9(String str) throws Exception {
        log(DEBUG9, str);
    }

    /** write the specified message
     *
     * @param level the log level
     * @param str the specified message
     * @throws java.lang.Exception */
    public void log(int level, String str) throws Exception {
        int logLevel;

        switch (level) {
        case DEBUG:
        case DEBUG1:
            logLevel = -1;
            break;
        case DEBUG2:
        case DEBUG3:
        case DEBUG4:
        case DEBUG5:
        case DEBUG6:
        case DEBUG7:
        case DEBUG8:
        case DEBUG9:
            logLevel = -level;
            break;
        case INFO:
            logLevel = 0;
            break;
        case WARN:
            logLevel = 1;
            setWarning(str);
            break;
        case ERROR:
            logLevel = 2;
            setError(str);
            break;
        default:
            logLevel = 0;
        }
        spoolerLog.log(logLevel, str);
    }

    /** close the logger
     *
     * @throws java.lang.Exception */
    public void close() throws Exception {
        if (spoolerLog != null)
            spoolerLog = null;
    }

    /** @return Returns the logLevel. */
    public int getLogLevel() {
        int spoolerLogLevel = spoolerLog.level();
        int logLevel = INFO;
        switch (spoolerLogLevel) {
        case -1:
            logLevel = DEBUG1;
            break;
        case 0:
            logLevel = INFO;
            break;
        case 1:
            logLevel = WARN;
            break;
        case 2:
            logLevel = ERROR;
            break;
        default:
            logLevel = -spoolerLogLevel;

        }
        return logLevel;
    }

    /** @param logLevel The logLevel to set. */
    public void setLogLevel(int logLevel) {
        int level = logLevel;
        switch (level) {
        case DEBUG:
        case DEBUG1:
            logLevel = -1;
            break;
        case DEBUG2:
        case DEBUG3:
        case DEBUG4:
        case DEBUG5:
        case DEBUG6:
        case DEBUG7:
        case DEBUG8:
        case DEBUG9:
            logLevel = -level;
            break;
        case INFO:
            logLevel = 0;
            break;
        case WARN:
            logLevel = 1;
            break;
        case ERROR:
            logLevel = 2;
            break;
        default:
            logLevel = 0;
        }
        spoolerLog.set_level(logLevel);
    }

    /** @return String Name der Logdatei */
    public String getFileName() {
        return spoolerLog.filename();
    }

}
