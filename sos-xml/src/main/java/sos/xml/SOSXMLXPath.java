package sos.xml;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.CachedXPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import sos.util.SOSClassUtil;

/** @author ap */
public class SOSXMLXPath extends CachedXPathAPI {

    private static final Logger LOGGER = Logger.getLogger(SOSXMLXPath.class);
    private Document document = null;
    private Element root = null;
    private Node node = null;

    public SOSXMLXPath(StringBuffer xmlStr) throws Exception {
        super();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(false);
        docFactory.setValidating(false);
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        this.document = docBuilder.parse(new ByteArrayInputStream(xmlStr.toString().getBytes()));
        this.root = this.document.getDocumentElement();
    }

    public SOSXMLXPath(String filename) throws Exception {
        super();
        InputSource in = new InputSource(new FileInputStream(filename));
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(false);
        docFactory.setValidating(false);
        this.document = docFactory.newDocumentBuilder().parse(in);
        this.root = this.document.getDocumentElement();
    }

    public SOSXMLXPath(InputStream stream) throws Exception {
        super();
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(false);
        docFactory.setValidating(false);
        this.document = docFactory.newDocumentBuilder().parse(stream);
        this.root = this.document.getDocumentElement();
    }

    public SOSXMLXPath(InputStream stream, boolean xInclude) throws Exception {
        super();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance("org.apache.xerces.jaxp.DocumentBuilderFactoryImpl", this.getClass()
                    .getClassLoader());
            dbf.setNamespaceAware(true);
            dbf.setXIncludeAware(true);
            javax.xml.parsers.DocumentBuilder dom = dbf.newDocumentBuilder();
            this.document = dom.parse(stream);
            this.root = document.getDocumentElement();
        } catch (Exception e) {
            LOGGER.error("", e);
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }

    public Node selectSingleNode(String xpathExpression) throws Exception {
        return selectSingleNode(this.document.getDocumentElement(), xpathExpression);
    }

    public String selectSingleNodeValue(String xpathExpression) throws Exception {
        return selectSingleNodeValue(this.document.getDocumentElement(), xpathExpression);
    }

    public String selectSingleNodeValue(Element element, String xpathExpression) throws Exception {
        Node node = this.selectSingleNode(element, xpathExpression);
        if (node != null) {
            return this.getNodeText(node);
        }
        return null;
    }

    public NodeList selectNodeList(String xpathExpression) throws Exception {
        return selectNodeList(this.document.getDocumentElement(), xpathExpression);
    }

    public String selectDocumentText(String xpathExpression) throws Exception {
        return selectDocumentText(this.document.getDocumentElement(), xpathExpression);
    }

    public String selectDocumentText(Element element, String xpathExpression) throws Exception {
        Node node = this.selectSingleNode(element, xpathExpression);
        if (node == null) {
            return "";
        }
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();
        document.appendChild(document.importNode(node, true));
        StringWriter out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, new OutputFormat(document));
        serializer.serialize(document);
        return out.toString();
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