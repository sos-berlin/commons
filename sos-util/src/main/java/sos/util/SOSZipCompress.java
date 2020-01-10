package sos.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Mürüvet Öksüz */
public class SOSZipCompress {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSZipCompress.class);

    public SOSZipCompress() {
    }

    public void compressFile(List<String> filenames, String archivename) throws Exception {
        try {
            FileOutputStream f = new FileOutputStream(archivename);
            CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
            for (int i = 0; i < filenames.size(); i++) {
                String filename = filenames.get(i);
                if (filename == null) {
                    filename = "";
                }
                File file = new File(filename);
                out.putNextEntry(new ZipEntry(file.getName()));
                int size = (int) (file.length());
                int bytesRead = 0;
                byte[] data = null;
                FileInputStream in = null;
                try {
                    in = new FileInputStream(file);
                    data = new byte[size];
                    while (bytesRead < size) {
                        bytesRead += in.read(data, bytesRead, size - bytesRead);
                    }
                    out.write(data);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                        }
                    }
                }
            }
            out.close();
        } catch (Exception e) {
            throw new Exception("error in " + SOSClassUtil.getMethodName() + " " + e);
        }
    }

    public void compressFile(Map<String, String> filenames, String archivename) throws Exception {

        FileOutputStream f = new FileOutputStream(archivename);
        CheckedOutputStream csum = new CheckedOutputStream(f, new Adler32());
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(csum));
        for (Entry<String, String> filename : filenames.entrySet()) {
            out.putNextEntry(new ZipEntry(new File(filename.getValue()).getName()));
            int size = (int) (new File(filename.getKey()).length());
            int bytesRead = 0;
            byte[] data = null;
            FileInputStream in = null;
            try {
                in = new FileInputStream(filename.getKey());
                data = new byte[size];
                while (bytesRead < size) {
                    bytesRead += in.read(data, bytesRead, size - bytesRead);
                }
                out.write(data);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        out.close();
    }

    public List<ZipEntry> getCompressFileName(String archivname) throws Exception {
        List<ZipEntry> retVal = null;
        ZipFile zf = null;
        try {
            retVal = new ArrayList<ZipEntry>();
            zf = new ZipFile(archivname);
            Enumeration<? extends ZipEntry> e = zf.entries();
            while (e.hasMoreElements()) {
                retVal.add(e.nextElement());
            }
            return retVal;
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public List<Map<String, Object>> getCompressFiles(String archivname) throws Exception {
        List<Map<String, Object>> retVal = null;
        String readingFile = "";
        ZipInputStream in2 = null;
        FileInputStream fi = null;
        CheckedInputStream csumi = null;
        try {
            retVal = new ArrayList<Map<String, Object>>();
            fi = new FileInputStream(archivname);
            csumi = new CheckedInputStream(fi, new Adler32());
            in2 = new ZipInputStream(new BufferedInputStream(csumi));
            ZipEntry ze;
            Map<String, Object> doc = null;
            while ((ze = in2.getNextEntry()) != null) {
                doc = new HashMap<String, Object>();
                doc.put("filename", ze);
                int x;
                while ((x = in2.read()) != -1) {
                    readingFile = readingFile + (char) x;
                }
                doc.put("file", readingFile);
                retVal.add(doc);
            }
            return retVal;
        } finally {
            if (in2 != null) {
                try {
                    in2.close();
                } catch (Exception e) {
                }
            }
            if (csumi != null) {
                try {
                    csumi.close();
                } catch (Exception e) {
                }
            }
            if (fi != null) {
                try {
                    fi.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void main(String[] args) {
        String archname = "C:/temp/2.zip";
        int test = 1;
        if (test == 1) {
            try {
                Map<String, String> testdaten = new HashMap<String, String>();
                testdaten.put("C:/printout/spool/output/tiff/1.tif", "1.tif");
                testdaten.put("C:/printout/spool/output/tiff/2.tif", "2.tif");
                SOSZipCompress com = new SOSZipCompress();
                LOGGER.debug("Archivierung der Dateien");
                com.compressFile(testdaten, archname);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else if (test == 2) {
            try {
                List<String> testdaten = new ArrayList<String>();
                testdaten.add("C:/printout/spool/output/tiff/print00001.tif");
                testdaten.add("C:/printout/spool/output/tiff/print00002.tif");
                SOSZipCompress com = new SOSZipCompress();
                LOGGER.debug("Archivierung der Dateien");
                com.compressFile(testdaten, archname);
                LOGGER.debug("Ausgabe der Dateinamen, die archiviert wurden.");
                List<ZipEntry> list = com.getCompressFileName(archname);
                for (int i = 0; i < list.size(); i++) {
                    LOGGER.debug(i + " 'te Datei: " + list.get(i).toString());
                }
                LOGGER.debug("Ausgabe der Dateinamen mit Dateiinhalten, die archiviert wurden.");
                List<Map<String, Object>> list2 = com.getCompressFiles(archname);
                Map<String, Object> hash = null;
                for (int i = 0; i < list2.size(); i++) {
                    hash = list2.get(i);
                    LOGGER.debug(i + " 'te Datei: " + hash.get("filename"));
                    LOGGER.debug("documentinhalt: " + hash.get("file"));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else if (test == 3) {
            try {
                SOSZipCompress com = new SOSZipCompress();
                com.deCompressFile("C:/temp/a/RDN_20070511_000001.tar.gz");
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
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

    public void deCompressFile(String archivname) throws Exception {
        ZipFile zipFile = new ZipFile(archivname);
        Enumeration<? extends ZipEntry> e = zipFile.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = e.nextElement();
            LOGGER.debug("\n\nFile: " + zipFile);
            if (entry.isDirectory()) {
                (new File(entry.getName())).mkdir();
                continue;
            }
            copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entry.getName())));
        }
        zipFile.close();
    }

}