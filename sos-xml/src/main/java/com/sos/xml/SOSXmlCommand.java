package com.sos.xml;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sos.exception.SOSConnectionRefusedException;
import com.sos.exception.SOSNoResponseException;

import sos.xml.SOSXMLXPath;

public class SOSXmlCommand {

    private static final String DEFAULT_PROTOCOL = "http";
    private String protocol;
    private String host;
    private Long port;
    private String url;
    protected Map<String, Map<String, String>> attributes;
    private SOSXMLXPath sosxml;
    private int connectTimeout = 0;
    private int readTimeout = 0;
    private boolean allowAllHostnameVerifier = true;
    private String basicAuthorization = null;
    
    public enum ResponseStream {
        TO_STRING,
        TO_SOSXML,
        TO_STRING_AND_SOSXML;
    }

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

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setAllowAllHostnameVerifier(boolean flag) {
        allowAllHostnameVerifier = flag;
    }

    public void setBasicAuthorization(String basicAuthorization) {
        this.basicAuthorization = basicAuthorization;
    }

    public String getBasicAuthorization() {
        return basicAuthorization;
    }

    protected void setUrl(String url) {
        this.url = url;
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
        if (val == null || "".equals(val)) {
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
            if (dateString == null || dateString.isEmpty()) {
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
            Map<String, String> attrs = new HashMap<String, String>();
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

    public String executeXMLPost(String urlParameters) throws SOSConnectionRefusedException, SOSNoResponseException {
        return executeXMLPost(urlParameters, UUID.randomUUID().toString());
    }

    public String executeXMLPost(String urlParameters, String csrfToken) throws SOSConnectionRefusedException, SOSNoResponseException {
        return responseToString(requestXMLPost(urlParameters, csrfToken));
    }
    
    public String executeXMLPost(String urlParameters, ResponseStream responseStream) throws SOSConnectionRefusedException, SOSNoResponseException {
        return executeXMLPost(urlParameters, responseStream, UUID.randomUUID().toString());
    }

    public String executeXMLPost(String urlParameters, ResponseStream responseStream, String csrfToken) throws SOSConnectionRefusedException, SOSNoResponseException {
        return responseToString(requestXMLPost(urlParameters, csrfToken), responseStream);
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

    public HttpURLConnection requestXMLPost(String urlParameters, String csrfToken) throws SOSConnectionRefusedException {

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
            if ("https".equals(url.getProtocol())) {
                if (allowAllHostnameVerifier) {
                    HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
                }
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            if (basicAuthorization != null && !basicAuthorization.isEmpty()) {
                connection.setRequestProperty("Authorization", "Basic " + basicAuthorization);
            }
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/xml");

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            // connection.setRequestProperty("Content-Language", "en-US");
            connection.setRequestProperty("X-CSRF-Token", getCsrfToken(csrfToken));

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();
            return connection;
        } catch (Exception e) {
            if (connection != null) {
                connection.disconnect();
            }
            throw new SOSConnectionRefusedException(e);
        }
    }
    
    private String responseToString(HttpURLConnection connection) throws SOSNoResponseException {
        return responseToString(connection, ResponseStream.TO_STRING_AND_SOSXML);
    }

    private String responseToString(HttpURLConnection connection, ResponseStream responseStream) throws SOSNoResponseException {
        if (connection != null) {
            InputStream is = null;
            StringBuffer response = new StringBuffer();
            Reader rd = null;
            try {
                is = connection.getInputStream();
                if (responseStream == ResponseStream.TO_SOSXML) {
                    sosxml = new SOSXMLXPath(is);
                    return null;
                }
                rd = new InputStreamReader(is);
                final char[] buffer = new char[4096];
                int length;
                while((length = rd.read(buffer)) != -1) {
                    response.append(buffer, 0, length);
                }

                if (responseStream == ResponseStream.TO_STRING_AND_SOSXML) {
                    sosxml = new SOSXMLXPath(response);
                }
                return response.toString();
            } catch (Exception e) {
                throw new SOSNoResponseException(e);
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
        } else {
            return "";
        }
    }
}
