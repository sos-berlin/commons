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

/** <p>
 * Title:
 * </p>
 * <p>
 * Description: Logger-Class
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

public class SOSStandardLogger extends SOSLogger implements Serializable {

    private BufferedWriter writer;

    private static final DateFormat TIME = new SimpleDateFormat("HH:mm:ss.SSS");

    private static final DateFormat TIMESTAMP = new SimpleDateFormat("EEEE MMMM dd HH:mm:ss yyyy");

    private Date currentDate = null;

    private int logLevel = 10;

    private static final String INDENT = "\t";

    private String newLine = System.getProperty("line.separator");

    private boolean headerWritten = false;

    /** Constructor logging nach stdout
     *
     * @param logLevel value beetween 1 and 9
     * @throws Exception */
    public SOSStandardLogger(int logLevel) throws Exception {
        this(new OutputStreamWriter(System.out), logLevel);
    }

    /** logging in a Stream.
     * 
     * @param writer as stream object
     * @param logLevel value beetween 1 and 9
     * @throws Exception */
    public SOSStandardLogger(Writer writer, int logLevel) throws Exception {
        this.logLevel = logLevel;
        this.writer = new BufferedWriter(writer);

    }

    /** logging in Datei
     *
     * @param logfile the name of the logfile
     * @param logLevel value beetween 1 and 9
     * @throws Exception */
    public SOSStandardLogger(String logfile, int logLevel) throws Exception {
        File f = new File(logfile);

        this.logLevel = logLevel;
        writer = new BufferedWriter(new FileWriter(f, true));
    }

    /** write the header
     *
     * @throws java.lang.Exception */
    public void writeHeader() throws Exception {
        currentDate = new Date();
        String str = TIME.format(currentDate) + "---------- " + TIMESTAMP.format(currentDate) + newLine;
        this.write(str);
    }

    /** write the header
     * 
     * @param prefix String
     * @throws java.lang.Exception */
    public void writeHeader(String prefix) throws Exception {
        currentDate = new Date();
        String str = TIME.format(currentDate) + INDENT + "---------" + prefix + (char) (32) + TIMESTAMP.format(currentDate) + newLine;
        this.write(str);
    }

    /** write the specified message
     *
     * @param level loglevel Type
     * @param str the specified message
     * @throws Exception */
    protected synchronized void log(int level, String str) throws Exception {

        if ((logLevel == INFO || logLevel == WARN || logLevel == ERROR) && (level < logLevel)) {
            return;
        } else if ((level > logLevel) && (level != INFO && level != WARN && level != ERROR)) {
            return;
        }

        // System.out.println("level:" + level + ", logLevel: " + logLevel);
        StringBuffer buffer = new StringBuffer();
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

    /** Schreibt in den stream
     *
     * @param str the specified message
     * @throws IOException */
    private synchronized void write(String str) throws IOException {
        if (writer == null)
            throw new IOException("Logger is closed.");
        writer.write(str);
        writer.flush();
    }

    /** write warning-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void warn(String str) throws Exception {
        log(WARN, str);
        setWarning(str);
    }

    /** write error-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void error(String str) throws Exception {
        log(ERROR, str);
        setError(str);
    }

    /** write info-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void info(String str) throws Exception {
        log(INFO, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug(String str) throws Exception {
        log(DEBUG, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug1(String str) throws Exception {
        log(DEBUG1, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug2(String str) throws Exception {
        log(DEBUG2, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug3(String str) throws Exception {
        log(DEBUG3, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug4(String str) throws Exception {
        log(DEBUG4, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug5(String str) throws Exception {
        log(DEBUG5, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug6(String str) throws Exception {
        log(DEBUG6, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug7(String str) throws Exception {
        log(DEBUG7, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug8(String str) throws Exception {
        log(DEBUG8, str);
    }

    /** write debug-message
     *
     * @param str the specified message
     * @throws java.lang.Exception */
    public void debug9(String str) throws Exception {
        log(DEBUG9, str);
    }

    /** close the stream object
     *
     * @throws java.lang.Exception */
    public void close() throws Exception {
        if (writer != null)
            writer.close();
    }

    /** @return Returns the logLevel. */
    public int getLogLevel() {
        return logLevel;
    }

    /** @param logLevel The logLevel to set. */
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    /** @return Returns the newLine. */
    public String getNewLine() {
        return newLine;
    }

    /** @param newLine The newLine to set. */
    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    /** @return out */
    protected Writer getWriter() {
        return writer;
    }

}
