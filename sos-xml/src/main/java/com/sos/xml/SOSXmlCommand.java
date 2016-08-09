package com.sos.xml;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sos.xml.SOSXMLXPath;

public class SOSXmlCommand {
    protected static final String JOBSCHEDULER_DATE_FORMAT = "yyyy-mm-dd hh:mm:ss.SSS'Z'";
    protected static final String JOBSCHEDULER_DATE_FORMAT2 = "yyyy-mm-dd'T'hh:mm:ss.SSS'Z'";
    private String protocol;
    private String host;
    private Long port;
    private String url;
    protected HashMap<String, String> attributes;
    private StringBuilder response;
    private SOSXMLXPath sosxml;

    public SOSXmlCommand(String protocol, String host, Long port) {
        super();
        if (protocol == null || "".equals(protocol)) {
            protocol = "http";
        }

        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public SOSXmlCommand(String url) {
        super();
        this.url = url;
    }

    public SOSXmlCommand(String host, int port) {
        super();
        this.host = host;
        this.port = new Long(port);
    }

    public String getAttribut(String key) {
        return attributes.get(key);
    }

    public Integer getAttributAsIntegerOr0(String key) {
        try {
            return Integer.parseInt(attributes.get(key));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public Date getAttributAsDate(String dateAttribute) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT);
        SimpleDateFormat formatter2 = new SimpleDateFormat(JOBSCHEDULER_DATE_FORMAT2);
        Date date;
        try{
            date = formatter.parse(getAttribut(dateAttribute));
        }catch (Exception e){
            date = formatter2.parse(getAttribut(dateAttribute));
        }
        return date;
    }

    public void executeXPath(String xPath) throws Exception {
        if (sosxml != null) {
            attributes = new HashMap<String, String>();
            Node n = sosxml.selectSingleNode(xPath);
            if (n != null) {
                NamedNodeMap map = n.getAttributes();
                for (int j = 0; j < map.getLength(); j++) {
                    attributes.put(map.item(j).getNodeName(), map.item(j).getNodeValue());
                }
            }
        }
    }

    public NodeList selectNodelist(String xPath) throws Exception {
        if (sosxml != null) {
            return sosxml.selectNodeList(xPath);
        } else {
            return null;
        }
    }

    public String excutePost(String urlParameters) throws Exception {

        HttpURLConnection connection = null;

        String targetURL;
        if (url != null && !"".equals(url)) {
            targetURL = url;
        } else {
            targetURL = String.format("%s://%s:%s", protocol, host, port);
            ;
        }

        try {
            // Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            response = new StringBuilder();

            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            sosxml = new SOSXMLXPath(new StringBuffer(response));

            return response.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
