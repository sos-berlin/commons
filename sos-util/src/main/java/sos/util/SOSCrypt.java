package sos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;


/** @author Ghassan Beydoun */
public class SOSCrypt {

    public static String md5encrypt(String str) throws Exception {
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(str.getBytes());
        return toHexString(md.digest());
    }

    public static String md5encrypt(File inputFile) throws Exception {
        byte[] b = createChecksum(inputFile);
        String result = toHexString(b);
        return result;
    }

    public static byte[] createChecksum(File inputFile) throws Exception {
        InputStream fis = new FileInputStream(inputFile);
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

    private static String toHexString(byte[] b) {
        char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        int length = b.length * 2;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < b.length; i++) {
            // oberer Byteanteil
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            // unterer Byteanteil
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

}