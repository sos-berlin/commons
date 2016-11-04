package com.sos.xml;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sos.xml.SOSXMLXPath;
import sos.xml.exceptions.ConnectionRefusedException;
import sos.xml.exceptions.NoResponseException;

public class SOSXmlCommand {
    private static final String DEFAULT_PROTOCOL = "http";
    private String protocol;
    private String host;
    private Long port;
    private String url;
    protected Map<String, Map<String, String>> attributes;
    private StringBuilder response;
    private SOSXMLXPath sosxml;

    public SOSXmlCommand(String protocol, String host, Long port) {
        if (protocol == null || "".equals(protocol)) {
            protocol = DEFAULT_PROTOCOL;
        }

        attributes = new HashMap<String, Map<String, String>>();

        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public SOSXmlCommand(String url) {
        attributes = new HashMap<String, Map<String, String>>();
        this.url = url;
    }
    
    public SOSXmlCommand(String host, int port) {
        attributes = new HashMap<String, Map<String, String>>();
        this.host = host;
        this.port = new Long(port);
        this.protocol = DEFAULT_PROTOCOL;
    }
    
    protected String getUrl() {
        return url;
    }

    public String getAttribute(String key, String attribute) {
        return attributes.get(key).get(attribute);
    }

    public String getAttribute(String attribute) {
        return getAttribute("", attribute);
    }

    public String getAttributeWithDefault(String attribute, String defaultValue) {
        return getAttributeWithDefault("", attribute, defaultValue);
    }

    public String getAttributeWithDefault(String key, String attribute, String defaultValue) {
        String val = attributes.get(key).get(attribute);
        if (val == null || "".equals("val")) {
            return defaultValue;
        } else {
            return attributes.get(key).get(attribute);
        }
    }

    public Integer getAttributeAsIntegerOr0(String key, String attribute) {
        try {
            return Integer.parseInt(attributes.get(key).get(attribute));
        } catch (Exception e) {
            return 0;
        }
    }

    public Integer getAttributeAsInteger(String key, String attribute) {
        try {
            return Integer.parseInt(attributes.get(key).get(attribute));
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getAttributeAsIntegerOr0(String attribute) {
        return getAttributeAsIntegerOr0("", attribute);
    }

    public Integer getAttributeAsInteger(String attribute) {
        return getAttributeAsInteger("", attribute);
    }

    public Date getAttributeAsDate(String key, String dateAttribute) {
        Date date;
        try {
            String dateString = getAttribute(key, dateAttribute);
            if (dateString == null) {
                return null;
            }
            dateString = dateString.trim().replaceFirst("^(\\d{4}-\\d{2}-\\d{2}) ", "$1T");
            date = Date.from(Instant.parse(dateString));
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    public Date getAttributeAsDate(String dateAttribute) {
        return getAttributeAsDate("", dateAttribute);
    }

    public Element executeXPath(String key, String xPath) throws Exception {
        Element element = null;
        if (sosxml != null) {
            HashMap<String, String> attrs = new HashMap<String, String>();
            Node n = sosxml.selectSingleNode(xPath);
            if (n != null) {
                element = (Element) n;
                NamedNodeMap map = n.getAttributes();
                for (int j = 0; j < map.getLength(); j++) {
                    attrs.put(map.item(j).getNodeName(), map.item(j).getNodeValue());
                }
                attributes.put(key, attrs);
            }
        } else {
            attributes.put(key, new HashMap<String, String>());
        }
        return element;    
    }

    public Element executeXPath(String xPath) throws Exception {
        return executeXPath("", xPath);
    }

    public NodeList selectNodelist(String xPath) throws Exception {
        if (sosxml != null) {
            return sosxml.selectNodeList(xPath);
        } else {
            return null;
        }
    }
    
    public String executePost(String urlParameters) throws ConnectionRefusedException, NoResponseException {
        return executePost(urlParameters, UUID.randomUUID().toString());
    }

    public String executePost(String urlParameters, String csrfToken) throws ConnectionRefusedException, NoResponseException {

        HttpURLConnection connection = null;

        String targetURL;
        if (url != null && !"".equals(url)) {
            targetURL = url;
        } else {
            targetURL = String.format("%s://%s:%s", protocol, host, port);
        }

        try {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/xml");

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            // connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("X-CSRF-Token", getCsrfToken(csrfToken));

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();
        } catch (Exception e) {
            if (connection != null) {
                connection.disconnect();
            }
            throw new ConnectionRefusedException(targetURL, e);
        }

        // Get Response
        InputStream is = null;
        response = new StringBuilder();
        BufferedReader rd = null;
        try {
            is = connection.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            sosxml = new SOSXMLXPath(new StringBuffer(response));
            return response.toString();
        } catch (Exception e) {
            throw new NoResponseException(targetURL, e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
            }
            try {
                if (rd != null) {
                    rd.close();
                }
            } catch (Exception e) {
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    public SOSXMLXPath getSosxml() {
        return sosxml;
    }
    
    private String getCsrfToken(String csrfToken) {
        if (csrfToken == null || csrfToken.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        return csrfToken;
    }
}
