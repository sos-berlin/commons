package sos.xml;

import java.io.ByteArrayInputStream;
import sos.util.SOSClassUtil;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// xalan.jar
import org.apache.xpath.CachedXPathAPI;
//import org.apache.xml.serialize.XMLSerializer;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
//import org.apache.xml.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderImpl;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;;

/**
 * @author ap
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SOSXMLXPath extends CachedXPathAPI {

	public Document document = null;
	public Element root = null;
	public Node node = null;

	public static void main(String[] args) {
		
		try { 
			// SOSXMLXPath xpath = new SOSXMLXPath("c:/factory/custom/ecb/ecb_order_transformed.xml");
			// String astring = xpath.selectSingleNodeValue("//Model");
			// System.out.println("model: "+ astring);

			SOSXMLXPath xpath_Xinclude = new SOSXMLXPath(new FileInputStream("C:/temp/a.xml"), true);
			NodeList nl = xpath_Xinclude.selectNodeList(xpath_Xinclude.document.getElementsByTagName("*").item(0), "//test");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				System.out.println("Tagname: " + n.getNodeName());
				org.w3c.dom.NamedNodeMap map = n.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					System.out.println(j  + "'te attribut : " + map.item(j));
				}				
			}
			
			if(true) return;
			
            String xmlStr = "<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope " +
            		"xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>    " +
            		"<soapenv:Header>        " +
            		"<wsa:To xmlns:wsa='http://schemas.xmlsoap.org/ws/2004/08/addressing'>" +
            		"http://localhost:4444/servingxml_xml2flat_service</wsa:To>        " +
            		"<wsa:ReplyTo xmlns:wsa='http://schemas.xmlsoap.org/ws/2004/08/addressing'>            " +
            		"<wsa:Address>http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:Address>        </wsa:ReplyTo>    </soapenv:Header>    <soapenv:Body>       " +
            		" <addOrder xmlns='http://www.sos-berlin.com/scheduler'>            " +
            		" <title>ServingXML Order</title>            " +
            		" <param>                " +
            		" <name>service</name>" +
            		" <value>bookReviews</value>            " +
            		"</param>            " +
            		"<param>                " +
            		"<name>resources</name>                " +
            		"<value>http://localhost:4444/servingxml/resources/servingxml_xml2flat/bookReviews.xml</value>            " +
            		"</param>            " +
            		"<param>                " +
            		"<name>extension</name>                " +
            		"<value>.txt</value>            " +
            		"</param>            " +
            		"<xml_payload>               " +
            		" <myns:books xmlns:myns='http://www.mydomain.com/MyNamespace'>                 " +
            		"<myns:book id='002' categoryCode='F'>                    " +
            		"<myns:title>Kafka on the Shore</myns:title>                   " +
            		" <myns:author>Haruki Murakami</myns:author>                    " +
            		"<myns:price>25.17</myns:price>                  " +
            		"  <myns:reviews>                      <myns:review>        " +
            		"                <myns:reviewer>Curley</myns:reviewer>               " +
            		"         <myns:rating>*****</myns:rating>                    " +
            		"  </myns:review>                      <myns:review>      " +
            		"                  <myns:reviewer>Larry</myns:reviewer>             " +
            		"           <myns:rating>***</myns:rating>                      </myns:review>  " +
            		"                    <myns:review>                      " +
            		"  <myns:reviewer>Moe</myns:reviewer>                     " +
            		"   <myns:rating>*</myns:rating>                 " +
            		"     </myns:review>                  " +
            		"  </myns:reviews>             " +
            		"   </myns:book>              " +
            		"  </myns:books>         " +
            		"   </xml_payload>       " +
            		" </addOrder>   " +
            		" </soapenv:Body></soapenv:Envelope>";
            SOSXMLXPath myxpath = new SOSXMLXPath(new StringBuffer(xmlStr));
            
            String xstring = myxpath.selectSingleNodeValue("//param[name[text()='service']]/value");
            System.out.println("param:" + xstring + "\n\n");
            
            // Node xNode = myxpath.selectSingleNode("//param/name[text()='service']");
            Node xNode = myxpath.selectSingleNode("//xml_payload");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();                
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();                
            Document xDoc = docBuilder.newDocument();
            // xDoc.importNode(xNode, true);
            // Element xElem = xDoc.createElement("test");
            // xElem.appendChild(xDoc.importNode(xNode, true));
            xDoc.appendChild(xDoc.importNode(xNode, true));
            
            StringWriter out = new StringWriter(); 
            XMLSerializer serializer = new XMLSerializer(out, new OutputFormat(xDoc)); 
            serializer.serialize(xDoc); 
            String result = out.toString(); 
            
            
		    SOSXMLXPath xpath = new SOSXMLXPath(new StringBuffer("<spooler><answer><ERROR code=\"4711\" text=\"ein Fehler\"/></answer></spooler>"));
			String astring = xpath.selectSingleNodeValue("//ERROR/@code");
			String bstring = xpath.selectSingleNodeValue("//ERROR/@text");
			System.out.println("code: "+ astring + " text: " + bstring);
			
			//mo: liefert alle Attribute eines paths
			SOSXMLXPath xpath_mo = new SOSXMLXPath("J:/E/java/mo/sos.xml/src/sos/xml/parser/spooler.xml");						
			//NodeList nl = xpath.selectNodeList(xpath.document.getElementsByTagName("spooler").item(0), "//answer//state//remote_schedulers//remote_scheduler");
			NodeList n2 = xpath_mo.selectNodeList(xpath_mo.document.getElementsByTagName("*").item(0), "//spooler//answer//state//remote_schedulers//remote_scheduler");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = n2.item(i);
				System.out.println("Tagname: " + n.getNodeName());
				org.w3c.dom.NamedNodeMap map = n.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					System.out.println(j  + "'te attribut : " + map.item(j));
				}				
			}
			
			
			
		} catch (Exception e) {
          System.out.println(e.getMessage());      
        }
	}
	
	
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
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		this.document = docFactory.newDocumentBuilder().parse(in);
		this.root = this.document.getDocumentElement();
	}

	/**
	 * re
	 * @param stream
	 * @throws Exception
	 */
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

			
//			javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl",this.getClass().getClassLoader());
			
			
			dbf.setNamespaceAware(true);
			dbf.setXIncludeAware(true);
			
		
			javax.xml.parsers.DocumentBuilder dom = dbf.newDocumentBuilder();

			this.document = dom.parse(stream);
			this.root = document.getDocumentElement();
			
			/*System.out.println(root);
			for(int i = 0; i < root.getChildNodes().getLength(); i++)
				System.out.println(root.getChildNodes().item(i).getNodeName());
			*/
			
			
		
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

	public String selectSingleNodeValue(Element element, String xpathExpression) throws Exception {
		
		Node node = this.selectSingleNode(element, xpathExpression);
		if (node != null) return this.getNodeText(node);
 
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
        if (node == null) return "";

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();                
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();                
        Document document = docBuilder.newDocument();
        document.appendChild(document.importNode(node, true));
        
        StringWriter out = new StringWriter(); 
        XMLSerializer serializer = new XMLSerializer(out, new OutputFormat(document)); 
        serializer.serialize(document); 
        return out.toString(); 
    }
    
    
    
    /**
     * erzeugt ein XML-Dokument
     * @param nodeName Name des Startknotens
     */
/*	
    private Document createDocument() 
    	throws Exception {
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            this.document = docBuilder.newDocument();

            return this.document;
            
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }
    */
    /**
     * erzeugt ein XML-Dokument
     * @param nodeName Name des Startknotens
     */
/*	
    private Document createDocument(String nodeName) 
    	throws Exception {
        
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setNamespaceAware(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            this.document = docBuilder.newDocument();

			this.node = this.document.createElement(nodeName);
            
            return this.document;
            
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }
*/    
    /**
     * schließt ein XML-Dokument
     */
/*	
    private Document closeDocument() 
    	throws Exception {

        try {
            this.document.appendChild(this.node);
            return this.document;
            
        } catch (Exception e) {
            throw new Exception(SOSClassUtil.getMethodName() + ": " + e.toString());
        }
    }
*/    
    /**
     * liefert das XML-Dokument
     */
    public Document getDocument()
    	throws Exception {
        
         return this.document;
    }
    
    /**
     * liefert das XML-Dokument
     */
	
    public Element getElement()
    	throws Exception {

    	return (Element)this.node;
    }

    
    public String getNodeText(Node node)
    {
    	String strRet = "";
    		
    	if (null != node)
    	{
    		NodeList children = node.getChildNodes();
    		for (int i = 0; i < children.getLength(); ++i)
    		{
    			Node item = children.item(i);
    			switch (item.getNodeType())
    			{
    			case Node.TEXT_NODE:
    			case Node.CDATA_SECTION_NODE:
    				strRet += item.getNodeValue();
    			}
    		}
    	}
    		
    	return strRet;
    }

}