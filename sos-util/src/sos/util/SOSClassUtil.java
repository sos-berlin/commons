package sos.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$
 */

public class SOSClassUtil extends java.lang.Object {

	/**
	 * Liefert den Namen des Aufruferes zurck
	 *
	 * @return String Name der Methode
	 * @throws java.lang.Exception
	 */

	/**
	 * Liefert den Namen des Aufruferes zurck
	 *
	 * @return String Name der Methode
	 * @throws java.lang.Exception
	 */
	public static String getMethodName() {

		try {
			StackTraceElement trace[] = new Throwable().getStackTrace();
			String lineNumber = trace[1].getLineNumber() > 0 ? "(" + trace[1].getLineNumber() + ")" : "";
			return trace[1].getClassName() + "." + trace[1].getMethodName() + lineNumber;
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return "";

	}

	/**
	 * Liefert den Namen der Klasse zurck
	 *
	 * @return String Name der Klasse
	 * @throws java.lang.Exception
	 */
	public static String getClassName() throws Exception {
		StackTraceElement trace[] = new Throwable().getStackTrace();
		return trace[1].getClassName();
	}

}
