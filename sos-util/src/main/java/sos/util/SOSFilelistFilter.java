package sos.util;

import java.io.File;
import java.io.FilenameFilter;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** <p>
 * Title:
 * </p>
 * <p>
 * Description: Filefilter-Schnittstelle
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

public class SOSFilelistFilter implements FilenameFilter {

    Pattern pattern;

    /** Konstruktor
     * 
     * @param regexp ein regul�er Ausdruck
     * @param flag ist ein Integer-Wert: CASE_INSENSITIVE, MULTILINE, DOTALL,
     *            UNICODE_CASE, and CANON_EQ
     * @see <a
     *      href="http://java.sun.com/j2se/1.4.2/docs/api/constant-values.html#java.util.regex.Pattern.UNIX_LINES">Constant
     *      Field Values</a> */
    public SOSFilelistFilter(String regexp, int flag) throws Exception {
        pattern = Pattern.compile(regexp, flag);
    }

    public boolean accept(File dir, String filename) {
        Matcher matcher = pattern.matcher(filename);
        return matcher.find();
    }

}
