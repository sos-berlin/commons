package sos.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

/**
 * <p>Title: crypt-Klasse</p>
 * <p>Description:  Verwendet java standard Algorithmen wie MD5, SHA-1</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: SOS GmbH</p>
 * @author <a href="mailto:ghassan.beydoun@sos-berlin.com">Ghassan Beydoun</a>
 * @version $Id$
 */



  public class SOSCrypt {

    /**
     * Liefert verschlsselten Text nach der eingegebenen Algorithmos zurck
     * @param text der Text der verschlsselt werden soll
     * @param algName Name des verwendeten Algorithmus, ein
     * java standard Algorithmus name z.B. MD5, SHA-1
     * @return String der verschluesselte Text mit BASE64 Kodierung.
     * @throws java.lang.Exception
     */
    public static String encrypt( String text, String algName ) throws Exception {

      BASE64Encoder encoder = null;
      MessageDigest md      = null;
      encoder = new BASE64Encoder();
      md = MessageDigest.getInstance(algName);
      md.reset();
      md.update(text.getBytes());
      return encoder.encode(md.digest());

    }
    
    /**
     * Errechnet den MD5-Code eines Strings<br> 
     * Dieser Code ist eine hexadezimale Zahl mit 32 Zeichen Länge.<br>
     * Analog zur php Funktion md5() 
     * 
     * @param str
     * @return
     * @throws Exception
     */
    public static String MD5encrypt(String str) throws Exception {

        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(str.getBytes());

        return toHexString(md.digest());
    }
    
    /**
     * Errechnet den MD5-Code einer Datei<br> 
     * Dieser Code ist eine hexadezimale Zahl mit 32 Zeichen Länge.<br> 
     * 
     * @param inputFile 
     * @return
     * @throws Exception
     */
    public static String MD5encrypt(File inputFile) throws Exception {
    	byte[] b = createChecksum(inputFile);
    	String result = toHexString(b);
    	return result;
    }

    public static byte[] createChecksum(File inputFile) throws
    Exception{
    	InputStream fis =  new FileInputStream(inputFile);

    	byte[] buffer = new byte[1024];
    	MessageDigest md = MessageDigest.getInstance("MD5");
    	int numRead;
    	do {
    		numRead = fis.read(buffer);
    		if (numRead > 0) {
    			md.update(buffer, 0, numRead);
    		}
    	} while (numRead != -1);
    	fis.close();
    	return md.digest();
    }
    
    /**
     * Wandelt ein Byte-Array in ein Hex-String um.
     * 
     * @param bin
     *            Byte-Array
     */
    private static String toHexString(byte[] b) {
        
        char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        
        int length = b.length * 2;

        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < b.length; i++) {
            // oberer Byteanteil
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            // unterer Byteanteil
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    


}

