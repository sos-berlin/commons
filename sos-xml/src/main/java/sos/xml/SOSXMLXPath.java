package sos.xml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.CachedXPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sos.util.SOSClassUtil;

/** @author ap */
public class SOSXMLXPath extends CachedXPathAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SOSXMLXPath.class);
    private Document document = null;
    private Element root = null;
    private Node node = null;

    public SOSXMLXPath(StringBuffer xmlStr) throws Exception {
        super();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            docFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            docFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            docFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            docFactory.setXIncludeAware(false);
            docFactory.setExpandEntityReferences(false);
            docFactory.setNamespaceAware(false);
            docFactory.setValidating(false);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            this.document = docBuilder.parse(new InputSource(new StringReader(xmlStr.toString())));
            // this.document = docBuilder.parse(new ByteArrayInputStream(xmlStr.toString().getBytes()));
            this.root = this.document.getDocumentElement();
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            throw new SAXException("A DOCTYPE was passed into the XML document", e);
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
        }
    }

    public SOSXMLXPath(String filename) throws Exception {
        super();
        try {
            InputSource in = new InputSource(new FileInputStream(filename));

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            docFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            docFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            docFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            docFactory.setXIncludeAware(false);
            docFactory.setExpandEntityReferences(false);
            docFactory.setNamespaceAware(false);
            docFactory.setValidating(false);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            this.document = docBuilder.parse(in);
            this.root = this.document.getDocumentElement();
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            throw new SAXException("A DOCTYPE was passed into the XML document", e);
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
        }
    }

    public SOSXMLXPath(Path filename) throws Exception {
        super();
        try {
            InputSource in = new InputSource(new FileInputStream(filename.toFile()));
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            docFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            docFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            docFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            docFactory.setXIncludeAware(false);
            docFactory.setExpandEntityReferences(false);
            docFactory.setNamespaceAware(false);
            docFactory.setValidating(false);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            this.document = docBuilder.parse(in);
            this.root = this.document.getDocumentElement();
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            throw new SAXException("A DOCTYPE was passed into the XML document", e);
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
        }
    }

    public SOSXMLXPath(InputStream stream) throws Exception {
        super();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            docFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            docFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            docFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            docFactory.setXIncludeAware(false);
            docFactory.setExpandEntityReferences(false);
            docFactory.setNamespaceAware(false);
            docFactory.setValidating(false);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            docBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            this.document = docBuilder.parse(stream);
            this.root = this.document.getDocumentElement();
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            throw new SAXException("A DOCTYPE was passed into the XML document", e);
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
        }
    }

    public SOSXMLXPath(InputStream stream, boolean xInclude) throws Exception {
        super();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl", this.getClass()
                    .getClassLoader());
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setExpandEntityReferences(false);
            dbf.setNamespaceAware(true);
            dbf.setXIncludeAware(xInclude);
            javax.xml.parsers.DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            docBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
            this.document = docBuilder.parse(stream);
            this.root = document.getDocumentElement();
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            throw new SAXException("A DOCTYPE was passed into the XML document", e);
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            throw new IOException("IOException occurred, XXE may still possible: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }

    public Node selectSingleNode(String xpathExpression) throws Exception {
        return selectSingleNode(this.document.getDocumentElement(), xpathExpression);
    }

    public String selectSingleNodeValue(String xpathExpression) throws Exception {
        return selectSingleNodeValue(this.document.getDocumentElement(), xpathExpression);
    }
    
    public String selectSingleNodeValue(String xpathExpression, String default_) throws Exception {
        return selectSingleNodeValue(this.document.getDocumentElement(), xpathExpression, default_);
    }

    public String selectSingleNodeValue(Node context, String xpathExpression) throws Exception {
        Node node = this.selectSingleNode(context, xpathExpression);
        if (node != null) {
            return this.getNodeText(node);
        }
        return null;
    }
    
    public String selectSingleNodeValue(Node context, String xpathExpression, String default_) throws Exception {
        Node node = this.selectSingleNode(context, xpathExpression);
        if (node != null) {
            return this.getNodeText(node);
        }
        return default_;
    }
    
    public NodeList selectNodeList(String xpathExpression) throws Exception {
        return selectNodeList(this.document.getDocumentElement(), xpathExpression);
    }

    public String selectDocumentText(String xpathExpression) throws Exception {
        return selectDocumentText(this.document.getDocumentElement(), xpathExpression);
    }
    
    public String selectDocumentText(Node context, String xpathExpression) throws Exception {
        return selectDocumentText(context, xpathExpression, false);
    }

    public String selectDocumentText(Node context, String xpathExpression, boolean withXMLDeclaration) throws Exception {
        Node node = this.selectSingleNode(context, xpathExpression);
        if (node == null) {
            return "";
        }
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        document.appendChild(document.importNode(node, true));
        return selectDocumentText(document, withXMLDeclaration);
    }
    
    public String selectDocumentText(Document document, boolean withXMLDeclaration) throws Exception {
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, (withXMLDeclaration ? "yes" : "no"));
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            throw e;
        }
        finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (Exception e) {}
            }
        }
    }

    public Document getDocument() throws Exception {
        return this.document;
    }

    public Element getElement() throws Exception {
        return (Element) this.node;
    }

    public String getNodeText(Node node) {
        String strRet = "";
        if (null != node) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); ++i) {
                Node item = children.item(i);
                switch (item.getNodeType()) {
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    strRet += item.getNodeValue();
                    break;
                }
            }
        }
        return strRet;
    }

    public Element getRoot() {
        return root;
    }

}