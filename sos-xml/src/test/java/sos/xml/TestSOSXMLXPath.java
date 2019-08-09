package sos.xml;

import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Ignore("This Test just stores the tests from the main method in sos.xml.SOSXMLXPath.java, Tests have to be reviewed to make them runnable")
public class TestSOSXMLXPath {

    private static final Logger LOGGER = Logger.getLogger(TestSOSXMLXPath.class);
    private static final String XML =
            "<?xml version='1.0' encoding='UTF-8'?><soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'>"
                    + "<soapenv:Header><wsa:To xmlns:wsa='http://schemas.xmlsoap.org/ws/2004/08/addressing'>"
                    + "http://localhost:4444/servingxml_xml2flat_service"
                    + "</wsa:To><wsa:ReplyTo xmlns:wsa='http://schemas.xmlsoap.org/ws/2004/08/addressing'><wsa:Address>"
                    + "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous</wsa:Address></wsa:ReplyTo></soapenv:Header><soapenv:Body>"
                    + "<addOrder xmlns='http://www.sos-berlin.com/scheduler'><title>ServingXML Order</title><param><name>service</name>"
                    + "<value>bookReviews</value></param><param><name>resources</name><value>"
                    + "http://localhost:4444/servingxml/resources/servingxml_xml2flat/bookReviews.xml"
                    + "</value></param><param><name>extension</name><value>.txt</value></param><xml_payload>"
                    + "<myns:books xmlns:myns='http://www.mydomain.com/MyNamespace'><myns:book id='002' categoryCode='F'>"
                    + "<myns:title>Kafka on the Shore</myns:title><myns:author>Haruki Murakami</myns:author><myns:price>25.17</myns:price>"
                    + "<myns:reviews><myns:review><myns:reviewer>Curley</myns:reviewer><myns:rating>*****</myns:rating></myns:review>"
                    + "<myns:review><myns:reviewer>Larry</myns:reviewer><myns:rating>***</myns:rating></myns:review><myns:review>"
                    + "<myns:reviewer>Moe</myns:reviewer><myns:rating>*</myns:rating></myns:review></myns:reviews></myns:book></myns:books>"
                    + "</xml_payload></addOrder></soapenv:Body></soapenv:Envelope>";

    @Test
    private void testXml() {
        try {
            SOSXMLXPath myxpath = new SOSXMLXPath(new StringBuffer(XML));
            NodeList nl = myxpath.selectNodeList(myxpath.getDocument().getElementsByTagName("*").item(0), "//test");
            String xstring = myxpath.selectSingleNodeValue("//param[name[text()='service']]/value");
            LOGGER.info("param:" + xstring + "\n\n");
            SOSXMLXPath xpath = new SOSXMLXPath(new StringBuffer("<spooler><answer><ERROR code=\"4711\" text=\"ein Fehler\"/></answer></spooler>"));
            String astring = xpath.selectSingleNodeValue("//ERROR/@code");
            String bstring = xpath.selectSingleNodeValue("//ERROR/@text");
            LOGGER.info("code: " + astring + " text: " + bstring);
            SOSXMLXPath xpathMo = new SOSXMLXPath("J:/E/java/mo/sos.xml/src/sos/xml/parser/spooler.xml");
            NodeList n2 =
                    xpathMo.selectNodeList(xpathMo.getDocument().getElementsByTagName("*").item(0),
                            "//spooler//answer//state//remote_schedulers//remote_scheduler");
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = n2.item(i);
                LOGGER.info("Tagname: " + n.getNodeName());
                org.w3c.dom.NamedNodeMap map = n.getAttributes();
                for (int j = 0; j < map.getLength(); j++) {
                    LOGGER.info(j + "'te attribut : " + map.item(j));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Test
    private void test2Xml() {
        try {
            SOSXMLXPath xpathXinclude = new SOSXMLXPath(new FileInputStream("C:/temp/a.xml"), true);
            NodeList nl = xpathXinclude.selectNodeList(xpathXinclude.getDocument().getElementsByTagName("*").item(0), "//test");
            LOGGER.info(nl.getLength());
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                LOGGER.info("Tagname: " + n.getNodeName());
                org.w3c.dom.NamedNodeMap map = n.getAttributes();
                for (int j = 0; j < map.getLength(); j++) {
                    LOGGER.info(j + "'te attribut : " + map.item(j));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}