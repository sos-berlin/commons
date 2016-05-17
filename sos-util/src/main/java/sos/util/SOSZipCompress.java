package sos.util;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import org.apache.log4j.Logger;

/** @author Mürüvet Öksüz */
public class SOSZipCompress {

    private static final Logger LOGGER = Logger.getLogger(SOSZipCompress.class);
    private SOSString sosString = null;

    public SOSZipCompress() throws Exception {
        try {
            sosString = new SOSString();
        } catch (Exception e) {
            throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public void compressFile(ArrayList filenames, String archivename) throws Exception {
        try {
            FileOutputStream f = new FileOutputStream(archivename);
            CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
            for (int i = 0; i < filenames.size(); i++) {
                out.putNextEntry(new ZipEntry(new File(sosString.parseToString(filenames.get(i))).getName()));
                int size = (int) (new File(sosString.parseToString(filenames.get(i))).length());
                int bytesRead = 0;
                byte[] data = null;
                FileInputStream in = null;
                try {
                    in = new FileInputStream(sosString.parseToString(filenames.get(i)));
                    data = new byte[size];
                    while (bytesRead < size) {
                        bytesRead += in.read(data, bytesRead, size - bytesRead);
                    }
                    out.write(data);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
            out.close();
        } catch (Exception e) {
            throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public void compressFile(HashMap filenames, String archivename) throws Exception {
        try {
            FileOutputStream f = new FileOutputStream(archivename);
            CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
            Iterator keys = filenames.keySet().iterator();
            Iterator values = filenames.values().iterator();
            String key = "";
            String val = "";
            while (keys.hasNext()) {
                key = sosString.parseToString(keys.next());
                val = sosString.parseToString(values.next());
                out.putNextEntry(new ZipEntry(new File(sosString.parseToString(val)).getName()));
                int size = (int) (new File(sosString.parseToString(key)).length());
                int bytesRead = 0;
                byte[] data = null;
                FileInputStream in = null;
                try {
                    in = new FileInputStream(sosString.parseToString(key));
                    data = new byte[size];
                    while (bytesRead < size) {
                        bytesRead += in.read(data, bytesRead, size - bytesRead);
                    }
                    out.write(data);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
            out.close();
        } catch (Exception e) {
            throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public ArrayList getCompressFileName(String archivname) throws Exception {
        ArrayList retVal = null;
        try {
            retVal = new ArrayList();
            ZipFile zf = new ZipFile(archivname);
            Enumeration e = zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                retVal.add(ze2);
            }
            return retVal;
        } catch (Exception e) {
            throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public ArrayList getCompressFiles(String archivname) throws Exception {
        ArrayList retVal = null;
        String readingFile = "";
        try {
            retVal = new ArrayList();
            FileInputStream fi = new FileInputStream(archivname);
            CheckedInputStream csumi = new CheckedInputStream(fi, new Adler32());
            ZipInputStream in2 = new ZipInputStream(new BufferedInputStream(csumi));
            ZipEntry ze;
            HashMap doc = null;
            while ((ze = in2.getNextEntry()) != null) {
                doc = new HashMap();
                doc.put("filename", ze);
                int x;
                while ((x = in2.read()) != -1) {
                    readingFile = readingFile + (char) x;
                }
                doc.put("file", readingFile);
                retVal.add(doc);
            }
            in2.close();
            return retVal;
        } catch (Exception e) {
            throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public static void main(String[] args) {
        String archname = "C:/temp/2.zip";
        int test = 1;
        if (test == 1) {
            try {
                HashMap testdaten = new HashMap();
                testdaten.put("C:/printout/spool/output/tiff/1.tif", "1.tif");
                testdaten.put("C:/printout/spool/output/tiff/2.tif", "2.tif");
                SOSZipCompress com = new SOSZipCompress();
                LOGGER.debug("Archivierung der Dateien");
                com.compressFile(testdaten, archname);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        } else if (test == 2) {
            try {
                ArrayList testdaten = new ArrayList();
                testdaten.add("C:/printout/spool/output/tiff/print00001.tif");
                testdaten.add("C:/printout/spool/output/tiff/print00002.tif");
                SOSZipCompress com = new SOSZipCompress();
                LOGGER.debug("Archivierung der Dateien");
                com.compressFile(testdaten, archname);
                LOGGER.debug("Ausgabe der Dateinamen, die archiviert wurden.");
                ArrayList list = com.getCompressFileName(archname);
                for (int i = 0; i < list.size(); i++) {
                    LOGGER.debug(i + " 'te Datei: " + list.get(i));
                }
                LOGGER.debug("Ausgabe der Dateinamen mit Dateiinhalten, die archiviert wurden.");
                ArrayList list2 = com.getCompressFiles(archname);
                HashMap hash = null;
                for (int i = 0; i < list2.size(); i++) {
                    hash = (HashMap) list2.get(i);
                    LOGGER.debug(i + " 'te Datei: " + hash.get("filename"));
                    LOGGER.debug("documentinhalt: " + hash.get("file"));
                }
            } catch (Exception e) {
                LOGGER.error(e);
            }
        } else if (test == 3) {
            try {
                SOSZipCompress com = new SOSZipCompress();
                com.deCompressFile("C:/temp/a/RDN_20070511_000001.tar.gz");
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }
    }

    public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }

    public ArrayList deCompressFile(String archivname) throws Exception {
        ArrayList retVal = null;
        try {
            retVal = new ArrayList();
            ZipFile zipFile = new ZipFile(archivname);
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                LOGGER.debug("\n\nFile: " + zipFile);
                if (entry.isDirectory()) {
                    (new File(entry.getName())).mkdir();
                    continue;
                }
                copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entry.getName())));
            }
            zipFile.close();
            return retVal;
        } catch (Exception e) {
            throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

}