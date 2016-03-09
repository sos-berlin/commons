package sos.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

import sos.util.SOSFile;
import sos.util.SOSLogger;
import sos.util.SOSStandardLogger;

/** Klasse zum Senden von Dateien an eine URL.
 * 
 * @author fs
 * @since 22.02.2006 */
public class SOSHttpPost {

    private String targetURL = null;
    private SOSLogger soslogger = null;

    /** @param url URL des Servers, an den die Datei(en) geschickt werden */
    public SOSHttpPost(URL url) {

        this(url, null);
    }

    /** @param url URL des Servers, an den die Datei(en) geschickt werden */
    public SOSHttpPost(String url) {

        this(url, null);
    }

    /** @param url URL des Servers, an den die Datei(en) geschickt werden
     * @param soslogger SOSLogger zur Protokollierung */
    public SOSHttpPost(URL url, SOSLogger soslogger) {

        this(url.toString(), soslogger);
    }

    /** @param url URL des Servers, an den die Datei(en) geschickt werden
     * @param soslogger SOSLogger zur Protokollierung */
    public SOSHttpPost(String url, SOSLogger soslogger) {

        this.targetURL = url;
        this.soslogger = soslogger;

        // für HttpClient NULL-Logger setzen
        System.setProperty("org.apache.commons.logging.log", "sos.util.SOSJCLNullLogger");
        // System.out.println("HttpClient Logger: "+System.getProperty("org.apache.commons.logging.log"));

        // System.setProperty("org.apache.commons.logging.log",
        // "org.apache.commons.logging.impl.SimpleLog");
        // log4j
        // System.setProperty("org.apache.commons.logging.log",
        // "org.apache.commons.logging.impl.Log4JLogger");
        // System.setProperty("log4j.configuration",
        // "httpsend.log4j.properties");
    }

    private static void printUsage() {

        String usage = "\n";
        usage += " usage: SOSHttpPost -i INPUT [-o OUTPUT] -u URL\n";
        usage += "        INPUT will be sent to server URL, OUTPUT contains the server response.\n";
        usage += "        This tool expects a service for INPUT processing on the server.\n";
        usage += "        INPUT must be a file or directory.\n";
        usage += "        if -o is set OUTPUT must be a file if INPUT is a file or a directory if INPUT is a directory.\n";
        usage += "        URL must be a valid server URL.";
        System.out.println(usage);
    }

    private void log_warn(String msg) throws Exception {
        if (this.soslogger != null)
            this.soslogger.warn(msg);
    }

    private void log_info(String msg) throws Exception {

        if (this.soslogger != null)
            this.soslogger.info(msg);
    }

    private String writeResponse(InputStream responseStream, String outputFile) throws Exception {

        if (outputFile == null)
            throw new Exception("cannot write response: output file is null");
        if (responseStream == null)
            throw new Exception("cannot write response: response is null");

        File out = new File(outputFile);

        if (!out.canRead()) {

            File path = new File(out.getParent());
            if (!path.canRead())
                path.mkdirs();

            out.createNewFile();
        }

        if (!out.canWrite())
            throw new Exception("cannot write " + out.getCanonicalPath());

        FileOutputStream outputStream = new FileOutputStream(out);

        try {

            byte buffer[] = new byte[1000];
            int numOfBytes = 0;

            while ((numOfBytes = responseStream.read(buffer)) != -1)
                outputStream.write(buffer, 0, numOfBytes);

            return " - write response to file " + out.getCanonicalPath();

        } catch (Exception e) {
            throw new Exception("error occurred logging to file [" + out.getCanonicalPath() + "]: " + e.getMessage());
        } finally {
            try {
                if (responseStream != null) {
                    responseStream.close();
                }
            } catch (Exception ex) {
            } // gracefully ignore this error
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception ex) {
            } // gracefully ignore this error
        }
    }

    private void sendFile(File inputFile, String outputFile) throws Exception {

        this.log_info("sending " + inputFile.getCanonicalPath() + " ...");

        int responseCode = -1;

        try {

            String suffix = null;
            String contentType = null;
            if (inputFile.getName().lastIndexOf('.') != -1) {

                suffix = inputFile.getName().substring(inputFile.getName().lastIndexOf('.') + 1);
                if (suffix.equals("htm") || suffix.equals("html"))
                    contentType = "text/html";
                if (suffix.equals("xml"))
                    contentType = "text/xml";
            }

            // encoding ermitteln
            if (contentType != null)
                if (contentType.equals("text/html") || contentType.equals("text/xml")) {

                    BufferedReader br = new BufferedReader(new FileReader(inputFile));
                    String buffer = "";
                    String line = null;
                    int c = 0;
                    // die ersten 5 Zeilen nach einem encoding durchsuchen
                    while ((line = br.readLine()) != null || ++c > 5)
                        buffer += line;
                    Pattern p = Pattern.compile("encoding[\\s]*=[\\s]*['\"](.*?)['\"]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(buffer);
                    if (m.find())
                        contentType += "; charset=" + m.group(1);

                    br.close();
                }

            if (contentType == null)
                contentType = "text/plain";

            PostMethod post = new PostMethod(this.targetURL);

            post.setRequestEntity(new InputStreamRequestEntity(new FileInputStream(inputFile), inputFile.length()));
            post.setRequestHeader("Content-Type", contentType);
            // post.setFollowRedirects(true);

            HttpClient httpClient = new HttpClient();

            responseCode = httpClient.executeMethod(post);

            // Response Code überprüfen
            // codes 200 - 399 gehen durch
            if (responseCode < 200 || responseCode >= 400)
                throw new HttpException(this.targetURL.toString() + " returns " + post.getStatusLine().toString());

            // response
            String writeMsg = "";
            if (outputFile != null)
                writeMsg = this.writeResponse(post.getResponseBodyAsStream(), outputFile);

            // abschließende Meldung
            this.log_info(inputFile.getCanonicalPath() + " sent" + writeMsg);

        } catch (UnknownHostException uhx) {
            throw new Exception("UnknownHostException: " + uhx.getMessage());
        } catch (FileNotFoundException nfx) {
            throw new Exception("FileNotFoundException: " + nfx.getMessage());
        } catch (HttpException htx) {
            throw new Exception("HttpException: " + htx.getMessage());
        } catch (IOException iox) {
            throw new Exception("IOException: " + iox.getMessage());
        }
    }

    /** Sendet eine Datei an den Server, rekursiv für Verzeichnisse
     * 
     * @param file Datei oder Verzeichnis
     * @return liefert die Anzahl der gesendeten Dateien zurück
     * @throws Exception */
    private int recursiveSend(File inputFile, String inputFileSpec, String outputFile) throws Exception {

        int sendcounter = 0;

        // Verzeichnis
        if (inputFile.isDirectory()) {
            Vector filelist = SOSFile.getFilelist(inputFile.getAbsolutePath(), inputFileSpec, 0);
            Iterator iterator = filelist.iterator();

            File nextFile;
            while (iterator.hasNext()) {
                nextFile = (File) iterator.next();
                sendcounter += this.recursiveSend(nextFile, inputFileSpec, ((outputFile == null) ? null : outputFile + "/" + nextFile.getName()));
            }

            // Datei
        } else if (inputFile.isFile()) {

            this.sendFile(inputFile, outputFile);
            sendcounter++;

        } else {
            throw new Exception(inputFile.getCanonicalPath() + " is no normal file");
        }

        return sendcounter;
    }

    /** Sendet die Eingabedatei oder den Inhalt des Eingabeverzeichnis an den
     * Server. Die Response des Servers wird nicht gespeichert.
     * 
     * @param inputFile Eingabedatei oder Eingabeverzeichnis
     * @throws Exception */
    public void process(File inputFile) throws Exception {

        this.process(inputFile, null);
    }

    /** Sendet die Eingabedatei oder den Inhalt des Eingabeverzeichnis an den
     * Server. Schreibt für jede gesendete Datei die Response des Servers in die
     * Ausgabedatei bzw. in eine gleichnamige Datei im Ausgabeverzeichnis. Wenn
     * inputFile eine Datei ist, dann muss auch outputFile eine Datei sein. Wenn
     * inputFile ein Verzeichnis ist, dann muss auch outputFile ein Verzeichnis
     * sein.
     * 
     * @param inputFile Eingabedatei oder Eingabeverzeichnis
     * @param outputFile Ausgabedatei oder Ausgabeverzeichnis
     * @throws Exception */
    public void process(File inputFile, File outputFile) throws Exception {

        try {

            if (Input_And_Output_Processable(inputFile, outputFile)) {

                this.log_info("send file(s) to " + this.targetURL.toString());

                int nrOfSentFiles = 0;

                if (outputFile != null)
                    nrOfSentFiles = this.recursiveSend(inputFile, "", outputFile.getAbsolutePath());
                else
                    nrOfSentFiles = this.recursiveSend(inputFile, "", null);

                this.log_info(nrOfSentFiles + " file" + ((nrOfSentFiles == 1) ? "" : "s") + " sent");
            }

        } catch (Exception e) {

            this.log_warn(e.getMessage());
            throw e;
        }
    }

    private boolean Input_And_Output_Processable(File inputFile, File outputFile) throws Exception {

        boolean outputFileNewCreated = false;

        // existiert inputFile?
        if (!inputFile.canRead())
            throw new Exception(inputFile.getCanonicalPath() + " not found or not readable");

        // test des outputFiles übergehen falls null
        if (outputFile != null) {

            // outputFile existiert nicht
            if (!outputFile.canRead()) {
                try {

                    if (inputFile.isDirectory())
                        outputFile.mkdirs();
                    else
                        outputFile.createNewFile();

                    outputFileNewCreated = true;

                } catch (Exception e) {
                    throw new Exception("out: " + outputFile.getPath() + " - " + e.getMessage());
                }
            }

            // outputFile existiert jetzt immer
            if (!inputFile.isDirectory() && outputFile.isDirectory())
                throw new Exception(inputFile.getCanonicalPath() + " is a file and " + outputFile.getCanonicalPath() + " is a directory but "
                        + "inputFile and outputFile must be both directories or files");

            if (inputFile.isDirectory() && !outputFile.isDirectory())
                throw new Exception(inputFile.getCanonicalPath() + " is a directory and " + outputFile.getCanonicalPath() + " is a file but "
                        + "inputFile and outputFile must be both directories or files");

            if (!outputFile.canWrite())
                throw new Exception(outputFile.getCanonicalPath() + " is not writable");
        }

        // nach dem Test NEU angelegte Datei oder NEU angelegtes Verzeichnis
        // löschen
        if (outputFileNewCreated) {
            outputFile.delete();
        }

        return true;
    }

    public static void main(String[] args) {

        String input = null;
        File inputFile = null;
        String output = null;
        File outputFile = null;
        URL url = null;
        SOSHttpPost httpsend = null;

        // Kommandozeilenparameter einlesen
        try {

            if (args.length == 0) {
                SOSHttpPost.printUsage();
                System.exit(0);
            }

            if (args.length == 1) {
                String arg = args[0].toLowerCase();
                if (arg.equals("h") || arg.equals("help") || arg.equals("-h") || arg.equals("-help") || arg.equals("--h") || arg.equals("--help")) {
                    SOSHttpPost.printUsage();
                    System.exit(0);
                }
            }

            for (int i = 0; i < args.length; i++) {

                if (args[i].equals("-i") || args[i].equals("-I")) {
                    if (i + 1 < args.length)
                        input = args[i + 1];
                    else
                        throw new Exception("value for parameter -i is missing (inputfile)");
                }

                if (args[i].equals("-o") || args[i].equals("-O")) {
                    if (i + 1 < args.length)
                        output = args[i + 1];
                    else
                        throw new Exception("value for parameter -o is missing (outputfile)");
                }

                if (args[i].equals("-u") || args[i].equals("-U")) {
                    if (i + 1 < args.length)
                        url = new URL(args[i + 1]);
                    else
                        throw new Exception("value for parameter -u is missing (URL)");
                }
            }

            if (input == null)
                throw new Exception("inputfile is missing");
            if (url == null)
                throw new Exception("URL is missing");

            inputFile = new File(input);

            outputFile = null;
            if (output != null)
                outputFile = new File(output);

        } catch (MalformedURLException mux) {
            System.err.println("MalformedURLException: " + mux.getMessage());
            System.exit(-1);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(-1);
        }

        // HttpSend Objekt mit SOSStandardLogger erzeugen
        try {

            httpsend = new SOSHttpPost(url, new SOSStandardLogger(0));

        } catch (Exception x) {
            System.err.println("Error: " + x.getMessage());
            System.exit(-1);
        }

        // Ausführen
        try {

            httpsend.process(inputFile, outputFile);

        } catch (Exception e) {
            System.exit(-1);
        }

        System.exit(0);
    }
}
