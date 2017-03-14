package sos.util;

/** @author Ghassan Beydoun */
public class SOSSchedulerLogger extends SOSLogger {

    private sos.spooler.Log spoolerLog;

    public SOSSchedulerLogger(sos.spooler.Log spoolerLog) throws Exception {
        if (spoolerLog == null) {
            throw new Exception("spooler_log is null.");
        }
        this.spoolerLog = spoolerLog;
    }

    public void warn(String str)   {
        log(WARN, str);
        setWarning(str);
    }

    public void error(String str)  {
        log(ERROR, str);
        setError(str);
    }

    public void info(String str) {
        log(INFO, str);
    }

    public void debug(String str)  {
        log(DEBUG, str);
    }

    public void debug1(String str)  {
        log(DEBUG1, str);
    }

    public void debug2(String str)   {
        log(DEBUG2, str);
    }

    public void debug3(String str)   {
        log(DEBUG3, str);
    }

    public void debug4(String str)  {
        log(DEBUG4, str);
    }

    public void debug5(String str)   {
        log(DEBUG5, str);
    }

    public void debug6(String str) {
        log(DEBUG6, str);
    }

    public void debug7(String str)  {
        log(DEBUG7, str);
    }

    public void debug8(String str)   {
        log(DEBUG8, str);
    }

    public void debug9(String str)   {
        log(DEBUG9, str);
    }

    public void log(int level, String str)  {
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

    public void close() throws Exception {
        if (spoolerLog != null) {
            spoolerLog = null;
        }
    }

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

    public String getFileName() {
        return spoolerLog.filename();
    }

}
