package sos.util;


import java.io.IOException;
import java.io.BufferedWriter;
import java.io.Writer;


/**
 * <p>Title: NullBufferedWriter </p>
 * <p>Description: Daten werden zum /dev/null umgeleitet</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$
 */


public final class NullBufferedWriter extends BufferedWriter {


  public NullBufferedWriter( Writer writer ) {
    super(writer);
  }

  public void write(char[] buf, int off, int len) throws IOException {}

  public void write(String s) throws IOException {}

  public void write(int c)  throws IOException {}


}
