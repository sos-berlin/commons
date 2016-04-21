package sos.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/** @author Ghassan Beydoun
 * @author Andreas Püschel
 * @author Andreas Liebert */
public class SOSXMLTransformer {

    private static final HashMap EMPTY_HASH_MAP = new HashMap();

    private SOSXMLTransformer() {

    }

    public static void transform(String data, File xslFile, File outputFile) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(data, xslFile, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(String data, File xslFile, File outputFile, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (data.isEmpty()) {
            throw new Exception("SOSXMLTransformer: no xml document contained in data.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(new StringReader(data)), new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(File xmlFile, File xslFile, File outputFile) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(xmlFile, xslFile, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(File xmlFile, File xslFile, File outputFile, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (!xmlFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing xml document.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(new FileInputStream(xmlFile)), new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(StreamSource stream, File xslFile, File outputFile) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(stream, xslFile, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(StreamSource stream, File xslFile, File outputFile, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stream == null) {
            throw new Exception("SOSXMLTransformer: no xml document contained in stream.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
        addParameters(transformer, parameters);
        transformer.transform(stream, new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(FileInputStream stream, File xslFile, File outputFile) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(stream, xslFile, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(FileInputStream stream, File xslFile, File outputFile, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stream == null) {
            throw new Exception("SOSXMLTransformer: no xml document contained in stream.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(stream), new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(File inputFile, File outputFile) throws TransformerException, TransformerConfigurationException, FileNotFoundException,
            IOException, Exception {
        transform(inputFile, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(File inputFile, File outputFile, HashMap parameters) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        if (!inputFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing xml document.");
        }
        StreamSource stream = new StreamSource(inputFile);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Source stylesheet = tFactory.getAssociatedStylesheet(stream, null, null, null);
        if (stylesheet == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet found in input file.");
        }
        Transformer transformer = tFactory.newTransformer(stylesheet);
        addParameters(transformer, parameters);
        transformer.transform(stream, new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(String data, StreamSource stylesheetStream, File outputFile) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(data, stylesheetStream, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(String data, StreamSource stylesheetStream, File outputFile, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (data.isEmpty()) {
            throw new Exception("SOSXMLTransformer: no xml document contained in data.");
        }
        if (stylesheetStream == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet contained in stream.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(stylesheetStream);
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(new StringReader(data)), new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(StreamSource stream, StreamSource stylesheetStream, File outputFile) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        transform(stream, stylesheetStream, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(StreamSource stream, StreamSource stylesheetStream, File outputFile, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stream == null) {
            throw new Exception("SOSXMLTransformer: no xml document contained in stream.");
        }
        if (stylesheetStream == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet contained in stream.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(stylesheetStream);
        addParameters(transformer, parameters);
        transformer.transform(stream, new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(FileInputStream stream, FileInputStream stylesheetStream, File outputFile) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        transform(stream, stylesheetStream, outputFile, EMPTY_HASH_MAP);
    }

    public static void transform(FileInputStream stream, FileInputStream stylesheetStream, File outputFile, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stylesheetStream == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet contained in stream");
        }
        StreamSource styleStream = new StreamSource(stylesheetStream);
        StreamSource inputStream = new StreamSource(stream);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(styleStream);
        addParameters(transformer, parameters);
        transformer.transform(inputStream, new StreamResult(new FileOutputStream(outputFile)));
    }

    public static void transform(String data, File xslFile, StreamResult outputStream) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(data, xslFile, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(String data, File xslFile, StreamResult outputStream, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (data.isEmpty()) {
            throw new Exception("SOSXMLTransformer: no xml document contained in data.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(new StringReader(data)), outputStream);
    }

    public static void transform(File xmlFile, File xslFile, StreamResult outputStream) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(xmlFile, xslFile, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(File xmlFile, File xslFile, StreamResult outputStream, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (!xmlFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing xml document.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StreamSource xslSource = new StreamSource(xslFile);
        Transformer transformer = tFactory.newTransformer(xslSource);
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(new FileInputStream(xmlFile)), outputStream);
    }

    public static void transform(StreamSource stream, File xslFile, StreamResult outputStream) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(stream, xslFile, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(StreamSource stream, File xslFile, StreamResult outputStream, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stream == null) {
            throw new Exception("SOSXMLTransformer: no xml document contained in stream.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
        addParameters(transformer, parameters);
        transformer.transform(stream, outputStream);
    }

    public static void transform(FileInputStream stream, File xslFile, StreamResult outputStream) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        transform(stream, xslFile, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(FileInputStream stream, File xslFile, StreamResult outputStream, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stream == null) {
            throw new Exception("SOSXMLTransformer: no xml document contained in stream.");
        }
        if (!xslFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing stylesheet.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslFile));
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(stream), outputStream);
    }

    public static void transform(File inputFile, StreamResult outputStream) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(inputFile, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(File inputFile, StreamResult outputStream, HashMap parameters) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        if (!inputFile.exists()) {
            throw new Exception("SOSXMLTransformer: no file found containing xml document.");
        }
        StreamSource stream = new StreamSource(inputFile);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Source stylesheet = tFactory.getAssociatedStylesheet(stream, null, null, null);
        if (stylesheet == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet found in input file.");
        }
        Transformer transformer = tFactory.newTransformer(stylesheet);
        addParameters(transformer, parameters);
        transformer.transform(stream, outputStream);
    }

    public static void transform(StreamSource inputStream, StreamResult outputStream) throws TransformerException, TransformerConfigurationException,
            FileNotFoundException, IOException, Exception {
        transform(inputStream, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(StreamSource inputStream, StreamResult outputStream, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
        InputStream in = inputStream.getInputStream();
        int c;
        while ((c = in.read()) != -1) {
            bAOS.write((char) c);
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bAOS.toByteArray());
        Source stylesheet = tFactory.getAssociatedStylesheet(new StreamSource(byteArrayInputStream), null, null, null);
        if (stylesheet == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet found in input file.");
        }
        Transformer transformer = tFactory.newTransformer(stylesheet);
        addParameters(transformer, parameters);
        byteArrayInputStream = new ByteArrayInputStream(bAOS.toByteArray());
        transformer.transform(new StreamSource(byteArrayInputStream), outputStream);
    }

    public static void transform(String data, StreamSource stylesheetStream, StreamResult outputStream) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        transform(data, stylesheetStream, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(String data, StreamSource stylesheetStream, StreamResult outputStream, HashMap parameters) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (data.isEmpty()) {
            throw new Exception("SOSXMLTransformer: no xml document contained in data.");
        }
        if (stylesheetStream == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet contained in stream.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(stylesheetStream);
        addParameters(transformer, parameters);
        transformer.transform(new StreamSource(new StringReader(data)), outputStream);
    }

    public static void transform(StreamSource stream, StreamSource stylesheetStream, StreamResult outputStream) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        transform(stream, stylesheetStream, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(StreamSource stream, StreamSource stylesheetStream, StreamResult outputStream, HashMap parameters)
            throws TransformerException, TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stream == null) {
            throw new Exception("SOSXMLTransformer: no xml document contained in stream.");
        }
        if (stylesheetStream == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet contained in stream.");
        }
        TransformerFactory tFactory = TransformerFactory.newInstance();

        Transformer transformer = tFactory.newTransformer(stylesheetStream);
        addParameters(transformer, parameters);
        transformer.transform(stream, outputStream);
    }

    public static void transform(FileInputStream stream, FileInputStream stylesheetStream, StreamResult outputStream) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        transform(stream, stylesheetStream, outputStream, EMPTY_HASH_MAP);
    }

    public static void transform(FileInputStream stream, FileInputStream stylesheetStream, StreamResult outputStream, HashMap parameters)
            throws TransformerException, TransformerConfigurationException, FileNotFoundException, IOException, Exception {
        if (stylesheetStream == null) {
            throw new Exception("SOSXMLTransformer: no stylesheet contained in stream");
        }
        StreamSource styleStream = new StreamSource(stylesheetStream);
        StreamSource inputStream = new StreamSource(stream);
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(styleStream);
        addParameters(transformer, parameters);
        transformer.transform(inputStream, outputStream);
    }

    private static void addParameters(Transformer transformer, HashMap parameters) {
        Iterator iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next().toString();
            transformer.setParameter(key, parameters.get(key).toString());
        }
    }

    public static void main(String args[]) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: SOSXMLTransformer xmlFile  xslFile  outputFile");
            return;
        }
        File xmlFile = new File(args[0]);
        if (!xmlFile.canRead()) {
            System.out.println("SOSXMLTransformer: xml input file not found: " + xmlFile.getAbsolutePath());
            return;
        }
        File xslFile = new File(args[1]);
        if (!xslFile.canRead()) {
            System.out.println("SOSXMLTransformer: xsl input file not found: " + xslFile.getAbsolutePath());
            return;
        }
        File outputFile = new File(args[2]);
        SOSXMLTransformer.transform(xmlFile, xslFile, outputFile);
    }

}