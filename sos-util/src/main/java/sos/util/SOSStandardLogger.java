package sos.util;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;

import java.text.SimpleDateFormat;
import java.text.DateFormat;

import java.util.Date;

/** @author Ghassan Beydoun */
public class SOSStandardLogger extends SOSLogger implements Serializable {

    private static final DateFormat TIME = new SimpleDateFormat("HH:mm:ss.SSS");
    private static final DateFormat TIMESTAMP = new SimpleDateFormat("EEEE MMMM dd HH:mm:ss yyyy");
    private static final String INDENT = "\t";
    private BufferedWriter writer;
    private Date currentDate = null;
    private int logLevel = 10;
    private String newLine = System.getProperty("line.separator");
    private boolean headerWritten = false;

    public SOSStandardLogger(int logLevel) throws Exception {
        this(new OutputStreamWriter(System.out), logLevel);
    }

    public SOSStandardLogger(Writer writer, int logLevel) throws Exception {
        this.logLevel = logLevel;
        this.writer = new BufferedWriter(writer);
    }

    public SOSStandardLogger(String logfile, int logLevel) throws Exception {
        File f = new File(logfile);
        this.logLevel = logLevel;
        writer = new BufferedWriter(new FileWriter(f, true));
    }

    public void writeHeader() throws Exception {
        currentDate = new Date();
        String str = TIME.format(currentDate) + "---------- " + TIMESTAMP.format(currentDate) + newLine;
        this.write(str);
    }

    public void writeHeader(String prefix) throws Exception {
        currentDate = new Date();
        String str = TIME.format(currentDate) + INDENT + "---------" + prefix + (char) (32) + TIMESTAMP.format(currentDate) + newLine;
        this.write(str);
    }

    protected synchronized void log(int level, String str) throws Exception {
        if ((logLevel == INFO || logLevel == WARN || logLevel == ERROR) && (level < logLevel)) {
            return;
        } else if ((level > logLevel) && (level != INFO && level != WARN && level != ERROR)) {
            return;
        }
        StringBuilder buffer = new StringBuilder();
        currentDate = new Date();
        if (!headerWritten) {
            this.writeHeader();
            headerWritten = true;
        }
        buffer.append(TIME.format(currentDate));
        buffer.append(INDENT);
        if (level == INFO) {
            buffer.append("[info]").append((char) 32).append((char) 32);
        } else if (level == WARN) {
            buffer.append("[warn]").append((char) 32).append((char) 32);
            setWarning(str);
        } else if (level == ERROR) {
            buffer.append("[error]").append((char) 32);
            setError(str);
        } else if (level == DEBUG) {
            buffer.append("[debug]").append((char) 32);
        } else {
            buffer.append("[debug");
            if (level == 0) {
                buffer.append("]").append((char) 32).append((char) 32);
            } else {
                buffer.append(Integer.toString(level)).append("]");
            }
        }
        buffer.append((char) 32).append(str).append(newLine);
        this.write(buffer.toString());
    }

    private synchronized void write(String str) throws IOException {
        if (writer == null) {
            throw new IOException("Logger is closed.");
        }
        writer.write(str);
        writer.flush();
    }

    public void warn(String str) throws Exception {
        log(WARN, str);
        setWarning(str);
    }

    public void error(String str) throws Exception {
        log(ERROR, str);
        setError(str);
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
        if (writer != null) {
            writer.close();
        }
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    protected Writer getWriter() {
        return writer;
    }

}
