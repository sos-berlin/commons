package sos.util;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.Writer;

/** @author Ghassan Beydoun */
public final class NullBufferedWriter extends BufferedWriter {

    public NullBufferedWriter(Writer writer) {
        super(writer);
    }

    public void write(char[] buf, int off, int len) throws IOException {
        //
    }

    public void write(String s) throws IOException {
        //
    }

    public void write(int c) throws IOException {
        //
    }

}