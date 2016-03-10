package sos.util;

import java.io.BufferedWriter;
import java.io.Writer;
import java.util.Iterator;

/** @author Ghassan Beydoun */
public class SOSCSVWriter extends BufferedWriter {

    private static final String FIELD_SEPARATOR = ";";
    private static final String QUOTE = "\"";
    private String fieldSeparator;
    private String quote = "";
    private boolean useQuotes = false;
    private int lineCount = 0;
    private StringBuilder line = new StringBuilder();
    private int fieldCount = 0;
    private String field = null;

    public SOSCSVWriter(Writer writer) {
        super(writer);
        this.fieldSeparator = SOSCSVWriter.FIELD_SEPARATOR;
        this.field = null;
        this.fieldCount = 0;
        this.line = new StringBuilder();
    }

    public SOSCSVWriter(Writer writer, String separator, boolean useQuotes) {
        super(writer);
        if (useQuotes) {
            this.setQuote(SOSCSVWriter.QUOTE);
        }
        this.fieldSeparator = separator;
        this.field = null;
        this.fieldCount = 0;
        this.line = new StringBuilder();
    }

    public void writeRecord(Iterator record) throws Exception {
        fieldCount = 0;
        line = new StringBuilder();
        while (record.hasNext()) {
            if (fieldCount++ > 0) {
                line.append(fieldSeparator);
            }
            field = null;
            try {
                field = record.next().toString();
            } catch (Exception e) {
                continue;
            }
            encodeField();
        }
        super.write(line.toString());
        this.lineCount++;
        super.newLine();
    }

    public int getLineCount() throws Exception {
        return this.lineCount;
    }

    private void encodeField() throws Exception {
        line.append(quote);
        if (field.indexOf('\r') >= 0 || field.indexOf('\n') >= 0) {
            throw new Exception(line.toString() + ": not allowed character (CR,LF) on line[" + lineCount + "].");
        }
        line.append(field);
        line.append(quote);
    }

    public final String getQuote() {
        return quote;
    }

    public final void setQuote(String quote) {
        this.quote = quote;
    }

    public final boolean isUseQuotes() {
        return useQuotes;
    }

    public final void setUseQuotes(boolean useQuotes) {
        this.useQuotes = useQuotes;
    }

}